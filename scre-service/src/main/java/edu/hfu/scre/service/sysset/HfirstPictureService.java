package edu.hfu.scre.service.sysset;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.hfu.scre.dao.sysset.HfirstPictureDao;
import edu.hfu.scre.entity.SysHfirstPicture;
import edu.hfu.scre.entity.SysNotice;
import edu.hfu.scre.util.FileUpload;




@Service
@Transactional
public class HfirstPictureService {
	@Resource
	HfirstPictureDao hfirstPictureDao;
	
	public String updHfirstPicture(MultipartFile[] file,String basePath,SysHfirstPicture shp) throws Exception {
		if (null==shp.getHfirstPictureId()) {
			shp=hfirstPictureDao.addHfirstPicture(shp);
		}
		FileUpload up=new FileUpload();
		String filePath=up.saveAdPic(file, basePath, shp.getHfirstPictureId());
		
		shp.setHfirstPicturePath(filePath);
		hfirstPictureDao.updHfirstPicture(shp);
		return filePath;
	}
	
	public SysHfirstPicture addNullHfirstPicture() throws Exception{
		SysHfirstPicture shp=new SysHfirstPicture();
		hfirstPictureDao.addHfirstPicture(shp);
		return shp;
	}
	public List<SysHfirstPicture> findfirstPictureById()throws Exception{
		return hfirstPictureDao.findfirstPictureById();
	}
}
