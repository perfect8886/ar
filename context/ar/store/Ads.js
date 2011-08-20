Ext.define('ar.store.Ads', {
	extend :'Ext.data.Store',
	requires : [ 'ar.model.Ad' ],
	model :'ar.model.Ad',

	autoLoad :false,

	proxy : {
		type :'ajax',
		url :'/arServlet?method=listAds',
		reader : {
			type :'json',
			root :'ads'
		}
	}
});