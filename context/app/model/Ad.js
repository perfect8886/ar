Ext.define('ar.model.Ad',
		{
			extend :'Ext.data.Model',
			fields : [ 'name', 'area', 'publishTime', 'desc', 'imageUrl',
					'actors', 'types', 'directors', 'id', 'movieSimilarity',
					'interest', 'result' ]
		});