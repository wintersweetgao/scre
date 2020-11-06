package edu.hfu.scre.service.sysset;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import edu.hfu.scre.dao.sysset.NoticeDao;
import edu.hfu.scre.entity.SysNotice;
@Service
@Transactional
public class NoticeService {
	@Resource
	NoticeDao noticeDao;
	public void saveNotice(SysNotice nte) throws Exception {
		nte.setCreateTime(new Date());
		noticeDao.saveNotice(nte);
		
	}
	public void updNotice(SysNotice nte) throws Exception {
		nte.setCreateTime(new Date());
		noticeDao.updNotice(nte);
	}
	public boolean delNotice(Integer NoticeId) throws Exception {
		return noticeDao.delNotice(NoticeId);
	}
	public List<SysNotice> findNoticeByPage(SysNotice nte,int pageNo, int maxResults)throws Exception{
		return noticeDao.findNoticeByPage(nte, pageNo, maxResults);
		
	}
	public Integer findNoticeCountOther(SysNotice nte) throws Exception {
		return noticeDao.findNoticeCountOther(nte);
	}
	public List<SysNotice> findNoticeById(SysNotice nte)throws Exception{
	
		return noticeDao.findNoticeById(nte);
		
	}
}
