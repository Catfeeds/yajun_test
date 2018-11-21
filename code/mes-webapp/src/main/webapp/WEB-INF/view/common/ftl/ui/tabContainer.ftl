<#macro tabContainer id="" idsAndTitles="" activeId="" tabsDirection="" class="" contentBorder="true" asyncLoadView="true">
	<#assign thisActiveId=activeId />
	<#assign contentUrls=[] />
	<#list idsAndTitles?split(",") as idAndTitle>
		<#assign index=idAndTitle?index_of(":")/>
		<#assign tabId=idAndTitle?substring(0,index)/>
		<#if thisActiveId=="">
			<#assign thisActiveId=tabId/>
		</#if>
	</#list>
	<div <#if id!="">id="${id}"</#if> class="tabbable<#if tabsDirection!=""> ${tabsDirection}</#if><#if class!=""> ${class}</#if>">
		<#if "tabs-below"==tabsDirection>
			<div class="tab-content no-border padding-24">
				<#assign activeId=thisActiveId/>
				<#nested/>
			</div>
		</#if>
		<ul class="nav nav-tabs padding-12 tab-color-blue background-blue">
			<#list idsAndTitles?split(",") as idAndTitle>
				<#assign index=idAndTitle?index_of(":")/>
				<#assign tabId=idAndTitle?substring(0,index)/>
				<#assign title=idAndTitle?substring(index+1)/>
				<li <#if tabId==thisActiveId>class="active"</#if>>
					<a data-toggle="tab" href="#${tabId}">
						<@s.m code="${title}" />
					</a>
				</li>
			</#list>
		</ul>
		<#if "tabs-below"!=tabsDirection>
			<div class="tab-content<#if contentBorder=="false"> no-border</#if> padding-24">
				<#assign activeId=thisActiveId/>
				<#nested/>
			</div>
		</#if>
	</div>
	<script type="text/javascript">
	var contentUrls_${id} = new Array();
	var tabLoaded_${id} = new Array();
	var tabIds_${id} = new Array();
	<#list contentUrls as contentUrl>
		tabLoaded_${id}[${contentUrl_index}] = false;
		contentUrls_${id}[${contentUrl_index}]="${contentUrl}";
	</#list>
	<#list idsAndTitles?split(",") as idAndTitle>
		<#assign index=idAndTitle?index_of(":")/>
		<#assign tabId=idAndTitle?substring(0,index)/>
		tabIds_${id}[${idAndTitle_index}]="${tabId}";
	</#list>
	jQuery(function($) {
		$("#${id} ul li").click(function() {
 			${id}TabClick($(this).index());
 		});
	});
	function ${id}TabClick(tabIndex) {
		if(contentUrls_${id}[tabIndex] == ''){
			return;
		}
		if(tabLoaded_${id}[tabIndex]) {
			return;
		}
		loadTabView(contentUrls_${id}[tabIndex], ${asyncLoadView}, function(html){
			$('#'+tabIds_${id}[tabIndex]).html(html);
			tabLoaded_${id}[tabIndex] = true;
		});
	}
	</script>
</#macro>