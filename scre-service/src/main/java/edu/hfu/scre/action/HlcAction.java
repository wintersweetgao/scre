package edu.hfu.scre.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import edu.hfu.scre.entity.RptAchievementMaterial;
import edu.hfu.scre.entity.RptPaperMaterial;
import edu.hfu.scre.entity.SysNotice;
import edu.hfu.scre.entity.ViewscreAchieve;
import edu.hfu.scre.service.review.CommonScreService;
import edu.hfu.scre.service.review.MaterialService;
import edu.hfu.scre.service.sysset.NoticeService;

@RestController
@RequestMapping(value = "/")
public class HlcAction {
	@Resource
	NoticeService noticeservice;
	@Autowired
	private HttpSession session;
	@Resource
	CommonScreService commonService;
	@Resource
	MaterialService materialService;
	private static final Logger LOG = LoggerFactory.getLogger(HlcAction.class);
	//首页公告显示
	@RequestMapping(value = "/getNoticee", method = { RequestMethod.POST, RequestMethod.GET })
	public Map<String, Object> getNoticee(SysNotice nte, Integer pageNo, Integer maxResults) {
		
		Map<String, Object> rtnMap = new HashMap<>();
		try {
			pageNo = 1;
			maxResults = 6;
			List<SysNotice> ls = noticeservice.findNoticeByPage(nte, pageNo, maxResults);
			rtnMap.put("rows", ls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtnMap;
	}
	//首页各系展示
	@RequestMapping(value="/queryViewExhibition", method= {RequestMethod.GET,RequestMethod.POST})
	public Map<String,Object> queryViewExhibition(ViewscreAchieve view){
		Map<String,Object> rtnMap=new HashMap<>();
		try {  	
			view.setRecommend("1");
			List<Map<String, Object>> rows=commonService.getScreStatistics(view);
			LOG.debug("message:"+rows);
			rtnMap.put("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtnMap;
	}
	//显示系内容更多
	@RequestMapping(value="/queryViewExhall", method= {RequestMethod.GET,RequestMethod.POST})
	public Map<String,Object> queryViewExhall(ViewscreAchieve view, Integer pageNo, Integer maxResults){
		Map<String,Object> rtnMap=new HashMap<>();
		try {  	
				view.setRecommend("1");
				List<Map<String, Object>> rows=commonService.getScreStatisticsall(view, pageNo, maxResults);
				Integer total=commonService.findViewscreAchieveCount(view);
				rtnMap.put("total", total);
				LOG.debug("message:"+rows);
				rtnMap.put("rows", rows);
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return rtnMap;
	}
	
	//显示系内容
//	@RequestMapping(value="/showRptPageDetail", method= {RequestMethod.GET,RequestMethod.POST})
//	public 	 Map<String,Object> showRptPageDetail(String rptId,String className) {
//		Map<String,Object> rtnMap=new HashMap<>();
//		try {
//			List<Map<String, Object>> rows = materialService.getMaterialById(className, rptId);
//			rtnMap.put("rows", rows);
//		} catch (Exception e) {
//			
//			e.printStackTrace();
//		}
//		return rtnMap;
//	}
	//跳转各系内容页
	@RequestMapping(path = "/getexcellence", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView getexcellence(String rptId,String className) throws Exception {
		ModelAndView mod = new ModelAndView("/excellence.btl");
		try {
			List<Map<String, Object>> rows = materialService.getMaterialById(className, rptId);
			mod.addObject("rows", rows);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
		return mod;
	}
	

	//首页公告更多显示
	@RequestMapping(value = "/getNoticeshou", method = { RequestMethod.POST, RequestMethod.GET })
	public Map<String, Object> getNoticeshou(SysNotice nte, Integer pageNo, Integer maxResults) {
		
		Map<String, Object> rtnMap = new HashMap<>();
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
	//跳转公告内容更多页面
	@RequestMapping(path = "/getNoticeall", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView getNoticeall() throws Exception {
		ModelAndView mod = new ModelAndView("/gonggaoall.btl");
		return mod;
	}
//	//跳转优秀作品内容更多页面
//	@RequestMapping(path = "/getWorksall", method = { RequestMethod.POST, RequestMethod.GET })
//	public ModelAndView getWorksall() throws Exception {
//		ModelAndView mod = new ModelAndView("/works.btl");
//		return mod;
//	}
	//跳转公告内容
	@RequestMapping(path = "/idxNotice", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView findNoticeById(SysNotice nte) throws Exception {
		ModelAndView mod = new ModelAndView("/gonggao.btl");  
		List<SysNotice> ls = noticeservice.findNoticeById(nte);				
		SysNotice sysNotice = ls.size() == 0 ? null: ls.get(0);
		//阅读量
		if(sysNotice != null) {
			sysNotice.setNoticeReadvolume(sysNotice.getNoticeReadvolume()+1);	
			noticeservice.updNotice(sysNotice);	
		}		
		mod.addObject("notice", sysNotice);
		return mod;
	}
	@RequestMapping(path = "/getNoticeById", method = { RequestMethod.POST, RequestMethod.GET })
	public Map<String, Object> getNoticeById(SysNotice nte) {
		Map<String, Object> rtnMap = new HashMap<>();
		try {
			List<SysNotice> ls = noticeservice.findNoticeById(nte);
			rtnMap.put("rows", ls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtnMap;
	}
	//财信系更多
	@RequestMapping(path = "/excellenceWorks/excellenceCaiXin", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView excellenceCaiXin() throws Exception {
		ModelAndView mod = new ModelAndView("/excellenceWorks/excellenceCaiXin.btl");
		return mod;
	}
	//管理系更多
	@RequestMapping(path = "/excellenceWorks/excellenceGuanLi", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView excellenceGuanLi() throws Exception {
		ModelAndView mod = new ModelAndView("/excellenceWorks/excellenceGuanLi.btl");
		return mod;
	}
	//会计系更多
	@RequestMapping(path = "/excellenceWorks/excellenceKuaiJi", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView excellenceKuaiJi() throws Exception {
		ModelAndView mod = new ModelAndView("/excellenceWorks/excellenceKuaiJi.btl");
		return mod;
	}
	//经济系更多
	@RequestMapping(path = "/excellenceWorks/excellenceJingJi", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView excellenceJingJi() throws Exception {
		ModelAndView mod = new ModelAndView("/excellenceWorks/excellenceJingJi.btl");
		return mod;
	}
	//艺术系更多
	@RequestMapping(path = "/excellenceWorks/excellenceYiShu", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView excellenceYiShu() throws Exception {
		ModelAndView mod = new ModelAndView("/excellenceWorks/excellenceYiShu.btl");
		return mod;
	}
	//人文系更多
	@RequestMapping(path = "/excellenceWorks/excellenceRenWen", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView excellenceRenWen() throws Exception {
		ModelAndView mod = new ModelAndView("/excellenceWorks/excellenceRenWen.btl");
		return mod;
	}
	
	
	
}