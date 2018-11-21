<#macro button class="btn btn-white" title="button" iconCls="circle-o" type="button" url="" permissionCode="" width="700" height="400" onclick=""  downName="">
    <#if noPermissionModule=="false" && permissionCode!="">
    	 <@sec.authorize ifAnyGranted=permissionCode> 
    	 		<#if type="button">
				<span title="<@s.m code="${title}" />" class="${class}"  onclick="${onclick}">
						<i class="icon-${iconCls}"></i><@s.m code="${title}" />
					</span>
				<#elseif type="add">
					<span class="${class}"  onclick="add('<@s.m code='${title}' />','${url}','<#if dgId!="">${dgId}<#else>dg${currentPageId}</#if>')" ><i class="icon-${iconCls}"></i><@s.m code="${title}" /></span>
				<#elseif type="update">
					<span class="${class}"  
					 onclick="update('<@s.m code='${title}' />','${url}','<#if dgId!="">${dgId}<#else>dg${currentPageId}</#if>')" ><i class="icon-${iconCls}"></i><@s.m code="${title}" /></span>
				<#elseif type="view">
					<span class="${class}"  
					onclick="detail('<@s.m code='${title}' />','${url}','<#if dgId!="">${dgId}<#else>dg${currentPageId}</#if>')" ><i class="icon-${iconCls}"></i><@s.m code="${title}" /></span>
				<#elseif type="delete">
					<span class="${class}"  
					onclick="frame_grid_remove('${url}','<#if dgId!="">${dgId}<#else>dg${currentPageId}</#if>')" ><i class="icon-${iconCls}"></i><@s.m code="${title}" /></span>
				<#elseif type="rowAdd">
					<span class="${class}"  
					onclick="append('<#if dgId!="">${dgId}<#else>dg${currentPageId}</#if>')" ><i class="icon-${iconCls}"></i><@s.m code="${title}" /></span>
				<#elseif type="save">
					<span class="${class}"  
					onclick="save('${url}','<#if dgId!="">${dgId}<#else>dg${currentPageId}</#if>')" ><i class="icon-${iconCls}"></i><@s.m code="${title}" /></span>
				<#elseif type="reject">
					<span class="${class}"  
					onclick="reject('<#if dgId!="">${dgId}<#else>dg${currentPageId}</#if>')" ><i class="icon-${iconCls}"></i><@s.m code="${title}" /></span>
				<#elseif type="down">
					<span class="${class}"  
					iconCls="<#if iconCls!='' && iconCls!='default'>${iconCls}<#else>page_excel</#if>"  onclick="down('<@s.m code="${downName}" />','${url}','<#if dgId!="">${dgId}<#else>dg${currentPageId}</#if>')" ><i class="icon-${iconCls}"></i><@s.m code="${title}" /></span>	
				</#if>	
    	  </@sec.authorize>
    <#else>
	    	<#if type="button">
					<span title="<@s.m code="${title}" />" class="${class}"  onclick="${onclick}">
						<i class="icon-${iconCls}"></i><@s.m code="${title}" />
					</span>
				<#elseif type="add">
					<span class="${class}" onclick=add('<@s.m code="${title}" />','${url}','<#if dgId!="">${dgId}<#else>dg${currentPageId}</#if>') >
					<i class="icon-${iconCls}"></i><@s.m code="${title}" />
					</span>
				<#elseif type="update">
					<span class="${class}" onclick=update('<@s.m code="${title}" />','${url}','<#if dgId!="">${dgId}<#else>dg${currentPageId}</#if>') >
					<i class="icon-${iconCls}"></i><@s.m code="${title}" />
					</span>
				<#elseif type="view">
					<span class="${class}"  onclick=detail('<@s.m code="${title}" />','${url}','<#if dgId!="">${dgId}<#else>dg${currentPageId}</#if>') >
					<i class="icon-${iconCls}"></i><@s.m code="${title}" />
					</span>
				<#elseif type="delete">
					<span class="${class}"  
					 onclick=frame_grid_remove('${url}','<#if dgId!="">${dgId}<#else>dg${currentPageId}</#if>') ><i class="icon-${iconCls}"></i><@s.m code="${title}" /></span>
				<#elseif type="rowAdd">
					<span class="${class}"  
					 onclick=append('<#if dgId!="">${dgId}<#else>dg${currentPageId}</#if>') ><i class="icon-${iconCls}"></i><@s.m code="${title}" /></span>
				<#elseif type="save">
					<span class="${class}"  
					 onclick=save('${url}','<#if dgId!="">${dgId}<#else>dg${currentPageId}</#if>') ><i class="icon-${iconCls}"></i><@s.m code="${title}" /></span>
				<#elseif type="reject">
					<span class="${class}"  
					 onclick=reject('<#if dgId!="">${dgId}<#else>dg${currentPageId}</#if>') ><i class="icon-${iconCls}"></i><@s.m code="${title}" /></span>
				<#elseif type="down">
					<span class="${class}"  
					iconCls="<#if iconCls!='' && iconCls!='default'>${iconCls}<#else>page_excel</#if>"  onclick="down('<@s.m code="${downName}" />','${url}',this)" ><i class="icon-${iconCls}"></i><@s.m code="${title}" /></span>	
			</#if>	
    </#if>
	
</#macro>