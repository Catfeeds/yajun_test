jQuery.fn.extend({
    autoHeight: function(){
        return this.each(function(){
            var $this = jQuery(this);
            if( !$this.attr('_initAdjustHeight') ){
                $this.attr('_initAdjustHeight', $this.outerHeight());
            }
            _adjustH(this).on('input', function(){
                _adjustH(this);
            });
        });
        /**
         * 重置高度 
         * @param {Object} elem
         */
        function _adjustH(elem){
            var $obj = jQuery(elem);
            return $obj.css({height: $obj.attr('_initAdjustHeight'), 'overflow-y': 'hidden'})
                    .height( elem.scrollHeight );
        }
    }
});
// 使用
$(function(){
	textAreaAutoHeight();
});
function textAreaAutoHeight() {
	$('textarea').autoHeight();
    if ($('textarea').height() < 30) {
    	$('textarea').height(30);
    }
	$('textarea').each(function(index, element) {
		if ('' != $.trim($(element).val())) {
			var rows = $(element).val().split('\n').length;
			$(element).height((rows + ($(element).val().length / 2 / element.cols)) * 21);
		} else {
			$(element).height(30);
		}
	});
}
var g_fileInput_id_index = 0;
$(function() {
	$(".form-horizontal input").addClass("col-xs-10 col-sm-5 form-control");
	$(".form-horizontal textarea").addClass("col-xs-10 col-sm-5 form-control");
	$(".form-horizontal input[type='radio']").attr("class","");
	$(".form-horizontal input[type='checkbox']").attr("class","");
	$(".form-horizontal input[type='file']").addClass("file");
	$(".form-horizontal select").addClass("col-xs-10 col-sm-5 form-control");
	$(".editable_table_td input").addClass( "");
	$(".editable_table_td select").addClass( "");
//	$(".Validform_checktip").each(function(index, element) {
//		if ($(element).parent().hasClass("value")) {} else {
//			//$(element).after("<span class='Validform_checktip_tmp'></span>");
//		}
//	});
//	$("form div.row").each(function(index, element) {
//		if ($(element).find("span.Validform_checktip,span.linkbutton_box").length == 0) {
//			$(element).addClass("form_row_margin_bottom");
//		}
//	});
	$('.del_img_btn,.del_attachment_btn').click(function() {
		$(this).parent().detach();
	});
	$('.icon-trash').click(function() {
		$(this).parents("li").detach();
		$(this).parents(".file_input_box").detach();
	});
	$('.icon-download-alt').click(function(){
		var attachmentId = $(this).parents("li").find("input[type=hidden]")[0].value;
		downAttachment($("#baseUrl").val() + "/attachment/down.do", {
			id : attachmentId
		})
	});
	$('.file_input_box .icon-plus').click(function() {
		g_fileInput_id_index++;
		var file_input_name = $(this).parents(".file_input_box").find('input[type="file"]')[0].name;
		var file_input_box = "<div class='file_input_box'>";
		file_input_box += '<input id="attachementList_' + g_fileInput_id_index + '" name="' + file_input_name + '" type="file" class="file" multiple/>';
		file_input_box += '<div class="action-buttons inline"><a href="javascript:void(0);"><i class="icon-trash bigger-125 red"></i></a></div>';
		file_input_box += "</div>";
		$(this).parents(".file_input_box").after(file_input_box);
		initFileInput("attachementList_" + g_fileInput_id_index,{
			showUpload : false,
			showPreview : false
		});
		$("#attachementList_" + g_fileInput_id_index).parents(".file_input_box").find(".icon-trash").click(function() {
			$(this).parents(".file_input_box").detach();
		});
	});
	$('.img_input_box .icon-plus').click(function() {
		g_fileInput_id_index++;
		var file_input_name = $(this).parents(".img_input_box").find('input[type="file"]')[0].name;
		var file_input_box = "<div class='img_input_box'>";
		file_input_box += '<input id="pictureList_' + g_fileInput_id_index + '" name="' + file_input_name + '" type="file" class="file" multiple/>';
		file_input_box += '<div class="action-buttons inline"><a href="javascript:void(0);"><i class="icon-trash bigger-125 red"></i></a></div>';
		file_input_box += "</div>";
		$(this).parents(".img_input_box").after(file_input_box);
		initFileInput("pictureList_" + g_fileInput_id_index,{
			showUpload : false,
			allowedFileTypes : ['image'],
			showPreview : true,
		});
		$("#pictureList_" + g_fileInput_id_index).parents(".img_input_box").find(".icon-trash").click(function() {
			$(this).parents(".img_input_box").detach();
		});
	});
	$(".form_img").click(function() {
		window.open($(this).attr('orgSrc'));
	});
	initFormRequiredFlag();
	$(".view_form").find("input,select,textarea").attr("disabled", true);
	$(".form_block_folding").click(function() {
		foldingFormBlock(this);
	});
});
