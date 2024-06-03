package org.joelson.mattias.turfgame.application.view;

import org.joelson.mattias.turfgame.application.model.ApplicationData;

import java.beans.PropertyChangeEvent;
import java.util.Objects;
import javax.swing.Action;

final class ActionUtil {
    
    private ActionUtil() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }
    
    public static void enabledPropertyChange(PropertyChangeEvent evt, Action action, String propertyName, Object enabledValue) {
        if (Objects.equals(evt.getPropertyName(), propertyName)) {
            action.setEnabled(Objects.equals(evt.getNewValue(), enabledValue));
        }
    }
    
    public static void addEnabledPropertyChangeListener(ApplicationData applicationData, Action action, String propertyName, Object enabledValue) {
        applicationData.addPropertyChangeListener(propertyName, evt -> enabledPropertyChange(evt, action, propertyName, enabledValue));
    }
}
