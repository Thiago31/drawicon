package drawicon.drawable;

import static drawicon.drawable.Poligon.PATTERN;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Draws a regular poligon element.
 *
 * @author thiago
 */
public class RegularPoligon extends Poligon {

    /**
     * Pattern to recognize a regular poligon element.
     */
    public static final String PATTERN
            = "RegularPoligon\\[x:(\\d+) y:(\\d+) r:(\\d+) n:(\\d+) dir:(a|v) draw:(on|off) fill:(on|off)( colorDraw:(\\w+))?( colorFill:(\\w+))?\\]";

    /**
     * Constructor. Builds a regular poligon.
     *
     * @param data string that defines poligon's attributes. This string must
     * match {@code RegularPoligon.PATTERN}.
     */
    public RegularPoligon(String data) {
        Pattern p = Pattern.compile(PATTERN);
        Matcher m = p.matcher(data);
        if (!m.find()) {
            throw new IllegalArgumentException("RegularPoligon " + data
                    + " is not a valide RegularPoligon element");
        }

        float xc = (Float.parseFloat(m.group(1))) / 100;
        float yc = (100.0f - Float.parseFloat(m.group(2))) / 100;
        float r = (Float.parseFloat(m.group(3))) / 100;
        int n = Integer.parseInt(m.group(4));
        if (n < 3) {
            throw new IllegalArgumentException("Number of vertices in"
                    + " Regular Poligon must be greather than or equal to 3."
                    + " Illegal value is done -> n=" + n);
        }
        
        float px = xc;
        float py = yc - r;
        
        double angle = 2 * Math.PI / n;
        
        float tx, ty;
        if(m.group(5).equals("a")){
            double angleM = angle / 2;
            tx = (float)(px*Math.cos(angleM) - py*Math.sin(angleM)
                    - xc*Math.cos(angleM) + yc*Math.sin(angleM) + xc);
            ty = (float)(px*Math.sin(angleM) + py*Math.cos(angleM)
                    - xc*Math.sin(angleM) - yc*Math.cos(angleM) + yc);
            px = tx;
            py = ty;            
        }

        xp = new float[n];
        yp = new float[n];
        xp[0] = px;
        yp[0] = py;
        
        for(int i = 1; i < n; i++){
            tx = (float)(px*Math.cos(angle) - py*Math.sin(angle)
                    - xc*Math.cos(angle) + yc*Math.sin(angle) + xc);
            ty = (float)(px*Math.sin(angle) + py*Math.cos(angle)
                    - xc*Math.sin(angle) - yc*Math.cos(angle) + yc);
            px = tx;
            py = ty;
            xp[i] = px;
            yp[i] = py;
        }
        this.setColorAttributes(m.group(6), m.group(7), m.group(9), m.group(11));
    }

}
