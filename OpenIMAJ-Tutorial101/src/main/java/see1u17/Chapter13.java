package see1u17;

import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.openimaj.data.dataset.GroupedDataset;
import org.openimaj.data.dataset.ListDataset;
import org.openimaj.data.dataset.VFSGroupDataset;
import org.openimaj.experiment.dataset.split.GroupedRandomSplitter;
import org.openimaj.experiment.dataset.util.DatasetAdaptors;
import org.openimaj.feature.DoubleFV;
import org.openimaj.feature.DoubleFVComparison;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.model.EigenImages;

public class Chapter13 {

	public static void main(String[] args) throws Exception {
		faceRecognisition();

	}
	
	public static void faceRecognisition() throws FileSystemException, Exception {
		//Getting the data set
		VFSGroupDataset<FImage> dataset = new VFSGroupDataset<FImage>("zip:http://datasets.openimaj.org/att_faces.zip", ImageUtilities.FIMAGE_READER);
		
		//Splitting into training and testing sets
		int nTraining = 5;
		int nTesting = 5;
		GroupedRandomSplitter<String, FImage> splits =  new GroupedRandomSplitter<String, FImage>(dataset, nTraining, 0, nTesting);
		GroupedDataset<String, ListDataset<FImage>, FImage> training = splits.getTrainingDataset();
		GroupedDataset<String, ListDataset<FImage>, FImage> testing = splits.getTestDataset();
		
		//Getting the Eigenfaces
		List<FImage> basisImages = DatasetAdaptors.asList(training);
		System.out.println(basisImages.size() + " for having " + nTraining + " sets " );
		int nEigenvectors = 100;
		EigenImages eigen = new EigenImages(nEigenvectors);
		eigen.train(basisImages);
		
		
		List<FImage> eigenFaces = new ArrayList<FImage>();
		for (int i = 0; i < 12; i++) {
		    eigenFaces.add(eigen.visualisePC(i));
		}
		DisplayUtilities.display("EigenFaces", eigenFaces);
		
		//Creating a database for the faces
		Map<String, DoubleFV[]> features = new HashMap<String, DoubleFV[]>();
		for (final String person : training.getGroups()) {
		    final DoubleFV[] fvs = new DoubleFV[nTraining];

		    for (int i = 0; i < nTraining; i++) {
		        final FImage face = training.get(person).get(i);
		        fvs[i] = eigen.extractFeature(face);
		       
		    }
		    features.put(person, fvs);
		}
		
		FImage image;
		boolean displayed = false;
		
		for (final String person : features.keySet()) {
            for (final DoubleFV fv : features.get(person)) {    
            	if(!displayed) {
            		image = eigen.reconstruct(fv).normalise();	
                	DisplayUtilities.display(image);
                	displayed = true;
            	}
            	
        	}
        }
		
			
		
		double correct = 0;
		double incorrect = 0;
		double unknown = 0;
		double threshold = 12f;
		
		for (String truePerson : testing.getGroups()) {
		    for (FImage face : testing.get(truePerson)) {
		        DoubleFV testFeature = eigen.extractFeature(face);

		        String bestPerson = null;
		  
		        
		        double minDistance = Double.MAX_VALUE;
		        for (final String person : features.keySet()) {
		            for (final DoubleFV fv : features.get(person)) {
		            	
		                double distance = fv.compare(testFeature, DoubleFVComparison.EUCLIDEAN);
		               
		                if(distance < minDistance) {
		                	minDistance = distance;	
		                	bestPerson = person;
		                	    	
	                    	
		                } 
		               
		              
		            }
		        }
		        
		        if(minDistance > threshold) {
		        	bestPerson = "unknown";
		        	unknown++;
		        }

		        System.out.println("Actual: " + truePerson + "\tguess: " + bestPerson);

		        
		     
		        if (truePerson.equals(bestPerson))
		            correct++;
		        else
		            incorrect++;
		    }
		}

		System.out.println("Accuracy: " + (correct / (correct + incorrect) + " with " + unknown + " unknowns "));
		
	}
	
	/*
	 * Exercise 2: Explore the effect of training set size
	 * 
	 * Reducing the size of the training set lead to a drop in accuracy. From 93-95, it dropped down to 75% for a value of 2 for the training set.
	 * Setting the training set to 1 leads to an average accuracy of 68%
	 * 
	 * Exercise 3: Adding a threshold
	 * 
	 * The threshold doesn't improve the overall accuracy, however, we can identify unknown people, which is not the same as a wrong guess. So, it improves
	 * the overall efficiency, however, it doesn't improve accuracy. Having 20 correct guesses and 20 wrong guesses is could be worse 
	 * than having 20 correct guesses, 18 wrong guesses and 2 unknowns.
	 * I chose 12 as an appropriate threshold, as it doesn't greatly affect the accuracy of the algorithm but also is able to identify unknowns.
	 */
	
	

}
