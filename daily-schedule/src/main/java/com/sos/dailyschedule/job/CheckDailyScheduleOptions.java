

package com.sos.dailyschedule.job;

import java.util.HashMap;

import com.sos.JSHelper.Annotations.JSOptionClass;

import com.sos.JSHelper.Exceptions.JSExceptionMandatoryOptionMissing;
import com.sos.JSHelper.Listener.JSListener; 
import org.apache.log4j.Logger;

/**
 * \class 		CheckDailyScheduleOptions - Checking a DailySchedule with runs in History
 *
 * \brief 
 * An Options as a container for the Options super class. 
 * The Option class will hold all the things, which would be otherwise overwritten at a re-creation
 * of the super-class.
 *
 *

 *
 * see \see C:\Dokumente und Einstellungen\Uwe Risse\Lokale Einstellungen\Temp\scheduler_editor-463771972724180853.html for (more) details.
 * 
 * \verbatim ;
 * mechanicaly created by JobDocu2OptionsClass.xslt from http://www.sos-berlin.com at 20111212133923 
 * \endverbatim
 */
@JSOptionClass(name = "CheckDailyScheduleOptions", description = "Checking a DailySchedule with runs in History")
public class CheckDailyScheduleOptions extends CheckDailyScheduleOptionsSuperClass {
	@SuppressWarnings("unused")  //$NON-NLS-1$
	private final String					conClassName						= "CheckDailyScheduleOptions";  //$NON-NLS-1$
	@SuppressWarnings("unused")
	private static Logger		logger			= Logger.getLogger(CheckDailyScheduleOptions.class);

    /**
    * constructors
    */
    
	public CheckDailyScheduleOptions() {
	} // public CheckDailyScheduleOptions

	public CheckDailyScheduleOptions(JSListener pobjListener) {
		this();
		this.registerMessageListener(pobjListener);
	} // public CheckDailyScheduleOptions

		//

	public CheckDailyScheduleOptions (HashMap <String, String> JSSettings) throws Exception {
		super(JSSettings);
	} // public CheckDailyScheduleOptions (HashMap JSSettings)
/**
 * \brief CheckMandatory - prüft alle Muss-Optionen auf Werte
 *
 * \details
 * @throws Exception
 *
 * @throws Exception
 * - wird ausgelöst, wenn eine mandatory-Option keinen Wert hat
 */
		@Override  // CheckDailyScheduleOptionsSuperClass
	public void CheckMandatory() {
		try {
			super.CheckMandatory();
		}
		catch (Exception e) {
			throw new JSExceptionMandatoryOptionMissing(e.toString());
		}
	} // public void CheckMandatory ()
}

