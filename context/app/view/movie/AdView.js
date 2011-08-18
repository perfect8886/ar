Ext
		.define(
				'ar.view.movie.AdView',
				{
					extend :'Ext.view.View',
					alias :'widget.adView',
					requires : [ 'ar.store.Ads', 'Ext.tip.*' ],

					tpl :new Ext.XTemplate(
							'<tpl for=".">',
							'<div class="thumb-wrap">',
							'<div class="thumb">',
							(!Ext.isIE6 ? '<img src="{imageUrl}" />'
									: '<div style="width:76px;height:76px;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src=\'{imageUrl}\')"></div>'),
							'</div>',
							' <div align="center"><b>{name}</b></div>',
							'<HR align=left width=180 color=#000000 SIZE=1 shade>',
							' <div align="left"><b>影片相似：</b>{[this.stars(values.movieSimilarity)]}</div>',
							' <div align="left"><b>兴趣相似：</b>{[this.stars(values.interest)]}</div>',
							' <div align="left"><b>综合推荐：</b>{[this.stars(values.result)]}</div>',
							'</div>',
							'</tpl>',
							{
								stars : function(value) {
									var res = "";

									for ( var i = 0; i < (value * 5 - 1); i++) {
										res += '<img src="./img/star_full.' + ((Ext.isIE6) ? 'ico'
												: 'png') + '" />';
									}
									var rest = 5 * value - i;
									if (rest < 0.33) {

									} else if (rest >= 0.33 && rest < 0.67) {
										res += '<img src="./img/star_half.' + ((Ext.isIE6) ? 'ico'
												: 'png') + '" />';
										i++;
									} else {
										res += '<img src="./img/star_full.' + ((Ext.isIE6) ? 'ico'
												: 'png') + '" />';
										i++;
									}
									while (i < 5) {
										res += '<img src="./img/star_empty.' + ((Ext.isIE6) ? 'ico'
												: 'png') + '" />';
										i++;
									}

									return res;
								}
							}),

					itemSelector :'div.thumb-wrap',
					singleSelect :true,
					cls :'x-image-view',
					autoScroll :true,

					initComponent : function() {
						Ext.QuickTips.init();
						this.store = Ext.create('ar.store.Ads');
						this.store.load();

						this
								.on(
										'render',
										function(view) {
											view.tip = Ext
													.create(
															'Ext.tip.ToolTip',
															{
																target :view.el,
																delegate :view.itemSelector,
																trackMouse :true,
																renderTo :Ext
																		.getBody(),
																listeners : {
																	beforeshow : function updateTipBody(
																			tip) {
																		var record = view
																				.getRecord(tip.triggerElement);
																		tip
																				.update('风格：'
																						+ record
																								.get('types')
																						+ '<br><HR align=left width=490 color=#000000 SIZE=1 shade>'
																						+ '地区：'
																						+ record
																								.get('area')
																						+ '<br><HR align=left width=490 color=#000000 SIZE=1 shade>'
																						+ '发行时间：'
																						+ record
																								.get('publishTime')
																						+ '<br><HR align=left width=490 color=#000000 SIZE=1 shade>'

																						+ '主演：'
																						+ record
																								.get('actors'));
																	}
																}
															});
										});

						this.callParent();
					}
				});