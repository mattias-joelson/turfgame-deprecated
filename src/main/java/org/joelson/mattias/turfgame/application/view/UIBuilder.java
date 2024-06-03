package org.joelson.mattias.turfgame.application.view;

import java.awt.Container;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class UIBuilder {
    
//    public static final String NEW_USER_DIALOG_TITLE = "New User";
    
    private UIBuilder() throws IllegalAccessException {
        throw new IllegalAccessException("Should not be instantiated!");
    }
    
//    public static JFrame createApplicationFrame() {
//        JFrame frame = new JFrame("Turfgame Statistics");
//        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
//        frame.addWindowListener(createFrameWindowListener(frame));
//
//        frame.setJMenuBar(MenuBuilder.createApplicationMenu());
//
//        return frame;
//    }
    
//    private static WindowListener createFrameWindowListener(JFrame frame) {
//        WindowListener windowListener = new WindowAdapter() {
//            public void windowClosing(WindowEvent e) {
//                // TODO close if OK, otherwise abort
//                frame.dispose();
//                System.exit(0);
//            }
//        };
//
//        return windowListener;
//    }

//    public static JDialog queryUserDialog(Frame frame, String title) {
//        JDialog dialog = new JDialog(frame, title);
//
//        Container contentPane = dialog.getContentPane();
//        contentPane.setLayout(new GridLayout(3, 2));
//        contentPane.add(new JLabel("User name"));
//        contentPane.add(new JTextField());
//        contentPane.add(new JLabel("User ID"));
//        contentPane.add(new JTextField());
//        contentPane.add(new JButton("Cancel"));
//        contentPane.add(new JButton("OK"));
//
//        return dialog;
//    }
    
//    public static Container createInputFieldContainer(List<String> fields, List<Action> actions) {
//        return null;
//    }
    
    //public static void show
}
