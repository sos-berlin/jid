package com.sos.dailyschedule.classes;

import java.io.File;

public class SOSDatabaseConfigurationFileMatcherEntry {

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public void setFile(File file) {
        this.file = file;
    }

    private String dbName;
    private File file;

    public String getDbName() {
        return dbName;
    }

    public File getFile() {
        return file;
    }

}
