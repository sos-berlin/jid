

package com.sos.dailyschedule.job;

import java.util.HashMap;

import com.sos.JSHelper.Annotations.JSOptionClass;

import com.sos.JSHelper.Exceptions.JSExceptionMandatoryOptionMissing;
import com.sos.JSHelper.Listener.JSListener; 
import org.apache.log4j.Logger;

/**
 * \class 		CreateDailyScheduleOptions - Creating a DaysSchedule depending on actual Runtimes
 *
 * \brief 
 * An Options as a container for the Options super class. 
 * The Option class will hold all the things, which would be otherwise overwritten at a re-creation
 * of the super-class.
 *
 *

 *
 * see \see C:\Dokumente und Einstellungen\Uwe Risse\Lokale Einstellungen\Temp\scheduler_editor-2235912449518755069.html for (more) details.
 * 
 * \verbatim ;
 * mechanicaly created by JobDocu2OptionsClass.xslt from http://www.sos-berlin.com at 20111027105329 
 * \endverbatim
 */
@JSOptionClass(name = "CreateDaysScheduleOptions", description = "Creating a DaysSchedule depending on actual Runtimes")
public class CreateDailyScheduleOptions extends CreateDailyScheduleOptionsSuperClass {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	@SuppressWarnings("unused")  //$NON-NLS-1$
	private final String					conClassName						= "CreateDailyScheduleOptions";  //$NON-NLS-1$
	@SuppressWarnings("unused")
	private static Logger		logger			= Logger.getLogger(CreateDailyScheduleOptions.class);

    /**
    * constructors
    */
    
	public CreateDailyScheduleOptions() {
	} // public CreateDaysScheduleOptions

	public CreateDailyScheduleOptions(JSListener pobjListener) {
		this();
		this.registerMessageListener(pobjListener);
	} // public CreateDaysScheduleOptions
 

	public CreateDailyScheduleOptions (HashMap <String, String> JSSettings) throws Exception {
		super(JSSettings);
	} // public CreateDaysScheduleOptions (HashMap JSSettings)
/**
 * \brief CheckMandatory - prüft alle Muss-Optionen auf Werte
 *
 * \details
 * @throws Exception
 *
 * @throws Exception
 * - wird ausgelöst, wenn eine mandatory-Option keinen Wert hat
 */
		@Override  // CreateDaysScheduleOptionsSuperClass
	public void CheckMandatory() {
			
		try {
			super.CheckMandatory();
		}
		catch (Exception e) {
			throw new JSExceptionMandatoryOptionMissing(e.toString());
		}
	} // public void CheckMandatory ()
}

