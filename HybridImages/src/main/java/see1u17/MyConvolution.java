//package see1u17;
package uk.ac.soton.ecs.see1u17.hybridimages;

import org.openimaj.image.FImage;
import org.openimaj.image.processor.SinglebandImageProcessor;

public class MyConvolution implements SinglebandImageProcessor<Float, FImage> {
	private float[][]  kernel;

	public MyConvolution(float[][] kernel) {
		this.kernel = kernel;
	}

	@Override
	public void processImage(FImage image) {
		
		int image_height = image.getHeight();
    	int image_width = image.getWidth();
    	
    	
    	FImage kernel_converted = new FImage(kernel);
    	FImage clone = image.newInstance(image_width, image_height);
    		
		int kernel_height =  kernel_converted.height;
    	int kernel_width =  kernel_converted.width;
    	int half_height = Math.floorDiv(kernel_height , 2);
    	int half_width = Math.floorDiv(kernel_width, 2);
    	
		
		for(int y = half_height; y < image_height - (kernel_height - half_height); y++) {
			for(int x = half_width; x < image_width - (kernel_width - half_width); x ++) {
				 
				float new_pixel_sum = 0f;
				
				for(int kernel_y = 0, kernel_yy = kernel_height - 1;kernel_y < kernel_height; kernel_y++, kernel_yy--) {
					
					
					for(int kernel_x = 0,kernel_xx = kernel_width - 1; kernel_x < kernel_width; kernel_x++,kernel_xx--) {
						
						int new_x = x + kernel_x - half_width;
						int new_y = y + kernel_y - half_height;
						
						new_pixel_sum += image.pixels[new_y][new_x] * kernel_converted.pixels[kernel_yy][kernel_xx];
						
					}
				}
				clone.pixels[y][x] = new_pixel_sum;
			}
		}
		
		
		image.internalAssign(clone);
	}
}