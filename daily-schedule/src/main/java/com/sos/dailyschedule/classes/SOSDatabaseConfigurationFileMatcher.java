package com.sos.dailyschedule.classes;

import java.io.File;
import java.util.ArrayList;

public class SOSDatabaseConfigurationFileMatcher {

    private static final String HIBERNATE_CFG_XML = "hibernate.cfg.xml";
    private ArrayList<SOSDatabaseConfigurationFileMatcherEntry> hibernateConfigurationFiles;
    private File defaultConfigurationFile;

    public SOSDatabaseConfigurationFileMatcher(File defaultConfigurationFile_) {
        defaultConfigurationFile = defaultConfigurationFile_;
    }

    private boolean match(String fName, String prefix) {
        return fName.matches(prefix + "\\..*\\.xml");
    }

    private ArrayList<SOSDatabaseConfigurationFileMatcherEntry> searchFile(File dir, String find) {
        File[] files = dir.listFiles();
        // Add the default configuration file
        hibernateConfigurationFiles = new ArrayList<SOSDatabaseConfigurationFileMatcherEntry>();
        SOSDatabaseConfigurationFileMatcherEntry sosDatabaseConfigurationFileMatcherDefaultEntry = new SOSDatabaseConfigurationFileMatcherEntry();
        sosDatabaseConfigurationFileMatcherDefaultEntry.setDbName("default");
        sosDatabaseConfigurationFileMatcherDefaultEntry.setFile(defaultConfigurationFile);
        hibernateConfigurationFiles.add(sosDatabaseConfigurationFileMatcherDefaultEntry);
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (!files[i].isDirectory()) {
                    String fName = files[i].getName();
                    if (match(fName, find)) {
                        SOSDatabaseConfigurationFileMatcherEntry sosDatabaseConfigurationFileMatcherEntry = 
                                new SOSDatabaseConfigurationFileMatcherEntry();
                        sosDatabaseConfigurationFileMatcherEntry.setDbName(fName.replaceAll(find + "\\.(.*)\\.xml", "$1"));
                        sosDatabaseConfigurationFileMatcherEntry.setFile(files[i]);
                        hibernateConfigurationFiles.add(sosDatabaseConfigurationFileMatcherEntry);
                    }
                }
            }
        }
        return hibernateConfigurationFiles;
    }

    public ArrayList<SOSDatabaseConfigurationFileMatcherEntry> scanForHibernateConfigurationFiles() {
        File dir = defaultConfigurationFile.getParentFile();
        String prefix = HIBERNATE_CFG_XML.replaceAll("(.*)\\.xml", "$1");
        return searchFile(dir, prefix);
    }

    public File getFile(String searchDBName) {
        for (int i = 0; i < this.hibernateConfigurationFiles.size(); i++) {
            SOSDatabaseConfigurationFileMatcherEntry sosDatabaseConfigurationFileMatcherEntry = hibernateConfigurationFiles.get(i);
            String dbName = sosDatabaseConfigurationFileMatcherEntry.getDbName();
            if (dbName.equals(searchDBName)) {
                return sosDatabaseConfigurationFileMatcherEntry.getFile();
            }
        }
        return null;
    }

}