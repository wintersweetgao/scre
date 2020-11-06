package edu.hfu.scre.dao.sysset;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


import edu.hfu.scre.dao.base.BaseDaoImpl;
import edu.hfu.scre.entity.SysNotice;

@Repository
public class NoticeDao {
	private static final Logger LOG = LoggerFactory.getLogger(NoticeDao.class);
	@Resource
	private BaseDaoImpl dao;
	//增加
	public void saveNotice(SysNotice nte) throws Exception {
		dao.save(nte);
	}
	//修改
	public void updNotice(SysNotice nte) throws Exception {
		dao.update(nte);
	}
	//删除
	public boolean delNotice(Integer NoticeId) throws Exception {
		String hql="delete SysNotice where NoticeId=?0";
		int res = dao.bulkUpdate(hql, NoticeId);
		if (res == 1) {
			return true;
		} else {
			return false;
		}
	}
	//查询
	public List<SysNotice> findNoticeByPage(SysNotice nte,int pageNo, int maxResults)throws Exception{
		String hql="from SysNotice ";
		List<Object> params=new ArrayList<Object>();
		int  index=0;
		if (nte!=null) {
			if(null!=nte.getNoticeTitle()&&!"".equals(nte.getNoticeTitle())) {
				hql+= (index==0?"where":"and")+"  noticeTitle like ?"+(index++);
				params.add('%'+nte.getNoticeTitle()+'%');
			}
			if(null!=nte.getNoticeId()&&!"".equals(nte.getNoticeId())) {
				hql+= (index==0?"where":"and")+"  noticeId like ?"+(index++);
				params.add('%'+nte.getNoticeId()+'%');
			}
		}
		hql+=" order by createTime desc";
		System.out.println("hql:"+hql);
		return dao.findByPage(hql,params.toArray(), pageNo, maxResults);
		
	}
	public Integer findNoticeCountOther(SysNotice nte) throws Exception {
		String hql="from SysNotice o ";
		List<Object> params=new ArrayList<>();
		int  index=0;
		if (nte!=null) {
			if (null!=nte.getNoticeName()&&!nte.getNoticeName().equals("")) {
				hql+= (index==0?"where":"and")+ " o.NoticeName=?"+(index++);
				params.add(nte.getNoticeName());
			}
			if (null!=nte.getNoticeTitle()&&!nte.getNoticeTitle().equals("")) {
				hql+=(index==0?"where":"and")+" o.NoticeTitle()=?"+(index++);
				params.add(nte.getNoticeTitle());
			}
		}
		hql+=" order by createTime desc";
		int count=dao.queryBeanCountByHql(hql, params.toArray());
		return count;
		
	}
	public List<SysNotice> findNoticeById(SysNotice nte)throws Exception{
		String hql="from SysNotice ";
		List<Object> params=new ArrayList<Object>();
		int  index=0;
		if (nte!=null) {
			if(null!=nte.getNoticeId()&&!"".equals(nte.getNoticeId())) {
				hql+= (index==0?"where":"and")+"  noticeId=?"+(index++);
				params.add(nte.getNoticeId());
			}
		}
		hql+=" order by createTime desc";
		System.out.println("hql:"+hql);
		return dao.find(hql,params.toArray());
		
	}
}
