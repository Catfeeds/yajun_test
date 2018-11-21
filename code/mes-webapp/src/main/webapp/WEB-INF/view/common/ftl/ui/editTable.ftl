<#macro editTable width="100%" colspan="1" rowspan="1" style="" colCount=1>
	<table  border="0" cellpadding="0" cellspacing="0" width="${width}" style="${style}" class="formtable">
	<#assign currentIndex=0/>
	<#assign colCount=colCount/>
		<tr><#nested/>
		
		<#if currentIndex % colCount !=0>
			<#list 1 .. (colCount-(currentIndex % colCount)) as t>
			<td valign="top" class="tabRow"><table cellpadding="0" cellspacing="1" height="100%"  >
			  		<tr><td align="right" width="148" nowrap><label class="col-sm-3 control-label no-padding-right" for="form-input-readonly"></label></td> 
			  		<td class="blankValue value" ><span class="Validform_checktip"></span></td></tr>
				  </table>
			  </td>
			</#list>
		</#if>
	</table>
</#macro>

