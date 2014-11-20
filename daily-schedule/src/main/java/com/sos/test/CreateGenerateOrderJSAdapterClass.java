

package com.sos.test;

import org.apache.log4j.Logger;

import sos.scheduler.job.JobSchedulerJobAdapter;

import com.sos.JSHelper.Exceptions.JobSchedulerException;
// Super-Class for JobScheduler Java-API-Jobs
/**
 * \class 		CreateGenerateOrderJSAdapterClass - JobScheduler Adapter for "Creating PDIR generate order"
 *
 * \brief AdapterClass of CreateGenerateOrder for the SOSJobScheduler
 *
 * This Class CreateGenerateOrderJSAdapterClass works as an adapter-class between the SOS
 * JobScheduler and the worker-class CreateGenerateOrder.
 *

 *
 *
 * \verbatim ;
 * mechanicaly created by C:\ProgramData\sos-berlin.com\jobscheduler\scheduler_ur\config\JOETemplates\java\xsl\JSJobDoc2JSAdapterClass.xsl from http://www.sos-berlin.com at 20130225111725
 * \endverbatim
 */
public class CreateGenerateOrderJSAdapterClass extends JobSchedulerJobAdapter  {
	private final String					conClassName						= "CreateGenerateOrderJSAdapterClass";
	private static Logger		logger			= Logger.getLogger(CreateGenerateOrderJSAdapterClass.class);

	public void init() {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::init";
		doInitialize();
	}

	private void doInitialize() {
	} // doInitialize

	@Override
	public boolean spooler_init() {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::spooler_init";
		return super.spooler_init();
	}

	@Override
	public boolean spooler_process() throws Exception {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::spooler_process";

		try {
			super.spooler_process();
			doProcessing();
		}
		catch (Exception e) {
            throw new JobSchedulerException("Fatal Error:" + e.getMessage(), e);
   		}
		finally {
		} // finally
        return signalSuccess();

	} // spooler_process

	@Override
	public void spooler_exit() {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::spooler_exit";
		super.spooler_exit();
	}

	private void doProcessing() throws Exception {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::doProcessing";

		CreateGenerateOrder objR = new CreateGenerateOrder();
		CreateGenerateOrderOptions objO = objR.Options();

        objO.CurrentNodeName(this.getCurrentNodeName());
		objO.setAllOptions(getSchedulerParameterAsProperties(getJobOrOrderParameters()));
		objO.CheckMandatory();
        objR.setJSJobUtilites(this);
		objR.Execute();
	} // doProcessing

}

