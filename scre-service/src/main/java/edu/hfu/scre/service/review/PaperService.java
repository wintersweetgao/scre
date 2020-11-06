package edu.hfu.scre.service.review;

import java.util.Date;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import edu.hfu.scre.dao.review.PaperDao;
import edu.hfu.scre.entity.RptPaper;
import edu.hfu.scre.service.sysset.ScreStandardService;

@Service
@Transactional
public class PaperService {

	@Resource
	ScreStandardService standService;
	@Resource
	PaperDao dao;
	/**
	 * 保存
	 * @param paper
	 * @return
	 * @throws Exception
	 */
	public String savePaper(RptPaper paper)throws Exception {
		
//		paper.setCreateTime(new Date());
//		//计算科研积分
//		Integer expectedMark=standService.getStandardMark(paper.getPaperType() ,scie.getScreTopic(),scie.getInOrder());
//		paper.setExpectedMark(expectedMark);
//		paper.setStatus("BApprove");
//		int recordId=dao.savePaper(paper);;
//		logDao.addReviewLog("RptScientific", recordId, "通过","AApprove", "BApprove", "新增科研课题:"+scie.getScieName());
		return "succ";
		 
	}
	/**
	 * 更新
	 * @param paper
	 * @throws Exception
	 */
	public void updPaper(RptPaper paper)throws Exception {
		dao.updPaper(paper);
	}
}
