

package com.sos.jid.dialog.classes;

import java.util.HashMap;

import com.sos.JSHelper.Annotations.JSOptionClass;

import com.sos.JSHelper.Exceptions.JSExceptionMandatoryOptionMissing;
import com.sos.JSHelper.Listener.JSListener; 
import org.apache.log4j.Logger;

/**
 * \class 		nameOptions - title
 *
 * \brief 
 * An Options as a container for the Options super class. 
 * The Option class will hold all the things, which would be otherwise overwritten at a re-creation
 * of the super-class.
 *
 *

 *
 * see \see C:\Dokumente und Einstellungen\Uwe Risse\Lokale Einstellungen\Temp\scheduler_editor-8766529980398533444.html for (more) details.
 * 
 * \verbatim ;
 * mechanicaly created by JobDocu2OptionsClass.xslt from http://www.sos-berlin.com at 20120203101939 
 * \endverbatim
 */
@JSOptionClass(name = "nameOptions", description = "title")
public class SOSDashboardOptions extends SOSDashboardOptionsSuperClass {
	@SuppressWarnings("unused")  //$NON-NLS-1$
	private final String					conClassName						= "nameOptions";  //$NON-NLS-1$
	@SuppressWarnings("unused")
	private static Logger		logger			= Logger.getLogger(SOSDashboardOptions.class);

    /**
    * constructors
    */
    
	public SOSDashboardOptions() {
		super();
	} // public nameOptions

	public SOSDashboardOptions(JSListener pobjListener) {
		this();
		this.registerMessageListener(pobjListener);
	} // public nameOptions

		//

	public SOSDashboardOptions (HashMap <String, String> JSSettings) throws Exception {
		super(JSSettings);
	} // public nameOptions (HashMap JSSettings)
/**
 * \brief CheckMandatory - prüft alle Muss-Optionen auf Werte
 *
 * \details
 * @throws Exception
 *
 * @throws Exception
 * - wird ausgelöst, wenn eine mandatory-Option keinen Wert hat
 */
		@Override  // nameOptionsSuperClass
	public void CheckMandatory() {
		try {
			super.CheckMandatory();
		}
		catch (Exception e) {
			throw new JSExceptionMandatoryOptionMissing(e.toString());
		}
	} // public void CheckMandatory ()
}

