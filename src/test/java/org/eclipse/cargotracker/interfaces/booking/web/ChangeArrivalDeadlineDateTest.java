package org.eclipse.cargotracker.interfaces.booking.web;

import org.eclipse.cargotracker.interfaces.booking.facade.BookingServiceFacade;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.CargoRoute;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.Location;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.RouteCandidate;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ChangeArrivalDeadlineDateTest {

    private BookingServiceFacadeFake bookingServiceFacade;
    private TestableChangeArrivalDeadlineDate changeArrivalDeadlineDate;

    @Before
    public void setUp() throws Exception {
        bookingServiceFacade = new BookingServiceFacadeFake();
        changeArrivalDeadlineDate = new TestableChangeArrivalDeadlineDate();

        Field field = ChangeArrivalDeadlineDate.class
                .getDeclaredField("bookingServiceFacade");
        field.setAccessible(true);
        field.set(changeArrivalDeadlineDate, bookingServiceFacade);
    }

    @Test
    public void testLoadConvertsDisplayDate() throws Exception {
        bookingServiceFacade.cargo = new CargoRouteStub("03/15/2009");
        changeArrivalDeadlineDate.setTrackingId("ABC123");

        changeArrivalDeadlineDate.load();

        assertEquals("ABC123", bookingServiceFacade.loadedTrackingId);
        assertSame(bookingServiceFacade.cargo,
                changeArrivalDeadlineDate.getCargo());
        assertEquals(new SimpleDateFormat("MM/dd/yyyy").parse("03/15/2009"),
                changeArrivalDeadlineDate.getArrivalDeadlineDate());
    }

    @Test
    public void testLoadSurfacesMalformedDate() {
        bookingServiceFacade.cargo = new CargoRouteStub("not a date");
        changeArrivalDeadlineDate.setTrackingId("ABC123");

        try {
            changeArrivalDeadlineDate.load();
            fail("Expected malformed date to be surfaced as an error.");
        } catch (RuntimeException expected) {
            assertNull(changeArrivalDeadlineDate.getArrivalDeadlineDate());
        }
    }

    @Test
    public void testChangeArrivalDeadlineDelegatesAndCloses() {
        Date newDeadline = new Date();
        changeArrivalDeadlineDate.setTrackingId("ABC123");
        changeArrivalDeadlineDate.setArrivalDeadlineDate(newDeadline);

        changeArrivalDeadlineDate.changeArrivalDeadline();

        assertEquals(1, bookingServiceFacade.changeDeadlineCallCount);
        assertEquals("ABC123", bookingServiceFacade.deadlineTrackingId);
        assertSame(newDeadline, bookingServiceFacade.deadline);
        assertTrue(changeArrivalDeadlineDate.dialogClosed);
    }

    @Test
    public void testChangeArrivalDeadlineRejectsNullDate() {
        changeArrivalDeadlineDate.setTrackingId("ABC123");
        changeArrivalDeadlineDate.setArrivalDeadlineDate(null);

        changeArrivalDeadlineDate.changeArrivalDeadline();

        assertEquals(0, bookingServiceFacade.changeDeadlineCallCount);
        assertFalse(changeArrivalDeadlineDate.dialogClosed);
        assertEquals(1, changeArrivalDeadlineDate.errorMessages.size());
    }

    @Test
    public void testChangeArrivalDeadlineDoesNotCloseOnFacadeFailure() {
        bookingServiceFacade.failOnChangeDeadline = true;
        changeArrivalDeadlineDate.setTrackingId("ABC123");
        changeArrivalDeadlineDate.setArrivalDeadlineDate(new Date());

        try {
            changeArrivalDeadlineDate.changeArrivalDeadline();
            fail("Expected the facade failure to be surfaced.");
        } catch (RuntimeException expected) {
            assertFalse(changeArrivalDeadlineDate.dialogClosed);
        }
    }

    private static class TestableChangeArrivalDeadlineDate
            extends ChangeArrivalDeadlineDate {

        private static final long serialVersionUID = 1L;

        private boolean dialogClosed = false;
        private final List<String> errorMessages = new ArrayList<>();

        @Override
        protected void closeDialog() {
            this.dialogClosed = true;
        }

        @Override
        protected void addErrorMessage(String summary) {
            this.errorMessages.add(summary);
        }
    }

    private static class CargoRouteStub extends CargoRoute {

        private static final long serialVersionUID = 1L;

        private final String arrivalDeadlineDate;

        private CargoRouteStub(String arrivalDeadlineDate) {
            super("ABC123", "USNYC", "JNTKO", new Date(), false, false,
                    "USNYC", "IN_PORT");
            this.arrivalDeadlineDate = arrivalDeadlineDate;
        }

        @Override
        public String getArrivalDeadlineDate() {
            return arrivalDeadlineDate;
        }
    }

    private static class BookingServiceFacadeFake
            implements BookingServiceFacade {

        private CargoRoute cargo;
        private String loadedTrackingId;
        private int changeDeadlineCallCount = 0;
        private String deadlineTrackingId;
        private Date deadline;
        private boolean failOnChangeDeadline = false;

        @Override
        public String bookNewCargo(String origin, String destination,
                                   Date arrivalDeadline) {
            throw new UnsupportedOperationException();
        }

        @Override
        public CargoRoute loadCargoForRouting(String trackingId) {
            this.loadedTrackingId = trackingId;
            return cargo;
        }

        @Override
        public void assignCargoToRoute(String trackingId,
                                       RouteCandidate route) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void changeDestination(String trackingId,
                                      String destinationUnLocode) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void changeDeadline(String trackingId, Date arrivalDeadline) {
            if (failOnChangeDeadline) {
                throw new RuntimeException("Deadline change failed.");
            }

            this.changeDeadlineCallCount++;
            this.deadlineTrackingId = trackingId;
            this.deadline = arrivalDeadline;
        }

        @Override
        public List<RouteCandidate> requestPossibleRoutesForCargo(
                String trackingId) {
            return Collections.emptyList();
        }

        @Override
        public List<Location> listShippingLocations() {
            return Collections.emptyList();
        }

        @Override
        public List<CargoRoute> listAllCargos() {
            return Collections.emptyList();
        }
    }
}
