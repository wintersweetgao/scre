package edu.hfu.scre.dao.review;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import edu.hfu.scre.dao.base.BaseDaoImpl;
import edu.hfu.scre.entity.RptScientific;
import edu.hfu.scre.entity.RptScientificMaterial;

@Repository
public class ScientificDao {
	private static final Logger LOG = LoggerFactory.getLogger(ScientificDao.class);
	@Resource
	private BaseDaoImpl dao;
	
	@SuppressWarnings("unchecked")
	public List<RptScientific> getScieById(RptScientific scie) throws Exception {
		Map<String, Object> map=dao.beanToHql(scie);
		return dao.find((String)map.get("hql"),((List<Object>)map.get("param")).toArray());
	}
	//查询系
	public List<RptScientific> getScientificContent(RptScientific scie,Integer pageNo,Integer maxResults) throws Exception{
		String hql="from RptScientific ";
		int index=0;
		List<Object> params=new ArrayList<Object>();
		if(null!=scie) {
			if(null!=scie.getScieName()&&!"".equals(scie.getScieName())) {
				hql+= (index==0?"where":"and")+"  scieName like ?"+(index++);
				params.add('%'+scie.getScieName()+'%');
			}
			if(null!=scie.getScieLeader()&&!"".equals(scie.getScieLeader())) {
				hql+= (index==0?"where":"and")+"  scieLeader like ?"+(index++);
				params.add('%'+scie.getScieLeader()+'%');
			}
			if(null!=scie.getStaffParentDepart()&&!"".equals(scie.getStaffParentDepart())) {
				hql+= (index==0?"where":"and")+"  staffParentDepart = ?"+(index++);
				params.add(scie.getStaffParentDepart());
			}
			if(null!=scie.getStaffDepart()&&!"".equals(scie.getStaffDepart())) {
				hql+= (index==0?"where":"and")+"  staffDepart = ?"+(index++);
				params.add(scie.getStaffDepart());
			}
			if(null!=scie.getStatus()&&!"".equals(scie.getStatus())) {
				hql+= (index==0?"where":"and")+"  status = ?"+(index++);
				params.add(scie.getStatus());
			}
			if(null!=scie.getStaffName()&&!"".equals(scie.getStaffName())) {
				hql+= (index==0?"where":"and")+"  staffName = ?"+(index++);
				params.add(scie.getStaffName());
			}
			if(null!=scie.getUserCode()&&!"".equals(scie.getUserCode())) {
				hql+= (index==0?"where":"and")+"  userCode = ?"+(index++);
				params.add(scie.getUserCode());
			}
			if(null!=scie.getBelongCycle()&&!"".equals(scie.getBelongCycle())) {
				hql+= (index==0?"where":"and")+"  belongCycle = ?"+(index++);
				params.add(scie.getBelongCycle());
			}
			if(null!=scie.getScreType()&&!"".equals(scie.getScreType())) {
				hql+= (index==0?"where":"and")+"  screType = ?"+(index++);
				params.add(scie.getScreType());
			}
			if(null!=scie.getScreTopic()&&!"".equals(scie.getScreTopic())) {
				hql+= (index==0?"where":"and")+"  screTopic = ?"+(index++);
				params.add(scie.getScreTopic());
			}
		}
		hql+="order by scieId";
		LOG.debug("hql:"+hql);
		return dao.findByPage(hql,params.toArray(),pageNo, maxResults);
	}
	
	public Integer getScientificCount(RptScientific scie) throws Exception{
		String hql="from RptScientific  ";
		int index=0;
		List<Object> params=new ArrayList<Object>();
		if(null!=scie) {
			if(null!=scie.getScieName()&&!"".equals(scie.getScieName())) {
				hql+= (index==0?"where":"and")+ " scieName like ?"+(index++);
				params.add('%'+scie.getScieName()+'%');
			}
			if(null!=scie.getScieLeader()&&!"".equals(scie.getScieLeader())) {
				hql+=(index==0?"where":"and")+ "  scieLeader like ?"+(index++);
				params.add('%'+scie.getScieLeader()+'%');
			}
			if(null!=scie.getStaffParentDepart()&&!"".equals(scie.getStaffParentDepart())) {
				hql+=(index==0?"where":"and")+ "  staffParentDepart = ?"+(index++);
				params.add(scie.getStaffParentDepart());
			}
			if(null!=scie.getStaffDepart()&&!"".equals(scie.getStaffDepart())) {
				hql+=(index==0?"where":"and")+ "  staffDepart = ?"+(index++);
				params.add(scie.getStaffDepart());
			}
			if(null!=scie.getStatus()&&!"".equals(scie.getStatus())) {
				hql+=(index==0?"where":"and")+ "  status = ?"+(index++);
				params.add(scie.getStatus());
			}
			if(null!=scie.getStaffName()&&!"".equals(scie.getStaffName())) {
				hql+=(index==0?"where":"and")+ "  staffName = ?"+(index++);
				params.add(scie.getStaffName());
			}
			if(null!=scie.getUserCode()&&!"".equals(scie.getUserCode())) {
				hql+=(index==0?"where":"and")+ "  userCode = ?"+(index++);
				params.add(scie.getUserCode());
			}
			if(null!=scie.getBelongCycle()&&!"".equals(scie.getBelongCycle())) {
				hql+=(index==0?"where":"and")+ "  belongCycle = ?"+(index++);
				params.add(scie.getBelongCycle());
			}
			if(null!=scie.getScreType()&&!"".equals(scie.getScreType())) {
				hql+=(index==0?"where":"and")+ "  screType = ?"+(index++);
				params.add(scie.getScreType());
			}
			if(null!=scie.getScreTopic()&&!"".equals(scie.getScreTopic())) {
				hql+=(index==0?"where":"and")+ "  screTopic = ?"+(index++);
				params.add(scie.getScreTopic());
			}
		} 
		
		return dao.queryBeanCountByHql(hql, params.toArray());
	}
	
	
	public Integer saveScientific(RptScientific scie) throws Exception {
		Integer id=(Integer) dao.save(scie);
		return id;
	}
	/**
	 * 根据名称获取 
	 * @param scieName
	 * @return
	 * @throws Exception
	 */
	public List<RptScientific> getScientificByName(String scieName) throws Exception {
		String hql="from RptScientific where scieName = ?0 ";
		return dao.find(hql,scieName);
	}
	
	public RptScientific getScientificById(Integer scieId)throws Exception {
		return dao.get(RptScientific.class, scieId);
	}
	
	public void updScientific(RptScientific scie) throws Exception {
		dao.update(scie);
	}

	
	public boolean delScientific(Integer scieId) throws Exception {
		String hql="delete RptScientific where scieId=?0";
		int res=dao.bulkUpdate(hql, scieId);
		if(res==1) {
			return true;
		}else  {
			return false;
		}
	}
	
	public void delScientificMaterial(Integer scieId) throws Exception {
		String hql="delete RptScientificMaterial where scientific.scieId=?0";
		dao.bulkUpdate(hql, scieId);
	}
	//获取物料
	public List<RptScientificMaterial> getScientificMateriaById(Integer scieId)throws Exception {
		String hql="from RptScientificMaterial where scientific.scieId=?0";
		return dao.find(hql, scieId);
	}
	/**
	 * 修改记录状态,返回修改成功的记录数
	 */
	public int updScientificStatus(Integer scieId,String status) throws Exception{
		String hql="update RptScientific set status=?0 where scieId=?1";
		return dao.bulkUpdate(hql, status,scieId);
	}

}

