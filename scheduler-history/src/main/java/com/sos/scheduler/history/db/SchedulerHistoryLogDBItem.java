package com.sos.scheduler.history.db;

import java.io.IOException;

import javax.persistence.Column;

import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

import javax.persistence.Transient;

import org.apache.log4j.Logger;

import com.sos.hibernate.classes.DbItem;
import com.sos.scheduler.history.classes.SOSStreamUnzip;

@MappedSuperclass
public class SchedulerHistoryLogDBItem extends DbItem {

    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(SchedulerHistoryLogDBItem.class);
    private byte[] log;

    public SchedulerHistoryLogDBItem() {

    }

    // Wenn PGSQL verwendet wird, muss die Annotation LOB entfernt werden.
    // Es gibt in der Auslieferung den JAR com.sos.hibernate_pgsql.jar, der nur
    // diese eine Klasse ohne @LOB enthält
    // com.sos.hibernate_pgsql.jar muss dann dem Klassenpfad hinzugefügt werden,
    // wenn PGSQL verwendet wird.

    @Lob
    @Column(name = "`LOG`", nullable = true)
    public byte[] getLog() {
        return log;
    }

    @Column(name = "`LOG`", nullable = true)
    public void setLog(final byte[] log) {
        this.log = log;
    }

    @Transient
    public String getLogAsString() throws IOException {
        if (log == null) {
            return null;
        } else {
            SOSStreamUnzip SOSUnzip = new SOSStreamUnzip(log);
            return SOSUnzip.unzip2String();
        }
    }
}
