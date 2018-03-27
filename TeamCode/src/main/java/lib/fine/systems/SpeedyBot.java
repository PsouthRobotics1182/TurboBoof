package lib.fine.systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import lib.fine.core.FineIMU;

/**
 * Created by drew on 11/24/17.
 */

public class SpeedyBot {
    public FineSlide drive;
    public Flipper lift;
    public SuckyBois suckyBois;

    private LinearOpMode opMode;

    //runmode is whether to use encoders or not
    public SpeedyBot(LinearOpMode opMode, DcMotor.RunMode mode) {
        drive = new FineSlide(opMode, mode);
        lift = new Flipper(opMode);
        suckyBois = new SuckyBois(opMode);

        drive.setMode(FineIMU.Mode.OFF_PAD);

        this.opMode = opMode;
    }

    private void resetTimer() {
    }

    public void sleep(int ms) {
        ElapsedTime runTime = new ElapsedTime();
        runTime.reset();

        while (runTime.milliseconds() < ms && opMode.opModeIsActive()) {
            opMode.telemetry.addData("Time", runTime.milliseconds() + "/" + ms);
            opMode.telemetry.update();
            opMode.idle();
        }

    }
    public void addTelemetry() {
        drive.addTelemetry();
        lift.addTelemetry();
    }
}
