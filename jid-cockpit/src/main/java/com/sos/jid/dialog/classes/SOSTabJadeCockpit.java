package com.sos.jid.dialog.classes;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Display;

   class SOSTabJadeCockpit extends CTabItem {
 
    public SOSTabJadeCockpit(String caption, CTabFolder parent) {
        super(parent, SWT.NONE);
        setText(caption);
             
     }
}
