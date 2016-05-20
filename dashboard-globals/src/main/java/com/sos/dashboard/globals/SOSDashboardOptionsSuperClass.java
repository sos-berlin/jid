package com.sos.dashboard.globals;

import java.util.HashMap;

import com.sos.JSHelper.Annotations.JSOptionClass;
import com.sos.JSHelper.Annotations.JSOptionDefinition;
import com.sos.JSHelper.Exceptions.JSExceptionMandatoryOptionMissing;
import com.sos.JSHelper.Listener.JSListener;
import com.sos.JSHelper.Options.JSOptionsClass;
import com.sos.JSHelper.Options.SOSOptionBoolean;
import com.sos.JSHelper.Options.SOSOptionInFileName;
import com.sos.JSHelper.Options.SOSOptionString;

@JSOptionClass(name = "SOSDashboardOptionsSuperClass", description = "SOSDashboardOptionsSuperClass")
public class SOSDashboardOptionsSuperClass extends JSOptionsClass {

    private static final long serialVersionUID = -6664326152593606058L;
    private final String conClassName = "SOSDashboardOptionsSuperClass";

    @JSOptionDefinition(name = "Hibernate_Configuration_File", description = "", key = "Hibernate_Configuration_File", type = "SOSOptionString",
            mandatory = false)
    public SOSOptionInFileName hibernateConfigurationFile = new SOSOptionInFileName(this, conClassName + ".Hibernate_Configuration_File",
            "Configuration File for Hibernate", "./hibernate_mysql.cfg.xml", " ", true);
    public SOSOptionInFileName hibernateConfigFile = hibernateConfigurationFile;
    public SOSOptionInFileName hibernateConfig = (SOSOptionInFileName) hibernateConfigurationFile.setAlias(conClassName + ".Config");

    @JSOptionDefinition(name = "security_server", description = "", key = "security_server", type = "SOSOptionString", mandatory = false)
    public SOSOptionString securityServer = new SOSOptionString(this, conClassName + ".SecurityServer", "Security Server for security rest service",
            " ", " ", false);

    @JSOptionDefinition(name = "scheduler_id", description = "", key = "scheduler_id", type = "SOSOptionString", mandatory = false)
    public SOSOptionString schedulerId = new SOSOptionString(this, conClassName + ".SchedulerId", "scheduler_id that is assigned to this dashboard",
            " ", " ", true);

    @JSOptionDefinition(name = "scheduler_id", description = "", key = "session_id", type = "SOSOptionString", mandatory = false)
    public SOSOptionString sessionId = new SOSOptionString(this, conClassName + ".SchedulerId", "sessionId that is assigned to this dashboard",
            " ", " ", false);

    @JSOptionDefinition(name = "enableJobStart", description = "", key = "enableJobStart", type = "SOSOptionBoolean", mandatory = false)
    public SOSOptionBoolean enableJobStart = new SOSOptionBoolean(this, conClassName + ".enable_job_start", "Starting jobs and orders in context menu",
            "false", "false", false);
    @JSOptionDefinition(name = "enable_joc", description = "", key = "enable_joc", type = "SOSOptionBoolean", mandatory = false)
    public SOSOptionBoolean enableJOC = new SOSOptionBoolean(this, conClassName + ".enable_joc", "Show JOC", "false", "false", false);

    @JSOptionDefinition(name = "enable_reports", description = "", key = "enable_reports", type = "SOSOptionBoolean", mandatory = false)
    public SOSOptionBoolean enableReports = new SOSOptionBoolean(this, conClassName + ".enable_reports", "Show Reports", "false", "false", false);
    @JSOptionDefinition(name = "enable_jade", description = "", key = "enable_jade", type = "SOSOptionBoolean", mandatory = false)
    public SOSOptionBoolean enableJade = new SOSOptionBoolean(this, conClassName + ".enable_jade", "Show Jade Tab", "false", "false", false);

    @JSOptionDefinition(name = "enable_joe", description = "", key = "enable_joe", type = "SOSOptionBoolean", mandatory = false)
    public SOSOptionBoolean enableJOE = new SOSOptionBoolean(this, conClassName + ".enable_joe", "Show JOC", "false", "false", false);

    @JSOptionDefinition(name = "enable_events", description = "", key = "enable_events", type = "SOSOptionBoolean", mandatory = false)
    public SOSOptionBoolean enableEvents = new SOSOptionBoolean(this, conClassName + ".enable_events", "Show Events", "false", "false", false);

    @JSOptionDefinition(name = "enable_jobnet", description = "", key = "enable_jobnet", type = "SOSOptionBoolean", mandatory = false)
    public SOSOptionBoolean enableJobnet = new SOSOptionBoolean(this, conClassName + ".enable_jobnet", "Show JobNet", "false", "false", false);

    @JSOptionDefinition(name = "enable_scheduler_instances", description = "", key = "enable_scheduler_instances", type = "SOSOptionBoolean",
            mandatory = false)
    public SOSOptionBoolean enableSchedulerInstances = new SOSOptionBoolean(this, conClassName + ".enable_scheduler_instances",
            "Show Scheduler Instances", "false", "false", false);

    public SOSOptionBoolean getEnableJade() {
        return enableJade;
    }

    public SOSOptionBoolean getEnableReports() {
        return enableReports;
    }

    public SOSOptionBoolean getEnableJOC() {
        return enableJOC;
    }

    public SOSOptionBoolean getEnableEvents() {
        return enableEvents;
    }

    public SOSOptionBoolean getEnableJOE() {
        return enableJOE;
    }

    public SOSOptionBoolean getEnableJobnet() {
        return enableJobnet;
    }

    public SOSOptionBoolean getEnableSchedulerInstances() {
        return enableSchedulerInstances;
    }

    public void setEnableJOC(final SOSOptionBoolean enableJOC_) {
        enableJOC = enableJOC_;
    }

    public SOSOptionBoolean getEnableJobStart() {
        return enableJobStart;
    }

    public void setEnableJobStart(final SOSOptionBoolean enableJobStart_) {
        enableJobStart = enableJobStart_;
    }

    public void setSessionId(final SOSOptionString sessionId_) {
        sessionId = sessionId_;
    }

    public SOSOptionString getSessionId() {
        return this.sessionId;
    }

    public SOSOptionString getSecurityServer() {
        return this.securityServer;
    }

    public void setSecurityServer(final SOSOptionString p_securityServer) {
        this.securityServer = p_securityServer;
    }

    public SOSOptionString getSchedulerId() {
        return this.schedulerId;
    }

    public void setSchedulerId(final SOSOptionString p_schedulerId) {
        this.schedulerId = p_schedulerId;
    }

    public SOSOptionInFileName getConfigurationFile() {
        return hibernateConfigurationFile;
    }

    public void setConfigurationFile(final SOSOptionInFileName p_configurationFile) {
        this.hibernateConfigurationFile = p_configurationFile;
    }

    public SOSDashboardOptionsSuperClass() {
        objParentClass = this.getClass();
    }

    public SOSDashboardOptionsSuperClass(JSListener pobjListener) {
        this();
        this.registerMessageListener(pobjListener);
    }

    public SOSDashboardOptionsSuperClass(HashMap<String, String> JSSettings) throws Exception {
        this();
        this.setAllOptions(JSSettings);
    }

    public void setAllOptions(HashMap<String, String> pobjJSSettings) {
        flgSetAllOptions = true;
        objSettings = pobjJSSettings;
        super.setSettings(objSettings);
        super.setAllOptions(pobjJSSettings);
        flgSetAllOptions = false;
    }

    @Override
    public void checkMandatory() throws JSExceptionMandatoryOptionMissing, Exception {
        try {
            super.checkMandatory();
        } catch (Exception e) {
            throw new JSExceptionMandatoryOptionMissing(e.toString());
        }
    }

    @Override
    public void commandLineArgs(String[] pstrArgs) {
        super.commandLineArgs(pstrArgs);
        this.setAllOptions(super.objSettings);
    }

}