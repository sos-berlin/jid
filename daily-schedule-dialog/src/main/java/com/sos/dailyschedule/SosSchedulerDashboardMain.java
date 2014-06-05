package com.sos.dailyschedule;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;

 



import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import sos.scheduler.editor.app.ErrorLog;
import sos.util.SOSClassUtil;

import com.sos.JSHelper.Logging.Log4JHelper;
import com.sos.auth.SOSJaxbSubject;
import com.sos.auth.rest.SOSWebserviceAuthenticationRecord;
import com.sos.auth.rest.client.SOSRestShiroClient;
import com.sos.auth.rest.permission.model.SOSPermissionShiro;
import com.sos.dailyschedule.dialog.DashboardShowDialog;
import com.sos.dashboard.globals.DashBoardConstants;
import com.sos.dashboard.globals.SOSDashboardOptions;
import com.sos.dialog.auth.SOSLoginDialog;
import com.sos.i18n.I18NBase;
import com.sos.i18n.annotation.I18NResourceBundle;
import com.sos.scheduler.db.SchedulerInstancesDBItem;
import com.sos.scheduler.db.SchedulerInstancesDBLayer;

/**
 * \class 		SosSchedulerDashboardMain - Main-Class for "Transfer files by FTP/SFTP and execute commands by SSH"
 *
 * \brief MainClass to launch sosftp as an executable command-line program
 *
 * This Class SosSchedulerDashboardMain is the worker-class.
 *
 *
 */
@I18NResourceBundle(baseName = "SOSSchedulerDashboardMain", defaultLocale = "en")
public class SosSchedulerDashboardMain extends I18NBase {
    private static final String COMMAND_PERMISSION = "/jobscheduler/rest/sosPermission/permissions?user=%s&pwd=%s";
    private static final String SOS_PRODUCTS_JID_EXECUTE = "sos:products:jid:execute";
    private static final String COMMAND_IS_ENABLED = "/jobscheduler/engine/plugin/security/is_enabled";
    private final static String   conClassName         = "SosSchedulerDashboardMain";
    public static final String    conSVNVersion        = "$Id: SosSchedulerDashboardMain.java 16415 2012-02-01 17:21:40Z ur $";
    private static Logger         logger               = Logger.getLogger(SosSchedulerDashboardMain.class);
    @SuppressWarnings("unused")
    private static Log4JHelper    objLogger            = null;
    protected SOSDashboardOptions objOptions           = null;
    public static final String    SOSDashBoard_Intro   = "SosSchedulerDashboardMain.SOSDB-Intro";
    public static final String    SOSDashboard_E_0001  = "SosSchedulerDashboardMain.SOSDX_E_0001";
    public static final String    SOS_EXIT_WO_ERRORS   = "SosSchedulerDashboardMain.SOS_EXIT_WO_ERRORS";
    public static final String    SOS_EXIT_CODE_RAISED = "SosSchedulerDashboardMain.SOS_EXIT_CODE_RAISED";
    private boolean isAuthenticated;
    private SOSJaxbSubject currentUser=null;
    
    /**
     * 
     * \brief main
     * 
     * \details
     *
     * \return void
     *
     * @param pstrArgs
     * @throws Exception
     */
    public final static void main(final String[] pstrArgs) {
        @SuppressWarnings("unused")
        final String conMethodName = conClassName + "::Main"; //$NON-NLS-1$
        SosSchedulerDashboardMain objEngine = new SosSchedulerDashboardMain();
        objEngine.execute(pstrArgs);
    }

    protected SosSchedulerDashboardMain() {
        super(DashBoardConstants.conPropertiesFileName);
        /*	try {
        	 	AddAnnotation.addPersonneNameAnnotationToMethod("com.sos.scheduler.history.db.SchedulerHistoryLogDBItem", "getLog");
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        	*/
    }

    private boolean doLogin() throws MalformedURLException {
        
        
        SOSLoginDialog sosLoginDialog = new SOSLoginDialog(new Shell(), 0);
        
        do {
        
        SOSRestShiroClient sosRestShiroClient = new SOSRestShiroClient();
        sosLoginDialog.open();
        if (sosLoginDialog.getUser() != null){
    
            SOSWebserviceAuthenticationRecord sosWebserviceAuthenticationRecord = new SOSWebserviceAuthenticationRecord();
            sosWebserviceAuthenticationRecord.setUser(sosLoginDialog.getUser());
            sosWebserviceAuthenticationRecord.setPassword(sosLoginDialog.getPassword());
            sosWebserviceAuthenticationRecord.setResource(objOptions.securityServer.Value() + COMMAND_PERMISSION);
            sosWebserviceAuthenticationRecord.setSessionId("");
           
            SOSPermissionShiro sosPermissionShiro = sosRestShiroClient.getPermissions(sosWebserviceAuthenticationRecord);
            currentUser = new SOSJaxbSubject(sosPermissionShiro);
            }}
        while (!sosLoginDialog.isCancel() && (!(currentUser==null || currentUser.isAuthenticated())));              
       
        return (currentUser != null && currentUser.isAuthenticated());
         
    }
    
   
    
    private boolean getSecurityEnabled() {
        boolean enabled = false;
       // SOSRestShiroClient sosRestShiroClient = new SOSRestShiroClient();
        
        SchedulerInstancesDBLayer schedulerInstancesDBLayer = new SchedulerInstancesDBLayer(objOptions.hibernateConfigurationFile.JSFile());
        schedulerInstancesDBLayer.getFilter().setSosCommandWebservice(true);
        List<SchedulerInstancesDBItem> l = schedulerInstancesDBLayer.getSchedulerInstancesList();
        Iterator <SchedulerInstancesDBItem> schedulerInstances = l.iterator();
        while (!enabled && schedulerInstances.hasNext()) {
            SchedulerInstancesDBItem schedulerInstancesDBItem = (SchedulerInstancesDBItem) schedulerInstances.next();
            String webServicAddress = String.format("http://%s:%s",schedulerInstancesDBItem.getHostname(),schedulerInstancesDBItem.getJettyHttpPort());
            try {
//                enabled = sosRestShiroClient.isEnabled(new URL(webServicAddress + COMMAND_IS_ENABLED));
                enabled = schedulerInstancesDBItem.getisSosCommandWebservice();
                if (enabled) {
                    objOptions.securityServer.Value(webServicAddress);
                }
            }catch (Exception e) {
                enabled = true;
            }
        }            

        
        return enabled;
    }
    
    
    private void execute(final String[] pstrArgs) {
        final String conMethodName = conClassName + "::Execute";
        try {
            objLogger = new Log4JHelper("./log4j.properties"); //$NON-NLS-1$
            logger = Logger.getRootLogger();
            logger.info(Messages.getMsg(SOSDashBoard_Intro)); // $NON-NLS-1$
            logger.info(conSVNVersion);
            Shell shell = new Shell();
            
            objOptions = new SOSDashboardOptions();
            objOptions.CommandLineArgs(pstrArgs);
            
            boolean securityEnabled = getSecurityEnabled();
                  
            
//            if (/*false &&*/ sosRestShiroClient.isEnabled(new URL(objOptions.securityServer.Value() + COMMAND_IS_ENABLED))) {
            if (securityEnabled) {
                 
              
                isAuthenticated = doLogin() &&  (currentUser.hasRole("jid") || currentUser.isPermitted(SOS_PRODUCTS_JID_EXECUTE)) ;
               
//              objOptions.enableJobnet.value(currentUser.hasRole("jobnet") ||  currentUser.isPermitted("sos:products:dashboard:jobnet"));
            }else {
                isAuthenticated = true;
                if (pstrArgs.length > 0) {
                    logger.debug("pstrArgs = " + pstrArgs[0].toString());
                    logger.debug("user-dir = " + System.getProperty("user.dir"));
                    objOptions.CommandLineArgs(pstrArgs);
                }
                else {
                  /* objOptions.enableJOC.value(true);
                    objOptions.enableJOE.value(true);
                    objOptions.enableEvents.value(true);
                    objOptions.enableJobnet.value(false);

                    objOptions.enableJobStart.value(true);
                    */
                }
            }

          

            if (isAuthenticated) {
                if (currentUser != null) {
                    objOptions.enableJOC.value(currentUser.hasRole("joc") || currentUser.isPermitted("sos:products:jid:joctab:show"));
                    objOptions.enableJOE.value(currentUser.hasRole("joe") || currentUser.isPermitted("sos:products:jid:joetab:show"));
                    objOptions.enableEvents.value(currentUser.hasRole("events") || currentUser.isPermitted("sos:products:jid:eventtab:show"));
                    //objOptions.enableJobnet.value(currentUser.hasRole("jobnet") || currentUser.isPermitted("sos:products:jid:jobnettab:show"));
                    objOptions.enableJobStart.value(currentUser.isPermitted("sos:products:jid:jobstart"));
                }

                try {

                    Composite composite = new Composite(shell, SWT.NONE);

                    /*
                    if (pstrArgs.length > 0) {
                    	logger.debug("pstrArgs = " + pstrArgs[0].toString());
                    	logger.debug("user-dir = " + System.getProperty("user.dir"));
                    	objOptions.CommandLineArgs(pstrArgs);
                    }
                    else {
                    	objOptions.enableJOC.value(true);
                    	objOptions.enableJOE.value(true);
                    	objOptions.enableEvents.value(true);
                    	objOptions.enableJobnet.value(false);

                    	objOptions.enableJobStart.value(true);
                    }
                    */
                    objOptions.CheckMandatory();
                    logger.debug(objOptions.toString());
                    DailyScheduleDataProvider dataProvider = new DailyScheduleDataProvider(objOptions.hibernateConfigurationFile.JSFile());
                    DashboardShowDialog window = new DashboardShowDialog(composite);

                    try {
                        window.setDataProvider(dataProvider);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION);
                        messageBox.setMessage(Messages.getLabel(DashBoardConstants.conSOSDashB_CouldNotConnect) + "\nMessage: " + e.getMessage());
                        int rc = messageBox.open();

                        objOptions.enableJobnet.value(false);

                    }
                    window.setObjOptions(objOptions);
                    window.open();
                    logger.debug("...............quit");
                }
                catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }

        catch (Exception e) {
            try {
                logger.fatal("sudden death", e);
                new ErrorLog("JID", "error in " + SOSClassUtil.getMethodName() + "cause: " + e.toString(), e);
                int intExitCode = 99;
                logger.error(String.format(getMsg(SOS_EXIT_CODE_RAISED), conMethodName, intExitCode), e);
                System.exit(intExitCode);
            }
            catch (Exception ee) {
            }
        }

    } // private void Execute
} // class SosSchedulerDashboardMain