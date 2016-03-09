package com.sos.jid.dialog.classes;

import com.sos.JSHelper.Basics.VersionInfo;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.options.Options;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.MainWindow;

public class SOSTabJOE extends CTabItem {

    public SOSTabJOE(String caption, CTabFolder parent, Composite joeComposite) {
        super(parent, SWT.NONE);
        setText(caption);

        MainWindow window = new MainWindow();

        window.createSShell(joeComposite, joeComposite);
        String strT = VersionInfo.JAR_VERSION;
        MainWindow.container.setTitleText(strT);
        joeComposite.getShell().setText(strT);
        ErrorLog.setSShell(joeComposite.getShell());
        Options.conJOEGreeting = strT;
        joeComposite.getShell().setData(joeComposite.getShell().getText());
        window.OpenLastFolder();
        Editor.objMainWindow = window;

    }
}
