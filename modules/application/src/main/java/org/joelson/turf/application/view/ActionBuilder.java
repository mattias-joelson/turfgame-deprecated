package org.joelson.turf.application.view;

import javax.swing.Action;
import javax.swing.KeyStroke;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

class ActionBuilder {

    private final Consumer<ActionEvent> actionPerformed;
    private String name;
    private String shortDescription;
    private String longDescription;
    private Integer mnemonicKey;
    private KeyStroke acceleratorKey;

    public ActionBuilder(Consumer<ActionEvent> actionPerformed) {
        this.actionPerformed = Objects.requireNonNull(actionPerformed, "Action to perform can not be null");
    }

    public ActionBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ActionBuilder withShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }

    public ActionBuilder withLongDescription(String longDescription) {
        this.longDescription = longDescription;
        return this;
    }

    public ActionBuilder withMnemonicKey(int mnemonicKey) {
        this.mnemonicKey = mnemonicKey;
        return this;
    }

    public ActionBuilder withAcceleratorKey(int acceleratorKey) {
        return withAcceleratorKey(
                KeyStroke.getKeyStroke(acceleratorKey, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
    }

    public ActionBuilder withAcceleratorKey(KeyStroke acceleratorKey) {
        this.acceleratorKey = acceleratorKey;
        return this;
    }

    public Action build() {
        Action action = new DelegatingAction(actionPerformed);
        if (name != null) {
            action.putValue(Action.NAME, name);
        }
        if (shortDescription != null) {
            action.putValue(Action.SHORT_DESCRIPTION, shortDescription);
        }
        if (longDescription != null) {
            action.putValue(Action.LONG_DESCRIPTION, longDescription);
        }
        if (mnemonicKey != null) {
            action.putValue(Action.MNEMONIC_KEY, mnemonicKey);
        }
        if (acceleratorKey != null) {
            action.putValue(Action.ACCELERATOR_KEY, acceleratorKey);
        }
        return action;
    }

    private static final class DelegatingAction implements Action {

        private static final String ENABLED = "enabled";

        private final Consumer<ActionEvent> actionPerformed;

        private PropertyChangeSupport propertyChangeSupport;
        private Map<String, Object> values;
        private boolean enabled = true;

        private DelegatingAction(Consumer<ActionEvent> actionPerformed) {
            this.actionPerformed = actionPerformed;
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
