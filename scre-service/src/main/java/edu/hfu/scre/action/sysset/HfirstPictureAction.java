package edu.hfu.scre.action.sysset;
import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


import edu.hfu.scre.entity.SysHfirstPicture;
import edu.hfu.scre.service.sysset.HfirstPictureService;




/**
 * 首页滚动图
 * */
@RestController
@RequestMapping(path = "/sysset")
public class HfirstPictureAction {
	@Resource
	HfirstPictureService hfirstPictureService;
	 
	@RequestMapping(path = "/initHfirstPicture", method = RequestMethod.GET)
	public ModelAndView HfirstPicture() {
		ModelAndView mod = new ModelAndView("/sysset/hfirstPicture.btl");
		try {
			List<SysHfirstPicture> lsp = hfirstPictureService.findfirstPictureById();
			int lsSize=lsp.size();
			if (lsSize<6) {
				for (int i=0;i<6-lsSize;i++) {
					lsp.add(hfirstPictureService.addNullHfirstPicture());
				}
			}
			mod.addObject("lsp", lsp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mod;
	}
	@RequestMapping(path = "/hfirstPictureFileUp",method = RequestMethod.POST)
	public String hfirstPictureFileUp(SysHfirstPicture shp,MultipartFile[] materialfile){
        String mess="";
        try {
        	if (null==materialfile||(materialfile.length==0)) {
   			 mess= "上传失败，请选择文件";
           }else {
        	   File path = new File(ResourceUtils.getURL("classpath:").getPath());
        	   String filePath=hfirstPictureService.updHfirstPicture(materialfile,path.getAbsolutePath(),shp);
        	   mess="succ:"+filePath;
           }
        	
        } catch (Exception e) {
        	mess=e.toString().replace("java.lang.RuntimeException:", "");
        	e.printStackTrace();
        }
        return mess ;
    }
}