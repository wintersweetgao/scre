package edu.hfu.scre.service.sysset;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import edu.hfu.scre.dao.sysset.ScreCycleDao;
import edu.hfu.scre.dao.sysset.ScreStandardDao;
import edu.hfu.scre.dao.sysset.StaffTopicStandDao;
import edu.hfu.scre.entity.SysScreCycle;
import edu.hfu.scre.entity.SysScreStandard;
import edu.hfu.scre.entity.SysStaffMarkStandard;

@Service
@Transactional
public class ScreCycleService {

    @Resource
    ScreCycleDao screCycleDao;
    @Resource
    StaffTopicStandDao staffTopicStandDao;
    @Resource
    ScreStandardDao screStandardDao;
    public List<SysScreCycle> getAllScreCycle() throws Exception{
        return screCycleDao.queryScreCycle(null);
    }

    public void addNextCycle(SysScreCycle cycle) throws Exception {
        SysScreCycle oldcycle = screCycleDao.getLastCycle();
        if (null != oldcycle) {
            oldcycle.setIsOver("结束");
            screCycleDao.updScreCycle(oldcycle);
        }
        //	所属职称积分标准-list1
        cycle.setCreateTime(new Date());
        cycle.setIsOver("进行中");
        screCycleDao.saveScreCycle(cycle);
        SysStaffMarkStandard markStandard = new SysStaffMarkStandard();
        markStandard.setBelongCycle(oldcycle.getCycleName());
        List<SysStaffMarkStandard> list1 = staffTopicStandDao.queryStaffTopicStand(markStandard);
        //循环保存
        for (SysStaffMarkStandard stand1 : list1) {
            SysStaffMarkStandard newStand=new SysStaffMarkStandard();
            newStand.setMarkStandard(stand1.getMarkStandard());
            newStand.setStaffTitle(stand1.getStaffTitle());
            // stand1.setStandardId(null);
            newStand.setBelongCycle(cycle.getCycleName());
            staffTopicStandDao.saveStaffTopicStand(newStand);
        }
        //科研达标积分标准-list2
        cycle.setCreateTime(new Date());
        cycle.setIsOver("进行中");
        screCycleDao.saveScreCycle(cycle);
        SysScreStandard standard = new SysScreStandard();
        standard.setBelongCycle(oldcycle.getCycleName());
        List<SysScreStandard> list2 = screStandardDao.queryScreStandard(standard);
        for (SysScreStandard stand2 : list2) {
            SysScreStandard newStandard=new SysScreStandard();
            newStandard.setScreMark(stand2.getScreMark());
            // stand2.setStandardId(null);
            newStandard.setScreType(stand2.getScreType());
            newStandard.setScreTopic(stand2.getScreTopic());
            newStandard.setValidNum(stand2.getValidNum());
            newStandard.setWeight1(stand2.getWeight1());
            newStandard.setWeight2(stand2.getWeight2());
            newStandard.setWeight3(stand2.getWeight3());
            newStandard.setWeight4(stand2.getWeight4());
            newStandard.setWeight5(stand2.getWeight5());
            newStandard.setBelongCycle(cycle.getCycleName());
            screStandardDao.saveScreStandard(newStandard);
        }
        //System.out.println("内容为："+list1);
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public SysScreCycle getLastCycle()throws Exception{
        return screCycleDao.getLastCycle();
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public List<SysScreCycle> getLastFiveCycle()throws Exception{
        return  screCycleDao.getLastFiveCycle();
    }
}
