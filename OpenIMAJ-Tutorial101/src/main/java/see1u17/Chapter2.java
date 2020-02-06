package see1u17;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.processing.edges.CannyEdgeDetector;
import org.openimaj.image.typography.hershey.HersheyFont;
import org.openimaj.math.geometry.shape.Ellipse;

public class Chapter2 {
	
	public static void main  (String[] args) throws Exception {
		firstImage();
	}
	
	public static void firstImage() throws Exception {
    	MBFImage image = ImageUtilities.readMBF(new URL("http://static.openimaj.org/media/tutorial/sinaface.jpg"));
    	System.out.print(image.colourSpace);
    	JFrame mainFrame = DisplayUtilities.createNamedWindow("Red Channel");
    	DisplayUtilities.display(image.getBand(0), mainFrame);
    	TimeUnit.SECONDS.sleep(3);
    	
    	mainFrame.setTitle("Chapter 2");
    	MBFImage clone = image.clone();
    	for (int y=0; y<image.getHeight(); y++) {
    	    for(int x=0; x<image.getWidth(); x++) {
    	        clone.getBand(1).pixels[y][x] = 0;
    	        clone.getBand(2).pixels[y][x] = 0;
    	        
    	    }   	    
    	}
    	DisplayUtilities.display(clone, mainFrame);
    	TimeUnit.SECONDS.sleep(3);
    	
    	image.processInplace(new CannyEdgeDetector());
    	DisplayUtilities.display(image, mainFrame);
    	
    	TimeUnit.SECONDS.sleep(3);
    	
    	image.drawShapeFilled(new Ellipse(700f, 450f, 25f, 15f, 0f), RGBColour.GREEN);
    	image.drawShapeFilled(new Ellipse(700f, 450f, 20f, 10f, 0f), RGBColour.WHITE);
    	image.drawShapeFilled(new Ellipse(650f, 425f, 30f, 17f, 0f), RGBColour.GREEN);
    	image.drawShapeFilled(new Ellipse(650f, 425f, 25f, 12f, 0f), RGBColour.WHITE);
    	image.drawShapeFilled(new Ellipse(600f, 380f, 35f, 20f, 0f), RGBColour.GREEN);
    	image.drawShapeFilled(new Ellipse(600f, 380f, 30f, 15f, 0f), RGBColour.WHITE);
    	image.drawShapeFilled(new Ellipse(500f, 300f, 115f, 85f, 0f), RGBColour.GREEN);
    	image.drawShapeFilled(new Ellipse(500f, 300f, 100f, 70f, 0f), RGBColour.WHITE);
    	
    	image.drawText("OpenIMAJ is", 425, 300, HersheyFont.ASTROLOGY, 20, RGBColour.BLACK);
    	image.drawText("Awesome", 425, 330, HersheyFont.ASTROLOGY, 20, RGBColour.BLACK);
    	
    	DisplayUtilities.display(image, mainFrame);
    	
    }
}
