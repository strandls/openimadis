/**
    This file is generated and updated by Sencha Cmd. You can edit this file as
    needed for your application, but these edits will have to be merged by
    Sencha Cmd when it performs code generation tasks such as generating new
    models, controllers or views and when running "sencha app upgrade".

    Ideally changes to this file would be limited and most work would be done
    in other places (such as Controllers). If Sencha Cmd cannot merge your
    changes and its generated code, it will produce a "merge conflict" that you
    will need to resolve manually.
*/

// DO NOT DELETE - this directive is required for Sencha Cmd packages to work.
//@require @packageOverrides

Ext.Loader.setConfig({
	enabled: true,
	disableCaching: false
});

Ext.application({
	name: 'Manage',
	appFolder: 'js/app',

	autoCreateViewport: true,

	views: [
		'Viewport'
	],

	models: [
		'Attachment',
		'Bookmark',
		'Channel',
		'ChannelContrast',
		'Client',
		'Comment',
		'Download',
		'ExportFormat',
		'Field',
		'History',
		'HistoryType',
		'ImageMetaData',
		'LUT',
		'MemberProject',
		'Microscope',
		'Overlay',
		'Project',
		'ProjectName',
		'ProjectUser',
		'ProfileType',
		'Profile',
		'ProfileLengthUnit',
		'ProfileTimeUnit',
		'RecordMetaData',
		'RecordThumbnail',
		'SearchResult',
		'Service',
		'Task',
		'Thumbnail',
		'Token',
		'UserAnnotation',
		'Workflow',
		'WorkflowCategory',
		'WebApplication',
		'WebApplicationTree',
		'Suggest',
		'OverlayLocation',
		'Import'
	],

	stores: [ 
		'AllClients',
		'Bookmarks',
		'BookmarksInSelection',
		'BookmarksThumbnails',
		'ChannelContrasts',
		'Channels',
		'Comments',
		'Downloads',
		'DownloadsThumbnails',
		'ExportFormats',
		'Historys',
		'HistoryTypes',
		'ImageMetaDatas',
		'LUTs',
		'LegendAvailableFieldStore',
		'LegendSelectedFieldStore',
		'LegendFieldStore',
		'MemberProjects',
		'Memberships',
		'MicroscopeStore',
		'NavigatorAvailableFields',
		'NavigatorFields',
		'Navigators',
		'NavigatorSelectedFields',
		'NavigatorThumbnails',
		'Overlays',
		'PriorityStore',
		'ProjectAvailableFields',
		'ProjectFieldChoosers',
		'ProjectFields',
		'ProjectNames',
		'Projects',
		'ProjectSelectedFields',
		'ProjectUsers',
		'ProfileStore',
		'ProfileTypeStore',
		'ProfileTimeUnitStore',
		'ProfileLengthUnitStore',
		'RecentProjects',
		'RecordMetaDatas',
		'RecordThumbnails',
		'SearchResults',
		'SearchThumbnails',
		'SelectionRecords',
		'Services',
		'SystemAttachments',
		'TaskInspectors',
		'TaskMonitors',
		'Tasks',
		'TaskStates',
		'TaskThumbnails',
		'Tokens',
		'TokenUsers',
		'UserAnnotations',
		'UserAttachments',
		'UserFields',
		'UserFieldTypes',
		'WorkflowClients',
		'Workflows',
		'WorkflowStore',
		'WorkflowTree',
		'FavouriteWorkflowStore',
		'FavouriteWorkflowTree',
		'AvailableWorkflowStore',
		'AvailableWorkflowTree',
		'SelectedWorkflowStore',
		'SelectedWorkflowTree',
		'admin.Microscopes',
		'Suggest',
		'TagSearch',
		'WebApplications',
		'WebApplicationsTree',
		'OverlayLocation',
		'UserFieldValues',
		'Imports',
		'CompletedImports'
	],

	controllers: [
		'AuthTokens',
		'Bookmarks',
		'Downloads',
		'Headers',
		'ImageControls',
		'ImageView',
		'Movie',
		'Navigator',
		'ProjectSettings',
		'RecordController',
		'Search',
		'Selections',
		'Tasks',
		'Thumbnails',
		'WorkflowController',
		'Zoom',
		'Overlays',
		'admin.Logs',
		'admin.Memberships',
		'admin.MicroscopeProfiles',
		'admin.Microscopes',
		'admin.Projects',
		'admin.Units',
		'admin.UserAnnotations',
		'admin.Users',
		'admin.Utils',
		'admin.Usage',
		'admin.Worker'
	],
	
	launch: function() {
		//HACK to show tooltips
		// BUG in ExtJS 4.2
		delete Ext.tip.Tip.prototype.minWidth;

		$('#loadingMessage').remove();

		Ext.EventManager.on(window, 'unload', function() {
			//window.location='../auth/logout?success=../newApp/login.html';
	    });
	}
});
