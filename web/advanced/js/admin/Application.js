Ext.Loader.setConfig({
    enabled:true,
    disableCaching : false,
    paths: {
        'Admin': 'js/admin',
        'Manage': 'js/app'	
    }
});

// Models
Ext.require('Admin.model.User');
Ext.require('Admin.model.UserRank');
Ext.require('Admin.model.Unit');
Ext.require('Admin.model.UnitType');
Ext.require('Admin.model.UserStatus');
Ext.require('Admin.model.Project');
Ext.require('Admin.model.Client');
Ext.require('Admin.model.Publisher');
Ext.require('Admin.model.Membership');
Ext.require('Admin.model.UserRole');
Ext.require('Admin.model.UserAnnotation');
Ext.require('Admin.model.LogModel');
Ext.require('Admin.model.logging.UsageLogs');
Ext.require('Admin.model.logging.ProjectUsageLogs');
Ext.require('Admin.model.Downloads');
Ext.require('Admin.model.AuthCodes');
Ext.require('Admin.model.License');
Ext.require('Manage.model.Client');
Ext.require('Admin.model.Uploads');
Ext.require('Admin.model.Microscope');
Ext.require('Admin.model.Profile');
Ext.require('Admin.model.ProfileTimeUnit');
Ext.require('Admin.model.ProfileLengthUnit');
Ext.require('Admin.model.ProfileType');


// Stores
Ext.require('Admin.store.UserStore');
Ext.require('Admin.store.UnitStore');
Ext.require('Admin.store.UnitTypeStore');
Ext.require('Admin.store.UserRankStore');
Ext.require('Admin.store.UserStatusStore');
Ext.require('Admin.store.ProjectStore');
Ext.require('Admin.store.ClientStore');
Ext.require('Admin.store.PublisherStore');
Ext.require('Admin.store.ProjectStatusStore');
Ext.require('Admin.store.MembershipStore');
Ext.require('Admin.store.UserProjectStore');
Ext.require('Admin.store.UserRoleStore');
Ext.require('Admin.store.AvailableStore');
Ext.require('Admin.store.SelectedStore');
Ext.require('Admin.store.AvailableLDAPUsersStore');
Ext.require('Admin.store.SelectedLDAPUsersStore');
Ext.require('Admin.store.AvailableUnitsStore');
Ext.require('Admin.store.SelectedUnitsStore');
Ext.require('Admin.store.LogStore');
Ext.require('Admin.store.UserAnnotationStore');
Ext.require('Admin.store.logging.UsageLogs');
Ext.require('Admin.store.logging.ProjectUsageLogs');
Ext.require('Manage.store.AllClientStore');
Ext.require('Admin.store.AssociationStore');
Ext.require('Admin.store.DownloadsStore');
Ext.require('Admin.store.AuthCodesStore');
Ext.require('Admin.store.LicenseStore');
Ext.require('Admin.store.UploadsStore');
Ext.require('Admin.store.MicroscopeStore');
Ext.require('Admin.store.ProfileStore');
Ext.require('Admin.store.ProfileTimeUnitStore');
Ext.require('Admin.store.ProfileLengthUnitStore');
Ext.require('Admin.store.ProfileTypeStore');

// Controllers
Ext.require('Admin.controller.UserController');
Ext.require('Admin.controller.UnitController');
Ext.require('Admin.controller.ProjectController');
Ext.require('Admin.controller.MembershipController');
Ext.require('Admin.controller.UserAnnotationController');
Ext.require('Admin.controller.LoggingController');
Ext.require('Admin.controller.MicroscopeController');
Ext.require('Admin.controller.ProfileController');

// Main view
Ext.require('Admin.view.Viewport');

/**
 * Making sure console does not throw error when it is not available
 */
if (typeof console === "undefined") {
        console = {
                log : function() {
                },
                warn : function() {
                },
                error : function() {
                }
        };
}

if (typeof Ext.global.console === "undefined") {
        Ext.global.console = console;
}


Ext.application({
    name: 'Admin',
    autoCreateViewport: false,
    
    // This model and store specification will create an instance for each store loaded, 
    // giving it a storeId equal to its name. 
    // This allows us to use the name of the store whenever we bind it to a data component 
    
    models: ['Admin.model.User', 'Admin.model.Project','Admin.model.Client','Admin.model.Publisher', 'Admin.model.Membership', 'Admin.model.LogModel',
             'Admin.model.UserRank', 'Admin.model.UserStatus',
             'Admin.model.UserRole', 'Admin.model.UnitType', 'Admin.model.Unit', 'Admin.model.UserAnnotation',
             'Admin.model.logging.UsageLogs','Admin.model.logging.ProjectUsageLogs','Manage.model.Client' , 'Admin.model.Downloads', 'Admin.model.AuthCodes' , 'Admin.model.License', 'Admin.model.Uploads', 'Admin.model.Microscope', 'Admin.model.Profile', 'Admin.model.ProfileTimeUnit', 'Admin.model.ProfileLengthUnit', 'Admin.model.ProfileType'],
    stores: [ 'Admin.store.UserStore', 'Admin.store.ProjectStore','Admin.store.ClientStore','Admin.store.PublisherStore','Admin.store.UnitTypeStore','Admin.store.UnitStore',
              'Admin.store.MembershipStore','Admin.store.UserProjectStore','Admin.store.UserRankStore',
              'Admin.store.UserStatusStore', 'Admin.store.ProjectStatusStore', 
              'Admin.store.UserRoleStore','Admin.store.AvailableStore', 
              'Admin.store.SelectedStore','Admin.store.AvailableUnitsStore', 
              'Admin.store.SelectedUnitsStore','Admin.store.AvailableLDAPUsersStore', 
              'Admin.store.SelectedLDAPUsersStore','Admin.store.UserAnnotationStore', 'Admin.store.LogStore', 
              'Admin.store.logging.UsageLogs', 'Admin.store.logging.ProjectUsageLogs','Manage.store.AllClientStore', 'Admin.store.AssociationStore' , 'Admin.store.DownloadsStore', 'Admin.store.AuthCodesStore' , 'Admin.store.LicenseStore', 'Admin.store.UploadsStore', 'Admin.store.MicroscopeStore', 
'Admin.store.ProfileStore', 'Admin.store.ProfileTimeUnitStore', 'Admin.store.ProfileLengthUnitStore', 'Admin.store.ProfileTypeStore'],
    
    // When including the controllers in the application, the framework will
    // automatically load the controller and call the init method on it.
    // Inside the init method, one should set up listeners for view and 
    // application events. In larger applications, one might want to load additional controllers
    // at runtime. This can be done by using the getController method.
    controllers: ['Admin.controller.UserController', 'Admin.controller.ProjectController',
                  'Admin.controller.MembershipController', 'Admin.controller.UserAnnotationController',
                  'Admin.controller.LoggingController','Admin.controller.UnitController',
		  'Admin.controller.MicroscopeController', 'Admin.controller.ProfileController'],
    
    launch: function() {
        Ext.create('Admin.view.Viewport', {});
        
        // Logout on exit
        Ext.EventManager.on(window, 'unload', function() {
            Ext.Ajax.request( {
                method : 'POST',
                url : '../auth/logout'
            });
        });
    }
});
