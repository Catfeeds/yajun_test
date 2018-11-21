package com.wis.basis.numRule.service.impl;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.DateUtils;
import com.wis.basis.numRule.dao.TsNumRuleDao;
import com.wis.basis.numRule.entity.NumRuleValue;
import com.wis.basis.numRule.entity.TsNumRule;
import com.wis.basis.numRule.service.TsNumRuleService;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.core.utils.SerialNumUtil;

import net.sf.json.JSONObject;

@Service
public class TsNumRuleServiceImpl extends BaseServiceImpl<TsNumRule> implements TsNumRuleService {
	private final static String RULE_TYPE_DATE = "DATE";
	private final static String RULE_TYPE_SEQ = "SEQ";
	private final static String SPLIT_STRING = "^";
	@Autowired
	private DictEntryService entryService;

	@Autowired
	public void setDao(TsNumRuleDao dao) {
		this.dao = dao;
	}

	@Override
	public TsNumRule findByType(String type) {
		if (StringUtils.isNotBlank(type)) {
			TsNumRule rule = new TsNumRule();
			rule.setType(type);
			List<TsNumRule> rules = findByEg(rule);
			return rules.size() > 0 ? rules.get(0) : null;
		}
		return null;
	}

	@Override
	public String getSerialNumber(String type, NumRuleValue seqRuleNo) {
		TsNumRule numRule = findByType(type);
		// 可选的规则
		Map<String, DictEntry> mapTypeCode = entryService.getMapTypeCode(ConstantUtils.ENTRY_TYPE_SERIALIZABLE_RULE);
		String serialNumber;
		try {
			serialNumber = generalSerialNumber(numRule, mapTypeCode, seqRuleNo);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
			throw new BusinessException("SEQUENCE_RULE_EXCEPTION");
		}
		return serialNumber;
	}

	/**
	 * 
	 * @param numRule
	 *            所选择的规则
	 * @param mapTypeCode
	 *            数据字典中规则列表
	 * @param ruleNo
	 *            规则所对应的编码
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private String generalSerialNumber(TsNumRule choiceRule, Map<String, DictEntry> mapTypeCode, NumRuleValue ruleNo)
			throws IllegalArgumentException, IllegalAccessException {
		boolean isHaveTimeOrSeq = false;// 编码规则中是否含有seq或date
		StringBuffer buffer = new StringBuffer(choiceRule.getType() + ":");
		// 前缀
		if (StringUtils.isNotBlank(choiceRule.getPrefix())) {
			if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(choiceRule.getPrefixReset())) {// 如果重置
				appendNo(mapTypeCode, ruleNo, choiceRule.getPrefix(), buffer, isHaveTimeOrSeq);
			} else {
				buffer.append(SPLIT_STRING);
			}
		}
		// 中缀
		if (StringUtils.isNotBlank(choiceRule.getInfix())) {
			if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(choiceRule.getInfixReset())) {// 如果重置
				appendNo(mapTypeCode, ruleNo, choiceRule.getInfix(), buffer, isHaveTimeOrSeq);
			} else {
				buffer.append(SPLIT_STRING);
			}
		}
		// 后缀
		if (StringUtils.isNotBlank(choiceRule.getSuffix())) {
			if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(choiceRule.getSuffixReset())) {
				appendNo(mapTypeCode, ruleNo, choiceRule.getSuffix(), buffer, isHaveTimeOrSeq);
			} else {
				buffer.append(SPLIT_STRING);
			}
		}

		// 序列
		Long nextLong = SerialNumUtil.getInstance().nextLong(buffer.toString());
		buffer.delete(0, choiceRule.getType().length() + 1);
		String sequence = buffer.toString();

		// 前缀不需要重置
		if (StringUtils.isNotBlank(choiceRule.getPrefix())
				&& ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO.equals(choiceRule.getPrefixReset())) {
			StringBuffer notRestBuffer = new StringBuffer();
			appendNo(mapTypeCode, ruleNo, choiceRule.getPrefix(), notRestBuffer, isHaveTimeOrSeq);
			sequence = sequence.replaceFirst("\\" + SPLIT_STRING, notRestBuffer.toString());
		}

		// 中缀不需要重置
		if (StringUtils.isNotBlank(choiceRule.getInfix())
				&& ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO.equals(choiceRule.getInfixReset())) {
			StringBuffer notRestBuffer = new StringBuffer();
			appendNo(mapTypeCode, ruleNo, choiceRule.getInfix(), notRestBuffer, isHaveTimeOrSeq);
			sequence = sequence.replaceFirst("\\" + SPLIT_STRING, notRestBuffer.toString());
		}
		// 后缀不需要重置
		if (StringUtils.isNotBlank(choiceRule.getSuffix())
				&& ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO.equals(choiceRule.getSuffixReset())) {
			StringBuffer notRestBuffer = new StringBuffer();
			appendNo(mapTypeCode, ruleNo, choiceRule.getSuffix(), notRestBuffer, isHaveTimeOrSeq);
			sequence = sequence.replaceFirst("\\" + SPLIT_STRING, notRestBuffer.toString());
		}

		sequence = getSeqence(sequence, nextLong, choiceRule.getLength() == null ? 0 : choiceRule.getLength());
		int noLength = sequence.length();
		if (choiceRule.getLength() != null) {// 序列长度大于设定的长度
			if (noLength > choiceRule.getLength()) {
				throw new BusinessException("SEQUENCE_RULE_LENGTH_ERROR", choiceRule.getLength(), noLength, sequence);
			}
		}
		return sequence;
	}

	/**
	 * 把生成的编号拼接
	 * 
	 * @param mapTypeCode
	 *            系统可识别的编码类型
	 * @param ruleNo
	 *            规则所对应的编号
	 * @param rule
	 *            前缀，中缀，或后缀
	 * @param sequence
	 *            序列拼接
	 * @throws IllegalAccessException
	 */
	private boolean appendNo(Map<String, DictEntry> mapTypeCode, NumRuleValue ruleNo, String rule,
			StringBuffer sequence, boolean isHaveTimeOrSeq) throws IllegalAccessException {
		// 截取编码规则
		String[] rules = rule.split("}");
		for (String ruleStr : rules) {
			int index1 = ruleStr.indexOf("{");
			if (index1 != -1) {
				String finalStr = ruleStr.substring(0, index1);// 不是编码规则，是一个不变的字符串
				sequence.append(finalStr);
				ruleStr = ruleStr.replace(finalStr, "");
				index1 = ruleStr.indexOf("{");
				int index2 = ruleStr.indexOf("[");
				if (index2 != -1) {
					String type = ruleStr.substring(index1 + 1, index2);// 编码规则
					String lengthRule = ruleStr.substring(ruleStr.indexOf("[") + 1, ruleStr.indexOf("]"));// 编码的长度或类型规则
					isHaveTimeOrSeq = getNo(ruleNo, type, lengthRule, mapTypeCode, isHaveTimeOrSeq, sequence);// 生成编号
				}
			} else {// 不是编码规则，是一个不变的字符串
				sequence.append(ruleStr);
			}
		}
		return isHaveTimeOrSeq;
	}

	/**
	 * 获取编码
	 * 
	 * @param ruleNo
	 *            编码
	 * @param type
	 *            所选的编码规则
	 * @param lengthRule
	 *            编码长度规则
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private boolean getNo(NumRuleValue ruleNo, String type, String lengthRule, Map<String, DictEntry> mapTypeCode,
			boolean isHaveTimeOrSeq, StringBuffer sequence) throws IllegalArgumentException, IllegalAccessException {
		String no = "";
		if (RULE_TYPE_DATE.equals(type)) {// 编码规则是时间
			try {
				no = DateUtils.formatDate(new Date(), lengthRule);
				isHaveTimeOrSeq = true;
			} catch (Exception e) {
				e.printStackTrace();
				throw new BusinessException("SEQUENCE_RULE_DATE_FORMAT_ERROR", lengthRule);
			}
		} else if (RULE_TYPE_SEQ.equals(type)) {// 编码规则是序列
			no = RULE_TYPE_SEQ + "{" + lengthRule + "}";
			isHaveTimeOrSeq = true;
		} else {
			no = generalNoByChoiceRule(ruleNo, type, lengthRule, mapTypeCode);
		}
		sequence.append(no);
		return isHaveTimeOrSeq;
	}

	/**
	 * 根据选定的编码规则生成编码
	 * 
	 * @param ruleNo
	 *            编码规则所对应的编号
	 * @param type
	 *            编码规则类型
	 * @param lengthRule
	 *            编码对应的长度 或者类型
	 * @param mapTypeCode
	 *            系统可识别的规则
	 * @param no
	 *            生成编号
	 * @return
	 * @throws IllegalAccessException
	 */
	private String generalNoByChoiceRule(NumRuleValue ruleNo, String type, String lengthRule,
			Map<String, DictEntry> mapTypeCode) throws IllegalAccessException {
		String no;
		if (mapTypeCode.containsKey(type)) {// 输入的编码规则存在
			// 通过反射获取属性的值
			no = reflectGetNo(ruleNo, type);

			if (StringUtils.isNotBlank(lengthRule)) {// 对长度有限制
				String[] split = lengthRule.split(",");
				if (split.length == 1) {// 如果只写一个数字，则如果编号长度大于限制，则截取，如果不够则补0
					int noLength = Integer.parseInt(split[0]);
					if (noLength <= no.length()) {
						no = no.substring(0, noLength);
					} else {
						no = String.format("%0" + (noLength - no.length()) + "d", 0) + no;
					}
				} else {// 如果大于等于两个数字，则只取前两个数字并且数字小于编号长度，则截取，
					int noLength1 = Integer.parseInt(split[0]);
					int noLength2 = Integer.parseInt(split[1]);
					if (noLength1 <= no.length() && noLength2 <= no.length()) {
						if (noLength1 < noLength2) {
							no = no.substring(noLength1, noLength2);
						} else {
							no = no.substring(noLength2, noLength1);
						}
					}
				}
			}
		} else {
			throw new BusinessException("SEQUENCE_RULE_TYPE_ERROR", type);
		}
		return no;
	}

	/**
	 * 反射获取 属性的值
	 * 
	 * @param ruleNo
	 * @param type
	 * @return
	 * @throws IllegalAccessException
	 */
	private String reflectGetNo(NumRuleValue ruleNo, String type) throws IllegalAccessException {
		Field[] declaredFields = ruleNo.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			if (field.getName().toUpperCase().contains(type.toUpperCase())) {
				field.setAccessible(true);
				return field.get(ruleNo).toString();
			}
		}
		return null;
	}

	private void checkPartBar(TsNumRule numRule) {
		// 可选的规则
		Map<String, DictEntry> mapTypeCode = entryService.getMapTypeCode(ConstantUtils.ENTRY_TYPE_SERIALIZABLE_RULE);

		StringBuffer partBarCheckRule = new StringBuffer();//物料 校验规则
		if (StringUtils.isNotBlank(numRule.getPrefix())) {//前缀
			partBarCheckRule.append(numRule.getPrefix());
		}
		if (StringUtils.isNotBlank(numRule.getInfix())) {//中缀
			partBarCheckRule.append(numRule.getInfix());
		}
		if (StringUtils.isNotBlank(numRule.getSuffix())) {//后缀
			partBarCheckRule.append(numRule.getSuffix());
		}
		int codeLength = 0;//编码长度
		while (partBarCheckRule.length() > 0) {
			Map<String, String> partCheckRuleMap = getPartCheckRuleMap(partBarCheckRule);
			int lastIndex = Integer.valueOf(partCheckRuleMap.get("end"));
			if (partCheckRuleMap.get("type") != null) {//规则类型
				if (!mapTypeCode.containsKey(partCheckRuleMap.get("type"))) {
					throw new BusinessException("SEQUENCE_RULE_TYPE_ERROR", partCheckRuleMap.get("type"));
				}

				Integer start = Integer.valueOf(partCheckRuleMap.get("start"));
				lastIndex = lastIndex - start;
				if (lastIndex <= 0) {
					throw new BusinessException("SEQUENCE_RULE_BAR_CODE_INDEX_ERROR", start,
							partCheckRuleMap.get("end"));
				}

				if (start != codeLength) {
					throw new BusinessException("SEQUENCE_RULE_BAR_CODE_ERROR", codeLength);
				}
			}
			codeLength += lastIndex;
		}

	}

	private Map<String, String> getPartCheckRuleMap(StringBuffer str) {
		Map<String, String> map = new HashMap<String, String>();
		int start = str.indexOf("{");
		int end = str.indexOf("}");
		String finalString = null;
		String type = null;
		if (start != -1 && start != 0) {
			finalString = str.substring(0, start);
			str.delete(0, start);
		} else if (start == -1) {
			finalString = str.toString();
			start = str.length();
			str.delete(0, start);
		} else {
			type = str.substring(start + 1, end);
			str.delete(start, end + 1);
		}

		String typeStart = null;
		String typeEnd = null;
		if (type != null) {
			int a = type.indexOf("[");
			int b = type.indexOf("]");
			String[] split = type.substring(a + 1, b).split(",");
			typeStart = split[0];
			typeEnd = split[1];
			type = type.substring(0, a);
		} else {
			typeStart = "0";
			typeEnd = finalString.length() + "";
		}
		map.put("type", type);
		map.put("finalString", finalString);
		map.put("start", typeStart + "");
		map.put("end", typeEnd + "");
		return map;
	}

	@Override
	public TsNumRule checkAndGetBean(String jsonBean) {
		TsNumRule bean = getBean(JSONObject.fromObject(jsonBean));
		// 可选的规则
		Map<String, DictEntry> mapTypeCode = entryService.getMapTypeCode(ConstantUtils.ENTRY_TYPE_SERIALIZABLE_RULE);
		if (ConstantUtils.SERIALIZABLE_TYPE_PART_BAR_CHECK.equals(bean.getType())) {//物料条码检验
			try {
				checkPartBar(bean);
			} catch (BusinessException e) {
				throw e;
			} catch (Exception e) {
				log.error(ExceptionUtils.getStackTrace(e));
				throw new BusinessException("SEQUENCE_RULE_EXCEPTION");
			}
			return bean;
		}

		StringBuffer buffer = new StringBuffer();
		NumRuleValue ruleNo = new NumRuleValue("PART", "LINE", "PLANT", "WORKSHOP", "PORDER", "AVI", "UN_CHECK",
				"SUPPLIER");
		boolean isHaveTimeOrSeq = false;
		try {
			// 前缀
			if (StringUtils.isNotBlank(bean.getPrefix())) {
				isHaveTimeOrSeq = appendNo(mapTypeCode, ruleNo, bean.getPrefix(), buffer, isHaveTimeOrSeq);
			}
			// 中缀
			if (StringUtils.isNotBlank(bean.getInfix())) {
				isHaveTimeOrSeq = appendNo(mapTypeCode, ruleNo, bean.getInfix(), buffer, isHaveTimeOrSeq);
			}
			// 后缀
			if (StringUtils.isNotBlank(bean.getSuffix())) {
				isHaveTimeOrSeq = appendNo(mapTypeCode, ruleNo, bean.getSuffix(), buffer, isHaveTimeOrSeq);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("SEQUENCE_RULE_EXCEPTION");
		}

		if (!isHaveTimeOrSeq) {
			throw new BusinessException("SEQUENCE_RULE_NOT_HAVE_DATE_OR_SEQ");
		}
		String sequence = buffer.toString();
		sequence = getSeqence(sequence, 1L, bean.getLength() == null ? 0 : bean.getLength());
		int noLength = sequence.length();
		if (bean.getLength() != null) {// 序列长度大于设定的长度
			if (noLength > bean.getLength()) {
				throw new BusinessException("SEQUENCE_RULE_LENGTH_ERROR", bean.getLength(), noLength, sequence);
			} else {
				bean.setCurrentValue(sequence);
			}
		} else {
			bean.setCurrentValue(sequence);
		}
		return bean;
	}

	private String getSeqence(String seq, Long serialNum, int defineSeqLength) {
		// 计算长度
		int everySeqLength = 0;
		if (defineSeqLength > 0) {
			int seqNum = 0;
			String seqTmp = seq;
			while (seqTmp.indexOf("{") != -1) {
				int a = seqTmp.indexOf("{");
				int b = seqTmp.indexOf("}");
				seqTmp = seqTmp.replaceFirst("SEQ\\" + seqTmp.substring(a, b + 1), "");
				seqNum++;
			}
			everySeqLength = (defineSeqLength - seqTmp.length()) / seqNum;
		} else {
			everySeqLength = 1;
		}

		// 替换SEQ
		while (seq.indexOf("{") != -1) {
			int a = seq.indexOf("{");
			int b = seq.indexOf("}");
			String[] seqLengthRules = seq.substring(a + 1, b).split(",");
			if (seqLengthRules.length > 0) {
				int seqLength = Integer
						.parseInt(StringUtils.isBlank(seqLengthRules[0]) || Integer.parseInt(seqLengthRules[0]) == 0
								? "1" : seqLengthRules[0]);
				seq = seq.replaceFirst(RULE_TYPE_SEQ + "\\" + seq.substring(a, b + 1),
						String.format("%0" + seqLength + "d", serialNum));
			} else {
				seq = seq.replaceFirst(RULE_TYPE_SEQ + "\\" + seq.substring(a, b + 1),
						String.format("%0" + everySeqLength + "d", serialNum));
			}
		}
		return seq;
	}

	private TsNumRule getBean(JSONObject json) {
		TsNumRule bean = new TsNumRule();
		if (StringUtils.isNotBlank(json.getString("id"))) {
			bean.setId(json.getInt("id"));
		}
		bean.setType(json.getString("type"));// 编码类型
		bean.setPrefix(json.getString("prefix"));// 前缀
		bean.setInfix(json.getString("infix"));// 中缀
		bean.setSuffix(json.getString("suffix"));// 后缀
		if (StringUtils.isNotBlank(json.getString("length"))) {// 长度
			bean.setLength(json.getInt("length"));
		}
		// 前缀是否重置
		if (StringUtils.isNotBlank(json.optString("prefixReset", "")) && json.getString("prefixReset").equals("on")) {
			bean.setPrefixReset(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES);
		} else {
			bean.setPrefixReset(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO);
		}

		// 中缀是否重置
		if (StringUtils.isNotBlank(json.optString("infixReset", "")) && json.getString("infixReset").equals("on")) {
			bean.setInfixReset(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES);
		} else {
			bean.setInfixReset(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO);
		}

		// 后缀是否重置
		if (StringUtils.isNotBlank(json.optString("suffixReset", "")) && json.getString("suffixReset").equals("on")) {
			bean.setSuffixReset(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES);
		} else {
			bean.setSuffixReset(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO);
		}
		return bean;
	}

}
