(function ($) {
    'use strict';

//    $.extend($.fn.bootstrapTable.defaults, {
//        fixedColumns: false,
//        fixedNumber: 1
//    });
    var getFieldIndex = function (columns, field) {
        var index = -1;

        $.each(columns, function (i, column) {
            if (column.field === field) {
                index = i;
                return false;
            }
            return true;
        });
        return index;
    };

    var BootstrapTable = $.fn.bootstrapTable.Constructor,
        _initToolbar = BootstrapTable.prototype.initToolbar;
    
    BootstrapTable.prototype.toggleColumn = function (index, checked, needUpdate) {
        if (index === -1) {
            return;
        }
        this.columns[index].visible = checked;
        this.initHeader();
        this.initSearch();
        this.initPagination();
        this.initBody();

        if (this.options.showColumns) {
        	var $colItems = this.$toolbar.find('.keep-open input:not(.table_selectAll)');
        	var $selectAllItem = this.$toolbar.find('.keep-open input.table_selectAll');
            var $items = $colItems.prop('disabled', false);
            var $selectedItems = this.$toolbar.find('.keep-open input:not(.table_selectAll):checked');

            if (needUpdate) {
                $items.filter(sprintf('[value="%s"]', index)).prop('checked', checked);
            }
            
            if (!checked) {
            	$selectAllItem.prop('checked', checked);
            } else {
            	if (($colItems.length) == $selectedItems.length) {
            		$selectAllItem.prop('checked', checked);
            	}
            }

            if ($items.filter(':checked').length <= this.options.minimumCountColumns) {
                $items.filter(':checked').prop('disabled', true);
            }
        }
    };

    BootstrapTable.prototype.initToolbar = function () {
    	var $selectAll,
    		that = this;
    	_initToolbar.apply(this, Array.prototype.slice.apply(arguments));

    	 if (!this.options.showColumns) {
            return;
        }
    	 var $selectdropdownmenu = this.$toolbar.find('ul.dropdown-menu')
    	 $selectAll = $('<li><label><input type="checkbox" data-field="-1" value="1" class="table_selectAll" checked="checked">' + commons_msg.selectAll + '</label></li>');
    	 $selectAll.off('click').on('click', function (event) {
             event.stopImmediatePropagation();
         });
    	 var $selectAllInput = $selectAll.find('input');
    	 $selectAllInput.off('click').on('click', function() {
    		 for (var i = that.columns.length - 1; i >= 0; i--) {
    			 var column = that.columns[i];
    			 if (column.radio || column.checkbox) {
                     continue;
                 }
                 if (that.options.cardView && (!column.cardVisible)) {
                	 continue;
                 }
                 var checked = $selectAllInput.prop('checked');
                 if (column.switchable) {
                	 var currentInput = $selectdropdownmenu.find('input[data-field="' + column.field + '"]');
                	 if (currentInput.prop('checked') != checked) {
                		 currentInput.click();
					}
                 }
			 }
    	 });
    	 $($selectdropdownmenu.find('li')[0]).before($selectAll);
    	 
    };
})(jQuery);