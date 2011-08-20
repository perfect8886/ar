Ext.define('ad.store.Ads', {
	extend :'Ext.data.Store',
	requires : [ 'ad.model.Ad' ],
	model :'ad.model.Ad',

	autoLoad :false,

	proxy : {
		type :'ajax',
		url :'/adServlet?method=list',
		reader : {
			type :'json',
			root :'ads'
		}
	}
});