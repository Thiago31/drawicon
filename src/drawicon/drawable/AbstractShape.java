package drawicon.drawable;

import java.awt.Color;

/**
 * Abstract class with drawable elements common features.
 *
 * @author thiago
 */
public abstract class AbstractShape implements Drawer {

    /**
     * If true, shape is drawn.
     */
    protected boolean toDraw;

    /**
     * If true, shape is filled.
     */
    protected boolean toFill;

    /**
     * Color to draw shape.
     */
    protected Color drawColor;

    /**
     * Color to fill shape.
     */
    protected Color fillColor;
    
    /**
     * Sets color attributes.
     * @param draw if "on", draws this shape.
     * @param fill if "on", fills this shape.
     * @param dColor draw color name.
     * @param fColor fill color name.
     */
    protected final void setColorAttributes(String draw, String fill,
            String dColor, String fColor){
        toDraw = draw.equals("on");
        toFill = fill.equals("on");
        drawColor = dColor == null ? Color.BLACK : ColorDic.getColor(dColor);
        fillColor = fColor == null ? Color.WHITE : ColorDic.getColor(fColor);        
    }

}
