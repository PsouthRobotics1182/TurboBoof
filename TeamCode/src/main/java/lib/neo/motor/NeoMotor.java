package lib.neo.motor;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;

/**
 * Created by Robotics on 4/9/2018.
 */

public class NeoMotor {
    DcMotor motor;
    public NeoMotor(LinearOpMode opMode, String name) {
        this.motor = opMode.hardwareMap.get(DcMotor.class, name);
    }
}
