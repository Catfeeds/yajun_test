
Ext.define('WIS.view.main.WidgetGrid', {
    extend: 'Ext.grid.Panel',
    xtyle:'mainwidgetgrid',
    title: '收入分析',
    forceFit:true,
    store:  Ext.create('Ext.data.Store', {
        fields: ['name', 'progress'],
        data: [
            { name: 'Lisa', progress: .159, sequence1: [1, 2, 4, 5, 4, 5], sequence2: [1, -2, 4, 5, 4, -5], sequence3: [1, -2, 4, 5, 4, -5] },
            { name: 'Bart', progress: .216, sequence1: [1, 2, 4, 5, 4, 5], sequence2: [1, -2, 4, 5, 4, -5], sequence3: [1, -2, 4, 5, 4, -5] },
            { name: 'Homer', progress: .55, sequence1: [1, 2, 4, 5, 4, 5], sequence2: [1, -2, 4, 5, 4, -5], sequence3: [1, -2, 4, 5, 4, -5] },
            { name: 'Maggie', progress: .167, sequence1: [1, 2, 4, 5, 4, 5], sequence2: [1, -2, 4, 5, 4, -5], sequence3: [1, -2, 4, 5, 4, -5] },
            { name: 'Marge', progress: .145, sequence1: [1, 2, 4, 5, 4, 5], sequence2: [1, -2, 4, 5, 4, -5], sequence3: [1, -2, 4, 5, 4, -5] }
        ]
    }),
    columns: [
        {
            text: 'Name',
            dataIndex: 'name'
        },
        {
            text: 'progress',
            xtype: 'widgetcolumn',
            width: 120,
            dataIndex: 'progress',
            widget: {
                xtype: 'progressbarwidget',
                textTpl: [
                    '{percent:number("0")}% done'
                ]
            }
        }
        , {
            text: 'Line',
            width: 100,
            dataIndex: 'sequence2',
            xtype: 'widgetcolumn',
            widget: {
                xtype: 'sparklineline',
                tipTpl: 'Value: {y:number("0.00")}'
            }
        }
        , {
            text: 'Bar',
            width: 100,
            dataIndex: 'sequence2',
            xtype: 'widgetcolumn',
            widget: {
                xtype: 'sparklinebar'
            }
        }, {
            text: 'Bullet',
            width: 100,
            dataIndex: 'sequence3',
            xtype: 'widgetcolumn',
            widget: {
                xtype: 'sparklinebullet'
            }
        }
    ]
});

