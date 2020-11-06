
package edu.hfu.scre.service.review.commonScreService;

import com.hljcj.acts.core.annotation.TestBean;
import com.hljcj.acts.core.runtime.ActsRuntimeContext;
import com.hljcj.acts.core.template.ActsTestBase;
import edu.hfu.scre.SpringBootTestApplication;
import edu.hfu.scre.entity.SysStaff;
import edu.hfu.scre.service.security.MyUserDetails;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestExecutionListeners;
import org.testng.annotations.Test;

import com.alipay.test.acts.model.PrepareData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import edu.hfu.scre.service.review.CommonScreService;

import java.security.Principal;
import java.util.ArrayList;


/**
 *
 * @author lenovo
 * @version $Id: SaveScreActsTest.java,v 0.1 2020-10-29 下午 03:25:25 lenovo Exp $
 */
@SpringBootTest(classes = SpringBootTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestExecutionListeners(listeners = MockitoTestExecutionListener.class)
public class SaveScreActsTest extends ActsTestBase {

    @TestBean
    @Autowired
    protected CommonScreService commonScreService;

    @Mock
    SecurityContext context;
    @Mock
    Authentication authentication;
    @Mock
    Principal principal;

    /**
     * 说明：runTest(caseId, desc, prepareData)脚本中的process方法中的clear,prepare,execute,check四个方法如果无法满足脚本
     * 需求可以进行重写。forExample:
     * public void prepare(ActsRuntimeContext actsRuntimeContext) throws ActsTestException {
     *      userDifined();//自定义数据准备;
     *      super.prepare(actsRuntimeContext);//继承父类数据准备方法
     * }
     *
     * {@link edu.hfu.scre.service.review.commonScreService#saveScre(T)}
     */
    @Test(dataProvider = "ActsDataProvider")
    public void saveScre
                (String caseId, String desc, PrepareData prepareData) {
        runTest(caseId, prepareData);
    }

    @Override
    public void beforeActsTest(ActsRuntimeContext actsRuntimeContext) {
        super.beforeActsTest(actsRuntimeContext);

        SysStaff sysStaff = new SysStaff();
        sysStaff.setStaffName("高冬梅");
        sysStaff.setStaffTitle("讲师(中)级");
        sysStaff.setStaffParentDepart("财经信息工程系");
        sysStaff.setStaffDepart("计算机科学与技术教研室");
        sysStaff.setUserCode("2019190036");

        MyUserDetails myUserDetails = new MyUserDetails("2019190036","admin",new ArrayList<>(), sysStaff);

        Mockito.when(context.getAuthentication()).thenReturn(authentication);
        Mockito.when(context.getAuthentication().getPrincipal()).thenReturn(myUserDetails);
        SecurityContextHolder.setContext(context);
    }
    public void setCommonScreService(CommonScreService commonScreService) {
        this.commonScreService = commonScreService;
    }
}
