package drawicon.drawable;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.imageio.ImageIO;

/**
 * This class draws an icon based on commands read from a string. Commands must
 * be written in icx format. Elements that are drawn by IconDrawer instances
 * includes:
 * <ul>
 * <li>lines</li>
 * <li>circles</li>
 * <li>ellipses</li>
 * <li>rectangles</li>
 * <li>poligons</li>
 * <li>regular poligons</li>
 * </ul>
 * Each drawable element implements Drawer interface and is used to draw this
 * icon. Icons can be draw in different sizes using {@code getImage(int size)}
 * method.
 *
 * @author thiago
 */
public class IconDrawer {

    /**
     * Icon sizes.
     */
    private final int[] sizes = {36, 48, 72, 96, 144, 192};

    /**
     * Directories to put icons.
     */
    private final String[] pathNames = {"mipmap-ldpi", "mipmap-mdpi", "mipmap-hdpi",
        "mipmap-xhdpi", "mipmap-xxhdpi", "mipmap-xxxhdpi"};

    /**
     * Drawable elements in this {@code IconDrawer}.
     */
    private final ArrayList<Drawer> elements;

    /**
     * Stores lines not recognized as drawable elements in {@code read()}
     * method.
     */
    private final ArrayList<String> notElements;

    /**
     * Stores lines with some syntax error in {@code read()} method.
     */
    private final ArrayList<String> syntaxErrorElements;

    /**
     * Marks if some error occurred in last {@code read()} method call.
     */
    private boolean lastError;

    /**
     * File name separator.
     */
    private static final String SEPARATOR = System.getProperty("file.separator");

    /**
     * Constructor. Makes a new IconDrawer.
     */
    public IconDrawer() {
        elements = new ArrayList<>();
        notElements = new ArrayList<>();
        syntaxErrorElements = new ArrayList<>();
    }

    /**
     * Reads commands to draw image from string. Each command is separated with
     * end line character (\n).
     *
     * @param data input string to read command from.
     */
    public void read(String data) {

        elements.clear();
        notElements.clear();
        syntaxErrorElements.clear();
        lastError = false;
        Scanner scanner = new Scanner(data);

        int lineCount = 1;
        while (scanner.hasNext()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                lineCount++;
                continue;
            }
            int b1 = line.indexOf('[');
            if (b1 == -1) {
                addNotElementError(line, lineCount);
                lineCount++;
                continue;
            }
            String elementType = line.substring(0, b1);
            switch (elementType) {
                case "Line":
                    if (line.matches(Line.PATTERN)) {
                        elements.add(new Line(line));
                    } else {
                        addSyntaxError(line, lineCount);
                    }
                    break;
                case "Circle":
                    if (line.matches(Circle.PATTERN)) {
                        elements.add(new Circle(line));
                    } else {
                        addSyntaxError(line, lineCount);
                    }
                    break;
                case "Ellipse":
                    if (line.matches(Ellipse.PATTERN)) {
                        elements.add(new Ellipse(line));
                    } else {
                        addSyntaxError(line, lineCount);
                    }
                    break;
                case "Rectangle":
                    if (line.matches(Rectangle.PATTERN)) {
                        elements.add(new Rectangle(line));
                    } else {
                        addSyntaxError(line, lineCount);
                    }
                    break;
                case "Poligon":
                    if (line.matches(Poligon.PATTERN)) {
                        elements.add(new Poligon(line));
                    } else {
                        addSyntaxError(line, lineCount);
                    }
                    break;
                case "RegularPoligon":
                    if (line.matches(RegularPoligon.PATTERN)) {
                        elements.add(new RegularPoligon(line));
                    } else {
                        addSyntaxError(line, lineCount);
                    }
                    break;
                default:
                    addNotElementError(line, lineCount);
            }
            lineCount++;

        }
    }

    /**
     * Stores line not properly recognized in {@code read()} method call.
     *
     * @param line line with syntax error.
     * @param lineNumber line number.
     */
    private void addSyntaxError(String line, int lineNumber) {
        syntaxErrorElements.add("line " + lineNumber + ": " + line);
        lastError = true;
    }

    /**
     * Stores line not properly recognized in {@code read()} method call.
     *
     * @param line line not recognized as valid drawable element.
     * @param lineNumber line number.
     */
    private void addNotElementError(String line, int lineNumber) {
        notElements.add("line " + lineNumber + ": " + line);
        lastError = true;
    }

    /**
     * Makes an imagem using drawable elements defined in read() method.
     *
     * @param size image size in pixels.
     * @return an image with size X size pixels.
     */
    public BufferedImage getImage(int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        elements.stream().forEach((d) -> {
            d.draw(g2, size);
        });
        g2.dispose();
        return image;
    }

    /**
     * Save icons in specified directory.
     *
     * @param dir directory to save icons. A subdirectory to each icon will be
     * created.
     * @param fileName icon file name. All icons have this same fale name.
     */
    public void save(File dir, String fileName) {
        if (!dir.exists()) {
            throw new IllegalArgumentException("Error: directory " + dir.getName() + " doesn't exists.");
        }
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("Error: " + dir.getName() + " is not a directory.");
        }
        String dirName = dir.getAbsolutePath();
        if (!dirName.endsWith(SEPARATOR)) {
            dirName += SEPARATOR;
        }
        for (int i = 0; i < pathNames.length; i++) {
            File outDir = new File(dirName + pathNames[i]);
            outDir.mkdir();
            File out = new File(dirName + pathNames[i] + SEPARATOR + fileName);
            BufferedImage icon = getImage(sizes[i]);
            try {
                ImageIO.write(icon, "PNG", out);
            } catch (IOException ioe) {

            }
        }
    }

    /**
     * Save icons in specified directory.
     *
     * @param dirName directory name to save icons. A subdirectory to each icon
     * will be created.
     * @param fileName icon file name. All icons have this same fale name.
     */
    public void save(String dirName, String fileName) {
        File dir = new File(dirName);
        save(dir, fileName);
    }

    /**
     * Returns a list containing all lines not recognized as drawable elements
     * in {@code read()} method call.
     *
     * @return list containing all lines not recognized as drawable elements.
     */
    public String[] getNotElementLines() {
        return notElements.toArray(new String[0]);
    }

    /**
     * Returns a list containing all lines with some syntax error in
     * {@code read()} method call.
     *
     * @return list containing all lines with some syntax error.
     */
    public String[] getSyntaxErrorLines() {
        return syntaxErrorElements.toArray(new String[0]);
    }

    /**
     * Returns true if some error occurs in last {@code read()} method call. Two
     * errors can occur: a line is not recognized as valid drawable element, or
     * a line contains some syntax error.
     *
     * @return true if some error occurs in last {@code read()} method call.
     */
    public boolean someError() {
        return lastError;
    }
}
