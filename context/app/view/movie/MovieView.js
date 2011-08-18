Ext
		.define(
				'ar.view.movie.MovieView',
				{
					alias :'widget.movieView',
					extend :'Ext.panel.Panel',

					initComponent : function() {
						Ext.apply(this, {
							id :'itemCt',
							cls :'item-ct',
							// flex :2,
							border :false,
							autoScroll :true,

							layout : {
								type :'hbox',
								align :'middle',
								pack :'center'
							},

							items : [ {
								id :'imgCt',
								border :true,
								margin :'0 0 0 0',
								width :120,
								height :200
							}, {
								id :'contentCt',
								width :300,
								border :false
							} ]
						});

						this.callParent(arguments);
					},

					/**
					 * Binds a record to this view
					 */
					bind : function(record) {
						var imgCt = Ext.getCmp('imgCt'), contentCt = Ext
								.getCmp('contentCt');

						var imgTpl = new Ext.XTemplate(
								'<div>',
								(!Ext.isIE6 ? '<img src="{imageUrl}" />'
										: '<div style="width:76px;height:76px;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src=\'{imageUrl}\')"></div>'),
								'</div>');

						var contentTpl = new Ext.XTemplate(
								'<div>',
								'<div class="title">{name}</div>',
								'<HR align=left width=490 color=#990099 SIZE=3 shade>',
								'<div class="director"><span>导演：</span></div>',
								'<HR align=left width=490 color=#990099 SIZE=1 shade>',
								'<div class="director">{directors}</div>',
								'<div class="type"><span>类型：</span></div>',
								'<HR align=left width=490 color=#990099 SIZE=1 shade>',
								'<div class="type">{types}</div>',
								'<div class="publish"><span>发行日期：</span></div>',
								'<HR align=left width=490 color=#990099 SIZE=1 shade>',
								'<div class="publish">{publishTime}</div>',
								'<div class="area"><span>地区：</span></div>',
								'<HR align=left width=490 color=#990099 SIZE=1 shade>',
								'<div class="area">{area}</div>',
								'<div class="actor"><span>主演：</span></div>',
								'<HR align=left width=490 color=#990099 SIZE=1 shade>',
								'<div class="actor">{actors}</div>', '</div>');

						imgTpl.overwrite(imgCt.el, record.data);
						contentTpl.overwrite(contentCt.el, record.data);

						// update the layout of the contentTpl
						contentCt.setHeight('auto');
						this.doLayout();
						this.record = record;
					}
				});