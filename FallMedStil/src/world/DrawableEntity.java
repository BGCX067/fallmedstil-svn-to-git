/**
 * @author Adam Emil Skoog
 * @author David Holmquist
 * @date   2011-03-23
 */

// Ändringar:
// 2011-03-30

package world;

import java.awt.Rectangle;

/**
 * Grundklass för enheter som kan ritas ut.
 */
public abstract class DrawableEntity extends Entity
 {
    /**
     * Ritar ut enheten.
     */
    public void draw()
     {
        update();
        onDraw();
     }

    /**
     * Alla barnklasser laddar över denna för att själva tillämpa egna tillägg
     * vid utritning.
     */
    public abstract void onDraw();

    // Krockruta.
    Rectangle hitbox;
 }
