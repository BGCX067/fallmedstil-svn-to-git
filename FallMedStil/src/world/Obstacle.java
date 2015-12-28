/**
 * @author David Holmquist
 * @date 2011-03-30
 */


  //Ändringar
  //2011-04-27
// 2011-04-28
// 2011-05-04
package world;

import collision.CollidableImageEntity;
import collision.Point;
import collision.Polygon;
import fallmedstil.Game;

/**
 *
 * Hanterar alla hinder som ska ritas ut.
 */
public class Obstacle extends CollidableImageEntity{
    public Obstacle() {
        super(Game.getImageLoader().get("sprites/obstacleBox.png"));


        Polygon p = addPolygonMask();

        p.addVertex(new Point(0.f,  0.f));
        p.addVertex(new Point(35.f, 0.f));
        p.addVertex(new Point(35.f, 35.f));
        p.addVertex(new Point(0.f, 35.f));
        bake();
    }

    public boolean isTaken()
     {
        return taken;
     }

    @Override
    public void collidedWith(CollidableImageEntity other) {
        taken = true;
    }

    private boolean taken = false;
}
