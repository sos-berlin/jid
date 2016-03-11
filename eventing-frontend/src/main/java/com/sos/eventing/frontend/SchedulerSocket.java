package com.sos.eventing.frontend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/** @author Ghassan Beydoun */
@Deprecated
public class SchedulerSocket extends Socket {

    private BufferedReader in = null;
    private PrintWriter out = null;
    private int port;
    private String host;
    private int timeout;

    public SchedulerSocket() {
        super();
    }

    public SchedulerSocket(final String host, final int port, final int timeout) throws IOException {
        super(host, port);
        this.host = host;
        this.port = port;
        this.timeout = timeout;
    }

    public void connect(final String host, final int port, final int timeout) throws IOException {
        this.host = host;
        this.port = port;
        InetAddress addr = InetAddress.getByName(host);
        SocketAddress sockaddr = new InetSocketAddress(addr, port);
        super.connect(sockaddr, timeout);
    }

    public PrintWriter getPrintWriter() throws IOException {
        if (out == null) {
            out = new PrintWriter(super.getOutputStream(), true);
        }
        return out;
    }

    public BufferedReader getBufferedReader() throws IOException {
        if (in == null) {
            in = new BufferedReader(new InputStreamReader(super.getInputStream()));
        }
        return in;
    }

    @Override
    public synchronized void close() throws IOException {
        if (getOutputStream() != null) {
            getOutputStream().flush();
        }
        super.close();

    }

    public void sendRequest(final String request) throws Exception {
        out = getPrintWriter();
        if (out != null) {
            if (request.indexOf("<?xml") == 0) {
                out.print(request + "\r\n");
            } else {
                out.print("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>" + request + "\r\n");
            }
            out.flush();
        }
    }

    public String getResponse() throws IOException, RuntimeException {
        int b;
        StringBuffer response = new StringBuffer();
        in = getBufferedReader();
        if (in != null) {
            while ((b = in.read()) != -1) {
                if (b == 0) {
                    break;
                }
                response.append((char) b);
            }
        }
        return response.toString();
    }

    @Override
    public int getPort() {
        return port;
    }

    public void setPort(final int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

}
