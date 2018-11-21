<#macro column title="" stitle="" field="" width="" align="left" hidden="" checkbox="" sortable="true" style="" formatter="" dataClass="text-nowrap" permissionCode="" valign="" colspan="1" rowspan="1">
<#if checkbox!="">
		<th data-checkbox="${checkbox}" 
		<#if field!="">
			data-field="${field}"
		</#if>
		<#if formatter!="">
			data-formatter="${formatter}" 
		</#if>
		<#if valign!="">
			data-valign="${valign}"
		</#if>
		<#if colspan!="1">
			data-colspan="${colspan}" 
		</#if>
		<#if rowspan!="1">
			data-rowspan="${rowspan}" 
		</#if>></th>
<#else>
<#if noPermissionModule=="true" || formPermissionMap[permissionModule]?seq_contains(permissionCode)>
		<th data-searchable="true" data-align="${align}" data-sortable="${sortable}"
		data-checkbox="${checkbox}"
		<#if field!="">
			data-field="${field}"
		</#if>
		<#if valign!="">
			data-valign="${valign}"
		</#if>
		<#if dataClass!="">	
			data-class="${dataClass}"
		</#if>
		<#if hidden!="">
			data-visible="${hidden}" 
		</#if>
		<#if formatter!="">
			data-formatter="${formatter}" 
		</#if>
		<#if style!="">
			data-cell-style="${style}"
		<#else>
			data-cell-style="fixWidth"
		</#if>
		<#if width=="false">
		<#elseif width!="">	
			data-width="${width}"
		<#else>	
			data-width="20"
		</#if>
		<#if colspan!="1">
			data-colspan="${colspan}"
		</#if>
		<#if rowspan!="1">
			data-rowspan="${rowspan}" 
		</#if>
		>
            <#if stitle!="">
                ${stitle}
            <#else>
                <@s.m code="${title}" />
            </#if>
        </th>
	</#if>
</#if>

</#macro>