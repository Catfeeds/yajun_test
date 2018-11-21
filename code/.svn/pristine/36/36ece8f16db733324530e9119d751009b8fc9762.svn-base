<%@page import="java.util.Arrays"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>  
<fmt:setLocale value="zh_CN" scope="session" />
<fmt:setBundle basename="languages/front/core/messages" var="messagesBundle"/>  

<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8" />
		<title><fmt:message key="INDEX_TITLE" bundle="${messagesBundle}"/>  </title>
		<!-- <link rel="shortcut icon" href="res/images/main/logo.ico"> -->
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />

		<!-- basic styles -->

		<link href="res/bootstrap/css/bootstrap.min.css" rel="stylesheet" />
		<link rel="stylesheet" href="res/bootstrap/css/font-awesome.min.css" />

		<!--[if IE 7]>
		  <link rel="stylesheet" href="res/bootstrap/css/font-awesome-ie7.min.css" />
		<![endif]-->

		<!-- page specific plugin styles -->


		<!-- ace styles -->

		<link rel="stylesheet" href="res/bootstrap/css/ace.min.css" />
		<link rel="stylesheet" href="res/bootstrap/css/ace-rtl.min.css" />

		<!--[if lte IE 8]>
		  <link rel="stylesheet" href="res/bootstrap/css/ace-ie.min.css" />
		<![endif]-->

		<!-- inline styles related to this page -->

		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->

		<!--[if lt IE 9]>
		<script src="res/bootstrap/js/html5shiv.js"></script>
		<script src="res/bootstrap/js/respond.min.js"></script>
		<![endif]-->
		
		<link rel="stylesheet" href="res/css/wis.css" />
		<script type="text/javascript" src="res/js/jquery-1.8.0.min.js"></script>
	  <script type="text/javascript" src="res/js/jquery.cookie.js"></script>
	
	  <script type="text/javascript">
	
	  <%
	  		String titleMsg = "LOGIN_ERROR_CODE_DEFAULT";
	  			if (request.getParameter("code") != null) {
	  				String code = request.getParameter("code").toString();
	  				if (Arrays.asList(new String[]{"1","2","3","4","5","6","10"}).contains(code)) {
	  					titleMsg = "LOGIN_ERROR_CODE_"+code;
	  				} else {
	  					titleMsg = "LOGIN_ERROR_CODE_ERROR";
	  				}
	 			}%>
	 	var msg="<fmt:message key='<%=titleMsg%>' bundle='${messagesBundle}'/>";
		function doChgLang() {
			var lang = $("#lang").val();
			$.ajax({
				type : 'POST',
				url : '${pageContext.request.contextPath}/locale/change.do',
				data : {
					lang : lang
				},
				success : function(data) {
					window.location.reload();
				}
			});
		}
	 </script>
	 <link rel="stylesheet" type="text/css" href="res/css/tipsy.css" media="all" />
	</head>

	<body class="login-layout">
		<div class="main-container">
			<div class="main-content">
				<div class="row">
					<div class="col-sm-10 col-sm-offset-1">
						<div class="login-container">
							<div class="center center_top">
							</div>

							<div class="space-6"></div>

							<div class="position-relative">
								<div id="alertMessage"></div>
								<div id="login" class="login-box visible widget-box no-border">
									<div class="widget-body inner">
										<div class="widget-main">
											<h5 class="">
												<img style="height: 24px;" alt="" src="res/images/main/logo-dainkin.png"> 
											</h5>
											<h4 class="header black bolder bigger">
												<fmt:message key='MAIN_TITLE' bundle='${messagesBundle}'/>
											</h4>

											<div class="space-6"></div>

											<form name="formLogin" id="formLogin" action="j_spring_security_check.action"  method="post">
												<fieldset>
													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input class="form-control" name="j_username" type="text" id="j_username" iscookie="true" nullmsg="<fmt:message key='LOGIN_USER_NAME_CHECK' bundle='${messagesBundle}'/>" />
															<i class="icon-user"></i>
														</span>
													</label>

													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input name="j_password" type="password" id="j_password"  nullmsg="<fmt:message key='LOGIN_USER_PASSWORD_CHECK' bundle='${messagesBundle}'/>" class="form-control" />
															<i class="icon-lock"></i>
														</span>
													</label>
													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
														<select onchange="doChgLang()"  class="form-control" name="lang" id="lang" iscookie="true" nullmsg="<fmt:message key='LOGIN_USER_LANG_CHECK' bundle='${messagesBundle}'/>">
												            <option ${'zh_CN'==lang?'selected':'' } value="zh_CN">中文(简体)</option>
												            <option ${'en_US'==lang?'selected':'' } value="en_US">English</option>
												       </select>
												       <i class="icon-globe"></i>
												       </span>
											       </label>

													<div class="space"></div>

													<div class="clearfix">
														<label class="inline">
															<input type="checkbox" class="ace" id="on_off" name="remember" checked="ture" class="on_off_checkbox" value="0" />
															<span class="lbl"> <fmt:message key='LOGIN_REMEMBER_USER' bundle='${messagesBundle}'/></span>
														</label>

														<button id="forgetpass" type="button" class="width-15 pull-right btn btn-sm btn-primary">
															<fmt:message key='LOGIN_RESET' bundle='${messagesBundle}'/>
														</button>
														<button id="but_login" type="button" class="width-15 pull-right btn btn-sm btn-primary">
															<fmt:message key='LOGIN_LOGIN' bundle='${messagesBundle}'/>
														</button>
													</div>

													<div class="space-4"></div>
												</fieldset>
											</form>

										</div><!-- /widget-main -->
									</div><!-- /widget-body -->
								</div>
								<!-- /login-box -->
							</div><!-- /position-relative -->
						</div>
					</div><!-- /.col -->
				</div><!-- /.row -->
			</div>
		</div><!-- /.main-container -->

<div id="versionBar">
   <div class="copyright">
    &copy;<fmt:message key='LOGIN_COPYRIGHT' bundle='${messagesBundle}'/> 
   <span class="tip">大金空调(推荐使用IE9+,谷歌浏览器可以获得更快,更安全的页面响应速度)技术支持</span>
   </div>
  </div>

		<!-- basic scripts -->


		<!--[if !IE]> -->

		<script type="text/javascript">
			window.jQuery || document.write("<script src='res/bootstrap/js/jquery-2.0.3.min.js'>"+"<"+"/script>");
		</script>

		<!-- <![endif]-->

		<!--[if IE]>
<script type="text/javascript">
 window.jQuery || document.write("<script src='res/bootstrap/js/jquery-1.10.2.min.js'>"+"<"+"/script>");
</script>
<![endif]-->

		<script type="text/javascript">
			if("ontouchend" in document) document.write("<script src='res/bootstrap/js/jquery.mobile.custom.min.js'>"+"<"+"/script>");
		</script>

		<!-- inline scripts related to this page -->

		<script type="text/javascript">
			function show_box(id) {
			 jQuery('.widget-box.visible').removeClass('visible');
			 jQuery('#'+id).addClass('visible');
			}
		</script>
<script type="text/javascript" src="res/js/login/jquery-jrumble.js"></script>
<script type="text/javascript" src="res/js/login/jquery.tipsy.js"></script>
<script type="text/javascript" src="res/js/login/iphone.check.js"></script>
<script type="text/javascript" src="res/js/login/login.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$(window).resize();
	$("#on_off").change(function() {
		if (this.checked) {
			$(this).val(1);
		} else {
			$(this).val(0);
		}
	});
});
$(window).resize(function(){
	var clientHeight = document.documentElement.clientHeight;
	var loginboxHeight = $("#login").height();
	$(".center_top").height((clientHeight - loginboxHeight) / 2 - clientHeight * 0.1);
});
</script>
</body>
</html>