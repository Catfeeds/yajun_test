<#macro select title="" url="" callback="" params="" dynamicUrl="false" zIndex="" beforeSelect="0">
	 <a href="#" class="linkbutton" plain="true" icon="magnifier" 
		onClick="frame_Select({title:'<@s.m code="${title}" />',<#if dynamicUrl="true">url:${url}+'${params}'<#else>url:'${url}'+'${params}' </#if>,callback:${callback} <#if zIndex!="">,zIndex:${zIndex!}</#if>,beforeSelect:${beforeSelect}})" ><i class="icon-search"><@s.m code="FRAME_CHOOSE" /></i></a>
	 <a href="#" class="linkbutton" plain="true" icon="icon-redo" onClick="frame_cleanSelect(this);"><i class="icon-share-alt"><@s.m code="FRAME_CLEAN" /></i></a>
</#macro>