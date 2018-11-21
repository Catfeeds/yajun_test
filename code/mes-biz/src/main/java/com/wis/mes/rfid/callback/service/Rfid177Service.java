package com.wis.mes.rfid.callback.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.alien.enterpriseRFID.reader.AlienClass1Reader;
import com.alien.enterpriseRFID.reader.AlienReaderConnectionException;
import com.alien.enterpriseRFID.reader.AlienReaderConnectionRefusedException;
import com.alien.enterpriseRFID.reader.AlienReaderNotValidException;
import com.alien.enterpriseRFID.reader.AlienReaderTimeoutException;
import com.wis.basis.common.utils.LogConstant;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.rfid.callback.IRfidCallBack;
import com.wis.mes.rfid.util.RfidUtil;
import com.wis.mes.rfid.vo.RfidReadVo;
import com.wis.mes.rfid.vo.TagVo;

@Component(value="rfid177Service")
public class Rfid177Service implements IRfidCallBack{
	
	private static AlienClass1Reader reader = null;
	private Log rfidLogger = LogFactory.getLog(LogConstant.RFID_LOG);
	
	@Override
	public synchronized RfidReadVo getRfidReadVo(TmUloc uloc) throws Exception {
		AlienClass1Reader alienClass1Reader = null;
		RfidReadVo rfidRead = new RfidReadVo();
		rfidRead.setTmUloc(uloc);
		alienClass1Reader = createConnection(uloc);
		TagVo rfidReadTagVo = null;
		for(int i=0;i<5;i++) {
			rfidReadTagVo = RfidUtil.rfidRead(alienClass1Reader);
			if(null != rfidReadTagVo) {
				break;
			}
		}
		if (null != rfidReadTagVo && StringUtils.isNotBlank(rfidReadTagVo.getUserTagId())) {
			rfidRead.setSn(rfidReadTagVo.getUserTagId());
			rfidRead.setEpcInfo(rfidReadTagVo.getEpcTagInfo());
		}else {
			rfidRead.setMessage("获取标签信息失败，请检查标签是否到位");
		}
		return rfidRead;
	}

	@Override
	public AlienClass1Reader createConnection(TmUloc uloc) {
		try {
			if(null != reader) {
				if(!reader.isOpen() || !reader.isValidateOpen()) {
					reader.close();
					reader.open();
				}
			}else {
				reader = new AlienClass1Reader(uloc.getRfidIP(),uloc.getRfidPort());
				reader.open();
				reader.doReaderCommand(RfidUtil.readLine(RfidUtil.Custom));
				reader.doReaderCommand(RfidUtil.readLine(RfidUtil.TAG_LIST_CUSTOM_FORMAT));
			}
			reader.doReaderCommand(RfidUtil.readLine(RfidUtil.readLine("clear")));
			reader.doReaderCommand(RfidUtil.readLine(RfidUtil.ACQG2MASK_RESETTING));
			reader.doReaderCommand(RfidUtil.readLine(RfidUtil.readLine("Rfattenuation = 0 40 10")));
			reader.doReaderCommand(RfidUtil.readLine(RfidUtil.appointAntRead(uloc.getRfidAnt())));
			reader.doReaderCommand(RfidUtil.readLine(RfidUtil.appointAntWrite(uloc.getRfidAnt())));
		} catch (AlienReaderConnectionRefusedException e) {
			reader=null;
			rfidLogger.error(uloc.getRfidIP()+"  RFID连接被拒绝："+ExceptionUtils.getStackTrace(e));
			throw new BusinessException("RFID连接被拒绝");
		} catch (AlienReaderNotValidException e) {
			reader=null;
			rfidLogger.error(uloc.getRfidIP()+"  RFID验证失败："+ExceptionUtils.getStackTrace(e));
			throw new BusinessException("RFID验证失败");
		} catch (AlienReaderTimeoutException e) {
			reader=null;
			rfidLogger.error(uloc.getRfidIP()+"  RFID连接超时："+ExceptionUtils.getStackTrace(e));
			throw new BusinessException("RFID连接超时");
		} catch (AlienReaderConnectionException e) {
			reader=null;
			rfidLogger.error(uloc.getRfidIP()+"  RFID连接异常："+ExceptionUtils.getStackTrace(e));
			throw new BusinessException("RFID连接失败");
		}  catch (Exception e) {
			reader=null;
			rfidLogger.error(uloc.getRfidIP()+"  RFID未知："+ExceptionUtils.getStackTrace(e));
			throw new BusinessException("RFID请求异常");
		}
		return reader;
	}
}
