<#macro editTableMulti width="100%" style="" colCount=2>
	<table  border="0" cellpadding="0" cellspacing="0" width="100%" style="${style}" class="formtable">
	<#assign currentIndex=0/>
	<#assign colCount=colCount/>
		<tr><#nested/>
		
		<#if currentIndex % colCount !=0>
			<#list 1 .. (colCount-(currentIndex % colCount)) as t>
			<td valign="top">
			      <table cellpadding="0" cellspacing="1" height="100%"  class="formtable">
			  		<tr>
			  		<td align="right" width="150" nowrap><label class="Validform_label"></label></td> 
			  		<td class="blankValue value" ><span class="Validform_checktip"></span></td>
			  		</tr>
				  </table>
			  </td>
			</#list>
		</#if>
	</table>
</#macro>

