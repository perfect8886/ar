Ext.define('ad.controller.AdManager', {
	extend :'Ext.app.Controller',
	// requires : [ 'ad.model.Ad', 'ad.view.AdList', 'Ext.window.MessageBox' ],

	// views : [ 'AdList', 'AdForm' ],
	views : [ 'AdForm' ],
	// stores : [ 'Ads' ],

	// models : [ 'Ad' ],

	init : function() {
		this.control( {
			'adList' : {
				itemdblclick :this.adEdit
			}
		});
	},

	adEdit : function(grid, record) {

	}
});