package edu.hfu.scre.action.review;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import edu.hfu.scre.entity.SysReviewLog;
import edu.hfu.scre.service.review.CommonScreService;

@RestController
@RequestMapping(value="/review")
public class ReviewLogAction {
    @Resource
    CommonScreService commonService;

    @RequestMapping(value="/initReviewLog", method= {RequestMethod.GET,RequestMethod.POST})
    @PreAuthorize("hasPermission('/initReviewLog','000002005,000001005,000004005,000000005,000005005,000003005')")
    public ModelAndView initReviewLog(String tblName,Integer screId){
        ModelAndView mod= new ModelAndView("/review/reviewLog.btl");
        try {
            List<SysReviewLog> ls=commonService.findLog(tblName,screId);
            mod.addObject("log", ls);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mod;
    }


    /**
     * 获取审批记录
     * @param scieId
     * @return
     */
    public Map<String, Object> getReviewLog(String tblName,Integer scieId) {
        Map<String,Object> rtnMap=new HashMap<>();
        try {
            List<SysReviewLog> ls=commonService.findLog(tblName,scieId);
            rtnMap.put("total", ls.size());
            rtnMap.put("rows", ls);
        } catch (Exception e) {
            e.printStackTrace();
            rtnMap.put("total", 0);
            rtnMap.put("rows", null);
        }
        return rtnMap;
    }
}
