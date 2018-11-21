<#macro inputRange type="text" title=""  field="" placeHolder="" options=""  value="" maxlength="128" onchange="" class="" id="" style="">
	<#if title!="">
	<span class="wizardInput">
		<label class="control-label" ><@s.m code="${title}" /> ：</label>
	</#if>
	<#if type=="text">
		<input id="${id}Start" maxlength=${maxlength} type="text" <#if placeHolder!="">placeHolder="${placeHolder}"</#if> <#if field!=""> name="${field}Start"</#if> value="${value!}"/>
				<span style="display:inline-block; width:10px; text-align:center;">-</span>
		<input id="${id}End" style="display:inline-block;" maxlength=${maxlength} type="text" <#if placeHolder!="">placeHolder="${placeHolder}"</#if> <#if field!=""> name="${field}End"</#if> value="${value!}"/>
	<#elseif type=="select">
		<select class="${class}" <#if field!=""> name="${field}"</#if> id="${id}" style="${style}" onchange="${onchange}" > 
			<#if options?is_sequence>
				<#list options as option>
					<#if value?? && value!="" && value==option.code>
						<option value ="${option.code}" selected="selected">${option.name}</option>
					<#else>
						<option value ="${option.code}" >${option.name}</option>
					</#if>
				</#list>
			</#if>
		</select>
		<span style="display:inline-block; width:10px;text-align:center;">±</span>
		<input type="text" style="display:inline-block;" class="input-mini" id="${id}Range" <#if field!=""> name="${field}Range"</#if> value="${value!}"/>
	</#if>
	<#if title!="">
	</span>
	</#if>
</#macro>