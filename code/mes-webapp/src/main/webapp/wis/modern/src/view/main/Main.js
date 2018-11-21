/**
 * This class is the main view for the application. It is specified in app.js as the
 * "mainView" property. That setting causes an instance of this class to be created and
 * added to the Viewport container.
 *
 * TODO - Replace the content of this view to suit the needs of your application.
 */
Ext.define('WIS.view.main.Main', {
    extend: 'Ext.tab.Panel',
    xtype: 'app-main',

    requires: [
        'Ext.MessageBox',
		'Ext.dataview.List',
		'Ext.plugin.PullRefresh',
        'WIS.view.main.MainController',
        'WIS.view.main.MainModel',
        'WIS.view.main.List',
        'WIS.view.main.Dataview',
        'WIS.view.main.Form'
    ],

    controller: 'main',
    viewModel: 'main',

    defaults: {
        tab: {
            iconAlign: 'top'
        },
        styleHtmlContent: true
    },

    tabBarPosition: 'bottom',

    items: [
        {
            title: 'Home',
            iconCls: 'x-fa fa-home',
            layout: 'fit',
            // The following grid shares a store with the classic version's grid as well!
            items: [{
                xtype: 'mainlist'
            }]
        },{
            title: 'Users',
            iconCls: 'x-fa fa-user',
            layout: 'fit',
            items: [{
                xtype: 'maindataview'
            }]
        },{
            title: 'Groups',
            iconCls: 'x-fa fa-users',
            layout: 'fit',
            items: [{
                xtype: 'maindataview',
                itemTpl: [
     					 '<div> <b>{name}:</b> {text} </div>',
                       '<div><img src="https://www.sencha.com/forum/images/statusicon/forum_new-48.png" alt=" photo" /></div>'
                       ]
            }]
        },{
            title: 'Settings',
            iconCls: 'x-fa fa-cog',
            layout: 'fit',
            items: [{
                xtype: 'mainform'
            }]
        }
    ]
});
