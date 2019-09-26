package drawicon.gui;

import drawicon.drawable.IconDrawer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

/**
 * Main window of this application. This windows contains two panels:
 * <ul>
 * <li>Left panel, in which user can write codes to draw icon.</li>
 * <li>Right panel, in which icon is drawn after clicking <b>File -> Make
 * icons</b>
 * or opening a icx file in <b>File -> open</b></li>
 * </ul>
 * This program is used to draw icons for android applications in a
 * non-interactive manner.
 *
 * @author thiago
 */
public class MainWindow extends JFrame {

    /**
     * Default serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Drawer that draws a icon from data read from file or written in Left
     * panel.
     */
    private final IconDrawer iconDrawer;

    /**
     * Text area to receive text input, namely, the code to draw icons.
     */
    private final JTextArea textArea;

    /**
     * Panel that shows drawn icon.
     */
    private final ImageViewer viewer;

    /**
     * File chooser to select files for input/output actions.
     */
    private final JFileChooser fc;

    /**
     * Constructors. Builds this main window.
     */
    public MainWindow() {
        iconDrawer = new IconDrawer();
        textArea = new JTextArea(40, 50);
        viewer = new ImageViewer();
        fc = new JFileChooser();
        setTitle("Draw Icon");
        setJMenuBar(new MyMenuBar());
        setContentPane(new MyContent(textArea, viewer));
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * Menu bar for this main window.
     */
    private class MyMenuBar extends JMenuBar {

        /**
         * Default serial version UID.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Constructor. Builds this menu bar. This menu bar contains only one
         * menu: <i>File</li>, and five submenus:
         * <ul>
         * <li><i>Open</i>: opens a icx file and displays it in Lift panel</li>
         * <li><i>Save</i>: saves a icx file</li>
         * <li><i>Make icons</i>: make icons and displays them in Right
         * panel</li>
         * <li><i>Save icons</i>: save icons to some directory (usually
         * "res")</li>
         * <li><i>Exit</i>: Exits program</li>
         * </ul>
         */
        public MyMenuBar() {
            JMenu file = new JMenu("File");

            int shortcutMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

            JMenuItem save = new JMenuItem("Save");
            save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, shortcutMask));
            save.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fc.setDialogTitle("Save icx file");
                    fc.setSelectedFile(new File("icon.icx"));
                    int option = fc.showSaveDialog(MainWindow.this);
                    if (option != JFileChooser.APPROVE_OPTION) {
                        return;
                    }
                    File selectedFile = fc.getSelectedFile();
                    if (selectedFile.exists()) {
                        int response = JOptionPane.showConfirmDialog(MainWindow.this,
                                "The file " + selectedFile.getName()
                                + " already exists.\nDo you want to replace it?",
                                "Confirm Save", JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE);
                        if (response != JOptionPane.YES_OPTION) {
                            return;
                        }
                    }
                    try (PrintWriter pw = new PrintWriter(selectedFile)) {
                        pw.print(textArea.getText());
                    } catch (IOException ioe) {
                        JOptionPane.showMessageDialog(MainWindow.this,
                                "Error: couldn't write to file"
                                + selectedFile.getName(), "Writing Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            JMenuItem saveIcons = new JMenuItem("Save icons");
            saveIcons.setEnabled(false);
            saveIcons.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, shortcutMask));
            saveIcons.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String[] files = SaveIconsDialog.showDialog(MainWindow.this);
                    if (files == null) {
                        return;
                    }
                    iconDrawer.save(files[0], files[1]);
                }
            });

            JMenuItem makeIcons = new JMenuItem("Make icons");
            makeIcons.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, shortcutMask));
            makeIcons.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    iconDrawer.read(textArea.getText());
                    viewer.repaint();
                    saveIcons.setEnabled(true);
                    if (iconDrawer.someError()) {
                        WarningDialog.showDialog(MainWindow.this,
                                iconDrawer.getSyntaxErrorLines(),
                                iconDrawer.getNotElementLines());
                    }
                }
            });

            JMenuItem open = new JMenuItem("Open");
            open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, shortcutMask));
            open.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fc.setDialogTitle("Open icx file");
                    fc.setSelectedFile(null);
                    int option = fc.showOpenDialog(MainWindow.this);
                    if (option != JFileChooser.APPROVE_OPTION) {
                        return;
                    }
                    File selectedFile = fc.getSelectedFile();
                    try (FileReader fr = new FileReader(selectedFile);
                            BufferedReader br = new BufferedReader(fr)) {
                        textArea.setText("");
                        while (br.ready()) {
                            String line = br.readLine();
                            textArea.append(line);
                            textArea.append("\n");
                        }
                        makeIcons.doClick();
                    } catch (IOException ioe) {
                        JOptionPane.showMessageDialog(MainWindow.this, "Error: couldn't open file"
                                + selectedFile.getName() + " for reading.",
                                "Reading Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            JMenuItem exit = new JMenuItem("Exit");
            exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, shortcutMask));
            exit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });

            file.add(open);
            file.add(save);
            file.add(makeIcons);
            file.add(saveIcons);
            file.add(exit);
            add(file);
        }

    }

    /**
     * Content panel of this window. It contains two panels: Left panel, in
     * which user can write codes to draw icon, and Right panel, in which icon
     * is drawn. Combo box below Right panel is used to select icon size to
     * display.
     */
    private class MyContent extends JPanel {

        /**
         * Default serial version UID.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Constructor. Builds this panel.
         *
         * @param textArea text area to be displayed in Left panel.
         * @param viewer image viewer to be displayed in Right panel.
         */
        public MyContent(JTextArea textArea, ImageViewer viewer) {
            super(new GridLayout(1, 2, 4, 4));
            textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            textArea.setMargin(new Insets(4, 4, 4, 4));
            JScrollPane scroller = new JScrollPane(textArea);
            scroller.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            JLabel labelText = new JLabel("TEXT EDITOR", JLabel.CENTER);

            JPanel textPanel = new JPanel(new BorderLayout());
            textPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

            textPanel.add(scroller, BorderLayout.CENTER);
            textPanel.add(labelText, BorderLayout.NORTH);

            JPanel drawPanel = new JPanel(new BorderLayout());

            JLabel labelImage = new JLabel("IMAGE VIEWER", JLabel.CENTER);
            String[] sizeNames = {"Full screen", "ldpi", "mdpi", "hdpi",
                "xhdpi", "xxhdpi", "xxxhdpi"};
            int[] sizes = {-1, 36, 48, 72, 96, 144, 192};

            JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

            JComboBox<String> combo = new JComboBox<>(sizeNames);
            combo.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        int size = sizes[combo.getSelectedIndex()];
                        viewer.setImageSize(size);
                    }
                }

            });
            JLabel labelSize = new JLabel("image size:");
            comboPanel.add(labelSize);
            comboPanel.add(combo);

            drawPanel.add(comboPanel, BorderLayout.SOUTH);
            drawPanel.add(viewer, BorderLayout.CENTER);
            drawPanel.add(labelImage, BorderLayout.NORTH);
            drawPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

            add(textPanel);
            add(drawPanel);

        }

    }

    /**
     * Panel to display icon image.
     */
    private class ImageViewer extends JPanel {

        /**
         * Default serial version UID.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Image size. If set to -1, screen size is used to compute image size,
         * otherwise, image is drawn with this size.
         */
        private int graphicSize;

        /**
         * Constructor. Builds this Image Viewer.
         */
        public ImageViewer() {
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
            graphicSize = -1;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            int size;
            if (graphicSize == -1) {
                size = Math.min(getWidth(), getHeight());
                size *= 4;
                size /= 5;
            } else {
                size = graphicSize;
            }
            BufferedImage image = iconDrawer.getImage(size);

            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;

            g.drawImage(image, x, y, this);

        }

        /**
         * Sets image size to be drawn.
         * @param size image size.
         */
        public void setImageSize(int size) {
            graphicSize = size;
            repaint();
        }
    }
}
