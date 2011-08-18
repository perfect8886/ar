Ext.define('ar.view.movie.AdWin', {
	extend :'Ext.window.Window',
	requires : [ 'ar.store.Ads', 'ar.view.movie.AdView' ],
	// alias :'widget.adList',
	title :'请选择广告',
	layout :'fit',
	autoShow :false,
	height :300,
	width :680,
	initComponent : function() {
		this.items = [ {
			xtype :'adView',
			trackOver :true
		} ];
		// this.items = [ {
	// xtype :'panel',
	// title :'My Images',
	// layout :'fit',
	// padding :'5 5 5 0',
	// items : {
	// xtype :'imageview',
	// /***************************************************************
	// * (add a '/' at the front of this line to turn this on)
	// * listeners: { containermouseout: function (view, e) {
	// * Ext.log('ct', e.type); }, containermouseover: function (view,
	// * e) { Ext.log('ct', e.type); }, itemmouseleave: function
	// * (view, record, item, index, e) { Ext.log('item', e.type, '
	// * id=', record.id); }, itemmouseenter: function (view, record,
	// * item, index, e) { Ext.log('item', e.type, ' id=', record.id); }
	// * },/
	// **************************************************************/
	// trackOver :true
	// }
	// } ];
	this.callParent(arguments);
}
});