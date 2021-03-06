package lib.fine.vision;

import org.ddogleg.struct.FastQueue;

import java.util.ArrayList;
import java.util.List;

import boofcv.abst.feature.associate.AssociateDescription;
import boofcv.abst.feature.detdesc.DetectDescribePoint;
import boofcv.alg.descriptor.UtilFeature;
import boofcv.struct.feature.TupleDesc;
import boofcv.struct.image.ImageGray;
import georegression.struct.point.Point2D_F64;

/**
 * After interest points have been detected in two images the next step is to associate the two
 * sets of images so that the relationship can be found.  This is done by computing descriptors for
 * each detected feature and associating them together.  In the code below abstracted interfaces are
 * used to allow different algorithms to be easily used.  The cost of this abstraction is that detector/descriptor
 * specific information is thrown away, potentially slowing down or degrading performance.
 *
 * @author Peter Abeles
 */
public class OkayAssociator<T extends ImageGray<T>, TD extends TupleDesc> {

	// algorithm used to detect and describe interest points
    DetectDescribePoint<T, TD> detDesc;
	// Associated descriptions together by minimizing an error metric
	AssociateDescription<TD> associate;

	// location of interest points
	public List<Point2D_F64> pointsA;
	public List<Point2D_F64> pointsB;

	Class<T> imageType;

	public OkayAssociator(DetectDescribePoint<T, TD> detDesc,
                          AssociateDescription<TD> associate,
                          Class<T> imageType) {
		this.detDesc = detDesc;
		this.associate = associate;
		this.imageType = imageType;
	}

	/**
	 * Detect and associate point features in the two images.  Display the results.
	 */
	public void associate(T imageA , T imageB )
	{
		T inputA = imageA;
		T inputB = imageB;

		// stores the location of detected interest points
		pointsA = new ArrayList<>();
		pointsB = new ArrayList<>();

		// stores the description of detected interest points
		FastQueue<TD> descA = UtilFeature.createQueue(detDesc,100);
		FastQueue<TD> descB = UtilFeature.createQueue(detDesc,100);

		// describe each image using interest points
		describeImage(inputA,pointsA,descA);
		describeImage(inputB,pointsB,descB);

		// Associate features between the two images
		associate.setSource(descA);
		associate.setDestination(descB);
		associate.associate();

	}

	/**
	 * Detects features inside the two images and computes descriptions at those points.
	 */
	private void describeImage(T input, List<Point2D_F64> points, FastQueue<TD> descs )
	{
		detDesc.detect(input);

		for( int i = 0; i < detDesc.getNumberOfFeatures(); i++ ) {
			points.add( detDesc.getLocation(i).copy() );
			descs.grow().setTo(detDesc.getDescription(i));
		}
	}

}