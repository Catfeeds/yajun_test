<#macro editorMulti title="" checktip="" permissionCode="" oneRow="" tdwidth="">
	<#if noPermissionModule=="true" || formPermissionMap[permissionModule]?seq_contains(permissionCode)>
		<#if oneRow!="">
		
		<#if currentIndex % colCount !=0>
			<#list 1 .. (colCount-(currentIndex % colCount)) as t>
			<td valign="top" width="${100/colCount}%" class="tabRow"><table cellpadding="0" cellspacing="1" height="100%"  class="formtable">
			  		<tr><td align="right" width="150" nowrap><label class="Validform_label"></label></td> 
			  		<td class="blankValue value"><span class="Validform_checktip"></span></td></tr>
				  </table>
			  </td>
			   <#assign currentIndex=currentIndex+1/>
			</#list>
		</#if>
		
			</tr>
			
			<tr><td valign="top" colspan="${colCount}" class="tabRow">
				<table cellpadding="0" cellspacing="0" height="100%"  class="formtable">
			  		<tr>
			  			  <td align="right" width="150" nowrap>
						     <label class="Validform_label">
						      	<@s.m code="${title}" />:
						      </label>
						     </td>
						     <td class="value" <#if tdwidth!="">width="${tdwidth}"</#if>>
						     	<#nested/>
						     	 <span class="Validform_checktip"><@s.m code="${checktip}" /></span>
						     </td>
			  		</tr>
				  </table>
			  </td>
		   </tr><tr>
		<#else>
				<td valign="top" width="${100/colCount}%" class="tabRow">
				  <table cellpadding="0" cellspacing="0" height="100%"  class="formtable">
				  		<tr>
				  			  <td align="right" width="150" nowrap>
							     <label class="Validform_label">
							      	<@s.m code="${title}" />:
							      </label>
							     </td>
							     <td class="value" width="200">
							     <#assign editorCode=permissionCode/>
							     	<#nested/>
							     	 <span class="Validform_checktip"><@s.m code="${checktip}" /></span>
							     </td>
				  		</tr>
					  </table>
				  </td>
				  <#assign currentIndex=currentIndex+1/>
			<#if currentIndex % colCount == 0 >
				 </tr><tr>
			</#if>
		</#if>
	</#if> 
	
</#macro>