package com.sos.scheduler.history.classes;

import java.util.ArrayList;

import com.sos.hibernate.classes.DbItem;

public class SOSIgnoreList {

    private ArrayList<DbItem> ignoreList = null;

    public SOSIgnoreList() {
        ignoreList = new ArrayList<DbItem>();
    }

    public void add(DbItem s) {
        if (!this.contains(s)) {
            ignoreList.add(s);
        }
    }

    public void reset() {
        ignoreList.clear();
    }

    public boolean contains(DbItem s) {
        for (DbItem oh : ignoreList) {
            if (oh.isOrderJob()) {
                if ((oh.getOrderId() == null || "null".equals(oh.getOrderId()) || oh.getOrderId().equals(s.getOrderId()))
                        && oh.getJobChain().equals(s.getJobChain())) {
                    return true;
                }
            } else {
                if (oh.getJob() == null || oh.getJob().equals(s.getJob())) {
                    return true;
                }
            }
        }
        return false;
    }

    public int size() {
        return ignoreList.size();
    }

}