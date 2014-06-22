package com.sos.jid.dialog.classes;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.MainWindow;

import com.sos.JSHelper.Basics.JSVersionInfo;
import com.sos.joe.globals.options.Options;

public class SOSTabJOE extends CTabItem {
 
    public SOSTabJOE(String caption, CTabFolder parent, Composite joeComposite) {
        super(parent, SWT.NONE);
        setText(caption);

        MainWindow window = new MainWindow();
        
        window.createSShell(joeComposite,joeComposite);
        String strT =  JSVersionInfo.conVersionNumber;
        MainWindow.container.setTitleText(strT);
        joeComposite.getShell().setText(strT);
        Options.conJOEGreeting = strT;
        joeComposite.getShell().setData(joeComposite.getShell().getText());
        window.OpenLastFolder();
        Editor.objMainWindow = window;
         
     }
}

