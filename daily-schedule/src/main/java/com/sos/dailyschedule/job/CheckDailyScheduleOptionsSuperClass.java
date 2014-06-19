package com.sos.dailyschedule.job;

import java.util.HashMap;

import com.sos.JSHelper.Options.*;
import com.sos.JSHelper.Annotations.JSOptionClass;
import com.sos.JSHelper.Annotations.JSOptionDefinition;
import com.sos.JSHelper.Exceptions.JSExceptionMandatoryOptionMissing;
import com.sos.JSHelper.Listener.JSListener;
import org.apache.log4j.Logger;

/**
 * \class 		CheckDailyScheduleOptionsSuperClass - Checking a DailySchedule with runs in History
 *
 * \brief 
 * An Options-Super-Class with all Options. This Class will be extended by the "real" Options-class (\see CheckDailyScheduleOptions.
 * The "real" Option class will hold all the things, which are normaly overwritten at a new generation
 * of the super-class.
 *
 *

 *
 * see \see C:\Dokumente und Einstellungen\Uwe Risse\Lokale Einstellungen\Temp\scheduler_editor-467935809262608294.html for (more) details.
 * 
 * \verbatim ;
 * mechanicaly created by C:\Dokumente und Einstellungen\Uwe Risse\Eigene Dateien\sos-berlin.com\jobscheduler.1.3.9\scheduler_139\config\JOETemplates\java\xsl\JSJobDoc2JSOptionSuperClass.xsl from http://www.sos-berlin.com at 20111220173927 
 * \endverbatim
 * \section OptionsTable Tabelle der vorhandenen Optionen
 * 
 * Tabelle mit allen Optionen
 * 
 * MethodName
 * Title
 * Setting
 * Description
 * IsMandatory
 * DataType
 * InitialValue
 * TestValue
 * 
 * 
 *
 * \section TestData Eine Hilfe zum Erzeugen einer HashMap mit Testdaten
 *
 * Die folgenden Methode kann verwendet werden, um f?en Test eine HashMap
 * mit sinnvollen Werten f? einzelnen Optionen zu erzeugen.
 *
 * \verbatim
 private HashMap <String, String> SetJobSchedulerSSHJobOptions (HashMap <String, String> pobjHM) {
	pobjHM.put ("		CheckDailyScheduleOptionsSuperClass.auth_file", "test");  // This parameter specifies the path and name of a user's pr
		return pobjHM;
  }  //  private void SetJobSchedulerSSHJobOptions (HashMap <String, String> pobjHM)
 * \endverbatim
 */
@JSOptionClass(name = "CheckDailyScheduleOptionsSuperClass", description = "CheckDailyScheduleOptionsSuperClass")
public class CheckDailyScheduleOptionsSuperClass extends JSOptionsClass {
    private final String   conClassName       = "CheckDailyScheduleOptionsSuperClass";
    @SuppressWarnings("unused")
    private static Logger  logger             = Logger.getLogger(CheckDailyScheduleOptionsSuperClass.class);

    /**
     * \var configuration_file : Die Datei mit den Einstellungen f? Datenbank. Beispiel: <?xml v
     * Die Datei mit den Einstellungen f? Datenbank. Beispiel: <?xml version="1.0" encoding="UTF-8"?> <!DOCTYPE hibernate-configuration PUBLIC "-//Hibernate/Hibernate Configuration DTD 3.0//EN" "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd"> <hibernate-configuration> <session-factory> <property name="hibernate.connection.driver_class">oracle.jdbc.driver.OracleDriver</property> <property name="hibernate.connection.password">dbpwd</property> <property name="hibernate.connection.url">jdbc:oracle:thin:@8of9:1521:dbserver</property> <property name="hibernate.connection.username">dbuser</property> <property name="hibernate.dialect">org.hibernate.dialect.Oracle10gDialect</property> <property name="hibernate.show_sql">true</property> <property name="hibernate.connection.autocommit">false</property> <property name="hibernate.format_sql">true</property> <property name="hibernate.temp.use_jdbc_metadata_defaults">false</property> <mapping class="com.sos.jade.db.JadeTransferDBItem"/> <mapping class="com.sos.jade.db.JadeTransferDetailDBItem"/> <mapping class="com.sos.dailyschedule.db.DailyScheduleDBItem"/> <mapping class="com.sos.scheduler.history.db.SchedulerHistoryDBItem"/> <mapping class="com.sos.scheduler.history.db.SchedulerOrderHistoryDBItem"/> </session-factory> </hibernate-configuration>
     *
     */
    @JSOptionDefinition(name = "configuration_file", description = "Die Datei mit den Einstellungen f? Datenbank. Beispiel: <?xml v", key = "configuration_file", type = "SOSOptionString", mandatory = false)
    public SOSOptionString configuration_file = new SOSOptionString(this, conClassName + ".configuration_file", // HashMap-Key
                                                      "Die Datei mit den Einstellungen f? Datenbank. Beispiel: <?xml v", // Titel
                                                      " ", // InitValue
                                                      " ", // DefaultValue
                                                      false // isMandatory
                                              );

    /**
     * \brief getconfiguration_file : Die Datei mit den Einstellungen f? Datenbank. Beispiel: <?xml v
     * 
     * \details
     * Die Datei mit den Einstellungen f? Datenbank. Beispiel: <?xml version="1.0" encoding="UTF-8"?> <!DOCTYPE hibernate-configuration PUBLIC "-//Hibernate/Hibernate Configuration DTD 3.0//EN" "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd"> <hibernate-configuration> <session-factory> <property name="hibernate.connection.driver_class">oracle.jdbc.driver.OracleDriver</property> <property name="hibernate.connection.password">dbpwd</property> <property name="hibernate.connection.url">jdbc:oracle:thin:@8of9:1521:dbserver</property> <property name="hibernate.connection.username">dbuser</property> <property name="hibernate.dialect">org.hibernate.dialect.Oracle10gDialect</property> <property name="hibernate.show_sql">true</property> <property name="hibernate.connection.autocommit">false</property> <property name="hibernate.format_sql">true</property> <property name="hibernate.temp.use_jdbc_metadata_defaults">false</property> <mapping class="com.sos.jade.db.JadeTransferDBItem"/> <mapping class="com.sos.jade.db.JadeTransferDetailDBItem"/> <mapping class="com.sos.dailyschedule.db.DailyScheduleDBItem"/> <mapping class="com.sos.scheduler.history.db.SchedulerHistoryDBItem"/> <mapping class="com.sos.scheduler.history.db.SchedulerOrderHistoryDBItem"/> </session-factory> </hibernate-configuration>
     *
     * \return Die Datei mit den Einstellungen f? Datenbank. Beispiel: <?xml v
     *
     */
    public SOSOptionString getconfiguration_file() {
        return configuration_file;
    }

    /**
     * \brief setconfiguration_file : Die Datei mit den Einstellungen f? Datenbank. Beispiel: <?xml v
     * 
     * \details
     * Die Datei mit den Einstellungen f? Datenbank. Beispiel: <?xml version="1.0" encoding="UTF-8"?> <!DOCTYPE hibernate-configuration PUBLIC "-//Hibernate/Hibernate Configuration DTD 3.0//EN" "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd"> <hibernate-configuration> <session-factory> <property name="hibernate.connection.driver_class">oracle.jdbc.driver.OracleDriver</property> <property name="hibernate.connection.password">dbpwd</property> <property name="hibernate.connection.url">jdbc:oracle:thin:@8of9:1521:dbserver</property> <property name="hibernate.connection.username">dbuser</property> <property name="hibernate.dialect">org.hibernate.dialect.Oracle10gDialect</property> <property name="hibernate.show_sql">true</property> <property name="hibernate.connection.autocommit">false</property> <property name="hibernate.format_sql">true</property> <property name="hibernate.temp.use_jdbc_metadata_defaults">false</property> <mapping class="com.sos.jade.db.JadeTransferDBItem"/> <mapping class="com.sos.jade.db.JadeTransferDetailDBItem"/> <mapping class="com.sos.dailyschedule.db.DailyScheduleDBItem"/> <mapping class="com.sos.scheduler.history.db.SchedulerHistoryDBItem"/> <mapping class="com.sos.scheduler.history.db.SchedulerOrderHistoryDBItem"/> </session-factory> </hibernate-configuration>
     *
     * @param configuration_file : Die Datei mit den Einstellungen f? Datenbank. Beispiel: <?xml v
     */
    public void setconfiguration_file(SOSOptionString p_configuration_file) {
        this.configuration_file = p_configuration_file;
    }

    /**
     * \var dayOffset : 
     * set the from-to intervall for calculating the days schedule. Samples: 0: actual day -2:from the before yesterday until today 1: from today until tomorrow For each day a days schedule will be creatied. Existing days schedules will be deleted.
     *
     */
    @JSOptionDefinition(name = "dayOffset", description = "", key = "dayOffset", type = "SOSOptionInteger", mandatory = false)
    public SOSOptionInteger dayOffset = new SOSOptionInteger(this, conClassName + ".dayOffset", // HashMap-Key
                                              "", // Titel
                                              "-1", // InitValue
                                              "-1", // DefaultValue
                                              false // isMandatory
                                      );

    /**
     * \brief getdayOffset : 
     * 
     * \details
     * set the from-to intervall for calculating the days schedule. Samples: 0: actual day -2:from the before yesterday until today 1: from today until tomorrow For each day a days schedule will be creatied. Existing days schedules will be deleted.
     *
     * \return 
     *
     */
    public SOSOptionInteger getdayOffset() {
        return dayOffset;
    }

    /**
     * \brief setdayOffset : 
     * 
     * \details
     * set the from-to intervall for calculating the days schedule. Samples: 0: actual day -2:from the before yesterday until today 1: from today until tomorrow For each day a days schedule will be creatied. Existing days schedules will be deleted.
     *
     * @param dayOffset : 
     */
    public void setdayOffset(SOSOptionInteger p_dayOffset) {
        this.dayOffset = p_dayOffset;
    }

  

    /**
     * \var scheduler_id : 
     * 
     *
     */
    @JSOptionDefinition(name = "scheduler_id", description = "", key = "scheduler_id", type = "SOSOptionString", mandatory = false)
    public SOSOptionString scheduler_id = new SOSOptionString(this, conClassName + ".scheduler_id", // HashMap-Key
                                                "", // Titel
                                                "", // InitValue
                                                "", // DefaultValue
                                                false // isMandatory
                                        );

    /**
     * \brief getscheduler_id : 
     * 
     * \details
     * 
     *
     * \return 
     *
     */
    public SOSOptionString getscheduler_id() {
        return scheduler_id;
    }

    /**
     * \brief setscheduler_id : 
     * 
     * \details
     * 
     *
     * @param scheduler_id : 
     */
    public void setscheduler_id(SOSOptionString p_scheduler_id) {
        this.scheduler_id = p_scheduler_id;
    }

    /**
     * \var check_all_jobscheduler_instances : 
     * 
     *
     */
    @JSOptionDefinition(name = "check_all_jobscheduler_instances", description = "", key = "check_all_jobscheduler_instances", type = "SOSOptionBool", mandatory = false)
    public SOSOptionBoolean check_all_jobscheduler_instances = new SOSOptionBoolean(this, conClassName + ".check_all_jobscheduler_instances", // HashMap-Key
                                                                     "", // Titel
                                                                     "", // InitValue
                                                                     "", // DefaultValue
                                                                     false // isMandatory
                                                             );

    /**
     * \brief getcheck_all_jobscheduler_instances : 
     * 
     * \details
     * 
     *
     * \return 
     *
     */
    public SOSOptionBoolean getcheck_all_jobscheduler_instances() {
        return check_all_jobscheduler_instances;
    }

    /**
     * \brief setcheck_all_jobscheduler_instances : 
     * 
     * \details
     * 
     *
     * @param check_all_jobscheduler_instances : 
     */
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

    /**
     * \brief getAllOptionsAsString - liefert die Werte und Beschreibung aller
     * Optionen als String
     *
     * \details
     * 
     * \see toString 
     * \see toOut
     */
    private String getAllOptionsAsString() {
        @SuppressWarnings("unused")
        final String conMethodName = conClassName + "::getAllOptionsAsString";
        String strT = conClassName + "\n";
        final StringBuffer strBuffer = new StringBuffer();
        // strT += IterateAllDataElementsByAnnotation(objParentClass, this,
        // JSOptionsClass.IterationTypes.toString, strBuffer);
        // strT += IterateAllDataElementsByAnnotation(objParentClass, this, 13,
        // strBuffer);
        strT += this.toString(); // fix
        //
        return strT;
    } // private String getAllOptionsAsString ()

    /**
     * \brief setAllOptions - ?mmt die OptionenWerte aus der HashMap
     *
     * \details In der als Parameter anzugebenden HashMap sind Schl?(Name)
     * und Wert der jeweiligen Option als Paar angegeben. Ein Beispiel f?
     * Aufbau einer solchen HashMap findet sich in der Beschreibung dieser
     * Klasse (\ref TestData "setJobSchedulerSSHJobOptions"). In dieser Routine
     * werden die Schl?analysiert und, falls gefunden, werden die
     * dazugeh?en Werte den Properties dieser Klasse zugewiesen.
     *
     * Nicht bekannte Schl?werden ignoriert.
     *
     * \see JSOptionsClass::getItem
     *
     * @param pobjJSSettings
     * @throws Exception
     */
    public void setAllOptions(HashMap<String, String> pobjJSSettings) {
        @SuppressWarnings("unused")
        final String conMethodName = conClassName + "::setAllOptions";
        flgSetAllOptions = true;
        objSettings = pobjJSSettings;
        super.Settings(objSettings);
        super.setAllOptions(pobjJSSettings);
        flgSetAllOptions = false;
    } // public void setAllOptions (HashMap <String, String> JSSettings)

    /**
     * \brief CheckMandatory - pr?le Muss-Optionen auf Werte
     *
     * \details
     * @throws Exception
     *
     * @throws Exception
     * - wird ausgel? wenn eine mandatory-Option keinen Wert hat
     */
    @Override
    public void CheckMandatory() throws JSExceptionMandatoryOptionMissing //
    , Exception {
        try {
            super.CheckMandatory();
        }
        catch (Exception e) {
            throw new JSExceptionMandatoryOptionMissing(e.toString());
        }
    } // public void CheckMandatory ()

    /**
     *
     * \brief CommandLineArgs - ?ernehmen der Options/Settings aus der
     * Kommandozeile
     *
     * \details Die in der Kommandozeile beim Starten der Applikation
     * angegebenen Parameter werden hier in die HashMap ?agen und danach
     * den Optionen als Wert zugewiesen.
     *
     * \return void
     *
     * @param pstrArgs
     * @throws Exception
     */
    @Override
    public void CommandLineArgs(String[] pstrArgs) {
        super.CommandLineArgs(pstrArgs);
        this.setAllOptions(super.objSettings);
    }
} // public class CheckDailyScheduleOptionsSuperClass