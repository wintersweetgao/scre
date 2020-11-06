package service;

import edu.hfu.scre.ScreManagerApplication;
import edu.hfu.scre.dao.review.AcademicDao;
import edu.hfu.scre.entity.AuthGrant;
import edu.hfu.scre.entity.RptAcademic;
import edu.hfu.scre.entity.RptPaper;
import edu.hfu.scre.service.review.CommonScreService;
import edu.hfu.scre.service.security.UserGrantService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScreManagerApplication.class)
@TestPropertySource(locations = "classpath:application.properties")
@ActiveProfiles("test")
public class ServiceTest {
	@Resource
	private UserGrantService userGrantService;
	@Resource
    AcademicDao academicDao;
	@Resource
    CommonScreService common;
	
	public void  testGrantService() {
		try {
			List<AuthGrant> ls=userGrantService.getAllGrant();
			if (null==ls) {
				System.out.println("ddd");
			}else {
				for(AuthGrant grant:ls) {
					System.out.println(grant.getGrantCode());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Transactional
	public void testAcademicDao(){
		try {
			RptAcademic academic=new RptAcademic();
			academic.setAcademicId(1);
			academic.setExpectedMark(1);
			List<RptAcademic> ls=academicDao.getAcademicByCon(academic, 1, 10);
			System.out.println(ls.size());
			int res=academicDao.getAcademicCountByCon(academic);
			System.out.println(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testCommonSave() {
		try {
			RptPaper paper=new RptPaper();
			paper.setBelongCycle("第一");
			paper.setInOrder(1);
			paper.setScreType("科研论文");
			paper.setPaperThesis("论文题目");
			paper.setScreTopic("中文核心期刊");
			paper.setPaperJournal("发表期刊名称");
			paper.setPaperSponsor("期刊名");
			paper.setStaffName("高冬梅");
			System.out.println(common.saveScre(paper));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void testCommonUpd() {
		try {
			RptPaper paper=new RptPaper();
			paper.setPaperId(6);
			paper.setBelongCycle("第一");
			paper.setInOrder(1);
			paper.setScreType("科研论文");
			paper.setPaperThesis("论文题目");
			paper.setScreTopic("中文核心期刊");
			paper.setPaperJournal("发表期刊名称");
			paper.setPaperSponsor("期刊名");
			paper.setStaffName("高冬梅");
			System.out.println(common.updScre(paper,6));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testCommonDel() {
		try {
			RptPaper paper=new RptPaper();
			paper.setPaperId(8);
			common.delScre(paper,"paperId","");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
