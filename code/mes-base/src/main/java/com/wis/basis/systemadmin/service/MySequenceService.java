package com.wis.basis.systemadmin.service;

import com.wis.basis.systemadmin.entity.MySequence;
import com.wis.core.service.BaseService;

public interface MySequenceService extends BaseService<MySequence>{

	public MySequence doGetSequence(String module,Integer length,String _today);
	
	/**201402010001*/
	public String doGetFormatSequence(String module,Integer length);
	/**20140201_type_0001*/
	public String doSequence(String module,String type,Integer length);

	/**module201402010001*/
	public String doSequence(String module,Integer length);
	
	/**2014code000001*/
	public String doSequence(String code);
	
	/**20140001*/
	public String doSequence(String code,Integer length,String _today);
}
