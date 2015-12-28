/**
 * @author Adam Emil Skoog
 * @date   2011-04-11
 */

// Ändringar:
// 2011-04-27
// 2011-04-28
// 2011-05-04

package collision;

import fallmedstil.Game;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Ring mottaglig för överlappningskollar.
 */
public class Circle extends Shape
 {
    /**
     * Grundskapare.
     */
    public Circle()
     {
        this.radius = 10.f;
     }

    /**
     * Skapare.
     * @param radius Radie att sätta.
     */
    public Circle(final float radius)
     {
        this.radius = radius;
     }

    /**
     * Skapare.
     * @param radius   Radie att sätta.
     * @param position Stad att sätta.
     */
    public Circle(final float radius,final Point position)
     {
        super(position);
        this.radius = radius;
     }

    /**
     * Sätter radien.
     * @param radius Ny radie.
     * @return Ändrad ring.
     */
    public Circle setRadius(final float radius)
     {
        this.radius = radius;
        return this;
     }

    /**
     * Giver radien.
     * @return Radien.
     */
    public float getRadius()
     {
        return radius;
     }

    /**
     * Sätter avståndet från kanten.
     * @param offset Avstånd att sätta.
     * @return Ändrad ring.
     */
    public Circle setOffset(final Point offset)
     {
        this.offset = offset;
        return this;
     }

    /**
     * Giver avstånd från kanten.
     * @return Avstånd från kanten.
     */
    public Point getOffset()
     {
        return offset;
     }

    public boolean intersectsWith(final Shape other)
     {
        // Kolla om detta är en annan ring.
        if (other instanceof Circle)
         {
            // Kasta om.
            Circle o = (Circle)other;

            // Se efter om avståndet i kvadrat är mindre än eller lika med
            // ringarnas sammanlagda radie i kvadrat, då detta innebär att de
            // båda överlappar med var andra.
            if (((position().x() + getOffset().x()) - (o.position().x() + o.getOffset().x())) *
                 ((position().x() + getOffset().x()) - (o.position().x() + o.getOffset().x())) +
                ((position().y() + getOffset().y()) - (o.position().y() + o.getOffset().y())) *
                 ((position().y() + getOffset().y()) - (o.position().y() + o.getOffset().y())) <=
                (getRadius() + o.getRadius()) * (getRadius() + o.getRadius()))
                return true;
         }
        // Kolla annars om detta är en skepnad.
        if (other instanceof Polygon)
         {
            // Kasta om.
            Polygon o = (Polygon)other;
            o.moveVertices(o.position());

            // Gå i genom hörnen hos skepnaden.
            for (int i = 0; i < o.getVertexCount(); ++ i)
             {
                // Hämta ut ändarna av linjen.
                final Point A = o.getVertex(i),
                            B = o.getVertex((i == o.getVertexCount() - 1) ? 0 : i + 1);

                // Se om någon av ändarna är inom ringen.
                if (pointIntersects(A))
                 {
                    o.moveVertices(o.position().negative());
                    o.collisionPoint = A;
                    return true;
                 }

                if (pointIntersects(B))
                 {
                    o.moveVertices(o.position().negative());
                    o.collisionPoint = B;
                    return true;
                 }

                // Kolla i annat fall om linjen går i genom ringen.
                final float ANGLE = (float)Math.atan2(A.x() - B.x(),A.y() - B.y());

                // Drag en motsatt linje att kolla mot.
                final Point P = new Point((position().x() + getOffset().x()) + (float)Math.cos(ANGLE) * getRadius(),
                                          (position().y() + getOffset().y()) - (float)Math.sin(ANGLE) * getRadius()),
                            Q = new Point((position().x() + getOffset().x()) - (float)Math.cos(ANGLE) * getRadius(),
                                          (position().y() + getOffset().y()) + (float)Math.sin(ANGLE) * getRadius());

                // Utför kollen.
                if (Maths.linesIntersect(A,B,P,Q))
                 {
                    o.moveVertices(o.position().negative());
                    o.collisionPoint = Maths.getLastLineIntersection();
                    return true;
                 }
             }

            // Gå i genom trekanterna för att leta efter överlappningar.
            final int TRIANGLE_COUNT = o.getTriangleCount();

            if (TRIANGLE_COUNT > 1)
             {
                o.moveVertices(o.position().negative());

                for (int i = 0; i < TRIANGLE_COUNT; ++ i)
                 {
                    final Polygon TRIANGLE = o.getTriangle(i);

                    if (TRIANGLE.intersectsWith(this))
                     {
                        o.collisionPoint = TRIANGLE.getLastCollisionPoint();
                        return true;
                     }
                 }

                o.moveVertices(o.position());
             }

           o.moveVertices(o.position().negative());
         }

        return false;
     }

    /**
     * Återgiver huru vida en viss stad är inom ringen.
     * @param point Staden att kolla mot.
     * @return Huru vida staden är inom ringen.
     */
    private boolean pointIntersects(final Point point)
     {
        // Se efter om avståndet i kvadrat är mindre än eller lika med ringens
        // radie i kvadrat, då detta innebär att de båda överlappar var andra.
        return (((position().x() + getOffset().x()) - point.x()) *
                ((position().x() + getOffset().x()) - point.x()) +
                ((position().y() + getOffset().y()) - point.y()) *
                ((position().y() + getOffset().y()) - point.y()) <=
                getRadius() * getRadius());
     }

    public Shape bake()
     {
        boundingBox.set(getOffset().y() - getRadius(),
                        getOffset().x() - getRadius(),
                        getOffset().y() + getRadius(),
                        getOffset().x() + getRadius());
        return this;
     }

    @Override
    public void debugDraw(final Color colour)
     {
        Graphics2D g = Game.getWindow().getGraphics2D();
        g.setColor(colour);
        g.fillOval((int)Math.round((position().x() + getOffset().x()) - getRadius() - Game.getCamera().position().x()),
                   (int)Math.round((position().y() + getOffset().y()) - getRadius() - Game.getCamera().position().y()),
                   (int)Math.round(getRadius() * 2),
                   (int)Math.round(getRadius() * 2));
     }

    // Medlemmar.
    private float radius = .0f;
    private Point offset = new Point();
 }
