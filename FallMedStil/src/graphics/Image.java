/**
 * @author Adam Emil Skoog
 * @date   2011-03-28
 */

// Ändringar:
// 2011-03-30
// 2011-04-28

package graphics;

import collision.Box;
import java.awt.Graphics2D;

/**
 * Innehåller ett bildhandtag för utritning.
 */
public class Image
 {
    /**
     * Skapare.
     * @param image Bilden att sätta.
     */
    Image(final java.awt.Image image)
     {
        this.image = image;
        boundingBox.set(.0f,.0f,image.getHeight(null),image.getWidth(null));
     }

    /**
     * Ritar ut bilden.
     * @param g Utritningshandtaget för rutan.
     * @param x Vågrät stad.
     * @param y Lodrät stad.
     */
    public void draw(Graphics2D g,final float x,final float y)
     {
        // Se efter om någon bild att rita ut är till.
        if (image != null)
            g.drawImage(image,(int)x,(int)y,null);
     }

    /**
     * Giver omgivande ruta.
     * @return Omgivande ruta.
     */
    public Box getBoundingBox()
     {
        return boundingBox;
     }

    // Bilden.
    private java.awt.Image image;

    // Omgivande ruta.
    Box boundingBox = new Box();
 }
