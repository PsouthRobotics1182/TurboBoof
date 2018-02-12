package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import lib.fine.systems.FineBot;
import lib.fine.systems.FishinPol;
import lib.fine.systems.SpeedyBot;

/**
 * Created by drew on 11/23/17.
 */
@TeleOp(name = "TeleOperations v2.45423")
public class FineTele extends LinearOpMode {
    SpeedyBot robot;
    @Override
    public void runOpMode() throws InterruptedException {
        robot = new SpeedyBot(this, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.lift.grabTop(0.1);
        robot.lift.grabBottom(0.3);
        //robot.juulHitler.up();
        //robot.juulHitler.middle();
        telemetry.addData("Ready", null);
        telemetry.update();
        waitForStart();
        while (opModeIsActive()) {
            driveTrain();
            goodSucc();
            //juulHitler();
            //robot.fishinPol.setPower(gamepad2.left_stick_y);
            //robot.juulHitler.addTelemetry();

            //telemetry.update();
        }
    }

    private void driveTrain() {
        double correction;
        if (gamepad1.a)
            correction = robot.drive.imu.align(0);
        else
            correction = 0;
        telemetry.addData("Correction", correction);

        robot.drive.setLeftPower(-gamepad1.left_stick_y-correction);
        robot.drive.setRightPower(-gamepad1.right_stick_y+correction);
        double crossPower;
        if (gamepad1.left_trigger > 0)
            crossPower = -gamepad1.left_trigger;
        else if (gamepad1.right_trigger > 0)
            crossPower = gamepad1.right_trigger;
        else
            crossPower = 0;

        robot.drive.setCrossPower(crossPower);
    }

    private void goodSucc() {
        double leftPower = gamepad2.left_trigger;
        double rightPower = gamepad2.right_trigger;
        if (gamepad2.right_bumper)
            rightPower = -1;
        if (gamepad2.left_bumper)
            leftPower = -1;

        robot.suckyBois.setLeftPower(leftPower);
        robot.suckyBois.setRightPower(rightPower);
    }


}
