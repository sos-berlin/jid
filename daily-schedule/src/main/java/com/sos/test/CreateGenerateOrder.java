package com.sos.test;

import static com.sos.scheduler.messages.JSMessages.JSJ_F_107;
import static com.sos.scheduler.messages.JSMessages.JSJ_I_110;
import static com.sos.scheduler.messages.JSMessages.JSJ_I_111;

import org.apache.log4j.Logger;

import com.sos.JSHelper.Basics.JSJobUtilitiesClass;
import com.sos.JSHelper.Exceptions.JobSchedulerException;

/**
 * \class 		CreateGenerateOrder - Workerclass for "Creating PDIR generate order"
 *
 * \brief AdapterClass of CreateGenerateOrder for the SOSJobScheduler
 *
 * This Class CreateGenerateOrder is the worker-class.
 *

 *
 *
 * \verbatim ;
 * mechanicaly created by C:\ProgramData\sos-berlin.com\jobscheduler\scheduler_ur\config\JOETemplates\java\xsl\JSJobDoc2JSWorkerClass.xsl from http://www.sos-berlin.com at 20130225111725
 * \endverbatim
 */
public class CreateGenerateOrder extends JSJobUtilitiesClass<CreateGenerateOrderOptions> {
	private final String	conClassName	= "CreateGenerateOrder";
	private static Logger	logger			= Logger.getLogger(CreateGenerateOrder.class);

	/**
	 *
	 * \brief CreateGenerateOrder
	 *
	 * \details
	 *
	 */
	public CreateGenerateOrder() {
		super(new CreateGenerateOrderOptions());
	}

	/**
	 *
	 * \brief Execute - Start the Execution of CreateGenerateOrder
	 *
	 * \details
	 *
	 * For more details see
	 *
	 * \see JobSchedulerAdapterClass
	 * \see CreateGenerateOrderMain
	 *
	 * \return CreateGenerateOrder
	 *
	 * @return
	 */
	public CreateGenerateOrder Execute() throws Exception {
		final String conMethodName = conClassName + "::Execute";

		JSJ_I_110.toLog(conMethodName);

		try {
			Options().CheckMandatory();
			logger.debug(Options().toString());

		}
		catch (Exception e) {
			throw new JobSchedulerException(JSJ_F_107.get(conMethodName) + ": " + e.getMessage(), e);
		}
		finally {
		}

		JSJ_I_111.toLog(conMethodName);
		return this;
	}

	public void init() {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::init";
	}

} // class CreateGenerateOrder