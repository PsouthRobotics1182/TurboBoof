package lib.fine.core;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by drew on 3/22/18.
 */

public class LimitSwitch {

    private LinearOpMode opMode;

    private DigitalChannel limit;

    public LimitSwitch(String name, LinearOpMode opMode) {
        limit = opMode.hardwareMap.get(DigitalChannel.class, name);
        limit.setMode(DigitalChannel.Mode.INPUT);
        this.opMode = opMode;
    }
    public boolean isPressed() {
        return !limit.getState(); //true is not pressed

    }
}

