Ext
		.define(
				'ar.view.movie.MovieList',
				{
					extend :'Ext.grid.Panel',
					requires : [ 'ar.store.Movies', 'Ext.tip.*' ],
					alias :'widget.movieList',

					store :Ext.create('ar.store.Movies', {
						storeId :'movieStore'
					}),
					viewConfig : {
						stripeRows :true
					},
					hideHeaders :true,

					initComponent : function() {
						// Ext.QuickTips.init();
					// this.tbar = Ext.create('Ext.toolbar.Toolbar', {
					// items : {
					// tooltip :'功能选项',
					// icon :'./img/system.png',
					// width :80,
					// height :49,
					// menu : {
					// xtype :'menu',
					// plain :true,
					// items : {
					// xtype :'buttongroup',
					// title :'User options',
					// columns :2,
					// defaults : {
					// xtype :'button',
					// scale :'large',
					// iconAlign :'left'
					// },
					// items : [ {
					// text :'兴趣区域图',
					// icon :'./img/piechart.png',
					// width :140,
					// hight :80,
					// action :'static'
					// } ]
					// }
					// }
					// }
					// });

					var store = Ext.data.StoreManager.lookup('movieStore');
					var ip = "";
					store.load( {
						params : {
							start :0,
							limit :10
						},
						callback : function(records, operation, success) {
							var result = operation.getResultSet();
							ip += result.message;
						}
					});

					this.columns = [
							{
								header :'ID',
								dataIndex :'id',
								hidden :true
							},
							{
								header :'海报',
								dataIndex :'imageUrl',
								renderer : function(value) {
									return '<img src="' + value + '" alt="Pic"/>';
								},
								align :'center',
								width :200
							},
							{
								header :'基本信息',
								xtype :'templatecolumn',
								tpl :'<b>名称：</b>{name}<br><br><b>地区：</b>{area}<br><br><b>发行日期：</b>{publishTime}<br><br><b>类别：</b>{types}<br><br><p style="font-size:150% font-family:黑体"><b>导演：</b>{directors}</p>',
								width :350,
								flex :1
							}, {
								header :'主演',
								dataIndex :'actors',
								renderer : function(value) {
									var actor = value.toString().split(",");
									var str = "";
									var i = 0;
									for (i; i < actor.length; i++) {
										str += actor[i];
										if ((i + 1) % 5 != 0) {
											if (i != actor.length-1) {
												str += ",  ";
											}
										} else {
											str += "<br><br>";
										}
									}
									return "<b>主演：</b><br><br>" + str;
								},
								flex :2
							} ];

					this.dockedItems = [ {
						xtype :'pagingtoolbar',
						store :store,
						dock :'bottom',
						displayInfo :true
					} ];
					this.callParent(arguments);
				}
				});