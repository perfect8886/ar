Ext.application( {
	name :'ar',

	appFolder :'app',

	launch : function() {
		Ext.create('Ext.container.Viewport', {
			layout :'border',
			items : [ {
				region :'center',
				xtype :'movieList',
				title :'<h1>视频广告推荐原型系统</h1>'
			} ]
		});
	},

	controllers : [ 'AdRecommend' ]
});