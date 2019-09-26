package drawicon.drawable;

import java.awt.Graphics2D;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Draws a rectangle element.
 * @author thiago
 */
public class Rectangle extends AbstractShape{
    
    /**
     * Pattern to recognize a rectangle element.
     */    
    public static final String PATTERN =
          "Rectangle\\[x:\\[(\\d+) (\\d+)\\] y:\\[(\\d+) (\\d+)\\] draw:(on|off) fill:(on|off)( colorDraw:(\\w+))?( colorFill:(\\w+))?\\]";  

    /**
     * Rectangle top left x-position.
     */    
    private float xv;
    
    /**
     * Rectangle top left y-position.
     */
    private float yv;
    
    /**
     * Rectangle width.
     */
    private float lw;
    
    /**
     * Rectangle height.
     */
    private float lh;
    
    /**
     * Constructor. Builds a rectangle.
     *
     * @param data string that defines rectangle's attributes. This string must
     * match {@code Rectangle.PATTERN}.
     */
    public Rectangle(String data){
        Pattern p = Pattern.compile(PATTERN);
        Matcher m = p.matcher(data);
        if(!m.find()){
            throw new IllegalArgumentException("Rectangle " + data + " is not a valide Rectangle element");
        }
        
        float x1 = Float.parseFloat(m.group(1));
        float x2 = Float.parseFloat(m.group(2));
        float y1 = Float.parseFloat(m.group(3));
        float y2 = Float.parseFloat(m.group(4));
        
        xv = Math.min(x1, x2) / 100;
        yv = (100.0f - Math.max(y1, y2)) / 100;
        lw = Math.abs(x1 - x2) / 100;
        lh = Math.abs(y1 - y2) / 100;
        
        setColorAttributes(m.group(5), m.group(6), m.group(8), m.group(10));
    }
    
    @Override
    public void draw(Graphics2D g, int size) {

        int xi = (int)(xv * size);
        int yi = (int)(yv * size);
        int lwi = (int)(lw * size);
        int lhi = (int)(lh * size);
        
        if(toFill){
            g.setColor(fillColor);
            g.fillRect(xi, yi, lwi, lhi);
        }
        
        if(toDraw){
            lwi--;
            lhi--;
            g.setColor(drawColor);
            g.drawRect(xi, yi, lwi, lhi);
        }
    }
    
}
