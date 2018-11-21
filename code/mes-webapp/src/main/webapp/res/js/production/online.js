/**
 * 产品上线JS
 */	
var baseUrl = $("#baseUrl").val();
var checkedAviId = "";
//ajaxRequest(url, data, callback, showSuccess) 
$(function(){
	$("#snInput").focus();
	ajaxRequest(baseUrl+"/onLine/initList.do",{},function(data){
		var result = data.data;
		var lineData = result.lineData;
		var ulocData = result.ulocData;
		var shift = result.shiftNo;
		$("#shiftNo").text(shift);
		var porderData = result.porderData;
		var qty = result.qty;
		var select = "";
		$(lineData).each(function(i,item){
			select +="<option value="+item.id+">"+item.no+"-"+item.nameCn+"</option>";
		});
		if(select == ""){
			$("#lineSelect").html("<option value=''>没有产线权限</option>");
		}else{
			$("#lineSelect").html(select);
		}
		select ="";
		$(ulocData).each(function(i,item){
			select +="<option value="+item.id+">"+item.no+"-"+item.name+"</option>";
		});
		if(select == ""){
			$("#ulocSelect").html("<option value=''>没有工位权限</option>");
		}else{
			$("#ulocSelect").html(select);
		}
		select ="";
		$(porderData).each(function(i,item){
			if(i ==0){
				$("#partNo").text(item.part.no);
				$("#partName").text(item.part.nameCn);
			}
			select +="<option value="+item.id+" data-planStartTime='"+item.planStartTime+"'>"+item.no+"</option>";
		});
		if(select == ""){
			$("#porder").html("<option value=''>没有待上线的工单</option>");
		}else{
			$("#porder").html(select);
		}
		$("#qualityQty").text(qty.qualityQty);
		$("#unqualityQty").text(qty.unQualityQty);
		
		getTableData(result);
	},false);
});

function clearHtmlData(){
	$("#porder").html("<option value=''>没有待上线的工单</option>");
	$("#reachTable").html("");
	$("#crossTable").html("");
	$("#qualityQty").text("");
	$("#unqualityQty").text("");
	$("#partNo").text("");
	$("#partName").text("");
}

function changeLine(lineId){
	ajaxRequest(baseUrl+"/onLine/getUlocDataByLineId.do", {
		tmLineId:lineId
	}, function(data){
		var result = data.data;
		var ulocSelect = "";
		$(result).each(function(i,item){
			ulocSelect += "<option value="+item.id+">"+item.no+"-"+item.name+"</option>";
			if(i == 0){
				getOnlineListDataByLineAndUloc(lineId,item.id);
			}
		});
		if(ulocSelect==""){
			$("#ulocSelect").html("<option value=''>没有工位权限</option>");
			clearHtmlData();
			$("#waitSNPanel").hide();
		}else{
			$("#ulocSelect").html(ulocSelect);
			$("#waitSNPanel").hide();
		}
	}, false); 
	checkedAviId="";
}	
function changeUloc(ulocId){
	getOnlineListDataByLineAndUloc($("#lineSelect").val(),ulocId);
	checkedAviId="";
	$("#waitSNPanel").hide();
}
function changePorder(porderId,aviId){
	ajaxRequest(baseUrl+"/onLine/changedPorderSelect.do", {
		toPorderId:porderId,
		tmLineId:$("#lineSelect").val(),
		tmUlocId:$("#ulocSelect").val()
	}, function(data){
		var result = data.data;
		var shift = result.shiftNo;
		$("#shiftNo").text(shift);
		var qty = result.qty;
		var porder = result.porder;
		$("#qualityQty").text(qty.qualityQty);
		$("#unqualityQty").text(qty.unQualityQty);
		$("#partNo").text(porder.part.no);
		$("#partName").text(porder.part.nameCn);
		getTableData(result);
	}, false); 
	if(aviId){
		checkedAviId=aviId;
	}else{
		checkedAviId="";
		getWaitSN();
	}
}

function getOnlineListDataByLineAndUloc(lineId,uloId){
	ajaxRequest(baseUrl+"/onLine/getOnlineListDataByLineAndUloc.do", {
		tmLineId:lineId,
		tmUlocId:uloId
	}, function(data){
		var result = data.data;
		var shift = result.shiftNo;
		$("#shiftNo").text(shift);
		var porderData = result.porderData;
		var qty = result.qty;
		var select = "";
		select ="";
		$(porderData).each(function(i,item){
			if(i ==0){
				$("#partNo").text(item.part.no);
				$("#partName").text(item.part.nameCn);
			}
			select +="<option value="+item.id+" data-planStartTime='"+item.planStartTime+"'>"+item.no+"</option>";
		});
		if(select == ""){
			$("#porder").html("<option value=''>没有待上线的工单</option>");
		}else{
			$("#porder").html(select);
		}
		$("#qualityQty").text(qty.qualityQty);
		$("#unqualityQty").text(qty.unQualityQty);
		getTableData(result);
	}, false); 
}


function getTableData(result){
	var aviData = result.aviData;
	var onLineData = result.onLineData;
	var table ="";
	$(aviData).each(function(i,item){
		var qty = item.qty;
		var onlineQty = item.onlineQty? item.onlineQty:0;
		table +="<tr id='"+item.id+"'><td>"+getCheckboxInput(item)+"</td><td>"+item.no+"</td><td>"
						+item.porder.no+"</td><td>"+$("#partNo").text()+"-"+$("#partName").text()+"</td><td>"
						+(qty-onlineQty)+"</td><td>"+onlineQty+"</td><td>"+$("#porder").find("option").attr("data-planStartTime")+"</td></tr>";
	});
	if(table==""){
		$("#porder").html("<option value=''>没有待上线的工单</option>");
	}
	$("#reachTable").html(table);
	$("#reachTable").find("tr td").not("tr td:first-child").bind('click',function(){
		clickAviTr($(this).parent("tr").attr("id"));
	});
	table="";
	$(onLineData).each(function(i,item){
		var record = item.record;
		table+="<tr><td>"+item.record.sn+"</td><td>"+record.porderNo+"</td><td>"+record.part.no+"-"+record.part.nameCn+"</td><td>"
					+item.nextUloc+"</td><td>"+new Date(record.onlineTime).Format("yyyy-MM-dd hh:mm:ss")+"</td></tr>";
	});
	$("#crossTable").html(table);
}

function getCheckboxInput(item){
	if(checkedAviId&&checkedAviId == item.id){
		return "<input type='checkbox' id='"+item.id+"' name='aviCheckBox' onclick='clickAviCheckBox("+item.id+");' checked='checked'/>";
	}else{
		return "<input type='checkbox' id='"+item.id+"' name='aviCheckBox' onclick='clickAviCheckBox("+item.id+");'/>";
	}
}

function clickAviTr(checkId){
	$("input[name='aviCheckBox']").each(function(){
		if(this.id == checkId){
			this.checked=true;
			checkedAviId = this.id;
			getWaitSN(this.id);
		}else{
			this.checked =false;
		}
	});
}

function getWaitSN(toPorderAviId){
	if(!toPorderAviId){
		$("#waitSNPanel").empty();
		$("#waitSNPanel").hide();
		return;
	}
	ajaxRequest(baseUrl+"/onLine/getWaitOnlineSn.do", {
		aviId:toPorderAviId,
		ulocId:$("#ulocSelect").val()
	}, function(result){
		var waitPanel = "";
		$(result.data).each(function(i,item){
			waitPanel+="<div style='float: left;margin-right: 20px;margin-left: 5px;'><a href="+"javascript:clickSN('"+item.sn+"');"+">"+item.sn+"</a></div>";
		});
		if(waitPanel ==""){
			$("#waitSNPanel").hide();
		}else{
			$("#waitSNPanel").show();
			$("#waitSNPanel").html(waitPanel);
		}
	}, false);
}

function clickAviCheckBox(checkId){
	$("input[name='aviCheckBox']").each(function(){
		if(this.id == checkId){
			this.checked=true;
			checkedAviId = this.id;
			getWaitSN(this.id);
		}else{
			this.checked =false;
		}
	});
}

function doScanSN(sn){
	if(event.keyCode==13){
		clickSN(sn);
	}
}
/**
 * 点击或者回车SN
 * @param recordId
 * @param sn
 * @returns
 */
function clickSN(sn){
	$("#snInput").val(sn);
	var check = checkSN();
	if(!check.isSuccess){
		return;
	}
	var currentSeq = check.currentSeq;
	var aviId = check.aviId;
	checkedAviId = aviId;
	ajaxRequest(baseUrl+"/onLine/scanSN.do", {
		toPorderAviId:aviId,
		tmUlocId:$("#ulocSelect").val(),
		SN:sn
	}, function(result){
		var data = result.data;
		if(data){
			if(data.bindPart){
				var url = baseUrl+"/onLine/onlineNeedBindPart.do?currentUlocSeq="+data.currentUlocSeq+"&SN="+sn+"&toPorderAviId="+aviId+"&tmUlocId="+$("#ulocSelect").val();
				createwindow("绑定关键件", url, 'OnlineBindPartGridId');
			}
			if(data.bindParameter){
				var url = baseUrl+"/onLine/onlineNeedBindParameter.do?currentUlocSeq="+data.currentUlocSeq+"&SN="+sn+"&toPorderAviId="+aviId+"&tmUlocId="+$("#ulocSelect").val();
				createwindow("绑定参数", url, 'OnlineBindParameterGridId',false);
			}
			if(data.bindQuality){
				var url = baseUrl+"/onLine/onlineNeedBindQuality.do?currentUlocSeq="+data.currentUlocSeq+"&SN="+sn+"&toPorderAviId="+aviId+"&tmUlocId="+$("#ulocSelect").val();
				createwindow("质量项检查", url, 'OnlineRecordQualityGridId');
			}
			return;
		}
		if(result.success){
			if($("#isAutoOnline").val()=="true"){
				confirmOnline();
			}else{
				infoTip(result.message);
			}
		}
	}, false);
}
/**
 * 生成SN
 * @returns
 */
function generateSN(){
	
	if(checkedAviId==""){
		infoTip("请选择生产序列!");
		return;
	}
	ajaxRequest(baseUrl+"/onLine/generateSN.do", {
		toPorderAviId:checkedAviId,
		tmUlocId:$("#ulocSelect").val()
	}, function(result){
		if(result.success){
			$("#snInput").val(result.data);
		}
		refreshTableAndWaitSNPanel();
	}, true); 
}
function refreshTableAndWaitSNPanel(){
	changePorder($("#porder").val(),checkedAviId);
	getWaitSN(checkedAviId);
}

function checkSN(){
	var data={
			isSuccess:false,
			currentSeq:"",
			aviId:""
	}
	if(!$("#snInput").val()){
		errorTip("请扫描SN!");
		return data;
	}
	var isSuccess = true;
	ajaxRequestSync(baseUrl+"/onLine/checkSN.do", {
		tmUlocId:$("#ulocSelect").val(),
		SN:$("#snInput").val()
	}, function(result){
		data.isSuccess = result.success;
		if(result.success){
			data.currentSeq= result.data.currentSeq;
			data.aviId = result.data.aviId;
		}
	}, false);
	return data;
}

/**
 * 绑定关键件
 * @returns
 */
function bindPart(){
	var check = checkSN();
	if(!check.isSuccess){
		return;
	}
	var currentSeq = check.currentSeq;
	var aviId = check.aviId;
	var url = baseUrl+"/onLine/onlineNeedBindPart.do?currentUlocSeq="+currentSeq+"&SN="+$("#snInput").val()+"&toPorderAviId="+aviId+"&tmUlocId="+$("#ulocSelect").val();
	createwindow("绑定关键件", url, 'OnlineBindPartGridId');
}
/**
 * 绑定参数
 * @returns
 */
function bindParameter(){
	var check = checkSN();
	if(!check.isSuccess){
		return;
	}
	var currentSeq = check.currentSeq;
	var aviId = check.aviId;
	
	var url = baseUrl+"/onLine/onlineNeedBindParameter.do?currentUlocSeq="+currentSeq+"&SN="+$("#snInput").val()+"&toPorderAviId="+aviId+"&tmUlocId="+$("#ulocSelect").val();
	createwindow("绑定参数", url, 'OnlineBindParameterGridId',false);
}
/**
 * 质检项检查
 * @returns
 */
function qualityCheck(){
	var check = checkSN();
	if(!check.isSuccess){
		return;
	}
	var currentSeq = check.currentSeq;
	var aviId = check.aviId;
	var url = baseUrl+"/onLine/onlineNeedBindQuality.do?currentUlocSeq="+currentSeq+"&SN="+$("#snInput").val()+"&toPorderAviId="+aviId+"&tmUlocId="+$("#ulocSelect").val();
	createwindow("质量项检查", url, 'OnlineRecordQualityGridId');
}
/**
 * 记录不合格
 * @returns
 */
function recordNC(){
	var check = checkSN();
	if(!check.isSuccess){
		return;
	}
	var currentSeq = check.currentSeq;
	var aviId = check.aviId;
	var url=baseUrl+"/onLine/recordNC.do?toPorderAviId="+aviId+"&currentUlocSeq="+currentSeq+"&tmUlocId="+$("#ulocSelect").val()+"&SN="+$("#snInput").val();
	createwindow("记录不合格", url, 'OnlineRecordNCGridId');
}
/**
 * 确认上线
 * @returns
 */
function confirmOnline(){
	var check = checkSN();
	if(!check.isSuccess){
		return;
	}
	var currentSeq = check.currentSeq;
	var aviId = check.aviId;
	ajaxRequest(baseUrl+"/onLine/confirmOnline.do", {
		toPorderAviId:aviId,
		tmUlocId:$("#ulocSelect").val(),
		SN:$("#snInput").val()
	}, function(result){
		var data = result.data;
		if(data){
			if(data.bindPart){
				var url = baseUrl+"/onLine/onlineNeedBindPart.do?currentUlocSeq="+data.currentUlocSeq+"&SN="+$("#snInput").val()+"&toPorderAviId="+aviId+"&tmUlocId="+$("#ulocSelect").val();
				createwindow("绑定关键件", url, 'OnlineBindPartGridId');
				return;
			}
			if(data.bindParameter){
				var url = baseUrl+"/onLine/onlineNeedBindParameter.do?currentUlocSeq="+data.currentUlocSeq+"&SN="+$("#snInput").val()+"&toPorderAviId="+aviId+"&tmUlocId="+$("#ulocSelect").val();
				createwindow("绑定参数", url, 'OnlineBindParameterGridId',false);
				return;
			}
			if(data.bindQuality){
				var url = baseUrl+"/onLine/onlineNeedBindQuality.do?currentUlocSeq="+data.currentUlocSeq+"&SN="+$("#snInput").val()+"&toPorderAviId="+aviId+"&tmUlocId="+$("#ulocSelect").val();
				createwindow("质量项检查", url, 'OnlineRecordQualityGridId');
				return;
			}
			/*if(data.RING_OFFLINE){
				var isOffline = confirm("环形工位，确认下线吗？");
				if(isOffline){
					
				}else{
					
				}
				return;
			}*/
		}
		if(result.success){
			$("#snInput").val("");
			refreshTableAndWaitSNPanel();
			$("#snInput").focus();
		}
	}, true); 
}

function printSN(){
	var check = checkSN();
	if(!check.isSuccess){
		return;
	}
	var url=baseUrl+"/onLine/printSn.do?SN="+$("#snInput").val();
 	window.open(url,"打印预览");
}

function printCard(){
	var aviId="";
	if($("#snInput").val()!=""){
		var check = checkSN();
		if(!check.isSuccess){
			return;
		}
		aviId = check.aviId;
	}else{
		if(checkedAviId==""){
			infoTip("请选择一个生产序列，或者扫描SN!");
		}else{
			aviId=checkedAviId;
		}
	}
	var url=baseUrl+"/avi/printTransferCard.do?id="+aviId;
	window.open(url,"打印预览");
}