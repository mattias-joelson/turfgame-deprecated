package org.joelson.turf.application.view;

import org.joelson.turf.application.model.ApplicationData;

import javax.swing.Action;
import java.beans.PropertyChangeEvent;
import java.util.Objects;

final class ActionUtil {

    private ActionUtil() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }

    public static void enabledPropertyChange(
            PropertyChangeEvent evt, Action action, String propertyName, Object enabledValue) {
        if (Objects.equals(evt.getPropertyName(), propertyName)) {
            action.setEnabled(Objects.equals(evt.getNewValue(), enabledValue));
        }
    }

    public static void addEnabledPropertyChangeListener(
            ApplicationData applicationData, Action action, String propertyName, Object enabledValue) {
        applicationData.addPropertyChangeListener(propertyName,
                evt -> enabledPropertyChange(evt, action, propertyName, enabledValue));
    }
}
