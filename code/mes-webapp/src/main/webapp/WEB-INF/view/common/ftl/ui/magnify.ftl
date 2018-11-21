<#macro magnify label="" title="" url="" callback="" params="" dynamicUrl="false" zIndex="" beforeSelect="0" title="" id="" field="" value="" displayValue="" datatype="" errormsg="">
	<#assign tempId="${id}" />
	<#if tempId==""><#assign tempId="${field}" /></#if>
	<#if label!="">
		<span class="frameInputSpan">
			 <label  class="control-label" ><@s.m code="${label}" />  ：</label>
	</#if>
	<span class="value">
	<input type="hidden"  id="hidden_${tempId}" <#if field!=""> name="${field}"</#if> <#if value!="">value="${value}"</#if>/>
	<input type="text" <#if isForm=="true">style="width:50%" </#if>id="display_${tempId}" readonly="readonly" <#if displayValue!="">value="${displayValue}"</#if> <#if datatype!="">datatype="*" <#if errormsg!="">errormsg="<@s.m code='${errormsg}'/>" nullmsg="<@s.m code='${errormsg}'/>"</#if></#if>/>
	<span class="linkbutton_box">
	<a href="javascript:void(0);" title="<@s.m code="FRAME_CHOOSE" />" class="linkbutton" plain="true" icon="magnifier" 
		onClick="frame_Select({<#if title!="">title:'<@s.m code="${title}" />'<#elseif clmTitle??>title:'<@s.m code="${clmTitle}" />'<#else>title:'<@s.m code="${label}" />'</#if>,<#if dynamicUrl="true">url:${url}<#else>url:'${url}'+'${params}' </#if>,callback:${callback} <#if zIndex!="">,zIndex:${zIndex!}</#if>,beforeSelect:${beforeSelect}})" ><i class="icon-search"><@s.m code="FRAME_CHOOSE" /></i></a>
	<a href="javascript:void(0);" title="<@s.m code="FRAME_CLEAN" />" class="linkbutton" plain="true" icon="icon-redo" onClick="frame_cleanSelect(this);"><i class="icon-share-alt"><@s.m code="FRAME_CLEAN" /></i></a>
	</span>
	</span>
	<#if label!="">
		</span>
	</#if>
</#macro>