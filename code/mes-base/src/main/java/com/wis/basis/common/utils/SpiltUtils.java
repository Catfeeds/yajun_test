package com.wis.basis.common.utils;

import java.util.ArrayList;
import java.util.List;

public class SpiltUtils {
	/**
	 * 拆分list
	* @Title:averageAssign 
	* @param @param paramList 要拆分的list
	* @param @param num 以num为一组进行拆分
	* @param @return    
	* @return List<List<T>>
	* @throws
	 */
	public static <T> List<List<T>> averageAssign(List<T> paramList,int num){  
	    List<List<T>> result=new ArrayList<List<T>>();  
	    int remaider=paramList.size()%num;  //计算出余数  
	    int number=paramList.size()/num;  //得出要循环多少次
        if (remaider != 0) {
        	number = number + 1;
        }
	    for(int i=0;i<number;i++){  
	        List<T> value=null;  
	       if (i == 0) {
	    	   if (paramList.size() < num) {
	    		  value=paramList.subList(i*num, paramList.size()); 
	    	   } else {
	    		   value=paramList.subList(i*num, (i+1)*num); 
	    	   }
	       } else if((i>0 && i != number-1) || remaider == 0){
	    	   value=paramList.subList(i*num, (i+1)*num); 
	       } else {
	    	   value=paramList.subList(i*num, i*num+remaider);
	       }
	        result.add(value);  
	    }  
	    return result;  
	}  
}
