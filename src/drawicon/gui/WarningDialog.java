package drawicon.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * This dialog shows warning messages if some error occur while reading input
 * file. Two errors can occur: A line is not recognized as valid drawable
 * element, or, a line contains some syntax error, such as lacking a ']' end
 * character. Error lines are skiped by application and element from this line
 * is not drawn. User must fix error line in order to properly draw her/his
 * icon.
 *
 * @author thiago
 */
public class WarningDialog extends JDialog {

    /**
     * Default serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Font for this dialog.
     */
    private final Font labelFont = new Font(Font.DIALOG, Font.PLAIN, 12);

    /**
     * Shows this message dialog.
     *
     * @param parent parent frame
     * @param syntaxErrors list containing syntax errors.
     * @param notElementErrors list containing not element errors.
     */
    public static void showDialog(JFrame parent, String[] syntaxErrors,
            String[] notElementErrors) {
        WarningDialog dialog = new WarningDialog(parent, syntaxErrors,
                notElementErrors);
        dialog.setVisible(true);
    }

    /**
     * Constructor. Buiulds this message dialog.
     *
     * @param parent parent frame
     * @param syntaxErrors list containing syntax errors.
     * @param notElementErrors list containing not element errors.
     */
    private WarningDialog(JFrame parent, String[] syntaxErrors,
            String[] notElementErrors) {
        super(parent, "Reading errors", true);

        JLabel label = new JLabel("<html><p align=\"center\">Warning: Some errors are found in your"
                + " input and are shown below."
                + "<br/>These lines are not included in drawing commands.<br/>"
                + "Please fix them to properly draw your icon:</p></html>");
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(6, 6, 6, 6),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.RED),
                        BorderFactory.createEmptyBorder(4, 4, 4, 4))));
        label.setFont(labelFont);

        JPanel errorsList = new JPanel(new GridLayout(0, 1, 4, 4));

        if (syntaxErrors.length > 0) {
            errorsList.add(new ViewErrors(syntaxErrors, 
                    "Lines with syntax errors:"));
        }
        if (notElementErrors.length > 0) {
            errorsList.add(new ViewErrors(notElementErrors, 
                    "Lines not recognized as valid drawable elements:"));
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton ok = new JButton("OK");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPanel.add(ok);

        JPanel content = new JPanel(new BorderLayout());
        content.add(label, BorderLayout.NORTH);
        content.add(errorsList, BorderLayout.CENTER);
        content.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(content);
        pack();
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(parent);
    }

    /**
     * Shows a scrollab text with message errors.
     */
    private class ViewErrors extends JPanel {

        /**
         * Default serial version UID.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Constructor. Builds this View Error panel.
         *
         * @param errors list containing errors to show.
         * @param title error title.
         */
        public ViewErrors(String[] errors, String title) {
            super(new BorderLayout());
            JLabel titleLabel = new JLabel(title);
            titleLabel.setBorder(BorderFactory.createEmptyBorder(6, 4, 0, 4));
            titleLabel.setFont(labelFont);

            JTextArea text = new JTextArea(10, 60);
            text.setEditable(false);
            text.setMargin(new Insets(4, 4, 4, 4));
            text.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            for (String error : errors) {
                text.append(error);
                text.append("\n");
            }
            JScrollPane scroller = new JScrollPane(text);
            scroller.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(4, 4, 4, 4),
                    BorderFactory.createLineBorder(Color.BLACK)));

            add(titleLabel, BorderLayout.NORTH);
            add(scroller, BorderLayout.CENTER);

        }

    }

}
