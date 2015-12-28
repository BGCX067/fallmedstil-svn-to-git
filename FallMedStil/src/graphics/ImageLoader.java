/**
 * @author Adam Emil Skoog
 * @date   2011-03-28
 */

// Ändringar:
// 2011-03-30

package graphics;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * Laddar in bildfiler.
 */
public class ImageLoader
 {
    /**
     * Lägger till sökvägen för en fil som skall laddas in.
     * @param filePath Sökvägen för filen.
     * @return Ändrad bildladdare.
     */
    public ImageLoader add(final String filePath)
     {
        resources.add(new Resource(filePath));
        return this;
     }

    /**
     * Laddar in bilderna från alla sökvägar som är tillagda.
     * @return Ändrad bildladdare.
     */
    public ImageLoader loadAll()
     {
        // Gå i genom alla sökvägar som lagts till, för att ladda in bilderna.
        for (Resource i : resources)
         {
            // Skapa ett bildhandtag att hålla bilden i.
            BufferedImage image = null;

            try
             {
                // Hämta filen.
                URL URLPath = getClass().getClassLoader().getResource(i.getFilePath());

                // Se om det gick vägen.
                if (URLPath != null)
                    image = ImageIO.read(URLPath);
             }
            catch (IOException e) {}

            // Se om bilden kunde laddas.
            if (image != null)
             {
                // Det krävs en hemskt stor hop av kedjade tillrop för att skapa
                // bilden, så jag delar upp allt nedan.
                GraphicsEnvironment   e = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice        d = e.getDefaultScreenDevice();
                GraphicsConfiguration c = d.getDefaultConfiguration();

                // Sedan skapas bilden.
                java.awt.Image finalImage = c.createCompatibleImage(image.getWidth(),
                                                                    image.getHeight(),
                                                                    Transparency.BITMASK);

                // Vi ritar ut bilden i bufferten för bilden som skall sparas.
                finalImage.getGraphics().drawImage(image,0,0,null);

                // Bilden tilldelas åt ett nytt handtag.
                i.setImage(new Image(finalImage));
             }
            else
             {
                System.out.println("Misslyckades med att ladda in bilden \"" + i + "\".");
                System.exit(0);
             }
         }

        return this;
     }

    /**
     * Giver en redan inladdad bild med hjälp av sökvägen som gavs vid tillägg.
     * @param filePath Sökvägen som bilden laddades in från.
     * @return Bilden som motsvarar sökvägen, eller null, om ingen är till.
     */
    public Image get(final String filePath)
     {
        // Leta i genom samlingen efter en motsvarande bild.
        for (Resource i : resources)
            if (i.getImage() != null)
                if (i.getFilePath().matches(filePath))
                    return i.getImage();

        return null;
     }

    /**
     * Omslagsklass för bilder som skall laddas in eller är inladdade.
     */
    private class Resource
     {
        /**
         * Skapare. Lägger till en bild utan att ladda in den.
         * @param filePath Sökvägen för bilden som skall laddas in.
         */
        public Resource(final String filePath)
         {
            this.filePath = filePath;
         }

        /**
         * Giver sökvägen som gavs vid skapandet.
         * @return Sökvägen till bilden.
         */
        public String getFilePath()
         {
            return filePath;
         }

        /**
         * Sätter bildfilen efter inladdning.
         * @param image Bilden som skall sättas.
         */
        public Resource setImage(final Image image)
         {
            this.image = image;
            return this;
         }

        /**
         * Giver bilden som är satt.
         * @return Bilden.
         */
        public Image getImage()
         {
            return image;
         }

        // Sökväg och bild.
        private String filePath;
        private Image  image;
     }

    // Samling att hålla bilder att ladda in, och inladdade bilder.
    private ArrayList<Resource> resources = new ArrayList<Resource>();
 }
