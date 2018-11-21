var flow=null;
var baseUrl=$("#baseUrl").val();
var partArray = new Array();
var parameterArray = new Array();
var sipArray = new Array();
var deleateTmPathUlocIds  = new Array();
$(function() {
	//var data =eval("({states:{rect4:{type:'start',text:{text:'开始'}, attr:{ x:409, y:10, width:50, height:50}, props:{text:{value:'开始'},temp1:{value:''},temp2:{value:''}}},rect5:{type:'task',text:{text:'任务1'}, attr:{ x:386, y:116, width:100, height:50}, props:{text:{value:'任务1'},assignee:{value:''},form:{value:''},desc:{value:''}}},rect6:{type:'fork',text:{text:'分支'}, attr:{ x:410, y:209, width:50, height:50}, props:{text:{value:'分支'},temp1:{value:''},temp2:{value:''}}},rect7:{type:'task',text:{text:'任务2'}, attr:{ x:192, y:317, width:100, height:50}, props:{text:{value:'任务2'},assignee:{value:''},form:{value:''},desc:{value:''}}},rect8:{type:'task',text:{text:'任务3'}, attr:{ x:385, y:317, width:100, height:50}, props:{text:{value:'任务3'},assignee:{value:''},form:{value:''},desc:{value:''}}},rect9:{type:'task',text:{text:'任务4'}, attr:{ x:556, y:320, width:100, height:50}, props:{text:{value:'任务4'},assignee:{value:''},form:{value:''},desc:{value:''}}},rect10:{type:'join',text:{text:'合并'}, attr:{ x:410, y:416, width:50, height:50}, props:{text:{value:'合并'},temp1:{value:''},temp2:{value:''}}},rect11:{type:'end',text:{text:'结束'}, attr:{ x:409, y:633, width:50, height:50}, props:{text:{value:'结束'},temp1:{value:''},temp2:{value:''}}},rect12:{type:'task',text:{text:'任务5'}, attr:{ x:384, y:528, width:100, height:50}, props:{text:{value:'任务5'},assignee:{value:''},form:{value:''},desc:{value:''}}}},paths:{path13:{from:'rect4',to:'rect5', dots:[],text:{text:'TO 任务1'},textPos:{x:37,y:-4}, props:{text:{value:''}}},path14:{from:'rect5',to:'rect6', dots:[],text:{text:'TO 分支'},textPos:{x:56,y:-1}, props:{text:{value:''}}},path15:{from:'rect6',to:'rect8', dots:[],text:{text:'TO 任务3'},textPos:{x:24,y:-5}, props:{text:{value:''}}},path16:{from:'rect8',to:'rect10', dots:[],text:{text:'TO 合并'},textPos:{x:41,y:8}, props:{text:{value:''}}},path17:{from:'rect10',to:'rect12', dots:[],text:{text:'TO 任务5'},textPos:{x:36,y:-5}, props:{text:{value:''}}},path18:{from:'rect12',to:'rect11', dots:[],text:{text:'TO 结束'},textPos:{x:32,y:0}, props:{text:{value:''}}},path19:{from:'rect6',to:'rect7', dots:[{x:244,y:232}],text:{text:'TO 任务2'},textPos:{x:0,y:-10}, props:{text:{value:'TO 任务2'}}},path20:{from:'rect7',to:'rect10', dots:[{x:242,y:435}],text:{text:'TO 合并'},textPos:{x:-3,y:17}, props:{text:{value:'TO 合并'}}},path21:{from:'rect6',to:'rect9', dots:[{x:607,y:234}],text:{text:'TO 任务4'},textPos:{x:0,y:-10}, props:{text:{value:'TO 任务4'}}},path22:{from:'rect9',to:'rect10', dots:[{x:607,y:439}],text:{text:'TO 合并'},textPos:{x:-8,y:16}, props:{text:{value:'TO 合并'}}}},props:{props:{name:{value:'新建流程'},key:{value:''},desc:{value:''}}}})");
	//var data = $("#tmPathId").val()?eval(getPathGrapJSON($("#tmPathId").val())):{};
	initPlantSelect();
	if($("#tmPlantId").val()){
		plantChange($("#tmPlantId").val());
		workshopChange($("#tmWorkshopId").val());
		lineChange($("#tmLineId").val());
		var data = $("#tmPathId").val()?eval(getPathGrapJSON($("#tmPathId").val())):{};
		_initFlow(data);
	}else{
		_initFlow({});
	}
});
function getPathGrapJSON(tmPathId){
	var data;
	$.ajax({
		url:baseUrl+"/path/getPathGrapData.do",
		type:"GET",
		dataType:"json",
		data:{
			tmPathId:tmPathId
		},
		async:false,
		success:function(result){
			data = result;
		}
	});
	return data;
}
function _initFlow(data) {
	console.log('_init flow begin');
	flow = $('#myflow').myflow({
						basePath : "",
						editable : true, // 是否可编辑
						restore : data,
						//nodeIdName : "objectId",
						onDeleteNode : function(objectId) {
							if (objectId) {
								if (window.confirm('你确定要删除这个节点么?')) {
									return true;
								} else {
									return false;
								}
							} else {
								return true;
							}
						},
						tools : {
							save : {
								onclick : function(data) {
									//$("#saveWaiting").show();
									//saveFlow(data, tracePathId);
									console.log('save:\n' + data);
									if(!jQuery.isEmptyObject(eval('(' + data+ ')').states)){
										savePathAndSons(data);
									}
									return true;
								}
							}
						}
					});
	$("#loadWaiting").hide();
	console.log('_init flow is done!');
}
function savePathAndSons(data){
	$("#coveDiv").show();
	ajaxRequest(baseUrl+"/path/doSavePathAndSons.do",{
		data:data,
		tmPathId:$("#tmPathId").val(),
		tmPlantId:$("#plantSelect").val(),
		tmWorkshopId:$("#workshopSelect").val(),
		tmLineId:$("#lineSelect").val(),
		partArray:JSON.stringify(partArray),
		parameterArray:JSON.stringify(parameterArray),
		sipArray:JSON.stringify(sipArray),
		deleateTmPathUlocIds:JSON.stringify(deleateTmPathUlocIds)
	},function(result){
		if(result.success){
			$.ajax({
		        url: baseUrl+"/path/showGrapPath.do",
		        cache: false,
		        global: false,
		        type: "GET",
		        dataType: "html",
		        async:true,
		        data : {
		        	"tmPathId":result.data
		        },
		        success: function(html){
		        	$("#coveDiv").hide();
		        	var conLen = html.length;
		    		$(".main-content").html(html);
		        }
		    });
			if ($("#menu-toggler").hasClass("display")) {
				$("#menu-toggler").removeClass("display")
				$("#sidebar").removeClass("display");
			}
		}else{
			$("#coveDiv").hide();
		}
	},true);
}
function initPlantSelect(){
	$.ajax({
		url:"path/getPlantDictEntry.do",
		type:"GET",
		dataType:"json",
		data:{},
		success:function(result){
			$("#plantSelect").empty();
			$("#plantSelect").append(" <option value=''>---请选择---</option>");
			$(result).each(function(i,item){
				$("#plantSelect").append(" <option  value='" + item.code + "'>"+item.name + "</option>");
			});
			if($("#tmPlantId").val()){
				$("#plantSelect").val($("#tmPlantId").val());
				$("#plantSelect").attr("disabled","disabled");
				$("#plantSelect").css("background-color","#eee");
			}
			
		}
	});
}

function plantChange(plantId){
	getWorkShopData(plantId);
	//getLineData(plantId);
	//getUlocData(plantId);
}
function workshopChange(workShopId){
	getLineData($("#plantSelect").val(),workShopId);
	//getUlocData($("#plantSelect").val(),workShipId);
}
function lineChange(lineId){
	getUlocData($("#plantSelect").val(),$("#workshopSelect").val(),lineId);
}
function getWorkShopData(plantId){
	$.ajax({
		url:"path/getWorkShopByPlantId.do",
		type:"GET",
		dataType:"json",
		data:{
			"plantId":plantId
		},
		success:function(result){
			$("#workshopSelect").empty();
			$("#workshopSelect").append(" <option value=''>---请选择---</option>");
			$(result).each(function(i,item){
				$("#workshopSelect").append(" <option  value='" + item.code + "'>"+item.name + "</option>");
			});
			if($("#tmWorkshopId").val()){
				$("#workshopSelect").val($("#tmWorkshopId").val());
				$("#workshopSelect").attr("disabled","disabled");
				$("#workshopSelect").css("background-color","#eee");
			}
		}
	});
}
function getLineData(plantId,workshopId){
	$.ajax({
		url:"path/getLineByWorkShopIdAndPlantId.do",
		type:"GET",
		dataType:"json",
		data:{
			"plantId":plantId,
			"workShopId":workshopId
		},
		success:function(result){
			$("#lineSelect").empty();
			$("#lineSelect").append(" <option value=''>---请选择---</option>");
			$(result).each(function(i,item){
				$("#lineSelect").append(" <option  value='" + item.code + "'>"+item.name + "</option>");
			});
			if($("#tmLineId").val()){
				$("#lineSelect").val($("#tmLineId").val());
				$("#lineSelect").attr("disabled","disabled");
				$("#lineSelect").css("background-color","#eee");
			}
		}
	});
}
function getUlocData(plantId,workshopId,lineId){
	$.ajax({
		url:"path/getUlocDictEntryMap.do",//before url = getUlocDictEntry
		type:"GET",
		dataType:"json",
		async:false,
		data:{
			"plantId":plantId,
			"workShopId":workshopId,
			"lineId":lineId
		},
		success:function(result){
			var ulocDivs="";
			$(result).each(function(i,item){
				ulocDivs+="<div class='node state' data-id="+item.code+" data-value="+item.name+" data-isSip="+item.isSip+" type='task'><img src='res/js/flow/img/16/task_empty.png'/>"
									+"&nbsp;&nbsp;"+item.name+"</div>";
			});
			$("#ulocPanel").html(ulocDivs);
			_initFlow({});
		}
	});
}
