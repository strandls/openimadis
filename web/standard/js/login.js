Ext.require([
    'Ext.tip.*',
    'Ext.Button',
    'Ext.window.MessageBox'
]);
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
					},{
						xtype : 'button',
						id : 'guestButton',
						text : 'Guest User',
						//handleMouseEvents: true,
						width : 80,
						x : 70,
						y : 150,
						disabled: false,
            			tooltip: 'Access for public users',
						//formBind : false,                		
						handler : function() {
							this.up().LoginGuest();
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
					},LoginGuest : function() {	
					var form = this.getForm();
							Ext.Ajax.request({
								method : 'POST',
								url : form.url,
								params : {
									loginUsername : "public",
									loginPassword : "Welcome!1"
								},
								success : function (result, response) {
									if (result.responseText == "success") {
										window.location = location.href.substring(0, location.href.lastIndexOf("/"));
									} 
								}
								
							});
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

Ext.QuickTips.init();


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
		src : 'images/openImadis.png',
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
	},'->',
	{
		xtype : 'image',
		src : 'images/fbi_logo.jpg',
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

	Ext.Ajax.request({
            method: 'GET',
            url: 'message.txt',
            success: function (response) {
		var bd = Ext.getBody();
		bd.createChild({tag: 'div', html: response.responseText, style: 'font-weight: bold; background-color:#fff; position:fixed; top:0; width: 100%; padding: 5px; background-color: #f9edbe; border: 1px solid #f0c36d; z-index:999999;'});
            }
        });
});
