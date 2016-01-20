package com.sos.dailyschedule.job;

import java.util.HashMap;

import com.sos.JSHelper.Options.*;
import com.sos.JSHelper.Annotations.JSOptionClass;
import com.sos.JSHelper.Annotations.JSOptionDefinition;
import com.sos.JSHelper.Exceptions.JSExceptionMandatoryOptionMissing;
import com.sos.JSHelper.Listener.JSListener;
import org.apache.log4j.Logger;

@JSOptionClass(name = "CheckDailyScheduleOptionsSuperClass", description = "CheckDailyScheduleOptionsSuperClass")
public class CheckDailyScheduleOptionsSuperClass extends JSOptionsClass {

    private final String conClassName = "CheckDailyScheduleOptionsSuperClass";
    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(CheckDailyScheduleOptionsSuperClass.class);

    @JSOptionDefinition(name = "configuration_file", description = "Die Datei mit den Einstellungen f? Datenbank. Beispiel: <?xml v", key = "configuration_file", type = "SOSOptionString", mandatory = false)
    public SOSOptionString configuration_file = new SOSOptionString(this, conClassName + ".configuration_file", // HashMap-Key
    "Die Datei mit den Einstellungen f? Datenbank. Beispiel: <?xml v", // Titel
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
    "-1", // InitValue
    "-1", // DefaultValue
    false // isMandatory
    );

    public SOSOptionInteger getdayOffset() {
        return dayOffset;
    }

    public void setdayOffset(SOSOptionInteger p_dayOffset) {
        this.dayOffset = p_dayOffset;
    }

    @JSOptionDefinition(name = "scheduler_id", description = "", key = "scheduler_id", type = "SOSOptionString", mandatory = false)
    public SOSOptionString scheduler_id = new SOSOptionString(this, conClassName + ".scheduler_id", // HashMap-Key
    "", // Titel
    "", // InitValue
    "", // DefaultValue
    false // isMandatory
    );

    public SOSOptionString getscheduler_id() {
        return scheduler_id;
    }

    public void setscheduler_id(SOSOptionString p_scheduler_id) {
        this.scheduler_id = p_scheduler_id;
    }

    @JSOptionDefinition(name = "check_all_jobscheduler_instances", description = "", key = "check_all_jobscheduler_instances", type = "SOSOptionBool", mandatory = false)
    public SOSOptionBoolean check_all_jobscheduler_instances = new SOSOptionBoolean(this, conClassName + ".check_all_jobscheduler_instances", // HashMap-Key
    "", // Titel
    "", // InitValue
    "", // DefaultValue
    false // isMandatory
    );

    public SOSOptionBoolean getcheck_all_jobscheduler_instances() {
        return check_all_jobscheduler_instances;
    }

    public void setcheck_all_jobscheduler_instances(SOSOptionBoolean p_check_all_jobscheduler_instances) {
        this.check_all_jobscheduler_instances = p_check_all_jobscheduler_instances;
    }

    public CheckDailyScheduleOptionsSuperClass() {
        objParentClass = this.getClass();
    } // public CheckDailyScheduleOptionsSuperClass

    public CheckDailyScheduleOptionsSuperClass(JSListener pobjListener) {
        this();
        this.registerMessageListener(pobjListener);
    } // public CheckDailyScheduleOptionsSuperClass

    //

    public CheckDailyScheduleOptionsSuperClass(HashMap<String, String> JSSettings) throws Exception {
        this();
        this.setAllOptions(JSSettings);
    } // public CheckDailyScheduleOptionsSuperClass (HashMap JSSettings)

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
    public void CheckMandatory() throws JSExceptionMandatoryOptionMissing, Exception {
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
} // public class CheckDailyScheduleOptionsSuperClass