package edu.hfu.scre.action.sysset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Strings;

import edu.hfu.scre.entity.SysNotice;
import edu.hfu.scre.entity.SysStaff;
import edu.hfu.scre.service.security.MyUserDetails;
import edu.hfu.scre.service.sysset.NoticeService;
import edu.hfu.scre.util.FileProcess;


@RestController
@RequestMapping(path = "/sysset")
public class NoticeAction {
    private static final Logger LOG = LoggerFactory.getLogger(NoticeAction.class);
    @Value("${server.servlet.context-path}") String context;
    @Resource
    NoticeService noticeservice;
    private FileProcess fileProcess = new FileProcess();

    @RequestMapping(path = "/initNotice",method = RequestMethod.GET)
    public ModelAndView initnotice() {
        ModelAndView mod = new ModelAndView("/sysset/notice.btl");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        if (principal != null && principal instanceof MyUserDetails) {
            MyUserDetails usd = (MyUserDetails) principal;
            SysStaff user = (SysStaff) usd.getCustomObj();
            mod.addObject("user", user);
        }
        return mod;
    }

    @RequestMapping(path = "/getAllNotice",method = {RequestMethod.POST,RequestMethod.GET})
    public Map<String,Object> getAllNotice(SysNotice nte,Integer pageNo,Integer maxResults){
        Map<String,Object> rtnMap=new HashMap<>();
        try {
            List<SysNotice> ls=noticeservice.findNoticeByPage(nte, pageNo, maxResults);
            Integer total=noticeservice.findNoticeCountOther(nte);
            rtnMap.put("total", total);
            rtnMap.put("rows", ls);
        } catch (Exception e) {
            e.printStackTrace();
            rtnMap.put("total", 0);
        }
        return rtnMap;
    }
    @RequestMapping(path = "/delNoticeById",method = RequestMethod.POST)
    public Map<String,Object> delNoticeById(Integer noticeId) {
        Map<String,Object> rtnMap=new HashMap<>();
        try {
            String userCode = getUserCodeBySession() ;

            boolean total=noticeservice.delNotice(noticeId);
            String mess="succ";
            rtnMap.put("total", total);
            rtnMap.put("mess", mess);
        } catch (Exception e) {
            e.printStackTrace();
            rtnMap.put("total", 0);
        }
        return rtnMap;
    }

    @RequestMapping(path = "/addNotice", method = RequestMethod.POST)
    public String addNotice(SysNotice nte) {
        String mess="";
        try {
            if (Strings.isNullOrEmpty(nte.getNoticeContent())) {
                return mess="通知内容为空" ;
            }
            String userCode = getUserCodeBySession() ;

            fileProcess.fileMove(nte, userCode, context);
            noticeservice.saveNotice(nte);
            mess="succ";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mess;
    }
    @RequestMapping(path = "/updNotice",method = RequestMethod.POST)
    public String updNotice(SysNotice nte) {
        String mess="";
        try {
            String userCode = getUserCodeBySession() ;
            fileProcess.fileMove(nte, userCode, context);
            noticeservice.updNotice(nte);
            mess="succ";
        } catch (Exception e) {
            e.printStackTrace();
            mess=e.toString();
        }
        return mess;
    }

    public String getUserCodeBySession(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal=auth.getPrincipal();
        String userCode="";
        if (principal != null && principal instanceof MyUserDetails) {
            MyUserDetails usd = (MyUserDetails) principal;
            SysStaff user=(SysStaff)usd.getCustomObj();
            userCode=user.getUserCode();
        }
        return  userCode ;
    }

}
