package org.joelson.turf.application.view;

import org.joelson.turf.application.controller.ApplicationActions;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import java.awt.Toolkit;
import java.awt.event.InputEvent;

final class MenuBuilder {

    private MenuBuilder() throws IllegalAccessException {
        throw new IllegalAccessException("Should not be instantiated!");
    }

    static JMenuBar createApplicationMenu(ApplicationActions applicationActions, ApplicationUI applicationUI) {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = addMenu(menuBar, "File");
        addMenuItem(fileMenu, applicationUI.openDatabaseAction());
        addMenuItem(fileMenu, "Close DB", createMenuShortcutAccelerator('W'), applicationUI::closeDatabase);
        addMenuItem(fileMenu, applicationUI.exportDatabaseAction());
        addMenuItem(fileMenu, applicationUI.importDatabaseAction());
        fileMenu.addSeparator();
        addMenuItem(fileMenu, "Quit", createMenuShortcutAccelerator('Q'), applicationActions::closeApplication);

        JMenu statisticsMenu = addMenu(menuBar, "Statistics");
        addMenuItem(statisticsMenu, applicationUI.regionTableAction());
        addMenuItem(statisticsMenu, applicationUI.regionHistoryTableAction());
        addMenuItem(statisticsMenu, applicationUI.zoneTableAction());
        addMenuItem(statisticsMenu, applicationUI.zoneHistoryTableAction());
        addMenuItem(statisticsMenu, applicationUI.zonePointsHistoryTableAction());
        addMenuItem(statisticsMenu, applicationUI.userTableAction());
        addMenuItem(statisticsMenu, applicationUI.visitTableAction());
        addMenuItem(statisticsMenu, applicationUI.sessionTableAction());
        addMenuItem(statisticsMenu, applicationUI.statisticsAction());
        addMenuItem(statisticsMenu, applicationUI.zoneOwnershipGraphAction());
        addMenuItem(statisticsMenu, applicationUI.zoneTakeGraphAction());
        addMenuItem(statisticsMenu, applicationUI.uniqueRoundZoneGraphAction());
        addMenuItem(statisticsMenu, applicationUI.zoneVisitTableAction());
        addMenuItem(statisticsMenu, applicationUI.municipalityVisitAction());

        JMenu turfgameMenu = addMenu(menuBar, "Turfgame");
        addMenuItem(turfgameMenu, applicationUI.readZonesAction());
        addMenuItem(turfgameMenu, applicationUI.readZonesFromFileAction());

        JMenu lundkvistMenu = addMenu(menuBar, "Lundkvist");
        addMenuItem(lundkvistMenu, applicationUI.readMunicipalityFromFileAction());

        JMenu zundinMenu = addMenu(menuBar, "Zundin");
        addMenuItem(zundinMenu, applicationUI.readTodayFromFileAction());

        JMenu wardedMenu = addMenu(menuBar, "Warded");
        addMenuItem(wardedMenu, applicationUI.readWardedFromFileAction());

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
        return KeyStroke.getKeyStroke(ch,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx() | InputEvent.SHIFT_DOWN_MASK);
    }
}
