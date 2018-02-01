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
import lib.fine.systems.FinestLift;

/**
 * Created by drew on 11/25/17.
 */
@Autonomous(name = "Strawberry Juul V2.386")
public class RedJuul extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        FineBot robot = new FineBot(this, DcMotor.RunMode.RUN_USING_ENCODER, Color.RED);
        CrackedMonocle vuforia = new CrackedMonocle(this);
        ElapsedTime runTime = new ElapsedTime();
        vuforia.activate();

        telemetry.addData("Ready", null);
        telemetry.update();

        waitForStart();
        runTime.reset();

        RelicRecoveryVuMark column = vuforia.getColumn();

        robot.fishinPol.rotate(1);

        robot.lift.grabBottom(1);
        robot.lift.grabTop(1);
        robot.sleep(1000);
        robot.lift.lift(1);
        robot.sleep(1000);
        robot.lift.lift(FinestLift.STAY_POWER);

        robot.reversePullBois.powerLift(0.5);
        robot.sleep(500);
        robot.reversePullBois.powerLift(0);

        robot.juulHitler.hitRed();

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
            if (column == RelicRecoveryVuMark.UNKNOWN)
                column = vuforia.getColumn();
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

        if (column == RelicRecoveryVuMark.UNKNOWN)
            column = vuforia.getColumn();
        telemetry.addData("Column", column);
        telemetry.update();
        //sleep(5000);
        int mmStrafe = 660;

        int mmBase = 610;
        if (column == RelicRecoveryVuMark.UNKNOWN)
            column = RelicRecoveryVuMark.RIGHT;
        if (column == RelicRecoveryVuMark.RIGHT)
            mmStrafe = mmBase;
        else if (column == RelicRecoveryVuMark.CENTER)
            mmStrafe = mmBase + 170;
        else if (column == RelicRecoveryVuMark.LEFT)
            mmStrafe = mmBase + 190 + 190;

        robot.drive.strafeRange(mmStrafe, 0.8);

        robot.lift.lift(0);

        robot.lift.leftSwingBottom(0.8);

        double currentPos = robot.drive.getRange();
        robot.drive.resetEncoders();
        while (robot.drive.strafingRight((int) currentPos-90, 1)) {
            idle();
        }
        robot.drive.stop();

        robot.lift.leftSwingBottom(1);
        robot.drive.resetEncoders();
        while (robot.drive.drivingForwardConserv(180, 1, 0)) {
            idle();
        }
        robot.drive.stop();
        robot.drive.resetEncoders();

        robot.drive.driveBackward(200, 1);

        robot.reversePullBois.powerLift(0.5);
        robot.sleep(500);
        robot.reversePullBois.powerLift(0);
        robot.lift.grabTop(0.1);
        robot.lift.grabBottom(0.3);
        robot.fishinPol.rotate(0);
        robot.sleep(500);
    }
}
