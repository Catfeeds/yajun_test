/**
 * 产品过点JS
 */
var baseUrl = $("#baseUrl").val();
var isScan = false;
$(function(){
	ajaxRequest(baseUrl+"/crossPoint/initList.do", {}, function(result){
		if(result.success){
			var data = result.data;
			var lineData = data.lineData;
			var ulocData = data.ulocData;
			var select ="";
			$(lineData).each(function(i,item){
				select +="<option value="+item.id+">"+item.no+"-"+item.nameCn+"</option>";
			});
			if(select == ""){
				$("#crossPointLineSelect").html("<option value=''>没有产线权限</option>");
			}else{
				$("#crossPointLineSelect").html(select);
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
			var qty = data.qty;
			$("#qualityQty").text(qty.qualityQty);
			$("#unqualityQty").text(qty.unQualityQty);
			$("#shiftNo").text(data.shiftNo);
			var onLineData = data.onLineData;
			var wip = data.wip;
			getTableData(onLineData,wip);
		}
	}, false);
});


function getTableData(onLineData,wip){
	var table ="";
	$(wip).each(function(i,item){
		var isHold = item.isHold == "YES"? "是" : "否";
		table +="<tr id='"+item.id+"'><td>"+item.sn+"</td><td>"+item.aviNo+"</td><td>"+item.porderNo+"</td><td>"
						+item.partNo+"-"+item.partName+"</td><td>"+new Date(item.scanDate).Format("yyyy-MM-dd hh:mm:ss")+"</td>" +
							"<td>"+isHold+"</td></tr>";
	});
	$("#crossPointReachTable").html(table);
	table="";
	$(onLineData).each(function(i,item){
		var record = item.record;
		table+="<tr><td>"+item.record.sn+"</td><td>"+record.aviNo+"</td><td>"+record.porderNo+"</td><td>"+record.part.no+"-"+record.part.nameCn+"</td><td>"
					+item.nextUloc+"</td><td>"+new Date(record.onlineTime).Format("yyyy-MM-dd hh:mm:ss")+"</td></tr>";
	});
	$("#crossPointCrossTable").html(table);
}

function crossPointChangeLine(tmLineId){
	ajaxRequest(baseUrl+"/crossPoint/initList.do", {
		tmLineId:tmLineId
	}, function(result){
		if(result.success){
			var data = result.data;
			var ulocData = data.ulocData;
			var select ="";
			$(ulocData).each(function(i,item){
				select +="<option value="+item.id+">"+item.no+"-"+item.name+"</option>";
			});
			if(select == ""){
				$("#ulocSelect").html("<option value=''>没有工位权限</option>");
			}else{
				$("#ulocSelect").html(select);
			}
			var qty = data.qty;
			$("#qualityQty").text(qty.qualityQty?qty.qualityQty:0);
			$("#unqualityQty").text(qty.unQualityQty?qty.unQualityQty:0);
			$("#shiftNo").text(data.shiftNo);
			var onLineData = data.onLineData;
			var wip = data.wip;
			getTableData(onLineData,wip);
			$("#snInput").val("");
			$("#snInput").focus();
		}
	}, false);
}


function crossPointChangeUloc(tmUlocId){
	ajaxRequest(baseUrl+"/crossPoint/getListDataByUlocId.do", {
		tmUlocId:tmUlocId
	}, function(result){
		if(result.success){
			var data = result.data;
			var qty = data.qty;
			$("#qualityQty").text(qty.qualityQty);
			$("#unqualityQty").text(qty.unQualityQty);
			$("#shiftNo").text(data.shiftNo);
			getTableData(data.onLineData,data.wip);
			$("#snInput").val("");
			$("#snInput").focus();
		}
	}, false);
}

function crossPointCheckSN(){
	var obj = {
		isSuccess:false,
		currentSeq:"",
		aviId:"",
	};
	if(!$("#snInput").val()){
		errorTip("请扫描SN!");
		return obj;
	}
	if(!isScan){
		errorTip("请扫描SN!");
		return obj;
	}
	ajaxRequestSync(baseUrl+"/crossPoint/checkSN.do",{
		tmUlocId:$("#ulocSelect").val(),
		SN:$("#snInput").val()
	},function(result){
		obj.isSuccess = result.success;
		if(result.success){
			var data = result.data;
			obj.currentSeq=data.currentUlocSeq;
			obj.aviId=data.aviId;
		}
	},false);
	return obj;
}

function clickSN(sn){
	if(!$("#snInput").val()){
		errorTip("请扫描SN!");
		return;
	}
	ajaxRequest(baseUrl+"/crossPoint/scanSN.do", {
		tmUlocId:$("#ulocSelect").val(),
		SN:sn
	}, function(result){
		var data = result.data;
		if(data){
			var aviId = data.aviId;
			$("#partNo").text(data.partNo);
			$("#partName").text(data.partName);
			$("#porder").text(data.porderNo);
			if(data.bindPart){
				var url = baseUrl+"/onLine/onlineNeedBindPart.do?currentUlocSeq="+data.currentUlocSeq+"&SN="+sn+"&toPorderAviId="+aviId+"&tmUlocId="+$("#ulocSelect").val();
				createwindow("绑定关键件", url, 'OnlineBindPartGridId');
				return;
			}
			if(data.bindParameter){
				var url = baseUrl+"/onLine/onlineNeedBindParameter.do?currentUlocSeq="+data.currentUlocSeq+"&SN="+sn+"&toPorderAviId="+aviId+"&tmUlocId="+$("#ulocSelect").val()+"&data="+JSON.stringify(data.bindParameter);
				createwindow("绑定参数", url, 'OnlineBindParameterGridId',false);
				return;
			}
			if(data.bindQuality){
				var url = baseUrl+"/onLine/onlineNeedBindQuality.do?currentUlocSeq="+data.currentUlocSeq+"&SN="+sn+"&toPorderAviId="+aviId+"&tmUlocId="+$("#ulocSelect").val()+"&data="+JSON.stringify(data.bindQuality);
				createwindow("质量项检查", url, 'OnlineRecordQualityGridId');
				return;
			}
		}
		if(result.success){
			if($("#isAutoOnline").val()=="true"){
				confirmCrossPoint();
			}else{
				infoTip("没有需要绑定的数据，可以过点!");
			}
		}
	}, false);
}

function crossPointScanSN(sn){
	if(event.keyCode==13){
		isScan = true;
		clickSN(sn);
	}
}

/**
 * 绑定关键件
 * @returns
 */
function bindPart(){
	var check = crossPointCheckSN();
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
	var check = crossPointCheckSN();
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
	var check = crossPointCheckSN();
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
	var check = crossPointCheckSN();
	if(!check.isSuccess){
		return;
	}
	var currentSeq = check.currentSeq;
	var aviId = check.aviId;
	var url=baseUrl+"/onLine/recordNC.do?toPorderAviId="+aviId+"&currentUlocSeq="+currentSeq+"&tmUlocId="+$("#ulocSelect").val()+"&SN="+$("#snInput").val();
	createwindow("记录不合格", url, 'OnlineRecordNCGridId');
}

/**
 * 确认过点
 * @returns
 */
function confirmCrossPoint(){
	var check = crossPointCheckSN();
	if(!check.isSuccess){
		return;
	}
	var aviId = check.aviId;
	ajaxRequest(baseUrl+"/crossPoint/confirmCrossPoint.do",{
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
				var url = baseUrl+"/onLine/onlineNeedBindParameter.do?currentUlocSeq="+data.currentUlocSeq+"&SN="+$("#snInput").val()+"&toPorderAviId="+aviId+"&tmUlocId="+$("#ulocSelect").val()+"&data="+JSON.stringify(data.bindParameter);
				createwindow("绑定参数", url, 'OnlineBindParameterGridId',false);
				return;
			}
			if(data.bindQuality){
				var url = baseUrl+"/onLine/onlineNeedBindQuality.do?currentUlocSeq="+data.currentUlocSeq+"&SN="+$("#snInput").val()+"&toPorderAviId="+aviId+"&tmUlocId="+$("#ulocSelect").val()+"&data="+JSON.stringify(data.bindQuality);
				createwindow("质量项检查", url, 'OnlineRecordQualityGridId');
				return;
			}
		}
		if(result.success){
			$("#snInput").val("");
			$("#ulocSelect").change();
			$("#snInput").focus();
		}
	}, true);
}

function holdOrNotHold(){
	if(!$("#snInput").val()){
		errorTip("请扫描SN!");
		return;
	}
	var isHold = "";
	var wipId = "";
	var tmUlocId = $("#ulocSelect").val();
	ajaxRequest(baseUrl+"/crossPoint/holdCheckSn.do", {
		SN:$("#snInput").val(),
		tmUlocId:tmUlocId
	}, function(result){
		if(result.success){
			isHold = result.data.isHold;
			wipId = result.data.wipId;
			if(isHold == "YES"){
				var url = baseUrl+"/crossPoint/doCancelHold.do";
				BootstrapDialog.confirm({
					title : "解HOLD",
					message : "确认要解HOLD吗",
					closable : true,
					draggable : true,
					btnCancelLabel : commons_msg.btnCancel,
					btnOKLabel : commons_msg.btnConfirm,
					callback : function(result) {
						if (result) {
							ajaxRequest(url,{'id' :wipId},function(){
								crossPointChangeUloc($("#ulocSelect").val());
							});
						}
		            }
				 });
				
			}else{
				var url = baseUrl+"/crossPoint/doHoldInput.do?id="+wipId;
				createwindowCallBack("请输入HOLD理由", url, crossPointChangeUloc,tmUlocId);
			}
		}
	}, false);
	
}

