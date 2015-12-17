/**
 * Header for the home page
 * Header has two sections. The left side has some banner about the app
 * Right side has info about the logged in user and logout link.
 */
Ext.require([
    'Manage.view.settings.SettingsPanel', 'Manage.view.ProjectUsers','Manage.view.ChangePassword'
]);

Ext.define('Manage.view.Header', {
    extend : 'Ext.toolbar.Toolbar',
    xtype : 'imgHeader',
    alias : 'widget.imgHeader',
    height : 30,
    initComponent : function() {
		var appDetails=Ext.JSON.decode(syncAJAXcallUrl('../project/getWebApplicationVersion')); 
        var items = [{
            text: '<h1>' + appDetails.name + '</h1>'
        }, '->', {
            xtype : 'label',
            // TRICK: index.jsp dumps the welcome message in a hidden div with id "welcomeMessage"
            // Get the same and load it here
            text : Ext.get("welcomeMessage").dom.innerHTML
        },'-', ' ', {
            xtype : 'label',
            html : '<a href="#">Users</a>', 
            listeners : {
        		click : {
					element : 'el',
					fn:function(){
			        	Ext.create('Ext.window.Window', {
			                title : 'Project Members',
			                width : 300,
			                height : 500,
			                items : [{
			                    xtype : 'projectUsers'
			                }]
			            }).show();
					}
				}
        	}
        },'-', ' ', {
            xtype : 'label',
            html : '<a href="#">Change Password</a>', 
            listeners : {
        		click : {
					element : 'el',
					fn:function(){
			        	Ext.create('Ext.window.Window', {
			                title : 'Change Password',
			                width : 300,
			                height : 200,
			                items : [{
			                    xtype : 'changePassword'
			                }]
			            }).show();
					}
				}
        	}
        }, '-', ' ', {
            xtype : 'label',
            html : '<a href="../auth/logout?success=advanced">Logout</a>'
        }, ' ', '-',' ', {
            xtype : 'label',
            html : '<a href="../help.html" target="_blank">Help</a>'
        }, ' '];
        if (showAcqLink) {
            items.splice(3,0,{
                xtype : 'label',
            	html : '<a href="../project/launchAcqClient">Launch Acquisition</a>'
            });
        }
        Ext.apply(this, {
            items : items 
        });
        this.callParent();
    }
});

