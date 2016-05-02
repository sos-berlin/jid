package com.sos.scheduler.history.classes;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HistorySeverityTest {

    private HistorySeverity historySeverity = null;

    public HistorySeverityTest() {
        //
    }

    @Test
    public void testHistorySeverityString() {
        historySeverity = new HistorySeverity("success");
        int v = historySeverity.getIntValue();
        assertEquals("Test HistorySeverityString fails ...", 0, v);
    }

    @Test
    public void testHistorySeverityInteger() {
        historySeverity = new HistorySeverity(0);
        assertEquals("Test historySeverityInteger fails ...", "success", historySeverity.getStrValue());
    }

    @Test
    public void testSetStrValue() {
        historySeverity = new HistorySeverity(0);
        assertEquals("Test setStrValue fails ...", "success", historySeverity.getStrValue());
        historySeverity.setStrValue("error");
        int v = historySeverity.getIntValue();
        assertEquals("Test setStrValue fails ...", 1, v);
    }

    @Test
    public void testSetIntValue() {
        historySeverity = new HistorySeverity("success");
        assertEquals("Test setIntValue fails ...", "success", historySeverity.getStrValue());
        historySeverity.setIntValue(1);
        assertEquals("Test setIntValue fails ...", "error", historySeverity.getStrValue());
    }

    @Test
    public void testHasValue() {
        historySeverity = new HistorySeverity(1);
        historySeverity.setStrValue("");
        assertEquals("Test hasValue fails ...", false, historySeverity.hasValue());
        historySeverity.setIntValue(1);
        assertEquals("Test hasValue fails ...", true, historySeverity.hasValue());
    }

}