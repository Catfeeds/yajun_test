package com.wis.mes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {
	
	public static void createText(String content,String fileName){
		FileOutputStream fop = null;
		File file;
		try {

			file = new File("D:/"+fileName+".txt");
			fop = new FileOutputStream(file);
			if (!file.exists()) {
				file.createNewFile();
			}

			// get the content in bytes
			byte[] contentInBytes = content.getBytes();

			fop.write(contentInBytes);
			fop.flush();
			fop.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	} 
	
	
	private static String createSql(){
		StringBuffer sql = new StringBuffer();
//		String[] srcs = {"Complete_Flag","Exit_NO","NG_Out_Flag","Refresh_Flag","SN_Data"};
		//String[] srcs = {"Fault_Code","ID","SN_Data"};
		//String[] srcs = {"Out","In","Repair_Done","Flag","SN_Data"};
		String[] srcs = {"Fault_Code","ID","Status","SN_Data"};
		Integer[] nos = {102,119,5,59,63,74,7,80,25};
	/*	
		
		INSERT INTO "TKS_GROUP" VALUES (seq_tks_group_id.nextval, 'STATION_102', '102工位RFID读写标识', null, 'stationReadService', 'Channel1.L72');
		INSERT INTO "TKS_GROUP" VALUES (seq_tks_group_id.nextval, 'STATION_119', '119工位RFID读写标识', null, 'stationReadService', 'Channel1.L72');
		INSERT INTO "TKS_GROUP" VALUES (seq_tks_group_id.nextval, 'STATION_5', '5工位RFID读写标识', null, 'stationReadService', 'Channel1.L72');
		INSERT INTO "TKS_GROUP" VALUES (seq_tks_group_id.nextval, 'STATION_59', '59工位RFID读写标识', null, 'stationReadService', 'Channel1.L72');
		INSERT INTO "TKS_GROUP" VALUES (seq_tks_group_id.nextval, 'STATION_63', '63工位RFID读写标识', null, 'stationReadService', 'Channel1.L72');
		INSERT INTO "TKS_GROUP" VALUES (seq_tks_group_id.nextval, 'STATION_74', '74工位RFID读写标识', null, 'stationReadService', 'Channel1.L72');
		INSERT INTO "TKS_GROUP" VALUES (seq_tks_group_id.nextval, 'STATION_7', '7工位RFID读写标识', null, null, 'Channel1.L72');
		INSERT INTO "TKS_GROUP" VALUES (seq_tks_group_id.nextval, 'STATION_80', '80工位RFID读写标识', null, 'stationReadService', 'Channel1.L72');
		INSERT INTO "TKS_GROUP" VALUES (seq_tks_group_id.nextval, 'STATION_25', '25工位RFID读写标识', null, 'stationReadService', 'Channel1.L72');*/
		
		//Station13.Fault_Code
		for(Integer no:nos){
			for(String src:srcs){
				if(src.equals("SN_Data")){
					for(int i =0;i<22;i++){
						sql.append(" insert into tks_group_item values(seq_tks_group_item_id.nextval,(select id from tks_group where group_code='STATION_"+no+"'),");
						sql.append(" 'Station"+no+"."+src+i+"', ");
						sql.append(" '','',"+i+");\n");
					}
				}else{
					sql.append(" insert into tks_group_item values(seq_tks_group_item_id.nextval,(select id from tks_group where group_code='STATION_"+no+"'),");
					sql.append(" 'Station"+no+"."+src+"', ");
					sql.append(" '','',null);\n");
				}
				
			}
			
		}
		return sql.toString();
	}
	
	private static String productStaData(){
		StringBuffer sql = new StringBuffer();
		String[] srcs = {"Abnormal_Code","Abnormal_Duration","Back_Wait_Duration","Device_Work_Begin_Hour","Device_Work_Begin_Minute",
				"Device_Work_Begin_Second","Device_Work_End_Hour","Device_Work_End_Minute","Device_Work_End_Second",
				"Enter_Begin_Hour","Enter_Begin_Minute","Enter_Begin_Second","Enter_End_Hour","Enter_End_Minute","Enter_End_Second",
				"Exit_Begin_Hour","Exit_Begin_Minute","Exit_Begin_Second","Exit_End_Hour","Exit_End_Minute","Exit_End_Second",
				"Front_Wait_Duration","OK_NG","Sta_ID","Staff_Work_Duration","Station_Done_Time_Hour","Station_Done_Time_Minute",
				"Station_Done_Time_Second","Station_Work_Duration","Warning_Duration"};
		String[] srcNotes={"异常故障码","异常时间（秒）","后等工时间（秒）","设备工作开始时间(时)","设备工作开始时间(分)","设备工作开始时间(秒)","设备工作结束时间(分)",
				"设备工作结束时间(分)","设备工作结束时间(秒)","流入开始时间（时）","流入开始时间（分）","流入开始时间（秒）","流入结束时间（时）","流入结束时间（分）","流入结束时间（秒）",
				"流出开始时间（时）","流出开始时间（分）","流出开始时间（秒）","流出结束时间（时）","流出结束时间（分）","流出结束时间（秒）","前等工时间（秒）","OK_NG_CODE","工位编号",
				"员工作业时间（秒）","工位完成时间（时）","工位完成时间（时）","工位完成时间（分）","工位完成时间（秒）","工位作业时间（秒）","警告时间（秒）",};
		/*for(int i=1;i<=29;i++){*/
			sql.append("insert into tks_group values(seq_tks_group_id.nextval, ");
			sql.append("'PRODUCT_STA','工位产品数据'");
			sql.append("  ,'','','');\n");
	/*	}*/
		/*for(int i=1;i<=29;i++){*/
			for(int j=0;j<srcs.length;j++){
				sql.append(" insert into tks_group_item values(seq_tks_group_item_id.nextval,(select id from tks_group where group_code='PRODUCT_STA'),");
				sql.append("'"+srcs[j]+"', ");
				sql.append(" '"+srcNotes[j]+"','',null);\n");
			}
	/*	}*/
		return sql.toString();
	}
	
	
	private void ulocStatusData(){
		
	}
	
	
	public static void main(String[] args) {
		String sql = createSql();
		createText(sql,"product");
		/*String sql = productStaData();
		createText(sql,"product_sta");*/
	}
}