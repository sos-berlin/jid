package com.sos.jid.dialog.classes;

import java.util.Locale;
import java.util.prefs.Preferences;

import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.sos.dashboard.globals.DashBoardConstants;
import com.sos.dialog.classes.SOSIntegerInputDialog;
import com.sos.localization.Messages;

public class SOSMenuLimitItem extends MenuItem {

    private static final String conEnvVarSOS_LOCALE = "SOS_LOCALE";
    private Preferences prefs;
    private String prefNode = "MenuItemLimit";
    protected Messages messages = null;

    /** @wbp.parser.constructor */
    public SOSMenuLimitItem(Menu parent, int style) {
        super(parent, style);
        setMessageResource(DashBoardConstants.conPropertiesFileName);
        createItem(parent);
        prefs = Preferences.userNodeForPackage(this.getClass());
        prefNode = "MenuLimitItem";
    }

    protected void setMessageResource(final String pstrResourceBundleName) {
        String strSOSLocale = System.getenv(conEnvVarSOS_LOCALE);
        if (strSOSLocale == null) {
            messages = new Messages(pstrResourceBundleName, Locale.getDefault());
        } else {
            messages = new Messages(pstrResourceBundleName, new Locale(strSOSLocale));
        }
    }

    public SOSMenuLimitItem(Menu parent, int style, Preferences prefs_, String prefNode_) {
        super(parent, style);
        setMessageResource(DashBoardConstants.conPropertiesFileName);
        prefs = prefs_;
        prefNode = prefNode_;
        createItem(parent);
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    private void createItem(final Menu parent) {

        this.setText(messages.getLabel(DashBoardConstants.conSOSDashB_setLimit));
        this.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                SOSIntegerInputDialog dialog = new SOSIntegerInputDialog(parent.getShell());
                Integer limit = DashBoardConstants.conSettingLIMITDefault;
                if (prefs.node(prefNode) != null) {
                    limit = Integer.parseInt(prefs.node(prefNode).get(DashBoardConstants.conSettingLIMIT, String.valueOf(DashBoardConstants.conSettingLIMITDefault)));
                }
                dialog.setValue(limit);
                limit = dialog.open();
                if (limit != null) {
                    prefs.node(prefNode).put(DashBoardConstants.conSettingLIMIT, String.valueOf(limit));
                }
            }

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
    }

}
