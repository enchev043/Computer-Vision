package see1u17;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.openimaj.feature.DoubleFVComparison;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.pixel.statistics.HistogramModel;
import org.openimaj.math.statistics.distribution.MultidimensionalHistogram;

public class Chapter4 {
	
	public static void main(String[] args) throws Exception{
		histrogram();
	}
	
	private static void histrogram() throws IOException {
		
		MultidimensionalHistogram histogram = new MultidimensionalHistogram( 4, 4, 4 );
	
		URL[] imageURLs = new URL[] {
				new URL( "http://openimaj.org/tutorial/figs/hist1.jpg" ),
				new URL( "http://openimaj.org/tutorial/figs/hist2.jpg" ), 
				new URL( "http://openimaj.org/tutorial/figs/hist3.jpg" ) 
				};
		
		MBFImage[] similar_images = new MBFImage[2];
		
	
		List<MultidimensionalHistogram> histograms = new ArrayList<MultidimensionalHistogram>();
		HistogramModel model = new HistogramModel(4,4,4);
	
		for( URL u : imageURLs ) {
		    model.estimateModel(ImageUtilities.readMBF(u));
		    histograms.add( model.histogram.clone() );
		}
		
		double distance = Double.MAX_VALUE;
		
    	
		for( int i = 0; i < histograms.size(); i++ ) {
		    for( int j = i+1; j < histograms.size(); j++ ) {
		    		    	
		    	double temp_distance = histograms.get(i).compare( histograms.get(j), DoubleFVComparison.EUCLIDEAN );
		    	
		    	if (temp_distance < distance) {
		    		distance = temp_distance;
		    		similar_images[0] = ImageUtilities.readMBF(imageURLs[i]);
		    		similar_images[1] = ImageUtilities.readMBF(imageURLs[j]);    	
		    		System.out.println("Euclidean value is: " + temp_distance);
		    
		    	}
		    
		    }
		}
		
		for(MBFImage i : similar_images){
			DisplayUtilities.display((i), "EUCLIDEAN");
		}
		
		for( int i = 0; i < histograms.size(); i++ ) {
		    for( int j = i+1; j < histograms.size(); j++ ) {
		    			    	
		    	double temp_distance = histograms.get(i).compare( histograms.get(j), DoubleFVComparison.INTERSECTION );
		    	
		    	if (temp_distance > distance) {
		    		distance = temp_distance;
		    		similar_images[0] = ImageUtilities.readMBF(imageURLs[i]);
		    		similar_images[1] = ImageUtilities.readMBF(imageURLs[j]);  
		    		System.out.println("Intersection value is: " + temp_distance);
		    	}
		    
		    }
		}
		
		for(MBFImage i : similar_images){
			DisplayUtilities.display((i), "INTERSECTION");
		}
		
		distance = Double.MAX_VALUE;
		for( int i = 0; i < histograms.size(); i++ ) {
		    for( int j = i+1; j < histograms.size(); j++ ) {
		    		    	
		    	double temp_distance = histograms.get(i).compare( histograms.get(j), DoubleFVComparison.BHATTACHARYYA );
		   
		    	if (temp_distance < distance) {
		    		distance = temp_distance;
		    		similar_images[0] = ImageUtilities.readMBF(imageURLs[i]);
		    		similar_images[1] = ImageUtilities.readMBF(imageURLs[j]);  
		    	 	System.out.println("BHATTACHARYYA distance is: " + temp_distance);
		    	}
		    
		    }
		}
		
		for(MBFImage i : similar_images){
			DisplayUtilities.display((i), "BHATTACHARYYA");
		}
		
		/**
		 * Exercise 1: Finding and displaying similar images
		 * 
		 * Obviously, the first and the second image are the most similar ones. The result did not surprise me.
		 *  
		 * Exercise 2: Exploring comparison measures
		 * 
		 * I get the same result as in the first case because the images have more in common.
		 */
	}
}
