
if(processDelay()){
	
	$(document).ready(function() {
		getCookie();
		onfocus();
		$(".on_off_checkbox").iphoneStyle();
		$('.tip a ').tipsy({
			gravity : 'sw'
		});
		$('#login').show().animate({
			opacity : 1
		}, 2000);
		$('.logo').show().animate({
			opacity : 1,
			top : '1%'
		}, 800, function() {
			$('.logo').show().delay(1200).animate({
				opacity : 1,
				top : '1%'
			}, 300, function() {
				$('.formLogin').animate({
					opacity : 1,
					left : '0'
				}, 300);
				$('.userbox').animate({
					opacity : 0
				}, 200).hide();
			});
		});
		showError(msg,500); 
		setTimeout("hideTop()", 3000);
	});
}
$('.userload').click(function(e) {
	$('.formLogin').animate({
		opacity : 1,
		left : '0'
	}, 300);
	$('.userbox').animate({
		opacity : 0
	}, 200, function() {
		$('.userbox').hide();
	});
});
// 重置
$('#forgetpass').click(function(e) {
	$(":input").each(function() {
	$('#'+this.name).val("");
	});
});
// 点击登录
$('#but_login').click(function(e) {
	submit();
});
//回车登录
$(document).keydown(function(e){
	if(e.keyCode == 13) {
		submit();
	}
});
//表单提交
function submit()
{
	var submit = true;
	$("input[nullmsg]").each(function() {
		if ($("#" + this.name).val() == "") {
			showError($("#" + this.name).attr("nullmsg"), 500);
			jrumble();
			setTimeout('hideTop()', 1000);
			submit = false;
			return false;
		}
	});
	if (submit) {
		hideTop();
		loading('Landing..', 1);
		setTimeout("unloading()", 1000);
		setTimeout("Login()", 1000);
	}

}
//登录处理函数
function Login() {
	setCookie();
	$("#formLogin").submit();
	
}
//设置cookie
function setCookie()
{
	if ($('#on_off').val() == '1') {
		$("input[iscookie='true']").each(function() {
			$.cookie(this.name, $("#"+this.name).val(), "/",24);
			$.cookie("COOKIE_NAME","true", "/",24);
		});
	} else {
		$("input[iscookie='true']").each(function() {
			$.cookie(this.name,null);
			$.cookie("COOKIE_NAME",null);
		});
	}
}
//读取cookie
function getCookie()
{
	var COOKIE_NAME=$.cookie("COOKIE_NAME");
	if (COOKIE_NAME !=null) {
		$("input[iscookie='true']").each(function() {
			$($("#"+this.name).val( $.cookie(this.name)));
		});
		$("#on_off").attr("checked", true);
		$("#on_off").val("1");
	} 
	else
	{
		$("#on_off").attr("checked", false);
		$("#on_off").val("0");
	}
}
//点击消息关闭提示
$('#alertMessage').click(function() {
	hideTop();
});
//显示错误提示
function showError(str,timeOut) {
	$('#alertMessage').addClass('error').html(str).stop(true, true).show().animate({
		opacity : 1,
		right : '0'
	}, timeOut);

}

function onfocus() {
	if ($(window).width() > 480) {
		$('.tip input').tipsy({
			trigger : 'focus',
			gravity : 'w',
			live : true
		});
	} else {
		$('.tip input').tipsy("hide");
	}
}

function hideTop() {
	$('#alertMessage').animate({
		opacity : 0,
		right : '-20'
	}, 500, function() {
		$(this).hide();
	});
}
//加载信息
function loading(name, overlay) {
	$('body').append('<div id="overlay"></div><div id="preloader">' + name + '..</div>');
	if (overlay == 1) {
		$('#overlay').css('opacity', 0.1).fadeIn(function() {
			$('#preloader').fadeIn();
		});
		return false;
	}
	$('#preloader').fadeIn();
}

function unloading() {
	$('#preloader').fadeOut('fast', function() {
		$('#overlay').fadeOut();
	});
}
// 表单晃动
function jrumble() {
	$('.inner').jrumble({
		x : 4,
		y : 0,
		rotation : 0
	});
	$('.inner').trigger('startRumble');
	setTimeout('$(".inner").trigger("stopRumble")', 500);
}
function processDelay(){
	if(frameElement){
		var parentWin=frameElement.api.opener;
		while (true) {
			if(parentWin.frameElement!=null){
				parentWin=parentWin.frameElement.api.opener;
			}else{
				break;
			}
		}
		parentWin.location.href=window.location.href;
		return false;
	}else if($('#maintabs').length>0){
		var currentTab=$("#maintabs").tabs("getTab",1);
		window.location.href=currentTab.href;
		return false;
	}
	return true;
}

