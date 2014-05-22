package com.sos.dailyschedule.job;

import java.util.HashMap;

import com.sos.JSHelper.Options.*;
import com.sos.JSHelper.Annotations.JSOptionClass;
import com.sos.JSHelper.Annotations.JSOptionDefinition;
import com.sos.JSHelper.Exceptions.JSExceptionMandatoryOptionMissing;
import com.sos.JSHelper.Listener.JSListener; 
import org.apache.log4j.Logger;

import com.sos.JSHelper.Options.*;

/**
 * \class 		CreateDailyScheduleOptionsSuperClass - Creating a DailySchedule depending on actual Runtimes
 *
 * \brief 
 * An Options-Super-Class with all Options. This Class will be extended by the "real" Options-class (\see CreateDailyScheduleOptions.
 * The "real" Option class will hold all the things, which are normaly overwritten at a new generation
 * of the super-class.
 *
 *

 *
 * see \see C:\Dokumente und Einstellungen\Uwe Risse\Lokale Einstellungen\Temp\scheduler_editor-8285398818546013066.html for (more) details.
 * 
 * \verbatim ;
 * mechanicaly created by C:\Dokumente und Einstellungen\Uwe Risse\Eigene Dateien\sos-berlin.com\jobscheduler.1.3.9\scheduler_139\config\JOETemplates\java\xsl\JSJobDoc2JSOptionSuperClass.xsl from http://www.sos-berlin.com at 20111220173051 
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
 * Die folgenden Methode kann verwendet werden, um für einen Test eine HashMap
 * mit sinnvollen Werten für die einzelnen Optionen zu erzeugen.
 *
 * \verbatim
 private HashMap <String, String> SetJobSchedulerSSHJobOptions (HashMap <String, String> pobjHM) {
	pobjHM.put ("		CreateDailyScheduleOptionsSuperClass.auth_file", "test");  // This parameter specifies the path and name of a user's pr
		return pobjHM;
  }  //  private void SetJobSchedulerSSHJobOptions (HashMap <String, String> pobjHM)
 * \endverbatim
 */
@JSOptionClass(name = "CreateDailyScheduleOptionsSuperClass", description = "CreateDailyScheduleOptionsSuperClass")
public class CreateDailyScheduleOptionsSuperClass extends JSOptionsClass {
	private final String					conClassName						= "CreateDailyScheduleOptionsSuperClass";
		@SuppressWarnings("unused")
	private static Logger		logger			= Logger.getLogger(CreateDailyScheduleOptionsSuperClass.class);

		

/**
 * \var SchedulerHostName : 
 * This parameter specifies the host name or IP address of a server for which Job Scheduler is operated.
 *
 */
    @JSOptionDefinition(name = "SchedulerHostName", 
    description = "", 
    key = "SchedulerHostName", 
    type = "SOSOptionHostName", 
    mandatory = false)
    
    public SOSOptionHostName SchedulerHostName = new SOSOptionHostName(this, conClassName + ".SchedulerHostName", // HashMap-Key
                                                                "", // Titel
                                                                " ", // InitValue
                                                                " ", // DefaultValue
                                                                false // isMandatory
                    );

/**
 * \brief getSchedulerHostName : 
 * 
 * \details
 * This parameter specifies the host name or IP address of a server for which Job Scheduler is operated.
 *
 * \return 
 *
 */
    public SOSOptionHostName  getSchedulerHostName() {
        return SchedulerHostName;
    }

/**
 * \brief setSchedulerHostName : 
 * 
 * \details
 * This parameter specifies the host name or IP address of a server for which Job Scheduler is operated.
 *
 * @param SchedulerHostName : 
 */
    public void setSchedulerHostName (SOSOptionHostName p_SchedulerHostName) { 
        this.SchedulerHostName = p_SchedulerHostName;
    }

                        

/**
 * \var configuration_file : 
 * 
 *
 */
    @JSOptionDefinition(name = "configuration_file", 
    description = "", 
    key = "configuration_file", 
    type = "SOSOptionString", 
    mandatory = false)
    
    public SOSOptionString configuration_file = new SOSOptionString(this, conClassName + ".configuration_file", // HashMap-Key
                                                                "", // Titel
                                                                " ", // InitValue
                                                                " ", // DefaultValue
                                                                false // isMandatory
                    );

/**
 * \brief getconfiguration_file : 
 * 
 * \details
 * 
 *
 * \return 
 *
 */
    public SOSOptionString  getconfiguration_file() {
        return configuration_file;
    }

/**
 * \brief setconfiguration_file : 
 * 
 * \details
 * 
 *
 * @param configuration_file : 
 */
    public void setconfiguration_file (SOSOptionString p_configuration_file) { 
        this.configuration_file = p_configuration_file;
    }

                        

/**
 * \var dayOffset : 
 * set the from-to intervall for calculating the days schedule. Samples: 0: actual day -2:from the before yesterday until today 1: from today until tomorrow For each day a days schedule will be creatied. Existing days schedules will be deleted.
 *
 */
    @JSOptionDefinition(name = "dayOffset", 
    description = "", 
    key = "dayOffset", 
    type = "SOSOptionInteger", 
    mandatory = false)
    
    public SOSOptionInteger dayOffset = new SOSOptionInteger(this, conClassName + ".dayOffset", // HashMap-Key
                                                                "", // Titel
                                                                "0", // InitValue
                                                                "0", // DefaultValue
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
    public SOSOptionInteger  getdayOffset() {
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
    public void setdayOffset (SOSOptionInteger p_dayOffset) { 
        this.dayOffset = p_dayOffset;
    }

                        

/**
 * \var scheduler_port : 
 * The TCP-port for which a JobScheduler, see parameter scheduler_host .
 *
 */
    @JSOptionDefinition(name = "scheduler_port", 
    description = "", 
    key = "scheduler_port", 
    type = "SOSOptionPortNumber", 
    mandatory = false)
    
    public SOSOptionPortNumber scheduler_port = new SOSOptionPortNumber(this, conClassName + ".scheduler_port", // HashMap-Key
                                                                "", // Titel
                                                                "4444", // InitValue
                                                                "4444", // DefaultValue
                                                                false // isMandatory
                    );

/**
 * \brief getscheduler_port : 
 * 
 * \details
 * The TCP-port for which a JobScheduler, see parameter scheduler_host .
 *
 * \return 
 *
 */
    public SOSOptionPortNumber  getscheduler_port() {
        return scheduler_port;
    }

/**
 * \brief setscheduler_port : 
 * 
 * \details
 * The TCP-port for which a JobScheduler, see parameter scheduler_host .
 *
 * @param scheduler_port : 
 */
    public void setscheduler_port (SOSOptionPortNumber p_scheduler_port) { 
        this.scheduler_port = p_scheduler_port;
    }

                        
    public SOSOptionPortNumber SchedulerTcpPortNumber =
    (SOSOptionPortNumber) scheduler_port.SetAlias(conClassName + ".SchedulerTcpPortNumber");
        
        
	public CreateDailyScheduleOptionsSuperClass() {
		objParentClass = this.getClass();
	} // public CreateDailyScheduleOptionsSuperClass

	public CreateDailyScheduleOptionsSuperClass(JSListener pobjListener) {
		this();
		this.registerMessageListener(pobjListener);
	} // public CreateDailyScheduleOptionsSuperClass

		//

	public CreateDailyScheduleOptionsSuperClass (HashMap <String, String> JSSettings) throws Exception {
		this();
		this.setAllOptions(JSSettings);
	} // public CreateDailyScheduleOptionsSuperClass (HashMap JSSettings)
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
 * \brief setAllOptions - übernimmt die OptionenWerte aus der HashMap
 *
 * \details In der als Parameter anzugebenden HashMap sind Schlüssel (Name)
 * und Wert der jeweiligen Option als Paar angegeben. Ein Beispiel für den
 * Aufbau einer solchen HashMap findet sich in der Beschreibung dieser
 * Klasse (\ref TestData "setJobSchedulerSSHJobOptions"). In dieser Routine
 * werden die Schlüssel analysiert und, falls gefunden, werden die
 * dazugehörigen Werte den Properties dieser Klasse zugewiesen.
 *
 * Nicht bekannte Schlüssel werden ignoriert.
 *
 * \see JSOptionsClass::getItem
 *
 * @param pobjJSSettings
 * @throws Exception
 */
	public void setAllOptions(HashMap <String, String> pobjJSSettings) {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::setAllOptions";
		flgSetAllOptions = true;
		objSettings = pobjJSSettings;
		super.Settings(objSettings);
		super.setAllOptions(pobjJSSettings);
		flgSetAllOptions = false;
	} // public void setAllOptions (HashMap <String, String> JSSettings)

/**
 * \brief CheckMandatory - prüft alle Muss-Optionen auf Werte
 *
 * \details
 * @throws Exception
 *
 * @throws Exception
 * - wird ausgelöst, wenn eine mandatory-Option keinen Wert hat
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
 * \brief CommandLineArgs - Übernehmen der Options/Settings aus der
 * Kommandozeile
 *
 * \details Die in der Kommandozeile beim Starten der Applikation
 * angegebenen Parameter werden hier in die HashMap übertragen und danach
 * den Optionen als Wert zugewiesen.
 *
 * \return void
 *
 * @param pstrArgs
 * @throws Exception
 */
	@Override
	public void CommandLineArgs(String[] pstrArgs){
		super.CommandLineArgs(pstrArgs);
		this.setAllOptions(super.objSettings);
	}
} // public class CreateDailyScheduleOptionsSuperClass