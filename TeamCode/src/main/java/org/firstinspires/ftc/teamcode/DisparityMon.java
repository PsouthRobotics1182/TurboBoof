package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import boofcv.struct.image.GrayU8;
import lib.fine.vision.CrackedMonocle;

/**
 * Created by drew on 2/4/18.
 */
@TeleOp
public class DisparityMon extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        CrackedMonocle vision = new CrackedMonocle(this, false);

        vision.activate();

        while (!isStarted()) {
            vision.displayCurrentFrame();
        }

        waitForStart();
        /*
        GrayU8 distortedLeft = vision.getFrameAsBoof();
        telemetry.addData("First Frame", "got");
        telemetry.update();

        telemetry.addData("Second Frame", "getting ready");
        telemetry.update();
        ElapsedTime runTime = new ElapsedTime();
        runTime.reset();
        while (runTime.milliseconds() < 5000) {
            vision.displayCurrentFrame();
        }

        GrayU8 distortedRight = vision.getFrameAsBoof();
        telemetry.addData("Second Frame", "got");
        telemetry.update();


        vision.displayBitmap(vision.stereoProcess(distortedLeft, distortedRight));*/
        Mat distortedLeft = vision.getFramAsMat();
        Imgproc.cvtColor(distortedLeft, distortedLeft, Imgproc.COLOR_BGR2GRAY);
        telemetry.addData("First Frame", "got");
        telemetry.update();

        telemetry.addData("Second Frame", "getting ready");
        telemetry.update();
        ElapsedTime runTime = new ElapsedTime();
        runTime.reset();

        while (runTime.milliseconds() < 5000) {
            vision.displayCurrentFrame();
        }

        Mat distortedRight = vision.getFramAsMat();
        Imgproc.cvtColor(distortedRight, distortedRight, Imgproc.COLOR_BGR2GRAY);

        telemetry.addData("Second Frame", "got");
        telemetry.update();

        Bitmap disMap = vision.stereoProcessAsCV(distortedLeft, distortedRight);

        vision.displayBitmap(disMap);

        while (opModeIsActive()) {
            idle();
        }

    }
}
