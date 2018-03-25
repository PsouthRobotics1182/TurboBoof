package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

import lib.fine.systems.FineBot;
import lib.fine.systems.Flipper;
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

        waitForStart();
        while (opModeIsActive()) {

            driveTrain();
            intake();
            lift();

            telemetry.update();
        }
    }

    private void driveTrain() {

        double correction = (gamepad1.a) ? robot.drive.imu.align(0) : 0;

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

    private void lift() {

        robot.lift.lift(-gamepad2.right_stick_y);//powers the 4 seros


        //DO NOT CHANGE ORDER OF THESE STATEMENTS
        //THEY ARE IN THIS ORDER TO PRIORITIZE DRIVER CONTROLS OVER AUTOMATED ONES
        if (robot.lift.atBottom())
            robot.lift.flip(Flipper.INTAKE);
        else if (!robot.lift.atBottom() && !robot.lift.atTop())
            robot.lift.flip(Flipper.HORIZONTAL);

        if (gamepad2.dpad_up)
            robot.lift.flip(Flipper.VERTICAL);
        else if (gamepad2.dpad_right)
            robot.lift.flip(Flipper.HORIZONTAL);
        else if (gamepad2.dpad_left)
            robot.lift.flip(Flipper.OVER_DUMP);
        else if (gamepad2.dpad_down)
            robot.lift.flip(Flipper.INTAKE);

    }


}
