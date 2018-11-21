package com.wis.mes.rfid.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import com.wis.mes.rfid.vo.TagVo;
import com.wis.mes.util.StringUtil;

public class RfidUtil {
	private static Log logger = LogFactory.getLog(LogConstant.RFID_LOG);
	public final static String sign = "=";
	public final static String comma = ",";
	/** USER起始地址 **/
	public final static String G2READ_0 = "0";
	/** EPC区域 **/
	public final static String G2READ_1 = "1";
	/** EPC起始地址 **/
	public final static String G2READ_2 = "2";
	/** USER区域 **/
	public final static String G2READ_3 = "3";
	/** 指定EPC起始位置 **/
	public final static String G2READ_32 = "32";

	/****
	 * 自定义
	 **/
	public final static String Custom = "TagListFormat = Custom";
	/****
	 * 获取taglist
	 **/
	public final static String ALL_T = "t";
	/****
	 * %k（EPC标签号）,%m（信号轻度）; 设置获取参数
	 **/
	public final static String TAG_LIST_CUSTOM_FORMAT = "taglistcustomformat = %k,%m;";
	/****
	 * acqg2mask = 1,32,96,E2 00 00 17 31 05 02 68 12 00 A2 93 //指定EPC（） 32 起始位置 1
	 * EPC 96 tagID长度 1个数字是4个字节
	 * 
	 **/
	public final static String ACQG2MASK = "acqg2mask";
	/****
	 * g2read = 1 2 6（1、EPC区域，2、起始地址，6、长度）
	 **/
	public final static String G2READ_EPC = "g2read  = 1 2 6";
	/****
	 * g2read = 3 0 6（1、USER区域，2、起始地址，3、长度）
	 **/
	public final static String G2READ_USER = "g2read  = 3 0 11";
	/****
	 * g2write = 3 0 11 22 33 44 55 66 77 88 写入（3、USER区域，2、起始地址，6、长度）
	 **/
	public final static String G2WRITE_USER = "g2write = 3 0 ";

	/****
	 * acqg2mask = 00(重置EPC指定)
	 **/
	public final static String ACQG2MASK_RESETTING = "acqg2mask = 00";
	/****
	 * ant = 0
	 **/
	public final static String CUT_ANT = "ant";
	/****
	 * ProgAntenna = 0（指定写天线）
	 **/
	public final static String PROGANTENNA = "ProgAntenna";
	/****
	 * RFAttenuation = 0 60(0、天线号，60、衰减功率，) save
	 **/
	public final static String RFAttenuation = "RFAttenuation";

	public final static String NOTAGS = "(No Tags)";

	/****
	 * tagId格式设置
	 **/
	private static String tagIdFormat(String tagId) {
		if (StringUtils.isNotBlank(tagId)) {
			StringBuffer buffer = new StringBuffer(tagId.trim());
			int index;
			for (index = 2; index < buffer.length(); index += 3) {
				buffer.insert(index, ' ');
			}
			return buffer.toString();
		}
		return "";
	}

	/****
	 * 指定EPC命令
	 * 
	 * @throws Exception
	 **/
	public static void appointAcqg2mask(AlienClass1Reader reader, String tagId) throws Exception {
		if (StringUtils.isNotBlank(tagId)) {
			StringBuffer buffer = new StringBuffer();
			buffer.append(ACQG2MASK);
			buffer.append(sign);
			buffer.append(G2READ_1 + comma);
			buffer.append(G2READ_32 + comma);
			buffer.append(tagId.trim().length() * 4 + comma);// 一个数字或字母等于一个字节，一个字节等于四个字位。
			buffer.append(tagIdFormat(tagId));
			logger.info("指定EPC命令：" + buffer.toString());
			reader.doReaderCommand(readLine(buffer.toString()));
		}
	}

	/****
	 * 读取当前USER区
	 * 
	 * @throws Exception
	 **/
	public static String readUser(AlienClass1Reader reader, Integer num) throws Exception {
		String src = "";
		try {
			src = reader.doReaderCommand(readLine(G2READ_USER));
		} catch (AlienReaderCommandErrorException e) {
			if (num < 5) {
				num++;
				readUser(reader, num);
			} else {
				throw new BusinessException("RFID命令错误。");
			}
		}
		src = src.replace("G2Read = ", "");
		return unicode2String(src);
	}

	/****
	 * 写入USER区
	 * 
	 * @throws InterruptedException
	 * @throws Exception
	 **/
	public static void writeUser(AlienClass1Reader reader, String tagId, Integer num) throws Exception {
		if (StringUtils.isNotBlank(tagId)) {
			try {
				String g2writeUser = readLine(G2WRITE_USER + string2Unicode(tagId));
				reader.doReaderCommand(g2writeUser);
				logger.info("writeUser  " + g2writeUser + "  t" + rfidAllt(reader, 0));
				String userNo = readUser(reader, 0);
				if (!userNo.equals(tagId)) {
					throw new BusinessException("写入的SN与输入的SN不一致。");
				}
			} catch (AlienReaderCommandErrorException e) {
				logger.error("writeUser>>SN:" + tagId + "  number:" + num + "   e:" + e.getMessage());
				logger.info("writeUser t" + rfidAllt(reader, 0));
				if (num < 20) {
					num++;
					Thread.sleep(100);
					writeUser(reader, tagId, num);
				} else {
					throw new BusinessException("RFID命令错误。");
				}
			}
		}
	}

	public static String rfidAllt(AlienClass1Reader reader, Integer num) throws Exception {
		String epcTagInfo = "";
		try {
			epcTagInfo = reader.doReaderCommand(RfidUtil.readLine(RfidUtil.ALL_T));
		} catch (AlienReaderCommandErrorException e) {
			if (num < 5) {
				num++;
				rfidAllt(reader, num);
			}
		}
		return epcTagInfo;
	}

	/****
	 * 指定天线命令 只能读取
	 * 
	 * @throws Exception
	 **/
	public static String appointAntRead(Integer rfidAnt) throws Exception {
		if (null != rfidAnt) {
			String cutAnt = CUT_ANT + sign + rfidAnt;
			logger.info("appointAntRead 指定天线命令 只能读取 " + cutAnt);
			return readLine(cutAnt);
		}
		return "";
	}

	/****
	 * 指定天线命令 写入
	 * 
	 * @throws Exception
	 **/
	public static String appointAntWrite(Integer rfidAnt) throws Exception {
		if (null != rfidAnt) {
			String progantenna = PROGANTENNA + sign + rfidAnt;
			logger.info("appointAntWrite 指定天线命令 写入 " + progantenna);
			return readLine(progantenna);
		}
		return "";
	}

	public static String readLine(String command) throws Exception {
		InputStream stram = new ByteArrayInputStream(command.getBytes());
		BufferedReader in = new BufferedReader(new InputStreamReader(stram));
		return in.readLine();
	}

	/**
	 * 字符串转换为16进制字符串
	 * 
	 * @param s
	 * @return
	 */
	public static String stringToHexString(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch);
			str = str + s4;
		}
		return str;
	}

	/**
	 * 16进制字符串转换为字符串
	 * 
	 * @param s
	 * @return
	 */
	public static String hexStringToString(String s) {
		if (s == null || s.equals("")) {
			return null;
		}
		s = s.replace(" ", "");
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "gbk");
			new String();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}

	/**
	 * 字符串转换unicode
	 */
	public static String string2Unicode(String string) {
		StringBuffer unicode = new StringBuffer();
		for (int i = 0; i < string.length(); i++) {
			// 取出每一个字符
			char c = string.charAt(i);
			// 转换为unicode
			unicode.append(Integer.toHexString(c) + " ");
		}
		if (null != unicode && unicode.length() > 0) {
			unicode = unicode.deleteCharAt(unicode.length() - 1);
		}
		return unicode.toString();
	}

	/**
	 * unicode 转字符串
	 */
	public static String unicode2String(String unicode) {
		StringBuffer string = new StringBuffer();
		if (StringUtils.isNotBlank(unicode)) {
			String[] hex = unicode.split(" ");
			for (int i = 0; i < hex.length; i++) {
				// 转换出每一个代码点
				if (StringUtil.isNotBlank(hex[i]) && !hex[i].equals("00")) {
					int data = Integer.parseInt(hex[i], 16);
					// 追加成string
					string.append((char) data);
				}
			}
		}
		return string.toString();
	}

	/**** 将字符串转成ASCII码 **/
	public static String stringToAscii(String value, String blackCharacter) {
		if (StringUtils.isNotBlank(value)) {
			StringBuffer sbu = new StringBuffer();
			char[] chars = value.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				if (i != chars.length - 1) {
					sbu.append((int) chars[i]).append(blackCharacter);
				} else {
					sbu.append((int) chars[i]);
				}
			}
			return sbu.toString();
		}
		return "";
	}

	/*** 将ASCII转成字符串的 **/
	public static String asciiToString(String value) {
		if (StringUtils.isNotBlank(value)) {
			StringBuffer sbu = new StringBuffer();
			String[] chars = value.split(" ");
			for (int i = 0; i < chars.length; i++) {
				sbu.append((char) Integer.parseInt(chars[i]));
			}
			return sbu.toString();
		}
		return "";
	}

	public static TagVo rfidReadAnts(AlienClass1Reader reader, String ant) throws Exception {
		Map<Double, TagVo> map = new HashMap<Double, TagVo>();
		List<TagVo> tagVoList = rfidInfoSplit(reader, ant);
		TagVo tagVo = null;
		if (null != tagVoList && tagVoList.size() > 0) {
			Double[] signalIntensitys = new Double[tagVoList.size()];
			for (int i = 0; i < tagVoList.size(); i++) {
				Double signal = Double.valueOf(tagVoList.get(i).getSignalIntensity());
				signalIntensitys[i] = signal;
				map.put(signal, tagVoList.get(i));
			}
			MathUtils.Pair pair = MathUtils.minmax(signalIntensitys);
			tagVo = map.get(pair.getMax());
			appointAcqg2mask(reader, tagVo.getEpcId());// 指定epc区域
			String userAreaNo = RfidUtil.readUser(reader, 0);// 获取当前USER区SN编码，系统中验证是否符合写入条件
			tagVo.setUserTagId(userAreaNo);
		}
		return tagVo;
	}

	public static List<TagVo> rfidInfoSplit(AlienClass1Reader reader, String ant) throws Exception {
		// 读取标签信息
		String src = RfidUtil.rfidAllt(reader, 0);
		// String src = "E200 9A60 2003 0AF0 0000 4011,0,-39.0;E200 9A60 2003 0AF0 0000
		// 4011,0,-39.0;E200 9A60 2003 0AF0 0000 4012,1,-38.0;E200 9A60 2003 0AF0 0000
		// 4012,2,-40.0;";
		Map<String, List<TagVo>> map = new HashMap<String, List<TagVo>>();
		if (StringUtils.isNotBlank(src)) {
			String[] tagGroups = src.split(";");
			for (String tagItem : tagGroups) {
				String[] tagItems = tagItem.split(",");
				if (map.containsKey(tagItems[1])) {
					map.get(tagItems[1]).add(TagVo.createTagVo(tagItems[0], tagItems[1], tagItems[2], tagItem));
				} else {
					map.put(tagItems[1], new ArrayList<TagVo>());
					map.get(tagItems[1]).add(TagVo.createTagVo(tagItems[0], tagItems[1], tagItems[2], tagItem));
				}
			}
		}
		return map.containsKey(ant) ? map.get(ant) : null;
	}

	public static TagVo rfidRead(AlienClass1Reader reader) throws Exception {
		// 读取标签信息
		String epcTagInfo = RfidUtil.rfidAllt(reader, 0);
		Map<Double, String[]> map = new HashMap<Double, String[]>();
		TagVo tagVo = null;
		if (StringUtils.isNotBlank(epcTagInfo) && !RfidUtil.NOTAGS.equals(epcTagInfo)) {
			String[] epcTagInfos = epcTagInfo.split(";");
			Double[] signalIntensitys = new Double[epcTagInfos.length];
			for (int i = 0; i < epcTagInfos.length; i++) {
				String[] tagInfos = epcTagInfos[i].split(",");
				map.put(Double.valueOf(tagInfos[1]), tagInfos);
				signalIntensitys[i] = Double.valueOf(tagInfos[1]);
			}
			MathUtils.Pair pair = MathUtils.minmax(signalIntensitys);
			tagVo = new TagVo(map.get(pair.getMax())[0], map.get(pair.getMax())[1], epcTagInfo);
			RfidUtil.appointAcqg2mask(reader, map.get(pair.getMax())[0]);// 指定epc区域
			String userAreaNo = RfidUtil.readUser(reader, 0);// 获取当前USER区SN编码，系统中验证是否符合写入条件
			tagVo.setUserTagId(userAreaNo);
		}
		return tagVo;
	}

	public static void main(String[] args) throws Exception {
		// rfidReadAnts(new AlienClass1Reader(),"0");
		rfidTest();
	}

	private static void rfidTest() {
		AlienClass1Reader reader = new AlienClass1Reader("192.168.90.163", 23);
		try {
			reader.open();
			reader.doReaderCommand(RfidUtil.readLine("ant=0,1,2"));
			System.out.println(reader.doReaderCommand(RfidUtil.readLine("t")));
			// reader.doReaderCommand(RfidUtil.readLine(RfidUtil.appointAntWrite(0)));
			// System.out.println(reader.doReaderCommand(readLine("info")));
			// System.out.println(reader.doReaderCommand(readLine("g2read=3 0 11")));
		} catch (AlienReaderConnectionRefusedException e) {
			e.printStackTrace();
		} catch (AlienReaderNotValidException e) {
			e.printStackTrace();
		} catch (AlienReaderTimeoutException e) {
			e.printStackTrace();
		} catch (AlienReaderConnectionException e) {
			e.printStackTrace();
		} catch (AlienReaderCommandErrorException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
