

package com.sos.dailyschedule.job;

import java.util.Date;

import org.apache.log4j.Logger;

import com.sos.JSHelper.Basics.JSJobUtilitiesClass;
import com.sos.dailyschedule.db.DailyScheduleAdjustment;

public class CheckDailySchedule extends JSJobUtilitiesClass <CheckDailyScheduleOptions> {
	private final String					conClassName						= "CheckDailySchedule";  //$NON-NLS-1$
	private static Logger		logger			= Logger.getLogger(CheckDailySchedule.class);

	public CheckDailySchedule() {
		super(new CheckDailyScheduleOptions());
	}

	@Override
	public CheckDailyScheduleOptions getOptions() {
		if (objOptions == null) {
			objOptions = new CheckDailyScheduleOptions();
		}
		return objOptions;
	}

	public CheckDailySchedule Execute() throws Exception {
		try {
			getOptions().CheckMandatory();
			logger.debug(getOptions().dirtyString());
			DailyScheduleAdjustment dailyScheduleAdjustment = new DailyScheduleAdjustment(objOptions.configuration_file.Value());
			dailyScheduleAdjustment.setOptions(objOptions);
			//It is not neccessary to check into the future.
			dailyScheduleAdjustment.setTo(new Date());
 			dailyScheduleAdjustment.adjustWithHistory();
		} catch (Exception e) {
			e.printStackTrace(System.err);
			throw new Exception (e);
		}
		return this;
	}

}  // class CheckDailySchedule