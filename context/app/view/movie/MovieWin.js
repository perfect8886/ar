Ext.define('ar.view.movie.MovieWin', {
	extend :'Ext.window.Window',
	alias :'widget.movieWin',
	title :'电影详情',
	layout :'fit',
	autoShow :false,
	height :450,
	width :450,
	initComponent : function() {
		this.items = [ Ext.create('ar.view.movie.MovieView') ];
		this.buttons = [ {
			text :'观看',
			action :'view'
		}, {
			text :'取消',
			scope :this,
			handler :this.close
		} ];

		this.callParent(arguments);
	}
});