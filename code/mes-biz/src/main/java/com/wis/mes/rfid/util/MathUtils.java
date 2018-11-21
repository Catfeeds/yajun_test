package com.wis.mes.rfid.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MathUtils {
	public static class Pair {  
        public Pair(Double f, Double s) {  
            min = f;  
            max = s;  
        }  
        
        public Double getMin() {  
            return min;  
        }  
        
        public Double getMax() {  
            return max;  
        }  
        
        private Double min;  
        private Double max;  
    }  
	
	public static class IntPair {  
        public IntPair(Integer f, Integer s) {  
        	min = f;  
        	max = s;  
        }  
        
        public Integer getMin() {  
            return min;  
        }  
        
        public Integer getMax() {  
            return max;  
        }  
        
        private Integer min;  
        private Integer max;  
    } 
  
    public static Pair minmax(Double[] values) {  
		List<Double> list = Arrays.asList(values);
		Collections.sort(list);
        return new Pair(list.get(0),list.get(list.size()-1));  
    }  
    
    public static IntPair minmax(Integer[] values) {  
    	Integer min = Integer.MAX_VALUE;  
    	Integer max = Integer.MIN_VALUE;  
        for (Integer v : values) {  
            if (min > v)  
                min = v;  
            if (max < v)  
                max = v;  
        }  
        return new IntPair(min, max);  
    } 
    
    public static void main(String[] args) {
    	Integer[] a = {1,2,3,4,5,666};
    	Double[] b = {1.23};
    	MathUtils.IntPair apair = MathUtils.minmax(a);
    	MathUtils.Pair bpair = MathUtils.minmax(b);
    	Integer i = apair.getMax();
    	System.out.println(apair.getMin());
    	System.out.println(bpair.getMax());
	}
}
