/**
 * @author Adam Emil Skoog
 * @date   2011-03-16
 */

// Ändringar:
// 2011-03-23
// 2011-03-30
// 2011-04-01
// 2011-04-28

package world;

import collision.Point;

/**
 * Grundklassen för alla ting i spelvärlden.
 */
public class Entity
 {
    /**
     * Skapare.
     */
    public Entity()
     {
        position = new Point();
     }

    /**
     * Skapare.
     * @param x Vågrät stad.
     * @param y Lodrät stad.
     */
    public Entity(final float x,final float y)
     {
        position = new Point(x,y);
     }

    /**
     * Skapare.
     * @param position Stad.
     */
    public Entity(final Point position)
     {
        this.position = position;
     }

    /**
     * Giver staden.
     * @return stad.
     */
    public Point position()
     {
        return position;
     }

    /**
     * Sätter farten hos enheten.
     * @param value Fart att sätta.
     * @return Ändrad enhet.
     */
    public Entity setSpeed(final float value)
     {
        speed = value;
        return this;
     }

    /**
     * Giver hastigheten hos enheten.
     * @return Hastigheten.
     */
    public float getSpeed()
     {
        return speed;
     }

    /**
     * Sätter rättningen hos enheten. Täljs alltid om att vara mellan 0 och 360.
     * @param value Rättning att sätta.
     * @return Ändrad enhet.
     */
    public Entity setDirection(final float value)
     {
        direction = value;
        return this;
     }

    /**
     * Giver rättningen hos enheten.
     * @return Rättningen.
     */
    public float getDirection()
     {
        return direction;
     }

    /**
     * Sätter huru vida enheten skall vara mottaglig för tyngdkraft.
     * @param receptive Huru vida enheten skall märkas som mottaglig.
     * @return Ändrad enhet.
     */
    public Entity setReceptiveToGravity(final boolean receptive)
     {
        receptiveToGravity = receptive;
        return this;
     }

    /**
     * Återgiver huru vuda enheten är mottaglig för tyngdkraft.
     * @return Huru vida enheten är mottaglig.
     */
    public boolean isReceptiveToGravity()
     {
        return receptiveToGravity;
     }

    /**
     * Sätter tyngden.
     * @param weight Tyngd att sätta.
     * @return Ändrad enhet.
     */
    public Entity setWeight(final float weight)
     {
        this.weight = weight;
        return this;
     }

    /**
     * Giver tyngden.
     * @return Tyngden.
     */
    public float getWeight()
     {
        return weight;
     }

    /**
     * Håller enheten i gång.
     */
    public void update()
     {
        // Kör barnets kall innan dess att något händer.
        beforeUpdate();

        // Kör barnets kall efter det att allt är gjort.
        afterUpdate();
     }

    /**
     * Detta körs i början av var bildruta för alla enheter och kan laddas över
     * för att låta något särskilt hända för åtskilda enheter.
     */
    protected void beforeUpdate() {}

    /**
     * Detta körs i änden av var bildruta för alla enheter och kan laddas över
     * för att låta något särskilt hända för åtskilda enheter.
     */
    protected void afterUpdate() {}

    // Stad.
    protected Point position;

    // Rörelse.
    private float speed     = .0f,
                  direction = .0f;

    // Styrvärden.
    private boolean receptiveToGravity = false;
    float           airTime            = .0f,
                    weight             = 1.f;
 }
