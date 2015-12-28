/**
 * @author David Holmquist
 * @date 2011-03-30
 */

//ändringar 2011-04-13
package graphics;

import fallmedstil.Game;
import world.DrawableEntity;

/**
 *
 * hanterar bilder i fönstret som alltid ska visas, t.ex. mängd liv.
 */
public class HUD extends DrawableEntity {


 
    private Image liv;

    public HUD() {
        liv = Game.getImageLoader().get("sprites/liv.png");

        
    }
    @Override
    public void onDraw() {
        
    for(int i = 0; i < Game.getLevel().getCharacter().getLives(); ++ i)
        liv.draw(Game.getWindow().getGraphics2D(), Game.getWindow().getTrueSize().x - 40 - 34*i, 8);
    }
    
}


