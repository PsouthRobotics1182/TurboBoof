package lib.neo.bot;

import lib.neo.misc.NeoRunnables;
import lib.neo.drive.core.NeoDrive;
import lib.neo.motor.NeoMotor;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;

public class NeoBot {
    LinearOpMode opMode;
    NeoDrive driveTrain;
    public NeoBot(LinearOpMode opMode) {
        this.opMode = opMode;
    }
}
