package drawicon.drawable;

import java.awt.Graphics2D;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Draws a Ellipse element.
 *
 * @author thiago
 */
public class Ellipse extends AbstractShape {

    /**
     * Pattern to recognize an ellipse element.
     */
    public static final String PATTERN
            = "Ellipse\\[x:(\\d+) y:(\\d+) rw:(\\d+) rh:(\\d+) draw:(on|off) fill:(on|off)( colorDraw:(\\w+))?( colorFill:(\\w+))?\\]";

    /**
     * Ellipse top left x-position.
     */
    private float xv;

    /**
     * Ellipse top left y-position.
     */
    private float yv;

    /**
     * Ellipse diameter in x-axis.
     */
    private float lw;

    /**
     * Ellipse diameter in y-axis.
     */
    private float lh;

    /**
     * Constructor. Builds an ellipse.
     *
     * @param data string that defines ellipse's attributes. This string must
     * match {@code Ellipse.PATTERN}.
     */
    public Ellipse(String data) {
        Pattern p = Pattern.compile(PATTERN);
        Matcher m = p.matcher(data);
        if (!m.find()) {
            throw new IllegalArgumentException("Ellipse " + data + " is not a valide Ellipse element");
        }

        float rw = Float.parseFloat(m.group(3));
        float rh = Float.parseFloat(m.group(4));
        xv = (Float.parseFloat(m.group(1)) - rw) / 100;
        yv = (100.0f - Float.parseFloat(m.group(2)) - rh) / 100;
        lw = (2 * rw) / 100;
        lh = (2 * rh) / 100;
        
        setColorAttributes(m.group(5), m.group(6), m.group(8), m.group(10));

    }

    @Override
    public void draw(Graphics2D g, int size) {

        int xi = (int) (xv * size);
        int yi = (int) (yv * size);
        int liw = (int) (lw * size);
        int lih = (int) (lh * size);

        if (toFill) {
            g.setColor(fillColor);
            g.fillOval(xi, yi, liw, lih);
        }

        if (toDraw) {
            liw--;
            lih--;
            g.setColor(drawColor);
            g.drawOval(xi, yi, liw, lih);
        }
    }

}
