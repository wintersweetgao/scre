package edu.hfu.scre.service.review;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import edu.hfu.scre.dao.review.AchievementDao;
import edu.hfu.scre.dao.review.PaperDao;
import edu.hfu.scre.entity.RptAchievement;
import edu.hfu.scre.entity.RptPaper;
import edu.hfu.scre.service.sysset.ScreStandardService;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AchievementService {

    @Resource
    ScreStandardService standService;
    @Resource
    AchievementDao dao;
    /**
     * 保存
     * @param achievement
     * @return
     * @throws Exception
     */
    public String saveAchievement(RptAchievement achievement)throws Exception {

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
     * @param achievement
     * @throws Exception
     */
    public void updAchievement(RptAchievement achievement)throws Exception {
        dao.updAchievement(achievement);
    }

}
