/**
 * 
 */
package com.wis.mes.dakin.report.service;

import java.util.Map;

/**
 * @author caixia
 *
 */
public interface ProductMrgService {
    public Map<String,Object> getShiftEvery(String pressName,String startTime);
    public Map<String,Object> getShiftEveryCount(String pressName,String startTime);
}
