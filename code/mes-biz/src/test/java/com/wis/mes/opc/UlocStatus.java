package com.wis.mes.opc;

import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wis.mes.basis.BizBaseTestCase;
import com.wis.mes.master.uloc.entity.TmUlocAbnormal;
import com.wis.mes.master.uloc.service.TmUlocAbnormalService;
import com.wis.mes.webservice.ScanningGunService;

public class UlocStatus extends BizBaseTestCase {
	@Autowired
	private TmUlocAbnormalService ulocAbnormalService;

	@Test
	public void testGetUlocStatus() {
		TmUlocAbnormal eg = new TmUlocAbnormal();
		//eg.setAbnormalCode("0000000000100000");
		eg.setAbnormalName("回原点超时");
		String code = "R5YL0044";
		String name = "回原点超时";
		List<TmUlocAbnormal> findByEg = ulocAbnormalService.findByEg(eg);
		StringBuffer sql = new StringBuffer();
		for (TmUlocAbnormal abnormal : findByEg) {
			String ulocNo = abnormal.getUlocNo();
			String codeContent = ulocNo + " " + abnormal.getAbnormalCode();
			String insertSql = "insert into tm_code_rule(id, code, code_desc, tm_line_id, distinguish, data_source,plc_code)values(seq_code_rule_id.nextval,'"
					+ code + "','" + name + "',162,'Y','L','" + codeContent + "' );";
			sql.append(insertSql).append("\n");
		}
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");
		System.out.println(sql);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	}
	public static void main(String[] args) {
		UlocStatus ulocStatus = new UlocStatus();
		ulocStatus.testClient();
	}
	
	private void testClient() {
		JaxWsDynamicClientFactory factory = JaxWsDynamicClientFactory.newInstance();
		Client client = factory.createClient("http://localhost:8080/mes-webapp/webservice/scanningGun?wsdl");
		try {
			Object[] invoke = client.invoke("webServiceTest");
			System.out.println(invoke[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
