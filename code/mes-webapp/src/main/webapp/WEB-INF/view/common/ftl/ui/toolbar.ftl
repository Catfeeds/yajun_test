<#macro toolbar id="" showCollapsed="false">
<#if showCollapsed=="true">
<div class="widget-box transparent collapsed-block">
		<div class="widget-header widget-header-flat collapsed-block-head">
			<h4 class="lighter">
				<i class="icon-gears"></i>
			</h4>
			<div class="widget-toolbar collapsed-block-toolbar">
				<a href="#" data-action="collapse">
					<i class="icon-chevron-up"></i>
				</a>
			</div>	
		</div>
		<div class="widget-body">
		</#if>
			<div class="toolbar" <#if id!=""> id=${id}<#else>id="tb${currentPageId}"</#if>>
				<#nested/>
			</div>
			<div class="hr dotted"></div>
	<#if showCollapsed=="true">
		</div>
	</div>
	</#if>
</#macro>

