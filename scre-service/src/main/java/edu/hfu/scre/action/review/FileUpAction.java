package edu.hfu.scre.action.review;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Joiner;

import edu.hfu.scre.entity.RptPaperMaterial;
import edu.hfu.scre.entity.SysStaff;
import edu.hfu.scre.service.review.MaterialService;
import edu.hfu.scre.service.security.MyUserDetails;
import edu.hfu.scre.util.Constant;
import edu.hfu.scre.util.FileProcess;
import edu.hfu.scre.util.FileUpload;


@RestController
@RequestMapping(path = "/review")
public class FileUpAction {
	private static final Logger LOG = LoggerFactory.getLogger(FileUpAction.class);
	@Resource
	MaterialService materialService;
	
	
	@RequestMapping(value="/initUploadMaterial", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/initUploadMaterial','000002,000101,000102,000104,000005,000003')")
	public ModelAndView initUploadMaterial(){
		ModelAndView mod= new ModelAndView("/review/uploadMaterial.btl");
		return mod;
	}
	
	@RequestMapping(path = "/materialFileUp",method = RequestMethod.POST)
	@PreAuthorize("hasPermission('/materialFileUp','000002004,000001004,000004004,000000004,000005004,000003004')")
	public Map<String,Object> materialFileUp(MultipartFile[] materialfile,String materialType,
			String screNameTitle,Integer[] ordernum,String[] materialTitle
			,Integer screId) {
		LOG.debug("材料类型："+materialType+","+screNameTitle);
		Map<String,Object> mp=new HashMap<String, Object>();
		String mess="";
		if (null==materialfile||(materialfile.length==0)) {
			 mess= "上传失败，请选择文件";
        }else {
        	try {
    			File path = new File(ResourceUtils.getURL("classpath:").getPath());
    			FileUpload up=new FileUpload();
    			//返回标题与文件的对应关系
    			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    			Object principal=auth.getPrincipal();
    			String userCode="";
    			if (principal != null && principal instanceof MyUserDetails) {
    				MyUserDetails usd = (MyUserDetails) principal;
    				SysStaff user=(SysStaff)usd.getCustomObj();
    				userCode=user.getUserCode();
    			}
    			
    			Map<String,String> res=	up.saveFile(materialfile,path.getAbsolutePath(),userCode
    					, materialType,materialTitle,screNameTitle);
    			
    			List<RptPaperMaterial> materials=materialService.saveMaterialByType(res, materialType, materialTitle, ordernum, screId);
    			mess= "succ";
    			mp.put("mess", mess);
    			mp.put("materials", materials);
    		} catch (Exception e) {
    			e.printStackTrace();
    			mess= e.toString().replace("java.lang.RuntimeException:", "");
    			mp.put("mess", mess);
    		}
        }
		return mp;
	}
	 @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	 public Map<String, Object> uploadFile(HttpServletRequest request) {
		 Map<String, Object> resultMap = new HashMap<>();
		 try {
			 MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
			 Map<String, MultipartFile> fileMap = mRequest.getFileMap();
			 //request.getParameter("userCode")这个值是得不到的，上传按钮是工具自带的，传递的
			 FileProcess  fileProcess= new FileProcess();
			 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			 Object principal=auth.getPrincipal();
			 String userCode="";
			 if (principal != null && principal instanceof MyUserDetails) {
				 MyUserDetails usd = (MyUserDetails) principal;
				 SysStaff user=(SysStaff)usd.getCustomObj();
				 userCode=user.getUserCode();
			 }
			 for (Iterator<Map.Entry<String, MultipartFile>> it = fileMap.entrySet().iterator(); it.hasNext(); ) {
				 Map.Entry<String, MultipartFile> entry = it.next();
				 MultipartFile mFile = entry.getValue();
				 //userCode 先写死，后续真正用时从session中获取
				 String pathUrl = fileProcess.saveFile(mFile, userCode);
				 
				 if(pathUrl != null){
					 resultMap.put("error", 0);
	                    //前台返回路径要加ContextPath，不然图片显示不出来，且用/连接
					 resultMap.put("url", Joiner.on(Constant.BACK_SLASH).join(request.getContextPath(), pathUrl.replace(File.separatorChar, Constant.BACK_SLASH)));
				 }
			 }
		 } catch (Exception e) {
			 resultMap.put("error", 1);
			 resultMap.put("message", e.getMessage());
	            LOG.error("上传文件出错：", e);
		 }
		 return resultMap ;
	 }
}
