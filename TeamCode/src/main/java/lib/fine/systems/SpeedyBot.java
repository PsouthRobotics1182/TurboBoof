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
    public SuckyBois suckyBois;
    public Flipper flipper;

    private LinearOpMode opMode;
    private ElapsedTime runTime;


    public SpeedyBot(LinearOpMode opMode, DcMotor.RunMode mode) {
        drive = new FineSlide(opMode, mode);
        suckyBois = new SuckyBois(opMode);
        flipper = new Flipper(opMode);
        runTime = new ElapsedTime();

        drive.setMode(FineIMU.Mode.OFF_PAD);

        this.opMode = opMode;
    }


    private void resetTimer() {
        runTime.reset();
    }

    public void sleep(int ms) {
        resetTimer();
        while (runTime.milliseconds() < ms && opMode.opModeIsActive()) {
            opMode.telemetry.addData("Time", runTime.milliseconds() + "/" + ms);
            opMode.telemetry.update();
            opMode.idle();
        }
    }
    public void addTelemetry() {
        drive.addTelemetry();
    }
}
