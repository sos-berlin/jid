package com.sos.dailyschedule.job;

import com.sos.JSHelper.Basics.JSToolBox;
import com.sos.JSHelper.Listener.JSListenerClass;
import com.sos.dailyschedule.db.DailyScheduleDBItem;
import com.sos.dailyschedule.db.DailyScheduleDBLayer;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.junit.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * \class 		CreateDaysScheduleJUnitTest - JUnit-Test for "Creating a DaysSchedule depending on actual Runtimes"
 *
 * \brief MainClass to launch CreateDaysSchedule as an executable command-line program
 *

 *
 * see \see C:\Dokumente und Einstellungen\Uwe Risse\Lokale Einstellungen\Temp\scheduler_editor-2235912449518755069.html for (more) details.
 *
 * \verbatim ;
 * mechanicaly created by C:\Dokumente und Einstellungen\Uwe Risse\Eigene Dateien\sos-berlin.com\jobscheduler.1.3.9\scheduler_139\config\JOETemplates\java\xsl\JSJobDoc2JSJUnitClass.xsl from http://www.sos-berlin.com at 20111027105329
 * \endverbatim
 */
public class CreateDailyScheduleJUnitTest extends JSToolBox {
	@SuppressWarnings("unused")
	private final static String				conClassName	= "CreateDaysScheduleJUnitTest";						//$NON-NLS-1$
	@SuppressWarnings("unused")
	private static Logger					logger			= Logger.getLogger(CreateDailyScheduleJUnitTest.class);

	protected CreateDailyScheduleOptions	objOptions		= null;
	private CreateDailySchedule				objE			= null;

	public CreateDailyScheduleJUnitTest() {
		//
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		objE = new CreateDailySchedule();
		objE.registerMessageListener(this);
		objOptions = objE.Options();
		//		objOptions.registerMessageListener(this);

		JSListenerClass.bolLogDebugInformation = true;
		JSListenerClass.intMaxDebugLevel = 9;

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExecute() throws Exception {

		try {
			HashMap pobjHM = new HashMap();
			pobjHM.put("CreateDailyScheduleOptionsSuperClass.scheduler_port", 4210);
			pobjHM.put("CreateDailyScheduleOptionsSuperClass.schedulerHostName", "8of9.sos");
			pobjHM.put("CreateDailyScheduleOptionsSuperClass.dayOffset", 0);
			pobjHM.put("configurationFile","R:/nobackup/junittests/hibernate/hibernate.cfg.xml");


			objE.Options().setAllOptions(pobjHM);
			assertEquals("", objOptions.scheduler_port.value(), 4210);

			objE.Execute();

			DailyScheduleDBLayer d = new DailyScheduleDBLayer(new File("R:/nobackup/junittests/hibernate/hibernate.cfg.xml"));
			d.beginTransaction();

			Query query = d.createQuery(" from DailyScheduleDBItem where job like :test");
			query.setParameter("test", "/sos/dailyschedule/CreateDaysSchedule");
			List calendarList = query.list();

			for (int i = 0; i < calendarList.size(); i++) {
				DailyScheduleDBItem calendarItem = (DailyScheduleDBItem) calendarList.get(i);
				if (i == 0) {
					assertEquals("/sos/dailyschedule/CreateDaysSchedule", calendarItem.getJob());
					break;
				}
//				System.out.println("History: " + calendarItem.getJob());
			}

			d.commit();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

} // class CreateDaysScheduleJUnitTest