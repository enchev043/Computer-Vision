package see1u17;

import java.net.URL;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.connectedcomponent.GreyscaleConnectedComponentLabeler;
import org.openimaj.image.pixel.ConnectedComponent;
import org.openimaj.image.processor.PixelProcessor;
import org.openimaj.image.segmentation.FelzenszwalbHuttenlocherSegmenter;
import org.openimaj.image.segmentation.SegmentationUtilities;
import org.openimaj.image.typography.hershey.HersheyFont;
import org.openimaj.ml.clustering.FloatCentroidsResult;
import org.openimaj.ml.clustering.assignment.HardAssigner;
import org.openimaj.ml.clustering.kmeans.FloatKMeans;

import cern.colt.Arrays;

public class Chapter3 {
	
	public static void main(String[] args) throws Exception {
		clustering();
		//pixelProcessing();
	}
	
	public static void clustering() throws Exception {
    	
		/**
    	 * Clustering:
    	 * 1. Load an image
    	 * 2. Convert it to LAB
    	 * 3. Create K-Mean algorithm
    	 * 4. Extract vector data float[][] from image
    	 * 5. Apply K-Mean algorithm to vector data (Store in FloatCentroidResults)
    	 * 6. Build the new image from the cluster using HardAssigner
    	 * 7. Convert back to RGB to visualize
    	 * 
    	 */
    	
    	MBFImage input = ImageUtilities.readMBF(new URL("https://upload.wikimedia.org/wikipedia/en/e/e0/Iron_Man_bleeding_edge.jpg"));
    	DisplayUtilities.display(input, "Iron Man");
    	
    	/**
    	 * We use Euclidean distance to measure the similarity between colours. RGB colour space doesn't work as human eyes
    	 * so we have to convert the image to LAB colour space, which corresponds to the human eye's perception. 
    	 */
    	input = ColourSpace.convert(input, ColourSpace.CIE_Lab); //Convert the image to LAB colour space
    	
    	
    	/**
    	 * The algorithm takes as input an array of floating point vectors (float[][]).
    	 */
    	FloatKMeans cluster = FloatKMeans.createExact(2); // Construct K-Means algorithm with 2 classes/clusters of colours
    	
    	/**
    	 * We extract the pixels from the image into floating point vector [][]
    	 */
    	float[][] imageData = input.getPixelVectorNative(new float[input.getWidth() * input.getHeight()][0]);
    	
    	/**
    	 * Then we use the cluster algorithm to extract the results 
    	 */
    	FloatCentroidsResult result = cluster.cluster(imageData);
    	
    	float[][] centroids = result.centroids;
    	for (float[] fs : centroids) {
    	    System.out.println(Arrays.toString(fs));
    	}
    	
    	/**
    	 * Use all the vector points to construct a new image. This new image
    	 * only uses the colours classes from the K-Mean algorithm. 
    	 */
    	HardAssigner<float[],?,?> assigner = result.defaultHardAssigner();
    	for (int y=0; y<input.getHeight(); y++) {
    	    for (int x=0; x<input.getWidth(); x++) {
    	        float[] pixel = input.getPixelNative(x, y);
    	        int centroid = assigner.assign(pixel);
    	        input.setPixelNative(x, y, centroids[centroid]);
    	    }
    	}
    	
    	input = ColourSpace.convert(input, ColourSpace.RGB);
    	//DisplayUtilities.display(input);
    	
    	/**
    	 * ComponentLabel is used to refer to segments of the image
    	 * 
    	 * Converts a coloured image to a grey image because OpenIMAJ can't operate on
    	 * coloured images. The method flatten() averages the values of RGB to create a
    	 * grey image. 
    	 */
    	GreyscaleConnectedComponentLabeler labeler = new GreyscaleConnectedComponentLabeler();
    	List<ConnectedComponent> components = labeler.findComponents(input.flatten());
    	
    	/**
    	 * This labels each segment that was used to construct the image and is larger than 100 pixels
    	 */
    	int i = 0;
    	for (ConnectedComponent comp : components) {
    	    if (comp.calculateArea() < 200) 
    	        continue;
    	    input.drawText("Point:" + (i++), comp.calculateCentroidPixel(), HersheyFont.TIMES_MEDIUM, 20);
    	}
    	
    	DisplayUtilities.display(input);
    }
	
	public static void pixelProcessing() throws Exception {
    	MBFImage image = ImageUtilities.readMBF(new URL("https://upload.wikimedia.org/wikipedia/en/e/e0/Iron_Man_bleeding_edge.jpg"));
    	DisplayUtilities.display(image, "Iron Man");
    	
    	image = ColourSpace.convert(image, ColourSpace.CIE_Lab); //Convert the image to LAB colour space
    	
    	FloatKMeans cluster = FloatKMeans.createExact(2); // Construct K-Means algorithm with 2 classes/clusters of colours
    	
    	float[][] imageData = image.getPixelVectorNative(new float[image.getWidth() * image.getHeight()][0]);
    	
    	final FloatCentroidsResult result = cluster.cluster(imageData);
    	
    	final float[][] centroids = result.centroids;
    	
    	for (float[] fs : centroids) {
    	    System.out.println(Arrays.toString(fs));
    	}
    	
    	
    	
    	final HardAssigner<float[], ?, ?> assigner = result.defaultHardAssigner();
    	
    	
    	image.processInplace(new PixelProcessor<Float[]>() { 		
    	    public Float[] processPixel(Float[] pixel) {
    	    	
    	    	float[] castedPixel = ArrayUtils.toPrimitive(pixel);
    	    	int centroid = assigner.assign(castedPixel);
    	    	return ArrayUtils.toObject(centroids[centroid]);
    	    }   
    	});
    	
    	image = ColourSpace.convert(image, ColourSpace.RGB);
    	DisplayUtilities.display(image, "PixelProcessor");
    	
    	FelzenszwalbHuttenlocherSegmenter segment = new FelzenszwalbHuttenlocherSegmenter(); 
    	
    	SegmentationUtilities.renderSegments(image, segment.segment(image.flatten()));
    	DisplayUtilities.display(image, "FelzenszwalbHuttenlocherSegmenter");
    	
    }
	
	/**
	 * Exercise 1: The PixelProcessor
	 * 
	 * The advantage is that it makes it much easier to process all the pixels in an image simultaneously.
	 * We don't need to position of each pixel to process it and also, we don't need the size of the image.
	 * 
	 * The disadvantage is that we can't use it to track changes in the pixels as they happen. It is not
	 * as versatile because we have less parameters that we can tweak when processing images. For example,
	 * we can't access specific pixel values and change them based on a condition. 
	 */
}
