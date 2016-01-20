package com.sos.dailyschedule.job;

import java.util.HashMap;

import com.sos.JSHelper.Options.*;
import com.sos.JSHelper.Annotations.JSOptionClass;
import com.sos.JSHelper.Annotations.JSOptionDefinition;
import com.sos.JSHelper.Exceptions.JSExceptionMandatoryOptionMissing;
import com.sos.JSHelper.Listener.JSListener;
import org.apache.log4j.Logger;

import com.sos.JSHelper.Options.*;

@JSOptionClass(name = "CreateDailyScheduleOptionsSuperClass", description = "CreateDailyScheduleOptionsSuperClass")
public class CreateDailyScheduleOptionsSuperClass extends JSOptionsClass {

    private final String conClassName = "CreateDailyScheduleOptionsSuperClass";
    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(CreateDailyScheduleOptionsSuperClass.class);

    @JSOptionDefinition(name = "SchedulerHostName", description = "", key = "SchedulerHostName", type = "SOSOptionHostName", mandatory = false)
    public SOSOptionHostName SchedulerHostName = new SOSOptionHostName(this, conClassName + ".SchedulerHostName", // HashMap-Key
    "", // Titel
    " ", // InitValue
    " ", // DefaultValue
    false // isMandatory
    );

    public SOSOptionHostName getSchedulerHostName() {
        return SchedulerHostName;
    }

    public void setSchedulerHostName(SOSOptionHostName p_SchedulerHostName) {
        this.SchedulerHostName = p_SchedulerHostName;
    }

    @JSOptionDefinition(name = "configuration_file", description = "", key = "configuration_file", type = "SOSOptionString", mandatory = false)
    public SOSOptionString configuration_file = new SOSOptionString(this, conClassName + ".configuration_file", // HashMap-Key
    "", // Titel
    " ", // InitValue
    " ", // DefaultValue
    false // isMandatory
    );

    public SOSOptionString getconfiguration_file() {
        return configuration_file;
    }

    public void setconfiguration_file(SOSOptionString p_configuration_file) {
        this.configuration_file = p_configuration_file;
    }

    @JSOptionDefinition(name = "dayOffset", description = "", key = "dayOffset", type = "SOSOptionInteger", mandatory = false)
    public SOSOptionInteger dayOffset = new SOSOptionInteger(this, conClassName + ".dayOffset", // HashMap-Key
    "", // Titel
    "0", // InitValue
    "0", // DefaultValue
    false // isMandatory
    );

    public SOSOptionInteger getdayOffset() {
        return dayOffset;
    }

    public void setdayOffset(SOSOptionInteger p_dayOffset) {
        this.dayOffset = p_dayOffset;
    }

    @JSOptionDefinition(name = "scheduler_port", description = "", key = "scheduler_port", type = "SOSOptionPortNumber", mandatory = false)
    public SOSOptionPortNumber scheduler_port = new SOSOptionPortNumber(this, conClassName + ".scheduler_port", // HashMap-Key
    "", // Titel
    "4444", // InitValue
    "4444", // DefaultValue
    false // isMandatory
    );

    public SOSOptionPortNumber getscheduler_port() {
        return scheduler_port;
    }

    public void setscheduler_port(SOSOptionPortNumber p_scheduler_port) {
        this.scheduler_port = p_scheduler_port;
    }

    public SOSOptionPortNumber SchedulerTcpPortNumber = (SOSOptionPortNumber) scheduler_port.SetAlias(conClassName + ".SchedulerTcpPortNumber");

    public CreateDailyScheduleOptionsSuperClass() {
        objParentClass = this.getClass();
    } // public CreateDailyScheduleOptionsSuperClass

    public CreateDailyScheduleOptionsSuperClass(JSListener pobjListener) {
        this();
        this.registerMessageListener(pobjListener);
    } // public CreateDailyScheduleOptionsSuperClass

    //

    public CreateDailyScheduleOptionsSuperClass(HashMap<String, String> JSSettings) throws Exception {
        this();
        this.setAllOptions(JSSettings);
    } // public CreateDailyScheduleOptionsSuperClass (HashMap JSSettings)

    private String getAllOptionsAsString() {
        @SuppressWarnings("unused")
        final String conMethodName = conClassName + "::getAllOptionsAsString";
        String strT = conClassName + "\n";
        final StringBuffer strBuffer = new StringBuffer();
        strT += this.toString(); // fix
        return strT;
    } // private String getAllOptionsAsString ()

    public void setAllOptions(HashMap<String, String> pobjJSSettings) {
        @SuppressWarnings("unused")
        final String conMethodName = conClassName + "::setAllOptions";
        flgSetAllOptions = true;
        objSettings = pobjJSSettings;
        super.Settings(objSettings);
        super.setAllOptions(pobjJSSettings);
        flgSetAllOptions = false;
    } // public void setAllOptions (HashMap <String, String> JSSettings)

    @Override
    public void CheckMandatory() throws JSExceptionMandatoryOptionMissing //
            , Exception {
        try {
            super.CheckMandatory();
        } catch (Exception e) {
            throw new JSExceptionMandatoryOptionMissing(e.toString());
        }
    } // public void CheckMandatory ()

    @Override
    public void CommandLineArgs(String[] pstrArgs) {
        super.CommandLineArgs(pstrArgs);
        this.setAllOptions(super.objSettings);
    }
} // public class CreateDailyScheduleOptionsSuperClass