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
@TeleOp(name = "TeleOperations v3.45423")
public class FineTele extends LinearOpMode {
    FineBot robot;
    @Override
    public void runOpMode() throws InterruptedException {
        robot = new FineBot(this, DcMotor.RunMode.RUN_WITHOUT_ENCODER, Color.RED);

        telemetry.addData("Ready", null);
        telemetry.update();
        waitForStart();
        while (opModeIsActive()) {
            driveTrain();

            lift();
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


    double sensitivity = 0.3;
    private void lift() {
        robot.lift.lift(-gamepad2.right_stick_y);

        if (gamepad2.left_trigger > sensitivity)
            robot.lift.grabBottom(0.3);
        else if (gamepad2.right_trigger > sensitivity)
            robot.lift.grabBottom(1);

        if (gamepad2.left_bumper)
            robot.lift.grabTop(0.1);
        else if (gamepad2.right_bumper)
            robot.lift.grabTop(1);
    }

}
