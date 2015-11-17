/**
 * Collection of some utility methods used across the application
 */
/**
 * Check the response to see if there is a message and show the same
 * if present, else show the message passed
 */
function showErrorMessageRedirect(responseText, defaultMessage) {
    var response = Ext.decode(responseText);
    var callback = function(id) {
        if (id === "yes") {
            window.location = "login.html";
        }
    };
    if (response !== null && response.message && response.message !== null) {
        Ext.Msg.confirm("Failed", response.message, callback);
    } else {
        if (!defaultMessage || defaultMessage === null)
            defaultMessage = "Failed to communicate to server. Login to try again";
        Ext.Msg.alert("Failed", defaultMessage);
    }
}

/**
* wrapper function on console.log
*/
function showConsoleLog(sourceFile, sourceFunction, message) {
    console.log("[sourceFile="+sourceFile+"] [sourceMethod="+sourceFunction+"] log="+message);
}

/**
 * Show a simple message
 */
function showErrorMessage(responseText, defaultMessage) {
    var response = Ext.decode(responseText);
    if (response !== null && response.message && response.message !== null) {
        Ext.Msg.alert("Failed", response.message);
    } else {
        if (!defaultMessage || defaultMessage === null)
            defaultMessage = "Failed to communicate to server. Login to try again";
        Ext.Msg.alert("Failed", defaultMessage);
    }
}

/*Syncronous server call*/
function syncAJAXcallUrl(url) {
	if (window.XMLHttpRequest) {
		AJAX = new XMLHttpRequest();
	} else {
		AJAX = new ActiveXObject("Microsoft.XMLHTTP");
	}
	if (AJAX) {
		AJAX.open("GET", url, false);
		AJAX.send(null);
		return AJAX.responseText;
	} else {
		return false;
	}
}

/*Syncronous server call with params*/

function syncAJAXcall(url,params) {
	var newurl=url+"?";
	params.random=Math.random();
	for(var key in params){
		newurl=newurl+key+"="+params[key]+"&";
	}
	return syncAJAXcallUrl(newurl);
}

