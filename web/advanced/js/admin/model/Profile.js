/**
 * Model capturing Microscope Profile
 */
Ext.define('Admin.model.Profile', {
    extend : 'Ext.data.Model',
    fields : ['profileName', 'microscopeName', 'xPixelSize', 'yPixelSize', 'zPixelSize', 'timeUnit', 'lengthUnit', 'sourceType', 'xType', 'yType', 'zType',
	{
		name : 'timeUnitLowerCase',
		convert : function(value, record) {
			var unit = record.get('timeUnit');
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
