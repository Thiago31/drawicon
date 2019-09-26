package drawicon.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 * This dialog window is displayed if <b>Save icons</b> menu item is selected.
 * User must select a directory to save icon directories, either writing
 * directory path in "directory:" text box, or choosing it at browser button.
 * Usually, "res" directory under some android project folder are the local
 * chosen to save icons.
 * User also must write a file name for icons in "file name:" text box. All
 * icons are saved in png file format. PNG extension is automatically put in
 * file name if not done by user.
 * @author Thiago
 */
public class SaveIconsDialog extends JDialog {

    /**
     * Default serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constante to define that user clicks OK button.
     */
    private static final int OK = 1;
    
    /**
     * Constante to define that user clicks CANCEL button.
     */
    private static final int CANCEL = 2;
    
    /**
     * Option selected by user, either OK or CANCEL, or zero if user
     * clicks close button.
     */
    private int option = 0;
    
    /**
     * File name entered by user.
     */
    private String fileName = "";
    
    /**
     * Directory path entered by user.
     */
    private String dirName = "";

    /**
     * Displays this dialog window.
     * @param parent parent frame for this dialog.
     * @return String array with two elements: first is directory path and
     * second is file name.
     */
    public static String[] showDialog(JFrame parent) {
        SaveIconsDialog saveIcon = new SaveIconsDialog(parent);
        saveIcon.setVisible(true);
        if (saveIcon.getSelectedOption() != SaveIconsDialog.OK) {
            return null;
        }
        return new String[]{saveIcon.getDirName(), saveIcon.getFileName()};
    }

    /**
     * Builds this dialog window. User couldn't acessess constructor directly,
     * instead, user must call showDialog() method to display this dialog.
     * @param parent parent frame for this dialog.
     */
    private SaveIconsDialog(JFrame parent) {
        super(parent, "Save icon images", true);

        JPanel content = new JPanel(new BorderLayout());

        JPanel main = new JPanel(new GridLayout(2, 1));
        JPanel line1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel dir = new JLabel("directory:");
        JTextField dirText = new JTextField(26);
        Border normalBorder = dirText.getBorder();
        Border errorBorder = BorderFactory.createLineBorder(Color.RED);
        JButton browser = new JButton("Browser");
        browser.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                JFileChooser fc = new JFileChooser();
                fc.setDialogTitle("Open directory");
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int choose = fc.showOpenDialog(SaveIconsDialog.this);
                if(choose == JFileChooser.APPROVE_OPTION){
                    File file = fc.getSelectedFile();
                    dirText.setText(file.getAbsolutePath());
                }
            }
        });
        dir.setLabelFor(dirText);
        line1.add(dir);
        line1.add(dirText);
        line1.add(browser);

        JPanel line2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel fil = new JLabel("file name:");
        JTextField fileText = new JTextField(26);
        fil.setLabelFor(fileText);
        line2.add(fil);
        line2.add(fileText);

        main.add(line1);
        main.add(line2);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton cancel = new JButton("CANCEL");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                option = CANCEL;
                dispose();
            }
        });

        JButton ok = new JButton("OK");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (testDir(dirText.getText())) {
                    dirText.setBorder(normalBorder);
                    dirName = dirText.getText();
                } else {
                    JOptionPane.showMessageDialog(SaveIconsDialog.this,
                            "Error: directory " + dirText.getText()
                            + " doesn't exist or you don't have permission "
                            + "to write into it."
                            + "\nPlease enter a valid directory or choose it"
                            + " by clicking Browser button", "Directory not found",
                            JOptionPane.ERROR_MESSAGE);
                    dirText.setBorder(errorBorder);
                    return;
                }
                if (fileText.getText().equals("")) {
                    JOptionPane.showMessageDialog(SaveIconsDialog.this,
                            "Please enter a name to your icons in file name"
                            + " text box", "Empty file name",
                            JOptionPane.WARNING_MESSAGE);
                    fileText.setBorder(errorBorder);
                    return;
                } else{
                    fileText.setBorder(normalBorder);
                }
                fileName = fileText.getText();
                if (!fileName.endsWith(".png")) {
                    fileName += ".png";
                }
                option = OK;
                dispose();
            }
        });
        buttonPanel.add(cancel);
        buttonPanel.add(ok);

        content.add(main, BorderLayout.CENTER);
        content.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(content);
        pack();
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
    }

    /**
     * Returns option selected by user.
     * @return option selected by user.
     */
    private int getSelectedOption() {
        return option;
    }

    /**
     * Returns file name entered by user.
     * @return file name entered by user.
     */
    private String getFileName() {
        return fileName;
    }

    /**
     * Returns file path entered by user.
     * @return file path entered by user.
     */
    private String getDirName() {
        return dirName;
    }

    /**
     * Tests if path is a valid and writable directory.
     * @param testdir path to be tested.
     * @return true if path is a directory and user can write into it.
     */
    private boolean testDir(String testdir) {
        File dir = new File(testdir);
        return dir.isDirectory() && dir.canWrite();
    }
}
