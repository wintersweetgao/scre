package edu.hfu.scre.service.review;

import java.util.*;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import edu.hfu.scre.entity.SysScreCycle;
import edu.hfu.scre.entity.SysStaffMarkStandard;
import edu.hfu.scre.util.FormatUtil;
import org.springframework.stereotype.Service;

import edu.hfu.scre.dao.review.CommonScreDao;
import edu.hfu.scre.util.CacheData;

@Service
@Transactional
public class MyScreService {

	@Resource
	CommonScreDao commonDao;
	
	/**
	 * 获取个人的成果
	 * @param userCode
	 * @param belongCycle
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getScreCountByCode(
			String userCode,String belongCycle) throws Exception{
		List<Map<String,Object>> ls=new ArrayList<>();
		List<Map<String,Object>> ls1=commonDao.getScreCount("RptScientific", CacheData.getDictValByKey("RptScientific", "统计表标题"), userCode, belongCycle);
		List<Map<String,Object>> ls2=commonDao.getScreCount("RptBook", CacheData.getDictValByKey("RptBook", "统计表标题"), userCode, belongCycle);
		List<Map<String,Object>> ls3=commonDao.getScreCount("RptPaper", CacheData.getDictValByKey("RptPaper", "统计表标题"), userCode, belongCycle);
		List<Map<String,Object>> ls4=commonDao.getScreCount("RptPatent", CacheData.getDictValByKey("RptPatent", "统计表标题"), userCode, belongCycle);
		List<Map<String,Object>> ls5=commonDao.getScreCount("RptAcademic",CacheData.getDictValByKey("RptAcademic", "统计表标题"), userCode, belongCycle);
		List<Map<String,Object>> ls6=commonDao.getScreCount("RptAchievement", CacheData.getDictValByKey("RptAchievement", "统计表标题"), userCode, belongCycle);
		ls.addAll(ls1);
		ls.addAll(ls2);
		ls.addAll(ls3);
		ls.addAll(ls4);
		ls.addAll(ls5);
		ls.addAll(ls6);
		return ls;
	}
	/**
	 * 获取已经通过审批的个人科研
	 * @param userCode
	 * @param belongCycle
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getScreApproveCountByCode(
			String userCode,String belongCycle) throws Exception{
		List<Map<String,Object>> ls=new ArrayList<>();
		List<Map<String,Object>> ls1=commonDao.getScreCount("RptScientific", CacheData.getDictValByKey("RptScientific", "统计表标题"), userCode, belongCycle,"EApprove");
		List<Map<String,Object>> ls2=commonDao.getScreCount("RptBook", CacheData.getDictValByKey("RptBook", "统计表标题"), userCode, belongCycle,"EApprove");
		List<Map<String,Object>> ls3=commonDao.getScreCount("RptPaper",CacheData.getDictValByKey("RptPaper", "统计表标题"), userCode, belongCycle,"EApprove");
		List<Map<String,Object>> ls4=commonDao.getScreCount("RptPatent", CacheData.getDictValByKey("RptPatent", "统计表标题"), userCode, belongCycle,"EApprove");
		List<Map<String,Object>> ls5=commonDao.getScreCount("RptAcademic", CacheData.getDictValByKey("RptAcademic", "统计表标题"), userCode, belongCycle,"EApprove");
		List<Map<String,Object>> ls6=commonDao.getScreCount("RptAchievement", CacheData.getDictValByKey("RptAchievement", "统计表标题"), userCode, belongCycle,"EApprove");
		ls.addAll(ls1);
		ls.addAll(ls2);
		ls.addAll(ls3);
		ls.addAll(ls4);
		ls.addAll(ls5);
		ls.addAll(ls6);
		return ls;
	}

	/**
	 *
	 * @param entryDate 入职日期
	 * @param cycle 科研周期
	 * @param standMark 标准分
	 * @return （1）入职未到一年返回  -1 ；（2）在达标周期内 (实际任职月数/24)*本人职称标准分；（3）其他原值返回，如老员工
	 */
	public Integer getStandMark(Date entryDate,SysScreCycle cycle,Integer standMark){
		//计算考核分数
		//计算入职日期为考核周期结束前12个月，参与考核
		Calendar endDate = Calendar.getInstance();
		endDate.setTime(FormatUtil.strToDate(cycle.getEndDate(), "yyyy-MM-dd"));
		endDate.add(Calendar.MONTH, -12);

		Date beginDate=FormatUtil.strToDate(cycle.getBeginDate(), "yyyy-MM-dd");

		//入职未满1年 无需参加本轮达标
		if(entryDate.getTime()>endDate.getTime().getTime()) {
			standMark = -1;
		}
		//在达标周期内的
		if ((entryDate.getTime()>beginDate.getTime())&&(entryDate.getTime()<endDate.getTime().getTime())){
			int difMonth=FormatUtil.getMonthDiff(FormatUtil.strToDate(cycle.getEndDate(), "yyyy-MM-dd"), entryDate);
			standMark=(difMonth*standMark)/24;
		}
		return standMark ;
	}
}
