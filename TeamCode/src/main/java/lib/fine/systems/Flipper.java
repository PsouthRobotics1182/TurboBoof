package lib.fine.systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import lib.fine.core.FineMotor;

/**
 * Created by drew on 11/24/17.
 */

public class Flipper {
    private FineMotor lift;
    private Servo servo;
    private LinearOpMode opMode;

    public Flipper(LinearOpMode opMode) {
        lift = new FineMotor(opMode, "leftMotor");
        servo = opMode.hardwareMap.get(Servo.class, "flip");

        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lift.setDirection(DcMotorSimple.Direction.FORWARD);
        this.opMode = opMode;
        flip(1);

    }
    public void lift(double power) {
        lift.setPower(power);
    }

    public void flip (double flipiness) {
        servo.setPosition(flipiness);
    }
    public void addTelemetry() {
        lift.addTelemetry();
    }
}
