package com.sos.dailyschedule.dialog.classes;
import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.sos.dashboard.globals.SOSDashboardOptions;
import com.sos.localization.Messages;
import com.sos.scheduler.history.SchedulerHistoryDataProvider;
import com.sos.scheduler.history.SchedulerOrderStepHistoryDataProvider;
import com.sos.scheduler.history.db.SchedulerOrderHistoryDBItem;

import org.eclipse.swt.graphics.Point;

public class SOSDialogOrderStepHistory {
    private Logger        logger      = Logger.getLogger(SOSDialogOrderStepHistory.class);
    private Button        btnOk;
    private String        protocolValue;
    private SOSDashboardOptions objOptions;
    private SchedulerOrderHistoryDBItem schedulerOrderHistoryDBItem;
    private CTabFolder logTabFolder = null;
    private Messages messages;
    private String timeZone; 
   
  
  public SOSDialogOrderStepHistory(Shell parentShell, SOSDashboardOptions objOptions_, SchedulerOrderHistoryDBItem schedulerOrderHistoryDBItem_,CTabFolder logTabFolder_, Messages messages_, String timeZone_) {
    objOptions = objOptions_;
    messages = messages_;
    timeZone=timeZone_;
    schedulerOrderHistoryDBItem = schedulerOrderHistoryDBItem_;
    logTabFolder = logTabFolder_;
    execute(parentShell);
  }

  private void execute(Shell parentShell) {
    Display display = Display.getDefault();
    Shell shell = showForm(display, parentShell);
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
  }

  private Shell showForm(final Display display, Shell parentShell) {
    final Shell dialogShell = new Shell(parentShell, SWT.CLOSE | SWT.TITLE | SWT.BORDER | SWT.OK | SWT.PRIMARY_MODAL);
    dialogShell.setMinimumSize(new Point(340, 29));
    dialogShell.setLayout(new GridLayout(6, false));

    SchedulerHistoryDataProvider historyDataProvider = new SchedulerHistoryDataProvider(new File(objOptions.getConfigurationFile().Value()));
    historyDataProvider.setTimeZone(timeZone);
    
    SosSchedulerOrderStepHistoryTable tableStepHistoryDetail = new SosSchedulerOrderStepHistoryTable(dialogShell, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI, messages);    
    tableStepHistoryDetail.setLogTabFolder(logTabFolder);
    tableStepHistoryDetail.setDetailHistoryDataProvider(historyDataProvider);
    SchedulerOrderStepHistoryDataProvider schedulerOrderStepHistoryDataProvider = new SchedulerOrderStepHistoryDataProvider(new File(objOptions.getConfigurationFile().Value()),schedulerOrderHistoryDBItem.getHistoryId());
    schedulerOrderStepHistoryDataProvider.setTimeZone(timeZone);
    schedulerOrderStepHistoryDataProvider.getData(0);
    schedulerOrderStepHistoryDataProvider.fillTable(tableStepHistoryDetail);
   
    dialogShell.setSize(295, 170);
    
    btnOk = new Button(dialogShell, SWT.NONE);
    GridData gd_btnOk = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_btnOk.widthHint = 64;
    btnOk.setLayoutData(gd_btnOk);
    btnOk.setText("Ok");
    btnOk.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        dialogShell.dispose();
      }
    });
    dialogShell.setDefaultButton(btnOk);
    dialogShell.addDisposeListener(new DisposeListener() {
      @Override
      public void widgetDisposed(DisposeEvent e) {
      }
    });
    dialogShell.pack();
    
    Button btnCancel = new Button(dialogShell, SWT.NONE);
    btnCancel.setText("Cancel");
    new Label(dialogShell, SWT.NONE);
    btnCancel.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                dialogShell.dispose();
            }
        });
    
    dialogShell.open();
    return dialogShell;
  }

    
    
    public boolean cancel() {
        return (protocolValue==null);
    }
}
