package org.eclipse.cargotracker.interfaces.booking.web;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ListNotRoutedXhtmlTest {

    @Test
    public void testDeadlineLinkOpensAndRefreshesDeadlineDialog()
            throws ParserConfigurationException, SAXException, IOException,
            XPathExpressionException {
        Document xhtml = loadNotRoutedXhtml();
        XPath xpath = XPathFactory.newInstance().newXPath();
        Element deadlineLink = (Element) xpath.evaluate(
                "//*[local-name()='commandLink' and @id='arrivalDeadlineToUpdate']",
                xhtml, XPathConstants.NODE);

        assertEquals(
                "#{changeArrivalDeadlineDateDialog.showDialog(cargoNotRouted.trackingId)}",
                deadlineLink.getAttribute("action"));
        assertTrue((Boolean) xpath.evaluate(
                ".//*[local-name()='outputText' and @value='#{cargoNotRouted.arrivalDeadlineDate}']",
                deadlineLink, XPathConstants.BOOLEAN));
        assertTrue((Boolean) xpath.evaluate(
                ".//*[local-name()='i' and @class='fa fa-edit']",
                deadlineLink, XPathConstants.BOOLEAN));

        Node dialogReturn = (Node) xpath.evaluate(
                ".//*[local-name()='ajax' and @event='dialogReturn']",
                deadlineLink, XPathConstants.NODE);
        assertEquals("#{changeArrivalDeadlineDateDialog.handleReturn}",
                dialogReturn.getAttributes().getNamedItem("listener").getNodeValue());
        assertEquals("tableNotRouted",
                dialogReturn.getAttributes().getNamedItem("update").getNodeValue());

        Node tooltip = (Node) xpath.evaluate(
                ".//*[local-name()='tooltip' and @for='arrivalDeadlineToUpdate']",
                deadlineLink, XPathConstants.NODE);
        assertEquals("Click to change cargo arrival deadline date.",
                tooltip.getAttributes().getNamedItem("value").getNodeValue());
    }

    private Document loadNotRoutedXhtml() throws ParserConfigurationException,
            SAXException, IOException {
        File path = new File("src/main/webapp/admin/tables/listNotRouted.xhtml");
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(path);
    }
}
