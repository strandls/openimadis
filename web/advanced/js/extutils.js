function getAndSetText(oldText, success) {
    Ext.create('Ext.window.Window', {
        title: 'Edit',
        layout: 'fit',
        items: {  // Let's put an empty grid in just to illustrate fit layout
            xtype: 'textfield',
            fieldLabel: 'Enter Text',
            name: 'text',
            allowBlank: false
        },
        buttons: [
            {
                text: 'Save',
                handler : function() {
                    console.log("test");
                    var w = this.up('window');
                    var newValue = w.items.items[0].value;
                    success(newValue);
                    w.close();
                }    
            }
        ]
    }).show();

}
