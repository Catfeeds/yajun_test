<#macro editor title="" checktip="" permissionCode="">
	<#if noPermissionModule=="true" || formPermissionMap[permissionModule]?seq_contains(permissionCode)>
	 <tr>
     <td>    	 
     	 <div class="form-group">
			<label class="col-sm-3 control-label no-padding-right" for="form-input-readonly"><@s.m code="${title}" />:</label>
			<div class="col-sm-3">
				 <#assign editorCode=permissionCode/>
     			 <#nested/>
			</div>
		</div>
     </td>
    </tr>
	</#if>
</#macro>