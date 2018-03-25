package lib.fine.systems;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import lib.fine.core.FineIMU;
import lib.fine.core.FineMotor;

/**
 * Created by drew on 10/6/17.
 */

public class FineSlide {
    public FineIMU imu;
    private FineMotor leftFront;
    private FineMotor rightFront;
    private FineMotor leftRear;
    private FineMotor rightRear;
    private FineMotor cross;
    private ModernRoboticsI2cRangeSensor range;


    private LinearOpMode opMode;

    private static int ANGLE_TOLERENCE = 1;

    public static final int WHEEL_RADIUS_MM = 90;
    public static final int WHEEL_CIRCUMFRENCE = (int) (Math.PI  * WHEEL_RADIUS_MM);
    public static final double CHASSIS_WHEEL_SPACING_MM = 16 * 2.54; //mm


    public FineSlide(LinearOpMode opMode, DcMotor.RunMode mode) {
        this.opMode = opMode;
        leftFront = new FineMotor(opMode, "leftF");
        rightFront = new FineMotor(opMode, "rightF");
        leftRear = new FineMotor(opMode, "leftR");
        rightRear = new FineMotor(opMode, "rightR");
        cross = new FineMotor(opMode, "cross");
        imu = new FineIMU(opMode, "imu");
        range = opMode.hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "range");

        leftFront.setMode(mode);
        rightFront.setMode(mode);
        leftRear.setMode(mode);
        rightRear.setMode(mode);
        cross.setMode(mode);

        forward(true);
        imu.resetPID();
        imu.setMode(FineIMU.Mode.ON_PAD);
        resetEncoders();
    }

    public void setLeftPower(double power) {
        leftFront.setPower(power);
        leftRear.setPower(power);
    }

    public void setRightPower(double power) {
        rightFront.setPower(power);
        rightRear.setPower(power);
    }

    public void setFrontPower(double power) {
        leftFront.setPower(power);
        rightFront.setPower(power);
    }

    public void setRearPower(double power) {
        leftRear.setPower(power);
        rightRear.setPower(power);
    }
    public void setForwardPower(double power) {
        setLeftPower(power);
        setRightPower(power);
    }

    public void setCrossPower(double power) {
        cross.setPower(power);
    }

    public void stop() {
        setLeftPower(0);
        setRightPower(0);
        setCrossPower(0);
    }

    public boolean drivingForward(int mm, double power) {
        return drivingForward(mm, power, 0);
    }

    public boolean drivingForward(int mm, double power, int angle) {
        if (Math.abs(Math.abs(getPositionMin())) < Math.abs(MM2ticks(mm)) && opMode.opModeIsActive()) {
            double correction = imu.align(angle);
            //double correction = imu.correction(0);
            setRightPower(power+correction);//-correction);
            setLeftPower(power-correction);//+correction);
            return true;
        } else {
            imu.resetPID();
            return false;
        }
    }
    public boolean drivingForwardNoGyro(int mm, double power) {
        if (Math.abs(Math.abs(getPositionAve())) < Math.abs(MM2ticks(mm)) && opMode.opModeIsActive()) {            //double correction = imu.correction(0);
            setRightPower(power);//-correction);
            setLeftPower(power);//+correction);
            return true;
        } else {
            imu.resetPID();
            return false;
        }
    }
    public boolean drivingForwardConserv(int mm, double power, int angle) {
        if (Math.abs(Math.abs(getPositionAve())) < Math.abs(MM2ticks(mm)) && opMode.opModeIsActive()) {
            double correction = imu.align(angle);
            //double correction = imu.correction(0);
            setRightPower(power+correction);//-correction);
            setLeftPower(power-correction);//+correction);
            return true;
        } else {
            imu.resetPID();
            return false;
        }
    }

    public boolean drivingForwardTillLevel(double power) {
        double[] levelness = imu.getLevelness();
        if ((levelness[0] > ANGLE_TOLERENCE || levelness[1] > ANGLE_TOLERENCE) && opMode.opModeIsActive()) {
            double correction = imu.align(0);
            //double correction = imu.correction(0);
            setRightPower(power+correction);//-correction);
            setLeftPower(power-correction);//+correction);
            return true;
        } else {
            imu.resetPID();
            return false;
        }
    }


    public void driveForward(int mm, double power) {
        resetEncoders();
        while (drivingForward(mm, power) && opMode.opModeIsActive()) {
            opMode.telemetry.addData("Driving", "Now");
            opMode.telemetry.update();
        }
        stop();
    }

    public boolean drivingBackward(int mm, double power) {
        //forward(false);
        if (Math.abs(getPositionMin()) < Math.abs(MM2ticks(mm)) && opMode.opModeIsActive()) {
            //double correction = imu.correction(0);
            setRightPower(-power);//+correction);
            setLeftPower(-power);//-correction);
            return true;
        } else {
            imu.resetPID();
            //forward(true);
            return false;
        }
    }

    public boolean drivingBackwardConsv(int mm, double power) {
        //forward(false);
        if (Math.abs(getPositionAve()) < Math.abs(MM2ticks(mm)) && opMode.opModeIsActive()) {
            //double correction = imu.correction(0);
            setRightPower(-power);//+correction);
            setLeftPower(-power);//-correction);
            return true;
        } else {
            imu.resetPID();
            //forward(true);
            return false;
        }
    }

    public void driveBackward(int mm, double power) {
        resetEncoders();
        while (drivingBackward(mm, power) && opMode.opModeIsActive()) {
            opMode.telemetry.addData("Driving", "Now");
            opMode.telemetry.update();
        }
        stop();
    }
    //positive is left
    public boolean strafingLeft(int mm, double power) {
        power = -power;
        if (Math.abs(cross.getCurrentPosition()) < Math.abs(MM2ticks(mm)) && opMode.opModeIsActive()) {
            //double correction = imu.correction(0);
            //setLeftPower(correction);
            //setRightPower(-correction);
            setCrossPower(power);
            return true;
        } else {
            imu.resetPID();
            return false;
        }
    }
    public void strafeLeft(int mm, double power) {
        resetEncoders();
        while (strafingLeft(mm, power) && opMode.opModeIsActive()) {
            opMode.telemetry.addData("Driving", "Now");
            opMode.telemetry.update();
        }
        stop();
    }

    public boolean strafingRight(int mm, double power) {
        return strafingLeft(mm, -power);
    }

    public void strafeRight(int mm, double power) {
        resetEncoders();
        while (strafingRight(mm, power) && opMode.opModeIsActive()) {
            opMode.telemetry.addData("Driving", "Now");
            opMode.telemetry.update();
        }
        stop();
    }

    public boolean strafingRange(int mm, double power) {
        power = -power;
        double error = mm - range.getDistance(DistanceUnit.MM);
        if (Math.abs(error) > ANGLE_TOLERENCE && opMode.opModeIsActive()) {
            //power = power * error/Math.abs(mm);
            //double correction = imu.correction(0);
            //setLeftPower(correction);
            //setRightPower(-correction);
            setCrossPower(power * error/Math.abs(error));
            return true;
        } else {
            imu.resetPID();
            return false;
        }
    }

    public void strafeRange(int mm, double power) {
        resetEncoders();
        while (strafingRange(mm, power) && opMode.opModeIsActive()) {
            opMode.telemetry.addData("Driving", "Now");
            opMode.telemetry.update();
        }
        stop();
    }

    public boolean rotating(double angle, double maxPower) {
        if (Math.abs(imu.getHeading() - angle) > ANGLE_TOLERENCE && opMode.opModeIsActive()) {
            double correction = imu.align(angle);
            final double correction_abs = Math.abs(correction);
            correction = (correction_abs > maxPower) ? maxPower * correction / correction_abs : correction;

            opMode.telemetry.clearAll();
            opMode.telemetry.addData("Correction", correction);
            opMode.telemetry.update();
            setRightPower(correction);
            setLeftPower(-correction);

            return true;
        } else {
            imu.resetPID();
            //imu.resetAngle();
            return false;
        }
    }

    public void rotating(double maxPower) {
        setLeftPower(-maxPower);
        setRightPower(maxPower);
    }

    public void rotate(double angle, double maxPower) {
        resetEncoders();
        while (rotating(angle, maxPower) && opMode.opModeIsActive()) {
            opMode.telemetry.addData("Driving", "Now");
            opMode.telemetry.update();
        }
        imu.resetAngle();
        stop();
    }
    public void rotateNoReser(double angle, double maxPower) {
        resetEncoders();
        while (rotating(angle, maxPower) && opMode.opModeIsActive()) {
            opMode.telemetry.addData("Driving", "Now");
            opMode.telemetry.update();
        }
        stop();
    }
    //drives in the driection of the given vector with the orientation locked
    public void drive(double driveHeading, double magnitude) {
        //double angleAdjust = imu.correction(faceAngle);
        //double orientedAngle = driveHeading - faceAngle;
        final double yComp = magnitude * Math.sin(driveHeading);
        double xComp = magnitude * Math.cos(driveHeading);
        double leftComp = yComp;
        double rightComp = yComp;

        final double max = Math.max(Math.max(xComp, leftComp), rightComp);

        if (max > 1) {
            leftComp /= max;
            rightComp /= max;
            xComp /= max;
        }

        setLeftPower(leftComp);
        setRightPower(rightComp);
        setCrossPower(xComp);
    }

    public int getPositionLeft() {
        return (leftFront.getCurrentPosition() + leftRear.getCurrentPosition())/2;
    }
    public int getPositionRight() {
        return (rightFront.getCurrentPosition() + rightRear.getCurrentPosition())/2;
    }

    public int getPositionMin() {
        final int p1 = leftRear.getCurrentPosition();
        final int p2 = rightRear.getCurrentPosition();
        final int p3 = leftFront.getCurrentPosition();
        final int p4 = rightFront.getCurrentPosition();
        return Math.min(Math.min(Math.min(p1, p2), p3), p4);
    }

    public int getPositionAve() {
        final int p1 = leftRear.getCurrentPosition();
        final int p2 = rightRear.getCurrentPosition();
        final int p3 = leftFront.getCurrentPosition();
        final int p4 = rightFront.getCurrentPosition();
        return (p1 + p2 + p3 + p4) / 4;
    }

    public int getPositionMax() {
        final int p1 = leftRear.getCurrentPosition();
        final int p2 = rightRear.getCurrentPosition();
        final int p3 = leftFront.getCurrentPosition();
        final int p4 = rightFront.getCurrentPosition();
        return Math.max(Math.max(Math.max(p1, p2), p3), p4);
    }

    public void setMode(FineIMU.Mode mode) {
        imu.setMode(mode);
    }

    public void resetEncoders() {
        leftFront.resetEncoder();
        leftRear.resetEncoder();
        rightFront.resetEncoder();
        rightRear.resetEncoder();
        cross.resetEncoder();
    }

    public double getRange() {
        return range.getDistance(DistanceUnit.MM);
    }
    public void setAngleTolerence(int tolerence){
        ANGLE_TOLERENCE = tolerence;
    }

    public void resetAngle() {
        imu.resetAngle();
    }

    private void forward(boolean t) {
        final DcMotorSimple.Direction l = (t) ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD;
        final DcMotorSimple.Direction r = (t) ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE;
        final DcMotorSimple.Direction x = r;
        leftFront.setDirection(l);
        leftRear.setDirection(l);
        rightFront.setDirection(r);
        rightRear.setDirection(r);
        cross.setDirection(x);
    }

    public int MM2ticks(double mm) {

        final double rotations = mm/ WHEEL_CIRCUMFRENCE;
        final double ticks = leftRear.getTicksPerRev() * rotations;
        return (int) ticks;
    }
    public double ticks2MM(double ticks) {

        double rotations = ticks / leftRear.getTicksPerRev();
        double mm = rotations * WHEEL_CIRCUMFRENCE;
        return mm;

    }

    //LOG HELPER FUNCTIONS
    double robotAverageTravelSpeed = 200;//in ticks/sec
    private double time(double targetDistance) {//predicts time to travel based on distance
        return targetDistance/robotAverageTravelSpeed;
    }

    double c = 7.3;//constant to scale the predicted time to the k value
    private double k(double time) {
        return (1/time) * c;
    }
    //gives velocity in ticks/sec for robot pos to follow log curve
    private double logVelocity(double currentPos, double targetPos) {
        double velocity = k(time(targetPos)) * currentPos;
        velocity = velocity * (1 - (currentPos/targetPos));
        return velocity;
    }
    //converts velocity to power so peak planned velocity is 1
    private double logPower(double currentPos, double targetPos) {
        //get desired speed and shift horizontally so the beggining of the curve is at 0 ticks
        double desiredSpeed = logVelocity(currentPos - time(targetPos), targetPos);
        double power = desiredSpeed/logVelocity(0, targetPos);//scale speed based on max planned speed
        return power;
    }

    public void addTelemetry() {
        leftFront.addTelemetry();
        leftRear.addTelemetry();
        rightFront.addTelemetry();
        rightRear.addTelemetry();
        cross.addTelemetry();
        imu.addTelemetry();
        opMode.telemetry.addData("range", range.getDistance(DistanceUnit.MM));
    }
}
