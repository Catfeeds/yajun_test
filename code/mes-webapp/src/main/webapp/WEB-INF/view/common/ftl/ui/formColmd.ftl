<#macro formColmd title="" checktip="" permissionCode="" class="">
	<#if permissionCode != "">
		<#if noPermissionModule=="true" || formPermissionMap[permissionModule]?seq_contains(permissionCode)>
			<div class="col-md-2"><label class="control-label"><@s.m code="${title}" />: </label></div>
			<#if class!=""><div class="${class}"> <#assign editorCode=permissionCode/> <#nested/></div>
			<#else><div class="col-md-3"> <#assign editorCode=permissionCode/> <#nested/></div>
			</#if>
		</#if>
		<#else>
		<div class="col-md-2"><label class="control-label"><@s.m code="${title}" />: </label></div>
		<#if class!=""><div class="${class}"><#assign editorCode=''/> <#nested/></div>
		<#else><div class="col-md-3"><#assign editorCode=''/> <#nested/></div>
		</#if>
	</#if>
</#macro>