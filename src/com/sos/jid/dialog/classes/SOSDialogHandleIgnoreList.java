package com.sos.jid.dialog.classes;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
  
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

 
 
 

public class SOSDialogHandleIgnoreList {
    private static final String LIST_IGNORE_JOBS = "listIgnoreJobs";
    private static final String LIST_DEFAULT_IGNORE_ORDERS = "listDefaultIgnoreOrders";
    private static final String LIST_DEFAULT_IGNORE_JOBS = "listDefaultIgnoreJobs";
    private static final String LIST_IGNORE_ORDERS = "listIgnoreOrders";
    private static final String SOS_DASHBOARD_EXECUTED = "sosDashboardExecuted";

    
    final int               conDefaultPort  = 4444;
 	private Logger			logger			= Logger.getLogger(SOSDialogHandleIgnoreList.class);
	private Text			edSearchField;
	private Button			btnOk;
	 
    private Table           ignoreTable     = null;
    private Shell dialogShell;
    public Timer            inputTimer;
    private Display         display         = null;
    Preferences prefs;
    Composite parent;
    
	public SOSDialogHandleIgnoreList(Shell parentShell,Preferences prefs_) {
	    this.prefs = prefs_;
		execute(parentShell);
	}

	private void execute(Shell parentShell) {
	    inputTimer = new Timer();

		Display display = Display.getDefault();
		Shell shell = showForm(display, parentShell);
		new Label(dialogShell, SWT.NONE);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private Shell showForm(final Display display, Shell parentShell) {
		dialogShell = new Shell(parentShell, SWT.BORDER | SWT.RESIZE);
		dialogShell.setMinimumSize(new Point(300, 500));
		dialogShell.setSize(552, 595);
		dialogShell.setLayout(new GridLayout(2, false));
		

		parent = dialogShell;
        createContent();
		dialogShell.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
			}
		});
		dialogShell.pack();
		new Label(dialogShell, SWT.NONE);
		
		Button btSelectAll = new Button(dialogShell, SWT.NONE);
		btSelectAll.addSelectionListener(new SelectionAdapter() {
		    @Override
		    public void widgetSelected(SelectionEvent e) {
		        selectAll();
		    }
		});
		btSelectAll.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btSelectAll.setText("Select all");
		Button btDeselectAll = new Button(dialogShell, SWT.NONE);
		btDeselectAll.addSelectionListener(new SelectionAdapter() {
		    @Override
		    public void widgetSelected(SelectionEvent e) {
		        deselectAll();
		    }
		});
		btDeselectAll.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		btDeselectAll.setText("Deselect all");
		
		Button btReverse = new Button(dialogShell, SWT.NONE);
		btReverse.addSelectionListener(new SelectionAdapter() {
		    @Override
		    public void widgetSelected(SelectionEvent e) {
		        reverse();
		    }
		});
		btReverse.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btReverse.setText("Reverse");
		new Label(dialogShell, SWT.NONE);
		
		Button btnCancel = new Button(dialogShell, SWT.NONE);
		btnCancel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnCancel.setText("Cancel");
		btnCancel.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                dialogShell.dispose();
            }
        });
		new Label(dialogShell, SWT.NONE);
		
		Button btSaveDefault = new Button(dialogShell, SWT.NONE);
		btSaveDefault.addSelectionListener(new SelectionAdapter() {
		    @Override
		    public void widgetSelected(SelectionEvent e) {
                prefs.node(SOS_DASHBOARD_EXECUTED).put(LIST_DEFAULT_IGNORE_JOBS, getIgnoreFromList(LIST_IGNORE_JOBS));
                prefs.node(SOS_DASHBOARD_EXECUTED).put(LIST_DEFAULT_IGNORE_ORDERS, getIgnoreFromList(LIST_IGNORE_ORDERS));
		    }
		});
		btSaveDefault.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btSaveDefault.setText("Save as Default");
		
		Button btRestore = new Button(dialogShell, SWT.NONE);
		btRestore.addSelectionListener(new SelectionAdapter() {
		    @Override
		    public void widgetSelected(SelectionEvent e) {
		        ignoreTable.clearAll();
		        for (int ii=0;ii<10;ii++) {
    		        for (int i=0;i<ignoreTable.getItemCount();i++) {
    		           final TableItem newItemTableItem = ignoreTable.getItem(i);
    		           newItemTableItem.dispose();
    		        }
		        }
		     
		        addStringToListWithSource(LIST_DEFAULT_IGNORE_JOBS,LIST_IGNORE_JOBS);
		        addStringToListWithSource(LIST_DEFAULT_IGNORE_ORDERS,LIST_IGNORE_ORDERS);
		    }
		});
		btRestore.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btRestore.setText("Restore from Default");
		
		dialogShell.open();
		return dialogShell;
	}
	
	private void addStringToListWithSource(String l, String source) {
        String listOfIgnores = prefs.node(SOS_DASHBOARD_EXECUTED).get(l, "");
        String[] arrayOfIgnores = listOfIgnores.split(",");    
        for (int i=0;i<arrayOfIgnores.length;i++) {
            if (!arrayOfIgnores[i].trim().equals("")){
                final TableItem newItemTableItem = new TableItem(ignoreTable, SWT.BORDER);
                String [] textBuffer = new String[] { arrayOfIgnores[i]};
                newItemTableItem.setText(textBuffer);
                newItemTableItem.setChecked(true);
                newItemTableItem.setData(source);
            }
        }
	    
	}
    private void addStringToList(String l) {
        addStringToListWithSource(l,l);
    }
	
    private void fillTable(){
      if (prefs != null) {
          addStringToList(LIST_IGNORE_JOBS);
          addStringToList(LIST_IGNORE_ORDERS);
      }
   }
    
   public void reverse() {
      for (int i=0;i < ignoreTable.getItemCount();i++) {
             final TableItem newItemTableItem = ignoreTable.getItem(i);
             newItemTableItem.setChecked(!newItemTableItem.getChecked());
     }
   }

   public void setCheckedAll(boolean checked) {
       for (int i=0;i < ignoreTable.getItemCount();i++) {
              final TableItem newItemTableItem = ignoreTable.getItem(i);
              newItemTableItem.setChecked(checked);
      }
    }
   public String getIgnoreFromList(String source) {
       String result = "";
       for (int i=0;i < ignoreTable.getItemCount();i++) {
              final TableItem newItemTableItem = ignoreTable.getItem(i);
              if (newItemTableItem.getChecked() && newItemTableItem.getData().equals(source)){
                  result += "," + newItemTableItem.getText();
              }
       }
       return result;
    }
   
   public void deselectAll() {
       setCheckedAll(false);    }
   
   public void selectAll() {
       setCheckedAll(true);
    }
   
   public void selectWithRegularExpression() {
       for (int i=0;i < ignoreTable.getItemCount();i++) {
           final TableItem newItemTableItem = ignoreTable.getItem(i);
            
           Pattern p = Pattern.compile(edSearchField.getText()); 
           Matcher m = p.matcher(newItemTableItem.getText() ); 
           boolean match = m.find();
           newItemTableItem.setChecked(match);
       }
   }
   
   
   
	private void createContent() {
            
            Label lblRegularExpressionFor = new Label(dialogShell, SWT.NONE);
            lblRegularExpressionFor.setText("Regular expression for search");
            new Label(dialogShell, SWT.NONE);
            edSearchField = new Text(dialogShell, SWT.BORDER);
            GridData gd_edSearchField = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
            gd_edSearchField.widthHint = 150;
            edSearchField.setLayoutData(gd_edSearchField);
            
            edSearchField.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(final ModifyEvent e) {
                if (edSearchField != null) {
                    resetInputTimer();
                }
            }
        });
            
            btnOk = new Button(dialogShell, SWT.NONE);
            GridData gd_btnOk = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
            gd_btnOk.widthHint = 64;
            btnOk.setLayoutData(gd_btnOk);
            btnOk.setText("Ok");
            btnOk.addSelectionListener(new SelectionAdapter() {
            	@Override
            	public void widgetSelected(SelectionEvent e) {
                    prefs.node(SOS_DASHBOARD_EXECUTED).put(LIST_IGNORE_JOBS, getIgnoreFromList(LIST_IGNORE_JOBS));
                    prefs.node(SOS_DASHBOARD_EXECUTED).put(LIST_IGNORE_ORDERS,  getIgnoreFromList(LIST_IGNORE_ORDERS));
            		dialogShell.dispose();
            	}
            });
            dialogShell.setDefaultButton(btnOk);
            
            createTable();
            fillTable();


    }

	private void createTable() {
        
        ignoreTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.CHECK);
        ignoreTable.setHeaderVisible(false);
        ignoreTable.setLinesVisible(true);
        ignoreTable.setLayoutData(new org.eclipse.swt.layout.GridData(SWT.FILL, GridData.FILL, true, true, 1, 10));
        
        TableColumn tblclmnIgnore = new TableColumn(ignoreTable, SWT.NONE);
        tblclmnIgnore.setWidth(300);
        tblclmnIgnore.setResizable(false);
        tableResize();

	}

    private void tableResize() {
        dialogShell.addControlListener(new ControlAdapter() {
            public void controlResized(ControlEvent e) {
                Rectangle area = dialogShell.getClientArea();
                Point size = ignoreTable.computeSize(SWT.DEFAULT, SWT.DEFAULT);
                TableColumn lastColumn = ignoreTable.getColumns()[ignoreTable.getColumnCount() - 1];
                int colWidth = 0;
                for (int i = 0; i < ignoreTable.getColumns().length; i++) {
                    colWidth = colWidth + ignoreTable.getColumns()[i].getWidth();
                }
                colWidth = colWidth - lastColumn.getWidth() - 5;
                ScrollBar vBar = ignoreTable.getVerticalBar();
                int width = area.width - ignoreTable.computeTrim(0, 0, 0, 0).width;
                if (size.y > area.height + ignoreTable.getHeaderHeight()) {
                    Point vBarSize = vBar.getSize();
                    if (vBar.isVisible()) {
                        width -= vBarSize.x;
                    }
                }
                Point oldSize = ignoreTable.getSize();
                if (oldSize.x > area.width) {
                    lastColumn.setWidth(width - colWidth);
                    ignoreTable.setSize(area.width, area.height);
                } else {
                    ignoreTable.setSize(area.width, area.height);
                    lastColumn.setWidth(width - colWidth);
                }
            }
        });
    }
	
	  public class InputTask extends TimerTask {
          private static final String EMPTYSTRING = "";

        @Override
          public void run() {
              if (display == null) {
                  display = Display.getDefault();
              }
              display.syncExec(new Runnable() {
                  @Override
                  public void run() {
                      if (!edSearchField.equals(EMPTYSTRING)) {
                          try {
                                selectWithRegularExpression();
                          }
                          catch (Exception e) {
                              e.printStackTrace();
                          }
                          inputTimer.cancel();
                      }
                  };
              });
          }
      }

      private void resetInputTimer() {
          inputTimer.cancel();
          inputTimer = new Timer();
          inputTimer.schedule(new InputTask(), 1 * 1000, 1 * 1000);
      }

 
}
