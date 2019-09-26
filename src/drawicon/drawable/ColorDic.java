package drawicon.drawable;

import java.awt.Color;
import java.util.HashMap;

/**
 * Dictionary that defines colors.
 * @author thiago
 */
public class ColorDic {

    /**
     * Stores colors and their names.
     */
    private static final HashMap<String, Color> colors = new HashMap<>();

    /**
     * Makes common color entries.
     */
    static {
        colors.put("black", Color.BLACK);
        colors.put("white", Color.WHITE);
        colors.put("blue", Color.BLUE);
        colors.put("red", Color.RED);
        colors.put("green", Color.GREEN);
        colors.put("yellow", Color.YELLOW);
        colors.put("cyan", Color.CYAN);
        colors.put("magenta", Color.MAGENTA);
        colors.put("pink", Color.PINK);
        colors.put("orange", Color.ORANGE);
        colors.put("gray", Color.GRAY);
    }

    private ColorDic() {

    }

    /**
     * Returns a color with specified color name. If colorName is a pre-defined
     * color, return it. Otherwise, if colorName starts with '_' character
     * followed by six characters in hexadecimal format, makes a new color from
     * this string. If not, return BLACK color.
     * @param colorName color name.
     * @return Color with specified color name.
     */
    public static Color getColor(String colorName) {
        if (colors.containsKey(colorName)) {
            return colors.get(colorName);
        } else if(colorName.startsWith("_") && colorName.length() == 7){
            int red = Integer.parseInt(colorName.substring(1,3), 16);
            int green = Integer.parseInt(colorName.substring(3,5), 16);
            int blue = Integer.parseInt(colorName.substring(5), 16);
            Color c = new Color(red, green, blue);
            colors.put(colorName, c);
            return c;
        } else{
            return Color.BLACK;
        }
    }

}
