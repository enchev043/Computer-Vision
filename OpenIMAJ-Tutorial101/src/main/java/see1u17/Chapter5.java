package see1u17;

import java.net.URL;

import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.feature.local.matcher.BasicMatcher;
import org.openimaj.feature.local.matcher.BasicTwoWayMatcher;
import org.openimaj.feature.local.matcher.FastBasicKeypointMatcher;
import org.openimaj.feature.local.matcher.LocalFeatureMatcher;
import org.openimaj.feature.local.matcher.MatchingUtilities;
import org.openimaj.feature.local.matcher.consistent.ConsistentLocalFeatureMatcher2d;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.feature.local.engine.DoGSIFTEngine;
import org.openimaj.image.feature.local.keypoints.Keypoint;
import org.openimaj.math.geometry.transforms.HomographyModel;
import org.openimaj.math.geometry.transforms.HomographyRefinement;
import org.openimaj.math.geometry.transforms.estimation.RobustAffineTransformEstimator;
import org.openimaj.math.geometry.transforms.estimation.RobustHomographyEstimator;
import org.openimaj.math.model.fit.RANSAC;

public class Chapter5 {
	public static void main (String[] args) throws Exception {
		featureMatching();
	}
	
	private static void featureMatching() throws Exception {
		MBFImage query = ImageUtilities.readMBF(new URL("http://static.openimaj.org/media/tutorial/query.jpg"));
		MBFImage target = ImageUtilities.readMBF(new URL("http://static.openimaj.org/media/tutorial/target.jpg"));
		
		DoGSIFTEngine engine = new DoGSIFTEngine();	
		LocalFeatureList<Keypoint> queryKeypoints = engine.findFeatures(query.flatten());
		LocalFeatureList<Keypoint> targetKeypoints = engine.findFeatures(target.flatten());
		
		//Basic Matcher
		LocalFeatureMatcher<Keypoint> matcher = new BasicMatcher<Keypoint>(80);
		matcher.setModelFeatures(queryKeypoints);
		matcher.findMatches(targetKeypoints);
		
		MBFImage basicMatches = MatchingUtilities.drawMatches(query, target, matcher.getMatches(), RGBColour.RED);
		DisplayUtilities.display(basicMatches, "BASIC matches");
		
		//Affine Transform 
		RobustAffineTransformEstimator modelFitter = new RobustAffineTransformEstimator(5.0, 1500,
				new RANSAC.PercentageInliersStoppingCondition(0.7));
		matcher = new ConsistentLocalFeatureMatcher2d<Keypoint>(new FastBasicKeypointMatcher<Keypoint>(8), modelFitter);		
		
		matcher.setModelFeatures(queryKeypoints);
		matcher.findMatches(targetKeypoints);

		MBFImage consistentMatches = MatchingUtilities.drawMatches(query, target, matcher.getMatches(),RGBColour.RED);
		DisplayUtilities.display(consistentMatches, "RANSAC matches");
		
		//Display Affine Transform model
		//target.drawShape(
		//		  query.getBounds().transform(modelFitter.getModel().getTransform().inverse()), 3,RGBColour.BLUE);
		//		DisplayUtilities.display(target);
		
		
		//Basic Two Way Matcher
		LocalFeatureMatcher<Keypoint> matcher1 = new BasicTwoWayMatcher<Keypoint>();
		matcher1.setModelFeatures(queryKeypoints);
		matcher1.findMatches(targetKeypoints);
		
		MBFImage basicTwoWayMatches = MatchingUtilities.drawMatches(query, target, matcher1.getMatches(), RGBColour.RED);
		DisplayUtilities.display(basicTwoWayMatches, "BASIC two way matches");
		
		
		matcher1 = new ConsistentLocalFeatureMatcher2d<Keypoint>(new BasicTwoWayMatcher<Keypoint>(), modelFitter);
		matcher1.setModelFeatures(queryKeypoints);
		matcher1.findMatches(targetKeypoints);
		
		MBFImage basicTwoWayMatches1 = MatchingUtilities.drawMatches(query, target, matcher1.getMatches(), RGBColour.RED);
		DisplayUtilities.display(basicTwoWayMatches1, "BASIC two way matches affine");
		
		//target.drawShape(query.getBounds().transform(modelFitter.getModel().getTransform().inverse()), 3,RGBColour.BLUE);
		//DisplayUtilities.display(target);
				
		//Homography Model
		RobustHomographyEstimator modelFitter1 = new RobustHomographyEstimator(5.0, 1500, new RANSAC.PercentageInliersStoppingCondition(0.7), 
				HomographyRefinement.SINGLE_IMAGE_TRANSFER);
		LocalFeatureMatcher<Keypoint> matcher2 = new BasicTwoWayMatcher<Keypoint>();
		matcher2 = new ConsistentLocalFeatureMatcher2d<Keypoint>(new BasicTwoWayMatcher<Keypoint>(), modelFitter1);
		matcher2.setModelFeatures(queryKeypoints);
		matcher2.findMatches(targetKeypoints);
		
		MBFImage homographyModel = MatchingUtilities.drawMatches(query, target, matcher2.getMatches(), RGBColour.RED);
		DisplayUtilities.display(homographyModel, "homography model");
		target.drawShape(query.getBounds().transform(modelFitter1.getModel().getTransform().inverse()), 3,RGBColour.GREEN);
		DisplayUtilities.display(target);
		
	}
}
