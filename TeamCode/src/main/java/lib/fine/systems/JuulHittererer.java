package lib.fine.systems;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by drew on 11/24/17.
 */

public class JuulHittererer {
    public Servo juul;
    public Servo swingyBoi;
    public Servo pivot;
    private ColorSensor colorSensor;
    private LinearOpMode opMode;

    private static final int COLOR_CERTAINITY = 2;
    private static final double UP_POSTION = 1;
    private static final double DOWN_POSITION = 0.46;
    private static final double LEFT_POSITION = 0.3;
    private static final double RIGHT_POSITION = 0.7;
    private static final double MIDDL_POSITION = 0.57;

    public JuulHittererer(LinearOpMode opMode) {
        this.opMode = opMode;
        colorSensor = opMode.hardwareMap.get(ColorSensor.class, "color");
        juul = opMode.hardwareMap.get(Servo.class, "juul");
        swingyBoi = opMode.hardwareMap.get(Servo.class, "swing");
        pivot = opMode.hardwareMap.get(Servo.class, "pivot");
        colorSensor.enableLed(true);
        home();
    }

    public int getColor() {
        int colorDiff = colorSensor.blue() - colorSensor.red();
        return (colorDiff > COLOR_CERTAINITY) ? Color.BLUE
             : (colorDiff < -COLOR_CERTAINITY) ? Color.RED
             : Color.BLACK;
    }

    public void left() {
        swingyBoi.setPosition(0);
    }
    public void right() {
        swingyBoi.setPosition(1);
    }
    public void middle() {
        swingyBoi.setPosition(0.53);
    }
    public void pivots(double pivotness) {
        pivot.setPosition(pivotness);
    }
    public void down(double downess) {
        juul.setPosition(1-downess);
    }
    public void up() {
        juul.setPosition(1);
    }
    public void home() {
        up();
        right();
        pivots(0);
    }

    private void lower() {
        middle();
        down(0.6);
        opMode.sleep(500);
        pivots(0.25);
        opMode.sleep(500);
        down(0.9);
        opMode.sleep(500);
        pivots(0.6);
        opMode.sleep(500);
        down(1);
        pivots(0.68);
    }

    public void hitRed() {
        lower();

        opMode.sleep(1000);

        switch(getColor()) {
            case Color.RED:
                right();
                break;
            case Color.BLUE:
                left();
                break;
            default:
                opMode.telemetry.addData("Not Found", null);
                opMode.telemetry.update();
                opMode.sleep(1000);
                break;
        }

        opMode.sleep(1000);
        home();
    }

    public void hitBlue() {
        lower();

        opMode.sleep(1000);

        switch(getColor()) {
            case Color.BLUE:
                right();
                break;
            case Color.RED:
                left();
                break;
            default:
                opMode.telemetry.addData("Not Found", null);
                opMode.telemetry.update();
                opMode.sleep(1000);
                break;
        }

        opMode.sleep(1000);
        home();
    }


    public void forSureMiddle() {
        swingyBoi.setPosition(MIDDL_POSITION);
    }

    public void addTelemetry() {
        opMode.telemetry.addData("juul servo", juul.getPosition());
        opMode.telemetry.addData("swing servo", swingyBoi.getPosition());
        opMode.telemetry.addData("color blue", colorSensor.blue());
        opMode.telemetry.addData("color red", colorSensor.red());
        opMode.telemetry.addData("color green", colorSensor.green());
    }
}
