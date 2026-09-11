package org.eclipse.cargotracker.interfaces.booking.web;

import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ChangeArrivalDeadlineDateDialogXhtmlTest {

    @Test
    public void testUpdateButtonProcessesForm() throws IOException {
        String xhtml = loadDialogXhtml();
        Pattern pattern = Pattern.compile(
                "<p:commandButton\\s+value=\\\"Update\\\"[\\s\\S]*?/>"
        );
        Matcher matcher = pattern.matcher(xhtml);

        assertTrue(matcher.find());
        String updateButton = matcher.group();
        assertTrue(updateButton.contains(
                "action=\"#{changeArrivalDeadlineDate.changeArrivalDeadline()}\""));
        assertTrue(updateButton.contains("process=\"@form\""));
    }

    @Test
    public void testCancelButtonProcessesOnlyItself() throws IOException {
        String xhtml = loadDialogXhtml();
        Pattern pattern = Pattern.compile(
                "<p:commandButton\\s+value=\\\"Cancel\\\"[\\s\\S]*?/>"
        );
        Matcher matcher = pattern.matcher(xhtml);

        assertTrue(matcher.find());
        String cancelButton = matcher.group();
        assertTrue(cancelButton.contains(
                "action=\"#{changeArrivalDeadlineDateDialog.cancel()}\""));
        assertTrue(cancelButton.contains("process=\"@this\""));
    }

    private String loadDialogXhtml() throws IOException {
        String path = "src/main/webapp/admin/dialogs/changeArrivalDeadlineDate.xhtml";
        assertNotNull(path);
        return new String(Files.readAllBytes(Paths.get(path)),
                StandardCharsets.UTF_8);
    }
}
