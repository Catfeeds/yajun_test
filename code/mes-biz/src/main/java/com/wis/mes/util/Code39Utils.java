package com.wis.mes.util;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

public class Code39Utils{  
  
	private static int getCharIndex(char c){  
        if( c >= '0' && c <= '9' ){  
            return c - 48;  
        } else if( c >= 'A' && c <= 'Z' ){  
            return c - 55;  
        } else if( c == '-' ){  
            return 36;  
        } else if( c == '.' ){  
            return 37;  
        } else if( c == ' ' ){  
            return 38;  
        } else if( c == '$' ){  
            return 39;  
        } else if( c == '/' ){  
            return 40;  
        } else if( c == '+' ){  
            return 41;  
        } else if( c == '%' ){  
            return 42;  
        }
		return c;
    } 
	private static int getChar(int c) {  
        if( c >= 0 && c <= 9 ){  
            return c + 48;  
        } else if( c >= 10 && c <=  35){  
            return c + 55;  
        } else if( c == 36 ){  
            return 45;  
        } else if( c == 37 ){  
            return 46;  
        } else if( c == 38 ){  
            return 32;  
        } else if( c == 39 ){  
            return 36;  
        } else if( c == 40 ){  
            return 47;  
        } else if( c == 41 ){  
            return 43;  
        } else if( c == 42 ){  
            return 37;  
        }
		return c; 
    }  
	
	public static String computeCheckSum(String texto) {  
        int check = 0;  
        for( int i = 0; i < texto.length(); i++ ){  
            check += getCharIndex(texto.charAt(i));  
        }  
        return (Character.valueOf((char)getChar(check%43)).toString());  
    }  
	/**
	 * 
	 * 校验code39编码
	 * ***/
	public static String checkCode(String code){
		boolean FLAG = false;
		Map<String,Object> result = new HashMap<String,Object>();
		if(null != code && !"".equals(code)){
			String rawCode = code.substring(0,code.length()-1);//减去校验位 
			String lastCode = code.substring(code.length()-1,code.length());
			String newLastCode = computeCheckSum(rawCode);
			if(newLastCode.equals(lastCode)){
				FLAG = true;
			}
		}
		result.put("success", FLAG);
		result.put("code", code);
		return JSONObject.fromObject(result).toString();
	}
	
	public static void main(String[] args) {
		 String code = "702194758F00091533111$";  
	     System.out.println(checkCode(code));  
	}
}  