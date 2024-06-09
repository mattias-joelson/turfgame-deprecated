package org.joelson.mattias.turfgame.application.view;

import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public final class PopupMenuUtil {

    private static class SelectionPopupMenuListener implements PopupMenuListener {

        private final JTable table;
        private final JPopupMenu popupMenu;

        private SelectionPopupMenuListener(JTable table, JPopupMenu popupMenu) {
            this.table = table;
            this.popupMenu = popupMenu;
        }

        @Override
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            SwingUtilities.invokeLater(() -> {
                int rowAtPoint = table.rowAtPoint(SwingUtilities.convertPoint(popupMenu, 0, 0, table));
                if (rowAtPoint > -1) {
                    table.setRowSelectionInterval(rowAtPoint, rowAtPoint);
                }
            });
        }

        @Override
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

        }

        @Override
        public void popupMenuCanceled(PopupMenuEvent e) {

        }
    }

    private PopupMenuUtil() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated");
    }

    public static void addToTable(JTable table, JPopupMenu popupMenu) {
        table.setComponentPopupMenu(popupMenu);
        popupMenu.addPopupMenuListener(new SelectionPopupMenuListener(table, popupMenu));
    }
}
