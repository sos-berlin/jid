package com.sos.eventing.dialog.classes;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.sos.dialog.components.IntegerField;

public class SosDialogEventConfiguration {

    final int conDefaultPort = 4444;
    private GridLayout gridLayout;
    private final Logger logger = Logger.getLogger(SosDialogEventConfiguration.class);
    private IntegerField edPort;
    private Text edHost;
    private Text edEventhandlerDirectory;
    private Button btnOk;
    private String eventhandlerDirectoryValue;
    private String hostValue;
    private int portValue;

    public SosDialogEventConfiguration(final Shell parentShell, final String host, final int port, final String eventhandlerDirectoryValue_) {
        hostValue = host;
        portValue = port;
        eventhandlerDirectoryValue = eventhandlerDirectoryValue_;
        execute(parentShell);
    }

    private void execute(final Shell parentShell) {
        Display display = Display.getDefault();
        Shell shell = showForm(display, parentShell);
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    private Shell showForm(final Display display, final Shell parentShell) {
        final Shell dialogShell = new Shell(parentShell, SWT.PRIMARY_MODAL | SWT.SHEET);
        dialogShell.setSize(450, 300);
        dialogShell.setLayout(new GridLayout(3, false));
        new Label(dialogShell, SWT.NONE);
        new Label(dialogShell, SWT.NONE);
        new Label(dialogShell, SWT.NONE);
        new Label(dialogShell, SWT.NONE);
        Label lbHost = new Label(dialogShell, SWT.NONE);
        lbHost.setText("Host");
        edHost = new Text(dialogShell, SWT.BORDER);
        GridData gd_edHost = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
        gd_edHost.widthHint = 150;
        edHost.setLayoutData(gd_edHost);
        edHost.setText(hostValue);

        new Label(dialogShell, SWT.NONE);
        Label lbPort = new Label(dialogShell, SWT.NONE);
        lbPort.setText("Port");
        edPort = new IntegerField(dialogShell, SWT.BORDER);
        GridData gd_edPort = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
        gd_edPort.widthHint = 150;
        edPort.setLayoutData(gd_edPort);
        edPort.setText(String.valueOf(portValue));

        new Label(dialogShell, SWT.NONE);
        Label lbEventhandlerDirecors = new Label(dialogShell, SWT.NONE);
        lbEventhandlerDirecors.setText("Directory with event handler");
        edEventhandlerDirectory = new Text(dialogShell, SWT.BORDER);
        GridData gd_edEventhandlerDirectory = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
        gd_edEventhandlerDirectory.widthHint = 250;
        edEventhandlerDirectory.setLayoutData(gd_edPort);
        edEventhandlerDirectory.setText(eventhandlerDirectoryValue);

        new Label(dialogShell, SWT.NONE);
        btnOk = new Button(dialogShell, SWT.NONE);
        GridData gd_btnOk = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
        gd_btnOk.widthHint = 64;
        btnOk.setLayoutData(gd_btnOk);
        btnOk.setText("Ok");
        btnOk.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                hostValue = edHost.getText();
                eventhandlerDirectoryValue = edEventhandlerDirectory.getText();
                try {
                    portValue = edPort.getIntegerValue(conDefaultPort);
                } catch (NumberFormatException ee) {
                    // returning default port.
                }
                dialogShell.dispose();
            }
        });
        dialogShell.setDefaultButton(btnOk);
        dialogShell.addDisposeListener(new DisposeListener() {

            @Override
            public void widgetDisposed(final DisposeEvent e) {
            }
        });
        dialogShell.pack();
        dialogShell.open();
        return dialogShell;
    }

    public String getHost() {
        if (hostValue == null) {
            return hostValue;
        } else {
            if (hostValue.contains(":")) {
                return hostValue.split(":")[0];
            } else {
                return hostValue;
            }
        }
    }

    public String getEventhandlerDirectory() {
        return eventhandlerDirectoryValue;
    }

    private int getPortValue(final String port) {
        int p = conDefaultPort;
        try {
            p = Integer.parseInt(port);
        } catch (NumberFormatException e) {
        }
        return p;
    }

    public String getPort() {
        if (hostValue == null) {
            return String.valueOf(portValue);
        } else {
            if (hostValue.contains(":")) {
                return String.valueOf(getPortValue(hostValue.split(":")[1]));
            } else {
                return String.valueOf(portValue);
            }
        }
    }
}
