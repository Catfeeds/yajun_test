package com.wis.mes.basis;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;

@ContextConfiguration(locations = { 
		"classpath:applicationContext-aop.xml",
		"classpath:applicationContext-common.xml",
		"classpath:applicationContext-datasource.xml", 
		"classpath:applicationContext-redis.xml",
		"classpath:applicationContext-job.xml" })
@ActiveProfiles("test")
@TestExecutionListeners({ TransactionDbUnitTestExecutionListener.class })
public abstract class BizBaseTestCase extends AbstractJUnit4SpringContextTests {

}
