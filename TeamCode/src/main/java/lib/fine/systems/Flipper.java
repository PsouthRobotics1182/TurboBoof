package lib.fine.systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import lib.fine.core.FineMotor;

/**
 * Created by drew on 11/24/17.
 */

public class Flipper {
    //private FineMotor lift;
    private Servo flipper;
    private CRServo topLeft;
    private CRServo topRight;
    private CRServo bottomRight;
    private CRServo bottomLeft;

    private LinearOpMode opMode;

    public Flipper(LinearOpMode opMode) {
        //lift = new FineMotor(opMode, "leftMotor");
        flipper = opMode.hardwareMap.get(Servo.class, "flip");

        topLeft = opMode.hardwareMap.get(CRServo.class, "topLeft");
        topRight = opMode.hardwareMap.get(CRServo.class, "topRight");
        bottomRight = opMode.hardwareMap.get(CRServo.class, "bottomRight");
        bottomLeft = opMode.hardwareMap.get(CRServo.class, "bottomLeft");


        //lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //lift.setDirection(DcMotorSimple.Direction.FORWARD);
        this.opMode = opMode;
        flip(1);

    }
    public void lift(double power) {
        topLeft.setPower(power);
        topRight.setPower(-power);
        bottomLeft.setPower(power);
        bottomRight.setPower(-power);
    }

    public void flip (double flipiness) {
        flipper.setPosition(flipiness);
    }
    public void addTelemetry() {
        //lift.addTelemetry();
    }
}
