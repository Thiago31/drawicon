package drawicon.drawable;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Draws a line element.
 *
 * @author thiago
 */
public class Line implements Drawer {

    /**
     * Pattern to recognize a line element.
     */
    public static final String PATTERN
            = "Line\\[x:\\[(\\d+) (\\d+)\\] y:\\[(\\d+) (\\d+)\\]( colorDraw:(\\w+))?\\]";

    /**
     * Start line x-position.
     */
    private float x1;

    /**
     * Start line y-position.
     */
    private float y1;

    /**
     * End line x-position.
     */
    private float x2;

    /**
     * End line y-position.
     */
    private float y2;

    /**
     * Line color.
     */
    private Color color;

    /**
     * Constructor. Builds a line.
     *
     * @param data string that defines line's attributes. This string must
     * match {@code Line.PATTERN}.
     */
    public Line(String data) {

        Pattern p = Pattern.compile(PATTERN);
        Matcher m = p.matcher(data);
        if (!m.find()) {
            throw new IllegalArgumentException("Line " + data + " is not a valide Line element");
        }

        x1 = Float.parseFloat(m.group(1)) / 100;
        x2 = Float.parseFloat(m.group(2)) / 100;

        y1 = (100.0f - Float.parseFloat(m.group(3))) / 100;
        y2 = (100.0f - Float.parseFloat(m.group(4))) / 100;

        this.color = m.group(6) == null ? Color.BLACK : ColorDic.getColor(m.group(6));

    }

    @Override
    public void draw(Graphics2D g, int size) {
        g.setColor(color);
        size--;
        int x1l = (int) (x1 * size);
        int y1l = (int) (y1 * size);
        int x2l = (int) (x2 * size);
        int y2l = (int) (y2 * size);
        g.drawLine(x1l, y1l, x2l, y2l);
    }

}
