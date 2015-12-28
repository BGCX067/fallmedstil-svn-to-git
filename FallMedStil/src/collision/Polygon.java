/**
 * @author Adam Emil Skoog
 * @date   2011-04-11
 */

// Ändringar:
// 2011-04-28
// 2011-05-04

package collision;

import fallmedstil.Game;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * Skepnad med minst tre ändar medsols.
 */
public class Polygon extends Shape
 {
    /**
     * Grundskapare.
     */
    public Polygon()
     {
        super();
     }

    /**
     * Skapare.
     * @param position Stad att sätta.
     */
    public Polygon(final Point position)
     {
        super(position);
     }

    /**
     * Gör skepnaden klar att nyttjas.
     * @return Ändrad skepnadsenhet.
     */
    public Polygon bake()
     {
        // Lagra mitten i minnet om den är tom.
        if (center == null)
            center = new Point();

        // Nollställ innerstorleken för att räkna ut en ny.
        area = .0f;

        // Hämta bara trekantsmängden en gång för att slippa räkna ut det flera
        // gånger, då detta icke är ett sparat värde.
        final int TRIANGLE_COUNT = getTriangleCount();

        // Gå i genom trekanterna för att räkna ut innerstorleken.
        for (int i = 0; i < TRIANGLE_COUNT; ++ i)
         {
            // Hämta trekanten.
            final Polygon TRIANGLE = getTriangle(i);

            // Öka storleken med den som nu räknas ut.
            area += Maths.triangleArea(TRIANGLE.getVertex(0),
                                       TRIANGLE.getVertex(1),
                                       TRIANGLE.getVertex(2));
         }

        // Räkna ut omgivningsrutan.
        boundingBox.set(getVertex(0).y(),getVertex(0).x(),
                        getVertex(0).y(),getVertex(0).x());

        for (Point i : getVertices())
         {
            // Sätt ändarna i rutan till dess att vara samma som ändarna längst
            // ut åt alla håll i skepnaden.
            if (i.x() < boundingBox.left())
                boundingBox.setLeft(i.x());
            else if(i.x() > boundingBox.right())
                boundingBox.setRight(i.x());

            if (i.y() < boundingBox.top())
                boundingBox.setTop(i.y());
            else if (i.y() > boundingBox.bottom())
                boundingBox.setBottom(i.y());
         }

        // Räkna ut mitten.
        center.set((boundingBox.right() - boundingBox.left()) / 2.f,
                   (boundingBox.bottom() - boundingBox.top()) / 2.f);

        return this;
     }

    /**
     * Lägger till en ände, vars stad räknas från staden skepnadens, och icke
     * från kanten av skärmen; skepnaden skall flyttas med hjälp av
     * åtkomststillropet för staden som sedan kan flyttas med egna åtkomstskall.
     * @param position Stad ändens.
     * @return Ändrad skepnadsenhet.
     */
    public Polygon addVertex(final Point position)
     {
        vertices.add(position);
        return this;
     }

    /**
     * Giver änden vid en viss del av samlingen.
     * @param index Delen att hämta änden från.
     * @return Änden vid denna del.
     */
    public Point getVertex(final int index)
     {
        return vertices.get(index);
     }

    /**
     * Giver alla ändar i en samling.
     * @return Samlingen med ändar.
     */
    public ArrayList<Point> getVertices()
     {
        return vertices;
     }

    /**
     * Giver mängden ändar i skepnaden.
     * @return Mängden ändar.
     */
    public int getVertexCount()
     {
        return vertices.size();
     }

    /**
     * Giver trekanten vid en viss del av samlingen trekanter som skepnaden är
     * uppbyggd av.
     * @param index Delen att hämta trekanten från.
     * @return Trekanten vid delen som gavs.
     */
    public Polygon getTriangle(final int index)
     {
        // Skapa ett handtag att innehålla trekanten.
        Polygon result = new Polygon(position());

        // Tre- och fyrkanter är uppbyggda annorlunda från andra skepnader.
        if (getVertexCount() <= 4)
            for (int i = index * 2; i < index * 2 + 3; ++ i)
                result.addVertex(getVertex((i == getVertexCount()) ? 0 : i));
        else
         {
            result.addVertex(getVertex(index));
            result.addVertex(getVertex((index == getVertexCount() - 1) ? 0 : index + 1));
            result.addVertex(center);
         }

        return result;
     }

    /**
     * Giver mängden trekanter som skepnaden byggs upp av.
     */
    public int getTriangleCount()
     {
        return (getVertexCount() < 5) ? getVertexCount() - 2 : getVertexCount();
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
     * Giver senaste krockänden.
     * @return Senaste änden som krockade mot något.
     */
    public Point getLastCollisionPoint()
     {
        return collisionPoint;
     }

    /**
     * Giver innerstorleken hos skepnaden.
     * @return Innerstorleken.
     */
    public float getArea()
     {
        return area;
     }

    /*
     * Överladdning och egen tillämpning av detta ärvda tillrop.
     */
    public boolean intersectsWith(final Shape other)
     {
        // Kolla om detta är en annan skepnad av samma sort.
        if (other instanceof Polygon)
         {
            // Kasta om.
            Polygon o = (Polygon)other;

            // Flytta fram alla ändar in för kollen.
            moveVertices(position());
            o.moveVertices(o.position());

            // Gå i genom alla egna ändar.
            for (int i = 0; i < getVertexCount(); ++ i)
             {
                // Gå i genom alla ändar i annan skepnad.
                for (int j = 0; j < o.getVertexCount(); ++ j)
                 {
                    // Se efter om linjen mellan två nuvarande egna ändar går i
                    // genom linjen mellan två nuvarande ändrar i annan skepnad.
                    final Point P_1 = getVertex(i),                                           // Första ände i egen linje.
                                P_2 = getVertex((i == getVertexCount() - 1) ? 0 : i + 1),     // Annan ände i egen linje.
                                Q_1 = o.getVertex(j),                                         // Första ände i annan linje.
                                Q_2 = o.getVertex((j == o.getVertexCount() - 1) ? 0 : j + 1); // Annan ände i annan linje.

                    // Kolla om det är en överlappning mellan linjerna.
                    if (Maths.linesIntersect(P_1,P_2,Q_1,Q_2))
                     {
                        moveVertices(position().negative());
                        o.moveVertices(o.position().negative());
                        collisionPoint = Maths.getLastLineIntersection();
                        o.collisionPoint = Maths.getLastLineIntersection();
                        return true;
                     }
                 }
             }

            // Om ingen linjeöverlappning upptäcktes, går vi vidare med att se
            // efter om det är någon av ändarna som överlappar skepnaden.
            // Hämta först trekantsmängderna en gång.
            final int THIS_TRIANGLE_COUNT  = getTriangleCount(),
                      OTHER_TRIANGLE_COUNT = o.getTriangleCount();

            // Gå i genom alla egna trekanter.
            for (int i = 0; i < THIS_TRIANGLE_COUNT; ++ i)
             {
                // Gå i genom alla trekanter i annan skepnad.
                for (int j = 0; j < OTHER_TRIANGLE_COUNT; ++ j)
                 {
                    // Hämta trekanterna.
                    final Polygon TRIANGLE       = getTriangle(i),
                                  OTHER_TRIANGLE = o.getTriangle(j);

                    // Hämta ut ändarna i nuvarande egen trekant.
                    final Point A = TRIANGLE.getVertex(0).plus(position()),
                                B = TRIANGLE.getVertex(1).plus(position()),
                                C = TRIANGLE.getVertex(2).plus(position());

                    // Jämför mot alla ändar i annan trekant.
                    for (int k = 0; k < OTHER_TRIANGLE.getVertexCount(); ++ k)
                     {
                        final Point D = OTHER_TRIANGLE.getVertex(k).plus(o.position());

                        if (Maths.triangleArea(A,B,C) >= Maths.triangleArea(A,B,D) +
                            Maths.triangleArea(B,C,D) + Maths.triangleArea(C,A,D) - 1.f)
                         {
                            moveVertices(position().negative());
                            o.moveVertices(o.position().negative());
                            collisionPoint = D.copy();
                            o.collisionPoint = collisionPoint;
                            return true;
                         }
                     }
                 }
             }

            moveVertices(position().negative());
            o.moveVertices(o.position().negative());
         }
        // Kolla annars om detta är en ring.
        else if (other instanceof Circle)
            return other.intersectsWith(this);

        return false;
     }

    /**
     * Flyttar alla ändar i skepnaden givna steg i båda ledder.
     * @param amount Mängden bildpunkter att flytta i båda ledder.
     */
    public void moveVertices(final Point amount)
     {
        for (Point i : getVertices())
            i.move(amount.x(),amount.y());

        if (center != null)
            center.move(amount.x(),amount.y());
     }

    @Override
    public void debugDraw(final Color colour)
     {
        java.awt.Polygon polygon = new java.awt.Polygon();

        for (Point i : getVertices())
            polygon.addPoint((int)Math.round(i.x() + position().x() - Game.getCamera().position().x()),
                             (int)Math.round(i.y() + position().y() - Game.getCamera().position().y()));

        Graphics2D g = Game.getWindow().getGraphics2D();
        g.setColor(colour);
        g.fillPolygon(polygon);
     }

    // Upplysningar.
    private Point center;
    Point         collisionPoint;
    private float area = .0f;

    // Samling med ändar.
    private ArrayList<Point> vertices = new ArrayList<Point>();
 }
