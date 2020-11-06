/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package edu.hfu.scre.service.review.commonScreService;

import com.hljcj.acts.core.annotation.TestBean;
import com.hljcj.acts.core.template.ActsTestBase;
import edu.hfu.scre.SpringBootTestApplication;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.testng.annotations.Test;
import com.alipay.test.acts.model.PrepareData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import edu.hfu.scre.service.review.CommonScreService;

/**
 *
 * @author lenovo
 * @version $Id: DelScreActsTest.java,v 0.1 2020-11-02 上午 11:02:58 lenovo Exp $
 */
@SpringBootTest(classes = SpringBootTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestExecutionListeners(listeners = MockitoTestExecutionListener.class)
public class DelScreActsTest extends ActsTestBase {

    @TestBean
    @Autowired
    protected CommonScreService commonScreService;

    /**
     * 说明：runTest(caseId, desc, prepareData)脚本中的process方法中的clear,prepare,execute,check四个方法如果无法满足脚本
     * 需求可以进行重写。forExample:
     * public void prepare(ActsRuntimeContext actsRuntimeContext) throws ActsTestException {
     *      userDifined();//自定义数据准备;
     *      super.prepare(actsRuntimeContext);//继承父类数据准备方法
     * }
     *
     * {@link edu.hfu.scre.service.review.CommonScreService#delScre(T, java.lang.String, java.lang.String)}
     */
    @Test(dataProvider = "ActsDataProvider")
    public void delScre
                (String caseId, String desc, PrepareData prepareData) {
        runTest(caseId, prepareData);
    }

    public void setCommonScreService(CommonScreService commonScreService) {
        this.commonScreService = commonScreService;
    }

}
