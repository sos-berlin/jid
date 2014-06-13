package com.sos.schedulerinstances.classes;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.sos.dashboard.globals.DashBoardConstants;
import com.sos.dialog.components.SOSTableColumn;
import com.sos.hibernate.classes.DbItem;
import com.sos.localization.Messages;
import com.sos.scheduler.db.SchedulerInstancesDBItem;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

 
public class SelectSchedulerInstance {

  private Logger        logger      = Logger.getLogger(SelectSchedulerInstance.class);
  private List<SchedulerInstancesDBItem>   listOfSchedulerInstancesDBItems   = null;
  private Table tableList;
  private SchedulerInstancesDBItem selectedSchedulerInstancesDBItem=null;
  private Composite mainViewComposite;
  public SelectSchedulerInstance(Shell parentShell,List<SchedulerInstancesDBItem> listOfSchedulerInstancesDBItems) {
      this.listOfSchedulerInstancesDBItems =listOfSchedulerInstancesDBItems;
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
  
  public void fillTable(Table table) {
      Iterator<SchedulerInstancesDBItem> schedulerInstancesEntry = listOfSchedulerInstancesDBItems.iterator();
      while (schedulerInstancesEntry.hasNext()) {
          DbItem h = schedulerInstancesEntry.next();
          
              final SosSchedulerInstancesTableItem newItemTableItem = new SosSchedulerInstancesTableItem(table, SWT.BORDER);
              newItemTableItem.setDBItem(h);
              newItemTableItem.setData(h);
              newItemTableItem.setColor();
              newItemTableItem.setColumnsShort();
           
      }
  }
  
  private void buildTable(Shell shell) {
      
      Messages messages = new Messages(DashBoardConstants.conPropertiesFileName);

     
      mainViewComposite.setLayout(new GridLayout(2, false));
      
      tableList = new Table(mainViewComposite, SWT.FULL_SELECTION );
      GridData gd_tableList_1 = new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1);
      gd_tableList_1.widthHint = 421;
      tableList.setLayoutData(gd_tableList_1);
      tableList.setLayoutData(gd_tableList_1);
      new SOSTableColumn(tableList, SWT.NONE, messages.getLabel(DashBoardConstants.conSOSDashB_SchedulerID), 50);
      new SOSTableColumn(tableList, SWT.NONE, messages.getLabel(DashBoardConstants.conSOSDashB_Hostname), 100);
      new SOSTableColumn(tableList, SWT.NONE, messages.getLabel(DashBoardConstants.conSOSDashB_TCP_PORT), 100);
      new SOSTableColumn(tableList, SWT.NONE, messages.getLabel(DashBoardConstants.conSOSDashB_UDP_PORT), 100);
      
      fillTable(tableList);
 
      
      tableList.addSelectionListener(new SelectionAdapter() {
          public void widgetSelected(final SelectionEvent e) {
             if (tableList.getSelectionIndex() >= 0) {
                 TableItem t = tableList.getItem(tableList.getSelectionIndex());
                 if (t != null) {
                     selectedSchedulerInstancesDBItem = (SchedulerInstancesDBItem) t.getData();
                    }
                  }
               
          }
      });

      
      

  }

  private Shell showForm(final Display display, Shell parentShell) {
    final Shell dialogShell = new Shell(parentShell, SWT.PRIMARY_MODAL | SWT.SHEET);
    dialogShell.setSize(247, 187);
     
    mainViewComposite = new Composite(dialogShell, SWT.NONE);

     dialogShell.setLayout(new GridLayout(2, false));
    
    buildTable(dialogShell);
    
    Button btnOkButton = new Button(mainViewComposite, SWT.NONE);
    btnOkButton.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            if (selectedSchedulerInstancesDBItem != null) {
                dialogShell.dispose();
            }
        }
    });
    GridData gd_btnOkButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_btnOkButton.widthHint = 54;
    btnOkButton.setLayoutData(gd_btnOkButton);
    btnOkButton.setText("Ok");
    
    Button btnCancelButton = new Button(mainViewComposite, SWT.NONE);
    btnCancelButton.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            dialogShell.dispose();
        }
    });
    GridData gd_btnCancelButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_btnCancelButton.widthHint = 54;
    btnCancelButton.setLayoutData(gd_btnCancelButton);
    btnCancelButton.setText("Cancel");
    
    dialogShell.addDisposeListener(new DisposeListener() {
      @Override
      public void widgetDisposed(DisposeEvent e) {
      }
    });
    dialogShell.pack();
    
    dialogShell.open();
    return dialogShell;
  }

public SchedulerInstancesDBItem getSelectedSchedulerInstancesDBItem() {
    return selectedSchedulerInstancesDBItem;
}
}
