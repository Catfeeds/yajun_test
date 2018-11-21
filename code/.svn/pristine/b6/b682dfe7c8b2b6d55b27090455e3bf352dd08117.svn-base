/**
 * 
 */
package com.wis.mes.util;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;

import com.google.gson.Gson;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.LoadUtils;
import com.wis.core.entity.AuditEntity;

/**
 *
 * @author 赵吉超<jc_zhao@westinfosoft.com>
 *
 * @company 上海西信信息科技有限公司
 *
 * @date 2017年4月17日
 *
 * @desc 字符串处理工具类
 *
 */
@SuppressWarnings("unchecked")
public class StringUtil {

	/**
	 * 判断字符串是否为空
	 * 
	 * @param param
	 *            判定字符串
	 * 
	 * @return 是否为空
	 **/
	public static boolean isEmpty(String param) {
		if (param == null) {
			return true;
		}
		param = param.trim().replaceAll(ConstantUtils.STRING_SPACE, ConstantUtils.STRING_EMPTY);
		if (ConstantUtils.STRING_EMPTY.equals(param)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断字符串是否不为空
	 * 
	 * @param param
	 *            判定字符串
	 * 
	 * @return 是否不为空
	 **/
	public static boolean isNotEmpty(final String param) {
		return !isEmpty(param);
	}

	/**
	 * 判断列表是否为空
	 * 
	 * @param list
	 *            判定对象列表
	 * 
	 * @param json
	 *            json工具
	 * 
	 * @return 返回json
	 */
	public static String listIfNull(final List<? extends Object> list, final Gson json) {
		if (list == null || list.size() == 0) {
			return json.toJson("LIST_NULL");
		}
		return json.toJson(list);
	}

	/**
	 * 判断对象是否为空
	 * 
	 * @param obj
	 *            判定对象
	 * 
	 * @param json
	 *            json工具
	 * 
	 * @return 返回json
	 */
	public static String ifNull(final Object obj, final Gson json) {
		if (obj == null) {
			return json.toJson("NULL");
		}
		return json.toJson(obj);
	}

	/**
	 * 将字段转换成驼峰命名的变量名
	 * 
	 * @param param
	 *            字段名
	 * 
	 * @return 驼峰命名的变量名
	 */
	public static String transferColumnToCameleCase(String param) {
		if (isEmpty(param)) {
			return ConstantUtils.STRING_EMPTY;
		}
		if (param.contains(ConstantUtils.STRING_UNDERLINE)) {
			String result = param.substring(0, param.split(ConstantUtils.STRING_UNDERLINE)[0].length()).toLowerCase();
			param = param.substring(param.split(ConstantUtils.STRING_UNDERLINE)[0].length() + 1);
			if (param.contains(ConstantUtils.STRING_UNDERLINE)) {
				for (final String sub : param.split(ConstantUtils.STRING_UNDERLINE)) {
					result = result + sub.substring(ConstantUtils.MATH_ZERO, 1).toUpperCase()
							+ sub.substring(1).toLowerCase();
				}
			} else {
				if (isNotEmpty(param)) {
					result = result + param.substring(ConstantUtils.MATH_ZERO, 1).toUpperCase()
							+ param.substring(1).toLowerCase();
				}
			}
			return result;
		}
		return param.toLowerCase();
	}

	/**
	 * 将标准驼峰变量名转成字段名称
	 * 
	 * @param param
	 *            字段名
	 * 
	 * @return 字段名
	 */
	public static String transferCameleCaseToColumn(String param) {
		if (isEmpty(param)) {
			return ConstantUtils.STRING_EMPTY;
		}
		param = param.substring(ConstantUtils.MATH_ZERO, 1).toLowerCase() + param.substring(1);
		final StringBuffer sb = new StringBuffer();
		for (final char e : param.toCharArray()) {
			if (Character.isUpperCase(e)) {
				sb.append(ConstantUtils.STRING_UNDERLINE);
			}
			sb.append(String.valueOf(e));
		}
		return sb.toString().toUpperCase();
	}

	/**
	 * 判断列表是否为空
	 * 
	 * @param originList
	 *            列表
	 * 
	 * @return 是否为空
	 * 
	 */
	public static boolean isEmpty(final List<? extends Object> originList) {
		if (originList == null || originList.size() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 判断列表是否不为空
	 * 
	 * @param originList
	 *            列表
	 * 
	 * @return 是否不为空
	 * 
	 */
	public static boolean isNotEmpty(final List<? extends Object> originList) {
		return !isEmpty(originList);
	}

	/**
	 * 字符串列表去重
	 * 
	 * @param 字符串列表
	 *            originList
	 * 
	 * @return 去重后的效果Map[list:去重后的列表,resultStr:,...,,originCount:[元素:次数]]
	 */
	public static Map<String, Object> removeRepeated(final List<String> originList) {
		final Map<String, Object> map = new HashMap<String, Object>();

		final List<String> resultList = new ArrayList<String>();

		final StringBuffer sb = new StringBuffer(ConstantUtils.STRING_COMMA);

		final Map<String, Integer> originCountMap = new HashMap<String, Integer>();

		for (final String e : originList) {
			originCountMap.put(e, originCountMap.get(e) == null ? 1 : (originCountMap.get(e) + 1));
			if (originCountMap.get(e) == null) {
				resultList.add(e);
				sb.append(e).append(ConstantUtils.STRING_COMMA);
				originCountMap.put(e, 1);
			} else {
				originCountMap.put(e, originCountMap.get(e) + 1);
			}
		}

		map.put("list", resultList);
		map.put("resultStr", sb.toString());
		map.put("originCount", originCountMap);

		return map;
	}

	/**
	 * 判断导入是否全部结束
	 * 
	 * @param row
	 *            行数据
	 * 
	 * @param cellCount
	 *            单元格数
	 * 
	 * @return 是否结束
	 */
	public static boolean isEnd(final Row row, final int cellCount) {
		for (int i = ConstantUtils.MATH_ZERO; i < cellCount; i++) {
			if (isNotEmpty(LoadUtils.getCell(row, i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 根据 条件 获取 复杂查询条件字符串
	 * 
	 * @param clazz
	 *            类型 必须为AuditEntity的子类
	 * 
	 * @param param
	 *            查询条件
	 * 
	 *            注：key必须为字段名，如：NAME_CN
	 * 
	 *            注：value必须为 String、List以及其他基本类型的包装类
	 * 
	 * @return 查询条件字符串
	 **/
	public static String getSqlByParam(final Class<? extends AuditEntity> clazz, final Map<String, Object> param) {
		if (clazz == null) {
			return null;
		}
		StringBuffer sql = new StringBuffer("select * from ");
		sql.append(getTableName(clazz));
		if (param == null) {
			return sql.toString();
		}
		sql.append(" where 1 = 1");
		for (final String key : param.keySet()) {
			final Object value = param.get(key);
			if (value == null) {
				sql = sql.append(" and ").append(key).append(" is ").append(value);
			} else if (value instanceof String) {
				sql = sql.append(" and ").append(key).append(" = '").append(value).append("'");
			} else if (value instanceof List) {
				final List<Object> list = (List<Object>) value;
				for (int i = 0; i < list.size(); i++) {
					final Object obj = list.get(i);
					if (i == 0) {
						sql = sql.append(" and(").append(key);
					} else {
						sql = sql.append(" or ").append(key);
					}
					if (obj == null) {
						sql = sql.append(" is ").append(obj);
					} else if (obj instanceof String) {
						sql = sql.append(" = '").append(obj).append("'");
					} else {
						sql = sql.append(" = ").append(obj);
					}
					if (i == list.size() - 1) {
						sql = sql.append(")");
					}
				}

			} else {
				sql = sql.append(" and ").append(key).append(" = ").append(value);
			}
		}
		return sql.toString();
	}

	/**
	 * 根据 类类型 解析出表名
	 * 
	 * @param clazz
	 *            bean类类型
	 * 
	 * @return 表名
	 **/
	public static String getTableName(final Class<? extends AuditEntity> clazz) {
		for (final Annotation ano : clazz.getAnnotations()) {
			if (ano instanceof Table) {
				return ((Table) ano).name();
			}
		}
		return null;
	}

	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNotBlank(String str) {
		return !StringUtil.isBlank(str);
	}

	public static String getString(Object obj) {
		return obj == null ? "" : obj.toString();
	}
	public static String joins(List<String> arrays,char sign){
		StringBuffer buffer = new StringBuffer();
		if(null != arrays && arrays.size() > 0){
			for(String src:arrays){
				if(StringUtils.isNotBlank(src)){
					buffer.append(src+sign);
				}
			}
			if(buffer.length()>0){
				buffer.deleteCharAt(buffer.length()-1);
			}
		}
		return buffer.toString();
	}
	
	public static boolean isNotEmpty(Object obj){
		if(null != obj && !obj.equals("")){
			return true;
		}
		return false;
	}
	
	public static Integer[] getInts(String src) {
		if (StringUtils.isEmpty(src)) {
			return new Integer[0];
		}
		String[] arrays = src.split(",");
		List<Integer> resultList = new ArrayList<Integer>();
		for (int i = 0; i < arrays.length; i++) {
			if(!resultList.contains(Integer.valueOf(arrays[i]))) {
				resultList.add(Integer.valueOf(arrays[i]));
			}
		}
		return resultList.toArray(new Integer[resultList.size()]);
	}
}
