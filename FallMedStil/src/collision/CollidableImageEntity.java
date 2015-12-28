/*
 * @author Adam Emil Skoog
 * @date   2011-04-27
 */

// Ändringar:
// 2011-05-04

package collision;

import graphics.Image;
import java.util.ArrayList;
import world.ImageEntity;

/**
 * Förälder för enheter som kan ritas ut och är mottagliga för krockar.
 */
public abstract class CollidableImageEntity extends ImageEntity
 {
    /**
     * Grundskapare.
     */
    public CollidableImageEntity()
     {
        super();
     }

    /**
     * Skapare med bildfil.
     * @param image Bild att nyttja.
     */
    public CollidableImageEntity(Image image)
     {
        super(image);
     }

    /**
     * Bakar enheten, och ser till dess att en omgivande ruta skapas rätt.
     * Detta skall köras först efter att alla krockskepnader är tillagda.
     * @return Ändrad enhet.
     */
    public CollidableImageEntity bake()
     {
        if (getMasks().size() > 0)
         {
            for (Shape i : getMasks())
                i.bake();

            final Box FIRST_BOX = getMasks().get(0).getBoundingBox();
            boundingBox.set(FIRST_BOX);

            for (Shape i : getMasks())
             {
                if (i.getBoundingBox().top() < boundingBox.top())
                    boundingBox.setTop(i.getBoundingBox().top());

                if (i.getBoundingBox().left() < boundingBox.left())
                    boundingBox.setLeft(i.getBoundingBox().left());

                if (i.getBoundingBox().bottom() > boundingBox.bottom())
                    boundingBox.setBottom(i.getBoundingBox().bottom());

                if (i.getBoundingBox().right() > boundingBox.right())
                    boundingBox.setRight(i.getBoundingBox().right());
             }
         }

        return this;
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
     * Giver omgivande ruta, från stad räknad.
     * @return Omgivande ruta vid stad.
     */
    public Box getRelativeBoundingBox()
     {
        return new Box(getBoundingBox().top()    + position().y(),
                       getBoundingBox().left()   + position().x(),
                       getBoundingBox().bottom() + position().y(),
                       getBoundingBox().right()  + position().x());
     }

    /**
     * Skapar en skepnad för krockupptäckning.
     * @return Skepnaden som skapades.
     */
    public Polygon addPolygonMask()
     {
        Polygon result = new Polygon();
        masks.add(result);
        return result;
     }

    /**
     * Skapar en ringskepnad för krockupptäckning.
     * @return Rignen som skapades.
     */
    public Circle addCircleMask()
     {
        Circle result = new Circle();
        masks.add(result);
        return result;
     }

    /**
     * Giver hela samlingen med krockskepnader.
     * @return Samlingen med skepnaderna.
     */
    public ArrayList<Shape> getMasks()
     {
        return masks;
     }

    /**
     * Återgiver huru vida enheten krockar med en annan mottaglig.
     * @param other Annan enhet.
     * @return Huru vida det är en krock.
     */
    public boolean isIntersectingWith(final CollidableImageEntity other)
     {
        // Jämför först omgivande rutor.
        // Kolla i genom alla krockskepnader.
        if (getRelativeBoundingBox().intersectsWith(other.getRelativeBoundingBox()))
         {
            for (Shape i : getMasks())
             {
                for (Shape j : other.getMasks())
                 {
                    if (i.intersectsWith(j))
                     {
                        // Sänd meddelande om krocken.
                        collidedWith(other);
                        other.collidedWith(this);
                        return true;
                     }
                 }
             }
         }

        return false;
     }

    /**
     * Körs om två enheter krockar för båda enheter. Menad att tillämpas av barn.
     * @param other Annan enhet vid krock.
     */
    public abstract void collidedWith(CollidableImageEntity other);

    // Lista över krockskepnader.
    private ArrayList<Shape> masks       = new ArrayList<Shape>();
    private Box              boundingBox = new Box();
 }
