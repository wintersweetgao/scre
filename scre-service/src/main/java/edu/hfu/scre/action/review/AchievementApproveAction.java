package edu.hfu.scre.action.review;

import edu.hfu.scre.entity.*;
import edu.hfu.scre.service.review.CommonScreService;
import edu.hfu.scre.service.security.DepartService;
import edu.hfu.scre.service.security.MyUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="/review")
public class AchievementApproveAction {
    private static final Logger LOG = LoggerFactory.getLogger(AchievementApproveAction.class);
    @Resource
    DepartService departService;
    @Autowired
    private HttpSession session;
    @Resource
    CommonScreService commonService;
    /**
     * 进入DApprove界面
     * @return
     */
    @RequestMapping(value="/initAchievementDepart", method= {RequestMethod.GET,RequestMethod.POST})
    @PreAuthorize("hasPermission('/initAchievementDepart','000105')")
    public ModelAndView initAchievementDepart() {
        ModelAndView mod= new ModelAndView("/review/achievementDepart.btl");
        return mod;
    }
    //进入EApprove界面
    @RequestMapping(value="/initAchievementCollege", method= {RequestMethod.GET,RequestMethod.POST})
    @PreAuthorize("hasPermission('/initAchievementCollege','000205')")
    public ModelAndView initAchievementCollege(){
        //列表
        List<SysDepart> departs=departService.getAllDepartOne();
        ModelAndView mod= new ModelAndView("/review/achievementCollege.btl");
        mod.addObject("departs", departs);
        return mod;
    }

    /**
     * 查询本系所有人CApprove的
     * @param pageNo
     * @param maxResults
     * @param achievement
     * @return
     */
    @RequestMapping(value="/queryAchievementDepart", method= {RequestMethod.GET,RequestMethod.POST})
    @PreAuthorize("hasPermission('/queryAchievementDepart','000105000')")
    public Map<String,Object> queryAchievementDepart(RptAchievement achievement, Integer pageNo, Integer maxResults){
        Map<String,Object> rtnMap=new HashMap<>();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Object principal=auth.getPrincipal();
            if (principal != null && principal instanceof MyUserDetails) {
                MyUserDetails usd = (MyUserDetails) principal;
                SysStaff user=(SysStaff)usd.getCustomObj();
                String parentDepart=user.getStaffParentDepart();
                if (null==parentDepart||parentDepart.equals("")) {
                    achievement.setStaffDepart(user.getStaffDepart());
                }else {
                    achievement.setStaffParentDepart(parentDepart);
                }
                achievement.setStatus("CApprove");
            }
            SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
            if (null==cycle) {
                rtnMap.put("mess", "未获取到科研周期！");
                rtnMap.put("total", 0);
                rtnMap.put("rows", null);
            }else {
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
     * 查询本院所有人CApprove的
     * @param achievement
     * @param pageNo
     * @param maxResults
     * @return
     */
    @RequestMapping(value="/queryAchievementCollege", method= {RequestMethod.GET,RequestMethod.POST})
    @PreAuthorize("hasPermission('/queryAchievementCollege','000205000')")
    public Map<String,Object> queryAchievementCollege(RptAchievement achievement,Integer pageNo,Integer maxResults){
        Map<String,Object> rtnMap=new HashMap<>();
        try {
            SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
            if (null==cycle) {
                rtnMap.put("mess", "未获取到科研周期！");
                rtnMap.put("total", 0);
                rtnMap.put("rows", null);
            }else {
                achievement.setStatus("DApprove");
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
     * DApprove通过
     * @param screType
     * @param scieId
     * @param oldStatus
     * @param newStatus
     * @param reason
     * @return
     */
    @RequestMapping(value="/passAchievementDepart", method= {RequestMethod.GET,RequestMethod.POST})
    @PreAuthorize("hasPermission('/passAchievementDepart','000105001')")
    public String passAchievementDepart(Integer achievementId,String memo) {
        String mess="";
        try {
            commonService.passScreStatus(new RptAchievement(), "achievementId", achievementId, "CApprove", "DApprove", memo);
            mess="succ";
        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
            mess=e.toString();
        }
        return mess;
    }
    /**
     * 系拒绝
     * @param screType
     * @param scieId
     * @param newStatus
     * @param memo
     * @return
     */
    @RequestMapping(value="/refuseAchievementDepart", method= {RequestMethod.GET,RequestMethod.POST})
    @PreAuthorize("hasPermission('/refuseAchievementDepart','000105002')")
    public String refuseAchievementDepart(Integer achievementId,String memo) {
        String mess="";
        try {
            commonService.refuseScreStatus(new RptAchievement(), "achievementId", achievementId, "CApprove", "BApprove", memo);
            mess="succ";
        } catch (Exception e) {
            e.printStackTrace();
            mess=e.toString();
        }
        return mess;
    }

    /**
     * EApprove通过
     * @param screType
     * @param scieId
     * @param oldStatus
     * @param newStatus
     * @param reason
     * @return
     */
    @RequestMapping(value="/passAchievementCollege", method= {RequestMethod.GET,RequestMethod.POST})
    @PreAuthorize("hasPermission('/passAchievementCollege','000205001')")
    public String passAchievementCollege(Integer achievementId,String memo) {
        String mess="";
        try {
            commonService.passScreStatus(new RptAchievement(), "achievementId", achievementId, "DApprove", "EApprove", memo);
            mess="succ";
        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
            mess=e.toString();
        }
        return mess;
    }
    /**
     * 院拒绝
     * @param screType
     * @param scieId
     * @param newStatus
     * @param memo
     * @return
     */
    @RequestMapping(value="/refuseAchievementCollege", method= {RequestMethod.GET,RequestMethod.POST})
    @PreAuthorize("hasPermission('/refuseAchievementCollege','000205002')")
    public String refuseAchievementCollege(Integer achievementId,String memo) {
        String mess="";
        try {
            commonService.refuseScreStatus(new RptAchievement(), "achievementId", achievementId, "DApprove", "BApprove", memo);
            mess="succ";
        } catch (Exception e) {
            e.printStackTrace();
            mess=e.toString();
        }
        return mess;
    }

    public HttpSession getSession() {
        return session;
    }
    public void setSession(HttpSession session) {
        this.session = session;
    }

}
