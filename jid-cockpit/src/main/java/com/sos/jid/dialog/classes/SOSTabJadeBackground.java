package com.sos.jid.dialog.classes;
import org.eclipse.swt.custom.CTabFolder;



import com.sos.dashboard.globals.SOSDashboardOptions;
import com.sos.dialog.classes.SOSUrl;

   class SOSTabJadeBackground extends SosTabJOC{
 
    public SOSTabJadeBackground(final SOSDashboardOptions objOptions_,String caption, CTabFolder parent)  {
        super(parent,new SOSUrl(caption,String.format("http://sp:8282/jade-background-service-frontend/?security_server=%s&session_id=%s",objOptions_.securityServer.Value(),objOptions_.sessionId.Value())));
    }
}
