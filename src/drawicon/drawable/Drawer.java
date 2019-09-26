package drawicon.drawable;

import java.awt.Graphics2D;

/**
 * A Drawer is an object that draws a shape element into an image.
 * 
 * @author thiago
 */
public interface Drawer {
    
    /**
     * Draws this element to an image.
     * @param g graphic context to draw on.
     * @param size image size, in pixels (width and height are equal).
     */
    public void draw(Graphics2D g, int size);
    
}
