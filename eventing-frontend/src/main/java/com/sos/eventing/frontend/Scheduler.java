package com.sos.eventing.frontend;

/** <p>
 * Title:
 * </p>
 * <p>
 * Description: Scheduler
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: SOS GmbH
 * </p>
 * 
 * @author <a href="mailto:ghassan.beydoun@sos-berlin.com">Ghassan Beydoun</a>
 * @version $Id$ */

@Deprecated
public class Scheduler {

    private String tcpPort;

    private String hostName;

    // private SchedulerState state;

    private SchedulerSocket schedulerSocket;

    private boolean connected;

    private boolean supervisor = false;

    public boolean isSupervisor() {
        return supervisor;
    }

    public void setSupervisor(final boolean supervisor) {
        this.supervisor = supervisor;
    }

    public String getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(final String tcpPort) {
        this.tcpPort = tcpPort;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(final String hostName) {
        this.hostName = hostName;
    }

    /*
     * public SchedulerState getState() { return state; } public void
     * setState(SchedulerState state) { this.state = state; }
     */

    public SchedulerSocket getSchedulerSocket() {
        return schedulerSocket;
    }

    public void setSchedulerSocket(final SchedulerSocket schedulerSocket) {
        this.schedulerSocket = schedulerSocket;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(final boolean connected) {
        this.connected = connected;
    }

    public void setConnectedAt(final String connectedAt) {
        // TODO Auto-generated method stub

    }

    public void setSchedulerId(final String schedulerId) {
        // TODO Auto-generated method stub

    }

    public void setSchedulerVersion(final String schedulerVersion) {
        // TODO Auto-generated method stub

    }

}
