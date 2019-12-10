package org.joelson.mattias.turfgame.application.view;

import org.joelson.mattias.turfgame.application.controller.ApplicationActions;

import java.awt.Toolkit;
import java.awt.event.InputEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

class MenuBuilder {
    
    private MenuBuilder() throws IllegalAccessException {
        throw new IllegalAccessException("Should not be instantiated!");
    }
    
    public static JMenuBar createApplicationMenu(ApplicationActions applicationActions) {
        JMenuBar menuBar = new JMenuBar();
    
        JMenu fileMenu = addMenu(menuBar,"File");
        addMenuItem(fileMenu, "New User...", createMenuShortcutAccelerator('N'), applicationActions::changeUser);
        addMenuItem(fileMenu, "Open...", createMenuShortcutAccelerator('O'), applicationActions::loadData);
        addMenuItem(fileMenu, "Save...", createMenuShortcutAccelerator('S'), applicationActions::saveData);
        addMenuItem(fileMenu, "Save as...", createMenuShiftedShortcutAccelerator('S'), applicationActions::saveDataAs);
        fileMenu.addSeparator();
        addMenuItem(fileMenu, "Quit", createMenuShortcutAccelerator('Q'), applicationActions::closeApplication);
        
        JMenu turfgameMenu = addMenu(menuBar, "Turfgame");
        addMenuItem(turfgameMenu, "Read zones", null, applicationActions::readZones);
        
        return menuBar;
    }
    
    private static JMenu addMenu(JMenuBar menuBar, String caption) {
        JMenu menu = new JMenu(caption);
        menuBar.add(menu);
        return menu;
    }
    
    private static JMenuItem addMenuItem(JMenu menu, String caption) {
        return addMenuItem(menu, caption, null, null);
    }

    private static JMenuItem addMenuItem(JMenu menu, String caption, KeyStroke accelerator, Runnable action) {
        JMenuItem menuItem = new JMenuItem(caption);
        menu.add(menuItem);
        if (accelerator != null) {
            menuItem.setAccelerator(accelerator);
        }
        if (action != null) {
            menuItem.addActionListener(e -> action.run());
        }
        return menuItem;
    }
    
    private static KeyStroke createMenuShortcutAccelerator(char ch) {
        return KeyStroke.getKeyStroke(ch, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
    }
    
    private static KeyStroke createMenuShiftedShortcutAccelerator(char ch) {
        return KeyStroke.getKeyStroke(ch, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx() | InputEvent.SHIFT_DOWN_MASK);
    }
    
}
