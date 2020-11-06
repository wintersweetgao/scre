package edu.hfu.scre.service.security;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import edu.hfu.scre.dao.security.UserGrantDao;
import edu.hfu.scre.entity.SysPost;
import edu.hfu.scre.util.CacheData;

@Service
public class PostService {
	@Resource
	UserGrantDao userGrantDao;
	public List<SysPost> getAllSysPost(){
		 List<SysPost> ls=userGrantDao.getAllSysPost(CacheData.getAccessCode());
		return ls;
	}
}
