/**
 * This view is an example list of people.
 */
Ext.define('WIS.view.main.List', {
    extend: 'Ext.grid.Grid',
    xtype: 'mainlist',

    requires: [
        'WIS.store.Personnel'
    ],

    title: 'Personnel',

    store: {
        type: 'personnel'
    },
    columns: [
        { text: 'Name',  dataIndex: 'name' },
        { text: 'Email', dataIndex: 'email', flex: 1 },
        { text: 'Phone', dataIndex: 'phone', flex: 1 }
    ],

    listeners: {
        select: 'onItemSelected'
    }
});