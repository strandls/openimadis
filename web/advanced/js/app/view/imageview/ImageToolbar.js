Ext.define('Manage.view.imageview.ImageToolbar', {
	extend:'Ext.toolbar.Toolbar',
	xtype:'imagetoolbar',
	alias:'widget.imagetoolbar',
	initComponent:function() {
        this.menu = this.createMenu();
        var page =this;
	  var config = {
		  layout: {
                overflowHandler: 'Menu'
            },
            items: [
            // XXX: TODO change icon with toggle state	
            {
            	id: 'button_3d',
                iconCls: 'off3d',
                tooltip : '3D View',
                handler : function(button, e) {
                    button.up().fireEvent('show3D', button);
                }
            }, {
            	id: 'button_zproject',
                iconCls: 'zproject-off',
		        enableToggle: 'true',
                tooltip : 'Z Projection',
                toggleHandler : function(btn, state) {
                    if (state) {
                        btn.setIconCls('zproject-on'); 
                    } else {
                        btn.setIconCls('zproject-off'); 
                    }
                }
            },{
            	id: 'button_colorimage',
                iconCls: 'colorimage-off',
		        enableToggle: 'true',
                tooltip : 'Grey Scale',
                toggleHandler : function(btn, state) {
                    if (state) {
                        btn.setIconCls('colorimage-on'); 
                    } else {
                        btn.setIconCls('colorimage-off'); 
                    }
                }
            },{
            	id: 'button_allchannel',
                iconCls: 'allchannel-off',
		        enableToggle: 'true',
                tooltip : 'All Channels',
                toggleHandler : function(btn, state) {
                    if (state) {
                        btn.setIconCls('allchannel-on'); 
                    } else {
                        btn.setIconCls('allchannel-off'); 
                    }
                }
            }, /*{
                id : 'button_rulers',
                iconCls : 'rulers-off',
                enableToggle : 'true',
                tooltip : 'Show Scale',
                toggleHandler : function(btn, state) {
                    if (state) {
                        btn.setIconCls('rulers-on'); 
                    } else {
                        btn.setIconCls('rulers-off'); 
                    }
                }
            },*/{
                id : 'button_panzoom',
                iconCls : 'panzoom-on',
                enableToggle : 'true',
                tooltip : 'Pan & Zoom'
            },{
                id : 'button_fullres',
                iconCls : 'fullres-off',
                enableToggle : 'true',
                tooltip : 'Show Full Resolution images',
                toggleHandler : function(btn, state) {
                    if (state) {
                        btn.setIconCls('fullres-on'); 
                    } else {
                        btn.setIconCls('fullres-off'); 
                    }
                }
            }, {
					id : 'button_scalebar',
	                iconCls : 'rulers-off',
	                enableToggle : 'true',
	                tooltip : 'Turn on Scalebar',
	                toggleHandler : function(btn, state) {
	                    if (state) {
	                        btn.setIconCls('rulers-on'); 
	                    } else {
	                        btn.setIconCls('rulers-off'); 
	                    }
	                }
				},
            '->', {
                id : 'button_bookmark',
                iconCls : 'bookmark-off',
                enableToggle : 'true',
                tooltip : 'Bookmark this record',
                toggleHandler : function(btn, state) {
                    if (state) {
                        btn.setIconCls('bookmark-on'); 
                    } else {
                        btn.setIconCls('bookmark-off'); 
                    }
                }
            }, {
                id : 'button_thumbnail',
                iconCls : 'thumbnail',
                tooltip : 'Set image as thumbnail'
            }, {
                id : 'button_imagedownload',
                iconCls : 'imageDownload',
                tooltip : 'Screenshot'
            }]
	  };
	  
	  Ext.apply(this, Ext.apply(this.initialConfig, config));
	  this.callParent();

	},
   createMenu:function(){
	   var page =this;
	   var menu = Ext.create('Ext.menu.Menu', {
	        id: 'mainMenu',
	        style: {
	            overflow: 'visible'     // For the Combo popup
	        },
	        items: [
	            {
	                text: 'Frame',
	                checked:true,
	                checkHandler: page.onItemCheck(),
	                group:'type',
	                menu: {        // <-- submenu by nested config object
	                    items: [
	                        
	                        '<b class="menu-title">Choose Frame Rate </b>',
	                        {
	                            text: '1 FPS',
	                            checked: true,
	                            group: 'frame',
	                            checkHandler: page.onItemCheck()
	                        }, {
	                            text: '2 FPS',
	                            checked: false,
	                            group: 'frame',
	                            checkHandler: page.onItemCheck()
	                        }, {
	                            text: '3 FPS',
	                            checked: false,
	                            group: 'frame',
	                            checkHandler: page.onItemCheck()
	                        }, {
	                            text: '4 FPS',
	                            checked: false,
	                            group: 'frame',
	                            checkHandler:page.onItemCheck()
	                        }
	                    ]
	                }
	           },
	           {
	           text: 'Slice',
	           checked:false,
	           group:'type',
	           checkHandler: page.onItemCheck(),
               menu: {        // <-- submenu by nested config object
                   items: [
                       
                       '<b class="menu-title">Choose Slic Rate </b>',
                       {
                           text: '1 SPS',
                           checked: true,
                           group: 'slice',
                           checkHandler: page.onItemCheck()
                       }, {
                           text: '2 SPS',
                           checked: false,
                           group: 'slice',
                           checkHandler: page.onItemCheck()
                       }, {
                           text: '3 SPS',
                           checked: false,
                           group: 'slice',
                           checkHandler: page.onItemCheck()
                       }, {
                           text: '4 SPS',
                           checked: false,
                           group: 'slice',
                           checkHandler:page.onItemCheck()
                       }
                   ]
               }
          }
	           
	           
	        ]
	    });
	   return menu;
	   
   },
   
  onItemCheck:function(item, checked){
      // Ext.example.msg('Item Check', 'You {1} the "{0}" menu item.', item.text, checked ? 'checked' : 'unchecked');
   }
});
