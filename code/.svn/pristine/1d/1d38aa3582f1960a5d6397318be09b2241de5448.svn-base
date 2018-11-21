/**
 * 
 */
package com.wis.mes.util;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.wis.basis.common.utils.ConstantUtils;

/**
 *
 * @author 赵吉超<jc_zhao@westinfosoft.com>
 *
 * @company 上海西信信息科技有限公司
 *
 * @date 2017年4月17日
 *
 * @desc 数据转换处理工具类
 *
 */
public class MapperUtil {

    /**
     * 将结果集按照字段名称和类型转换成相应的值
     * 
     * @param rs
     *            结果集
     * 
     * @param fieldType
     *            字段类型
     * 
     * @param columnName
     *            字段名称
     * 
     * @return 相应的值
     * @throws SQLException
     */
    public static Object getColumnValue(final ResultSet rs, final String fieldType, final String columnName)
            throws SQLException {
        // 时间戳
        if (ConstantUtils.STRING_TIMESTAMP_FULL.equals(fieldType)) {
            return rs.getTimestamp(columnName);
        }
        // 日期
        if (ConstantUtils.STRING_DATE_FULL.equals(fieldType)) {
            return rs.getDate(columnName);
        }
        // 字符串
        if (ConstantUtils.STRING_STRING_FULL.equals(fieldType)) {
            return StringUtil.isEmpty(rs.getString(columnName)) ? ConstantUtils.STRING_EMPTY : rs.getString(columnName);
        }
        // 整型
        if (ConstantUtils.STRING_INT.equals(fieldType) || ConstantUtils.STRING_INTEGER_FULL.equals(fieldType)) {
            return Integer.valueOf(StringUtil.isEmpty(rs.getString(columnName)) ? ConstantUtils.STRING_ZERO
                    : rs.getString(columnName));
        }
        // 短整型
        if (ConstantUtils.STRING_SHORT.equals(fieldType) || ConstantUtils.STRING_SHORT_FULL.equals(fieldType)) {
            return Short.valueOf(StringUtil.isEmpty(rs.getString(columnName)) ? ConstantUtils.STRING_ZERO
                    : rs.getString(columnName));
        }
        // 长整型
        if (ConstantUtils.STRING_LONG.equals(fieldType) || ConstantUtils.STRING_LONG_FULL.equals(fieldType)) {
            return Long.valueOf(StringUtil.isEmpty(rs.getString(columnName)) ? ConstantUtils.STRING_ZERO
                    : rs.getString(columnName));
        }
        // 字节型
        if (ConstantUtils.STRING_BYTE.equals(fieldType) || ConstantUtils.STRING_BYTE_FULL.equals(fieldType)) {
            return rs.getByte(columnName);
        }
        // float，默认保留两位小数
        if (ConstantUtils.STRING_FLOAT.equals(fieldType) || ConstantUtils.STRING_FLOAT_FULL.equals(fieldType)) {
            return rs.getFloat(columnName);
        }
        // Double，默认保留两位小数
        if (ConstantUtils.STRING_DOUBLE.equals(fieldType) || ConstantUtils.STRING_DOUBLE_FULL.equals(fieldType)) {
            return rs.getDouble(columnName);
        }
        // boolean
        if (ConstantUtils.STRING_BOOLEAN.equals(fieldType) || ConstantUtils.STRING_BOOLEAN_FULL.equals(fieldType)) {
            return rs.getBoolean(columnName);
        }
        // BigDecimal
        if (ConstantUtils.STRING_DOUBLE_BIGDECIMAL.equals(fieldType)) {
            return BigDecimal.valueOf(
                    StringUtil.isEmpty(rs.getString(columnName)) ? ConstantUtils.MATH_ZERO : rs.getInt(columnName));
        }
        return null;
    }
}
