package edu.hfu.scre.service.statistics;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import edu.hfu.scre.service.review.MyScreService;
import org.springframework.stereotype.Service;

import edu.hfu.scre.dao.review.CommonScreDao;
import edu.hfu.scre.dao.security.UserGrantDao;
import edu.hfu.scre.entity.SysScreCycle;
import edu.hfu.scre.entity.SysStaff;
import edu.hfu.scre.entity.xmlBean.SysDictionary;
import edu.hfu.scre.service.sysset.StaffTopicStandService;
import edu.hfu.scre.util.CacheData;
import edu.hfu.scre.util.FormatUtil;

@Service
@Transactional
public class StatisticsService {
	@Resource
	private CommonScreDao commonScreDao;
	@Resource
	UserGrantDao userGrantDao;
	@Resource
	StaffTopicStandService staffTopicStandService;
	@Resource
	MyScreService myScreService ;
	
	//本系的所有提交过的科研作品
	public Map<String,Object> queryDepartScreInfo(String staffParentDepart,SysScreCycle cycle)throws Exception{
		Map<String,Object> rtnmap=new HashMap<>();
		//记录达标人，及其分数
		Map<String,Integer> markmap=new HashMap<>();
		String[] status= {"CApprove","DApprove","EApprove"};
		List<Map<String,Object>> ls=commonScreDao.getScreStatistics(staffParentDepart, status,cycle.getCycleName());
		int cAppCount=0;//已提交作品数量
		int dAppCount=0;//通过本系审核的作品数量
		int eAppCount=0;//通过本院审核的作品数量
		int reachCount=0;//达标人数
		for(Map<String,Object> map:ls) {
			String statTmp=String.valueOf(map.get("status"));
			int rptCount=Integer.parseInt(String.valueOf(map.get("rptCount")));
			cAppCount+=rptCount;
			if (statTmp.equals("DApprove")||statTmp.equals("EApprove")) {
				dAppCount+=rptCount;
			}
			if (statTmp.equals("EApprove")) {
				eAppCount+=rptCount;
				String userCode=String.valueOf(map.get("userCode"));
				String expectedMark=String.valueOf(map.get("expectedMark"));
				if (null==expectedMark||expectedMark.equals("")) {
					markmap.put(userCode, 0);
				}else {
					markmap.put(userCode, Integer.parseInt(expectedMark));
				}
			}
		}
		rtnmap.put("cAppCount", cAppCount);
		rtnmap.put("dAppCount", dAppCount);
		rtnmap.put("eAppCount", eAppCount);
		//计算 参与达标的人是否达标
		reachCount=getFinScreStaff(markmap,cycle);
		rtnmap.put("reachCount", reachCount);
//		for(String key:rtnmap.keySet()) {
//			System.out.println(key+"--"+rtnmap.get(key));
//		}
		return rtnmap;
	}
	/**
	 * 计算参与达标的人  是否达标
	 * @param markmap<userCode,expectedMark> 达标人以及 得到的分数
	 * @param cycle
	 * @return
	 */
	private int getFinScreStaff(Map<String,Integer> markmap,SysScreCycle cycle)throws Exception {
		int reachCount=0;
		//（1）根据userCode 获取达标人的最终职称、入职信息
		String userCodes="";
		for(String code:markmap.keySet()) {
			userCodes+=code+",";
		}
		if (!userCodes.equals("")) {
			Map<String,Integer> standMap=staffTopicStandService.queryStaffTopicStand(cycle.getCycleName());
			userCodes=userCodes.substring(0, userCodes.length()-1);
			List<SysStaff> staffLs=userGrantDao.getStaffInfoByCode(CacheData.getAccessCode(),userCodes);
			if (null!=staffLs) {
				Date beginDate=FormatUtil.strToDate(cycle.getBeginDate(), "yyyy-MM-dd");
				//计算入职日期为考核周期结束前12个月，参与考核
				Calendar endDate = Calendar.getInstance();
				endDate.setTime(FormatUtil.strToDate(cycle.getEndDate(), "yyyy-MM-dd"));
				endDate.add(Calendar.MONTH, -12);
				for(SysStaff staff:staffLs) {
					String staffTitle=staff.getStaffTitle();
					Date entry=staff.getEntryDate();

					Integer expectedMark=markmap.get(staff.getUserCode());
					Integer standMark = myScreService.getStandMark(entry,cycle,standMap.get(staffTitle));
					if(standMark == -1){
						//不参加达标人数不计算
                       continue;
					}
					if (expectedMark>=standMark) {
						reachCount++;
					}
				}
			}else {
				throw new  java.lang.RuntimeException("未找到员工列表："+userCodes);
			}
		}
		
		return reachCount;
	}
	
	
	public List<Map<String,Object>> getScreStatisByClassType(String staffParentDepart,String cycleName,String[] status)throws Exception{
		List<Map<String,Object>> ls=commonScreDao.getScreStatisByClassType(staffParentDepart, status,cycleName);
		//获取所有统计项
		List<SysDictionary>  dicts=CacheData.getDictListByType("统计表标题");
		
		List<Map<String,Object>> res=new ArrayList<>();
		
		for(SysDictionary dict:dicts) {
			String className=dict.getDictKey();
			String screType=dict.getDictValue();
			Map<String,Object> tmp=null;
			for(Map<String,Object> map:ls) {
				if (map.get("className").equals(className)) {
					tmp=map;
					ls.remove(map);
					break;
				}
			}
			if (tmp!=null) {//找到了
				tmp.put("screType", screType);
				res.add(tmp);
			}else {
				tmp=new HashMap<>();
				tmp.put("rptCount", "0");
				tmp.put("expectedMark", "0");
				tmp.put("className", className);
				tmp.put("screType", screType);
				res.add(tmp);
			}
		}
		return res;
	}
	
	/**
	 * 获取所有应参与达标的人
	 * @param cycleDate
	 * @return
	 * @throws Exception
	 */
	public Integer getAllScreStaff(SysScreCycle cycle) throws Exception{
		Calendar cycleDate = Calendar.getInstance();
		cycleDate.setTime(FormatUtil.strToDate(cycle.getEndDate(), "yyyy-MM-dd"));
		cycleDate.add(Calendar.MONTH, -12);
		return userGrantDao.getAllScreStaff(CacheData.getAccessCode(),FormatUtil.formatDateToStr(cycleDate.getTime(),"yyyy-MM-dd"));
	}
	/**
	 * 获取全院达标的人
	 * @param cycle
	 * @return
	 * @throws Exception
	 */
	public Integer getAllFinScreStaff(SysScreCycle cycle)throws Exception{
		String[] status= {"EApprove"};
		Map<String,Integer> markmap=new HashMap<>();
		List<Map<String,Object>> ls=commonScreDao.getScreStatistics(null, status,cycle.getCycleName());
		for(Map<String,Object> map:ls) {
			String userCode=String.valueOf(map.get("userCode"));
			String expectedMark=String.valueOf(map.get("expectedMark"));
			if (null==expectedMark||expectedMark.equals("")||expectedMark.equals("null")) {
				markmap.put(userCode, 0);
			}else {
				markmap.put(userCode, Integer.parseInt(expectedMark));
			}
		}
		
		int reachCount=getFinScreStaff(markmap,cycle);
		return reachCount;
	}
	
	/**
	 * 统计周期内的作品
	 * @param belongCycles
	 * @return key=screType,value 为 n个周期内数据
	 * @throws Exception
	 */
	public Map<String,List<Map<String,Object>>>getScreStatisByCycle(int cycleNums)throws Exception{
		Map<String,List<Map<String,Object>>> map=new HashMap<>();
		//获取统计项
		List<SysDictionary>  dicts=CacheData.getDictListByType("统计表标题");
		for(SysDictionary dict:dicts) {
			List<Map<String,Object>> ls= commonScreDao.getScreStatisByClassName(cycleNums,dict.getDictKey());
			if (null==ls||ls.size()==0) {
				ls=new ArrayList<Map<String,Object>>();
				for(int i=0;i<cycleNums;i++) {
					Map<String,Object> tmp=new HashMap<String, Object>();
					tmp.put("rptCount", 0);
					ls.add(tmp);
				}
				
			}
			map.put(dict.getDictValue(), ls);
		}
		
		return map;
	}
	
	public List<Map<String,Object>> getScreStatisByDepart(String belongCycle)throws Exception{
		String status="EApprove";
		return commonScreDao.getScreStatisByDepart(status,belongCycle);
	}
}
