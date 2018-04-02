package lib.fine.systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

import lib.fine.core.FineMotor;
import lib.fine.core.LimitSwitch;

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

    private LimitSwitch topLimit;
    private LimitSwitch bottomLimit;

    private LinearOpMode opMode;
    public static double VERTICAL = 0.45;
    public static double HORIZONTAL = 0.8;
    public static double INTAKE = 1;
    public static double OVER_DUMP = 0;


    public Flipper(LinearOpMode opMode) {
        flipper = opMode.hardwareMap.get(Servo.class, "flip");

        topLeft = opMode.hardwareMap.get(CRServo.class, "topLeft");
        topRight = opMode.hardwareMap.get(CRServo.class, "topRight");
        bottomRight = opMode.hardwareMap.get(CRServo.class, "bottomRight");
        bottomLeft = opMode.hardwareMap.get(CRServo.class, "bottomLeft");

        topLimit = new LimitSwitch("topSwitch", opMode);
        bottomLimit = new LimitSwitch("bottomSwitch", opMode);

        this.opMode = opMode;
        flip(1);

    }
    public void lift(double power) {
        if (power >= 0 && atTop())
            power = 0;
        else if (power <= 0 && atBottom())
            power = 0;
        setPower(power);

    }

    private void setPower(double power) {
        topLeft.setPower(power);
        topRight.setPower(-power);
        bottomLeft.setPower(power);
        bottomRight.setPower(-power);
    }

    public void flip (double flipiness) {
        flipper.setPosition(flipiness);
    }
    public boolean atTop() {
        return topLimit.isPressed();
    }
    public boolean atBottom() {
        return bottomLimit.isPressed();
    }
    public void addTelemetry() {
        //lift.addTelemetry();
    }

}
