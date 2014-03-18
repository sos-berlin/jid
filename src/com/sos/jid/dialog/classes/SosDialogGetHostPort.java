package com.sos.jid.dialog.classes;
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.graphics.Point;

public class SosDialogGetHostPort {
    final int               conDefaultPort  = 4444;
    final String               conDefaultHost  = "localhost";
	private GridLayout		gridLayout;
	private Logger			logger			= Logger.getLogger(SosDialogGetHostPort.class);
	private IntegerField	edPort;
	private Text			edHost;
    private Text            edUrl;
	private Combo           cbProtocol;
	private Button			btnOk;
    private String          protocolValue;
    private String          hostValue;
    private String          urlValue;
	private int				portValue;

	public SosDialogGetHostPort(Shell parentShell) {
		execute(parentShell);
	}

	private void execute(Shell parentShell) {
		Display display = Display.getDefault();
		Shell shell = showForm(display, parentShell);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private Shell showForm(final Display display, Shell parentShell) {
		final Shell dialogShell = new Shell(parentShell, SWT.PRIMARY_MODAL | SWT.SHEET);
		dialogShell.setMinimumSize(new Point(440, 29));
		dialogShell.setSize(395, 170);
		dialogShell.setLayout(new GridLayout(3, false));
		new Label(dialogShell, SWT.NONE);
		
		Label lblNewLabel = new Label(dialogShell, SWT.NONE);
		
		cbProtocol = new Combo(dialogShell, SWT.NONE);
		cbProtocol.setItems(new String[] {"HTTP", "HTTPS"});
		cbProtocol.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cbProtocol.select(0);
		new Label(dialogShell, SWT.NONE);
		Label lbHost = new Label(dialogShell, SWT.NONE);
		lbHost.setText("Host");
		edHost = new Text(dialogShell, SWT.BORDER);
		GridData gd_edHost = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_edHost.widthHint = 150;
		edHost.setLayoutData(gd_edHost);
		new Label(dialogShell, SWT.NONE);
		Label lbPort = new Label(dialogShell, SWT.NONE);
		lbPort.setText("Port");
		edPort = new IntegerField(dialogShell, SWT.BORDER);
		GridData gd_edPort = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_edPort.widthHint = 150;
		edPort.setLayoutData(gd_edPort);
		new Label(dialogShell, SWT.NONE);
		
		Label lblNewLabel_1 = new Label(dialogShell, SWT.NONE);
		lblNewLabel_1.setText("Url");
		
		edUrl = new Text(dialogShell, SWT.BORDER);
		edUrl.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(dialogShell, SWT.NONE);
		btnOk = new Button(dialogShell, SWT.NONE);
		GridData gd_btnOk = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnOk.widthHint = 64;
		btnOk.setLayoutData(gd_btnOk);
		btnOk.setText("Ok");
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
                urlValue = edUrl.getText();
                hostValue = edHost.getText();
				protocolValue = cbProtocol.getItem(cbProtocol.getSelectionIndex());
				try {
					portValue = edPort.getIntegerValue(conDefaultPort);
				}
				catch (NumberFormatException ee) {
					// returning default port.
				}
				dialogShell.dispose();
			}
		});
		dialogShell.setDefaultButton(btnOk);
		dialogShell.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
			}
		});
		dialogShell.pack();
		
		Button btnCancel = new Button(dialogShell, SWT.NONE);
		btnCancel.setText("Cancel");
		btnCancel.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                dialogShell.dispose();
            }
        });
		
		dialogShell.open();
		return dialogShell;
	}

	private String getHost() {
		if (hostValue == null) {
			return conDefaultHost;
		} else {
			if (hostValue.contains(":")) {
				return hostValue.split(":")[0];
			} else {
				return hostValue;
			}
		}
	}

	private int getPortValue(String port) {
		int p = conDefaultPort;
		try {
			p = Integer.parseInt(port);
		}
		catch (NumberFormatException e) {
		}
		return p;
	}

	private int getPort() {
		if (hostValue == null) {
			return portValue;
		} else {
			if (hostValue.contains(":")) {
				return getPortValue(hostValue.split(":")[1]);
			} else {
				return portValue;
			}
		}
	}

    public String getUrl() {
        if (urlValue != null && urlValue.length() > 0) {
            return urlValue;
        }else {
            return String.format("%s://%s:%s", protocolValue, getHost(),getPort());
        }
    }
    
    public boolean cancel() {
        return (protocolValue==null);
    }
}
