

package com.sos.test;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.sos.JSHelper.Annotations.JSOptionClass;
import com.sos.JSHelper.Exceptions.JSExceptionMandatoryOptionMissing;
import com.sos.JSHelper.Listener.JSListener;

/**
 * \class 		CreateGenerateOrderOptions - Creating PDIR generate order
 *
 * \brief
 * An Options as a container for the Options super class.
 * The Option class will hold all the things, which would be otherwise overwritten at a re-creation
 * of the super-class.
 *
 *

 *
 *
 * \verbatim ;
 * mechanicaly created by JobDocu2OptionsClass.xslt from http://www.sos-berlin.com at 20130225111725
 * \endverbatim
 */
@JSOptionClass(name = "CreateGenerateOrderOptions", description = "Creating PDIR generate order")
public class CreateGenerateOrderOptions extends CreateGenerateOrderOptionsSuperClass {
	/**
	 *
	 */
	private static final long	serialVersionUID	= -8254332282779452476L;
	@SuppressWarnings("unused")
	private final String					conClassName						= "CreateGenerateOrderOptions";
	@SuppressWarnings("unused")
	private static Logger		logger			= Logger.getLogger(CreateGenerateOrderOptions.class);

    /**
    * constructors
    */

	public CreateGenerateOrderOptions() {
	} // public CreateGenerateOrderOptions

	public CreateGenerateOrderOptions(final JSListener pobjListener) {
		this();
		this.registerMessageListener(pobjListener);
	} // public CreateGenerateOrderOptions

		//

	public CreateGenerateOrderOptions (final HashMap <String, String> JSSettings) throws Exception {
		super(JSSettings);
	} // public CreateGenerateOrderOptions (HashMap JSSettings)
/**
 * \brief CheckMandatory - prüft alle Muss-Optionen auf Werte
 *
 * \details
 * @throws Exception
 *
 * @throws Exception
 * - wird ausgelöst, wenn eine mandatory-Option keinen Wert hat
 */
		@Override  // CreateGenerateOrderOptionsSuperClass
	public void CheckMandatory() {
		try {
			super.CheckMandatory();
		}
		catch (Exception e) {
			throw new JSExceptionMandatoryOptionMissing(e.toString());
		}
	} // public void CheckMandatory ()
}

