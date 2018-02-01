package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.vuforia.HINT;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.ddogleg.fitting.modelset.ModelMatcher;
import org.ejml.data.DMatrixRMaj;
import org.ejml.data.FMatrixRMaj;
import org.ejml.ops.ConvertMatrixData;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import java.util.ArrayList;
import java.util.List;

import boofcv.abst.feature.disparity.StereoDisparity;
import boofcv.alg.distort.ImageDistort;
import boofcv.alg.distort.LensDistortionOps;
import boofcv.alg.filter.derivative.LaplacianEdge;
import boofcv.alg.geo.PerspectiveOps;
import boofcv.alg.geo.RectifyImageOps;
import boofcv.alg.geo.rectify.RectifyCalibrated;
import boofcv.android.VisualizeImageData;
import boofcv.core.image.ConvertImage;
import boofcv.core.image.border.BorderType;
import boofcv.factory.feature.disparity.DisparityAlgorithms;
import boofcv.factory.feature.disparity.FactoryStereoDisparity;
import boofcv.factory.geo.ConfigEssential;
import boofcv.factory.geo.ConfigRansac;
import boofcv.factory.geo.FactoryMultiViewRobust;
import boofcv.struct.calib.CameraPinholeRadial;
import boofcv.struct.distort.Point2Transform2_F64;
import boofcv.struct.geo.AssociatedPair;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.GrayS16;
import boofcv.struct.image.GrayU8;
import georegression.struct.se.Se3_F64;
import lib.fine.vision.BoofOps;
import lib.fine.vision.FinedementalMatrii;

/**
 * Created by drew on 1/28/18.
 */
@TeleOp
public class SingleCamDisperity extends LinearOpMode {

    // Disparity calculation parameters
    private static final int minDisparity = 15;
    private static final int maxDisparity = 255;

    public void runOpMode() throws InterruptedException {

        // Camera parameters
        int previewID = hardwareMap.appContext.getResources().getIdentifier("boofPreview", "id", hardwareMap.appContext.getPackageName());
        ImageView preview = (ImageView) ((Activity) hardwareMap.appContext).findViewById(previewID);

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();//cameraMonitorViewId);

        parameters.vuforiaLicenseKey = "AUgSTBn/////AAAAGSU/cD15/UsujI6xLYV74ziGgnCxnhNN3o+oqCbjOAYeuTL3onL+U3IeZxlEpkmbZUZo3dM9ASoSZmIJSdJD4qql7aQoGkyiMmQrG0VrtDRYXGfD0S2gkiP9zyr+Cq+j0OFfrefZrq+k+29VF6ON1KOoPJdDVfUvfbj96xmLd9E6p3bGoJUQSbgnGu+ZkMK2+0Qu8tFe6v8Wx+0v3amf6kgOAaLbjdGqAygEwk9pEOWFxIjpUcwZj8qNqZvtRJP+7csocK3MYC+stHvVh42xXaXeShzC737bkSj0G4lWCtI3JNFDw6NRKX0dmwLbIVMizvudFRXwF2SahUpwh+h/2T5WWSfWP3lcrDYQRgJ54PWG";

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        VuforiaLocalizer vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 1);
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);
        //tells VuforiaLocalizer to only store one frame at a time
        vuforia.setFrameQueueCapacity(1);

        telemetry.addData(">", "Press Play to start");
        telemetry.update();
        while(!isStarted()) {
            BoofOps.displayCurrentFrame(vuforia, preview);//TODO copy getImage to give bitmap and siplay at beggining
        }
        waitForStart();


        // Input images with lens distortion

        GrayU8 distortedLeft = BoofOps.getFrame(vuforia);
        telemetry.addData("First Frame", "got");
        telemetry.update();

        telemetry.addData("Second Frame", "getting ready");
        telemetry.update();
        ElapsedTime runTime = new ElapsedTime();
        runTime.reset();
        while (runTime.milliseconds() < 5000) {
            BoofOps.displayCurrentFrame(vuforia, preview);
        }


        GrayU8 distortedRight = BoofOps.getFrame(vuforia);
        telemetry.addData("Second Frame", "got");
        telemetry.update();

        // matched features between the two images

        List<AssociatedPair> matchedFeatures = FinedementalMatrii.computeMatches(distortedLeft, distortedRight);

        // compute disparity
        StereoDisparity<GrayU8, GrayF32> disparityAlg =
                FactoryStereoDisparity.regionSubpixelWta(DisparityAlgorithms.RECT,
                        minDisparity, maxDisparity, 5, 5, 20, 1, 0.1, GrayU8.class);


        disparityAlg.process(distortedLeft, distortedRight);
        GrayF32 disparity = disparityAlg.getDisparity();

        // show results
        Bitmap bm = Bitmap.createBitmap(disparity.getWidth(), disparity.getHeight(), Bitmap.Config.ARGB_8888);
        VisualizeImageData.disparity(disparity, minDisparity, maxDisparity, 0, bm, null);
        BoofOps.displayBitmap(bm, preview);

        telemetry.addData("Total found ", matchedFeatures.size());
        telemetry.update();
        while (opModeIsActive()) {
            idle();
        }
    }
}
