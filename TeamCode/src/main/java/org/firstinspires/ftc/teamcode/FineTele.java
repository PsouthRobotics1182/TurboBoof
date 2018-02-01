package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import lib.fine.systems.FineBot;
import lib.fine.systems.FishinPol;

/**
 * Created by drew on 11/23/17.
 */
@TeleOp(name = "TeleOperations v2.45423")
public class FineTele extends LinearOpMode {
    FineBot robot;
    @Override
    public void runOpMode() throws InterruptedException {
        robot = new FineBot(this, DcMotor.RunMode.RUN_WITHOUT_ENCODER, Color.RED);
        robot.lift.grabTop(0.1);
        robot.lift.grabBottom(0.3);
        //robot.juulHitler.up();
        //robot.juulHitler.middle();
        telemetry.addData("Ready", null);
        telemetry.update();
        waitForStart();
        while (opModeIsActive()) {
            driveTrain();
            bullyBois();
            liftControl();
            //juulHitler();
            fishinForFishEyedFools();
            //robot.fishinPol.setPower(gamepad2.left_stick_y);
            //robot.juulHitler.addTelemetry();

            //telemetry.update();
        }
    }

    private void fishinForFishEyedFools() {
        robot.fishinPol.setPower(gamepad2.left_stick_y);
        if (gamepad2.dpad_down)
            robot.fishinPol.rotate(0.5);
        else if (gamepad2.dpad_up)
            robot.fishinPol.rotate(0);

        if (gamepad2.a)
            robot.fishinPol.grab(1);
        else if (gamepad2.b)
            robot.fishinPol.grab(0);
    }

    private void juulHitler() {
        if (gamepad2.dpad_up) {
            robot.juulHitler.up(1);
        } else if (gamepad2.dpad_down) {
            robot.juulHitler.down();
        }

        if (gamepad2.a)
            robot.juulHitler.hitRed();

        if (gamepad2.x)
            robot.juulHitler.left();
        else if (gamepad2.y)
            robot.juulHitler.middle();
        else if (gamepad2.b)
            robot.juulHitler.right();
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
    private void liftControl() {
        if (gamepad2.right_bumper)
            robot.lift.grabTop(1);
        else if (gamepad2.left_bumper)
            robot.lift.grabTop(0.1);
        else if (gamepad2.right_trigger > 0.3)
            robot.lift.grabBottom(1);
        else if (gamepad2.left_trigger > 0.3)
            robot.lift.grabBottom(0.3);

        robot.lift.lift(gamepad2.right_stick_y);
    }

    private void bullyBois() {
        robot.reversePullBois.in();
        if (gamepad1.right_bumper)
            robot.reversePullBois.powerLift(-0.8);
        else if (gamepad1.left_bumper)
            robot.reversePullBois.powerLift(0.5);
        else
            robot.reversePullBois.powerLift(0);
    }
}
