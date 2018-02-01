package lib.fine.systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

import lib.fine.core.FineMotor;

/**
 * Created by drew on 11/24/17.
 */

public class FinestLift {
    private FineMotor lift;
    private Servo leftGrasper;
    private Servo rightGrasper;
    private Servo leftGrasperTop;
    private Servo rightGrasperTop;
    private LinearOpMode opMode;

    private static final double LEFT_UNGRAB_BOTTOM = 0.5;
    private static final double RIGHT_UNGRAB_BOTTOM = 0.49;
    private static final double LEFT_GRAB_BOTTOM = 0;
    private static final double RIGHT_GRAB_BOTTOM = 1;
    private static final double LEFT_GRAB_TOP = 0;
    private static final double RIGHT_GRAB_TOP = 0.8;
    private static final double LEFT_UNGRAB_TOP = 0.85;
    private static final double RIGHT_UNGRAB_TOP = 0.1;
    public static final double STAY_POWER = 0.0011;

    public FinestLift(LinearOpMode opMode, DcMotor.RunMode mode) {
        lift = new FineMotor(opMode, "arm");
        rightGrasper = opMode.hardwareMap.get(Servo.class, "leftGrasp");
        leftGrasper = opMode.hardwareMap.get(Servo.class, "rightGrasp");
        leftGrasperTop = opMode.hardwareMap.get(Servo.class, "leftGraspTop");
        rightGrasperTop = opMode.hardwareMap.get(Servo.class, "rightGraspTop");

        lift.setMode(mode);
        lift.setDirection(DcMotorSimple.Direction.FORWARD);
        this.opMode = opMode;

        grabBottom(0.3);
        grabTop(0.1);
    }
    public void lift(double power) {
        lift.setPower(power);
    }

    public void grabBottom(double grabiness) {
            /* Note: Using a double like an enum could be problematic if you end up accidentally
             * casting between different types with different accuracies such as as a float.
             * Using a range for float comparison (n < max && n > min) might be a better idea.
             * -David
             */
        leftGrasper.setPosition(LEFT_GRAB_BOTTOM * grabiness + LEFT_UNGRAB_BOTTOM * (1-grabiness));
        rightGrasper.setPosition(RIGHT_GRAB_BOTTOM * grabiness + RIGHT_UNGRAB_BOTTOM * (1-grabiness));
        //leftGrasperTop.setPosition(LEFT_GRAB_TOP * grabiness + LEFT_UNGRAB_TOP * (1-grabiness));
        //rightGrasperTop.setPosition(RIGHT_GRAB_TOP * grabiness + RIGHT_UNGRAB_TOP * (1-grabiness));
    }
    public void grabTop(double grabiness) {
            /* Note: Using a double like an enum could be problematic if you end up accidentally
             * casting between different types with different accuracies such as as a float.
             * Using a range for float comparison (n < max && n > min) might be a better idea.
             * -David
             */
        //leftGrasper.setPosition(LEFT_GRAB_BOTTOM * grabiness + LEFT_UNGRAB * (1-grabiness));
        //rightGrasper.setPosition(RIGHT_GRAB * grabiness + RIGHT_UNGRAB * (1-grabiness));
        leftGrasperTop.setPosition(LEFT_GRAB_TOP * grabiness + LEFT_UNGRAB_TOP * (1-grabiness));
        rightGrasperTop.setPosition(RIGHT_GRAB_TOP * grabiness + RIGHT_UNGRAB_TOP * (1-grabiness));
    }
    public void leftSwingBottom(double grabiness) {
        leftGrasper.setPosition(LEFT_GRAB_BOTTOM * grabiness + LEFT_UNGRAB_BOTTOM * (1-grabiness));
        rightGrasper.setPosition(RIGHT_UNGRAB_BOTTOM);
    }
    public void rightSwingBottom(double grabiness) {
        leftGrasper.setPosition(LEFT_UNGRAB_BOTTOM);
        rightGrasper.setPosition(RIGHT_GRAB_BOTTOM * grabiness + RIGHT_UNGRAB_BOTTOM * (1-grabiness));
    }

    public void dab() {
        lift.setPower(1);
        opMode.sleep(700);
        leftGrasperTop.setPosition(LEFT_GRAB_BOTTOM);
        rightGrasperTop.setPosition(RIGHT_UNGRAB_BOTTOM);
    }
    public void addTelemetry() {
        lift.addTelemetry();
        opMode.telemetry.addData("left servo", leftGrasper.getPosition());
        opMode.telemetry.addData("right servo", rightGrasper.getPosition());
    }
}
