package org.eclipse.cargotracker.interfaces.booking.web;

import org.junit.Test;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ChangeArrivalDeadlineDateDialogTest {

    @Test
    public void testBeanAnnotations() {
        ManagedBean managedBean = ChangeArrivalDeadlineDateDialog.class
                .getAnnotation(ManagedBean.class);

        assertNotNull(managedBean);
        assertEquals("changeArrivalDeadlineDateDialog", managedBean.name());
        assertNotNull(ChangeArrivalDeadlineDateDialog.class
                .getAnnotation(SessionScoped.class));
        assertTrue(Serializable.class.isAssignableFrom(
                ChangeArrivalDeadlineDateDialog.class));
    }

    @Test
    public void testShowDialogOpensConfiguredDynamicDialog() {
        TestableChangeArrivalDeadlineDateDialog dialog =
                new TestableChangeArrivalDeadlineDateDialog();

        dialog.showDialog("DEF789");

        assertEquals("/admin/dialogs/changeArrivalDeadlineDate.xhtml",
                dialog.path);
        assertEquals(true, dialog.options.get("modal"));
        assertEquals(true, dialog.options.get("draggable"));
        assertEquals(false, dialog.options.get("resizable"));
        assertEquals(410, dialog.options.get("contentWidth"));
        assertEquals(280, dialog.options.get("contentHeight"));

        List<String> trackingIds = dialog.params.get("trackingId");
        assertNotNull(trackingIds);
        assertEquals(1, trackingIds.size());
        assertEquals("DEF789", trackingIds.get(0));
    }

    @Test
    public void testCancelClosesWithEmptyResult() {
        TestableChangeArrivalDeadlineDateDialog dialog =
                new TestableChangeArrivalDeadlineDateDialog();

        dialog.cancel();

        assertEquals("", dialog.closeData);
    }

    @Test
    public void testHandleReturnNoOp() {
        new ChangeArrivalDeadlineDateDialog().handleReturn(null);
    }

    private static class TestableChangeArrivalDeadlineDateDialog
            extends ChangeArrivalDeadlineDateDialog {

        private static final long serialVersionUID = 1L;

        private String path;
        private Map<String, Object> options;
        private Map<String, List<String>> params;
        private String closeData;

        @Override
        protected void openDynamic(String path, Map<String, Object> options,
                                   Map<String, List<String>> params) {
            this.path = path;
            this.options = options;
            this.params = params;
        }

        @Override
        protected void closeDynamic(String data) {
            this.closeData = data;
        }
    }
}
