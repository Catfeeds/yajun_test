<#macro editFormMulti  idValue="" url="default" class="" permissionModule="" noPermissionModule="false">
	<#assign permissionModule=permissionModule/>
	<#assign noPermissionModule=noPermissionModule/>
	<form action="${url}" method="post" <#if class!="">class="${class}"</#if>>
		<input class="btn_sub" id="btn_sub" type="hidden"/>
			<input name="id" value="${idValue}" type="hidden"/>
		<#nested/>
	</form>
</#macro>

