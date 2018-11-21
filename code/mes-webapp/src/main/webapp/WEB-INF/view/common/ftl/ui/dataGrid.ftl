<#macro dataGrid  url="" paging="true" queryParams="btableQueryParams" singleSelect="false" rowNumbers="false" id="dg${currentPageId}" dataSearch="false" dataHeight="100%" showCheckAll="true" showRefresh="false" showColumns="false" sortable="true" detailView="false" detailFormatter="" dataClasses="bootstrap_table_datagrid">
<table 
		data-id="${id}"
		data-query-params="${queryParams}" 
		data-toggle="table" <#if id!=""> id=${id}<#else>id="dg${currentPageId}"</#if>
		data-side-pagination="server" data-classes="table table-hover table-condensed ${dataClasses}"
		data-url="${url}" data-pagination="${paging}" data-page-size="20" 
		data-single-select="${singleSelect}" data-checkbox-header="${showCheckAll}"
		data-page-list="[20, 50, 100, 200]" data-search="${dataSearch}"
		data-height="${dataHeight}" data-method="post"
		data-content-type="application/x-www-form-urlencoded"
		data-show-refresh="${showRefresh}" data-show-columns="${showColumns}"
		data-locale="${lang}" data-sortable="${sortable}"
		data-click-to-select="true"
		data-detail-view="${detailView}"
		data-striped="true"
		<#if detailView="true">data-detail-formatter="${detailFormatter}"</#if>
		>
	<thead>
		<tr>
			<#if rowNumbers="true"><th data-width="30" data-align="center" data-formatter="indexFormatter" data-switchable="false"></th></#if>
			<#nested/>
		</tr>
	</thead>
</table>
</#macro>