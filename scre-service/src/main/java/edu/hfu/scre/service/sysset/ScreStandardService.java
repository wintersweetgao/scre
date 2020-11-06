package edu.hfu.scre.service.sysset;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import edu.hfu.scre.dao.sysset.ScreStandardDao;
import edu.hfu.scre.entity.SysScreStandard;
import edu.hfu.scre.entity.SysStaffMarkStandard;

@Service
@Transactional
public class ScreStandardService {

	@Resource
	ScreStandardDao screStandardDao;
	public Integer getstandCount(SysScreStandard stand) throws Exception {
		return screStandardDao.getPatentByNameCount(stand);
	}
	public List<SysScreStandard> queryScreStandard(SysScreStandard stand) throws Exception{
		return screStandardDao.queryScreStandard(stand);
	}
	public List<SysScreStandard> queryAllScreStandard(SysScreStandard stand,Integer pageNo,Integer maxResults) throws Exception{
		return screStandardDao.queryAllScreStandard(stand,pageNo,maxResults);
	}
	public void saveScreStandard(SysScreStandard screStand) throws Exception {
		screStandardDao.saveScreStandard(screStand);
	}
	public void delScreStandard(Integer standardId) throws Exception {
		screStandardDao.delScreStandard(standardId);
	}
	public void updScreStandard(SysScreStandard screStand) throws Exception {
		screStandardDao.updScreStandard(screStand);
	}
	
	
	/**
	 * 根据科研类型  获取积分标准
	 * @param screType
	 * @param screTopic
	 * @return
	 * @throws Exception
	 */
	public Integer getStandardMark(String screType ,String screTopic,Integer inOrder,String belongCycle)throws Exception {
		SysScreStandard stand=new SysScreStandard();
		stand.setScreTopic(screTopic);
		stand.setScreType(screType);
		stand.setBelongCycle(belongCycle);
		List<SysScreStandard> ls=screStandardDao.queryScreStandard(stand);
		if (null!=ls&&ls.size()==1) {
			//多条时这里会是计算错误
			stand=ls.get(0);
			if (inOrder>stand.getValidNum()) {
				return 0;
			}else {
				String mthName="getWeight"+inOrder;
				Method mth=stand.getClass().getMethod(mthName);
				Object fieldVal=mth.invoke(stand);
				//权重浮点类型如：1, 0.5, 0.25, 0.125
				float weightValue= Float.parseFloat(String.valueOf(fieldVal));
				//分数等于：权重*标准分（权重为1的分数）
				float score = weightValue * stand.getScreMark() ;
				return Math.round(score);
			}
		}else {
			throw new java.lang.RuntimeException("未找到或找到大于1条-"+screTopic+"-"+screType+"-"+belongCycle+"-对应的积分标准");
		}
	}
}
