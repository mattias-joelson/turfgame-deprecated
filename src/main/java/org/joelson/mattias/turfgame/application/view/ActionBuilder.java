package org.joelson.mattias.turfgame.application.view;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import javax.swing.Action;

public class ActionBuilder {
    
    private final Consumer<ActionEvent> actionPerformed;
    private String name;
    
    public ActionBuilder(Consumer<ActionEvent> actionPerformed) {
        this.actionPerformed = Objects.requireNonNull(actionPerformed, "Action to perform can not be null"); //NON-NLS
    }
    
    public ActionBuilder withName(String name) {
        this.name = name;
        return this;
    }
    
    public Action build() {
        return new DelegatingAction(actionPerformed, name);
    }
    
    private static final class DelegatingAction implements Action {
        
        private static final String ENABLED = "enabled"; //NON-NLS
        
        private final Consumer<ActionEvent> actionPerformed;
        
        private PropertyChangeSupport propertyChangeSupport;
        private Map<String, Object> values;
        private boolean enabled = true;
        
        private DelegatingAction(Consumer<ActionEvent> actionPerformed, String name) {
            this.actionPerformed = actionPerformed;
            if (name != null) {
                putValue(NAME, name);
            }
        }
    
        @Override
        public Object getValue(String key) {
            return (values == null) ? null : values.get(key);
        }
        
        @Override
        public void putValue(String key, Object value) {
            if (ENABLED.equals(key)) {
                setEnabled((value instanceof Boolean) && (Boolean) value);
            } else {
                if (values == null) {
                    values = new HashMap<>();
                }
                Object oldValue = values.put(key, value);
                if (!Objects.equals(oldValue, value)) {
                    firePropertyChangeEvent(key, oldValue, value);
                }
            }
        }
    
        @Override
        public boolean isEnabled() {
            return enabled;
        }
        
        @Override
        public void setEnabled(boolean b) {
            if (b != enabled) {
                enabled = b;
                firePropertyChangeEvent(ENABLED, !b, b);
            }
        }
        
        @Override
        public void addPropertyChangeListener(PropertyChangeListener listener) {
            if (propertyChangeSupport == null) {
                propertyChangeSupport = new PropertyChangeSupport(this);
            }
            propertyChangeSupport.addPropertyChangeListener(listener);
        }
        
        @Override
        public void removePropertyChangeListener(PropertyChangeListener listener) {
            if (propertyChangeSupport != null) {
                propertyChangeSupport.removePropertyChangeListener(listener);
            }
        }
        
        private void firePropertyChangeEvent(String property, Object oldValue, Object newValue) {
            if (propertyChangeSupport != null) {
                propertyChangeSupport.firePropertyChange(property, oldValue, newValue);
            }
        }
    
        @Override
        public void actionPerformed(ActionEvent e) {
            actionPerformed.accept(e);
        }
    }
}
