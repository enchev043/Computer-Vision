//package see1u17;
package uk.ac.soton.ecs.see1u17.hybridimages;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.MBFImage;
import org.openimaj.image.processing.convolution.Gaussian2D;
import org.openimaj.image.processing.resize.ResizeProcessor;

public class MyHybridImages {
	
	private MBFImage lowImage;
	private MBFImage highImage;
	private float lowSigma = 5f;
	private float highSigma = 8f;
	private FImage lowKernel;
	private FImage highKernel;
	
	public MyHybridImages(MBFImage lowImage,MBFImage highImage) {
		this.lowImage = lowImage;
		this.highImage = highImage;
		
		//Different sigma sizes for different sized images
		//Small images require smaller sigma size
		if(lowImage.getWidth() > 300) {
			 lowSigma = 4f;
			 highSigma = 7f;
		} else if(lowImage.getWidth() > 200) {
			lowSigma = 2.5f;
			 highSigma = 5f;
		} else if(lowImage.getWidth() < 200) {
			lowSigma = 2f;
			highSigma = 4f;
		}
		displayHybrid(makeHybrid(this.low_filtering(this.lowImage), lowSigma,this.high_filtering(this.highImage), highSigma ));
		
	}
	
	/**
	 * Uses ResizeProcessor to shrink the image in the frame, so that the hybrid image is easily visible
	 * 5 sizes of the image are displayed, so the size of the frame is 5 times the width of the original image
	 * Each size is 20% less than the initial one
	 * The last image represents the hidden image in the hybrid image
	 * @param hybrid image
	 */
	private static void displayHybrid(MBFImage hybrid) {
		MBFImage frame = new MBFImage(hybrid.getWidth()*5, hybrid.getHeight());
		
		int distance = 0;
    	frame.drawImage(hybrid, 0, 0);
    	distance = (int)hybrid.getWidth();
    	
    	ResizeProcessor resizeProcessor = new ResizeProcessor(0.8f);
    	frame.drawImage(hybrid.process(resizeProcessor), distance, (int)(hybrid.getHeight() * 0.2f));
    	distance += (int)distance * (0.8f);
    	
    	resizeProcessor = new ResizeProcessor(0.6f);
    	frame.drawImage(hybrid.process(resizeProcessor), distance, (int)(hybrid.getHeight() * 0.4f));
    	distance += (int)(distance * 0.35f);
    	
    	resizeProcessor = new ResizeProcessor(0.4f);
    	frame.drawImage(hybrid.process(resizeProcessor), distance, (int)(hybrid.getHeight() * 0.6f));
    	distance += (int)(distance * 0.19f);
    	
    	resizeProcessor = new ResizeProcessor(0.2f);
    	frame.drawImage(hybrid.process(resizeProcessor),distance, (int)(hybrid.getHeight() * 0.8f));
    	
    	
    	DisplayUtilities.display(frame);
	}
	
	/**
	 * Calculates the size of the kernel
	 * @param sigma
	 * @return int representing the size of the kernel
	 */
	private static int getSize(Float sigma) {
		
		int size = (int) (8.0f * sigma + 1.0f);
		if(size % 2 == 0) size++;
		return size;
	}
	
	/**
	 * Does low frequency filtering on an image
	 * @param image
	 * @return new low-filterted image
	 */
	private MBFImage low_filtering(MBFImage image) {
		this.lowKernel = Gaussian2D.createKernelImage(getSize(lowSigma), lowSigma);
			
		MyConvolution convolution = new MyConvolution(lowKernel.pixels);
		
		MBFImage new_image = image.process(convolution);
		
	
		return new_image;
		
	}
	
	/**
	 * Does high filtering on an image by firstly calculating the low filtered version of the image
	 * and then it subtracts the pixel value of the low filtered image from the original image so
	 * that the high filtered version of the image is left
	 * @param image that is going to be high filtered
	 * @return new high filtered image
	 */
	private MBFImage high_filtering(MBFImage image) {
		this.highKernel = Gaussian2D.createKernelImage(getSize(lowSigma), highSigma);
		MyConvolution convolution = new MyConvolution(highKernel.pixels);
		
		MBFImage clone = image.newInstance(image.getWidth(), image.getHeight());
		MBFImage low_filtered_image = image.process(convolution);
		MBFImage new_image = low_filtered_image.newInstance(image.getWidth(), image.getHeight());
		
		for(int y = 0; y < image.getHeight(); y++) {
			for(int x = 0; x < image.getWidth(); x++) {
				if(low_filtered_image.getPixel(x,y)[0] != 0 | low_filtered_image.getPixel(x,y)[1] != 0 | low_filtered_image.getPixel(x,y)[2] != 0) {
					float first_band = image.getPixel(x,y)[0] - low_filtered_image.getPixel(x,y)[0] + 0.5f;
					float second_band = image.getPixel(x,y)[1] - low_filtered_image.getPixel(x,y)[1] + 0.5f;
					float third_band = image.getPixel(x,y)[2] - low_filtered_image.getPixel(x,y)[2] + 0.5f;	
					Float[] new_pixel = new Float[] {first_band, second_band, third_band};
					clone.setPixel(x, y, new_pixel);
				}
				
				
			}
			
		}
		
		new_image.internalAssign(clone);
		
		return new_image;
		
	}
	
	/**
	 * Combines the low filtered and high filtered image into one hybrid image by summing the pixel values
	 * @param lowImage
	 * @param lowSigma
	 * @param highImage
	 * @param highSigma
	 * @return new hybrid image
	 */
	public static MBFImage makeHybrid(MBFImage lowImage, float lowSigma, MBFImage highImage, float highSigma) {
		
		MBFImage new_image = lowImage.newInstance(lowImage.getWidth(), lowImage.getHeight());
		
		for(int y = 0; y < lowImage.getHeight(); y++) {
			for(int x = 0; x < lowImage.getWidth(); x++) {
				float first_band = lowImage.getPixel(x,y)[0] + highImage.getPixel(x,y)[0] - 0.5f;
				float second_band = lowImage.getPixel(x,y)[1] + highImage.getPixel(x,y)[1] - 0.5f;
				float third_band = lowImage.getPixel(x,y)[2] + highImage.getPixel(x,y)[2] - 0.5f;
				
				Float[] new_pixel = new Float[] {first_band, second_band, third_band};
				new_image.setPixel(x, y, new_pixel);
				
			}
			
			
		}
		
		return new_image;
	}
	
}