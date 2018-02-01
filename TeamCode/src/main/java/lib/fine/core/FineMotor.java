package lib.fine.core;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by drew on 11/23/17.
 */

public class FineMotor {

    private DcMotor motor;
    private int zeroPosition = 0;
    private DcMotor.RunMode mode = DcMotor.RunMode.RUN_USING_ENCODER;
    private LinearOpMode opMode;
    private String name;
    public FineMotor (LinearOpMode opMode, String name) {
        this.opMode = opMode;
        this.name = name;
        motor = opMode.hardwareMap.get(DcMotor.class, name);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    public void setPower(double power) {
        motor.setPower(power);
    }
    public void resetEncoder() {
        zeroPosition = motor.getCurrentPosition();
    }
    public int getCurrentPosition() {
        return motor.getCurrentPosition() - zeroPosition;
    }
    public int getAbsolutePosition() {
        return motor.getCurrentPosition();
    }
    public void setDirection(DcMotorSimple.Direction direction) {
        motor.setDirection(direction);
    }
    public void setMode(DcMotor.RunMode mode) {
        this.mode = mode;
        motor.setMode(mode);
    }
    public void setTargetPosition(int pos) {
        motor.setTargetPosition(pos);
    }
    public void addTelemetry() {
        opMode.telemetry.addData(name + " power", motor.getPower());
        opMode.telemetry.addData(name + " position", getCurrentPosition());
        opMode.telemetry.addData(name + " absolute position", getAbsolutePosition());
    }
}
