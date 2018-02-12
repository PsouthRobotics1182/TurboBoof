package lib.fine.systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import lib.fine.core.FineMotor;

/**
 * Created by Robotics on 2/12/2018.
 */

public class SuckyBois {

    private LinearOpMode opMode;
    private FineMotor leftM;
    private FineMotor rightM;

    public SuckyBois(LinearOpMode opMode) {
        this.opMode = opMode;
        leftM = new FineMotor(opMode, "leftM");
        leftM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftM.setDirection(DcMotorSimple.Direction.FORWARD);
        rightM = new FineMotor(opMode, "rightM");
        rightM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightM.setDirection(DcMotorSimple.Direction.REVERSE);
    }
    public void setLeftPower(double leftPower) {
        leftM.setPower(leftPower);
    }
    public void setRightPower(double rightPower) {
        rightM.setPower(rightPower);
    }
}
