/**
 * @author Adam Emil Skoog
 * @date   2011-04-11
 */

// Ändringar:
// 2011-04-28

package collision;

/*
import fallmedstil.Game;
import java.awt.Color;
*/

/**
 * Innehåller tillrop med näpna uträkningar.
 */
public class Maths
 {
    /**
     * Giver innerstorleken hos en trekant medsols uppbyggd av ändarna givna.
     * @param a Första änden.
     * @param b Annan änden.
     * @param c Tredje änden.
     * @return Innerstorleken trekantens.
     */
    public static float triangleArea(final Point a,final Point b,final Point c)
     {
        // Nyttja Pythagoras sats för att finna längderna hos sidorna.
        final float AB = (float)Math.sqrt((a.x() - b.x()) * (a.x() - b.x()) + (a.y() - b.y()) * (a.y() - b.y())),
                    BC = (float)Math.sqrt((b.x() - c.x()) * (b.x() - c.x()) + (b.y() - c.y()) * (b.y() - c.y())),
                    CA = (float)Math.sqrt((c.x() - a.x()) * (c.x() - a.x()) + (c.y() - a.y()) * (c.y() - a.y()));

        // Nyttja Herons formel för att finna storleken.
        // Avrundningen är viktig.
        final float S = (AB + BC + CA) / 2.f;
        return Math.round((float)Math.sqrt(S * (S - AB) * (S - BC) * (S - CA)));
     }

    /**
     * Återgiver huru vida två linjer överlappar var andra.
     * @param a1 Första änden av första linjen.
     * @param a2 Annan änden av första linjen.
     * @param b1 Första änden av annan linjen.
     * @param b2 Annan änden av annan linjen.
     * @return Huru vida linjerna i någon mån överlappar var andra.
     */
    public static boolean linesIntersect(final Point a1,final Point a2,
                                         final Point b1,final Point b2)
     {
/*
        Game.getWindow().getGraphics2D().setColor(Color.BLACK);
        Game.getWindow().getGraphics2D().drawLine((int)a1.x(),(int)a1.y(),(int)a2.x(),(int)a2.y());
        Game.getWindow().getGraphics2D().setColor(Color.BLACK);
        Game.getWindow().getGraphics2D().drawLine((int)b1.x(),(int)b1.y(),(int)b2.x(),(int)b2.y());
*/
        // Se efter om linjen mellan två nuvarande egna ändar går i
        // genom linjen mellan två nuvarande ändrar i annan skepnad.
        final float X_1     = a1.x() - a2.x(),                                // Egen linjebredd.
                    X_2     = b1.x() - b2.x(),                                // Annan linjebredd.
                    STEP_1  = (a1.y() - a2.y()) / ((X_1 == .0f) ? 1.f : X_1), // Egen lodrät linjeökning.
                    STEP_2  = (b1.y() - b2.y()) / ((X_2 == .0f) ? 1.f : X_2), // Annan lodrät linjeökning.
                    M_1     = STEP_1 * a2.x() - a2.y(),                       // Egen lodrät utgångshöjd.
                    M_2     = STEP_2 * b2.x() - b2.y(),                       // Annan lodrät utgångshöjd.
                    TMP_X   = (M_1 - M_2) / (STEP_1 - STEP_2),                // Funnen, vågrät stad för uträkning av motsvarande lodrät.
                    Y       = Math.round(TMP_X * STEP_1 - M_1),               // Funnen, lodrät stad.
                    X       = Math.round(TMP_X),                              // Slutgiltigen funnen, vågrät stad.
                    MIN_P_X = Math.round(Math.min(a1.x(),a2.x())),            // Vänsterdelen av rutan runt egen linje.
                    MAX_P_X = Math.round(Math.max(a1.x(),a2.x())),            // Högerdelen av rutan runt egen linje.
                    MIN_P_Y = Math.round(Math.min(a1.y(),a2.y())),            // Överdelen av rutan runt egen linje.
                    MAX_P_Y = Math.round(Math.max(a1.y(),a2.y())),            // Underdelen av rutan runt egen linje.
                    MIN_Q_X = Math.round(Math.min(b1.x(),b2.x())),            // Vänsterdelen av rutan runt annan linje.
                    MAX_Q_X = Math.round(Math.max(b1.x(),b2.x())),            // Högerdelen av rutan runt annan linje.
                    MIN_Q_Y = Math.round(Math.min(b1.y(),b2.y())),            // Överdelen av rutan runt annan linje.
                    MAX_Q_Y = Math.round(Math.max(b1.y(),b2.y()));            // Underdelen av rutan runt annan linje.
//Game.getWindow().getGraphics2D().fillOval((int)X,(int)Y,3,3);
        // Kolla om det är en överlappning mellan linjerna.

        lastLineIntersectionPoint = new Point(X,Y);

        return (X >= MIN_P_X && X <= MAX_P_X && Y >= MIN_P_Y && Y <= MAX_P_Y &&
                X >= MIN_Q_X && X <= MAX_Q_X && Y >= MIN_Q_Y && Y <= MAX_Q_Y);
     }

    /**
     * Giver senast upptäckta linjemötesstaden.
     * @return Senaste mötet.
     */
    public static Point getLastLineIntersection()
     {
        return lastLineIntersectionPoint;
     }

    // Medlemmar.
    private static Point lastLineIntersectionPoint;
 }
