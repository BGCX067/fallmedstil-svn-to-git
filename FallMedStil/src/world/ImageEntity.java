/**
 * @author Adam Emil Skoog
 * @author David Holmquist
 * @date   2011-03-23
 */

// Ändringar:
// 2011-03-30
// 2011-04-01
// 2011-04-11

package world;

import fallmedstil.Game;
import graphics.Image;

/**
 *
 * En enhet som har en bild den kan rita ut.
 */
public class ImageEntity extends DrawableEntity {
        /**
         *  finns för att den ska gå att skapa utan argument.
         */
        public ImageEntity() {
         
        }
        /**
         * skapar en ImageEntity med en bild.
         * @param bilden som ska sättas.
         */
        public ImageEntity(Image image) {
            setImage(image);
        }
        /**
         * sätter bilden.
         * @param bilden som ska sättas
         */
        public final void setImage(Image image) {
            this.image = image;
        }
        /**
         * gör det möjligt att nå bilden utanför klassen.
         * @return bilden man har.
         */
        public Image getImage() {
            return image;
        }
        /**
         * ritar ut bilden.
         */
        public void onDraw()
         {
            if (image != null)
                image.draw(Game.getWindow().getGraphics2D(),
                           position().x() - ((Game.getCamera() != null) ? Game.getCamera().position().x() : .0f),
                           position().y() - ((Game.getCamera() != null) ? Game.getCamera().position().y() : .0f));
         }

    private Image image;
}
