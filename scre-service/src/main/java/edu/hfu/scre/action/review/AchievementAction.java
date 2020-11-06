package edu.hfu.scre.action.review;

import edu.hfu.scre.entity.RptAchievement;
import edu.hfu.scre.entity.SysScreCycle;
import edu.hfu.scre.entity.SysStaff;
import edu.hfu.scre.entity.xmlBean.SysDictionary;
import edu.hfu.scre.service.review.CommonScreService;
import edu.hfu.scre.service.security.MyUserDetails;
import edu.hfu.scre.service.sysset.DictionaryService;
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
import org.hibernate.exception.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="/review")
public class AchievementAction {
    private static final Logger LOG = LoggerFactory.getLogger(AchievementAction.class);
    @Autowired
    private HttpSession session;
    @Resource
    CommonScreService commonService;
    @Resource
    private DictionaryService dictionaryService;
    /**
     * 科研获奖
     * @return
     */
    @RequestMapping(value="/initAchievement", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/initAchievement','000005')")
    public ModelAndView initAchievement(){
        ModelAndView mod= new ModelAndView("/review/achievement.btl");
        List<SysDictionary> ls=dictionaryService.getDictonaryByType("科研获奖");
        mod.addObject("dicts", ls);
        return mod;
    }
    /**
     * 查询获奖
     * @return
     */
    @RequestMapping(value="/queryAchievement", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/queryAchievement','000005000')")
    public Map<String,Object> queryAchievement(RptAchievement achievement, Integer pageNo, Integer maxResults) {
        LOG.debug("achievementName:"+achievement.getAchievementName());
        Map<String,Object> rtnMap=new HashMap<>();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Object principal=auth.getPrincipal();
            if (principal != null && principal instanceof MyUserDetails) {
                MyUserDetails usd = (MyUserDetails) principal;
                SysStaff user=(SysStaff)usd.getCustomObj();
                achievement.setStaffName(user.getStaffName());
                achievement.setStaffTitle(user.getStaffTitle());
                achievement.setStaffParentDepart(user.getStaffParentDepart());
                achievement.setStaffDepart(user.getStaffDepart());
                achievement.setUserCode(user.getUserCode());
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
            rtnMap.put("total", 0);
            rtnMap.put("rows", null);
            rtnMap.put("mess", e.toString());
        }
        return rtnMap;
    }
    /**
     * 增加
     * @param achievement
     * @return
     */
    @RequestMapping(value="/saveAchievement", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/saveAchievement','000005001')")
    public String  saveAchievement(RptAchievement achievement) {
        LOG.debug("getAchievementType:"+achievement.getAchievementType());
        LOG.debug("getAchievementName:"+achievement.getAchievementName());
        String mess="";
        try {
            SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
            if (null==cycle) {
                mess= "未获取到科研周期！";
            }else {
                achievement.setBelongCycle(cycle.getCycleName());
                mess= commonService.saveScre(achievement);
            }
        } catch (ConstraintViolationException con){
            con.printStackTrace();
            mess="成果名称重复";
        }catch (Exception e) {
            e.printStackTrace();
            mess =e.toString().replace("java.lang.RuntimeException:","");
        }
        return mess;
    }
    /**
     * 修改
     * @param achievement
     * @return
     */
    @RequestMapping(value="/updAchievement", method= {RequestMethod.GET,RequestMethod.POST})
    @PreAuthorize("hasPermission('/updAchievement','000005002')")
    public String updAchievement(RptAchievement achievement) {
        LOG.debug("achievementId:"+achievement.getAchievementId());
        String mess="";
        try {
            mess=commonService.updScre(achievement, achievement.getAchievementId());
        } catch(ConstraintViolationException con) {
            con.printStackTrace();
            mess="修改名称重复";
        }
		catch (Exception e) {
            mess=e.toString();
            e.printStackTrace();
        }
        return mess;
    }

    /**
     * 删除
     * @param achievementId
     * @param memo
     * @return
     */
    @RequestMapping(value="/delAchievement", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/delAchievement','000005006')")
    public Map<String,Object> delAchievement(Integer achievementId,String memo) {
        Map<String,Object> rtnMap=new HashMap<>();
        try {
            RptAchievement achievement=new RptAchievement();
            achievement.setAchievementId(achievementId);
            commonService.delScre(achievement,"achievementId", memo);
            String mess="succ";
            rtnMap.put("mess", mess);
        } catch (Exception e) {
            e.printStackTrace();
            rtnMap.put("mess", e.toString());
        }
        return rtnMap;
    }
    /**
     * 提交作品
     * @param achievementId
     * @param memo
     * @return
     */
    @RequestMapping(value="/submitAchievement", method= {RequestMethod.GET,RequestMethod.POST})
    @PreAuthorize("hasPermission('/submitAchievement','000005003')")
    public Map<String, Object> submitAchievement(Integer achievementId,String memo) {
        Map<String,Object> rtnMap=new HashMap<>();
        try {
            commonService.passScreStatus(new RptAchievement(), "achievementId", achievementId, "BApprove", "CApprove", memo);
            String mess="succ";
            rtnMap.put("mess", mess);
        } catch (Exception e) {
            e.printStackTrace();
            String mess=e.toString();
            rtnMap.put("mess", mess);
        }
        return rtnMap;
    }

    public HttpSession getSession() {
        return session;
    }
    public void setSession(HttpSession session) {
        this.session = session;
    }
}
