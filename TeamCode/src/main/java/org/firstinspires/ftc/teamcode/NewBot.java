package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

import lib.fine.systems.FineBot;
import lib.fine.systems.SpeedyBot;

/**
 * Created by drew on 11/23/17.
 */
@TeleOp(name = "TeleOperations v4.45423")
public class NewBot extends LinearOpMode {
    SpeedyBot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new SpeedyBot(this, DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Ready", null);
        telemetry.update();
        CRServo vex = hardwareMap.get(CRServo.class, "vex");

        waitForStart();
        while (opModeIsActive()) {
            driveTrain();
            intake();
            lift();
            double vexP = gamepad2.left_stick_y;
            telemetry.addData("vex P", vexP);
            telemetry.update();
            vex.setPower(vexP * 0.9);

        }
    }

    private void driveTrain() {
        double correction = (gamepad1.a) ? robot.drive.imu.align(0) : 0;

        telemetry.addData("Correction", correction);

        robot.drive.setLeftPower(-gamepad1.left_stick_y-correction);
        robot.drive.setRightPower(-gamepad1.right_stick_y+correction);

        double crossPower = (gamepad1.left_trigger > 0) ? -gamepad1.left_trigger : (gamepad1.right_trigger > 0) ? gamepad1.right_trigger : 0;

        robot.drive.setCrossPower(crossPower);
    }
    private void intake() {
        if (gamepad2.left_bumper)
            robot.suckyBois.setLeftPower(-1);
        else
            robot.suckyBois.setLeftPower(gamepad2.left_trigger);

        if (gamepad2.right_bumper)
            robot.suckyBois.setRightPower(-1);
        else
            robot.suckyBois.setRightPower(gamepad2.right_trigger);

    }


    final double sensitivity = 0.3;

    private void lift() {
        robot.lift.lift(-gamepad2.right_stick_y);
        if (gamepad2.a)
            robot.lift.flip(1);//intake
        else if (gamepad2.b)
            robot.lift.flip(0);//dump
        else if (gamepad2.x)
            robot.lift.flip(0.8);//horizontal
        else if (gamepad2.y)
            robot.lift.flip(0.45);
    }


}
