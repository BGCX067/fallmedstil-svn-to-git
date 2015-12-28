/**
 * @author Adam Emil Skoog
 * @date   2011-03-23
 */

// Ändringar:
// 2011-03-28
// 2011-03-30
// 2011-04-11

package fallmedstil;

import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

/**
 * Rutan som allt annat ritas ut i.
 */
public class Window extends JFrame
 {
    /*
     * Skapare.
     * @param width Bredden som rutan skall få.
     * @param height Höjden som rutan skall få.
     * @param caption Titeln som rutan skall få.
     */
    Window(final int width,final int height,final String caption)
     {
        // Nyttja skaparen i föräldern.
        super(caption);

        size = new Point(width,height);

        // Skapa innehållet i rutan.
        panel = (JPanel)getContentPane();
        panel.setPreferredSize(new Dimension(width,height));
        panel.setLayout(null);

        canvas.setPreferredSize(new Dimension(width,height));
        canvas.setBounds(new Rectangle(0,0,width,height));
        panel.add(canvas);

        // Gör klar för visning.
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);

        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();

        // Se till dess att göra rutan mottaglig för knapptryckningar.
        canvas.addKeyListener(inputManager);
        canvas.requestFocus();
     }

    /**
     * Giver rätt storlek.
     * @return Rätt storlek.
     */
    public Point getTrueSize()
     {
        return size;
     }

    /**
     * Giver knapptryckningsskötaren.
     * @return Knapptryckningsskötarenheten.
     */
    public InputManager getInputManager()
     {
        return inputManager;
     }

    /**
     * Sätter skiftningen som skärmen skall rensas i.
     * @param colour Skiftningen att sätta.
     * @return Ändrad ruta.
     */
    public Window setClearColour(final Color colour)
     {
        clearColour = colour;
        return this;
     }

    /**
     * Giver skiftningen som skärmen rensas i.
     * @return Rensningsskiftningen.
     */
    public Color getClearColour()
     {
        return clearColour;
     }

    /**
     * Giver handtaget för utritning.
     * @return Utritningshandtaget.
     */
    public Graphics2D getGraphics2D()
     {
        return graphics;
     }

    /**
     * Inleder bildrutan. Skall alltid köras först i huvudslingan.
     */
    public void beginFrame()
     {
        // Sätt rätt värde, så att det alltid går att rita ut något med vad som
        // återgives i genom att tillropa getGraphics2D().
        graphics = (Graphics2D)bufferStrategy.getDrawGraphics();

        // Rensa skärmen med skiftningen som är vald.
        getGraphics2D().setColor(clearColour);
        getGraphics2D().fillRect(0,0,getWidth(),getHeight());
     }

    /**
     * Avslutar bildrutan. Skall alltid köras sist i huvudslingan.
     */
    public void endFrame()
     {
        getGraphics2D().dispose();
        bufferStrategy.show();
     }

    /**
     * Sköter knapptryckningar i rutan.
     */
    public class InputManager extends KeyAdapter
     {
        /**
         * Skapare. Nollställer alla värden.
         */
        InputManager()
         {
            for (int i = 0; i < InputReceiver.KeyCode.values().length; ++ i)
                down[i] = false;
         }

        /**
         * Lägger till en knapptryckningsmottaglig enhet för att möjliggöra
         * denna mottaglighet och se till dess att enheten uppmärksammas om
         * knapphändelser.
         * @param receiver Enheten som läggas till skall.
         * @return Ändrad knapptryckningsskötare.
         */
        public InputManager registerInputReceiver(final InputReceiver receiver)
         {
            receivers.add(receiver);
            return this;
         }

        /**
         * Upptäcker om en knapp trycks neder, och sparar detta.
         * @param e Handtaget för händelsen.
         */
        @Override
        public void keyPressed(KeyEvent e)
         {
            // Gå i genom alla knapphandtag för att sätta rätt värden.
            for (InputReceiver.KeyCode i : InputReceiver.KeyCode.values())
             {
                // Se efter om detta är samma knapp som trycktes neder.
                if (i.toInt() == e.getKeyCode())
                 {
                    if (!down[i.ordinal()])
                     {
                        // Uppmärksamma mottagliga enheter om händelsen.
                        for (InputReceiver j : receivers)
                            j.onKeyPress(i);

                        // Se till dess att vi vet om att knappen nu är nedre.
                        down[i.ordinal()] = true;
                     }
                 }
             }
         }

        /**
         * Upptäcker om en knapp trycks släpps, och sparar detta.
         * @param e Handtaget för händelsen.
         */
        @Override
        public void keyReleased(KeyEvent e)
         {
            // Gå i genom alla knapphandtag för att sätta rätt värden.
            for (InputReceiver.KeyCode i : InputReceiver.KeyCode.values())
             {
                // Se efter om detta är samma knapp som släpptes.
                if (i.toInt() == e.getKeyCode())
                 {
                    // Uppmärksamma mottagliga enheter om händelsen.
                    for (InputReceiver j : receivers)
                        j.onKeyRelease(i);

                    // Se till dess att vi vet om att knappen nu är uppe.
                    down[i.ordinal()] = false;
                 }
             }
         }

        /**
         * Återgiver huru vida en viss knapp för stunden hålls neder.
         * @param code Namn för knappen som kollas skall.
         * @return Huru vida knappen är nedre.
         */
        public boolean keyIsDown(final InputReceiver.KeyCode code)
         {
            return down[code.ordinal()];
         }

        // Medlemmar.
        private boolean                  down[]    = new boolean[InputReceiver.KeyCode.values().length];
        private ArrayList<InputReceiver> receivers = new ArrayList<InputReceiver>();
     }

    // Skapa medlemmar.
    private Point          size;
    private Canvas         canvas         = new Canvas();
    private InputManager   inputManager   = new InputManager();
    private JPanel         panel;
    private BufferStrategy bufferStrategy;
    private Graphics2D     graphics;
    private Color          clearColour    = Color.white;
 }
