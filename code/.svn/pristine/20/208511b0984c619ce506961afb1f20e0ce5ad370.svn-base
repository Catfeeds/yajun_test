<#macro input type="text" title="" id="" field="" options="" value="" values="" disabled="" style="" excludeSelectOption="" gridID="" permissionCode="" onchange="" valueAll="" datatype="" errormsg="" readonly="" placeHolder="" class="" ignore="" maxlength="128" multipleSize="3" code="" label="" file="" fileType="image" multiple="true" multipleSelect="true">
	<#escape x as x?html>
	<#if title!="">
	<span class="frameInputSpan">
		 <label  class="control-label" ><@s.m code="${title}" /> ：</label>
	</#if>
	<#if type=="text">
		<input maxlength=${maxlength} class="${class}" type="text" style="${style}" id="${id}" <#if readonly!="">readonly="readonly"</#if> <#if placeHolder!="">placeHolder="${placeHolder}"</#if> <#if field!=""> name="${field}"</#if> value="${value!}" <#if datatype!="">datatype="${datatype}" <#if ignore!="">ignore="ignore"</#if> <#if errormsg!="">errormsg="<@s.m code='${errormsg}'/>" nullmsg="<@s.m code='${errormsg}'/>"</#if></#if>/>
	<#elseif type=="checkbox">
		<#if options?is_sequence>
			<#assign i=1>
			<#assign valueArray=values?split(",")>
			<#list options as option>
				<input <#if disabled!="" > disabled="disabled"</#if> <#if i==1><#if datatype!="">datatype="${datatype}" <#if ignore!="">ignore="ignore"</#if> <#if errormsg!="">errormsg="<@s.m code='${errormsg}'/>"</#if></#if></#if> type="checkbox" name="${field}" value="${option.code}" id="${field}_${i}" <#if valueArray?is_sequence><#list valueArray as v> <#if v==option.code> checked<#break></#if></#list></#if> /><label for="${field}_${i}" style="margin-right: 10px;">${option.name}</label>
				<#assign i=i+1>
			</#list>
		</#if>
	<#elseif type=="checkboxView">
		<#if options?is_sequence>
			<#list options as option>
				<#if value?is_sequence><#list value as v><#if v==option.code><label for="${field}_${option.code}" style="margin-right: 10px;">${option.name}</label></#if></#list></#if>
			</#list>
		</#if>
	<#elseif type=="select">
		<#if disabled!="" >
			<select class="${class}" <#if field!=""> name="${field}"</#if> id="${id}" style="${style}" disabled="disabled" onchange="${onchange}" <#if datatype!="">datatype="${datatype}" <#if ignore!="">ignore="ignore"</#if> <#if errormsg!="">errormsg="<@s.m code='${errormsg}'/>"</#if></#if>> 
				<#if excludeSelectOption==""><option value =""><@s.m code="PLEASE_CHOOSE" /></option></#if>
				<#if options?is_sequence>
					<#list options as option>
						<#if value?? && value!="" && value==option.code>
							<#if code?? && label?? &&code!="" &&label!="" >
								<option value ="${option[code]}" selected="selected">${option[label]}</option>
							<#else>
								<option value ="${option.code}" selected="selected">${option.name}</option>
							</#if>
						<#else>
							<#if code?? && label?? &&code!="" &&label!="" >
								<option value ="${option[code]}" >${option[label]}</option>
							<#else>
								<option value ="${option.code}" >${option.name}</option>
							</#if>
						</#if>
					</#list>
				</#if>
			</select>
		<#else>
			<select class="${class}" <#if field!=""> name="${field}"</#if> id="${id}" style="${style}" <#if readonly!="">readonly="readonly"</#if> onchange="${onchange}" <#if datatype!="">datatype="${datatype}" <#if ignore!="">ignore="ignore"</#if> <#if errormsg!="">errormsg="<@s.m code='${errormsg}'/>"</#if></#if>> 
				<#if excludeSelectOption=="">
					<#if valueAll==""><option value ="" ><@s.m code="PLEASE_CHOOSE" /></option>
					<#else>	<option value ="${valueAll}" ><@s.m code="PLEASE_CHOOSE" /></option>						
					</#if>
				</#if>
				<#if options?is_sequence>
					<#list options as option>
						<#if value?? && value!="" && value==option.code>
							<#if code?? && label?? &&code!="" &&label!="" >
								<option value ="${option[code]}" selected="selected">${option[label]}</option>
							<#else>
								<option value ="${option.code}" selected="selected">${option.name}</option>
							</#if>
						<#else>
							<#if code?? && label?? &&code!="" &&label!="" >
								<option value ="${option[code]}" >${option[label]}</option>
							<#else>
								<option value ="${option.code}" >${option.name}</option>
							</#if>
						</#if>
					</#list>
				</#if>
			</select>
		</#if>
	<#elseif type=="multiple">
		<#assign valueArray=values?split(",")>
		<#if disabled!="" >
			<select multiple class="${class}" <#if field!=""> name="${field}"</#if> id="${id}" style="${style}" disabled="disabled" onchange="${onchange}" <#if datatype!="">datatype="${datatype}" <#if ignore!="">ignore="ignore"</#if> <#if errormsg!="">errormsg="<@s.m code='${errormsg}'/>"</#if></#if>> 
				<#if excludeSelectOption==""><option value =""><@s.m code="PLEASE_CHOOSE" /></option></#if>
				<#if options?is_sequence>
					<#list options as option>
						<#if values??>
							<option value ="${option.code}" <#if valueArray?is_sequence><#list valueArray as v> <#if v==option.code> selected="selected"<#break></#if></#list></#if>>${option.name}</option>
						<#else>
							<option value ="${option.code}" >${option.name}</option>
						</#if>
					</#list>
				</#if>
			</select>
		<#else>
			<select <#if multipleSelect=="true">multiple</#if>   class="${class}" <#if field!=""> name="${field}"</#if> id="${id}" style="${style}" onchange="${onchange}" <#if datatype!="">datatype="${datatype}" <#if ignore!="">ignore="ignore"</#if> <#if errormsg!="">errormsg="<@s.m code='${errormsg}'/>"</#if></#if>> 
				<#if excludeSelectOption=="">
					<#if valueAll==""><option value ="" ><@s.m code="PLEASE_CHOOSE" /></option>
					<#else>	<option value ="${valueAll}" ><@s.m code="PLEASE_CHOOSE" /></option>						
					</#if>
				</#if>
				<#if options?is_sequence>
					<#list options as option>
						<#if values??>
							<option value ="${option.code!}" <#if valueArray?is_sequence><#list valueArray as v> <#if v==option.code> selected="selected"<#break></#if></#list></#if>>${option.name!}</option>
						<#else>
							<option value ="${option.code!}" >${option.name!}</option>
						</#if>
					</#list>
				</#if>
			</select>
			<script type="text/javascript">
				$(function () {
				    $('#${id}').multiselect({
				    	enableFiltering: true,
				    	nonSelectedText: '<@s.m code="PLEASE_CHOOSE" />',
				    	maxHeight: 150
				    });
				});
			</script>
		</#if>	
	<#elseif type=="selectText" >
		<#if disabled!="" >
			<#if value?? && value!="">
				<#if options?is_sequence>
					<#assign notfound="true" />
					<#list options as option>
						<#if value==option.code>
							<#assign notfound="false" />
							<input class="${class}" type="text" style="${style}"  disabled="disabled" value="${option.name}"/>
						</#if>
					</#list>
					<#if notfound=="true">
						<input class="${class}" type="text" style="${style}" disabled="disabled" value=""/>
					</#if>
				</#if>
			<#else>
				<input class="${class}" type="text" style="${style}" disabled="disabled" value=""/>
			</#if>
		<#else>
			<#if value?? && value!="">
				<#if options?is_sequence>
					<#list options as option>
						<#if value==option.code>
							<input class="${class}" type="text" style="${style}"  value="${option.name}"/>
						</#if>
					</#list>
				</#if>
			<#else>
				<input class="${class}" type="text" style="${style}"  value=""/>
			</#if>
		</#if>
	<#elseif type=="selectView" >
		<#if value??&& value!="">
			<#if options?is_sequence>
				<#list options as option>
					<#if value==option.code>
						${option.name}
					</#if>
				</#list>
			</#if>
		</#if>
	<#elseif type=="radio" >
		<#if options?is_sequence>
			<#assign i=1>
			<#list options as option>
				<#if value!="" && value==option.code>
				 	<input class="${class}" type="radio" style="width:auto;" <#if i==1><#if datatype!="">datatype="${datatype}" <#if ignore!="">ignore="ignore"</#if> <#if errormsg!="">errormsg="<@s.m code='${errormsg}'/>"</#if></#if></#if> <#if field!=""> name="${field}"</#if> value="${option.code}" checked="checked"/> ${option.name}
				<#else>
					<input class="${class}" type="radio" style="width:auto;" <#if i==1><#if datatype!="">datatype="${datatype}" <#if ignore!="">ignore="ignore"</#if> <#if errormsg!="">errormsg="<@s.m code='${errormsg}'/>"</#if></#if></#if> <#if field!=""> name="${field}"</#if> value="${option.code}"/> ${option.name}
				</#if>
				<#assign i=i+1>
			</#list>
		</#if>
	<#elseif type=="radioText" >
		<#if value!="">
			<#if options?is_sequence>
				<#list options as option>
					<#if value==option.code>
					<input class="${class}" type="text" style="${style}"  value="${option.name}"/>
					</#if>
				</#list>
			</#if>
		<#else>
			<input class="${class}" type="text" style="${style}"  value=""/>
		</#if>
	<#elseif type=="file" >
		<div class="<#if fileType=="image">img_input_box<#else>file_input_box</#if>">
			<input id="${id}" name="${field}" type="file" class="file" <#if multiple=="true">multiple</#if> <#if datatype!="">datatype="${datatype}"</#if>/>
			<#if multiple=="true">
			<div class="action-buttons inline"><a href="javascript:void(0);"><i class="icon-plus bigger-125 red"></i></a></div>
			</#if>
		</div>
		<script type="text/javascript">
			initFileInput("${id}",{
				showUpload : false,
				<#if fileType=="image">
				allowedFileTypes : ['image'],
				showPreview : true
				<#else>
				showPreview : false
				</#if>
			});
		</script>
	<#elseif type=="fileView">
		<#if fileType=="image">
			<#assign pictures=[] />
			<#if file?is_sequence>
				<#assign pictures=file/>
			<#else>
				<#assign pictures=[file]/>
			</#if>
			<#list pictures as picture>
				<div class="form_img_box" style="">
		            <input type="hidden" name="${field}" value="${picture.id}" />
		            <img class="form_img" style="" alt="${picture.name}" title="${picture.name}" src="${imagesBaseUrl}${picture.thumbRelURL}" orgSrc="${imagesBaseUrl}${picture.originRelURL}">
		            <#if readonly=="" || readonly=="false">
		            <a href="javascript:void(0);" class="del_img_btn"></a>
		            </#if>
		        </div>
	        </#list>
        <#else>
	        <ul class="attachment-list pull-left list-unstyled">
	        	<#assign attachments=[] />
				<#if file?is_sequence>
					<#assign attachments=file/>
				<#else>
					<#assign attachments=[file]/>
				</#if>
				<#list attachments as attachment>
	        	<li>
		        	<input type="hidden" name="${field}" value="${attachment.id}" />
					<div class="attached-file inline">
						<i class="icon-file-alt bigger-110 middle"></i>
						<span class="attached-name middle">${(attachment.name)!}</span>
					</div>
					<div class="action-buttons inline">
						<a href="javascript:void(0);">
							<#if readonly=="" || readonly=="false">
								<i class="icon-trash bigger-125 red"></i>
							<#else>
								<i class="icon-download-alt bigger-125 blue"></i>
							</#if>
						</a>
					</div>
				</li>
				</#list>
			</ul>
        </#if>
	<#elseif type=="" >
		<#if value!="">
			<#if options?is_sequence>
				<#list options as option>
					<#if value==option.code>
					<input class="${class}" type="text" style="${style}"  value="${option.name}"/>
					</#if>
				</#list>
			</#if>
		<#else>
			<input maxlength=${maxlength} class="${class}" type="text" style="${style}"  id="${id}" value=""/>
		</#if>
	</#if>
	<#if title!="">
	</span>
	</#if>
	</#escape>
</#macro>