package com.sos.dailyschedule.dialog;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Slider;

public class test {

    protected Shell shell;
    private Text edSearchfield;

    /**
     * Launch the application.
     * @param args
     */
    public static void main(String[] args) {
        try {
            test window = new test();
            window.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Open the window.
     */
    public void open() {
        Display display = Display.getDefault();
        createContents();
        shell.open();
        shell.layout();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    /**
     * Create contents of the window.
     */
    protected void createContents() {
        shell = new Shell();
        shell.setSize(450, 300);
        shell.setText("SWT Application");
        shell.setLayout(new GridLayout(3, false));
        new Label(shell, SWT.NONE);
        new Label(shell, SWT.NONE);
        new Label(shell, SWT.NONE);
        new Label(shell, SWT.NONE);
        new Label(shell, SWT.NONE);
        new Label(shell, SWT.NONE);
        new Label(shell, SWT.NONE);
        new Label(shell, SWT.NONE);
        
        CCombo combo = new CCombo(shell, SWT.NONE);
        GridData gd_combo = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        gd_combo.widthHint = 150;
        gd_combo.minimumWidth = 150;
         combo.setLayoutData(gd_combo);
         new Label(shell, SWT.NONE);
         new Label(shell, SWT.NONE);
         new Label(shell, SWT.NONE);
         new Label(shell, SWT.NONE);
         new Label(shell, SWT.NONE);
         
         edSearchfield = new Text(shell, SWT.BORDER);
         edSearchfield.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
         new Label(shell, SWT.NONE);
         new Label(shell, SWT.NONE);
         new Label(shell, SWT.NONE);
         new Label(shell, SWT.NONE);
         new Label(shell, SWT.NONE);
         new Label(shell, SWT.NONE);
         new Label(shell, SWT.NONE);
         new Label(shell, SWT.NONE);
         new Label(shell, SWT.NONE);
         new Label(shell, SWT.NONE);
         new Label(shell, SWT.NONE);
         new Label(shell, SWT.NONE);
         new Label(shell, SWT.NONE);
         new Label(shell, SWT.NONE);
         new Label(shell, SWT.NONE);
         new Label(shell, SWT.NONE);
         new Label(shell, SWT.NONE);
         
         Slider slider = new Slider(shell, SWT.NONE);
         new Label(shell, SWT.NONE);
         new Label(shell, SWT.NONE);
         
         Label lblNewLabel = new Label(shell, SWT.NONE);
         lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
         lblNewLabel.setText("New Label");

    }
}
