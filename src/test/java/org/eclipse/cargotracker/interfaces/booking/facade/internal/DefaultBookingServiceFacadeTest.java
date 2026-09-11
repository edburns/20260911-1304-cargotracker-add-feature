package org.eclipse.cargotracker.interfaces.booking.facade.internal;

import org.eclipse.cargotracker.application.BookingService;
import org.eclipse.cargotracker.application.util.DateUtil;
import org.eclipse.cargotracker.domain.model.cargo.Itinerary;
import org.eclipse.cargotracker.domain.model.cargo.TrackingId;
import org.eclipse.cargotracker.domain.model.location.UnLocode;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DefaultBookingServiceFacadeTest {

    private BookingServiceSpy bookingService;
    private DefaultBookingServiceFacade facade;

    @Before
    public void setUp() throws Exception {
        bookingService = new BookingServiceSpy();
        facade = new DefaultBookingServiceFacade();

        Field field = DefaultBookingServiceFacade.class
                .getDeclaredField("bookingService");
        field.setAccessible(true);
        field.set(facade, bookingService);
    }

    @Test
    public void testChangeDeadlineDelegatesOnce() {
        Date newDeadline = DateUtil.toDate("2009-06-01");

        facade.changeDeadline("ABC123", newDeadline);

        assertEquals(1, bookingService.changeDeadlineCallCount);
        assertEquals(new TrackingId("ABC123"), bookingService.trackingId);
        assertSame(newDeadline, bookingService.deadline);
    }

    private static class BookingServiceSpy implements BookingService {

        private int changeDeadlineCallCount = 0;
        private TrackingId trackingId;
        private Date deadline;

        @Override
        public TrackingId bookNewCargo(UnLocode origin, UnLocode destination,
                                       Date arrivalDeadline) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Itinerary> requestPossibleRoutesForCargo(
                TrackingId trackingId) {
            return Collections.emptyList();
        }

        @Override
        public void assignCargoToRoute(Itinerary itinerary,
                                       TrackingId trackingId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void changeDestination(TrackingId trackingId,
                                      UnLocode unLocode) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void changeDeadline(TrackingId trackingId, Date deadline) {
            this.changeDeadlineCallCount++;
            this.trackingId = trackingId;
            this.deadline = deadline;
        }
    }
}
