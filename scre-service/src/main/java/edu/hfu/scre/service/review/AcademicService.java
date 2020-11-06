package edu.hfu.scre.service.review;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import edu.hfu.scre.dao.review.AcademicDao;
import edu.hfu.scre.dao.review.PaperDao;
import edu.hfu.scre.entity.RptAcademic;
import edu.hfu.scre.entity.RptPaper;
import edu.hfu.scre.service.sysset.ScreStandardService;

@Service
@Transactional
public class AcademicService {

	@Resource
	ScreStandardService standService;
	@Resource
	AcademicDao dao;
	/**
	 * 保存
	 * @param paper
	 * @return
	 * @throws Exception
	 */
	public String saveAcademic(RptAcademic academic)throws Exception {
		
//		paper.setCreateTime(new Date());
//		//计算科研积分
//		Integer expectedMark=standService.getStandardMark(paper.getPaperType() ,scie.getScreTopic(),scie.getInOrder());
//		paper.setExpectedMark(expectedMark);
//		paper.setStatus("BApprove");
//		int recordId=dao.savePaper(paper);;
//		logDao.addReviewLog("RptScientific", recordId, "通过","新建", "BApprove", "新增科研课题:"+scie.getScieName());
		return "succ";
		 
	}
	/**
	 * 更新
	 * @param paper
	 * @throws Exception
	 */
	public void updAcademic(RptAcademic academic)throws Exception {
		dao.updAcademic(academic);
	}
}

