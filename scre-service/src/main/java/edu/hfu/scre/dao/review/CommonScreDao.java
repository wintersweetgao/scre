package edu.hfu.scre.dao.review;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import edu.hfu.scre.dao.base.BaseDaoImpl;
import edu.hfu.scre.entity.RptAcademic;
import edu.hfu.scre.entity.RptAcademicMaterial;
import edu.hfu.scre.entity.RptAchievement;
import edu.hfu.scre.entity.RptAchievementMaterial;
import edu.hfu.scre.entity.RptBook;
import edu.hfu.scre.entity.RptBookMaterial;
import edu.hfu.scre.entity.RptPaper;
import edu.hfu.scre.entity.RptPaperMaterial;
import edu.hfu.scre.entity.RptPatent;
import edu.hfu.scre.entity.RptPatentMaterial;
import edu.hfu.scre.entity.RptScientific;
import edu.hfu.scre.entity.RptScientificMaterial;
import edu.hfu.scre.entity.SysNotice;
import edu.hfu.scre.entity.ViewscreAchieve;
@Repository
public class CommonScreDao{
	@Resource
	private BaseDaoImpl dao;
	public <T> int  delScre(T t,String priKey) throws Exception{
		Field field=t.getClass().getDeclaredField(priKey);
		field.setAccessible(true);
		Object value=field.get(t);
		int IdValue=Integer.parseInt(String.valueOf(value));
		//获取要删除的对象
		Object obj= dao.get(t.getClass(), IdValue);
		//删除对应的文件
		if (obj instanceof RptBook) {
			RptBook book=(RptBook)obj;
			List<RptBookMaterial> ls=book.getMaterials();
			if (null!=ls&&ls.size()>0) {
				//成功之后  删除文件
				File path = new File(ResourceUtils.getURL("classpath:").getPath());
				if(null!=ls&&ls.size()>0) {
					for(RptBookMaterial material:ls) {
						String filePath=path.getAbsolutePath()+material.getMaterialPath();
						File file=new File(filePath);
						file.delete();
					}
				}
			}
		}else if (obj instanceof RptAcademic) {
			RptAcademic tmp=(RptAcademic)obj;
			List<RptAcademicMaterial> ls=tmp.getMaterials();
			if (null!=ls&&ls.size()>0) {
				//成功之后  删除文件
				File path = new File(ResourceUtils.getURL("classpath:").getPath());
				if(null!=ls&&ls.size()>0) {
					for(RptAcademicMaterial material:ls) {
						String filePath=path.getAbsolutePath()+material.getMaterialPath();
						File file=new File(filePath);
						file.delete();
					}
				}
			}
		}else if (obj instanceof RptAchievement) {
			RptAchievement tmp=(RptAchievement)obj;
			List<RptAchievementMaterial> ls=tmp.getMaterials();
			if (null!=ls&&ls.size()>0) {
				//成功之后  删除文件
				File path = new File(ResourceUtils.getURL("classpath:").getPath());
				if(null!=ls&&ls.size()>0) {
					for(RptAchievementMaterial material:ls) {
						String filePath=path.getAbsolutePath()+material.getMaterialPath();
						File file=new File(filePath);
						file.delete();
					}
				}
			}
		}else if (obj instanceof RptPaper) {
			RptPaper tmp=(RptPaper)obj;
			List<RptPaperMaterial> ls=tmp.getMaterials();
			if (null!=ls&&ls.size()>0) {
				//成功之后  删除文件
				File path = new File(ResourceUtils.getURL("classpath:").getPath());
				if(null!=ls&&ls.size()>0) {
					for(RptPaperMaterial material:ls) {
						String filePath=path.getAbsolutePath()+material.getMaterialPath();
						File file=new File(filePath);
						file.delete();
					}
				}
			}
		}else if (obj instanceof RptPatent) {
			RptPatent tmp=(RptPatent)obj;
			List<RptPatentMaterial> ls=tmp.getMaterials();
			if (null!=ls&&ls.size()>0) {
				//成功之后  删除文件
				File path = new File(ResourceUtils.getURL("classpath:").getPath());
				if(null!=ls&&ls.size()>0) {
					for(RptPatentMaterial material:ls) {
						String filePath=path.getAbsolutePath()+material.getMaterialPath();
						File file=new File(filePath);
						file.delete();
					}
				}
			}
		}else if (obj instanceof RptScientific) {
			RptScientific tmp=(RptScientific)obj;
			List<RptScientificMaterial> ls=tmp.getMaterials();
			if (null!=ls&&ls.size()>0) {
				//成功之后  删除文件
				File path = new File(ResourceUtils.getURL("classpath:").getPath());
				if(null!=ls&&ls.size()>0) {
					for(RptScientificMaterial material:ls) {
						String filePath=path.getAbsolutePath()+material.getMaterialPath();
						File file=new File(filePath);
						file.delete();
					}
				}
			}
		}
		
		dao.delete(obj);
		return IdValue;
	}
	//通用推荐
	public <T> int updScreRecommend(T t,String priKey,int idValue,String recommend)throws Exception{
		String hql ="update "+t.getClass().getName()+" set recommend=?0 where "+priKey+"=?1";
		return dao.bulkUpdate(hql, recommend,idValue);
	}
	public <T> int updScreStatus(T t,String priKey,int idValue,String newStatus)throws Exception{
		String hql ="update "+t.getClass().getName()+" set status=?0 where "+priKey+"=?1";
		return dao.bulkUpdate(hql, newStatus,idValue);
	}
	/**
	 * 统计某个用户的科研成果
	 * @param className
	 * @param screType
	 * @param userCode
	 * @param belongCycle
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getScreCount(String className,String screType,
			String userCode,String belongCycle)throws Exception{
		String hql ="select count(o) as rptCount, sum(expectedMark) as expectedMark,"
				+ " '"+screType+"' as screType ,'"+className+"' as className "
				+ "from "+className+" o where userCode=?0 and belongCycle=?1 ";
		return dao.findMap(hql, userCode,belongCycle);
	}
	/**
	 * 根据状态统计某个用户的科研成果
	 * @param className
	 * @param screType
	 * @param userCode
	 * @param belongCycle
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getScreCount(String className,String screType,
			String userCode,String belongCycle,String status)throws Exception{
		String hql ="select count(o) as rptCount, sum(expectedMark) as expectedMark,"
				+ " '"+screType+"' as screType ,'"+className+"' as className "
				+ "from "+className+" o where userCode=?0 and belongCycle=?1  and status=?2";
		return dao.findMap(hql, userCode,belongCycle,status);
	}
	
	public List<Map<String,Object>> getScreStatistics(ViewscreAchieve view)throws Exception{
		String hql ="select count(o) as rptCount, sum(o.expectedMark) as expectedMark,"
				+ "o.rptName as rptName,o.rptId as rptId"
				+ " from ViewscreAchieve o ";
		
		int index=0;
		List<Object> params=new ArrayList<Object>();
		if (view !=null) {
			if(null!=view.getStatus()&&!"".equals(view.getStatus())) {
				hql+= (index==0?"where":"and")+"  status = ?"+(index++);
				params.add(view.getStatus());
			}
			if(null!=view.getStaffParentDepart()&&!"".equals(view.getStaffParentDepart())) {
				hql+= (index==0?"where":"and")+"  staffParentDepart = ?"+(index++);
				params.add(view.getStaffParentDepart());
			}
			if(null!=view.getRecommend()&&!"".equals(view.getRecommend())) {
				hql+= (index==0?"where":"and")+"  recommend = ? "+(index++);
				params.add(view.getRecommend());
			}

		}
		hql+=" group by o.className";
		return dao.findMap(hql, params.toArray());
	}
	public Integer findViewscreAchieveCount(ViewscreAchieve view) throws Exception {
		String hql="from ViewscreAchieve  ";
		int index=0;
		List<Object> params=new ArrayList<Object>();
		if (view !=null) {
			if(null!=view.getStatus()&&!"".equals(view.getStatus())) {
				hql+= (index==0?" where":" and")+"  status = ?"+(index++);
				params.add(view.getStatus());
			}
			if(null!=view.getStaffParentDepart()&&!"".equals(view.getStaffParentDepart())) {
				hql+= (index==0?" where":"  and")+"  staffParentDepart = ?"+(index++);
				params.add(view.getStaffParentDepart());
			}
			if(null!=view.getRecommend()&&!"".equals(view.getRecommend())) {
				hql+= (index==0?" where ":" and")+"  recommend = ? "+(index++);
				params.add(view.getRecommend());
			}

		}
		hql+=" order by createTime desc";
		int count=dao.queryBeanCountByHql(hql, params.toArray());
		return count;
		
	}
	/**
	 * 优秀成果展示
	 * */
	public List<Map<String,Object>> getScreStatistics(ViewscreAchieve view,int pageNo,int maxResults)throws Exception{
		String hql ="select o.rptName as rptName,o.rptId as rptId,o.createTime as createTime,o.className  as className,o.staffParentDepart  as staffParentDepart"
				+ " from ViewscreAchieve o ";
		
		int index=0;
		List<Object> params=new ArrayList<Object>();
		if (view !=null) {
			if(null!=view.getStatus()&&!"".equals(view.getStatus())) {
				hql+= (index==0?" where":" and")+"  status = ?"+(index++);
				params.add(view.getStatus());
			}
			if(null!=view.getStaffParentDepart()&&!"".equals(view.getStaffParentDepart())) {
				hql+= (index==0?" where":"  and")+"  staffParentDepart = ?"+(index++);
				params.add(view.getStaffParentDepart());
			}
			if(null!=view.getRecommend()&&!"".equals(view.getRecommend())) {
				hql+= (index==0?" where ":" and")+"  recommend = ? "+(index++);
				params.add(view.getRecommend());
			}

		}
		hql+=" order by o.createTime desc";
		return dao.findByPageMap(hql, params.toArray(), pageNo, maxResults);
	}
	public List<Map<String,Object>> getScreStatistics(String staffParentDepart,String[] status,String belongCycle)throws Exception{
		String hql ="select count(o) as rptCount, sum(o.expectedMark) as expectedMark,"
				+ "o.status as status,o.userCode as userCode "
				+ "from ViewscreAchieve o ";
		
		int index=0;
		List<Object> params=new ArrayList<Object>();
		if(null!=status&&status.length>0) {
			hql+= (index==0?"where":"and")+"  status in (?"+(index++)+")";
			params.add(status);
		}
		if(null!=staffParentDepart&&!"".equals(staffParentDepart)) {
			hql+= (index==0?"where":"and")+"  staffParentDepart = ?"+(index++);
			params.add(staffParentDepart);
		}
		if(null!=belongCycle&&!"".equals(belongCycle)) {
			hql+= (index==0?"where":"and")+"  belongCycle = ?"+(index++);
			params.add(belongCycle);
		}
		hql+="group by status,userCode ";
		return dao.findMap(hql, params.toArray());
	}
	/**
	 * 获取每种科研作品的数量
	 * @param staffParentDepart
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getScreStatisByClassType(String staffParentDepart,String[] status,String belongCycle)throws Exception{
		String hql ="select count(o) as rptCount, sum(o.expectedMark) as expectedMark,"
				+ "o.className as className "
				+ "from ViewscreAchieve o ";
		int index=0;
		List<Object> params=new ArrayList<Object>();
		if(null!=status&&status.length>0) {
			hql+= (index==0?"where":"and")+"  status in (?"+(index++)+")";
			params.add(status);
		}
		if(null!=staffParentDepart&&!"".equals(staffParentDepart)) {
			hql+= (index==0?"where":"and")+"  staffParentDepart = ?"+(index++);
			params.add(staffParentDepart);
		}
		if(null!=belongCycle&&!"".equals(belongCycle)) {
			hql+= (index==0?"where":"and")+"  belongCycle = ?"+(index++);
			params.add(belongCycle);
		}
		hql+="group by className ";
		return dao.findMap(hql, params.toArray());
	}
	/**
	 * 近5个周期的各类作品趋势
	 * @return
	 */
	public List<Map<String,Object>> getScreStatisByClassName(int cycleNum,String className)throws Exception{
		String sql="select x.cycleName as cycleName,IFNULL(y.rptCount,0) as rptCount from ( " + 
				"SELECT  b.cycleName,b.beginDate,b.endDate,b.createTime  FROM sysscrecycle b ORDER BY b.createTime desc LIMIT 0,"+cycleNum+"  ) x " + 
				"LEFT JOIN " + 
				"(select a.belongCycle ,count(*) rptCount " + 
				"from viewscreachieve  a where a.className=?0  " + 
				"GROUP by a.belongCycle " + 
				") y " + 
				"on x.cycleName=y.belongCycle " + 
				"ORDER BY x.createTime";
		return dao.findBySQL(sql, new Object[] {className});
	}
	
	/**
	 * 获取每个科室的作品数
	 * @param staffParentDepart
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getScreStatisByDepart(String status,String belongCycle)throws Exception{
		String hql ="select count(o) as rptCount, sum(o.expectedMark) as expectedMark,"
				+ "o.staffParentDepart as staffParentDepart "
				+ "from ViewscreAchieve o ";
		int index=0;
		List<Object> params=new ArrayList<Object>();
		if(null!=status&&!"".equals(status)) {
			hql+= (index==0?"where":"and")+"  status = ?"+(index++);
			params.add(status);
		}
		if(null!=belongCycle&&!"".equals(belongCycle)) {
			hql+= (index==0?"where":"and")+"  belongCycle = ?"+(index++);
			params.add(belongCycle);
		}
		hql+="group by staffParentDepart ";
		return dao.findMap(hql, params.toArray());
	}
}
