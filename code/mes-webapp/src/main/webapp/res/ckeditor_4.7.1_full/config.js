/**
 * @license Copyright (c) 2003-2017, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
	config.toolbar = 'Full';  
	config.toolbar_Full =  
	[  
	    { name: 'basicstyles', items : [ 'Bold','Italic','Underline','Strike','Subscript','Superscript','-','RemoveFormat' ] },  
	    { name: 'paragraph', items : [ 'NumberedList','BulletedList','-','Outdent','Indent','-','Blockquote','CreateDiv',  
	    '-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','-','BidiLtr','BidiRtl' ] },  
	    { name: 'links', items : [ 'Link'] },  
	    //'Image',
	    { name: 'insert', items : [ 'Table','HorizontalRule','SpecialChar'] },  
	    '/',  
	    { name: 'styles', items : [ 'Styles','Format','Font','FontSize' ] },  
	    { name: 'colors', items : [ 'TextColor','BGColor' ] },  
	    { name: 'tools', items : [ 'Maximize', 'ShowBlocks' ] }  
	]; 
	 config.font_names = '宋体/SimSun;新宋体/NSimSun;仿宋/FangSong;楷体/KaiTi;仿宋_GB2312/FangSong_GB2312;'+  
     '楷体_GB2312/KaiTi_GB2312;黑体/SimHei;华文细黑/STXihei;华文楷体/STKaiti;华文宋体/STSong;华文中宋/STZhongsong;'+  
     '华文仿宋/STFangsong;华文彩云/STCaiyun;华文琥珀/STHupo;华文隶书/STLiti;华文行楷/STXingkai;华文新魏/STXinwei;'+  
     '方正舒体/FZShuTi;方正姚体/FZYaoti;细明体/MingLiU;新细明体/PMingLiU;微软雅黑/Microsoft YaHei;微软正黑/Microsoft JhengHei;'+  
     'Arial Black/Arial Black;'+ config.font_names;  
	// 取消 “拖拽以改变尺寸”功能
	config.resize_enabled = false;
	//CKEditor上传图片去掉超链接和高级选项卡以及上传和图像互换位置
	config.removeDialogTabs = 'image:advanced;image:Link;link:advanced;link:Upload;link:Link'; 
	
	config.image_previewText = '';
	config.filebrowserImageUploadUrl = "/system/upload/importImgInput.do";
};
