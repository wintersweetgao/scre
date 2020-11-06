package edu.hfu.scre.service.review;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import edu.hfu.scre.dao.review.ScientificDao;
import edu.hfu.scre.dao.sysset.ReviewLogDao;
import edu.hfu.scre.entity.RptScientific;
import edu.hfu.scre.entity.RptScientificMaterial;
import edu.hfu.scre.entity.SysReviewLog;
import edu.hfu.scre.service.sysset.ScreStandardService;

@Service(value = "scientificService")
@Transactional
public class ScientificService{
	
	@Resource
	ScientificDao scientificDao;
	@Resource
	ReviewLogDao logDao;
	@Resource
	ScreStandardService standService;
	
	//系查詢
	public List<RptScientific> getScientificContent(RptScientific scie,Integer pageNo, Integer maxResults) throws Exception {
		return scientificDao.getScientificContent(scie, pageNo, maxResults);
	}

	public Integer getScientificCount(RptScientific scie) throws Exception {
		return scientificDao.getScientificCount(scie);
	}
	
	/**
	 * 保存科研课题
	 * @param scie
	 * @return
	 * @throws Exception
	 */
	public String  saveScientific(RptScientific scie) throws Exception {

			scie.setCreateTime(new Date());
			//计算科研积分
			Integer expectedMark=standService.getStandardMark(scie.getScreType() ,scie.getScreTopic(),scie.getInOrder(),scie.getBelongCycle());
			scie.setExpectedMark(expectedMark);
			scie.setStatus("BApprove");
			int recordId=scientificDao.saveScientific(scie);
			logDao.addReviewLog("RptScientific", recordId, "通过","AApprove", "BApprove", "新增科研课题:"+scie.getScieName());
			return "succ";
		
	}

	public String updScientific(RptScientific scie) throws Exception {
		RptScientific oldSci=scientificDao.getScientificById(scie.getScieId());
		if (null!=scie) {
			oldSci.setScreType(scie.getScreType());//科研达标类型
			oldSci.setScreTopic(scie.getScreTopic());//科研达标内容
			oldSci.setScieName(scie.getScieName());//项目名称
			oldSci.setScieDepart(scie.getScieDepart());//项目审批部门
			oldSci.setScieLeader(scie.getScieLeader());//项目负责人
			oldSci.setScieStartDate(scie.getScieStartDate());//立项时间
			oldSci.setScieCloseDate(scie.getScieCloseDate());//结题时间
			oldSci.setScieOk(scie.getScieOk()); //是否结题	
			oldSci.setInOrder(scie.getInOrder());//参与次序
			//计算科研积分
			Integer expectedMark=standService.getStandardMark(scie.getScreType() ,scie.getScreTopic(),scie.getInOrder(),oldSci.getBelongCycle());
			oldSci.setExpectedMark(expectedMark);//预计得分
			scientificDao.updScientific(oldSci);
		}
		return "succ";
	}

	public boolean delScientific(Integer scieId,String scieName) throws Exception {
		logDao.addReviewLog("RptScientific", scieId,"通过", "BApprove", "FApprove", "删除科研课题:"+scieName);
		//先删除物料
		List<RptScientificMaterial> ls=scientificDao.getScientificMateriaById(scieId);
		scientificDao.delScientificMaterial(scieId);
		boolean res=scientificDao.delScientific(scieId);
		if(res) {
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
		return res;
	}
	public int submitScientific(Integer scieId,String scieName) throws Exception {
		logDao.addReviewLog("RptScientific", scieId,"通过", "BApprove", "CApprove", "提交科研课题:"+scieName);
		return scientificDao.updScientificStatus(scieId,"CApprove");
	}
	/**
	 * 获取审批记录
	 * @param recordId
	 * @return
	 * @throws Exception
	 */
	public List<SysReviewLog> findLog(Integer recordId)throws Exception{
		return logDao.findLog("RptScientific", recordId);
	}
	
	
	public Integer approveScientificStatus(Integer scieId,String action,String oldStatus,String newStatus,String reason) throws Exception {
		logDao.addReviewLog("RptScientific",scieId,action,oldStatus,newStatus, reason);
		return scientificDao.updScientificStatus(scieId, newStatus);
	}


}
