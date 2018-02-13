package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import lib.fine.systems.SpeedyBot;

@TeleOp(name = "TeleOperations v3.45423")
public class FineTele extends LinearOpMode {
    SpeedyBot robot;
    @Override
    public void runOpMode() throws InterruptedException {
        robot = new SpeedyBot(this, DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Ready", null);
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            driveTrain();
            goodSucc();
            flipp();
        }
    }

    private void driveTrain() {
        double correction;
        if (gamepad1.a)
            correction = robot.drive.imu.align(0);
        else
            correction = 0;

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

    private void flipp() {
        if (gamepad2.dpad_up)
            robot.flipper.flip(1);
        else if (gamepad2.dpad_down)
            robot.flipper.flip(0);
    }


}
