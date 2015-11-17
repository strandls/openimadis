var login = Ext.create('Ext.form.Panel', {
					labelWidth : 80,
					bodyStyle : "background-image:url('images/loginbackground.png')",
					layout : 'absolute',
					id : 'loginForm',
					url : '../auth/login',
					standardSubmit : true,
					items : [ {
						xtype : 'textfield',
						name : 'loginUsername',
						blankText : 'Enter your username',
						fieldLabel : 'Username',
						labelStyle : 'color: #FFF; font-size: 14px;',
						labelSeparator : '',
						labelAlign : 'top',
						allowBlank : false,
						x : 50,
						y : 200,
						style : 'font-size: 20px',
						autoHeight : true
					}, {
						xtype : 'textfield',
						name : 'loginPassword',
						blankText : 'Enter your password',
						fieldLabel : 'Password',
						inputType : 'password',
						labelStyle : 'color: #FFF; font-size: 14px;',
						labelSeparator : '',
						labelAlign : 'top',
						allowBlank : false,
						x : 50,
						y : 250,
						style : 'font-size: 20px',
						autoHeight : true
					}, {
						xtype : 'button',
						name : 'login',
						text : 'Log In',
						width : 80,
						x : 150,
						y : 370,
						formBind : true,
						handler : function() {
							this.up().doLogin();
						}
					} ],
					doLogin : function() {
						var form = this.getForm();
						if (form.isValid()) {
							Ext.Ajax.request({
								method : 'POST',
								url : form.url,
								params : {
									loginUsername : form.findField('loginUsername').getValue(),
									loginPassword : form.findField('loginPassword').getValue()
								},
								success : function (result, response) {
									if (result.responseText == "success") {
										window.location = location.href.substring(0, location.href.lastIndexOf("/"));
									} else {
										form.findField('loginPassword').setValue("");
										Ext.Msg.alert('Failed', result.responseText);
									}
								},
								failure : function (result, response) {
									Ext.Msg.alert('Failed', "Login failed. Please check your username and password");
								}
							});
						}
					},
					listeners : {
						afterRender : function(thisForm, options) {
							this.keyNav = Ext.create('Ext.util.KeyNav',
									this.el, {
										enter : this.doLogin,
										scope : this
									});
						}
					}
				});

var topLogos = Ext.create('Ext.toolbar.Toolbar', {
	height : 50,
	width : 500,
	layout : {
		type : 'hbox',
		align : 'stretch'
	},
	style : {
		background : 'white'
	},
	items : [ {
		xtype : 'image',
		src : 'images/iManageLogo.png',
		width : 218
	} ]
});

var bottomLogos = Ext.create('Ext.toolbar.Toolbar', {
	height : 50,
	width : 500,
	layout : {
		type : 'hbox',
		align : 'stretch'
	},
	style : {
		background : 'white'
	},
	items : [ {
		xtype : 'image',
		src : 'images/strand_logo.jpg',
		width : 130
	}, '->', {
		xtype : 'image',
		src : 'images/institut_curie.png',
		width : 78
	} ]
});

var win = Ext.create('Ext.window.Window', {
	layout : 'fit',
	closable : false,
	draggable : false,
	resizable : false,
	width : 509,
	height : 600,
	plain : true,
	border : false,
	tbar : [ topLogos ],
	bbar : [ bottomLogos ],
	items : [ login ]
});

Ext.onReady(function() {
	win.show();
	if (document.URL.split("?")[1] == "unauthorized") {
		Ext.Msg.alert('Login', 'Kindly login first before accessing the resource.');
	}
});
