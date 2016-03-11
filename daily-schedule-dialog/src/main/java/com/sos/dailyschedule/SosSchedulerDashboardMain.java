package com.sos.dailyschedule;

import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import com.sos.joe.globals.messages.ErrorLog;
import sos.util.SOSClassUtil;
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

@I18NResourceBundle(baseName = "SOSSchedulerDashboardMain", defaultLocale = "en")
public class SosSchedulerDashboardMain extends I18NBase {

    private static final String COMMAND_PERMISSION = "/jobscheduler/rest/sosPermission/permissions?user=%s&pwd=%s";
    private static final String SOS_PRODUCTS_JID_EXECUTE = "sos:products:jid:execute";
    private final static String CLASS_NAME = "SosSchedulerDashboardMain";
    private static final Logger LOGGER = Logger.getLogger(SosSchedulerDashboardMain.class);
    protected SOSDashboardOptions objOptions = null;
    public static final String SOSDASHBOARD_INTRO = "SosSchedulerDashboardMain.SOSDB-Intro";
    public static final String SOSDASHBOARD_E_0001 = "SosSchedulerDashboardMain.SOSDX_E_0001";
    public static final String SOS_EXIT_WO_ERRORS = "SosSchedulerDashboardMain.SOS_EXIT_WO_ERRORS";
    public static final String SOS_EXIT_CODE_RAISED = "SosSchedulerDashboardMain.SOS_EXIT_CODE_RAISED";
    private boolean isAuthenticated;
    private SOSJaxbSubject currentUser = null;
    private List<SchedulerInstancesDBItem> schedulerInstances;

    public final static void main(final String[] pstrArgs) {
        SosSchedulerDashboardMain objEngine = new SosSchedulerDashboardMain();
        objEngine.execute(pstrArgs);
    }

    protected SosSchedulerDashboardMain() {
        super(DashBoardConstants.conPropertiesFileName);

    }

    private boolean doLogin() throws Exception {
        boolean tryingNextServer = false;
        SOSLoginDialog sosLoginDialog = new SOSLoginDialog(new Shell(), 0);
        SOSRestShiroClient sosRestShiroClient = new SOSRestShiroClient();
        SOSWebserviceAuthenticationRecord sosWebserviceAuthenticationRecord = new SOSWebserviceAuthenticationRecord();
        do {
            try {
                if (!tryingNextServer || (currentUser != null && !currentUser.isAuthenticated())) {
                    sosLoginDialog.open();
                }
                if (sosLoginDialog.getUser() != null) {
                    sosWebserviceAuthenticationRecord.setUser(sosLoginDialog.getUser());
                    sosWebserviceAuthenticationRecord.setPassword(sosLoginDialog.getPassword());
                    sosWebserviceAuthenticationRecord.setResource(objOptions.securityServer.Value() + COMMAND_PERMISSION);
                    sosWebserviceAuthenticationRecord.setSessionId("");
                    SOSPermissionShiro sosPermissionShiro = sosRestShiroClient.getPermissions(sosWebserviceAuthenticationRecord);
                    currentUser = new SOSJaxbSubject(sosPermissionShiro);
                    if (currentUser == null) {
                        sosLoginDialog.setMsg("error authenticating user/password");
                    } else if (!currentUser.isAuthenticated()) {
                        sosLoginDialog.setMsg("could not authenticate user/password");
                    }
                } else {
                    sosLoginDialog.setMsg("login cancelled");
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                boolean enabled = getNextSecurityServer();
                tryingNextServer = true;
                if (!enabled) {
                    Shell shell = new Shell();
                    MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION);
                    String message = String.format(Messages.getLabel(DashBoardConstants.conSOSDashB_NoSecurityServer) + ": %s", objOptions.securityServer.Value());
                    messageBox.setMessage(message + "\nMessage: " + e.getMessage());
                    messageBox.open();
                    throw new Exception(message);
                }
            }
        } while (!(sosLoginDialog.isCancel() || ((currentUser != null && currentUser.isAuthenticated()))));
        return (currentUser != null && currentUser.isAuthenticated());
    }

    private boolean getNextSecurityServer() {
        boolean enabled = false;
        if (schedulerInstances == null) {
            return false;
        } else {
            Iterator<SchedulerInstancesDBItem> schedulerInstancesIterator = schedulerInstances.iterator();
            while (!enabled && schedulerInstancesIterator.hasNext()) {
                SchedulerInstancesDBItem schedulerInstancesDBItem = (SchedulerInstancesDBItem) schedulerInstancesIterator.next();
                String webServicAddress = String.format("http://%s:%s", schedulerInstancesDBItem.getHostname(), schedulerInstancesDBItem.getJettyHttpPort());
                try {
                    enabled = schedulerInstancesDBItem.getIsSosCommandWebservice();
                    if (enabled) {
                        schedulerInstancesDBItem.setIsSosCommandWebservice(false);
                        objOptions.securityServer.Value(webServicAddress);
                    }
                } catch (Exception e) {
                    LOGGER.info(e.getMessage(), e);
                    enabled = true;
                }
            }
            return enabled;
        }

    }

    private boolean getSecurityEnabled() {
        SchedulerInstancesDBLayer schedulerInstancesDBLayer = new SchedulerInstancesDBLayer(objOptions.hibernateConfigurationFile.Value());
        schedulerInstancesDBLayer.getFilter().setSosCommandWebservice(true);
        schedulerInstances = schedulerInstancesDBLayer.getSchedulerInstancesList();
        return getNextSecurityServer();
    }

    private void execute(final String[] pstrArgs) {
        final String conMethodName = CLASS_NAME + "::Execute";
        try {
            LOGGER.info(Messages.getMsg(SOSDASHBOARD_INTRO));
            Shell shell = new Shell();
            objOptions = new SOSDashboardOptions();
            objOptions.CommandLineArgs(pstrArgs);
            boolean securityEnabled = true;
            try {
                securityEnabled = getSecurityEnabled();
                if (securityEnabled) {
                    isAuthenticated = doLogin() && (currentUser.hasRole("jid") || currentUser.isPermitted(SOS_PRODUCTS_JID_EXECUTE));
                } else {
                    isAuthenticated = true;
                    if (pstrArgs.length > 0) {
                        LOGGER.debug("pstrArgs = " + pstrArgs[0].toString());
                        LOGGER.debug("user-dir = " + System.getProperty("user.dir"));
                        objOptions.CommandLineArgs(pstrArgs);
                    }
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION);
                messageBox.setMessage(Messages.getLabel(DashBoardConstants.conSOSDashB_CouldNotConnect) + "\nMessage: " + e.getMessage());
                messageBox.open();
                isAuthenticated = false;
                objOptions.enableJobnet.value(false);
            }
            if (isAuthenticated) {
                if (currentUser != null) {
                    objOptions.enableJOC.value(currentUser.hasRole("joc") || currentUser.isPermitted("sos:products:jid:joctab:show"));
                    objOptions.enableReports.value(currentUser.hasRole("reports") || currentUser.isPermitted("sos:products:jid:reportstab:show"));
                    objOptions.enableJOE.value(currentUser.hasRole("joe") || currentUser.isPermitted("sos:products:jid:joetab:show"));
                    objOptions.enableEvents.value(currentUser.hasRole("events") || currentUser.isPermitted("sos:products:jid:eventtab:show"));
                    objOptions.enableSchedulerInstances.value(currentUser.isPermitted("sos:products:jid:instances:show"));
                    objOptions.enableJobStart.value(currentUser.isPermitted("sos:products:jid:jobstart"));
                }
                try {
                    Composite composite = new Composite(shell, SWT.NONE);
                    objOptions.CheckMandatory();

                    LOGGER.debug(objOptions.toString());
                    DailyScheduleDataProvider dataProvider = new DailyScheduleDataProvider(objOptions.hibernateConfigurationFile.JSFile());
                    DashboardShowDialog window = new DashboardShowDialog(composite);
                    try {
                        window.setDataProvider(dataProvider);
                        window.setCurrentUser(currentUser);
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage(), e);
                        MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION);
                        messageBox.setMessage(Messages.getLabel(DashBoardConstants.conSOSDashB_CouldNotConnect) + "\nMessage: " + e.getMessage());
                        messageBox.open();
                        objOptions.enableJobnet.value(false);
                    }
                    window.setObjOptions(objOptions);
                    window.open();
                    LOGGER.debug("...............quit");
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                    throw e;
                }
            }
        } catch (Exception e) {
            try {
                LOGGER.fatal("sudden death", e);
                new ErrorLog("JID", "error in " + SOSClassUtil.getMethodName() + "cause: " + e.toString(), e);
                int intExitCode = 99;
                System.exit(intExitCode);
                LOGGER.error(String.format(getMsg(SOS_EXIT_CODE_RAISED), conMethodName, intExitCode), e);
            } catch (Exception ee) {
                LOGGER.error(ee.getMessage(), ee);
                int intExitCode = 199;
                System.exit(intExitCode);
            }
        }
    }

}
