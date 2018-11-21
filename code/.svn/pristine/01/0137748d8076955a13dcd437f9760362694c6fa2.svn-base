(function($){
var myflow = $.myflow;

$.extend(true, myflow.editors, {
	inputEditor : function(isReadOnly){
		var _props,_k,_div,_src,_r,readOnly=false;
		this.init = function(props, k, div, src, r){
			//update by liuzejun
			if(k=="pathNo"&&$("#tmPathId").val()!=""){
				isReadOnly=true;
			}
			_props=props; _k=k; _div=div; _src=src; _r=r;
			readOnly=_props[_k].readOnly  || false;
			readOnly = isReadOnly;
			$('<input style="width:100%;" />').val(props[_k].value).attr("readOnly",readOnly).change(function(){
				props[_k].value = $(this).val();
			}).appendTo('#'+_div);
			
			$('#'+_div).data('editor', this);
		}
		this.destroy = function(){
			$('#'+_div+' input').each(function(){
				_props[_k].value = $(this).val();
			});
		}
	},
	selectEditor : function(arg,datas){
		var _props,_k,_div,_src,_r;
		this.init = function(props, k, div, src, r){
			_props=props; _k=k; _div=div; _src=src; _r=r;

			var sle = $('<select  style="width:100%;"/>').val(props[_k].value).change(function(){
				props[_k].value = $(this).val();
			}).appendTo('#'+_div);
			datas = datas ? datas : {};
			if(typeof arg === 'string'){
				$.ajax({
				   type: "GET",
				   url: arg,
				   data:datas,
				   success: function(data){
					  var opts = eval(data);
					  sle.append('<option value="">-----请选择-----</option>'); 
					 if(opts && opts.length){
						for(var idx=0; idx<opts.length; idx++){
							sle.append('<option value="'+opts[idx].code+'">'+opts[idx].name+'</option>');
						}
						sle.val(_props[_k].value);
					 }
				   }
				});
			}else {
				for(var idx=0; idx<arg.length; idx++){
					sle.append('<option value="'+arg[idx].value+'">'+arg[idx].name+'</option>');
				}
				sle.val(_props[_k].value);
			}
			
			$('#'+_div).data('editor', this);
		};
		this.destroy = function(){
			$('#'+_div+' input').each(function(){
				_props[_k].value = $(this).val();
			});
		};
	}
});

})(jQuery);