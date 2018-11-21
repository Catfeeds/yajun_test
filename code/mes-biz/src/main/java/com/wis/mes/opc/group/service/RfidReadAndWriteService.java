package com.wis.mes.opc.group.service;

import java.net.UnknownHostException;
import java.util.List;

import org.jinterop.dcom.common.JIException;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.AddFailedException;
import org.openscada.opc.lib.da.DuplicateGroupException;

import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.mes.opc.group.entity.KsGroupItem;
import com.wis.mes.opc.util.OpcServer;

public interface RfidReadAndWriteService {
	void rfidStationWrite(JsonActionResult result, OpcServer server, String sn, List<KsGroupItem> itemList)
			throws Exception;


	void stationNgIn(JsonActionResult result, OpcServer server, String sn, List<KsGroupItem> itemList,
			boolean repairDone) throws Exception;

	String getSnByUlocNo(OpcServer server, List<KsGroupItem> redisItemListByGroupCode, String ulocNo)
			throws IllegalArgumentException, UnknownHostException, JIException, AddFailedException,
			NotConnectedException, DuplicateGroupException;

	void writeSN(OpcServer server, List<KsGroupItem> redisItemListByGroupCode, String ulocNo, String SN)
			throws IllegalArgumentException, DuplicateGroupException, UnknownHostException, JIException,
			AddFailedException, NotConnectedException;
}
