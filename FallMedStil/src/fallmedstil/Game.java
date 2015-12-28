/**
 * @author Adam Emil Skoog
 * @date   2011-03-23
 */

// Ändringar:
// 2011-03-30
// 2011-04-11
// 2011-04-27
// 2011-04-28

package fallmedstil;

import graphics.Camera;
import graphics.ImageLoader;
import java.awt.Color;
import java.util.logging.Logger;
import world.Level;
import world.Obstacle;

/**
 * Styr allt. Skapar rutan och håller i gång huvudslingan.
 */
public class Game
 {
    /**
     * Kör spelet och håller i gång allt till dess att spelaren valt att avsluta
     * det. Detta är allt man är tvungen att kalla.
     * @param width   Bredden som rutan skall skapas med.
     * @param height  Höjden som rutan skall skapas med.
     * @param caption Titeln som rutan skall skapas med.
     */
    public static void run(final int width,final int height,final String caption)
     {
        // Skapa rutan.
        window = new Window(width,height,caption);
        window.setClearColour(Color.yellow);

        // Skappa knapptryckningsmottagaren.
        inputReceiver = new InputReceiver(window.getInputManager());

        imageLoader.add("sprites/bg.png");
        imageLoader.add("sprites/characterbuzz.png");
        imageLoader.add("sprites/liv.png");
        imageLoader.add("sprites/obstacleBox.png");
        imageLoader.loadAll();

        level = new Level();

        /*
        Polygon p = new Polygon();
        p.addVertex(new Point(150.f,.0f));
        p.addVertex(new Point(300.f,150.f));
        p.addVertex(new Point(.0f,235.f));
        p.bake();
        Polygon q = new Polygon();
        q.addVertex(new Point(30.f,40.f));
        q.addVertex(new Point(100.f,.0f));
        q.addVertex(new Point(224.f,60.f));
        q.addVertex(new Point(188.f,200.f));
        q.addVertex(new Point(50.f,244.f));
        q.bake();
        Circle c = new Circle(55.f), d = new Circle(44.f);
         */

        // Sätt tiden i början för att få rätt värde.
        startTime = System.currentTimeMillis();

        // Huvudslingan.
        while (true)
         {
            // Inled bildrutan.
            window.beginFrame();

            // Ändra tiden.
            final float TIME = (float)(System.currentTimeMillis() - startTime) / 1000.f;
            frameTime        = TIME - runtime;
            runtime          = TIME;

            if (!isPaused())
                playtime += frameTime;

            level.draw();
/*
            p.position().set(400.f + (float)Math.sin(getPlaytime() / 2.f) * 200.f,
                             80.f + (float)Math.sin(getPlaytime()) * 100.f);
            q.position().set(400.f - (float)Math.sin(getPlaytime() / 4.f) * 220.f,
                             120.f + (float)Math.sin(getPlaytime()) * 60.f);
            c.position().set(400.f + (float)Math.sin(getPlaytime() / 3.77f) * 244.f,
                             300.f + (float)Math.sin(getPlaytime() / 4.f) * 200.f);
            d.position().set(400.f - (float)Math.sin(getPlaytime() / 2.44f) * 260.f,
                             300.f - (float)Math.sin(getPlaytime() / 3.4f) * 200.f);
            o.position().set(400.f + (float)Math.sin(getPlaytime() / 2.f) * 200.f,
                             80.f + (float)Math.sin(getPlaytime()) * 100.f);
            o.getMasks().get(0).position().set(o.position());
            final boolean PTQ = p.intersectsWith(q),
                          CTD = c.intersectsWith(d),
                          PTC = c.intersectsWith(p),
                          PTD = d.intersectsWith(p),
                          QTC = c.intersectsWith(q),
                          QTD = d.intersectsWith(q);
            p.debugDraw((PTQ || PTC || PTD) ? new Color(0,0,255,128) : new Color(255,0,0,128));
            q.debugDraw((PTQ || QTC || QTD) ? new Color(0,0,255,128) : new Color(0,255,0,128));
            c.debugDraw((CTD || PTC || QTC) ? new Color(0,0,255,128) : new Color(255,0,255,128));
            d.debugDraw((CTD || PTD || QTD) ? new Color(0,0,255,128) : new Color(22,22,22,128));
*/
           //bvg o.draw();
            // Avsluta bildrutan.
            window.endFrame();

            // Sov tio millisekunder var bildruta för att hindra vissa datorer
            // från att räkna ut tiden fel och ej kunna hänga med.
            try
             {
                Thread.sleep(10);
             }
            catch (InterruptedException e) {}
         }
     }

    /**
     * Giver rutan för utritning i genom andra enheter.
     * @return Rutan.
     */
    public static Window getWindow()
     {
        return window;
     }

    /**
     * Giver ögat.
     * @return Ögat.
     */
    public static Camera getCamera()
     {
        return camera;
     }

    /**
     * Giver bildladdaren för andra enheter att komma åt inladdade filer.
     * @return Bildladdaren.
     */
    public static ImageLoader getImageLoader()
     {
        return imageLoader;
     }

    /**
     * Giver nuvarande bana.
     * @return Banan som för stunden spelas.
     */
    public static Level getLevel()
     {
        return level;
     }

    /**
     * Giver tiden som är liden sedan dess att spelet öppnades.
     * @return Körtiden i sekunder.
     */
    static float getExecutionTime()
     {
        return runtime;
     }

    /**
     * Återställer speltiden.
     */
    public static void resetPlaytime()
     {
        playtime = .0f;
     }

    /**
     * Giver tiden som är liden sedan dess att speltiden senast återställdes.
     * @return Speltiden i sekunder.
     */
    public static float getPlaytime()
     {
        return playtime;
     }

    /**
     * Giver tiden som är liden sedan förra bildrutan.
     * @return Bildrutetiden i sekunder, eller noll, om spelet är fruset.
     */
    public static float getFrameTime()
     {
        return (isPaused()) ? .0f : getGlobalFrameTime();
     }

    /**
     * Giver tiden som är liden sedan förra bildrutan, oavsett om spelet är
     * fruset eller ej.
     * @return Bildrutetiden i sekunder.
     */
    public static float getGlobalFrameTime()
     {
        return frameTime;
     }

    /**
     * Sätter huru vida spelet skall vara fruset.
     * @param paused Huru vida spelet skall vara fruset.
     */
    public static void setPaused(final boolean paused)
     {
        Game.paused = paused;
     }

    /**
     * Återgiver huru vida spelet är fruset.
     * @return Huru vida spelet är fruset.
     */
    public static boolean isPaused()
     {
        return paused;
     }

    /**
     * Skaparen är dold, då det är otillåtet att skapa något från denna klass;
     * allt som finns här är tillgängligt rätt ur klassen.
     */
    private Game() {}

    /**
     * Sköter knapptryckningar i från spelskötaren.
     */
    private static class InputReceiver implements fallmedstil.InputReceiver
     {
        /**
         * Skapare.
         */
        public InputReceiver(final Window.InputManager manager)
         {
            // Lägg till enheten som mottaglig.
            manager.registerInputReceiver(this);
         }

        public void onKeyPress(final InputReceiver.KeyCode code)
         {
            if (code == InputReceiver.KeyCode.ESCAPE)
             {
                System.out.println("Bless.");
                System.exit(0);
             }
         }

        public void onKeyRelease(final InputReceiver.KeyCode code) {}
     }

    // Rutan.
    static Window window;
    static Camera camera = new Camera();

    // Mottaglighet.
    static InputReceiver inputReceiver;

    // Inladdare.
    static ImageLoader imageLoader = new ImageLoader();

    // Nuvarande bana.
    static Level level;

    // Tid.
    static long    startTime;
    static float   runtime    = .0f,
                   playtime   = .0f,
                   frameTime  = .0f;
    static boolean paused     = false;
 }
