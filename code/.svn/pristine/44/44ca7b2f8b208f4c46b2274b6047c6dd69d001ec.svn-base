<#macro editForm formId="" idValue="" url="default" class="" permissionModule="" isCreateBtn="" noPermissionModule="false" enctype="">
	<#escape x as x?html>
	<#assign permissionModule=permissionModule/>
	<#assign noPermissionModule=noPermissionModule/>
	<#assign isForm="true"/>
	<#if isCreateBtn!="">
		<form action="${url}" method="post" <#if class!="">class="${class}"</#if> <#if enctype!="">enctype="${enctype}"</#if>>
		
			<input name="id" value="${idValue}" type="hidden"/>
		<table  border="0" cellpadding="0" cellspacing="0" width="100%" >
				 <tr><#nested/></tr>
		</table>
	</form>
	<#else>
		<form <#if formId !=""> id="${formId}" </#if>action="${url}" method="post" <#if class!="">class="${class}"</#if> <#if enctype!="">enctype="${enctype}"</#if>>
			<input class="btn_sub" id="btn_sub" type="hidden" />
			<input name="id" value="${idValue}" type="hidden"/>
		<#nested/>
	</form>
	</#if>
	</#escape>
</#macro>

