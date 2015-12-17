Ext.Loader.setConfig({
    enabled:true,
    disableCaching : false,
    paths: {
        'Manage': 'js/app',
        'Admin': 'js/admin'
    }
});

Ext.require([
	'Manage.controller.OverlayController'
]); 
Ext.require('Manage.view.Viewport');
Ext.require('Manage.model.Record');
Ext.require('Manage.model.Field');
Ext.require('Manage.model.Comment');
Ext.require('Manage.model.ChannelContrast');
Ext.require('Manage.model.LUT');
Ext.require('Manage.model.Overlay');
Ext.require('Manage.model.Channel');
Ext.require('Manage.store.summarytable.SummaryTablePagingStore');
Ext.require('Manage.store.summarytable.SummaryTableRecords');
Ext.require('Manage.model.Thumbnail');
Ext.require('Manage.store.ThumbnailRecords');
Ext.require('Manage.controller.RecordSelection');
Ext.require('Manage.controller.ImageState');
Ext.require('Manage.controller.NavigatorState');
Ext.require('Manage.controller.MovieController');
Ext.require('Manage.store.NavigatorStore');
Ext.require('Manage.store.BookmarkStore');
Ext.require('Manage.model.ImageMetaData');
Ext.require('Manage.store.ImageMetaDataList');
Ext.require('Manage.store.UserAttachmentStore');
Ext.require('Manage.store.SystemAttachmentStore');
Ext.require('Manage.store.CommentStore');
Ext.require(['Manage.store.HistoryStore', 'Manage.store.HistoryTypeStore']);
Ext.require('Manage.store.ChannelContrastStore');
Ext.require('Manage.store.LUTStore');
Ext.require(['Manage.store.ExportFormatStore', 'Manage.store.DownloadStore']);
Ext.require('Manage.store.ProjectUserStore');
Ext.require('Manage.store.MemberProjectStore');
Ext.require('Manage.model.ProjectUser');
Ext.require('Manage.store.OverlayStore');
Ext.require('Manage.store.ChannelStore');
Ext.require('Manage.store.SearchResultsStore');
Ext.require('Manage.model.RecordMetaData');
Ext.require('Manage.store.RecordMetaDataStore');
Ext.require('Manage.store.settings.AvailableFieldStore');
Ext.require('Manage.store.settings.SelectedFieldStore');
Ext.require('Manage.store.settings.FieldStore');
Ext.require('Manage.store.summarytable.SummaryTableAvailableColumnsStore');
Ext.require('Manage.store.summarytable.SummaryTableSelectedColumnsStore');
Ext.require('Manage.store.LegendAvailableFieldStore');
Ext.require('Manage.store.LegendSelectedFieldStore');
Ext.require('Manage.store.LegendFieldStore');
Ext.require('Manage.store.summarytable.UserFields');
Ext.require('Manage.store.summarytable.UserFieldTypes');
Ext.require('Manage.model.Project');
Ext.require('Manage.model.ProjectName');
Ext.require('Manage.store.ProjectStore');
Ext.require('Manage.store.ProjectNameStore');
Ext.require('Manage.store.task.PriorityStore');
Ext.require('Manage.store.task.TaskStateStore');
Ext.require('Manage.store.task.WorkflowClientStore');
Ext.require('Manage.store.task.TaskMonitorStore');
Ext.require('Manage.store.task.TaskInspectorStore');
Ext.require('Manage.model.Task');
Ext.require('Manage.store.task.TaskStore');
Ext.require('Manage.store.RecentProjectStore');
Ext.require('Manage.view.RecentProjectsPanel');
Ext.require('Manage.view.AllProjectsPanel');
Ext.require('Manage.view.ProjectListing');
Ext.require('Manage.view.SearchPanel');
Ext.require('Manage.view.SplitView');
Ext.require('Manage.view.imageview.ImageContrast');
Ext.require('Manage.view.Bookmarks');
Ext.require('Manage.view.Downloads');
Ext.require('Manage.controller.SummaryTableController');
Ext.require('Manage.view.params.ParamsDialog');
Ext.require(['Manage.model.Workflow', 'Manage.model.WorkflowCategory']);
Ext.require(['Manage.store.WorkflowStore', 'Manage.store.WorkflowTreeStore']);
Ext.require(['Manage.view.workflow.Workflows', 'Manage.view.workflow.AddClient', 'Manage.view.workflow.ViewClients', 'Manage.view.workflow.AddPublisher', 'Manage.view.workflow.ViewPublishers']);
Ext.require('Manage.controller.WorkflowController');
Ext.require('Manage.controller.TaskController');
Ext.require('Manage.controller.ZoomController');
Ext.require('Manage.controller.ImageViewController');
Ext.require(['Manage.store.UserAnnotationStore', 'Manage.view.UserAnnotations']);
Ext.require(['Manage.model.Publisher', 'Manage.store.PublisherStore']);
Ext.require(['Manage.model.Client', 'Manage.store.UserClientStore', 'Manage.store.AllClientStore']);
Ext.require(['Manage.model.Service', 'Manage.store.ServiceStore']);
Ext.require(['Manage.model.Token', 'Manage.store.workflow.TokenStore', 'Manage.store.workflow.TokenUsers']);
Ext.require('Manage.model.Microscope');
Ext.require('Manage.store.MicroscopeStore');
Ext.require('Manage.model.Profile');
Ext.require('Manage.model.ProfileType');
Ext.require('Manage.store.ProfileStore');
Ext.require('Manage.store.ProfileTypeStore');

Ext.require('Manage.model.ProfileTimeUnit');
Ext.require('Manage.store.ProfileTimeUnitStore');
Ext.require('Manage.model.ProfileLengthUnit');
Ext.require('Manage.store.ProfileLengthUnitStore');

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
    name: 'Manage',
    autoCreateViewport: false,
    
    // This model and store specification will create an instance for each store loaded, 
    // giving it a storeId equal to its name. 
    // This allows us to use the name of the store whenever we bind it to a data component 
    
    models: ['Manage.model.Record', 'Manage.model.Thumbnail','Manage.model.Profile', 'Manage.model.ImageMetaData', 'Manage.model.Field', 'Manage.model.Project','Manage.model.ProjectName', 
            'Manage.model.RecordMetaData', 'Manage.model.Overlay','Manage.model.Microscope', 'Manage.model.Channel', 'Manage.model.LUT', 'Manage.model.ChannelContrast', 'Manage.model.ExportFormat','Manage.model.MemberProject', 'Manage.model.Downloads', 'Manage.model.Workflow', 'Manage.model.WorkflowCategory',
            'Manage.model.Client', 'Manage.model.Publisher', 'Manage.model.HistoryTypeModel', 'Manage.model.Service', 'Manage.model.Token','Manage.model.Task', 'Manage.model.ProjectUser', 'Manage.model.ProfileTimeUnit', 'Manage.model.ProfileLengthUnit', 'Manage.model.ProfileType'],    
    stores: [ 'Manage.store.summarytable.SummaryTablePagingStore','Manage.store.MicroscopeStore', 'Manage.store.summarytable.SummaryTableRecords','Manage.store.ThumbnailRecords','Manage.store.ImageMetaDataList', 
              'Manage.store.NavigatorStore', 'Manage.store.ProjectUserStore' ,'Manage.store.ProfileStore' ,
              'Manage.store.settings.AvailableFieldStore', 'Manage.store.settings.SelectedFieldStore', 'Manage.store.ProjectStore', 'Manage.store.ProjectNameStore', 'Manage.store.RecentProjectStore',
              'Manage.store.RecordMetaDataStore', 'Manage.store.summarytable.SummaryTableAvailableColumnsStore', 'Manage.store.summarytable.SummaryTableSelectedColumnsStore',
              'Manage.store.summarytable.UserFields', 'Manage.store.LegendAvailableFieldStore','Manage.store.LegendSelectedFieldStore','Manage.store.LegendFieldStore', 'Manage.store.summarytable.UserFieldTypes', 'Manage.store.UserAttachmentStore', 'Manage.store.SystemAttachmentStore', 'Manage.store.CommentStore', 'Manage.store.HistoryTypeStore', 'Manage.store.HistoryStore', 
              'Manage.store.settings.FieldStore', 'Manage.store.SearchResultsStore', 'Manage.store.OverlayStore', 'Manage.store.ExportFormatStore', 'Manage.store.MemberProjectStore', 'Manage.store.DownloadStore', 'Manage.store.ChannelStore',
              'Manage.store.BookmarkStore', 'Manage.store.ChannelContrastStore', 'Manage.store.LUTStore', 'Manage.store.WorkflowStore', 'Manage.store.WorkflowTreeStore', 'Manage.store.UserAnnotationStore',
              'Manage.store.UserClientStore', 'Manage.store.AllClientStore', 'Manage.store.PublisherStore', 'Manage.store.task.WorkflowClientStore','Manage.store.ServiceStore', 'Manage.store.workflow.TokenStore',
              'Manage.store.workflow.TokenUsers','Manage.store.task.PriorityStore',
              'Manage.store.task.TaskStateStore','Manage.store.task.TaskMonitorStore','Manage.store.task.TaskInspectorStore',
              'Admin.store.MembershipStore','Manage.store.task.TaskStore', 'Manage.store.ProfileTimeUnitStore', 'Manage.store.ProfileLengthUnitStore', 'Manage.store.ProfileTypeStore'],
    
    // When including the controllers in the application, the framework will
    // automatically load the controller and call the init method on it.
    // Inside the init method, one should set up listeners for view and 
    // application events. In larger applications, one might want to load additional controllers
    // at runtime. This can be done by using the getController method.
    controllers: ['Manage.controller.RecordSelection', 'Manage.controller.ImageState', 
                  'Manage.controller.NavigatorState', 'Manage.controller.SummaryTableController',
                  'Manage.controller.WorkflowController','Manage.controller.TaskController',
                  'Manage.controller.MovieController','Manage.controller.ZoomController',
                  'OverlayController','ImageViewController'],
    
    launch: function() {
    	$("#ext-gen1009").mask("Loading...");

//    	$(".x-component x-dd-drag-proxy x-dd-drop-nodrop x-component-default x-layer x-grid-col-dd").mask("Loading... blhabalha");
        Ext.create('Manage.view.Viewport', {});
        // Launch the project chooser first up
        Ext.ComponentQuery.query('navigator')[0].fireEvent('projectlisting');
        Ext.ComponentQuery.query('bookmarks')[0].fireEvent('refreshBookmarks');
        // Logout on exit
        
        Ext.EventManager.on(window, 'unload', function() {
        	window.location='../auth/logout';
        });
    	$("#ext-gen1009").unmask();
//    	$(".x-component x-dd-drag-proxy x-dd-drop-nodrop x-component-default x-layer x-grid-col-dd").unmask();
            
//        // Image overlay
//        /**
//         * Defining some constants
//         */
//        //WIDTH = $('#imageholder').width();
//        //HEIGHT = $('#imageholder').height();
//        WIDTH = 512;
//        HEIGHT = 512;
//        pen_color = "#FF0000";
//        pen_width = 3.0;
//        pen_opacity = 1.0;
//
//        /**
//         * Creating the sketchpad
//         */
//        sketchpad = Raphael.sketchpad('imageeditor', {
//            width: WIDTH,
//            height: HEIGHT,
//            editing: true
//        });
//
//        sketchpad.pen("pencil");
//        sketchpad.pen().color(pen_color).width(pen_width).opacity(pen_opacity);
//        sketchpad.mode(false);
        //image_content = new ImageHandle('.image_overlay .zoom_image', '.image_overlay', '.image_overlay #zoom_out_icon', '.image_overlay #zoom_in_icon', sketchpad);
        //image_content.mode(false);
    }
});
