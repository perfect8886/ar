Ext
		.define(
				'ad.view.AdList',
				{
					extend :'Ext.grid.Panel',
					requires : [ 'ad.store.Ads', 'Ext.tip.*' ],
					alias :'widget.adList',

					store :Ext.create('ad.store.Ads', {
						storeId :'adStore'
					}),
					viewConfig : {
						stripeRows :true
					},
					hideHeaders :true,

					initComponent : function() {
						var store = Ext.data.StoreManager.lookup('adStore');
						store.load( {
							params : {
								start :0,
								limit :10
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
									tpl :'<b>名称：</b>{name}<br><br><b>地区：</b>{areas}<br><br><b>发行日期：</b>{publishTime}<br><br><b>类别：</b>{types}<br><br><b>导演：</b>{directors}',
									width :350,
									flex :1
								},
								{
									header :'主演',
									dataIndex :'actors',
									renderer : function(value) {
										var actor = value.toString().split(",");
										var str = "";
										var i = 0;
										for (i; i < actor.length; i++) {
											str += actor[i];
											if ((i + 1) % 5 != 0) {
												if (i != actor.length - 1) {
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