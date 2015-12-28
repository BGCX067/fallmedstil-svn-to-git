/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world;

import collision.Circle;
import collision.CollidableImageEntity;
import collision.Point;
import collision.Polygon;
import collision.Shape;
import fallmedstil.Game;
import fallmedstil.InputReceiver;
import java.awt.Color;

/**
 * Karaktären man styr.
 * @author David Holmquist
 * @date 2011-03-23
 */

//ändringar 2011-03-30
//ändringar 2011-04-13
//ändringar 2011-04-28
// 2011-05-04
public class Character extends CollidableImageEntity implements InputReceiver{
    private int lives = 3;
    private boolean jump = false;
    private boolean flight;
    private long nextHurt;   //för odödlighet
    private float speed = 380.f;//180.f;
    private float jumpspeed = -300.f;
    private boolean canjump = false;
    private float hoverTime = 0.f;
    private float hoverTimeLimit = 2.f;
    private boolean hovering = false;
    private boolean stuck = false;
        @Override
        public void onKeyRelease(final KeyCode code) {

        }
        @Override
        public void onKeyPress(final KeyCode code) {
            if (code == KeyCode.ACTION_2 && !hovering) {
                ((Entity)this).airTime = 0;
                hovering = true;
               
            }

            if (code == KeyCode.ACTION_1 && canjump == true) {
                jump = true;
                canjump = false;
                position().moveY(-2.f);
            }

        }
        @Override
        protected void beforeUpdate() {
            if (jump) {
                position().moveY(jumpspeed * Game.getFrameTime() / 1.8f);
            }
            if (Game.getWindow().getInputManager().keyIsDown(KeyCode.ACTION_2) && hoverTime < hoverTimeLimit) {
                setWeight(0.175f);
                
                jump = false;
                canjump = false;
            }

            else
                setWeight((!jump && ((Entity)this).airTime < .005f) ? speed / 3.f : 1.f);

            if (hovering == true) {
                hoverTime += Game.getFrameTime();
            }

            speed += Game.getFrameTime() * 5.f / (1.f + Game.getPlaytime() / 50.f);

            if (!stuck)
             {
                position().moveX(speed * Game.getFrameTime());

                // Flytta krockskepnaden med spelaren.
                for (Shape i: getMasks())
                    i.position().set(position());

                Game.getCamera().position().moveX(speed * Game.getFrameTime());
             }
        }

        /**
         *  Kör konstruktorn i superklassen som ger oss bilden för karaktären.
         */
        public Character() {
            super(Game.getImageLoader().get("sprites/characterbuzz.png"));
            setReceptiveToGravity(true);
            Game.getWindow().getInputManager().registerInputReceiver(this);

            Polygon legMask = addPolygonMask();
            legMask.addVertex(new Point(60.f,123.f));
            legMask.addVertex(new Point(80.f,123.f));
            legMask.addVertex(new Point(88.f,189.f));
            legMask.addVertex(new Point(51.f,187.f));

            Polygon bodyMask = addPolygonMask();
            bodyMask.addVertex(new Point(38.f,28.f));
            bodyMask.addVertex(new Point(107.f,43.f));
            bodyMask.addVertex(new Point(100.f,108.f));
            bodyMask.addVertex(new Point(92.f,124.f));
            bodyMask.addVertex(new Point(58.f,124.f));
            bodyMask.addVertex(new Point(45.f,103.f));

            Circle headMask = addCircleMask();
            headMask.setRadius(28.f);
            headMask.setOffset(new Point(76.f,30.f));

            bake();
            position().set(100, 100);
        }

        
        /**
         * Säger till karaktären att hoppa
         */
        public void startJump() {
            jump = false; // så att man inte kan hoppa när man redan hoppar
        }
        /**
         *  Säger till karaktären att flyga
         */
        public void startFlight() {
            flight = false;  //så att man inte kan flyga när man redan flyger
        }
        /**
         * Kollar om det är möjligt att glidflyga
         * @return flyg
         */

        public boolean canFlight() {
            return flight;

        }

        /**
         * Sätter mängden liv.
         * @param lives Mängden liv som skall sättas.
         */
        public void setLives(int lives) {
            this.lives = lives;

        }
        /**
         * 
         * Gör det möjligt att ta reda på hur många liv man har.
         * @return mängden liv man har
         */
        public int getLives() {
            return lives;
        }

        /**
         * Giver hastigheten.
         * @return Hastigheten.
         */
        public float getSpeed()
         {
            return speed;
         }

        /**
         * Säger vad som händer med karaktären om man kolliderar med nåogot
         * @param other Enheten som man kolliderar med
         */
        public void collidedWith(CollidableImageEntity other){
        }
        public void notifyLanded() {
            jump = false;
            canjump = true;
            hoverTime = 0;
            hovering = false;
        }

        /**
         * Säger spelaren att han fastnat vid en viss stad och ej skall fara
         * längre fram än dit.
         * @param x Stad att stanna vid.
         */
        public void notifyStuckAt(final float x)
         {
            stuck = true;
            position().setX(x - 1.f);
            speed = .0f;
         }
}
