/**
 * @author Adam Emil Skoog
 * @date   2011-04-11
 */

// Ändringar:
// 2011-04-27
// 2011-04-29
// 2011-05-02

package collision;

/**
 * Innehåller värden för lodrätt och vågrätt stad.
 */
public class Point
 {
    /**
     * Grundskapare.
     */
    public Point() {}

    /**
     * Skapare
     * @param x Vågrät stad.
     * @param y Lodrät stad.
     */
    public Point(final float x,final float y)
     {
        this.x = x;
        this.y = y;
     }

    /**
     * Sätter staden.
     * @param position Ny stad.
     * @return Ändrat stadshandtag.
     */
    public Point set(final Point position)
     {
        this.x = position.x;
        this.y = position.y;
        return this;
     }

    /**
     * Sätter staden.
     * @param x Ny, vågrät stad.
     * @param y Ny, lodrät stad.
     * @return Ändrat stadshandtag.
     */
    public Point set(final float x,final float y)
     {
        this.x = x;
        this.y = y;
        return this;
     }

    /**
     * Sätter vågrät stad.
     * @param value Nytt värde.
     * @return Ändrat stadshandtag.
     */
    public Point setX(final float value)
     {
        return set(value,y());
     }

    /**
     * Sätter lodrät stad.
     * @param value Nytt värde.
     * @return Ändrat stadshandtag.
     */
    public Point setY(final float value)
     {
        return set(x(),value);
     }

    /**
     * Flyttar staden med mängden bildpunkter givna för båda ledder.
     * @param x Bildpunkter att flytta vågrätt.
     * @param y Bildpunkter att flytta lodrätt.
     * @return Ändrat stadshandtag.
     */
    public Point move(final float x,final float y)
     {
        return set(x() + x,y() + y);
     }

    /**
     * Flyttar vågräta staden med mängden bildpunkter givna.
     * @param value Bildpunkter att flytta.
     * @return Ändrat stadshandtag.
     */
    public Point moveX(final float value)
     {
        return move(value,.0f);
     }

    /**
     * Flyttar lodräta staden med mängden bildpunkter givna.
     * @param value Bildpunkter att flytta.
     * @return Ändrat stadshandtag.
     */
    public Point moveY(final float value)
     {
        return move(.0f,value);
     }

    /**
     * Giver vågrät stad.
     * @return Vågrät stad.
     */
    public float x()
     {
        return x;
     }

    /**
     * Giver lodrät stad.
     * @return Lodrät stad.
     */
    public float y()
     {
        return y;
     }

    /**
     * Giver staden omvänd. Ändrar icke enheten, utan skapar en ny.
     * @return Omvänd stad.
     */
    public Point negative()
     {
        return new Point(x * -1.f,y * -1.f);
     }

    /**
     * Lägger samman denna stad med en annan.
     * @param other Annan stad.
     * @return Sammanlagd stad som ny.
     */
    public Point plus(final Point other)
     {
        return new Point(x() + other.x(),y() + other.y());
     }

    /**
     * Skapar en ny stad med samma inställningar som denna.
     * @return Nya staden.
     */
    public Point copy()
     {
        return new Point(x(),y());
     }

    // Stad.
    private float x = .0f,
                  y = .0f;
 }
