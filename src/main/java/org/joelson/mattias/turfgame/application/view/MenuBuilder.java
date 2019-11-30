package org.joelson.mattias.turfgame.application.view;

import org.joelson.mattias.turfgame.application.controller.ApplicationActions;

import java.awt.Toolkit;
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
        addMenuItem(fileMenu, "Open...", createMenuShortcutAccelerator('O'), applicationActions::loadUser);
        
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
}
