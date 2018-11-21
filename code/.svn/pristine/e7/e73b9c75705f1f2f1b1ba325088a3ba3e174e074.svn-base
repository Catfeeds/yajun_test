package com.wis.mes.rfid.callback;

import com.alien.enterpriseRFID.reader.AlienClass1Reader;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.rfid.vo.RfidReadVo;

public interface IRfidCallBack {

	/**
	 * @author yajun_ren
	 * @param TmUloc uloc
	 * @return AlienClass1Reader
	 * */
	AlienClass1Reader createConnection(TmUloc uloc);
	
	/**
	 * @author yajun_ren
	 * @param TmUloc uloc
	 * @return RfidReadVo
	 * */
	RfidReadVo getRfidReadVo(TmUloc uloc) throws Exception;
}
