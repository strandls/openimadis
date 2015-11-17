/*
This file is part of Ext JS 4.2

Copyright (c) 2011-2013 Sencha Inc

Contact:  http://www.sencha.com/contact

Commercial Usage
Licensees holding valid commercial licenses may use this file in accordance with the Commercial
Software License Agreement provided with the Software or, alternatively, in accordance with the
terms contained in a written agreement between you and Sencha.

If you are unsure which license is appropriate for your use, please contact the sales department
at http://www.sencha.com/contact.

Build date: 2013-03-11 22:33:40 (aed16176e68b5e8aa1433452b12805c0ad913836)
*/
Ext.define('Ext.rtl.layout.container.boxOverflow.Scroller', {
    override: 'Ext.layout.container.boxOverflow.Scroller',
    
    scrollLeft: function(internal) {
        var me = this,
            layout = me.layout;
        
        if (layout.direction === 'horizontal' && layout.owner.getHierarchyState().rtl) {
            if (internal) {
                me.scrollBy(me.scrollIncrement, false);
            } else {
                me.scrollRight(true);
            }
        } else {
            me.callParent();
        }
    },

    scrollRight: function(internal) {
        var me = this,
            layout = me.layout;
        
        if (layout.direction === 'horizontal' && layout.owner.getHierarchyState().rtl) {
            if (internal) {
                me.scrollBy(-me.scrollIncrement, false);
            } else {
                me.scrollLeft(true);
            }
        } else {
            me.callParent();
        }
    },
    
    atExtremeBefore: function(internal) {
        var layout = this.layout;
        
        if (!internal && layout.direction === 'horizontal' && layout.owner.getHierarchyState().rtl) {
            return this.atExtremeAfter(true);
        } else {
            return this.callParent();
        }
    },
    
    atExtremeAfter: function(internal) {
        var layout = this.layout;
        
        if (!internal && layout.direction === 'horizontal' && layout.owner.getHierarchyState().rtl) {
            return this.atExtremeBefore(true);
        } else {
            return this.callParent();
        }
    }
});
