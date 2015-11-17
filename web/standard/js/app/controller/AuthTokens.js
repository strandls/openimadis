// Dependencies
Ext.require(['Manage.view.settings.AuthCodeGenerator', 'Manage.view.settings.ViewTokens','Manage.view.settings.TokenList',
             'Manage.view.settings.EditToken']);

/**
 * Controller for auth tokens related views
 */
Ext.define('Manage.controller.AuthTokens', {
    extend: 'Ext.app.Controller',

    refs :[{
        ref : 'tokenList',
        selector : 'tokenList'
    },{
        ref : 'viewTokens',
        selector : 'viewTokens'
    },{
        ref : 'rightTokenPane',
        selector : '#rightTokenPane'
    }],

    init: function() {
        this.control({
            "authcodeGen" : {    
                getAuthCode : this.onGetAuthCode,
                resetRightTokenPanel : this.resetRightTokenPanel
            }, "tokenList" : {
                addToken : this.onGenerateAuthCode,
                editToken : this.onEditToken,
                removeToken : this.onRemoveToken
            }, "editToken" : {
                updateToken : this.onUpdateToken,
                resetRightTokenPanel : this.resetRightTokenPanel
            }
        });
    },
    
    /**
     * Show view to generate auth code
     */
    onGenerateAuthCode : function() 
    {
    	showConsoleLog('AuthTokens', 'onGenerateAuthCode', 'entry');
    	
    	var panel = this.getRightTokenPane();
    	panel.removeAll();
    	panel.doLayout();
    	
    	panel.add([{xtype:'authcodeGen'}]);
    	panel.doLayout();
    },
    
    /**
     * Based on the information entered by user, generate authcode
     */
    onGetAuthCode : function(view, values) {
        var _this = this;
        var params = new Object();
        params["clientID"] = values["clientID"];
        params["expiryTime"] = values["expiryDay"].getTime();
        params["numberOfTokens"] = values["numberOfTokens"];
        delete values["clientID"];
        delete values["expiryDay"];
        delete values["numberOfTokens"];
        var services = new Array();
        for (var key in values) {
            if (values[key])
                services.push(key);
        }
        params["services"] = Ext.encode(services);
        
        var tokens =  params["numberOfTokens"] ;
        	Ext.Ajax.request({
        		method : 'POST',
        		url : '../compute/generateAuthCode',
        		params : params,
        		success : function (result, response){
        			var resp = Ext.decode(result.responseText);
        			var authCode = resp["authCode"];
        			var msg = "Codes are : " ;
        			for (var i = 0 ; i < tokens ; i++){
        				msg += authCode[i] + "<br/>" ; 
        			}
        			Ext.Msg.alert("Authorization Code",msg + "<br/><br/>Please copy and use the Authorization Code immediately. Authorization Code will not be displayed again anywhere. You can always generate another code if required." );
        			_this.refreshTokenStore();
        		},
        		failure : function(result, request) {
        			showErrorMessage(result.responseText, "Failed to generate auth code");
        		}
        	});
    },

    /**
     * Refresh the token store
     */
    refreshTokenStore : function() {
        var table = this.getTokenList();
        if (table && table !== null) {
            table.getStore().load({
                params : {
                    user : table.activeUser
                }
            });
        }
    },

    /**
     * Remove the selected token with confirmation
     */
    onRemoveToken : function(table, tokenModel) {
        var _this = this;
        Ext.Msg.confirm("Delete", "Are you sure you want to delete the selected token", function(id)  {
            if (id === "yes")
                _this.doRemoveToken(table, tokenModel);
        });
    },

    /**
     * Actual remove token
     */
    doRemoveToken : function(table, tokenModel) {
    	var _this=this;
        var tokenID = tokenModel.data["id"];
        Ext.Ajax.request({
            url : '../compute/removeToken',
            method : 'POST',
            params : {
                id : tokenID
            },
            success : function (result, response) {
            	_this.refreshTokenStore();
            },
            failure : function (result, response) {
                showErrorMessage(result.responseText, "Failed to delete token");
            }
        });
    },
    
    resetRightTokenPanel : function() {
    	var panel = this.getRightTokenPane();
    	panel.removeAll();
    	panel.doLayout();
    },
    
    /**
     * Edit chosen token
     */
    onEditToken : function(table, tokenModel) {
    	this.resetRightTokenPanel();
    	
    	var panel = this.getRightTokenPane();
    	
    	panel.add([{
          xtype : 'editToken',
          services : tokenModel.data.services,
          expiry : new Date(tokenModel.data.expiryTime),
          id : tokenModel.data.id
    	}]);
    	panel.doLayout();
    	
    },
    
    /**
     * Post the new values for a token to the server
     */
    onUpdateToken : function(view, values, id) {
        var table = this.getTokenList();
        var params = new Object();
        params["expiryTime"] = values["expiryDay"].getTime();
        delete values["expiryDay"];
        var services = new Array();
        for (var key in values) {
            if (values[key])
                services.push(key);
        }
        params["services"] = Ext.encode(services);
        params["id"] = id;

        var _this = this;
        Ext.Ajax.request({
            method : 'POST',
            url : '../compute/updateToken',
            params : params,
            success : function (result, response){
                Ext.Msg.alert("Success", "Token Updated");
                
                _this.resetRightTokenPanel();
                
                table.getStore().load({
                    params : {
                        user : table.activeUser
                    }
                });
            },
            failure : function(result, request) {
                showErrorMessage(result.responseText, "Failed to update token");
            }
        });
    }
});
