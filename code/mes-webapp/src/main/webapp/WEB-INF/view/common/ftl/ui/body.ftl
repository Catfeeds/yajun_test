<#macro body title="" permissionModule="" noPermissionModule="false" noBreadcrumbs="false">
	<#if noBreadcrumbs=="false">
		<div class="breadcrumbs">
			<ul class="breadcrumb">
				<li><i class="icon-home home-icon"></i> <a href="main.do"><@s.m code="INDEX_PAGE" /></a></li>
				<li class="active"><@s.m code="${title}" /></li>
			</ul>
		</div>
	</#if>
	<div class="page-content" fit="true">
	 	<#assign permissionModule=permissionModule/>
	 	<#assign noPermissionModule=noPermissionModule/>
		<#if noPermissionModule=="true" || formPermissionMap[permissionModule]??>
		  <#nested/>
		 <#else>
		 	<div class="noModulePermission">
		 		<@s.m code='ERROR_NO_FORM_PERMISSION' />
		 	</div>
		</#if>
	</div>
</#macro>

