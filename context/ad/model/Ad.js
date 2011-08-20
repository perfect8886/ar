Ext.define('ad.model.Ad', {
	extend :'Ext.data.Model',
	fields : [ 'name', 'areas', 'desc', 'imageUrl', 'actors', 'types',
			'directors', 'id', 'publishStartDate', 'publishEndDate',
			'startDayTime', 'endDayTime', 'ipPrefix' ]
});