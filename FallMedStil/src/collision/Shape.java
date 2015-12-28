/**
 * @author Adam Emil Skoog
 * @date   2011-04-11
 */

// Ändringar:
// 2011-05-04

package collision;

import java.awt.Color;

/**
 * Förälder åt enheter som utgör skepnader eller ringar.
 */
public abstract class Shape
 {
    /**
     * Grundskapare.
     */
    public Shape()
     {
        position = new Point();
     }

    /**
     * Skapare.
     * @param x Lodrät stad.
     * @param y Vågrät stad.
     */
    public Shape(final float x,final float y)
     {
        position = new Point(x,y);
     }

    /**
     * Skapare.
     * @param position Stad.
     */
    public Shape(final Point position)
     {
        this.position = position;
     }

    /**
     * Giver staden skepnadens.
     * @return Staden.
     */
    public Point position()
     {
        return position;
     }

    /**
     * Giver omgivande ruta.
     * @return Omgivande ruta.
     */
    public Box getBoundingBox()
     {
        return boundingBox;
     }

    /**
     * Återgiver huru vida skepnaden överlappar eller överlappas av en annan.
     * @param other Skepnaden som överlappning skall kollas mot.
     * @return Huru vida skepnaderna överlappar var andra i någon mån.
     */
    public abstract boolean intersectsWith(final Shape other);

    /**
     * Bakar skepnaden. Detta innebär att en omgivande ruta skapas, och att
     * andra inställningar gör skepnaden redo för nytta. Detta skall göras först
     * när enheten i övrigt är gjort klar.
     * @return Ändrad enhet.
     */
    public abstract Shape bake();

    public void debugDraw(final Color colour) {}

    // Stad.
    private Point position;

    // Omgivande ruta.
    protected Box boundingBox = new Box();
 }
