package drawicon.drawable;

import java.awt.Graphics2D;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Draws a circle element.
 * @author thiago
 */
public class Circle extends AbstractShape {

    /**
     * Pattern to recognize a circle element.
     */
    public static final String PATTERN =
            "Circle\\[x:(\\d+) y:(\\d+) r:(\\d+) draw:(on|off) fill:(on|off)( colorDraw:(\\w*))?( colorFill:(\\w+))?\\]";

    /**
     * Circle top left x-position.
     */
    private float xv;
    
    /**
     * Circle top left y-position.
     */
    private float yv;
    
    /**
     * Circle diamenter.
     */
    private float l;
    
    /**
     * Constructor. Builds a circle.
     * @param data string that defines circle's attributes. This string must
     * match {@code Circle.PATTERN}.
     */
    public Circle(String data) {
        Pattern p = Pattern.compile(PATTERN);
        Matcher m = p.matcher(data);
        if (!m.find()) {
            throw new IllegalArgumentException("Circle " + data + " is not a valide Circle element");
        }

        float r = Float.parseFloat(m.group(3));

        xv = (Float.parseFloat(m.group(1)) - r) / 100;
        yv = (100 - Float.parseFloat(m.group(2)) - r) / 100;
        l = (2 * r) / 100;
        setColorAttributes(m.group(4), m.group(5), m.group(7), m.group(9));
    }

    @Override
    public void draw(Graphics2D g, int size) {

        int xi = (int) (xv * size);
        int yi = (int) (yv * size);
        int li = (int) (l * size);

        if (toFill) {
            g.setColor(fillColor);
            g.fillOval(xi, yi, li, li);
        }

        if (toDraw) {
            li--;
            g.setColor(drawColor);
            g.drawOval(xi, yi, li, li);
        }
    }

}
