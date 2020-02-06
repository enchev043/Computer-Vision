package see1u17;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openimaj.data.dataset.GroupedDataset;
import org.openimaj.data.dataset.ListDataset;
import org.openimaj.data.dataset.VFSGroupDataset;
import org.openimaj.experiment.dataset.sampling.GroupSampler;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.annotation.evaluation.datasets.Caltech101;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.processing.resize.ResizeProcessor;
import org.openimaj.time.Timer;
import org.openimaj.util.function.Operation;
import org.openimaj.util.parallel.Parallel;
import org.openimaj.util.parallel.partition.RangePartitioner;

public class Chapter14 {
	public static void main(String[] args) throws Exception {
		parallelProcessing();
	}
	
	private static void parallelProcessing() throws IOException {
		/*
		 *Parallel.forIndex(0, 10, 1, new Operation<Integer>() {
			public void perform(Integer i) {
			    System.out.println(i);
			}
		});
 
		 */
				
		VFSGroupDataset<MBFImage> allImages = Caltech101.getImages(ImageUtilities.MBFIMAGE_READER);
		GroupedDataset<String, ListDataset<MBFImage>, MBFImage> images = GroupSampler.sample(allImages, 8, false);
		
		final List<MBFImage> output = new ArrayList<MBFImage>();
		final ResizeProcessor resize = new ResizeProcessor(200);
		
		Timer t1 = Timer.timer();
		//Paralleling the outer loop
		Parallel.forEachPartitioned(new RangePartitioner<ListDataset<MBFImage>>(images.values()), new Operation<Iterator<ListDataset<MBFImage>>>() {
			public void perform(Iterator<ListDataset<MBFImage>> it) {
				MBFImage current = new MBFImage(200, 200, ColourSpace.RGB);
				
				synchronized(current) {
					while(it.hasNext()) {
						ListDataset<MBFImage> clzImages = it.next();
						for (MBFImage i : clzImages) {
					        MBFImage tmp = new MBFImage(200, 200, ColourSpace.RGB);
					        tmp.fill(RGBColour.WHITE);

					        MBFImage small = i.process(resize).normalise();
					        int x = (200 - small.getWidth()) / 2;
					        int y = (200 - small.getHeight()) / 2;
					        tmp.drawImage(small, x, y);

					        current.addInplace(tmp);
					    }
						current.divideInplace((float) clzImages.size());
					    output.add(current);
					}				
				}
				
					
			}
		});
		System.out.println("Time for outer: " + t1.duration() + "ms");
		
		
		
		Timer t2 = Timer.timer();
		//Paralleling the inner loop
		for (ListDataset<MBFImage> clzImages : images.values()) {
		    final MBFImage current = new MBFImage(200, 200, ColourSpace.RGB);

		    Parallel.forEachPartitioned(new RangePartitioner<MBFImage>(clzImages), new Operation<Iterator<MBFImage>>() {
		    	public void perform(Iterator<MBFImage> it) {
		    	    MBFImage tmpAccum = new MBFImage(200, 200, 3);
		    	    MBFImage tmp = new MBFImage(200, 200, ColourSpace.RGB);

		    	    while (it.hasNext()) {
		    	        final MBFImage i = it.next();
		    	        tmp.fill(RGBColour.WHITE);

		    	        final MBFImage small = i.process(resize).normalise();
		    	        final int x = (200 - small.getWidth()) / 2;
		    	        final int y = (200 - small.getHeight()) / 2;
		    	        tmp.drawImage(small, x, y);
		    	        tmpAccum.addInplace(tmp);
		    	    }
		    	    
		    	    synchronized (current) {
		    	        current.addInplace(tmpAccum);
		    	    }
		    	   
		    	}
		    });
		    
		    current.divideInplace((float) clzImages.size());
		    output.add(current);
		}
		
		
		System.out.println("Time for inner: " + t2.duration() + "ms");
		
		
		Timer t3 = Timer.timer();
		//No paralleling
		for (ListDataset<MBFImage> clzImages : images.values()) {
		    MBFImage current = new MBFImage(200, 200, ColourSpace.RGB);

		    for (MBFImage i : clzImages) {
		        MBFImage tmp = new MBFImage(200, 200, ColourSpace.RGB);
		        tmp.fill(RGBColour.WHITE);

		        MBFImage small = i.process(resize).normalise();
		        int x = (200 - small.getWidth()) / 2;
		        int y = (200 - small.getHeight()) / 2;
		        tmp.drawImage(small, x, y);

		        current.addInplace(tmp);
		    }
		    current.divideInplace((float) clzImages.size());
		    output.add(current);
		}
		System.out.println("Time for inner: " + t3.duration() + "ms");
		DisplayUtilities.display("Images", output);
		
		/*
		 * Exercise 1: Parallelise the outer loop
		 * 
		 * The code is twice as slow compared to the paralleling of the inner loop. The reason for this is
		 * that processing the images in the list is the demanding task than processing the list. Even though
		 * multiple lists are read, processing the images takes longer because it is not done parallel.
		 * 
		 * It is more efficient to use multithreading on heavier tasks because the performance boost will be greater.
		 */
		
	}
}
