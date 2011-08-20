Ext.define('ar.store.Movies', {
	extend :'Ext.data.Store',
	requires : [ 'ar.model.Movie' ],
	model :'ar.model.Movie',

	sorters : [ 'name' ],

	pageSize :10,

	autoLoad :false,

	proxy : {
		type :'ajax',
		url :'/arServlet?method=list',
		reader : {
			type :'json',
			root :'movies',
			totalProperty :'total',
			successProperty :'success',
			messageProperty :'message'
		}
	}
});
