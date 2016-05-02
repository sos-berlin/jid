package com.sos.dashboard.globals;

import java.util.HashMap;

import com.sos.JSHelper.Annotations.JSOptionClass;
import com.sos.JSHelper.Exceptions.JSExceptionMandatoryOptionMissing;
import com.sos.JSHelper.Listener.JSListener;

@JSOptionClass(name = "nameOptions", description = "title")
public class SOSDashboardOptions extends SOSDashboardOptionsSuperClass {

    private static final long serialVersionUID = 1L;

    public SOSDashboardOptions() {
        super();
    }

    public SOSDashboardOptions(JSListener pobjListener) {
        this();
        this.registerMessageListener(pobjListener);
    }

    public SOSDashboardOptions(HashMap<String, String> JSSettings) throws Exception {
        super(JSSettings);
    }

    @Override
    public void CheckMandatory() {
        try {
            super.CheckMandatory();
        } catch (Exception e) {
            throw new JSExceptionMandatoryOptionMissing(e.toString());
        }
    }

}