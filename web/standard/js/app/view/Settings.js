/**
 * The window shown when the navigator button is clicked.
 */
Ext.define('Manage.view.Settings',{
		extend : 'Ext.tab.Panel',
		xtype : 'settings',
		alias : 'widget.settings',

		initComponent : function()
		{
			var projectName = this.projectName;
			var xLocation = this.xLocation;
			var yLocation = this.yLocation;
			var config = {
				items : [
						{
							xtype : 'changePassword',
							title : 'Change Password'
						},
						{
							xtype : 'projectUsers',
							title : 'Project Users',
							projectName : projectName
						},
						{
							xtype : 'viewTokens',
							title : 'Manage Auth Tokens'
						},
						{
							xtype : 'panel',
							title : 'Project Fields',

							layout : {
								type : 'vbox',
								align : 'stretch'
							},

							items : [{
										xtype:'label',
										id:'projectfieldchooser',
										margin : 5,
										style: 'font-weight:bold;'
									},
									{
										xtype : 'text',										
										html :  'The selected Fields will be used to sort Thumbnails and will '
												+ 'be the columns in the Summary Table for this project.',
										border : false,
										margin : 5,
										height : 30
									},
									{
										xtype : 'projectFieldChooser',
										flex : 1
									} ]
						},
						{
							xtype : 'panel',
							title : 'Legend Fields',

							layout : {
								type : 'vbox',
								align : 'stretch'
							},

							items : [
									{
										xtype : 'panel',
										border : false,
										margin : 5,

										height : 60,

										items : [
												{
													xtype : 'radiogroup',
													itemId : 'yLocationRadio',
													fieldLabel : 'Top/Bottom',
													width : 300,
													items : [
															{
																boxLabel : 'Top',
																name : 'tb',
																inputValue : 'top',
																checked : true
															},
															{
																boxLabel : 'Bottom',
																name : 'tb',
																inputValue : 'bottom'
															} ]
												},
												{
													xtype : 'radiogroup',
													fieldLabel : 'Left/Right',
													itemId : 'xLocationRadio',
													width : 300,
													items : [
															{
																boxLabel : 'Left',
																name : 'lr',
																inputValue : 'left',
																checked : true
															},
															{
																boxLabel : 'Right',
																name : 'lr',
																inputValue : 'right'
															} ]
												} ]
									},
									{
										xtype : 'legendsFieldChooser',
										flex : 1
									} ]
						}]
			};
			Ext.apply(this, Ext.apply(this.initialConfig, config));
			this.callParent();
		}
});