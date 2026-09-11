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

public class ListNotRoutedXhtmlTest {

    @Test
    public void testDeadlineLinkOpensAndRefreshesDeadlineDialog() throws IOException {
        String xhtml = loadNotRoutedXhtml();
        Pattern pattern = Pattern.compile(
                "<p:commandLink\\s+[\\s\\S]*?id=\"arrivalDeadlineToUpdate\"[\\s\\S]*?</p:commandLink>"
        );
        Matcher matcher = pattern.matcher(xhtml);

        assertTrue(matcher.find());
        String deadlineLink = matcher.group();
        assertTrue(deadlineLink.contains(
                "action=\"#{changeArrivalDeadlineDateDialog.showDialog(cargoNotRouted.trackingId)}\""));
        assertTrue(deadlineLink.contains(
                "value=\"#{cargoNotRouted.arrivalDeadlineDate}\""));
        assertTrue(deadlineLink.contains("class=\"fa fa-edit\""));
        assertTrue(deadlineLink.contains("event=\"dialogReturn\""));
        assertTrue(deadlineLink.contains(
                "listener=\"#{changeArrivalDeadlineDateDialog.handleReturn}\""));
        assertTrue(deadlineLink.contains("update=\"tableNotRouted\""));
        assertTrue(deadlineLink.contains(
                "value=\"Click to change cargo arrival deadline date.\""));
    }

    private String loadNotRoutedXhtml() throws IOException {
        String path = "src/main/webapp/admin/tables/listNotRouted.xhtml";
        assertNotNull(path);
        return new String(Files.readAllBytes(Paths.get(path)),
                StandardCharsets.UTF_8);
    }
}
