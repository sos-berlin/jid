package com.sos.scheduler.history.classes;

import java.util.ArrayList;

 

import com.sos.hibernate.classes.DbItem;
 import com.sos.scheduler.history.db.SchedulerOrderHistoryDBItem;
import com.sos.scheduler.history.db.SchedulerTaskHistoryDBItem;

public class SOSIgnoreList {

	private ArrayList <DbItem> ignoreList= null;
		
	public  SOSIgnoreList() {
		ignoreList = new ArrayList  <DbItem> ();
	}

	public void add(SchedulerTaskHistoryDBItem s){
	    if (!this.contains(s)) {
	        ignoreList.add(s);
	    }
	}

	public void add(SchedulerOrderHistoryDBItem s){
		ignoreList.add(s);
	}

	
	public void reset(){
		ignoreList.clear();
	}
 

	public boolean contains(SchedulerOrderHistoryDBItem s){
		  for( DbItem oh: ignoreList){
 			  if ( oh.getOrderId() == null || oh.getOrderId().equals(s.getOrderId()) && oh.getJobChain().equals(s.getJobChain())){
				  return true;
			  }
          }		
		  return false;
	}
	
	public boolean contains(SchedulerTaskHistoryDBItem s){
        for( DbItem oh: ignoreList){
            if ( oh.getJob() == null || oh.getJob().equals(s.getJob()) ){
                return true;
            }
        }     
        return false;
  }
	public int size(){
		return ignoreList.size();
	}
	
}
