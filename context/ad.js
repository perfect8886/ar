Ext.application( {
	name :'ad',

	appFolder :'ad',

	launch : function() {
		Ext.create('Ext.container.Viewport', {
			layout :'border',
			items : [ {
				region :'center',
				xtype :'adForm',
				title :'<h1>视频广告推荐原型系统--广告管理</h1>'
			} ]
		});
	},

	controllers : [ 'AdManager' ]
});