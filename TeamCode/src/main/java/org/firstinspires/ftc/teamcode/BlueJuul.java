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
@Autonomous(name = "BlueBerry Juul V2.266")
public class BlueJuul extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        FineBot robot = new FineBot(this, DcMotor.RunMode.RUN_USING_ENCODER, Color.BLUE);
        CrackedMonocle vuforia = new CrackedMonocle(this);
        ElapsedTime runTime = new ElapsedTime();
        vuforia.activate();

        telemetry.addData("Ready", null);
        telemetry.update();

        waitForStart();
        runTime.reset();

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

        robot.juulHitler.hitBlue();

        robot.sleep(500);
        robot.reversePullBois.powerLift(-0.8);
        robot.sleep(500);
        robot.reversePullBois.powerLift(0);

        RelicRecoveryVuMark column = vuforia.getColumn();

        robot.drive.resetEncoders();
        //double power = 0;
        robot.drive.strafeLeft(780, 1);
        robot.drive.stop();

        robot.drive.imu.setMode(FineIMU.Mode.ON_PAD);
        column = vuforia.getColumn();
        //robot.sleep(5000);

        robot.drive.resetEncoders();
        runTime.reset();
        while (robot.drive.drivingBackwardConsv(800, 0.6)) {
            idle();
        }
        robot.drive.stop();

        robot.drive.imu.setMode(FineIMU.Mode.OFF_PAD);

        robot.drive.resetEncoders();
        while (robot.drive.drivingForwardConserv(150, 0.6, -90)) {
            idle();
        }
        robot.drive.stop();
        robot.drive.setAngleTolerence(3);

        telemetry.addData("Column", column);
        telemetry.update();
        int mmStrafe = 610;
        int mmBase = 500;

        if (column == RelicRecoveryVuMark.UNKNOWN)
            column = RelicRecoveryVuMark.RIGHT;
        if (column == RelicRecoveryVuMark.LEFT)
            mmStrafe = mmBase;
        else if (column == RelicRecoveryVuMark.CENTER)
            mmStrafe = mmBase + 190;
        else if (column == RelicRecoveryVuMark.RIGHT)
            mmStrafe = mmBase + 190 + 190;

        robot.drive.resetEncoders();
        runTime.reset();
        while (robot.drive.strafingRange(mmStrafe, 0.8)&& runTime.milliseconds() < 5000 ){
            idle();
        }
        robot.drive.stop();

        //robot.drive.rotate(-90, 0.7);

        //robot.drive.rotate(0, 0.5);
        //robo

        runTime.reset();
        while (robot.drive.rotating(90, 0.5) && runTime.milliseconds() < 10000) {
            if (column == RelicRecoveryVuMark.UNKNOWN)
                column = vuforia.getColumn();
            idle();
        }
        robot.drive.stop();
        runTime.reset();
        while (robot.drive.rotating(90, 0.5) && runTime.milliseconds() < 5000) {
            if (column == RelicRecoveryVuMark.UNKNOWN)
                column = vuforia.getColumn();
            idle();
        }
        robot.drive.stop();
        robot.drive.imu.resetAngle();
        robot.lift.grabBottom(0.3);

        robot.drive.resetEncoders();
        while (robot.drive.drivingForwardConserv(110, 0.5, 0) && runTime.milliseconds() < 5000) {
            if (column == RelicRecoveryVuMark.UNKNOWN)
                column = vuforia.getColumn();
            idle();
        }
        robot.drive.stop();

        robot.drive.driveBackward(110, 1);

        robot.drive.resetEncoders();
        while (robot.drive.drivingForwardConserv(130, 0.5, 0) && runTime.milliseconds() < 5000) {
            if (column == RelicRecoveryVuMark.UNKNOWN)
                column = vuforia.getColumn();
            idle();
        }

        robot.drive.stop();

        robot.drive.driveBackward(200, 1);


        robot.reversePullBois.powerLift(0.5);
        robot.sleep(500);
        robot.reversePullBois.powerLift(0);
        robot.lift.grabTop(0.1);
        robot.fishinPol.rotate(0);
        robot.sleep(500);
    }
}
