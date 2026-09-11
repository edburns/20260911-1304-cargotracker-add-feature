package org.eclipse.cargotracker.interfaces.booking.web;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "changeArrivalDeadlineDateDialog")
@SessionScoped
public class ChangeArrivalDeadlineDateDialog implements Serializable {

    private static final long serialVersionUID = 1L;

    public void showDialog(String trackingId) {
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);
        options.put("contentWidth", 410);
        options.put("contentHeight", 280);

        Map<String, List<String>> params = new HashMap<>();
        List<String> values = new ArrayList<>();
        values.add(trackingId);
        params.put("trackingId", values);

        openDynamic("/admin/dialogs/changeArrivalDeadlineDate.xhtml", options,
                params);
    }

    public void handleReturn(SelectEvent event) { }

    public void cancel() {
        closeDynamic("");
    }

    protected void openDynamic(String path, Map<String, Object> options,
                               Map<String, List<String>> params) {
        PrimeFaces.current().dialog().openDynamic(path, options, params);
    }

    protected void closeDynamic(String data) {
        PrimeFaces.current().dialog().closeDynamic(data);
    }
}
