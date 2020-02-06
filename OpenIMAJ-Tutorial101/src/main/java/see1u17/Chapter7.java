package see1u17;

import java.io.File;

import org.openimaj.image.MBFImage;
import org.openimaj.image.processing.edges.CannyEdgeDetector;
import org.openimaj.image.segmentation.FelzenszwalbHuttenlocherSegmenter;
import org.openimaj.image.segmentation.SegmentationUtilities;
import org.openimaj.video.Video;
import org.openimaj.video.VideoDisplay;
import org.openimaj.video.VideoDisplayListener;
import org.openimaj.video.capture.VideoCapture;
import org.openimaj.video.xuggle.XuggleVideo;

/**
 * Chapter 7 of openIMAJ tutorial
 * @author simeon
 *
 */
public class Chapter7 {
	
	
	public static void main(String[] args) throws Exception {
		Video<MBFImage> video;
		
		video = new XuggleVideo(new File("C:\\Users\\simeo\\OpenIMAJ-Tutorial101\\src\\main\\java\\see1u17\\keyboardcat.flv"));
		
		//video = new VideoCapture(320, 240);
		
		VideoDisplay<MBFImage> display = VideoDisplay.createVideoDisplay(video);
		
		display.addVideoListener(
				  new VideoDisplayListener<MBFImage>() {
				    public void beforeUpdate(MBFImage frame) {
				        //frame.processInplace(new CannyEdgeDetector());
				        
				        FelzenszwalbHuttenlocherSegmenter segment = new FelzenszwalbHuttenlocherSegmenter(); 
				    	SegmentationUtilities.renderSegments(frame, segment.segment(frame.flatten()));
				        
				        
				    }

				    public void afterUpdate(VideoDisplay<MBFImage> display) {
				    }
				  });
	}
	
	/**
	 *  Exercise 1: Applying different types of image processing to the video
	 *  
	 *  Applying a segmentation algorithm affects the frames dramatically and causes huge frame drop. This is to be
	 *  expected, as it is a resourceful task.
	 *  
	 *  
	 */
	
}
