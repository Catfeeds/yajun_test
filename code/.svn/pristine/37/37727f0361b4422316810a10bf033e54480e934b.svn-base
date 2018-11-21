function ajaxRequest(url, data, callback, showSuccess, showErrorTip) {
	data = data ? data : {};
	showSuccess = showSuccess != null ? showSuccess : true;
	$.post(url, data, function(result) {
		if (result.success) {
			if (showSuccess) {
				infoTip("操作执行成功!");
			}
			if (callback) {
				callback(result);
			}
		} else {
			if (!showErrorTip) {
				errorTip(result.message);
			}
			if (callback) {
				callback(result);
			}
		}
	});
}

function ajaxRequestSync(url,data,callback,showSuccess){
	data = data ? data : {};
	showSuccess = showSuccess != null ? showSuccess : true;
	$.ajax({
		url:url,
		type:"post",
		data:data,
		dataType:"json",
		async:false,
		success:function(result){
			if (result.success) {
				if (showSuccess) {
					infoTip("操作执行成功!");
				}
				if (callback) {
					callback(result);
				}
			} else {
				if (callback) {
					callback(result);
				}
			}
		}
	});
}

function tip(title, message, iconUrl, sticky) {
	return $.gritter.add({
		title : title,
		text : message,
		image : iconUrl,
		sticky : sticky,
		time : 2000,
		speed : 500,
		position : 'bottom-right',
		class_name : 'gritter-info'// gritter-center
	});
}
function warningTip(message) {
	tip("出错啦", message,$('#_baseUrl').val()+'/res/bootstrap/css/images/button-error-01.png', false);
}
function infoTip(message) {
	tip("提示", message,$('#_baseUrl').val()+'/res/bootstrap/css/images/button-info-01.png', false);
}

function heartbeatConnection (){
	var ws;
	var lockReconnect = false;//避免重复连接
	var wsUrl ="ws://192.168.90.12/mes/websocket";
	function createWebSocket(url) {
	    try {
	        ws = new WebSocket(url);
	        initEventHandle();
	    } catch (e) {
	        reconnect(url);
	    }     
	}
	function initEventHandle() {
	    ws.onclose = function () {
	        reconnect(wsUrl);
	    };
	    ws.onerror = function () {
	        reconnect(wsUrl);
	    };
	    ws.onopen = function () {
	        //心跳检测重置
	        heartCheck.reset().start();
	    };
	    ws.onmessage = function (event) {
	    	var obj = event.data;
	        //如果获取到消息，心跳检测重置//拿到任何消息都说明当前连接是正常的
	    	if(obj && obj != 'HeartBeat'){
	    		var AT_KEY = "AT_THE_TABLE_SIGNAL";
	    		var SN_KEY = 'UNIQUE_IDENTIFICATION_CODE';
	    		if(obj.indexOf(AT_KEY) != -1){
		    		atTheTableSignal(obj.substring(AT_KEY.length,obj.length));
		    	}else if(obj.indexOf(SN_KEY) != -1){
		    		readSn(obj.substring(SN_KEY.length,obj.length));
		    	}
	    	}
	        heartCheck.reset().start();
	    }
	}
	function reconnect(url) {
	    if(lockReconnect) return;
	    lockReconnect = true;
	    //没连接上会一直重连，设置延迟避免请求过多
	    setTimeout(function () {
	        createWebSocket(url);
	        lockReconnect = false;
	    }, 2000);
	}
	//心跳检测
	var heartCheck = {
	    timeout: 60000,//60秒
	    timeoutObj: null,
	    reset: function(){
	        clearTimeout(this.timeoutObj);
	        return this;
	    },
	    start: function(){
	        this.timeoutObj = setTimeout(function(){
	            //这里发送一个心跳，后端收到后，返回一个心跳消息，
	            //onmessage拿到返回的心跳就说明连接正常
	            ws.send("HeartBeat");
	        }, this.timeout)
	    }
	}
	createWebSocket(wsUrl);
}

function sleep(numberMillis) {  
    var now = new Date();  
    var exitTime = now.getTime() + numberMillis;  
    while (true) {  
        now = new Date();  
        if (now.getTime() > exitTime)  
        return;  
        }  
	} 