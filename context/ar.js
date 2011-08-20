Ext.application( {
	name :'ar',

	appFolder :'ar',

	launch : function() {
		Ext.create('Ext.container.Viewport', {
			layout :'border',
			items : [ {
				region :'center',
				xtype :'movieList',
				title :'<h1>视频广告推荐原型系统--用户体验</h1>'
			} ]
		});
	},

	controllers : [ 'AdRecommend' ]
});