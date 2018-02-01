package lib.fine.core;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

/**
 * Created by drew on 11/24/17.
 */

public class FineIMU {
    public enum Mode {
        ON_PAD,
        OFF_PAD
    }
    private BNO055IMU imu;
    private Orientation angles;
    private ElapsedTime runTime;
    private LinearOpMode opMode;
    private String name;
    private double prevError = 0;
    private double integral = 0;
    private double zeroPos = 0;

    private static final double kP_OFF_PAD = 0.041;//0.031
    private static final double kI_OFF_PAD = 0.00;//0.00015
    private static final double kD_OFF_PAD = 0.1;//0.21


    private static final double kP_ON_PAD = 0.02;
    private static final double kI_ON_PAD = 0;
    private static final double kD_ON_PAD = 0.24;

    private Mode mode = Mode.ON_PAD;
    public FineIMU(LinearOpMode opMode, String name) {
        runTime = new ElapsedTime();
        this.opMode = opMode;
        this.name = name;
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = opMode.hardwareMap.get(BNO055IMU.class, name);
        imu.initialize(parameters);
        imu.startAccelerationIntegration(new Position(), new Velocity(), 100);
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }
    private double kP() {
        if (mode == Mode.ON_PAD)
            return kP_ON_PAD;
        else
            return kP_OFF_PAD;
    }
    private double kI() {
        if (mode == Mode.ON_PAD)
            return kI_ON_PAD;
        else
            return kI_OFF_PAD;
    }
    private double kD() {
        if (mode == Mode.ON_PAD)
            return kD_ON_PAD;
        else
            return kD_OFF_PAD;
    }

    public double getHeading() {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return angles.firstAngle - zeroPos;
    }
    public double getRate() {
        return imu.getAngularVelocity().zRotationRate;
    }
    public double[] getLevelness() {
        double[] levels = new double[2];
        levels[0] = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).secondAngle;
        levels[1] = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).thirdAngle;
        return levels;
    }
    public Acceleration getAcceleration() {
        return imu.getAcceleration();
    }
    public Velocity getVelocity() {
        return imu.getVelocity();
    }
    public Position getPosition() {
        return imu.getPosition();
    }

    public void resetPID() {
        prevError = 0;
        integral = 0;
    }

    public void resetAngle() {
        zeroPos = getHeading();
    }

    /*public double correction(double angle) {
        if (prevError == 0) runTime.reset();

        double error = angle - getHeading();
        double deravitive = (error - prevError)/runTime.milliseconds();
        integral = integral + error*runTime.milliseconds();

        double correction = error * kP() + integral* kI() + deravitive * kD();

        prevError = error;
        return correction;
    }*/
    public double align (double angle) {
        if (prevError == 0) runTime.reset();
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        final double heading = getHeading();
        double error = angle-heading;
        integral = integral + error * kP() *runTime.milliseconds();
        double deravitive = (error - prevError)/runTime.milliseconds();
        double correction = error * kP() + deravitive * kD() + integral * kI();
        //correction = Range.clip(correction, 0, 0.3);
        prevError = error;
        runTime.reset();
        return correction;
    }
    public double alignTune (double angle, double p, double i, double d) {
        if (prevError == 0) runTime.reset();
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        final double heading = getHeading();
        double error = angle-heading;
        integral = integral + error * p * runTime.milliseconds();
        double deravitive = (error - prevError)/runTime.milliseconds();
        double correction = error * p + deravitive * d + integral * i;
        //correction = Range.clip(correction, 0, 0.3);
        prevError = error;
        runTime.reset();
        return correction;
    }

    public void addTelemetry() {
        opMode.telemetry.addData(name + " heading", getHeading());
        opMode.telemetry.addData(name + " heading", imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle);
        opMode.telemetry.addData(name + " rate", imu.getAngularVelocity().zRotationRate);
    }

}
