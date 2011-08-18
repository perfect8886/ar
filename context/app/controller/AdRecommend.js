Ext.define('ar.controller.AdRecommend', {
	extend :'Ext.app.Controller',
	requires : [ 'ar.model.Movie', 'ar.model.Ad', 'ar.view.movie.MovieList',
			'ar.view.movie.AdWin', 'ar.view.movie.AdView',
			'ar.view.movie.MovieWin', 'ar.view.movie.MovieView',
			'Ext.window.MessageBox' ],

	views : [ 'movie.MovieList', 'movie.AdWin', 'movie.AdView',
			'movie.MovieWin', 'movie.MovieView' ],

	stores : [ 'Movies', 'Ads' ],

	models : [ 'Movie', 'Ad' ],

	init : function() {
		this.control( {
			'movieList' : {
				itemdblclick :this.movieView
			},
			'movieWin button[action=view]' : {
				click :this.movieRecord
			},
			'adView' : {
				itemdblclick :this.adChosed
			},
			'movieList button[action=static]' : {
				click :this.userStatic
			}
		});
	},

	/** show the interest pie chart of user */
	userStatic : function(button) {

	},

	/** show the movie detail in a window */
	movieView : function(grid, record) {
		var win = Ext.create('ar.view.movie.MovieWin');
		win.show();
		var view = win.items.items[0];
		view.bind(record);
	},

	/** record the movie id, and then show the recommended ads */
	movieRecord : function(button) {
		var win = button.up('window');
		var view = win.items.items[0];
		var record = view.record;
		var id = record.get("id");
		Ext.Ajax.request( {
			url :'/movieServlet?method=viewMovie',
			params : {
				id :id
			},
			success : function(response) {
				win.close();
				Ext.create('ar.view.movie.AdWin').show();
			},
			failure : function(response) {
				win.close();
			}
		});
	},

	/** record the ad id that the user choose */
	adChosed : function(view, record) {
		var id = record.get("id");
		Ext.Ajax.request( {
			url :'/movieServlet?method=viewAd',
			params : {
				id :id
			},
			success : function(response) {
				view.up('window').close();
				Ext.Msg.alert('Info', '<div align="center"><b>感  谢  观  赏！</b></div>');
			},
			failure : function(response) {

			}
		});
	}
});