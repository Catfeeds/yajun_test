package com.wis.mes.rfid.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alien.enterpriseRFID.reader.AlienClass1Reader;
import com.alien.enterpriseRFID.reader.AlienReaderCommandErrorException;
import com.alien.enterpriseRFID.reader.AlienReaderConnectionException;
import com.alien.enterpriseRFID.reader.AlienReaderConnectionRefusedException;
import com.alien.enterpriseRFID.reader.AlienReaderNotValidException;
import com.alien.enterpriseRFID.reader.AlienReaderTimeoutException;
import com.wis.basis.common.utils.LogConstant;
import com.wis.core.framework.service.exception.BusinessException;

public class RfidConnectionUtil {

	private static Log rfidLogger = LogFactory.getLog(LogConstant.RFID_LOG);
	
	private static Map<String, AlienClass1Reader> writeReaderMap = new ConcurrentHashMap<String, AlienClass1Reader>();
	
	/**7工位RFID,用于12数采服务器***/
	public static AlienClass1Reader writeConnection(String netWorkAddress, Integer netWorkPort,Integer RfidAnt){
		String key = netWorkAddress+netWorkPort;
		try {
				if (null != writeReaderMap && writeReaderMap.containsKey(key)) {
					AlienClass1Reader alienClass1Reader = writeReaderMap.get(key);
					if (!alienClass1Reader.isOpen() || !alienClass1Reader.isValidateOpen()) {
						alienClass1Reader.close();
						alienClass1Reader.open();
						writeReaderMap.put(key, alienClass1Reader);
					}
				} else {
					AlienClass1Reader alienClass1Reader = new AlienClass1Reader(netWorkAddress, netWorkPort);
					alienClass1Reader.open();
					writeReaderMap.put(key, alienClass1Reader);
				}
				inintCommand(writeReaderMap.get(key),RfidAnt);
				writeReaderMap.get(key).doReaderCommand(RfidUtil.readLine(RfidUtil.readLine("clear")));
				writeReaderMap.get(key).doReaderCommand(RfidUtil.readLine(RfidUtil.ACQG2MASK_RESETTING));
				writeReaderMap.get(key).doReaderCommand(RfidUtil.readLine(RfidUtil.readLine("Rfattenuation = 0 40 10")));
		} catch (AlienReaderConnectionRefusedException e) {
			newWriteReader();
			rfidLogger.error("RFID连接被拒绝："+ExceptionUtils.getStackTrace(e));
			throw new BusinessException("RFID连接被拒绝");
		} catch (AlienReaderNotValidException e) {
			newWriteReader();
			rfidLogger.error("RFID验证失败："+ExceptionUtils.getStackTrace(e));
			throw new BusinessException("RFID验证失败");
		} catch (AlienReaderTimeoutException e) {
			newWriteReader();
			rfidLogger.error("RFID连接超时："+ExceptionUtils.getStackTrace(e));
			throw new BusinessException("RFID连接超时");
		} catch (AlienReaderConnectionException e) {
			newWriteReader();
			rfidLogger.error("RFID连接异常："+ExceptionUtils.getStackTrace(e));
			throw new BusinessException("RFID连接失败");
		} catch (AlienReaderCommandErrorException e) {
			rfidLogger.error("RFID命令错误："+ExceptionUtils.getStackTrace(e));
			throw new BusinessException("RFID命令错误");
		} catch (Exception e) {
			rfidLogger.error("RFID未知："+ExceptionUtils.getStackTrace(e));
			throw new BusinessException("RFID请求异常");
		}
		return writeReaderMap.get(key);
	}
	private static void newWriteReader(){
		writeReaderMap = new ConcurrentHashMap<String, AlienClass1Reader>();
	}
	private static void inintCommand(AlienClass1Reader reader,Integer RfidAnt) throws Exception{
			reader.doReaderCommand(RfidUtil.readLine(RfidUtil.Custom));
			reader.doReaderCommand(RfidUtil.readLine(RfidUtil.TAG_LIST_CUSTOM_FORMAT));
			reader.doReaderCommand(RfidUtil.readLine(RfidUtil.appointAntRead(RfidAnt)));
			reader.doReaderCommand(RfidUtil.readLine(RfidUtil.appointAntWrite(RfidAnt)));
	} 
	
	public static void main(String[] args) {
		AlienClass1Reader reader = new AlienClass1Reader("192.168.90.163",23);//65535
		try {
			reader.open();
			System.out.println(reader.doReaderCommand(RfidUtil.readLine("info")));
			//System.out.println(reader.doReaderCommand(RfidUtil.readLine("NetworkTimeout=65535")));
			//System.out.println(reader.doReaderCommand(RfidUtil.readLine("save")));
			//reader.doReaderCommand(RfidUtil.readLine(RfidUtil.Custom));
			//reader.doReaderCommand(RfidUtil.readLine(RfidUtil.TAG_LIST_CUSTOM_FORMAT));
			//reader.doReaderCommand(RfidUtil.readLine(RfidUtil.ACQG2MASK_RESETTING));
			//reader.doReaderCommand(RfidUtil.readLine(RfidUtil.appointAntRead(0)));
			//reader.doReaderCommand(RfidUtil.readLine(RfidUtil.appointAntWrite(0)));
			//System.out.println(reader.doReaderCommand(RfidUtil.readLine("t")));
			//System.out.println(reader.doReaderCommand(RfidUtil.readLine("g2read = 3 0 11")));
//			RfidUtil.writeUser(reader, "702194886F003748331115", 0);
			//System.out.println(RfidUtil.readUser(reader, 0));
		} catch (AlienReaderConnectionRefusedException e) {
			e.printStackTrace();
		} catch (AlienReaderNotValidException e) {
			e.printStackTrace();
		} catch (AlienReaderTimeoutException e) {
			e.printStackTrace();
		} catch (AlienReaderConnectionException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			reader.close();
		}
	}
}