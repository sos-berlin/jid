

package com.sos.test;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.sos.JSHelper.Annotations.JSOptionClass;
import com.sos.JSHelper.Annotations.JSOptionDefinition;
import com.sos.JSHelper.Exceptions.JSExceptionMandatoryOptionMissing;
import com.sos.JSHelper.Listener.JSListener;
import com.sos.JSHelper.Options.JSOptionsClass;
import com.sos.JSHelper.Options.SOSOptionString;

/**
 * \class 		CreateGenerateOrderOptionsSuperClass - Creating PDIR generate order
 *
 * \brief 
 * An Options-Super-Class with all Options. This Class will be extended by the "real" Options-class (\see CreateGenerateOrderOptions.
 * The "real" Option class will hold all the things, which are normaly overwritten at a new generation
 * of the super-class.
 *
 *
 * 
 * \verbatim ;
 * mechanicaly created by C:\ProgramData\sos-berlin.com\jobscheduler\scheduler_ur\config\JOETemplates\java\xsl\JSJobDoc2JSOptionSuperClass.xsl from http://www.sos-berlin.com at 20130225111725 
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
	pobjHM.put ("		CreateGenerateOrderOptionsSuperClass.auth_file", "test");  // This parameter specifies the path and name of a user's pr
		return pobjHM;
  }  //  private void SetJobSchedulerSSHJobOptions (HashMap <String, String> pobjHM)
 * \endverbatim
 */
@JSOptionClass(name = "CreateGenerateOrderOptionsSuperClass", description = "CreateGenerateOrderOptionsSuperClass")
public class CreateGenerateOrderOptionsSuperClass extends JSOptionsClass {
	private final String					conClassName						= "CreateGenerateOrderOptionsSuperClass";
		@SuppressWarnings("unused")
	private static Logger		logger			= Logger.getLogger(CreateGenerateOrderOptionsSuperClass.class);

		

/**
 * \var hibernate_configuration_file : 
 * 
 *
 */
    @JSOptionDefinition(name = "hibernate_configuration_file", 
    description = "", 
    key = "hibernate_configuration_file", 
    type = "SOSOptionString", 
    mandatory = false)
    
    public SOSOptionString hibernate_configuration_file = new SOSOptionString(this, conClassName + ".hibernate_configuration_file", // HashMap-Key
                                                                "", // Titel
                                                                " ", // InitValue
                                                                " ", // DefaultValue
                                                                false // isMandatory
                    );

/**
 * \brief gethibernate_configuration_file : 
 * 
 * \details
 * 
 *
 * \return 
 *
 */
    public SOSOptionString  gethibernate_configuration_file() {
        return hibernate_configuration_file;
    }

/**
 * \brief sethibernate_configuration_file : 
 * 
 * \details
 * 
 *
 * @param hibernate_configuration_file : 
 */
    public void sethibernate_configuration_file (final SOSOptionString p_hibernate_configuration_file) { 
        hibernate_configuration_file = p_hibernate_configuration_file;
    }

                        

/**
 * \var number_of_documents : 
 * 
 *
 */
    @JSOptionDefinition(name = "number_of_documents", 
    description = "", 
    key = "number_of_documents", 
    type = "SOSOptionString", 
    mandatory = false)
    
    public SOSOptionString number_of_documents = new SOSOptionString(this, conClassName + ".number_of_documents", // HashMap-Key
                                                                "", // Titel
                                                                " ", // InitValue
                                                                " ", // DefaultValue
                                                                false // isMandatory
                    );

/**
 * \brief getnumber_of_documents : 
 * 
 * \details
 * 
 *
 * \return 
 *
 */
    public SOSOptionString  getnumber_of_documents() {
        return number_of_documents;
    }

/**
 * \brief setnumber_of_documents : 
 * 
 * \details
 * 
 *
 * @param number_of_documents : 
 */
    public void setnumber_of_documents (final SOSOptionString p_number_of_documents) { 
        number_of_documents = p_number_of_documents;
    }

                        

/**
 * \var number_of_documents_probe : 
 * 
 *
 */
    @JSOptionDefinition(name = "number_of_documents_probe", 
    description = "", 
    key = "number_of_documents_probe", 
    type = "SOSOptionString", 
    mandatory = false)
    
    public SOSOptionString number_of_documents_probe = new SOSOptionString(this, conClassName + ".number_of_documents_probe", // HashMap-Key
                                                                "", // Titel
                                                                " ", // InitValue
                                                                " ", // DefaultValue
                                                                false // isMandatory
                    );

/**
 * \brief getnumber_of_documents_probe : 
 * 
 * \details
 * 
 *
 * \return 
 *
 */
    public SOSOptionString  getnumber_of_documents_probe() {
        return number_of_documents_probe;
    }

/**
 * \brief setnumber_of_documents_probe : 
 * 
 * \details
 * 
 *
 * @param number_of_documents_probe : 
 */
    public void setnumber_of_documents_probe (final SOSOptionString p_number_of_documents_probe) { 
        number_of_documents_probe = p_number_of_documents_probe;
    }

                        

/**
 * \var number_of_sample_documents : 
 * 
 *
 */
    @JSOptionDefinition(name = "number_of_sample_documents", 
    description = "", 
    key = "number_of_sample_documents", 
    type = "SOSOptionString", 
    mandatory = false)
    
    public SOSOptionString number_of_sample_documents = new SOSOptionString(this, conClassName + ".number_of_sample_documents", // HashMap-Key
                                                                "", // Titel
                                                                " ", // InitValue
                                                                " ", // DefaultValue
                                                                false // isMandatory
                    );

/**
 * \brief getnumber_of_sample_documents : 
 * 
 * \details
 * 
 *
 * \return 
 *
 */
    public SOSOptionString  getnumber_of_sample_documents() {
        return number_of_sample_documents;
    }

/**
 * \brief setnumber_of_sample_documents : 
 * 
 * \details
 * 
 *
 * @param number_of_sample_documents : 
 */
    public void setnumber_of_sample_documents (final SOSOptionString p_number_of_sample_documents) { 
        number_of_sample_documents = p_number_of_sample_documents;
    }

                        

/**
 * \var output_file_name : 
 * 
 *
 */
    @JSOptionDefinition(name = "output_file_name", 
    description = "", 
    key = "output_file_name", 
    type = "SOSOptionString", 
    mandatory = false)
    
    public SOSOptionString output_file_name = new SOSOptionString(this, conClassName + ".output_file_name", // HashMap-Key
                                                                "", // Titel
                                                                " ", // InitValue
                                                                " ", // DefaultValue
                                                                false // isMandatory
                    );

/**
 * \brief getoutput_file_name : 
 * 
 * \details
 * 
 *
 * \return 
 *
 */
    public SOSOptionString  getoutput_file_name() {
        return output_file_name;
    }

/**
 * \brief setoutput_file_name : 
 * 
 * \details
 * 
 *
 * @param output_file_name : 
 */
    public void setoutput_file_name (final SOSOptionString p_output_file_name) { 
        output_file_name = p_output_file_name;
    }

                        

/**
 * \var source_id : 
 * 
 *
 */
    @JSOptionDefinition(name = "source_id", 
    description = "", 
    key = "source_id", 
    type = "SOSOptionString", 
    mandatory = false)
    
    public SOSOptionString source_id = new SOSOptionString(this, conClassName + ".source_id", // HashMap-Key
                                                                "", // Titel
                                                                " ", // InitValue
                                                                " ", // DefaultValue
                                                                false // isMandatory
                    );

/**
 * \brief getsource_id : 
 * 
 * \details
 * 
 *
 * \return 
 *
 */
    public SOSOptionString  getsource_id() {
        return source_id;
    }

/**
 * \brief setsource_id : 
 * 
 * \details
 * 
 *
 * @param source_id : 
 */
    public void setsource_id (final SOSOptionString p_source_id) { 
        source_id = p_source_id;
    }

                        

/**
 * \var workflow_execution_id : 
 * 
 *
 */
    @JSOptionDefinition(name = "workflow_execution_id", 
    description = "", 
    key = "workflow_execution_id", 
    type = "SOSOptionString", 
    mandatory = false)
    
    public SOSOptionString workflow_execution_id = new SOSOptionString(this, conClassName + ".workflow_execution_id", // HashMap-Key
                                                                "", // Titel
                                                                " ", // InitValue
                                                                " ", // DefaultValue
                                                                false // isMandatory
                    );

/**
 * \brief getworkflow_execution_id : 
 * 
 * \details
 * 
 *
 * \return 
 *
 */
    public SOSOptionString  getworkflow_execution_id() {
        return workflow_execution_id;
    }

/**
 * \brief setworkflow_execution_id : 
 * 
 * \details
 * 
 *
 * @param workflow_execution_id : 
 */
    public void setworkflow_execution_id (final SOSOptionString p_workflow_execution_id) { 
        workflow_execution_id = p_workflow_execution_id;
    }

                        
        
        
	public CreateGenerateOrderOptionsSuperClass() {
		objParentClass = this.getClass();
	} // public CreateGenerateOrderOptionsSuperClass

	public CreateGenerateOrderOptionsSuperClass(final JSListener pobjListener) {
		this();
		this.registerMessageListener(pobjListener);
	} // public CreateGenerateOrderOptionsSuperClass

		//

	public CreateGenerateOrderOptionsSuperClass (final HashMap <String, String> JSSettings) throws Exception {
		this();
		this.setAllOptions(JSSettings);
	} // public CreateGenerateOrderOptionsSuperClass (HashMap JSSettings)
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
	@Override
	public void setAllOptions(final HashMap <String, String> pobjJSSettings) {
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
	public void CommandLineArgs(final String[] pstrArgs) {
		super.CommandLineArgs(pstrArgs);
		this.setAllOptions(super.objSettings);
	}
} // public class CreateGenerateOrderOptionsSuperClass