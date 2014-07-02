/**
 * 
 */
package com.sos.dashboard.globals;

/**
 * @author KB
 *
 */
public class DashBoardConstants {
	/**
	 * 
	 */ 
    
    public static final String SOS_DASHBOARD = "SOS_DASHBOARD";

    
    public final static String  conPropertiesFileName               = "com.sos.dailyschedule.SOSSchedulerDashboardMain";

    
    public static final String  conSOSDashB_skipUnskip              = "SOSDashB_skipUnskip";            // "Skip/Unskip";

	public final static String	conSOSDashB_Message					= "SOSDashB_Message";			    // "Message";
	public static final String	STATUS_EXECUTED						= "executed";
	public static final String	STATUS_WAITING						= "waiting";
	public static final String	ORDER_CRITERIA						= "startTime";
	public static final String	SORT_MODE							= "desc";


	public static final int     STATUS_NOT_ASSIGNED                      = 0;
	public static final int     STATUS_ASSIGNED                          = 1;
	public static final int     STATUS_NOT_ASSIGNED_SUSPENDED            = 2;

    
    public static final String conSOSJobnet_Jobnet_jobchain_name = "jobnet/creator/jobnet_plan_creator";
    public static final int    conSOSDashB_Right_Mouse_Button = 3;

    public static final String conSOSJobnet_Kill = "SOSJobnet_Kill";
    public static final String conSOSJobnet_Abort = "SOSJobnet_Abort";
    public static final String conSOSJobnet_Restart_Ignore_Error = "SOSJobnet_Restart_Ignore_Error";
    public static final String conSOSJobnet_Start_On_Demand = "SOSJobnet_Start_On_Demand";
    public static final String conSOSJobnet_Restart_Error = "SOSJobnet_Restart_Error";
    public static final String conSOSJobnet_Repeat = "SOSJobnet_Repeat";
    public static final String conSOSJobnet_Start_Independent = "SOSJobnet_Start_Independent";
    public static final String conSOSJobnet_filter = "SOSJobnet_filter";
    public static final String conSOSJobnet_refresh = "SOSJobnet_refresh";
    public static final String conSOSJobnet_collapse = "SOSJobnet_collapse";
    public static final String conSOSJobnet_expand = "SOSJobnet_expand";
 

	
	public static final String	conSOSDashB_disableIgnore			= "SOSDashB_DisableIgnore";		      // "Disable ignore";
	public static final String	conSOSDashB_enableIgnore			= "SOSDashB_EnableIgnore";		      // "Enable ignore";
    public static final String  conSOSDashB_stepHistory             = "SOSDashB_StepHistory";             // "Step History";
    public static final String  conSOSDashB_resetIgnore             = "SOSDashB_ResetIgnore";             // "Reset ignore";
	public static final String	conTabLOG							= "Log";
    public static final String  conSOSDashB_addIgnore               = "SOSDashB_AddIgnore";               // "Add to ignore";
    public static final String  conSOSDashB_handleIgnore            = "SOSDashB_HandleIgnore";               // "Add to ignore";
    public static final String  conSOSDashB_setLimit                = "SOSDashB_SetLimit";                // "set limit";
 	public static final String	conSOSDashB_addIgnoreJobChainOrder	= "SOSDashB_AddIgnoreJobChainOrder";  // "Add to ignore JobChain and Order";
 	public static final String  conSOSDashB_show_with_error         = "SOSDashB_only_with_error";         // "Show with error";
 	public static final String  conSOSDashB_show_running            = "SOSDashB_only_running";            // "Show running";
 	public static final String  conSOSDashB_show_successfull        = "SOSDashB_only_successfull";        // "Show successfull";
    public static final String  conSOSDashB_show_stand_alone_jobs   = "SOSDashB_only_stand_alone_jobs";   // "Show stand alone jobs";
	public static final String	conSOSDashB_show_job_chains			= "SOSDashB_only_job_chains";		  // "Show job chains";
	public static final String	conSOSDashB_show_late				= "SOSDashB_only_late";			      // "Show late";
	public static final String	conSOSDashB_show_waiting			= "SOSDashB_only_waiting";			  // "Show waiting";
    public static final String  conSOSDashB_start_now               = "SOSDashB_start_now";               // "Start now";
    public static final String  conSOSDashB_start_on_demand         = "SOSDashB_start_on_demand";         // "Start on demand";
    public static final String  conSOSDashB_start_at_runtime        = "SOSDashB_start_at_runtime";        // "Start at runtime";
	public static final String	conSOSDashB_Today					= "SOSDashB_Today";				      // "Today";
    public static final String  conSOSDashB_Reset                   = "SOSDashB_Reset";                   // "Reset";
    public static final String  conSOSDashB_Reset_Filter            = "SOSDashB_Reset_Filter";            // "Reset Filter";
	public static final String	conSOSDashB_only_executed			= "SOSDashB_only_executed";		      // "only executed"
	public static final String	conSOSDashB_show_log_in_new_tab		= "SOSDashB_show_log_in_new_tab";	  // "Show log in a new tab";
	public static final String	conSOSDashB_open_scheduler			= "SOSDashB_open_scheduler";		  // "Open Scheduler";
	public static final String	conSOSDashB_close					= "SOSDashB_close";				      // "close";
	public static final String	conSOSDashB_new_log					= "SOSDashB_new_log";				  // "New Log";
    public static final String  conSOSDashB_NAME_TAB_HISTORY        = "SOSDashB_NAME_TAB_HISTORY";
    public static final String  conSOSDashB_NAME_TAB_STEP_HISTORY   = "SOSDashB_NAME_TAB_STEP_HISTORY";
	public static final String	conSOSDashB_NAME_TAB_PLANNED		= "SOSDashB_NAME_TAB_PLANNED";
    public static final String  conSOSDashB_NAME_TAB_SCHEDULER_INSTANCES     = "SOSDashB_NAME_TAB_SCHEDULER_INSTANCES";
	public static final String	conSOSDashB_NAME_TAB_JOBNET			= "SOSDashB_NAME_TAB_JOBNET";

	
	public static final String	conSOSDashB_FROM					= "SOSDashB_FROM";
	public static final String	conSOSDashB_TO						= "SOSDashB_TO";
	public final static String	conSOSDashB_JOBS					= "SOSDashB_JOBS";
	public final static String	conSOSDashB_JOBCHAINS				= "SOSDashB_JOBCHAINS";
	public final static String	conSOSDashB_LATE					= "SOSDashB_LATE";
	public final static String	conSOSDashB_Refresh					= "SOSDashB_Refresh";
	public final static String	conSOSDashB_SchedulerID				= "SOSDashB_SchedulerID";
	public final static String	conSOSDashB_JOB						= "SOSDashB_JOB";
	public final static String	conSOSDashB_JOBCHAIN				= "SOSDashB_JOBCHAIN";
	public final static String	conSOSDashB_ORDER					= "SOSDashB_ORDER";
	public final static String	conSOSDashB_START					= "SOSDashB_START";
	public final static String	conSOSDashB_END						= "SOSDashB_END";
	public final static String	conSOSDashB_DURATION				= "SOSDashB_DURATION";
    public final static String  conSOSDashB_EXIT                    = "SOSDashB_EXIT";
    public final static String  conSOSDashB_STATE                   = "SOSDashB_STATE";
    public final static String  conSOSDashB_STEP                    = "SOSDashB_STEP";
    public final static String  conSOSDashB_ERROR                   = "SOSDashB_ERROR";
    public final static String  conSOSDashB_ERROR_CODE              = "SOSDashB_ERROR_CODE";
    public final static String  conSOSDashB_ERROR_TEXT              = "SOSDashB_ERROR_TEXT";
	public static final String	conSOSDashB_ShowIgnored				= "SOSDashB_ShowIgnored";			// "%1s Jobs %2s Orders ignored";
	public final static String	conSOSDashB_Planned					= "SOSDashB_Planned";
	public final static String	conSOSDashB_Executed				= "SOSDashB_Executed";
	public final static String	conSOSDashB_Status					= "SOSDashB_Status";
    public final static String  conSOSDashB_JobNet                  = "SOSDashB_JobNet";
    public final static String  conSOSDashB_CouldNotConnect         = "SOSDashB_CouldNotConnect";
    public final static String  conSOSDashB_NoSecurityServer        = "SOSDashB_NoSecurityServer";
    public final static String  conSOSDashB_Copy                    = "SOSDashB_Copy";
    public final static String  conSOSDashB_SelectAll               = "SOSDashB_SelectAll";
    public final static String  conSOSDashB_SelectFont              = "SOSDashB_SelectFont";
    public final static String  conSOSDashB_Filter                  = "SOSDashB_Filter";
    public final static String  conSOSDashB_Search                  = "SOSDashB_Search";
    public final static String  conSOSDashB_SaveAsFile              = "SOSDashB_SaveAsFile";
    public final static String  conSOSDashB_Print                   = "SOSDashB_Print";
    public final static String  conSOSDashB_Export_To_Excel         = "SOSDashB_ExportToExcel";
    
    public static final String  conSettingREFRESHDefault            = "60";
    public static final Integer conSettingLIMITDefault              = 500;
    public static final String  SOS_DASHBOARD_HEADER                = "sosDashboardHeader";
    public static final String  conSettingREFRESH                   = "refresh";
    public static final String  conSettingLIMIT                     = "limit";

    public static final String  conSOSDashB_HOSTNAME                   = "SOSDashB_Hostname";
    public static final String  conSOSDashB_TCP_PORT                   = "SOSDashB_TCP_PORT";
    public static final String  conSOSDashB_SUPERVISOR_HOSTNAME        = "SOSDashB_Supervisor_Hostname";
    public static final String  conSOSDashB_SUPERVISOR_TCP_PORT        = "SOSDashB_Supervisor_TCP_PORT";
    public static final String  conSOSDashB_UDP_PORT                   = "SOSDashB_UDP_PORT";
    public static final String  conSOSDashB_START_TIME                 = "SOSDashB_START_TIME";
    public static final String  conSOSDashB_STOP_TIME                  = "SOSDashB_STOP_TIME";
    public static final String  conSOSDashB_DB_NAME                    = "SOSDashB_DB_NAME";
    public static final String  conSOSDashB_DB_HISTORY_TABLENAME       = "SOSDashB_DB_HISTORY_TABLENAME";
    public static final String  conSOSDashB_DB_ORDERHISTORY_TABLENAME  = "SOSDashB_DB_ORDERHISTORY_TABLENAME";
    public static final String  conSOSDashB_DB_ORDERS_TABLENAME        = "SOSDashB_DB_ORDERS_TABLENAME";
    public static final String  conSOSDashB_DB_TASKS_TABLENAME         = "SOSDashB_DB_TASKS_TABLENAME";
    public static final String  conSOSDashB_DB_VARIABLES_TABLENAME     = "SOSDashB_DB_VARIABLES_TABLENAME";
    public static final String  conSOSDashB_WORKING_DIRECTORY          = "SOSDashB_WORKING_DIRECTORY";
    public static final String  conSOSDashB_LIVE_DIRECTORY             = "SOSDashB_LIVE_DIRECTORY";
    public static final String  conSOSDashB_LOGDIR                     = "SOSDashB_LOGDIR";
    public static final String  conSOSDashB_INCLUDEPATH                = "SOSDashB_INCLUDEPATH";
    public static final String  conSOSDashB_INIPATH                    = "SOSDashB_INIPATH";
    public static final String  conSOSDashB_IS_SERVICE                 = "SOSDashB_IS_SERVICE";
    public static final String  conSOSDashB_IS_RUNNING                 = "SOSDashB_IS_RUNNING";
    public static final String  conSOSDashB_IS_PAUSED                  = "SOSDashB_IS_PAUSED";
    public static final String  conSOSDashB_IS_CLUSTER                 = "SOSDashB_IS_CLUSTER";
    public static final String  conSOSDashB_IS_AGENT                   = "SOSDashB_IS_AGENT";
    public static final String  conSOSDashB_PARAM                      = "SOSDashB_PARAM";
    public static final String  conSOSDashB_JETTY_HTTP_PORT            = "SOSDashB_JETTY_HTTP_PORT";
    public static final String  conSOSDashB_JETTY_HTTPS_PORT           = "SOSDashB_JETTY_HTTPS_PORT";
    public static final String  conSOSDashB_IS_COMMAND_WEBSERVICE      = "SOSDashB_IS_COMMAND_WEBSERVICE";
    
     
	private DashBoardConstants() {
		// TODO Auto-generated constructor stub
	}
}
