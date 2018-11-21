
function getPieMonthEC(year,acEnergyArray,deviceEnergyArray,lightEnergyArray,label,myChart){
	var centerxs= [10,25,40,55,10,25,40,55,10,25,40,55,75];
	var centerys = [15,15,15,15,40,40,40,40,65,65,65,65,40];
	var product = ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月','总能耗'];
	var app = {};
	option = {
//		title:{
//			text: '能耗月报占比',
//            left: 'center'
//		},
	     legend:{
	        orient:'vertical',
	        right:2,
	        top:'40'
	     },
	     tooltip: {
	    	 trigger: 'item',
	         formatter: "{a} {b} :{d}%",
	         textStyle: {fontSize:14}
	     },
	     dataset: {
	        source: [
	            ['product', '一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月','总能耗'],
	            acEnergyArray,
	            deviceEnergyArray,
	            lightEnergyArray
	        ]
	    },
	    grid: [
	        {x: '',y: '13%', width: '65%', height: '10%'},
	        {x: '',y: '38%', width: '65%', height: '10%'},
	        {x: '',y: '63%', width: '65%', height: '10%'},
	        {x: '43%',y: '45%', width: '65%', height: '10%'}
	    ],
	    xAxis: [
	        {
	            gridIndex: 0,
	            type: 'category',
	            axisTick: { show: false },
	            axisLine:{show:false},
	            data: ['一月','二月','三月','四月']
	        },
	        {
	            gridIndex: 1,
	            type: 'category',
	            axisTick: {show: false },
	            axisLine:{show:false},
	            data: ['五月','六月','七月','八月']
	        },
	        {
	            gridIndex: 2,
	            type: 'category',
	            axisTick: {show: false},
	            axisLine:{show:false},
	            data: ['九月','十月','十一月','十二月']
	        },
	        {
	            gridIndex: 3,
	            type: 'category',
	            axisTick: {show: false},
	            axisLine:{show:false},
	            data: [year+'年']
	        }
	    ],
	    yAxis: [
	        {gridIndex: 0,name:'月份',show:false},
	        {gridIndex: 1,name:'月份',show:false},
	        {gridIndex: 2,name:'月份',show:false},
	        {gridIndex: 3,name:'月份',show:false}
	    ],
	    toolbox: {
	        feature: {
	        	myTool : {
                 show : true,
                 title : '是否显示百分比',
                 icon : 'image://'+$('#baseUrl').val()+'/res/images/percent.png',
                 onclick : function (event){
                	 var label = {
		                		normal: {
            	                    fontSize:'8px',
            	                    formatter: '{d}%',
            	                    position: 'inside'
            	                }
		                	};
                	 if(app.isPercent){
                		 app.isPercent = false;
                		 myChart.setOption(getPieMonthEC(year,acEnergyArray,deviceEnergyArray,lightEnergyArray,false,myChart),true);
                	 }else{
                		 app.isPercent = true;
                		 myChart.setOption(getPieMonthEC(year,acEnergyArray,deviceEnergyArray,lightEnergyArray,label,myChart),true);
                	 }
                 }
	        	},
	            dataView: {show: true, readOnly: false}, 
	            restore: {show: true},
	            saveAsImage: {show: true}
	        }
	    },
	    series:centerxs.map(function (item, idx) {
	         return{
	            name: product[idx],
	            type: 'pie',
	            radius :idx==12?'30%':'12%',
	            center: [centerxs[idx]+'%', centerys[idx]+'%'],
	            label:label,
	            itemStyle: {
	                emphasis: {
	                    shadowBlur: 10,
	                    shadowOffsetX: 0,
	                    shadowColor: 'rgba(0, 0, 0, 0.5)'
	                }
	            }, 
	            encode: {
	                itemName: 'product',
	                value: product[idx]
	            }
	         }
	    })
	};
	return option;
}

function getTimeline(options){
   if(!options && !options.modelId)return false;
	var myChart = echarts.init(document.getElementById(options.id));
	 myChart.on('click', function (param) {
		 if(options.listeningFunction){
			 window[options.listeningFunction](param.name,options.params);
		 }
	 }); 
	var option = {
		    timeline: {
		        axisType: 'category',
		        show: true,
		        autoPlay: false,
		        controlStyle:{
		            show:options.controlShow
		         },
		         lineStyle:options.lineStyle?options.lineStyle:{},
		         label:options.label?options.label:{},
		         playInterval:options.playInterval?options.playInterval:3000,
		         data: options.data//['2018','2019','2020','2021','2022','2023','2024','2025','2026','2027']
		    }
		};
	 myChart.setOption(option,true);
}
