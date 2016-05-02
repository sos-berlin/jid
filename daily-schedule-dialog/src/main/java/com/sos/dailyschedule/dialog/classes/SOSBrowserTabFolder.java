package com.sos.dailyschedule.dialog.classes;

import java.util.prefs.Preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.sos.dashboard.globals.DashBoardConstants;
import com.sos.dialog.classes.SOSUrl;
import com.sos.jid.dialog.classes.SosDialogGetHostPort;
import com.sos.jid.dialog.classes.SosTabJOC;
import com.sos.localization.Messages;

public class SOSBrowserTabFolder {

    private String listOfUrls = "";
    private String listOfUrlTitels = "";
    private String prefKey;
    private String prefKeyTitles;
    private Preferences prefs;
    private CTabFolder tabFolder;
    private CTabFolder parent;
    private String tabName;
    private SOSUrl defaultUrl = null;
    private Messages messages;
    private String openMenueItem = null;
    private CTabItem tbTmUrls;

    public SOSBrowserTabFolder(CTabFolder parent_, String tabName_, Messages messages_) {
        parent = parent_;
        tabName = tabName_;
        messages = messages_;
    }

    public void openUrls() {
        SOSUrl url = null;
        listOfUrls = prefs.node(DashBoardConstants.SOS_DASHBOARD).get(prefKey, "");
        listOfUrlTitels = prefs.node(DashBoardConstants.SOS_DASHBOARD).get(prefKeyTitles, "");
        url = defaultUrl;
        Composite urlComposite = new Composite(parent, SWT.NONE);
        urlComposite.setLayout(new GridLayout());
        if (tbTmUrls == null) {
            tbTmUrls = new CTabItem(parent, SWT.NONE);
            tbTmUrls.setText(tabName);
        }
        tbTmUrls.setControl(urlComposite);
        tabFolder = new CTabFolder(urlComposite, SWT.NONE);
        if (openMenueItem != null) {
            createContextMenuBrowserTabfolder(tabFolder);
            createContextMenuBrowserTabfolder(parent);
        }
        tabFolder.setTabPosition(SWT.BOTTOM);
        tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        int startIndex = 0;
        if (defaultUrl != null) {
            startIndex = 1;
            new SosTabJOC(tabFolder, url);
        }
        if (!"".equals(listOfUrls)) {
            String[] urlList = listOfUrls.split(",");
            String[] urlListTitle = listOfUrlTitels.split(",");
            for (int i = startIndex; i < urlList.length; i++) {
                if (listOfUrlTitels.isEmpty() || urlListTitle.length < urlList.length) {
                    this.openUrl(new SOSUrl(urlList[i]));
                } else {
                    this.openUrl(new SOSUrl(urlListTitle[i], urlList[i]));
                }
            }
        }
        tabFolder.setSelection(0);
    }

    private void openUrl(SOSUrl url) {
        SosTabJOC tbtmJoc = new SosTabJOC(tabFolder, url);
        tabFolder.setSelection(tbtmJoc);
        saveBrowserTabs();
    }

    public void addUrl(SOSUrl url) {
        if ("".equals(listOfUrls)) {
            listOfUrls = url.getUrlValue();
        } else {
            listOfUrls = listOfUrls + "," + url.getUrlValue();
        }
        prefs.node(DashBoardConstants.SOS_DASHBOARD).put(prefKey, listOfUrls);
        if ("".equals(listOfUrlTitels)) {
            listOfUrlTitels = url.getTitle();
        } else {
            listOfUrlTitels = listOfUrlTitels + "," + url.getTitle();
        }
        prefs.node(DashBoardConstants.SOS_DASHBOARD).put(prefKeyTitles, listOfUrlTitels);
    }

    private void saveBrowserTabs() {
        listOfUrls = "";
        listOfUrlTitels = "";
        CTabItem[] tabs = tabFolder.getItems();
        for (CTabItem tab : tabs) {
            SOSUrl url = (SOSUrl) tab.getData();
            if (url != null) {
                if ("".equals(listOfUrls)) {
                    listOfUrls = url.getUrlValue();
                } else {
                    listOfUrls = listOfUrls + "," + url.getUrlValue();
                }
                if ("".equals(listOfUrlTitels)) {
                    listOfUrlTitels = url.getTitle();
                } else {
                    listOfUrlTitels = listOfUrlTitels + "," + url.getTitle();
                }
            }
        }
        prefs.node(DashBoardConstants.SOS_DASHBOARD).put(prefKey, listOfUrls);
        prefs.node(DashBoardConstants.SOS_DASHBOARD).put(prefKeyTitles, listOfUrlTitels);
    }

    public void closeAllBrowserTabs() {
        listOfUrls = "";
        CTabItem[] tabs = tabFolder.getItems();
        for (CTabItem tab : tabs) {
            if (tab != null) {
                tab.dispose();
            }
        }
        saveBrowserTabs();
        prefs.node(DashBoardConstants.SOS_DASHBOARD).put(prefKey, listOfUrls);
    }

    private void createContextMenuBrowserTabfolder(final CTabFolder cParent) {
        Menu contentMenu = new Menu(cParent);
        cParent.setMenu(contentMenu);
        MenuItem addItem = new MenuItem(contentMenu, SWT.PUSH);
        addItem.setText(openMenueItem);
        addItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            @Override
            public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
                SosDialogGetHostPort s = new SosDialogGetHostPort(cParent.getShell());
                if (!s.cancel()) {
                    openUrl(new SOSUrl(s.getTitle(), s.getUrl()));
                    for (int i = 0; i < parent.getTabList().length; i++) {
                        try {
                            if (parent.getItem(i).getText().equals(tabName)) {
                                parent.setSelection(i);
                            }
                        } catch (Exception ee) {
                        }
                    }
                }
            }

            @Override
            public void widgetDefaultSelected(final org.eclipse.swt.events.SelectionEvent e) {
            }
        });
        MenuItem closeItem = new MenuItem(contentMenu, SWT.PUSH);
        closeItem.setText(messages.getLabel(DashBoardConstants.conSOSDashB_close));
        closeItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            @Override
            public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
                SosTabJOC tbtmJoc = (SosTabJOC) tabFolder.getSelection();
                if (tbtmJoc != null && tabFolder.getItemCount() > 1) {
                    tbtmJoc.dispose();
                    saveBrowserTabs();
                }
            }

            @Override
            public void widgetDefaultSelected(final org.eclipse.swt.events.SelectionEvent e) {
            }
        });
    }

    public void setPrefKey(String prefKey) {
        this.prefKey = prefKey;
        this.prefKeyTitles = prefKey + "_titles";
    }

    public void setDefaultUrl(SOSUrl defaultUrl) {
        this.defaultUrl = defaultUrl;
    }

    public void setPrefs(Preferences prefs) {
        this.prefs = prefs;
    }

    public void setOpenMenueItem(String openMenueItem) {
        this.openMenueItem = openMenueItem;
    }

}