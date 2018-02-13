package lib.fine.systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import lib.fine.core.FineMotor;

/**
 * Created by zoghbya8828 on 2/13/2018.
 */

public class Flipper {

    private Servo servo;
    private FineMotor lift;
    private LinearOpMode opMode;

    public Flipper(LinearOpMode opMode) {
        this.opMode = opMode;

        servo = opMode.hardwareMap.get(Servo.class, "flip");
        lift = new FineMotor(opMode, "lift");
        lift.setDirection(DcMotorSimple.Direction.FORWARD);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void flip(double flippiness) {
        servo.setPosition(flippiness);
    }
}
