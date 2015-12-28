/**
 * @author Adam Emil Skoog
 * @date   2011-03-30
 */

// Ändringar:
// 2011-04-11
// 2011-04-27
// 2011-04-28
// 2011-05-04

package world;

import collision.Box;
import collision.CollidableImageEntity;
import collision.Point;
import collision.Polygon;
import collision.Shape;
import fallmedstil.Game;
import graphics.HUD;
import graphics.Image;
import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * En delvis slumpmässigt skapad bana.
 */
public class Level extends DrawableEntity
 {
    /**
     * Skapare. Slumpar innehållet i banan.
     */
    public Level()
     {
        // Lägg in spelaren.
        character = new Character();

        // Slumpa fram början av banan.
        for (int i = 0; i < 4; ++ i)
            addPieceOfGround(false);

        // Ladda in bakgrundsbilden.
        if (backgroundImage == null)
            backgroundImage = Game.getImageLoader().get("sprites/bg.png");
     }

    /**
     * Håller i gång banan och alla enheter i den.
     */
    @Override
    protected void beforeUpdate()
     {
        // Hämta omgivningsrutan spelarens.
        Box box = ((Polygon)getCharacter().getMasks().get(0)).getBoundingBox().copy();

        // Flytta fram.
        box.setTop   (box.top()    + getCharacter().position().y());
        box.setLeft  (box.left()   + getCharacter().position().x());
        box.setBottom(box.bottom() + getCharacter().position().y());
        box.setRight (box.right()  + getCharacter().position().x());

        // Rita ut bakgrunden.
        backgroundX += getCharacter().getSpeed() * Game.getFrameTime() / -6.f;
        backgroundImage.draw(Game.getWindow().getGraphics2D(),backgroundX,0);
        backgroundImage.draw(Game.getWindow().getGraphics2D(),backgroundX + Game.getWindow().getTrueSize().x,0);

        if (backgroundX < Game.getWindow().getTrueSize().x * -1.f)
            backgroundX = .0f;

        // Gå i genom alla delar av backen.
        for (int i = 0; i < ground.size(); ++ i)
         {
            Section s = ground.get(i);

            s.beforeUpdate();

            // Rita ut enheten.
            if (s.position().x() < Game.getCamera().position().x() + Game.getWindow().getTrueSize().x &&
                s.position().x() > Game.getCamera().position().x() - s.getWidth())
                s.onDraw();

            s.afterUpdate();

            // Plocka bort biten och skapa en ny om så krävs.
            if (s.position().x() < Game.getCamera().position().x() - s.getWidth())
             {
                ground.remove(i --);
                addPieceOfGround(true);
             }
         }

        // Gå i genom alla vanliga enheter, bortsett från spelaren.
        for (int i = 0; i < entities.size(); ++ i)
         {
            DrawableEntity e = (DrawableEntity)entities.get(i);

            e.beforeUpdate();

            if (e instanceof ImageEntity)
             {
                // Avgör om enheten är inom ramarna för utritning och krockkoll.
                if (e.position().x() > Game.getCamera().position().x() -
                    ((ImageEntity)e).getImage().getBoundingBox().right())
                 {
                    // Leta efter krock med spelaren.
                    if (e instanceof CollidableImageEntity)
                     {
                        CollidableImageEntity o = (CollidableImageEntity)e;
                        o.isIntersectingWith(getCharacter());
                     }

                    e.onDraw();
                    e.afterUpdate();
                 }
                else
                 {
                    entities.remove(i --);
                    e = null;
                 }

                // Kolla om detta är något som kan plockas upp.
                if (e != null)
                    if (e instanceof Obstacle)
                        if (((Obstacle)e).isTaken())
                            entities.remove(i --);
            }
         }

        // Räkna ut var spelaren borde landa.
        final float X = ((Polygon)getCharacter().getMasks().get(0)).getBoundingBox().left() + getCharacter().position().x() + 16.f,
                    Y = getYAt(X);

        if (Y < 1.f)
            getCharacter().setWeight(1.f);

        // Rita ut spelaren.
        getCharacter().beforeUpdate();

        // Flytta enheten om den är mottaglig för tyngdkraft.
        if (getCharacter().isReceptiveToGravity())
            getCharacter().position().moveY(getGravity() * (((Entity)getCharacter()).airTime += Game.getFrameTime() * getCharacter().getWeight()) / 3.f);

        // Vi ser efter om spelaren landat eller möjligtvis kört in i en vägg.
        if (getCharacter().position().y() > Y - ((Polygon)getCharacter().getMasks().get(0)).getBoundingBox().bottom() && Y > 1.f)
         {
            // Om man vid förra rutan var över ett hål, är detta en vägg.
            if (lastY < 1.f)
             {
                // Lista ut var väggen sitter.
                for (Section i : ground)
                 {
                    // Säg åt spelaren att stanna framfarten sin, för att kunna
                    // glida neder längs med väggen.
                    if (X >= i.position().x() && X <= i.position().x() + i.getWidth())
                     {
                        // Räkna ut just var spelaren skall stanna.
                        getCharacter().notifyStuckAt((float)Math.floor(i.position().x() - ((getCharacter().position().x() + getCharacter().getImage().getBoundingBox().right()) - X) - 5.f));
                        break;
                     }
                 }
             }
            else
             {
                // Flytta spelaren i lodrätt ledd för att lägga
                // honom i enlighet med backen.
                getCharacter().position().setY(Y - ((Polygon)getCharacter().getMasks().get(0)).getBoundingBox().bottom());

                // Återställ. Kastet krävs för att Character ligger
                // i ett annat paket än Entity, och jag gav värdet
                // airTime pakettillhörighet för att jag vill att
                // Level skall kunna ändra värdet, men inga klasser
                // i andra paket.
                ((Entity)getCharacter()).airTime = .0f;
                getCharacter().notifyLanded();
             }
         }

        // Rita ut spelaren.
        getCharacter().onDraw();
        getCharacter().afterUpdate();

        // Spara senaste lodräta stad för spelaren.
        lastY = Y;
     }

    /**
     * Ritar ut gränssnittet.
     */
    @Override
    protected void afterUpdate()
     {
        HUDLayer.draw();
     }

    /**
     * Gör inget i denna klass.
     */
    @Override
    public void onDraw() {}

    /**
     * Lägger in en ny enhet i banan.
     * @param entity Enheten att lägga in.
     * @return Ändrad bana.
     */
    public Level addEntity(final DrawableEntity entity)
     {
        entities.add(entity);
        return this;
     }

    /**
     * Lägger in en ny, slumpmässigt skapad bit av backe.
     * @param holeAllowed Huru vida det är tillåtet att ett hål slumpas ut.
     */
    private void addPieceOfGround(final boolean holeAllowed)
     {
        if (2 > Math.random() * 10 && direction > .0f && holeAllowed)
         {
            direction *= -1.f;
            lastX     += (300.f + Math.random() * 160.f) * (getCharacter().getSpeed() / 210.f);//280.f + Math.random() * 120.f;
         }

        Section s = new Section();
        s.position().setX(lastX);

        for (Shape j : s.getMasks())
            j.position().setX(lastX);

        lastX += s.getWidth();
        ground.add(s);

        // Kanske lägger vi till något att plocka upp här.
        if (direction < .0f)
         {
            if (4 > Math.random() * 10)
             {
                Obstacle o = new Obstacle();
                o.position().setX(ground.get(ground.size() - 1).position().x() +
                                  ground.get(ground.size() - 1).getWidth() / 2.1f);
                o.position().setY(getYAt(o.position().x()) - o.getImage().getBoundingBox().bottom());
                o.getMasks().get(0).position().set(o.position());

                addEntity(o);
             }
         }
     }

    /**
     * Giver spelarenheten.
     * @return Spelarenheten.
     */
    public Character getCharacter()
     {
        return character;
     }

    /**
     * Sätter tyngdkraften i banan.
     * @param gravity Tyngdkraften att sätta.
     * @return Ändrad bana.
     */
    public Level setGravity(final float gravity)
     {
        this.gravity = gravity;
        return this;
     }

    /**
     * Giver tyngdkraften i banan.
     * @return Tyngdkraften i banan.
     */
    public float getGravity()
     {
        return gravity;
     }

    /**
     * Giver banans höjdvärde vid en viss stad vågrätt in i den.
     * @param x Staden som höjden skall hämtas vid.
     * @return Höjden vid given stad, eller 0 om ingen backe där finns.
     */
    public float getYAt(final float x)
     {
        // Leta upp en del av backen som motsvarar värdet.
        for (Section i : ground)
            if (x >= i.position().x() && x <= i.position().x() + i.getWidth())
                return i.getYAt(x - i.position().x()) + i.position().y() + 4.f;

        return .0f;
     }

    /**
     * En lättviktsklass som innehåller upplysningar om en uppdelning av banan.
     */
    private class Section extends CollidableImageEntity
     {
        /**
         * Skapare. Slumpar ut en storlek åt delen.
         */
        public Section()
         {
            // Slumpa ut en ökning.
            horizontalIncrease = (float)Math.floor(40.f + (float)Math.random() * 30.f);
            //(float)Math.floor(30.f + (float)Math.random() * 20.f)
            verticalIncrease   = (float)Math.floor(80.f + horizontalIncrease);
            startDirection     = direction;

            // Spara bredden.
            width = (int)horizontalIncrease * DIVISIONS;

            // Flytta.
            position().setY(Game.getWindow().getTrueSize().y * .7f);

            // Tilläggshöjd för delarna, för att vara viss om att krockar
            // upptäcks helt rätt.
            final float Y_ADD = Game.getWindow().getTrueSize().y - position.y();

            // Se efter om detta är en dal.
            if (direction > .0f)
             {
                // Lägg in ändarna.
                for (int i = 0; i < DIVISIONS; ++ i)
                 {
                    Polygon mask = addPolygonMask();
                    mask.addVertex(new Point(horizontalIncrease * (float)i,Y_ADD));

                    for (int j = i; i <= j + 1; ++ i)
                     {
                        mask.addVertex((vertices[i] = new Point(horizontalIncrease * (float)i,
                                                                (float)Math.sin(((180.f / (float)DIVISIONS) *
                                                                (float)i) / 57.2957795f) *
                                                                direction * verticalIncrease)));

                        if (vertices[i].y() < height)
                            height = (int)Math.round(vertices[i].y());
                     }

                    mask.addVertex(new Point(horizontalIncrease * (float)(i - 1),Y_ADD));
                    i -= 2;
                 }
             }
            // Detta är annars en kulle.
            else
             {
                // Skapa krockskepnaden.
                Polygon mask = addPolygonMask();

                // Lägg in ändarna.
                mask.addVertex(new Point(.0f,Y_ADD));

                for (int i = 0; i <= DIVISIONS; ++ i)
                 {
                    mask.addVertex((vertices[i] = new Point(horizontalIncrease * (float)i,
                                                            (float)Math.sin(((180.f / (float)DIVISIONS) *
                                                            (float)i) / 57.2957795f) *
                                                            direction * verticalIncrease)));

                    if (vertices[i].y() < height)
                        height = (int)Math.round(vertices[i].y());
                 }

                // Lägg in sista änden.
                mask.addVertex(new Point(width,Y_ADD));
             }

            // Baka lodräta ökningarna mellan ändarna.
            for (int i = 0; i < factors.length; ++ i)
                factors[i] = (vertices[i + 1].y() - vertices[i].y()) /
                             (vertices[i + 1].x() - vertices[i].x());

            // Baka trekanterna och flytta.
            for (Shape i : getMasks())
             {
                // Ringar bakas ej.
                ((Polygon)i).bake();

                // Flytta.
                i.position().setY(position().y());
             }

            // Räkna ut omgivande ruta.
            boundingBox.set(height,.0f,Y_ADD,width);

            // Vänd rättningen.
            direction *= -1.f;
         }

        @Override
        public void collidedWith(CollidableImageEntity other) {}

        /**
         * Ritar ut delen.
         */
        @Override
        public void onDraw()
         {
            for (Shape i : getMasks())
                i.debugDraw(new Color(100,100,100));
         }

        /**
         * Giver bredden.
         * @return Bredden.
         */
        public int getWidth()
         {
            return width;
         }

        /**
         * Giver rutan som omgiver delen.
         * @return Omgivande ruta, från delen räknad.
         */
        public Box getBoundingBox()
         {
            return new Box(boundingBox.top()    + position.y(),
                           boundingBox.left()   + position.x(),
                           boundingBox.bottom() + position.y(),
                           boundingBox.right()  + position.x());
         }

        /**
         * Räknar ut och giver höjden i backen vid en vågrät stad.
         * @param x Vågräta staden att kolla mot, från delen räknad.
         * @return Lodrät stad vid denna stad, från delen räknad.
         */
        public float getYAt(final float x)
         {
            // Lista ut vad för del staden håller sig inom.
            int j = Math.min(Math.max(0,(int)Math.ceil(x / horizontalIncrease) - 1),DIVISIONS);

            // Nyttja räta vinkelns formel för att hämta ut rätt värde.
            return vertices[j].y() + (x - horizontalIncrease * j) * factors[j];
         }

        // Medlemmar.
        private int   width,
                      height = 0;
        private float horizontalIncrease,
                      verticalIncrease,
                      startDirection;
        private Box   boundingBox = new Box();

        // Sparad ökning och sparade ändar.
        float factors[]  = new float[DIVISIONS];
        Point vertices[] = new Point[DIVISIONS + 1];
     }

    // Bakgrund.
    private static Image backgroundImage = null;
    private static float backgroundX     = .0f;

    // Rättning för böjen.
    private static float direction = -1.f;

    // Mängden delar som en del byggs upp av.
    private static final int DIVISIONS = 12;

    // Samling med enheter som banan är uppbyggd av, bortsett från spelaren och
    // delarna av backen, som är särskilda enheter som sköts annorledes.
    private ArrayList<DrawableEntity> entities = new ArrayList<DrawableEntity>();

    // Särskild samling som bara innehåller delarna av backen. Jag nyttjar en
    // länkad lista då dessa är mycket kvickare vad gäller att plocka bort
    // enskilda ting, och jag slipper tappa i hastighet för att spelet görs
    // tvunget att flytta om eller till och med byta ut redan skapade fält. Då
    // tingen i en länkad lista bara är noder med hänvisningar mellan var andra,
    // innebär det en otroligt nyttig ökning i körhastighet av spelet.
    private LinkedList<Section> ground = new LinkedList<Section>();

    // Senaste vågräta värdet för utsättning av backe, som för var utsättning av
    // en ny del ökar med bredden hos denna, för att se till dess att alla nya
    // delar av backen som tillkommer hamnar rätt och långt nog in i banan.
    private float lastX = .0f,
                  lastY = 1.f;

    // Särskild hänvisning mot spelaren.
    private Character character;

    // Regler.
    private float gravity = 9.82f;

    // HUD.
    private HUD HUDLayer = new HUD();
 }
