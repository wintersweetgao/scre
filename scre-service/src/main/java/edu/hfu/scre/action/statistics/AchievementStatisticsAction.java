package edu.hfu.scre.action.statistics;

import edu.hfu.scre.entity.*;
import edu.hfu.scre.service.review.CommonScreService;
import edu.hfu.scre.service.security.DepartService;
import edu.hfu.scre.util.Constant;
import edu.hfu.scre.util.FormatUtil;
import edu.hfu.scre.util.ResponseUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.print.Book;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="/statistics")
public class AchievementStatisticsAction {
    private static final Logger LOG = LoggerFactory.getLogger(AchievementStatisticsAction.class);
    @Resource
    DepartService departService;
    @Autowired
    private HttpSession session;
    @Resource
    CommonScreService commonService;
    /**
     * 进入统计界面
     * @return
     */
    @RequestMapping(value="/initAchievementStatistics", method= {RequestMethod.GET,RequestMethod.POST})
    @PreAuthorize("hasPermission('/initAchievementStatistics','000305')")
    public ModelAndView initAchievementStatistics(){
        //列表
        List<SysDepart> departs=departService.getAllDepartOne();
        ModelAndView mod= new ModelAndView("/statistics/achievementStatistics.btl");
        mod.addObject("departs", departs);
        return mod;
    }
    /**
     * 查询本院所有人已提交的
     * @param scie
     * @param pageNo
     * @param maxResults
     * @param session
     * @return
     */
    @RequestMapping(value="/queryAchievementStatistics", method= {RequestMethod.GET,RequestMethod.POST})
    @PreAuthorize("hasPermission('/queryAchievementStatistics','000305000')")
    public Map<String,Object> queryAchievementStatistics(RptAchievement achievement,Integer pageNo,Integer maxResults){
        Map<String,Object> rtnMap=new HashMap<>();
        try {
            SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
            if (null==cycle) {
                rtnMap.put("mess", "未获取到科研周期！");
                rtnMap.put("total", 0);
                rtnMap.put("rows", null);
            }else {
                achievement.setStatus("EApprove");
                achievement.setBelongCycle(cycle.getCycleName());
                List<RptAchievement> rows=commonService.getContentByCon(achievement, pageNo, maxResults);
                Integer total=commonService.getCountByCon(achievement);
                String mess="succ";
                rtnMap.put("total", total);
                rtnMap.put("rows", rows);
                rtnMap.put("mess", mess);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            rtnMap.put("total", 0);
            rtnMap.put("rows", null);
            rtnMap.put("mess", e.toString());
        }
        return rtnMap;
    }
    /**
     *导出 excel 使用我们的模板导出
     *    /statistics/optexcel
     */
    @RequestMapping(value = "/achievementExcel", method = {RequestMethod.POST,RequestMethod.GET})
    @PreAuthorize("hasPermission('/achievementExcel','000305001')")
    public String achievementExcel(HttpServletResponse response, HttpServletRequest request, RptAchievement achievement) {
        try {
            SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
            String cycleName="";
            if (null==cycle) {
                throw new  java.lang.RuntimeException("未找到默认的科研周期");
            }else {
                achievement.setStatus("EApprove");
                achievement.setBelongCycle(cycle.getCycleName());
                cycleName=cycle.getCycleName()+"("+cycle.getBeginDate()+"至"+cycle.getEndDate()+"年度)";
            }
            Workbook wb = commonService.fillExcelDataWithTemplate(achievement, cycleName);
            String exportName= FormatUtil.getEntityName(achievement.getClass().getName())+FormatUtil.formatDateToStr(new Date(), "yyyyMMddHHmmss");
            ResponseUtil.export(response, wb, exportName+ Constant.EXCEL_EXTENSION );//指定文件名称
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     *是否推荐
     */
    @RequestMapping(value="/updAchievementRend", method= {RequestMethod.GET,RequestMethod.POST})
    public String updAchievementRend(RptAchievement achievement,Integer achievementId,String recommend) {
        LOG.debug("achievementId:"+ achievement.getAchievementId());
        String mess="";
        try {	
            commonService.updScreRecommend(achievement, achievementId,recommend);
        	mess="succ";
		} catch (Exception e) {
			e.printStackTrace();
			mess=e.toString();
		}
		return mess;
	}
}