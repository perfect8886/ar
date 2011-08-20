Ext
		.define(
				'ad.view.AdForm',
				{
					extend :'Ext.form.Panel',
					alias :'widget.adForm',
					requires : [ 'Ext.slider.*' ],
					width :500,
					bodyPadding :10,
					layout :'anchor',
					autoScroll :true,
					defaults : {
						anchor :'100%'
					},
					initComponent : function() {
						// TODO single combox for 风格，地区,multi combox for
					// 演员,store:所有演员，所有地区，所有风格，分页显示
					// TODO tricks.gif 背景图片
					this.items = [
							{
								xtype :'fieldset',
								width :500,
								title :'广告信息',
								defaultType :'textfield',
								collapsible :true,
								layout :'column',
								autoScroll :true,
								defaults : {
									layout :'anchor',
									defaults : {
										anchor :'100%'
									}
								},
								items : [
										{
											columnWidth :2 / 7,
											xtype :'displayfield',
											name :'imgUrl',
											value :'<img src="http://img3.douban.com/mpic/s6565629.jpg" alr="Pic"/>'
										},
										{

											columnWidth :5 / 7,
											xtype :'fieldset',
											title :'广告信息',
											defaultType :'textfield',
											layout :'anchor',
											defaults : {
												anchor :'100%'
											},
											items : [
													{
														fieldLabel :'名称',
														name :'name',
														allowBlank :false
													},
													{
														fieldLabel :'海报链接',
														name :'imgUrl',
														allowBlank :false
													},
													{
														xtype :'combobox',
														fieldLabel :'演员',
														displayField :'name',
														valueField :'id',
														width :300,
														store :Ext
																.create(
																		'Ext.data.ArrayStore',
																		{}),
														queryMode :'local',
														remoteFilter :true,
														// typeAhead :true,
														multiSelect :true
													},
													{
														xtype :'combobox',
														fieldLabel :'风格',
														displayField :'name',
														valueField :'id',
														width :300,
														store :Ext
																.create(
																		'Ext.data.ArrayStore',
																		{}),
														queryMode :'local',
														remoteFilter :true,
														typeAhead :true
													},
													{
														xtype :'combobox',
														fieldLabel :'地区',
														displayField :'name',
														valueField :'id',
														width :300,
														store :Ext
																.create(
																		'Ext.data.ArrayStore',
																		{}),
														queryMode :'local',
														remoteFilter :true,
														typeAhead :true
													}, {
														fieldLabel :'发行时间',
														name :'publishTime'
													} ]
										} ]
							},
							{
								width :500,
								xtype :'fieldset',
								title :'投放策略',
								collapsible :true,
								defaultType :'textfield',
								layout :'anchor',
								defaults : {
									anchor :'100%'
								},
								items : [
										{
											xtype :'datefield',
											width :300,
											fieldLabel :'起始日期',
											name :'publishStartDate',
											format :'Y-m-d',
											value :'2011-08-19',
											maxValue :new Date()
										},
										{
											xtype :'datefield',
											width :300,
											fieldLabel :'结束日期',
											name :'publishEndDate',
											format :'Y-m-d',
											value :'2012-12-21'
										},
										{
											xtype :'multislider',
											width :214,
											fieldLabel :'投放时段',
											values : [ 10, 200 ],
											increment :10,
											minValue :0,
											maxValue :240,
											tipText : function(thumb) {
												return String(thumb.value / 10) + ':00';
											},
											cls :'slider'
										}, {
											xtype :'textarea',
											fieldLabel :'投放IP',
											name :'ipPrefix'
										} ]
							} ];

					this.callParent(arguments);
				}
				});