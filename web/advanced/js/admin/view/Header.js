/**
 * Header for the home page
 * Header has two sections. The left side has some banner about the app
 * Right side has info about the logged in user and logout link.
 */
Ext.define('Admin.view.Header', {
    extend : 'Ext.toolbar.Toolbar',
    xtype : 'imgHeader',
    alias : 'widget.imgHeader',
    height : 30,
    initComponent : function() {
		var appDetails=Ext.JSON.decode(syncAJAXcallUrl('../admin/getWebAdminApplicationVersion'));
		var items = [{
	    	text: '<h1>' + appDetails.name + '</h1>'
	    }, '->', {
	        xtype : 'label',
	        // TRICK: index.jsp dumps the welcome message in a hidden div with id "welcomeMessage"
	        // Get the same and load it here
	        text : Ext.get("welcomeMessage").dom.innerHTML
	    }, ' ', '-', ' ', {
	        xtype : 'label',
	        html : '<a href="../auth/logout">Logout</a>'
	    }, ' ', '-',' ', {
	        xtype : 'label',
	        html : '<a href="../help.html" target="_blank">Help</a>'
	    }, ' '];
		
		Ext.apply(this, {
            items : items 
        });
        this.callParent();
	}
	
    
});
