package edu.hfu.scre.config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.beetl.ext.spring.BeetlSpringViewResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.hanb.filterJson.spring.PowerFastJsonHttpMessageConverter;

import edu.hfu.scre.util.StringToDateConverter;


@Configuration
public class WebConfig extends WebMvcConfigurationSupport {
	
	@Autowired
    private RequestMappingHandlerAdapter handlerAdapter;
	
	@Override
	protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		/*
		        先把JackSon的消息转换器删除.
		        备注: (1)源码分析可知，返回json的过程为:
		    		Controller调用结束后返回一个数据对象，for循环遍历conventers，
		    		找到支持application/json的HttpMessageConverter，然后将返回的数据序列化成json。
		                             具体参考org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor
		                             的writeWithMessageConverters方法
		       (2)由于是list结构，我们添加的fastjson在最后。因此必须要将jackson的转换器删除，不然会先匹配上jackson，导致没使用fastjson
		*/
		for (int i = converters.size() - 1; i >= 0; i--) {
			if (converters.get(i) instanceof MappingJackson2HttpMessageConverter) {
				converters.remove(i);
			}
		}
//		PowerJacksonHttpMessageConverter jacksonHttpMessageConverter=new PowerJacksonHttpMessageConverter();
//		List<MediaType> list = new ArrayList<MediaType>();    	
//		list.add(MediaType.APPLICATION_JSON_UTF8);    	
//		jacksonHttpMessageConverter.setSupportedMediaTypes(list);
//		converters.add(jacksonHttpMessageConverter);
		
		PowerFastJsonHttpMessageConverter quickJsonHttpMessageConverter = new PowerFastJsonHttpMessageConverter();
		//自定义fastjson配置
		FastJsonConfig config = new FastJsonConfig();
		config.setSerializerFeatures(
		               SerializerFeature.WriteMapNullValue,        // 是否输出值为null的字段,默认为false,我们将它打开
		               SerializerFeature.WriteNullListAsEmpty,     // 将Collection类型字段的字段空值输出为[]
		               SerializerFeature.WriteNullStringAsEmpty,   // 将字符串类型字段的空值输出为空字符串
		               SerializerFeature.WriteNullNumberAsZero,    // 将数值类型字段的空值输出为0
		               SerializerFeature.WriteDateUseDateFormat,
		               SerializerFeature.DisableCircularReferenceDetect    // 禁用循环检测
				);
		quickJsonHttpMessageConverter.setFastJsonConfig(config);
		// 添加支持的MediaTypes;不添加时默认为*/*,也就是默认支持全部
		// 但是MappingJackson2HttpMessageConverter里面支持的MediaTypes为application/json
		// 参考它的做法, fastjson也只添加application/json的MediaType
		List<MediaType> fastMediaTypes = new ArrayList<>();
		fastMediaTypes.add(MediaType.APPLICATION_JSON);
		fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
		quickJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
		converters.add(quickJsonHttpMessageConverter);
		super.configureMessageConverters(converters);
	}
	
	@Override 
	public void addResourceHandlers(ResourceHandlerRegistry registry){
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");//
	}
	@Value("${beetl.templatesPath}") String templatesPath;//模板根目录 ，比如 "templates"
	@Bean(name = "beetlConfig")
	public BeetlGroupUtilConfiguration getBeetlGroupUtilConfiguration() {
		BeetlGroupUtilConfiguration beetlGroupUtilConfiguration = new BeetlGroupUtilConfiguration();
	    //获取Spring Boot 的ClassLoader
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if(loader==null){
			loader = WebConfig.class.getClassLoader();
		}
		Properties extProperties=new Properties();
		try {
			InputStream in = getClass().getResourceAsStream("/extBeetlCfg.properties");
			extProperties.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//#beetle 自定义函数
		//FN.hasPermission = edu.hfu.scre.beetl.fn.HasPermission
		//extProperties.put("FN.hasPermission", "edu.hfu.scre.beetl.fn.HasPermission");
		beetlGroupUtilConfiguration.setConfigProperties(extProperties);//额外的配置，可以覆盖默认配置，一般不需要
		ClasspathResourceLoader cploder = new ClasspathResourceLoader(loader,templatesPath);
		beetlGroupUtilConfiguration.setResourceLoader(cploder);
		beetlGroupUtilConfiguration.init();
		//如果使用了优化编译器，涉及到字节码操作，需要添加ClassLoader
		beetlGroupUtilConfiguration.getGroupTemplate().setClassLoader(loader);
		return beetlGroupUtilConfiguration;
	}

	@Bean(name = "beetlViewResolver")
	public BeetlSpringViewResolver getBeetlSpringViewResolver(@Qualifier("beetlConfig") BeetlGroupUtilConfiguration beetlGroupUtilConfiguration) {
		BeetlSpringViewResolver beetlSpringViewResolver = new BeetlSpringViewResolver();
		//beetlSpringViewResolver.setPrefix("/templates");
//      	   beetlSpringViewResolver.setSuffix(".btl");
		beetlSpringViewResolver.setContentType("text/html;charset=UTF-8");
		beetlSpringViewResolver.setOrder(0);
		beetlSpringViewResolver.setConfig(beetlGroupUtilConfiguration);
		return beetlSpringViewResolver;
	}
	
    /**
     * 增加字符串转日期的功能
     */

    @PostConstruct
    public void initEditableAvlidation() {

        ConfigurableWebBindingInitializer initializer = (ConfigurableWebBindingInitializer)handlerAdapter.getWebBindingInitializer();
        if(initializer.getConversionService()!=null) {
            GenericConversionService genericConversionService = (GenericConversionService)initializer.getConversionService();           

            genericConversionService.addConverter(new StringToDateConverter());

        }

    }
}
