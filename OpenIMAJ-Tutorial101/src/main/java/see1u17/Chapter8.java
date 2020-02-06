package see1u17;

//import java.awt.geom.Point2D;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import org.openimaj.image.FImage;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.colour.Transforms;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.FaceDetector;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;
import org.openimaj.image.processing.face.detection.keypoints.FKEFaceDetector;
import org.openimaj.image.processing.face.detection.keypoints.FacialKeypoint.FacialKeypointType;
import org.openimaj.image.processing.face.detection.keypoints.KEDetectedFace;
import org.openimaj.math.geometry.point.Point2d;
import org.openimaj.math.geometry.point.Point2dImpl;
import org.openimaj.math.geometry.shape.Ellipse;
import org.openimaj.math.geometry.shape.Rectangle;
import org.openimaj.math.geometry.shape.Shape;
import org.openimaj.video.Video;
import org.openimaj.video.VideoDisplay;
import org.openimaj.video.VideoDisplayListener;
import org.openimaj.video.capture.VideoCapture;
import org.openimaj.video.capture.VideoCaptureException;

import Jama.Matrix;

public class Chapter8 {
	public static void main(String[] args) throws Exception {
		findingFaces();
	}
	
	private static void findingFaces() throws VideoCaptureException {
		Video<MBFImage> video;
		
		video = new VideoCapture(320, 240);
		VideoDisplay<MBFImage> vd = VideoDisplay.createVideoDisplay( video );
		//Point2d point = new Point2d();
		
		vd.addVideoListener( 
		  new VideoDisplayListener<MBFImage>() {
		    public void beforeUpdate( MBFImage frame ) {
		    	//FaceDetector<DetectedFace,FImage> fd = new HaarCascadeDetector(40);
		    	//List<DetectedFace> faces = fd.detectFaces(Transforms.calculateIntensity(frame));
		    	
		    	FaceDetector<KEDetectedFace,FImage> fd = new FKEFaceDetector();
		    	List<KEDetectedFace> faces = fd.detectFaces( Transforms.calculateIntensity( frame ) );
		    	Point2dImpl point2d = new Point2dImpl();
		    	
		    	for( KEDetectedFace face : faces ) {
		    	    frame.drawShape(face.getBounds(), RGBColour.RED);
		    	    
		    	    for(int i = 0; i < face.getKeypoints().length; i++) {
		    	    	
		    	    	face.getKeypoints()[i].position.translate((float)face.getBounds().minX(), (float)face.getBounds().minY());
		    	    	point2d = face.getKeypoints()[i].position;
		    	    	//System.out.println("value of i : " + i);
		    	    	frame.drawPoint(point2d, RGBColour.BLUE, 10);
		    	    	//System.out.println(	face.getKeypoint(FacialKeypointType.MOUTH_LEFT).position);
		    	    	
		    	    	//frame.drawShapeFilled(new Ellipse(x+20f, y+10f, 20f, 10f, 0f), RGBColour.WHITE);
		    	    	//frame.drawShapeFilled(new Ellipse(x+30f, y+20f, 20f, 10f, 0f), RGBColour.WHITE);
		    	    	//frame.drawShapeFilled(new Ellipse(x+40f, y+30f, 20f, 10f, 0f), RGBColour.WHITE);
		    	    }
		    	    float x = face.getKeypoint(FacialKeypointType.MOUTH_LEFT).position.x;
	    	    	float y = face.getKeypoint(FacialKeypointType.MOUTH_LEFT).position.y;
	    	    	
	    	    	//frame.drawShapeFilled(new Ellipse(x, y, 20f, 5f, 0f), RGBColour.WHITE);
	    	    	//frame.drawShapeFilled(new Ellipse(x-x*0.2f, y-y*0.15f, 10f, 10f, 0f), RGBColour.WHITE);
	    	    	//frame.drawShapeFilled(new Ellipse(x-x*0.37f, y-y*0.35f, 25f, 12f, 0f), RGBColour.WHITE);
	    	    	//frame.drawShapeFilled(new Ellipse(x-x*0.48f, y-y*0.48f, 30f, 15f, 0f), RGBColour.WHITE);
		    	    
		    	}
		    }

		    public void afterUpdate( VideoDisplay<MBFImage> display ) {
		    }
		  });
	}
	
	private class ImagePoints implements Point2d{

		@Override
		public Number getOrdinate(int dimension) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setOrdinate(int dimension, Number value) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public int getDimensions() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void readASCII(Scanner in) throws IOException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String asciiHeader() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void readBinary(DataInput in) throws IOException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public byte[] binaryHeader() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void writeASCII(PrintWriter out) throws IOException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void writeBinary(DataOutput out) throws IOException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public float getX() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void setX(float x) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public float getY() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void setY(float y) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void copyFrom(Point2d p) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Point2d copy() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void translate(float x, float y) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Point2d transform(Matrix m) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Point2d minus(Point2d a) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void translate(Point2d v) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
