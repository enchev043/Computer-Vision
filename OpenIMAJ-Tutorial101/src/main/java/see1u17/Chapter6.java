package see1u17;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Random;

import org.openimaj.data.dataset.VFSGroupDataset;
import org.openimaj.data.dataset.VFSListDataset;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.annotation.evaluation.datasets.cifar.FImageReader;
import org.openimaj.image.annotation.evaluation.datasets.cifar.MBFImageReader;
import org.openimaj.image.dataset.BingImageDataset;
import org.openimaj.image.dataset.FlickrImageDataset;
import org.openimaj.io.InputStreamObjectReader;
import org.openimaj.util.api.auth.DefaultTokenFactory;
import org.openimaj.util.api.auth.common.BingAPIToken;
import org.openimaj.util.api.auth.common.FlickrAPIToken;

public class Chapter6 {
	public static void main(String[] args) throws Exception {
		dataSets();
	}
	
	private static void dataSets() throws Exception{
		VFSListDataset<FImage> images = new VFSListDataset<FImage>("C:\\Users\\simeo\\OpenIMAJ-Tutorial101\\resources", ImageUtilities.FIMAGE_READER);
		DisplayUtilities.display(images.get(0));
		DisplayUtilities.display("My images", images);
		
		VFSListDataset<FImage> faces = new VFSListDataset<FImage>("zip:http://datasets.openimaj.org/att_faces.zip", ImageUtilities.FIMAGE_READER);
		DisplayUtilities.display("ATT faces", faces);
		
		VFSGroupDataset<FImage> groupedFaces = new VFSGroupDataset<FImage>( "zip:http://datasets.openimaj.org/att_faces.zip", ImageUtilities.FIMAGE_READER);
		for (final Entry<String, VFSListDataset<FImage>> entry : groupedFaces.entrySet()) {
			//DisplayUtilities.display(entry.getKey(), entry.getValue());
		}
		
		DefaultTokenFactory.delete(FlickrAPIToken.class);
		FlickrAPIToken flickrToken = DefaultTokenFactory.get(FlickrAPIToken.class);
		FlickrImageDataset<FImage> cats = FlickrImageDataset.create(ImageUtilities.FIMAGE_READER, flickrToken, "cat", 10);
	    DisplayUtilities.display("Cats", cats);
		
		
		//Displaying random set of faces
		FImage[] random_images = new FImage[groupedFaces.keySet().size()];
		int counter = 0;
		for (final Entry<String, VFSListDataset<FImage>> entry : groupedFaces.entrySet()) {
			random_images[counter] = entry.getValue().get(getRandomNumberInRange(0,entry.getValue().size()-1));
			
			counter++;
		}
		DisplayUtilities.display("Random images from the dataset", random_images);
		
		
		//BingAPIToken bingToken = new BingAPIToken("Simeon");
		//BingImageDataset.ImageDataSourceQuery query = new BingImageDataset.ImageDataSourceQuery();
		//query.setQuery("cat");
		//InputStreamObjectReader<FImage> reader = new BingImageReader<FImage>();
		
		//BingImageDataset<FImage> bing_images = BingImageDataset.create(reader,bingToken, new BingImageDataset.ImageDataSourceQuery() , 5);
		//DisplayUtilities.display("Cats", bing_images);
		
	}
	
	/**
	 * Exercise 2: Find out more about VFS datasets
	 *
	 * VFS provides a single API that can access many different file systems.  
	 * There are many different types of VFS, like, BZIP2, File, FTP, GZIP, HTTP, Tar and many others.
	 * 
	 */
	
	//Credit:https://www.mkyong.com/java/java-generate-random-integers-in-a-range/
	private static int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	
}
