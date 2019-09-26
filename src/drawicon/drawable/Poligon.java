package drawicon.drawable;

import java.awt.Graphics2D;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Draws a poligon element.
 * @author thiago
 */
public class Poligon extends AbstractShape{
    
    /**
     * Pattern to recognize a poligon element.
     */    
    public static final String PATTERN =
            "Poligon\\[x:\\[((\\d+)( \\d+){2,})\\] y:\\[((\\d+)( \\d+){2,})\\] draw:(on|off) fill:(on|off)( colorDraw:(\\w+))?( colorFill:(\\w+))?\\]";

    /**
     * x-coordinate poligon vertexes.
     */
    protected float[] xp;
    
    /**
     * y-coordinate poligon vertexes.
     */
    protected float[] yp;
    
    /**
     * Constructor. Builds a poligon.
     *
     * @param data string that defines poligon's attributes. This string must
     * match {@code Poligon.PATTERN}.
     */    
    public Poligon(String data){
        Pattern p = Pattern.compile(PATTERN);
        Matcher m = p.matcher(data);
        if(!m.find()){
            throw new IllegalArgumentException("Poligon " + data + " is not a valide Poligon element");
        }

        String[] xs = m.group(1).split(" ");
        xp = new float[xs.length];
        String[] ys = m.group(4).split(" ");
        yp = new float[ys.length];
        if(xp.length != yp.length){
            throw new IllegalArgumentException("x and y vector must have same number of elements");
        }
        
        for(int i = 0; i< xp.length; i++){
            xp[i] = Float.parseFloat(xs[i]) / 100;
            yp[i] = (100.0f - Float.parseFloat(ys[i])) / 100;
        }
        
        setColorAttributes(m.group(7), m.group(8), m.group(10), m.group(12));
    }
    
    //Protected void constructor only defined to allow subclassing this class
    //without need to call constructor with String data argument
    protected Poligon(){
        
    }
    
    @Override
    public void draw(Graphics2D g, int size) {
        size--;
        int[] xPoints = new int[xp.length];
        int[] yPoints = new int[yp.length];
        for(int i = 0; i < xp.length; i++){
            xPoints[i] = (int)(xp[i] * size);
            yPoints[i] = (int)(yp[i] * size);
        }
        
        if(toFill){
            g.setColor(fillColor);
            g.fillPolygon(xPoints, yPoints, xp.length);
        }
        
        if(toDraw){
            g.setColor(drawColor);
            g.drawPolygon(xPoints, yPoints, xp.length);
        }
        
    }
    
}
