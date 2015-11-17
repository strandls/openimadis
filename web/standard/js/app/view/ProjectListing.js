/**
 * View for project listing
 * The view consists of two panels,
 * 1. Recent projects - which shows the recent projects opened by the user
 * 2. All projects - Which shows all the projects the user is part of in 
 * the form of a detailed spreadsheet
 */
Ext.define('Manage.view.ProjectListing', {
    extend : 'Ext.tab.Panel',
    xtype : 'projectListing',
    alias : 'widget.projectListing',

    requires: [
    	'Manage.view.RecentProjects', 'Manage.view.AllProjects'
    ],

    items : [
        {
            xtype : 'recentProjects',
            listeners : {
                'addProject' : function(projectName, recordCount) {
                    this.up().fireEvent("changeProject", projectName, recordCount);
                }
            }
        }, {
            xtype : 'allProjects',
            listeners : {
                'addProject' : function(projectName, recordCount) {
                    this.up().fireEvent("changeProject", projectName, recordCount);
                }
            }
        }
    ],
    listeners : {
        beforetabchange : function(view, newCard, oldCard, opts) {
            newCard.getView().refresh();
        }
    }
});
