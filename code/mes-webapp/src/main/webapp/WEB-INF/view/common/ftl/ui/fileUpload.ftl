<#macro fileUpload id="multiple_file_upload">
		<div class="fileUploadSpan">	
			<input type="file" name="uploadify" id="${id}" />
				  <a href="javascript:$('#${id}').uploadify('upload','*')" class="linkbutton" plain="true" icon="arrow_up"  ><@s.m code="FRAME_UPLOAD_START" /></a>  
				        <a href="javascript:$('#${id}').uploadify('cancel','*')" class="linkbutton" plain="true" icon="cancel" ><@s.m code="FRAME_UPLOAD_END" /></a>  
				        <a href="javascript:$('#${id}').uploadify('stop','*')" class="linkbutton" plain="true" icon="control_stop"  hidden=true id="frameStopUpload"><@s.m code="FRAME_UPLOAD_STOP" /></a> 
			        
	        </div>   
</#macro>