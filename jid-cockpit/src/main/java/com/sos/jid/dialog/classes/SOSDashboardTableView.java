package com.sos.jid.dialog.classes;

import java.io.File;
import java.util.prefs.Preferences;
 
import com.sos.dashboard.globals.DashBoardConstants;
import com.sos.dashboard.globals.SOSDashboardOptions;
import com.sos.dialog.classes.SOSTable;
import com.sos.dialog.comparators.DateComperator;
import com.sos.dialog.comparators.SortBaseComparator;
import com.sos.dialog.comparators.StringComparator;
import com.sos.dialog.components.SOSTableColumn.ColumnType;
import com.sos.dialog.interfaces.ITableView;
import com.sos.hibernate.classes.DbItem;
import com.sos.hibernate.classes.SosSortTableItem;
import com.sos.hibernate.interfaces.ISOSDashboardDataProvider;
import com.sos.hibernate.interfaces.ISOSTableItem;
import com.sos.scheduler.db.SchedulerInstancesDBItem;
import com.sos.scheduler.db.SchedulerInstancesDBLayer;
import com.sos.scheduler.history.SchedulerHistoryDataProvider;
import com.sos.scheduler.history.db.SchedulerOrderHistoryDBLayer;
import com.sos.scheduler.history.db.SchedulerTaskHistoryDBLayer;
import com.sos.scheduler.model.SchedulerObjectFactory;
import com.sos.scheduler.model.commands.JSCmdModifyOrder;
import com.sos.scheduler.model.commands.JSCmdStartJob;
import com.sos.scheduler.model.objects.Spooler;
 
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Listener;

public class SOSDashboardTableView extends SOSDashboardMainView implements ITableView {
     
	private static Logger logger = Logger.getLogger(SOSDashboardTableView.class);
	protected SOSTable tableList = null;
	protected ISOSDashboardDataProvider tableDataProvider = null;
	private SortBaseComparator[][] comparables = null;
	private            String answer = "";
	private Integer historyLimit=0;


	protected SchedulerOrderHistoryDBLayer schedulerOrderHistoryDBLayer = null;
	protected SchedulerTaskHistoryDBLayer schedulerTaskHistoryDBLayer = null;
	protected SchedulerInstancesDBLayer schedulerInstancesDBLayer;
    

	public SOSDashboardTableView(Composite composite_) {
		super(composite_);
	}

	
	
	@Override
	public void getTableData() {
		logger.debug("...getTableData");
        this.showWaitCursor();
		if (tableList != null) {

			tableDataProvider.getData(getLimit());
			buildTable();
		}
        this.resetCursor();

	}

	@Override
	public void buildTable() {
		this.showWaitCursor();
		if (tableList != null) {
		    tableList.setRedraw(false);
			if (tableDataProvider.getFilter() != null && left != null) {
				left.setText(tableDataProvider.getFilter().getTitle());
			}
			clearTable(tableList);
			tableDataProvider.fillTable(tableList);
			SosSortTableItem sosSortTableItem = null;
			sosSortTableItem = null;
			int ll = tableList.getItemCount();
			comparables = new SortBaseComparator[tableList.getColumnCount()][ll];
			for (int i = 0; i < tableList.getItemCount(); i++) {
				for (int k = 0; k < tableList.getColumnCount(); k++) {
					sosSortTableItem = new SosSortTableItem((ISOSTableItem) tableList.getItems()[i]);
					if (this.tableList.getSOSTableColumn(k).getColumnType() == ColumnType.DATE) {
						comparables[k][i] = new DateComperator(sosSortTableItem, i, k);
					} else {
						comparables[k][i] = new StringComparator(sosSortTableItem, i, k);
					}
				}
			}
			sortTable(tableList, comparables);
	        tableList.setRedraw(true);
		}
		this.resetCursor();
	}

	@Override
	public void createTable() {
		sosDashboardHeader.getCbSchedulerId().addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				tableDataProvider.setSchedulerId(sosDashboardHeader.getCbSchedulerId().getText());
				actualizeList();
			}
		});
		sosDashboardHeader.getToDate().addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				tableDataProvider.setTo(sosDashboardHeader.getTo());
				actualizeList();
			}
		});
		sosDashboardHeader.getFromDate().addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				tableDataProvider.setFrom(sosDashboardHeader.getFrom());
				actualizeList();
			}
		});
		sosDashboardHeader.getRefreshInterval().addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent arg0) {
				sosDashboardHeader.setRefresh(getIntValue(sosDashboardHeader.getRefreshInterval().getText(), 10));
				sosDashboardHeader.resetRefreshTimer();
				prefs.node(DashBoardConstants.SOS_DASHBOARD_HEADER).put(DashBoardConstants.conSettingREFRESH, sosDashboardHeader.getRefreshInterval().getText());

			}
		});
        sosDashboardHeader.getRefreshInterval().setText(prefs.node(DashBoardConstants.SOS_DASHBOARD_HEADER).get(DashBoardConstants.conSettingREFRESH, DashBoardConstants.conSettingREFRESHDefault));
	 	sosDashboardHeader.getSearchField().addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if (sosDashboardHeader.getSearchField() != null) {
					tableDataProvider.setSearchField(sosDashboardHeader.getSosSearchFilterData());
					actualizeList();
					//sosDashboardHeader.resetInputTimer();
				}
			}
		});
		 
		sosDashboardHeader.getRefreshButton().addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				getList();
				getSchedulerIds();
			}
		});
		
		tableList.addListener(SWT.MouseDown, new Listener() {
            @Override
            public void handleEvent(final Event event) {
                if (event.button == RIGHT_MOUSE_BUTTON) // rechte maustaste
                {
                    setRightMausclick(true);
                } else {
                    setRightMausclick(false);
                }
            }
        });
		
		tableList.addKeyListener( new KeyListener() {
            public void keyPressed(final KeyEvent e) {
                final int code = e.keyCode;
                System.out.println("pressed: " + (char)code);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                 
            }
 
          
        });
		
		
			tableList.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (right != null && !isRightMouseclick()) {
				    if (tableList.getSelectionIndex() >= 0) {
    					TableItem t = tableList.getItem(tableList.getSelectionIndex());
    					if (t != null) {
        					showLog(tableList);
        					DbItem d = (DbItem) t.getData();
        					right.setText(d.getTitle());
        					detailHistoryDataProvider.setFrom(sosDashboardHeader.getFrom());
        					detailHistoryDataProvider.setTo(sosDashboardHeader.getTo());
        					detailHistoryDataProvider.setSchedulerId(d.getSchedulerId());
        					detailHistoryDataProvider.setJobname(d.getJob());
        					detailHistoryDataProvider.setJobchain(d.getJobChain());
        					detailHistoryDataProvider.setOrderid(d.getOrderId());
        					detailHistoryDataProvider.getData(getHistoryLimit());
        					clearTable(tableHistoryDetail);
        					detailHistoryDataProvider.fillTableShort(tableHistoryDetail, d.isStandalone());
    					}
				    }
				}
			}
		});

		this.setColumnsListener();
		this.tableResize();
	}
	


	private void tableResize() {
		mainViewComposite.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Rectangle area = mainViewComposite.getClientArea();
				Point size = tableList.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				TableColumn lastColumn = tableList.getColumns()[tableList.getColumnCount() - 1];
				int colWidth = 0;
				for (int i = 0; i < tableList.getColumns().length; i++) {
					colWidth = colWidth + tableList.getColumns()[i].getWidth();
				}
				colWidth = colWidth - lastColumn.getWidth() - 5;
				ScrollBar vBar = tableList.getVerticalBar();
				int width = area.width - tableList.computeTrim(0, 0, 0, 0).width;
				if (size.y > area.height + tableList.getHeaderHeight()) {
					Point vBarSize = vBar.getSize();
					if (vBar.isVisible()) {
						width -= vBarSize.x;
					}
				}
				Point oldSize = tableList.getSize();
				if (oldSize.x > area.width) {
					lastColumn.setWidth(width - colWidth);
					tableList.setSize(area.width, area.height);
				} else {
					tableList.setSize(area.width, area.height);
					lastColumn.setWidth(width - colWidth);
				}
			}
		});
	}

	public void setColumnsListener() {
		TableColumn[] columns = tableList.getColumns();
		
		for (int i = 0; i < columns.length; i++) {
			final int _i = i;
 			columns[i].addListener(SWT.Selection, new Listener() {
				private int colPos = -1;
				private boolean sortFlag;
				{
					colPos = _i;
				}

				public void handleEvent(Event event) {
					sortFlag = !sortFlag;
					tableList.setSortColumn(tableList.getColumn(colPos));
					if (sortFlag) {
						tableList.setSortDirection(SWT.UP);
					} else {
						tableList.setSortDirection(SWT.DOWN);
					}
					colPosForSort = colPos;
					colSortFlag = sortFlag;
					sortTable(tableList, comparables);
				}
			});
		}
	}

	
	
	   
	   protected SchedulerInstancesDBItem start(DbItem dbItem) {
	        this.showWaitCursor();
  
	        SchedulerInstancesDBItem schedulerInstanceDBItem = schedulerInstancesDBLayer.getInstanceById(dbItem.getSchedulerId());
	        if (schedulerInstanceDBItem != null) {
	            SchedulerObjectFactory objSchedulerObjectFactory = new SchedulerObjectFactory(schedulerInstanceDBItem.getHostName(), schedulerInstanceDBItem.getTcpPort());
	            objSchedulerObjectFactory.initMarshaller(Spooler.class);
	            if (dbItem.isOrderJob()) {
	                try {
	                    JSCmdModifyOrder objOrder = objSchedulerObjectFactory.StartOrder(dbItem.getJobChain(), dbItem.getOrderId(), false);
	                    if (objOrder.getAnswer().getOk() != null){
	                        answer += String.format("Command: Start Order: %s %s: %s", dbItem.getJobChain(), dbItem.getOrderId(),"OK") + "\n";
	                    }else {
	                        answer += String.format("Command: Start Order %s %s: %s", dbItem.getJobChain(), dbItem.getOrderId(),objOrder.getAnswer().getERROR())+ "\n";
	                   }
 	                } catch (Exception ee) {
   	                    answer += String.format("Command: Start Order %s %s: %s", dbItem.getJobChain(), dbItem.getOrderId(),ee.getMessage())+ "\n";
 	                    ee.printStackTrace();
	                } finally {
	                    this.RestoreCursor();
	                }

	            } else {
	                try {
	                    JSCmdStartJob objStartJob = objSchedulerObjectFactory.StartJob(dbItem.getJobName(), false);
                        if (objStartJob.getAnswer().getOk() != null){
                            answer += String.format("Command: Start Job: %s: %s", dbItem.getJobName(),"OK")+ "\n";
                        }else {
                            answer += String.format("Command: Start Job  %s: %s",dbItem.getJobName(),objStartJob.getAnswer().getERROR())+ "\n";
                       }
	                }
	                catch (Exception ee) {
                        answer += String.format("Command: Start Job  %s: %s --> Error: %s", dbItem.getJobName(), dbItem.getOrderId(),ee.getMessage())+ "\n";
	                    ee.printStackTrace();
	                } finally {
	                    this.RestoreCursor();
	                }
	            }
	        }
	     return schedulerInstanceDBItem;
	    }
	
	protected void showLog(Table table) {
		this.showWaitCursor();
		if (logTabFolder != null && table.getSelectionIndex() >= 0 && table.getSelectionIndex() >= 0) {
			SosTabLogItem logItem = (SosTabLogItem) logTabFolder.getSelection();
			if (logItem == null) {
				logTabFolder.setSelection(0);
				logItem = (SosTabLogItem) logTabFolder.getSelection();
			}
			TableItem t = table.getItem(table.getSelectionIndex());
			DbItem d = (DbItem) t.getData();
			logItem.addLog(table, d.getTitle(), detailHistoryDataProvider.getLogAsString(d));
		}
		this.RestoreCursor();
	}

	@Override
	public void getList() {
		logger.debug("...getList");
		if (tableList != null && tableDataProvider != null) {
	        int i = tableList.getTopIndex();
	        
	        tableDataProvider.getData(getLimit());
 			buildTable();
			tableList.setTopIndex(i);		
			}
	}

	public void getSchedulerIds() {
		logger.debug("...getSchedulerIds");
		if (tableList != null && tableDataProvider != null && sosDashboardHeader != null) {
			tableDataProvider.fillSchedulerIds(sosDashboardHeader.getCbSchedulerId());
		}
	}

	@Override
	public void actualizeList() {
	    // Just an alias for getList()
 	    getList();
	}

	public void setLeftTabFolder(CTabFolder leftTabFolder) {
		this.leftTabFolder = leftTabFolder;
	}

	public void setPrefs(Preferences prefs) {
		this.prefs = prefs;

	}

	public void setDetailHistoryDataProvider(SchedulerHistoryDataProvider detailHistoryDataProvider) {
		this.detailHistoryDataProvider = detailHistoryDataProvider;
	}

	public void setSosDashboardHeaderplanned(SosDashboardHeader sosDashboardHeader) {
		this.sosDashboardHeader = sosDashboardHeader;
	}

	public void setComparablesplanned(SortBaseComparator[][] comparablesplanned) {
		this.comparables = comparablesplanned;
	}

	public void setLogTabFolder(CTabFolder logTabFolder) {
		this.logTabFolder = logTabFolder;
	}

	public void setTableHistoryDetail(Table tableHistoryDetail) {
		this.tableHistoryDetail = tableHistoryDetail;
	}

	public SosDashboardHeader getSosDashboardHeader() {
		return sosDashboardHeader;
	}

	public void setObjOptions(SOSDashboardOptions objOptions) {
		this.objOptions = objOptions;
	}

	@Override
	public void createMenue() {
		logger.info("No menu is defined");
	}

	public void setRight(Group right) {
		this.right = right;
	}

	public void setLeft(Group left) {
		this.left = left;
	}

	public Composite getTableComposite() {
		return mainViewComposite;
	}

	public void setTableDataProvider(ISOSDashboardDataProvider tableDataProvider) {
		this.tableDataProvider = tableDataProvider;
	}

	public SOSTable getTableList() {
		return tableList;
	}
	   public void setDBLayer(File configurationFile)  {
	        schedulerInstancesDBLayer = new SchedulerInstancesDBLayer(configurationFile);
	        schedulerOrderHistoryDBLayer = new SchedulerOrderHistoryDBLayer(configurationFile);
	        schedulerTaskHistoryDBLayer = new SchedulerTaskHistoryDBLayer(configurationFile);
	    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
	    
    private int getLimit() {
        if (sosDashboardHeader != null){
           return sosDashboardHeader.getLimit();
        }else {
            return  0;
        }
        
    }

    public Integer getHistoryLimit() {
        return historyLimit;
    }

    public void setHistoryLimit(Integer historyLimit) {
        this.historyLimit = historyLimit;
    }
}
