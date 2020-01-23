package org.joelson.mattias.turfgame.application.view;

import org.joelson.mattias.turfgame.application.controller.ApplicationActions;

import java.awt.Toolkit;
import java.awt.event.InputEvent;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

final class MenuBuilder {
    
    private MenuBuilder() throws IllegalAccessException {
        throw new IllegalAccessException("Should not be instantiated!");
    }
    
    static JMenuBar createApplicationMenu(ApplicationActions applicationActions, ApplicationUI applicationUI) {
        JMenuBar menuBar = new JMenuBar();
    
        JMenu fileMenu = addMenu(menuBar,"File");
        addMenuItem(fileMenu, applicationUI.openDatabaseAction());
        addMenuItem(fileMenu, "Close DB", createMenuShortcutAccelerator('W'), applicationUI::closeDatabase);
        addMenuItem(fileMenu, applicationUI.exportDatabaseAction());
        addMenuItem(fileMenu, applicationUI.importDatabaseAction());
        fileMenu.addSeparator();
        addMenuItem(fileMenu, "Quit", createMenuShortcutAccelerator('Q'), applicationActions::closeApplication);
        
        JMenu statisticsMenu = addMenu(menuBar, "Statistics");
        addMenuItem(statisticsMenu, applicationUI.zoneTableAction());
        addMenuItem(statisticsMenu, applicationUI.userTableAction());
        addMenuItem(statisticsMenu, applicationUI.visitTableAction());
        
        JMenu turfgameMenu = addMenu(menuBar, "Turfgame");
        addMenuItem(turfgameMenu, applicationUI.readZonesAction());
        addMenuItem(turfgameMenu, applicationUI.readZonesFromFileAction());
        
        JMenu zundinMenu = addMenu(menuBar, "Zundin");
        addMenuItem(zundinMenu, applicationUI.readTodayFromFileAction());
        
        return menuBar;
    }
    
    private static JMenu addMenu(JMenuBar menuBar, String caption) {
        JMenu menu = new JMenu(caption);
        menuBar.add(menu);
        return menu;
    }
    
    private static JMenuItem addMenuItem(JMenu menu, Action action) {
        JMenuItem menuItem = new JMenuItem(action);
        menu.add(menuItem);
        return menuItem;
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
