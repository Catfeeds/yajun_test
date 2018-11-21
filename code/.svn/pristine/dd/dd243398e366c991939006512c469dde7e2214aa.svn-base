var sessionStorage = window.sessionStorage;  
var lang = sessionStorage.getItem('lang')
var typeMap;
function entryRenderer(key, value) {
	if (!typeMap) {
		var entrys = sessionStorage.getItem('entrys');
		if (entrys) {
			typeMap = eval("(" + entrys + ")");
		} else {
			return function(value) {
				return value;
			};
		} 
	}
	var entryMap = typeMap[key];
	if (entryMap != null && entryMap[value] != null) {
		return entryMap[value][lang];
	} else {
		return '';
	}
}
function entryRendererMulti(key, value) {
	if (!typeMap) {
		var entrys = sessionStorage.getItem('entrys');
		if (entrys) {
			typeMap = eval("(" + entrys + ")");
		} else {
			return function(value) {
				return value;
			};
		}
	}
	var entryMap = typeMap[key];
	var entryValues = "";
	if (value != null && value != "") {
		var values = value.split(",");
		for (var i = 0; i < values.length; i++) {
			if (entryMap != null && entryMap[values[i]] != null) {
				entryValues=entryValues!=""?entryValues+",":entryValues;
				entryValues+=entryMap[values[i]][lang];
			}
		}
	}
	return entryValues;
}

function keysValue(arrays,value){
	if(value){
		var vals = value.split(',');
		var srcs = "";
		for(var i in vals){
			for(var j in arrays){
				if(vals[i]== arrays[j].code){
					srcs += arrays[j].name +",";
					break;
				}
			}
		}
		if(srcs){
			return srcs.substring(0,srcs.length-1);
		}
	}
}
//初始化下拉框
function initSelect(selectId,defaultVal,items){
	  var objSelect=document.getElementById(selectId);
	  objSelect.innerHTML="<option value=''>请选择</option>";
	  if(items){
		  for(var i in items){
			  var objOption = document.createElement("OPTION");
			  objOption.text= items[i].name;
			  objOption.value=items[i].code;
			  if(defaultVal && defaultVal==items[i].code){
				  objOption.selected="selected";
			  }
			  objSelect.options.add(objOption);
		  }
	  }
}
//分页
function  loadData(options,callback){
		var queryParams = {};
		if(options.apiUrl){
			var values = options.formData;
	        for (let i in values) {
	            if (values[i] != null && values[i] != '' && values[i].length > 0) {
	                queryParams['queryCondition[' + i + ']'] = values[i];
	            }
	        }
	        if (options.pageSize) {
		        queryParams.offset = (options.currentPage - 1) * options.pageSize;
		        queryParams.limit = options.pageSize;
		    }
	     mui.ajax({
       	    type: "post",
            dataType: "json",
            contentType:'application/x-www-form-urlencoded',//ajax submit 编码
	    	    url: options.apiUrl,
				data:queryParams,
				success:function(r){
					var  searchList = r.rows;
					options.searchSongList = searchList;
					if(searchList.length > 0){
						if(r.currentPage == r.totalPage){//当前页等于总页数，代表已经是最后一页了，底部显示暂无数据
						  options.scrollData.noFlag = true;
						}
					  mui.toast(r.currentPage+"/"+r.totalPage,{ duration:'short', type:'div' });
					}else{
						//没有更多数据了
						 options.scrollData.noFlag = true;
					}
					options.currentPage++;
					callback();
				}
			})
		}
 }