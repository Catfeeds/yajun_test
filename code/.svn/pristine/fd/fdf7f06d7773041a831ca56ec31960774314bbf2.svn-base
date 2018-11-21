<#macro formBlock id="" title="" remarks="" icon="" collapse="false" defaultCollapse="true" style="">
	<div <#if id!="">id="${id}"</#if> class="widget-box transparent<#if defaultCollapse=="false"> collapsed</#if>" <#if style!="">style="${style}"</#if>>
		<div class="widget-header widget-header-flat">
			<h4 class="lighter">
				<#if icon!=""><i class="icon-${icon}"></i></#if>
				<#if title!=""><@s.m code="${title}" /></#if><#if remarks!="">（<@s.m code="${remarks}" />）</#if>
			</h4>
			<#if collapse="true">
				<div class="widget-toolbar">
					<a href="#" data-action="collapse">
						<#if defaultCollapse=="false">
						<i class="icon-chevron-down"></i>
						<#else>
						<i class="icon-chevron-up"></i>
						</#if>
					</a>
				</div>
			</#if>
		</div>
		<div class="widget-body">
			<#nested/>
		</div>
	</div>
</#macro>