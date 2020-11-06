package edu.hfu.scre.dao.review;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import edu.hfu.scre.dao.base.BaseDaoImpl;

@Repository
public class PatentDao {
	private static final Logger LOG = LoggerFactory.getLogger(PatentDao.class);
	@Resource
	private BaseDaoImpl dao;
	
}
