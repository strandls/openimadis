/**
 * Model capturing Microscope Profile
 */
Ext.define('Manage.model.admin.MicroscopeProfile', {
    extend : 'Ext.data.Model',
    fields : ['profileName', 'microscopeName', 'xPixelSize', 'yPixelSize', 'zPixelSize', 'timeUnit', 'exposureTimeUnit', 'lengthUnit', 'sourceType', 'xType', 'yType', 'zType',
	{
		name : 'timeUnitLowerCase',
		convert : function(value, record) {
			var unit = record.get('timeUnit');
			return unit.toLowerCase();
		}
	},{
		name : 'exposureTimeUnitLowerCase',
		convert : function(value, record) {
			var unit = record.get('exposureTimeUnit');
			return unit.toLowerCase();
		}
	}, {
		name : 'lengthUnitLowerCase',
		convert : function(value, record) {
			var unit = record.get('lengthUnit');
			return unit.toLowerCase();
		}
	}]
});
