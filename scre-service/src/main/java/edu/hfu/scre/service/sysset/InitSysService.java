package edu.hfu.scre.service.sysset;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import edu.hfu.scre.dao.sysset.InitSysDao;

@Service
@Transactional
public class InitSysService {

	@Resource
	private InitSysDao initSysDao;
	
	public void initView(List<Map<String,String>> ls_view) throws Exception{
		initSysDao.initView(ls_view);
	}
}
