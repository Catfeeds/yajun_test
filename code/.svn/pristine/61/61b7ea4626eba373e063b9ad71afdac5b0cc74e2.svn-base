<#macro searchColumns showSeachButton="true" searchCallback="" url="" showCollapsed="false" defaultCollapse="true" exportUrl="" downName="" permissionCode="">
	<#if showCollapsed=="true">
	<div class="widget-box transparent <#if defaultCollapse=="false"> collapsed</#if> collapsed-block">
		<div class="widget-header widget-header-flat collapsed-block-head">
			<h4 class="lighter">
				<i class="icon-search"></i>
			</h4>
			<div class="widget-toolbar collapsed-block-toolbar">
				<a href="#" data-action="collapse">
					<i class="icon-chevron-down"></i>
				</a>
			</div>	
		</div>
		<div class="widget-body">
		</#if>
			<div id="searchColumns_${dgId}" class="searchColumns" style="margin-top:5px;">
				<div>
					<#assign isForm="false"/>
					<#nested/>
				</div>
				<#if showSeachButton=="true">
					<div class="search_btn_box">
						<a href="javascript:void(0)" class="btn btn-sm btn-info btnSearchSubmit" onclick="refreshTable('${dgId}',this,'${url}','<#if searchCallback!="">${searchCallback}</#if>');"><span class="icon-search"></span><@s.m code="FRAME_SEARCH" /></a>
						<a href="javascript:void(0)" class="btn btn-sm btn-info" onclick="resetSearch()"><span class="icon-retweet"></span><@s.m code="FRAME_RESET"/></a>
						<#if exportUrl!="">
							<#if permissionCode!="">
	    	 					<@sec.authorize ifAnyGranted=permissionCode> 
									<a href="javascript:void(0)" class="btn btn-sm btn-info" onclick="down('<@s.m code="${downName}" />','${exportUrl}','<#if dgId!="">${dgId}<#else>dg${currentPageId}</#if>')"><span class="icon-signout"></span><@s.m code="BTN_EXPORT"/></a>
								</@sec.authorize>
	    					<#else>
								<a href="javascript:void(0)" class="btn btn-sm btn-info" onclick="down('<@s.m code="${downName}" />','${exportUrl}','<#if dgId!="">${dgId}<#else>dg${currentPageId}</#if>')"><span class="icon-signout"></span><@s.m code="BTN_EXPORT"/></a>
							</#if>
						</#if>
					</div>
				</#if>
			</div>
			<div style="display:none;" id="queryParams_${dgId}">
			</div>
			<div class="hr dotted"></div>
			<#if showCollapsed=="true">
		</div>
	</div>
	</#if>
</#macro>

