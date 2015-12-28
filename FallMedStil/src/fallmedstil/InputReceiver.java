/**
 * @author Adam Emil Skoog
 * @date   2011-04-11
 */

package fallmedstil;

import java.awt.event.KeyEvent;

/**
 * Förälder åt enheter som skall kunna upptäcka knapptryckningar.
 */
public interface InputReceiver
 {
    /**
     * Körs när någon knapp trycks neder.
     * @param code Handtag för knappen.
     */
    public void onKeyPress(final KeyCode code);

    /**
     * Körs när någon knapp släpps.
     * @param code Handtag för knappen.
     */
    public void onKeyRelease(final KeyCode code);

    /**
     * Namn för knapparna som kan kollas upp.
     */
    public enum KeyCode
     {
        // Knappnamn.
        LEFT    (KeyEvent.VK_LEFT),
        RIGHT   (KeyEvent.VK_RIGHT),
        UP      (KeyEvent.VK_UP),
        DOWN    (KeyEvent.VK_DOWN),
        ACTION_1(KeyEvent.VK_Z),
        ACTION_2(KeyEvent.VK_X),
        ESCAPE  (KeyEvent.VK_ESCAPE);

        // Dold skapare.
        private KeyCode(final int code)
         {
            this.code = code;
         }

        /**
         * Giver värdet i namnet som ett heltal.
         * @return Heltalsvärde namnets.
         */
        public int toInt()
         {
            return code;
         }

        // Heltalsvärde.
        private int code;
     }
 }
