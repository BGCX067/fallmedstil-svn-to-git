/**
 * @author Adam Emil Skoog
 * @date   2011-04-11
 */

// Ändringar:
// 2011-04-28
// 2011-05-02
// 2011-05-04

package collision;

/**
 * En fyrkant utan stöd för värdslig överlappning.
 */
public class Box
 {
    /**
     * Grunskapare.
     */
    public Box() {}

    /**
     * Skapare.
     * @param top    Övre delen av fyrkanten.
     * @param left   Vänstra delen av fyrkanten.
     * @param bottom Nedre delen av fyrkanten.
     * @param right  Högra delen av fyrkanten.
     */
    public Box(final float top,final float left,final float bottom,final float right)
     {
        this.top    = top;
        this.left   = left;
        this.bottom = bottom;
        this.right  = right;
     }

    /**
     * Sätter storleken.
     * @param top    Övre delen av fyrkanten.
     * @param left   Vänstra delen av fyrkanten.
     * @param bottom Nedre delen av fyrkanten.
     * @param right  Högra delen av fyrkanten.
     * @return Ändrad fyrkant.
     */
    public Box set(final float top,final float left,final float bottom,final float right)
     {
        this.top    = top;
        this.left   = left;
        this.bottom = bottom;
        this.right  = right;

        return this;
     }

    /**
     * Sätter storleken.
     * @param other Annan fyrkant att hämta storleken från.
     * @return Ändrad fyrkant.
     */
    public Box set(final Box other)
     {
        return set(other.top,other.left,other.bottom,other.right);
     }

    /**
     * Sätter övre delen av fyrkanten.
     * @param value Nytt värde att sätta.
     * @return Ändrad fyrkant.
     */
    public Box setTop(final float value)
     {
        top = value;
        return this;
     }

    /**
     * Giver övre delen av fyrkanten.
     * @return Övre delen.
     */
    public float top()
     {
        return top;
     }

    /**
     * Sätter vänstra delen av fyrkanten.
     * @param value Nytt värde att sätta.
     * @return Ändrad fyrkant.
     */
    public Box setLeft(final float value)
     {
        left = value;
        return this;
     }

    /**
     * Giver vänstra delen av fyrkanten.
     * @return Vänstra delen.
     */
    public float left()
     {
        return left;
     }

    /**
     * Sätter nedre delen av fyrkanten.
     * @param value Nytt värde att sätta.
     * @return Ändrad fyrkant.
     */
    public Box setBottom(final float value)
     {
        bottom = value;
        return this;
     }

    /**
     * Giver nedre delen av fyrkanten.
     * @return Nedre delen.
     */
    public float bottom()
     {
        return bottom;
     }

    /**
     * Sätter högra delen av fyrkanten.
     * @param value Nytt värde att sätta.
     * @return Ändrad fyrkant.
     */
    public Box setRight(final float value)
     {
        right = value;
        return this;
     }

    /**
     * Giver högra delen av fyrkanten.
     * @return Högra delen.
     */
    public float right()
     {
        return right;
     }

    /**
     * Skapar en ny fyrkant med samma inställningar.
     * @return Ny fyrkant.
     */
    public Box copy()
     {
        return new Box(top,left,bottom,right);
     }

    /**
     * Återgiver huru vida fyrkanten vidrör en annan.
     * @param other Annan fyrkant att kolla mot.
     * @return Huru vida fyrkanterna i någon mån överlappar var andra.
     */
    public boolean intersectsWith(final Box other)
     {
        return (((left() >= other.left() && left() <= other.right())     ||
                 (right() >= other.left() && right() <= other.right())   ||
                 (left() <= other.left() && right() >= other.left())     ||
                 (other.left() <= left() && other.right() >= left()))    &&
                ((top() >= other.top() && top() <= other.bottom())       ||
                 (bottom() >= other.top() && bottom() <= other.bottom()) ||
                 (top() <= other.top() && bottom() >= other.top())       ||
                 (other.top() <= top() && other.bottom() >= top())));
     }

    @Override
    public String toString(){
        return "T: " + top() + ", L: " + left() + ", B: " + bottom() + ", R: " + right();
    }

    // Storlek.
    private float top    = .0f,
                  left   = .0f,
                  bottom = .0f,
                  right  = .0f;
 }
