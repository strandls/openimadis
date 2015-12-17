/**
 * The main viewport. Combines all the views together to organise
 * the application. Takes care of authorization also. Only views
 * which can be seen by the logged in user are shown
 */
Ext.define('Admin.view.Viewport', {
    extend : 'Ext.container.Viewport',
    requires : [
        'Admin.view.UserList', 'Admin.view.Header', 'Admin.view.DownloadsList' , 'Admin.view.AuthCodesList','Admin.view.LicenseList','Admin.view.UploadsList',
        'Admin.view.ProjectList', 'Admin.view.ClientList', 'Admin.view.PublisherList', 'Admin.view.Membership',
        'Admin.view.UserAnnotationView', 'Admin.view.logging.UsageLogs','Admin.view.logging.ProjectUsageLogs',
        'Admin.view.UnitList', 'Admin.view.AssociationList', 'Admin.view.Association', 'Admin.view.LogList', 'Admin.view.UserProjects', 'Admin.view.UserProjectList',
	'Admin.view.MicroscopeList', 'Admin.view.ProfileList', 'Admin.view.MicroscopeProfiles'
    ],
    border : false,
    initComponent : function() {
		console.log("init");
        var views = this.getViewsToShow();
        Ext.apply (this, {
            layout : 'border',
            items : [{
                xtype : 'panel',
                region : 'center',
                layout : 'border',
                items : [{
                    xtype : 'imgHeader',
                    region : 'north',
                    split : 'false'
                }, {
                    xtype : 'tabpanel',
                    border : false,
                    region : 'center',
                    items : [views]
                }]
            }, {
                xtype : 'panel',
                region : 'west',
                width : 1
            }, {
                xtype : 'panel',
                region : 'east',
                width : 1
            }]
        });
        this.callParent();
    },

    getViewsToShow : function() {
    	console.log("views to show here");
        if (viewsToShow === undefined || viewsToShow === null) {
            viewsToShow = [];
        }
        var items = [];
        for (var i=0; i < viewsToShow.length; ++i) {
            items.push({
                xtype : viewsToShow[i]
            });
        }
        return items;
    }
});
