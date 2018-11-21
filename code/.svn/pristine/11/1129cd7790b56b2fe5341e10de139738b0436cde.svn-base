Ext.define('WIS.view.main.Dataview', {
	extend: "Ext.dataview.List",
    xtype: 'maindataview',
    variableHeights: true,
    infinite: true,
    disableSelection: true,
    allowDeselect: false,
    scrollToTopOnRefresh: false,
    emptyText: '<p class="no-searches">No tweets found matching that search</p>',
    store: {
        type: 'personnel'
    },
    listeners: {
        select: 'onItemSelected'
    },
    plugins: [{
        xclass: 'Ext.plugin.PullRefresh',
        pullText: 'Pull down for more new Tweets!'
    }],
    itemTpl: [
      	'<div>{name}</div>',
      	'<div>{email}</div>'
      ]
});