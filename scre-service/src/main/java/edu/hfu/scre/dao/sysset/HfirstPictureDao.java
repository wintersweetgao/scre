package edu.hfu.scre.dao.sysset;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import edu.hfu.scre.dao.base.BaseDaoImpl;
import edu.hfu.scre.entity.SysHfirstPicture;
import edu.hfu.scre.entity.SysNotice;


@Repository
public class HfirstPictureDao {
	@Resource
	private BaseDaoImpl dao;
	public void updHfirstPicture(SysHfirstPicture shp) throws Exception {
		dao.update(shp);
	}
	
	public SysHfirstPicture addHfirstPicture(SysHfirstPicture shp) throws Exception {
		 dao.save(shp);
		 return shp;
	}
	public List<SysHfirstPicture> findfirstPictureById()throws Exception{
		String hql="from SysHfirstPicture order by hfirstPictureId";
		return dao.find(hql);
		
	}
}
