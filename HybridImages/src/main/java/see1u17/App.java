package see1u17;

import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.processing.convolution.FConvolution;
import org.openimaj.image.processing.convolution.FGaussianConvolve;
import org.openimaj.image.processing.edges.CannyEdgeDetector;
import org.openimaj.image.processing.resize.ResizeProcessor;
import org.openimaj.image.typography.hershey.HersheyFont;
import org.openimaj.math.geometry.shape.Ellipse;

/**
 * OpenIMAJ Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws Exception {
    	hybrid();
    	//convolution();
    	
    	
    	
    }
    
    public static void convolution() throws Exception{
    	//Create an image
        //MBFImage image = new MBFImage(320,70, ColourSpace.RGB);
        
        //MBFImage image = ImageUtilities.readMBF(new URL("https://news.artnet.com/app/news-upload/2017/07/imageedit_2_6650366168-256x256.jpg"));
        MBFImage image = ImageUtilities.readMBF(new URL("http://comp3204.ecs.soton.ac.uk/cw/cat.jpg"));
        DisplayUtilities.display(image);
        //FImage f_image = image2.flatten();
        
        System.out.println(image.getPixel(10, 10)[0]);
        System.out.println(image.getPixel(10, 10)[1]);
        System.out.println(image.getPixel(10, 10)[2]);
        
        MBFImage clone = image.clone();
        MBFImage clone2 = image.clone();
        
        float[][] kernel = new float[4][5];
        System.out.println("Kernel lenght: " + kernel.length);
        System.out.println("Kernel[1] lenghts: " + kernel[1].length);
        
        float[][] kernel2 = new float[2][2];
        float[][] kernel3 = new float [3][3];
        float[][] kernel4 = new float [3][3];
        float[][] kernel5 = new float[3][3];
    	
        kernel3[0][0] = -1f;
        kernel3[0][1] = 0f;
        kernel3[0][2] = 1f;
        
        kernel3[1][0] = -1f;
        kernel3[1][1] = 0f;
        kernel3[1][2] = 1f;
        
        kernel3[2][0] = -1f;
        kernel3[2][1] = 0f;
        kernel3[2][2] = 1f;
        
        kernel4[0][0] = -1f;
        kernel4[1][0] = 0f;
        kernel4[2][0] = 1f;
        
        kernel4[0][1] = -1f;
        kernel4[1][1] = 0f;
        kernel4[2][1] = 1f;
        
        kernel4[0][2] = -1f;
        kernel4[1][2] = 0f;
        kernel4[2][2] = 1f;
        
        kernel5[0][0] = 1f;
        kernel5[1][0] = 2f;
        kernel5[2][0] = 1f;
        
        kernel5[0][1] = 0f;
        kernel5[1][1] = 0f;
        kernel5[2][1] = 0f;
        
        kernel5[0][2] = -1f;
        kernel5[1][2] = -2f;
        kernel5[2][2] = -1f;
        
    	kernel2[0][0] = 2.0f;
		kernel2[0][1] = -1.0f;
		kernel2[1][0] = -1.0f;
		kernel2[0][1] = 0f;
        
        for(int i = 0; i < kernel.length; i++) {
        	
        	for(int j = 0; j < kernel[1].length; j++) {
        		System.out.println(kernel[i][j]);
        		kernel[i][j] = 1/9f;
        		//System.out.println(1/9f);
        		//System.out.println(1/9);
        	}
        }
        
        for(float i: kernel[0]) {
        	System.out.println(i);
        	for(float j:kernel[1]) {
        		System.out.println(j);
        	}
        }
        
     
        
        FConvolution fconvolution = new FConvolution(kernel4);
        //fconvolution.processImage(f_image);
        MyConvolution convolution = new MyConvolution(kernel5);
        
        //Display the image
        //convolution.processImage(clone.flatten());
        //DisplayUtilities.display(clone.process(convolution), "Not mine");
        
        CannyEdgeDetector edgeDetector = new CannyEdgeDetector();
        
        DisplayUtilities.display(clone.process(convolution), "mine");
        DisplayUtilities.display(clone.process(edgeDetector), "Canny");
        //DisplayUtilities.display(clone2.process(convolution), "Mine");
    }

    public static void hybrid() throws Exception{
    	
    	MBFImage image = ImageUtilities.readMBF(new URL("http://comp3204.ecs.soton.ac.uk/cw/dog.jpg"));
    	MBFImage image2 = ImageUtilities.readMBF(new URL("http://comp3204.ecs.soton.ac.uk/cw/cat.jpg"));
    	
    		
    	
    	//File file1 = new File("images/plane.jpg");
    	//File file2 = new File("/HybridImages/src/main/java/see1u17/motorcycle.bmp");
    	
    	//System.out.println(file1.getPath());
    	
    	MBFImage image3 = ImageUtilities.readMBF(new File("resources/dog.bmp"));
    	MBFImage image4 = ImageUtilities.readMBF(new File("resources/cat.bmp"));
    	
    	MBFImage image5 = ImageUtilities.readMBF(new URL("https://www.partytogo.co.uk/ekmps/shops/partytogo/images/mask-darth-vader-mask-130094-p.png"));
    	MBFImage image6 = ImageUtilities.readMBF(new URL("https://images-na.ssl-images-amazon.com/images/I/71k-h43J7GL._SX425_.jpg"));
    	//DisplayUtilities.display(image2);
    	MyHybridImages hybrid = new MyHybridImages(image3, image4);
    	//JFrame frame = DisplayUtilities.createNamedWindow("Hybrid image", "Hybrid image", true);
    	//frame.setSize(1000, 500);
    	
    	/*
    	 * MBFImage hybridImage = hybrid.getHybrid();
    	MBFImage frame = new MBFImage(hybridImage.getWidth()*5, hybridImage.getHeight());
    	
    	int distance = 0;
    	frame.drawImage(hybridImage, 0, 0);
    	distance = (int)hybridImage.getWidth();
    	
    	ResizeProcessor resizeProcessor = new ResizeProcessor(0.8f);
    	frame.drawImage(hybridImage.process(resizeProcessor), distance, (int)(hybridImage.getHeight() * 0.2f));
    	distance += (int)distance * (0.8f);
    	
    	resizeProcessor = new ResizeProcessor(0.6f);
    	frame.drawImage(hybridImage.process(resizeProcessor), distance, (int)(hybridImage.getHeight() * 0.4f));
    	distance += (int)(distance * 0.35f);
    	
    	resizeProcessor = new ResizeProcessor(0.4f);
    	frame.drawImage(hybridImage.process(resizeProcessor), distance, (int)(hybridImage.getHeight() * 0.6f));
    	distance += (int)(distance * 0.19f);
    	
    	resizeProcessor = new ResizeProcessor(0.2f);
    	frame.drawImage(hybridImage.process(resizeProcessor),distance, (int)(hybridImage.getHeight() * 0.8f));
    	
    	
    	//frame.drawImage(hybridImage, hybridImage.getWidth()*2,  hybridImage.getHeight());
    	DisplayUtilities.display(frame);
    	DisplayUtilities.display(hybridImage);
    	 */
    	
    }
}
