package edu.hfu.scre.service.sysset;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import edu.hfu.scre.util.CacheData;
import edu.hfu.scre.util.XmlBuilder;
@Component
public class InitApplicationRunner implements ApplicationRunner{
	@javax.annotation.Resource
	InitSysService initSysService;
	@Override
	public void run(ApplicationArguments appArgs) throws Exception {
		//初始化数据字典
		initConstant();
		//初始化视图
		initView();
	}

	private void initConstant() throws Exception{
		//读取Resource目录下的XML文件
		Resource resource = new ClassPathResource("constant.xml");
		// 创建xml解析器
		SAXReader saxReader = new SAXReader();
		// 加载文件,读取到document中
		Document document = saxReader.read(new InputStreamReader(resource.getInputStream(), "UTF-8"));
		Element rootEle =document.getRootElement();
		for(Element  childEle:rootEle.elements()) {
			Attribute attr=childEle.attribute("listClass");
			String listClass=attr.getStringValue();
			Class<?> clazz=Class.forName(listClass);
			//XML转为JAVA对象
			Object data =  XmlBuilder.xmlStrToObject(clazz, childEle.asXML());  
			CacheData.seCacheData(data);
		}
	}
	
	private void initView() throws Exception{
		List<Map<String,String>> ls_view=new ArrayList<>();
		//获取视图
		//读取Resource目录下的XML文件
		Resource resource = new ClassPathResource("initView.xml");
		// 创建xml解析器
		SAXReader saxReader = new SAXReader();
		// 加载文件,读取到document中
		Document document = saxReader.read(new InputStreamReader(resource.getInputStream(), "UTF-8"));
		Element rootEle =document.getRootElement();
		for(Element  childEle:rootEle.elements()) {
			String attr=childEle.attribute("need-update").getStringValue();
			if ("true".equals(attr)) {
				Map<String,String> map=new HashMap<String,String>();
				map.put("viewname", childEle.attribute("viewname").getStringValue().trim());
				map.put("desc", childEle.attribute("desc").getStringValue().trim());
				map.put("type", childEle.attribute("type").getStringValue().trim());
				map.put("viewContent", rootEle.getStringValue().trim());
				ls_view.add(map);
				childEle.addAttribute("need-update", "false");
			}
		}
		
//		OutputFormat format = OutputFormat.createPrettyPrint();
//		format.setEncoding("UTF-8");
//		try {
//			XMLWriter writer = new XMLWriter(new FileWriter(resource.getFile()),format);
//			//写入数据
//			writer.write(document);
//			writer.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		initSysService.initView(ls_view);

	}
}
