package see1u17;

import java.net.URL;
import java.util.List;

import javax.swing.JFrame;

import org.apache.commons.lang.ArrayUtils;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.connectedcomponent.GreyscaleConnectedComponentLabeler;
import org.openimaj.image.pixel.ConnectedComponent;
import org.openimaj.image.processing.convolution.FGaussianConvolve;
import org.openimaj.image.processing.edges.CannyEdgeDetector;
import org.openimaj.image.processor.PixelProcessor;
import org.openimaj.image.segmentation.FelzenszwalbHuttenlocherSegmenter;
import org.openimaj.image.segmentation.SegmentationUtilities;
import org.openimaj.image.typography.hershey.HersheyFont;
import org.openimaj.math.geometry.shape.Ellipse;
import org.openimaj.ml.clustering.FloatCentroidsResult;
import org.openimaj.ml.clustering.assignment.HardAssigner;
import org.openimaj.ml.clustering.kmeans.FloatKMeans;

import cern.colt.Arrays;

/**
 * OpenIMAJ Hello world!
 *
 */
public class Chapter1 {
    public static void main( String[] args ) {
    	try {    		
    		HelloWorld();
    	} catch (Exception e){
    		System.out.print(e.getStackTrace());
    	}
    	
    }
    
    public static void HelloWorld() throws Exception {
    	//Create an image
        MBFImage image = new MBFImage(320,70, ColourSpace.RGB);

        //Fill the image with white
        image.fill(RGBColour.BLACK);
        		        
        //Render some test into the image
        image.drawText("Hello Programmer", 10, 60, HersheyFont.GOTHIC_ENGLISH, 50, RGBColour.WHITE);

        //Apply a Gaussian blur
        image.processInplace(new FGaussianConvolve(2f));
        
        //Display the image
        DisplayUtilities.display(image);
    }
    
    

    
    
    
}
