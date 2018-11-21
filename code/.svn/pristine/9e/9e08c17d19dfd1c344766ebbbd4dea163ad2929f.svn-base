(function($){
var myflow = $.myflow;
var baseUrl=$("#baseUrl").val();
$.extend(true,myflow.config.rect,{
	attr : {
		r : 8,
		fill : '#F6F7FF',
		stroke : '#03689A',
		"stroke-width" : 2
	}
});

$.extend(true,myflow.config.path,{
	attr : {
		path: {
            path: "M10 10L100 100",
            stroke: "#808080",
            fill: "none",
            "stroke-width": 2
        },
        arrow: {
            path: "M10 10L10 10",
            stroke: "#808080",
            fill: "#808080",
            "stroke-width": 2,
            radius: 4
        }
	}
});

$.extend(true,myflow.config.props.props,{
	tmPathId:{name:'tmPathId',label:'NULL',value:''},
	pathNo : {name:'pathNo', label:'编号', value:'', editor:function(){return new myflow.editors.inputEditor();}},
	pathName : {name:'pathName', label:'名称', value:'', editor:function(){return new myflow.editors.inputEditor();}},
	part : {name:'part', label:'物料', value:'', editor:function(){return new myflow.editors.selectEditor('path/getPartByPlantId.do',{plantId:$("#plantSelect").val()});}},
	enabled:{name:'enabled',label:'启用',value:'ON',editor: function(){return new myflow.editors.selectEditor('path/getEnabledDictEntry.do',{});}}
});


$.extend(true,myflow.config.tools.states,{
			start : {
				showType: 'image',
				type : 'start',
				name : {text:'<<start>>'},
				text : {text:'开始'},
				img : {src : baseUrl+'/res/js/flow/img/48/start_event_empty.png',width : 48, height:48},
				attr : {width:50 ,heigth:50 },
				props : {
					text: {name:'text',label: '显示', value:'', editor: function(){return new myflow.editors.textEditor();}, value:'开始'},
					temp1: {name:'temp1', label : '文本', value:'', editor: function(){return new myflow.editors.inputEditor();}},
					temp2: {name:'temp2', label : '选择', value:'', editor: function(){return new myflow.editors.selectEditor([{name:'aaa',value:1},{name:'bbb',value:2}]);}}
				}},
			end : {showType: 'image',type : 'end',
				name : {text:'<<end>>'},
				text : {text:'结束'},
				img : {src : baseUrl+'/res/js/flow/img/48/end_event_terminate.png',width : 48, height:48},
				attr : {width:50 ,heigth:50 },
				props : {
					text: {name:'text',label: '显示', value:'', editor: function(){return new myflow.editors.textEditor();}, value:'结束'},
					temp1: {name:'temp1', label : '文本', value:'', editor: function(){return new myflow.editors.inputEditor();}},
					temp2: {name:'temp2', label : '选择', value:'', editor: function(){return new myflow.editors.selectEditor([{name:'aaa',value:1},{name:'bbb',value:2}]);}}
				}},
			'end-cancel' : {showType: 'image',type : 'end-cancel',
				name : {text:'<<end-cancel>>'},
				text : {text:'取消'},
				img : {src : baseUrl+'/res/js/flow/img/48/end_event_cancel.png',width : 48, height:48},
				attr : {width:50 ,heigth:50 },
				props : {
					text: {name:'text',label: '显示', value:'', editor: function(){return new myflow.editors.textEditor();}, value:'取消'},
					temp1: {name:'temp1', label : '文本', value:'', editor: function(){return new myflow.editors.inputEditor();}},
					temp2: {name:'temp2', label : '选择', value:'', editor: function(){return new myflow.editors.selectEditor([{name:'aaa',value:1},{name:'bbb',value:2}]);}}
				}},
			'end-error' : {showType: 'image',type : 'end-error',
				name : {text:'<<end-error>>'},
				text : {text:'错误'},
				img : {src : baseUrl+'/res/js/flow/img/48/end_event_error.png',width : 48, height:48},
				attr : {width:50 ,heigth:50 },
				props : {
					text: {name:'text',label: '显示', value:'', editor: function(){return new myflow.editors.textEditor();}, value:'错误'},
					temp1: {name:'temp1', label : '文本', value:'', editor: function(){return new myflow.editors.inputEditor();}},
					temp2: {name:'temp2', label : '选择', value:'', editor: function(){return new myflow.editors.selectEditor([{name:'aaa',value:1},{name:'bbb',value:2}]);}}
				}},
			state : {showType: 'text',type : 'state',
				name : {text:'<<state>>'},
				text : {text:'状态'},
				img : {src : baseUrl+'/res/js/flow/img/48/task_empty.png',width : 48, height:48},
				props : {
					text: {name:'text',label: '显示', value:'', editor: function(){return new myflow.editors.textEditor();}, value:'状态'},
					temp1: {name:'temp1', label : '文本', value:'', editor: function(){return new myflow.editors.inputEditor();}},
					temp2: {name:'temp2', label : '选择', value:'', editor: function(){return new myflow.editors.selectEditor([{name:'aaa',value:1},{name:'bbb',value:2}]);}}
				}},
			fork : {showType: 'image',type : 'fork',
				name : {text:'<<fork>>'},
				text : {text:'分支'},
				img : {src : baseUrl+'/res/js/flow/img/48/gateway_parallel.png',width :48, height:48},
				attr : {width:50 ,heigth:50 },
				props : {
					text: {name:'text', label: '显示', value:'', editor: function(){return new myflow.editors.textEditor();}, value:'分支'},
					temp1: {name:'temp1', label: '文本', value:'', editor: function(){return new myflow.editors.inputEditor();}},
					temp2: {name:'temp2', label : '选择', value:'', editor: function(){return new myflow.editors.selectEditor('select.json');}}
				}},
			join : {showType: 'image',type : 'join',
				name : {text:'<<join>>'},
				text : {text:'合并'},
				img : {src : baseUrl+'/res/js/flow/img/48/gateway_parallel.png',width :48, height:48},
				attr : {width:50 ,heigth:50 },
				props : {
					text: {name:'text', label: '显示', value:'', editor: function(){return new myflow.editors.textEditor();}, value:'合并'},
					temp1: {name:'temp1', label: '文本', value:'', editor: function(){return new myflow.editors.inputEditor();}},
					temp2: {name:'temp2', label : '选择', value:'', editor: function(){return new myflow.editors.selectEditor('select.json');}}
				}},
				task : {   
					showType: 'text',
					type : 'task',
					name : {text:'<<task>>'},
					text : {text:''},
					id:{value:''},
					img : {src : baseUrl+'/res/js/flow/img/48/task_empty.png',width :48, height:48},
					props : {
						ulocNo: {name:'ulocNo', label: '工位', value:'', editor: function(){return new myflow.editors.inputEditor(true);}},
						isOnline:{name:'isOnline', label : '是否上线点', value:'', editor: function(){return new myflow.editors.selectEditor('path/getYesOrNoDictEntry.do');}},
						isOffline:{name:'isOffline', label : '是否下线点', value:'', editor: function(){return new myflow.editors.selectEditor('path/getYesOrNoDictEntry.do');}},
						isSip:{name:'isSip', label : '是否质检点', value:'NO', editor: function(){return new myflow.editors.selectEditor('path/getYesOrNoDictEntry.do');}},
						isAutoInstorage:{name:'isAutoInstorage', label : '是否自动入库', value:'NO', editor: function(){return new myflow.editors.selectEditor('path/getYesOrNoDictEntry.do');}},
						isReported:{name:'isReported', label : '是否报工点', value:'NO', editor: function(){return new myflow.editors.selectEditor('path/getYesOrNoDictEntry.do');}},
						erpUlocCode:{name:'erpUlocCode', label: '对应ERP工序号', value:'', editor: function(){return new myflow.editors.inputEditor();}},
						operateTime:{name:'operateTime', label: '操作时间', value:'', editor: function(){return new myflow.editors.inputEditor();}},
						intervalTime:{name:'intervalTime', label: '间隔通过时间', value:'', editor: function(){return new myflow.editors.inputEditor();}},
						inStroageTime:{name:'inStroageTime', label: '入库等待时间', value:'', editor: function(){return new myflow.editors.inputEditor();}},
						note:{name:'note', label: '备注', value:'', editor: function(){return new myflow.editors.inputEditor();}},
						ulocId:{name:'ulocId', label: 'NULL', value:''},
						tmPathUlocId:{name:'tmPathUlocId',label:'NULL',value:''}
			}},
			online : {
				showType: 'image',
				type : 'online',
				name : {text:'<<online>>'},
				text : {text:''},
				img : {src : baseUrl+'/res/js/flow/img/48/path_online.png',width : 48, height:48},
				attr : {width:50 ,heigth:50 },
				props : {
					ulocNo: {name:'ulocNo', label: '工位', value:'', editor: function(){return new myflow.editors.inputEditor(true);}},
					isOnline:{name:'isOnline', label : '是否上线点', value:'', editor: function(){return new myflow.editors.selectEditor('path/getYesOrNoDictEntry.do');}},
					isOffline:{name:'isOffline', label : '是否下线点', value:'', editor: function(){return new myflow.editors.selectEditor('path/getYesOrNoDictEntry.do');}},
					isSip:{name:'isSip', label : '是否质检点', value:'NO', editor: function(){return new myflow.editors.selectEditor('path/getYesOrNoDictEntry.do');}},
					isAutoInstorage:{name:'isAutoInstorage', label : '是否自动入库', value:'NO', editor: function(){return new myflow.editors.selectEditor('path/getYesOrNoDictEntry.do');}},
					isReported:{name:'isReported', label : '是否报工点', value:'NO', editor: function(){return new myflow.editors.selectEditor('path/getYesOrNoDictEntry.do');}},
					erpUlocCode:{name:'erpUlocCode', label: '对应ERP工序号', value:'', editor: function(){return new myflow.editors.inputEditor();}},
					operateTime:{name:'operateTime', label: '操作时间', value:'', editor: function(){return new myflow.editors.inputEditor();}},
					intervalTime:{name:'intervalTime', label: '间隔通过时间', value:'', editor: function(){return new myflow.editors.inputEditor();}},
					inStroageTime:{name:'inStroageTime', label: '入库等待时间', value:'', editor: function(){return new myflow.editors.inputEditor();}},
					note:{name:'note', label: '备注', value:'', editor: function(){return new myflow.editors.inputEditor();}},
					ulocId:{name:'ulocId', label: 'NULL', value:''},
					tmPathUlocId:{name:'tmPathUlocId',label:'NULL',value:''}
				}},
			offline : {
				showType: 'image',
				type : 'offline',
				name : {text:'<<offline>>'},
				text : {text:''},
				img : {src : baseUrl+'/res/js/flow/img/48/path_offline.png',width : 48, height:48},
				attr : {width:50 ,heigth:50 },
				props : {
					ulocNo: {name:'ulocNo', label: '工位', value:'', editor: function(){return new myflow.editors.inputEditor(true);}},
					isOnline:{name:'isOnline', label : '是否上线点', value:'', editor: function(){return new myflow.editors.selectEditor('path/getYesOrNoDictEntry.do');}},
					isOffline:{name:'isOffline', label : '是否下线点', value:'', editor: function(){return new myflow.editors.selectEditor('path/getYesOrNoDictEntry.do');}},
					isSip:{name:'isSip', label : '是否质检点', value:'NO', editor: function(){return new myflow.editors.selectEditor('path/getYesOrNoDictEntry.do');}},
					isAutoInstorage:{name:'isAutoInstorage', label : '是否自动入库', value:'NO', editor: function(){return new myflow.editors.selectEditor('path/getYesOrNoDictEntry.do');}},
					isReported:{name:'isReported', label : '是否报工点', value:'NO', editor: function(){return new myflow.editors.selectEditor('path/getYesOrNoDictEntry.do');}},
					erpUlocCode:{name:'erpUlocCode', label: '对应ERP工序号', value:'', editor: function(){return new myflow.editors.inputEditor();}},
					operateTime:{name:'operateTime', label: '操作时间', value:'', editor: function(){return new myflow.editors.inputEditor();}},
					intervalTime:{name:'intervalTime', label: '间隔通过时间', value:'', editor: function(){return new myflow.editors.inputEditor();}},
					inStroageTime:{name:'inStroageTime', label: '入库等待时间', value:'', editor: function(){return new myflow.editors.inputEditor();}},
					note:{name:'note', label: '备注', value:'', editor: function(){return new myflow.editors.inputEditor();}},
					ulocId:{name:'ulocId', label: 'NULL', value:''},
					tmPathUlocId:{name:'tmPathUlocId',label:'NULL',value:''}
				}},
			sip : {
				showType: 'image',
				type : 'sip',
				name : {text:'<<sip>>'},
				text : {text:''},
				img : {src : baseUrl+'/res/js/flow/img/48/path_sip.png',width : 48, height:48},
				attr : {width:50 ,heigth:50 },
				props : {
					ulocNo: {name:'ulocNo', label: '工位', value:'', editor: function(){return new myflow.editors.inputEditor(true);}},
					isOnline:{name:'isOnline', label : '是否上线点', value:'', editor: function(){return new myflow.editors.selectEditor('path/getYesOrNoDictEntry.do');}},
					isOffline:{name:'isOffline', label : '是否下线点', value:'', editor: function(){return new myflow.editors.selectEditor('path/getYesOrNoDictEntry.do');}},
					isSip:{name:'isSip', label : '是否质检点', value:'NO', editor: function(){return new myflow.editors.selectEditor('path/getYesOrNoDictEntry.do');}},
					isAutoInstorage:{name:'isAutoInstorage', label : '是否自动入库', value:'NO', editor: function(){return new myflow.editors.selectEditor('path/getYesOrNoDictEntry.do');}},
					isReported:{name:'isReported', label : '是否报工点', value:'NO', editor: function(){return new myflow.editors.selectEditor('path/getYesOrNoDictEntry.do');}},
					erpUlocCode:{name:'erpUlocCode', label: '对应ERP工序号', value:'', editor: function(){return new myflow.editors.inputEditor();}},
					operateTime:{name:'operateTime', label: '操作时间', value:'', editor: function(){return new myflow.editors.inputEditor();}},
					intervalTime:{name:'intervalTime', label: '间隔通过时间', value:'', editor: function(){return new myflow.editors.inputEditor();}},
					inStroageTime:{name:'inStroageTime', label: '入库等待时间', value:'', editor: function(){return new myflow.editors.inputEditor();}},
					note:{name:'note', label: '备注', value:'', editor: function(){return new myflow.editors.inputEditor();}},
					ulocId:{name:'ulocId', label: 'NULL', value:''},
					tmPathUlocId:{name:'tmPathUlocId',label:'NULL',value:''}
				}},	
			scan : {
				showType: 'image',
				type : 'scan',
				name : {text:'<<scan>>'},
				text : {text:''},
				img : {src : baseUrl+'/res/js/flow/img/48/path_scan.png',width : 48, height:48},
				attr : {width:50 ,heigth:50 },
				props : {
					ulocNo: {name:'ulocNo', label: '工位', value:'', editor: function(){return new myflow.editors.inputEditor(true);}},
					isOnline:{name:'isOnline', label : '是否上线点', value:'', editor: function(){return new myflow.editors.selectEditor('path/getYesOrNoDictEntry.do');}},
					isOffline:{name:'isOffline', label : '是否下线点', value:'', editor: function(){return new myflow.editors.selectEditor('path/getYesOrNoDictEntry.do');}},
					isSip:{name:'isSip', label : '是否质检点', value:'NO', editor: function(){return new myflow.editors.selectEditor('path/getYesOrNoDictEntry.do');}},
					isAutoInstorage:{name:'isAutoInstorage', label : '是否自动入库', value:'NO', editor: function(){return new myflow.editors.selectEditor('path/getYesOrNoDictEntry.do');}},
					isReported:{name:'isReported', label : '是否报工点', value:'NO', editor: function(){return new myflow.editors.selectEditor('path/getYesOrNoDictEntry.do');}},
					erpUlocCode:{name:'erpUlocCode', label: '对应ERP工序号', value:'', editor: function(){return new myflow.editors.inputEditor();}},
					operateTime:{name:'operateTime', label: '操作时间', value:'', editor: function(){return new myflow.editors.inputEditor();}},
					intervalTime:{name:'intervalTime', label: '间隔通过时间', value:'', editor: function(){return new myflow.editors.inputEditor();}},
					inStroageTime:{name:'inStroageTime', label: '入库等待时间', value:'', editor: function(){return new myflow.editors.inputEditor();}},
					note:{name:'note', label: '备注', value:'', editor: function(){return new myflow.editors.inputEditor();}},
					ulocId:{name:'ulocId', label: 'NULL', value:''},
					tmPathUlocId:{name:'tmPathUlocId',label:'NULL',value:''}
			}}
	});
})(jQuery);