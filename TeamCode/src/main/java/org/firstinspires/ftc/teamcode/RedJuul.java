package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

import lib.fine.core.FineIMU;
import lib.fine.vision.CrackedMonocle;
import lib.fine.systems.FineBot;

/**
 * Created by drew on 11/25/17.
 */

@Autonomous(name = "Strawberry Juul V2.386")
public class RedJuul extends LinearOpMode {

      //////////////////////
     // Global Variables //
    //////////////////////

    CrackedMonocle vuforia;
    RelicRecoveryVuMark column;

      //////////////////////
     // Helper Functions //
    //////////////////////

    /**
     * Update the current column if it is unknown.
     */

    public RelicRecoveryVuMark updateColumn() {
        return (column == RelicRecoveryVuMark.UNKNOWN) ? vuforia.getColumn() : column;
    }

      /////////////////
     // Main OpMode //
    /////////////////

    @Override
    public void runOpMode() throws InterruptedException {
          ////////////////////
         // Initialization //
        ////////////////////

        FineBot robot = new FineBot(this, DcMotor.RunMode.RUN_USING_ENCODER, Color.RED);
        vuforia = new CrackedMonocle(this, true);
        ElapsedTime runTime = new ElapsedTime();
        vuforia.activate();

        telemetry.addData("Ready", null);
        telemetry.update();

        waitForStart();
        runTime.reset();

        column = vuforia.getColumn();
        robot.lift.grabBottom(1);
        robot.lift.grabTop(1);
        robot.sleep(1000);
        robot.lift.lift(1);
        robot.sleep(500);
        robot.lift.lift(0);

        robot.reversePullBois.powerLift(0.5);
        robot.sleep(500);
        robot.reversePullBois.powerLift(0);

        robot.juulHittererer.hitRed();

        robot.sleep(500);
        robot.reversePullBois.powerLift(-0.8);
        robot.sleep(500);
        robot.reversePullBois.powerLift(0);

        robot.drive.resetEncoders();
        //double power = 0;
        robot.drive.strafeLeft(780, 1);
        robot.drive.stop();

        column = vuforia.getColumn();

        robot.drive.imu.setMode(FineIMU.Mode.ON_PAD);
        //robot.sleep(5000);

        robot.drive.resetEncoders();
        runTime.reset();
        while (robot.drive.drivingForwardConserv(800, 0.6, 0)) {
            updateColumn();
        }

        robot.drive.stop();

        robot.drive.imu.setMode(FineIMU.Mode.OFF_PAD);

        robot.drive.driveBackward(160, 1);

        robot.sleep(1000);
        column = vuforia.getColumn();

        robot.drive.setAngleTolerence(3);

        if (column == RelicRecoveryVuMark.UNKNOWN) {
            robot.drive.rotateNoReser(-15, 0.7);
            robot.sleep(1000);
            column = vuforia.getColumn();
        }

        robot.drive.rotate(0, 0.7);

        column = (column == RelicRecoveryVuMark.UNKNOWN) ? vuforia.getColumn() : column;

        telemetry.addData("Column", column);
        telemetry.update();
        //sleep(5000);
        int mmBase = 480;
        int mmStrafe = mmBase;

        switch(column) {
            case CENTER:
                mmStrafe = mmBase + 160;
                break;
            case UNKNOWN: // Fall-Through to Right
                column = RelicRecoveryVuMark.RIGHT;
            case RIGHT:
                mmStrafe = mmBase + 160 + 160;
                break;
            case LEFT:
                mmStrafe = mmBase;
                break;
        }

        runTime.reset();
        robot.drive.resetEncoders();
        while (robot.drive.strafingRange(mmStrafe, 0.8) && runTime.milliseconds() < 7000) {
            idle();
        }
        robot.drive.stop();
        //robot.drive.strafeRange(mmStrafe, 0.8);

        //robot.leftMotor.leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        robot.lift.grabBottom(0);


        robot.drive.resetEncoders();
        while (robot.drive.drivingForwardConserv(180, 1, 0)) {
            idle();
        }
        robot.drive.stop();

        robot.drive.driveBackward(100, 1);

        robot.lift.grabBottom(1);
        robot.sleep(1000);
        robot.drive.resetEncoders();
        while (robot.drive.drivingForwardConserv(120, 1, 0)) {
            idle();
        }

        robot.drive.driveBackward(200, 1);

        robot.reversePullBois.powerLift(0.5);
        robot.sleep(500);
        robot.reversePullBois.powerLift(0);
        robot.lift.grabTop(0.1);
        robot.lift.grabBottom(0.3);
        //robot.fishinPol.rotate(0);
        robot.sleep(500);
    }
}
