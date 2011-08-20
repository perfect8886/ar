Ext.define('ad.view.AdWin', {
	extend :'Ext.window.Window',
	requires : [ 'ad.view.AdForm' ],
	title :'定制广告',
	layout :'fit',
	autoShow :false,
	height :500,
	width :500,
	initComponent : function() {
		this.items = [ {
			xtype :'adForm'
		} ];
		this.buttons = [ {
			text :'保存',
			action :'save'
		}, {
			text :'取消',
			scope :this,
			handler :this.close
		} ];
		this.callParent(arguments);
	}
});

Ext.create('ad.view.AdWin');