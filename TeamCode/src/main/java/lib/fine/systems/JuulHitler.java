package lib.fine.systems;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by drew on 11/24/17.
 */

public class JuulHitler {
    private Servo juul;
    private Servo swingyBoi;
    private ColorSensor colorSensor;
    private LinearOpMode opMode;

    private static final int COLOR_CERTAINITY = 2;
    private static final double UP_POSTION = 1;
    private static final double DOWN_POSITION = 0.46;
    private static final double LEFT_POSITION = 0.3;
    private static final double RIGHT_POSITION = 0.7;
    private static final double MIDDL_POSITION = 0.57;

    public JuulHitler(LinearOpMode opMode) {
        this.opMode = opMode;

        colorSensor = opMode.hardwareMap.get(ColorSensor.class, "color");
        juul = opMode.hardwareMap.get(Servo.class, "juul");
        swingyBoi = opMode.hardwareMap.get(Servo.class, "side");
        colorSensor.enableLed(true);
        home();
    }

    public int getColor() {
        int colorDiff = colorSensor.blue() - colorSensor.red();
        return (colorDiff > COLOR_CERTAINITY) ? Color.BLUE
             : (colorDiff < -COLOR_CERTAINITY) ? Color.RED
             : Color.BLACK;
    }

    public void down() {
        double pos = juul.getPosition();
        while (juul.getPosition() > DOWN_POSITION && opMode.opModeIsActive()) {
            juul.setPosition(pos);
            opMode.sleep(60);
            pos -= 0.02;
        }
    }

    public void up(double upness) {
        double pos = juul.getPosition();
        while (juul.getPosition() < UP_POSTION * upness && opMode.opModeIsActive()) {
            juul.setPosition(pos);
            opMode.sleep(30);
            pos += 0.05;
        }
    }

    public void right() {
        double pos = swingyBoi.getPosition();
        while (swingyBoi.getPosition() < RIGHT_POSITION && opMode.opModeIsActive()) {
            swingyBoi.setPosition(pos);
            opMode.sleep(40);
            pos += 0.02;
        }
    }

    public void left() {
        double pos = swingyBoi.getPosition();
        while (swingyBoi.getPosition() > LEFT_POSITION && opMode.opModeIsActive()) {
            swingyBoi.setPosition(pos);
            opMode.sleep(40);
            pos -= 0.02;
        }
    }

    public void middle() {
        double pos = swingyBoi.getPosition();
        while (Math.abs(swingyBoi.getPosition() - MIDDL_POSITION) > 0.04 && opMode.opModeIsActive()) {
            if (swingyBoi.getPosition() > MIDDL_POSITION) {
                swingyBoi.setPosition(pos);
                opMode.sleep(60);
                pos -= 0.01;
            } else if (swingyBoi.getPosition() < MIDDL_POSITION) {
                swingyBoi.setPosition(pos);
                opMode.sleep(60);
                pos += 0.01;
            }
        }
        //swingyBoi.setPosition(MIDDL_POSITION);
    }

    public boolean uping(double upness) {
        juul.setPosition(juul.getPosition() + 0.05);
        return juul.getPosition() < UP_POSTION * upness && opMode.opModeIsActive();
    }
    public boolean downing() {
        juul.setPosition(juul.getPosition() - 0.02);
        return juul.getPosition() > DOWN_POSITION && opMode.opModeIsActive();
    }

    public boolean swingingMiddle() {
        if (swingyBoi.getPosition() > MIDDL_POSITION) {
            swingyBoi.setPosition(swingyBoi.getPosition() - 0.01);
        } else if (swingyBoi.getPosition() < MIDDL_POSITION) {
            swingyBoi.setPosition(swingyBoi.getPosition() + 0.01);
        }
        return Math.abs(swingyBoi.getPosition() - MIDDL_POSITION) > 0.04 && opMode.opModeIsActive();
    }

    public boolean swingingLeft() {
        swingyBoi.setPosition(swingyBoi.getPosition() - 0.02);
        return swingyBoi.getPosition() > LEFT_POSITION && opMode.opModeIsActive();
    }

    public boolean swingingRight() {
        swingyBoi.setPosition(swingyBoi.getPosition() + 0.02);
        return swingyBoi.getPosition() > RIGHT_POSITION && opMode.opModeIsActive();
    }
    public void home() {
        swingyBoi.setPosition(MIDDL_POSITION);
        up(1);
        right();
    }

    public void hitRed() {

        forSureMiddle();
        //down();
        juul.setPosition((DOWN_POSITION + UP_POSTION)/2);
        opMode.sleep(700);
        juul.setPosition(DOWN_POSITION);
        opMode.sleep(2000);

        if (getColor() == Color.RED)
            right();
        else if (getColor() == Color.BLUE)
            left();
        else {
            opMode.telemetry.addData("Not Found", null);
            opMode.telemetry.update();
            opMode.sleep(5000);
        }
        up(0.3);
        forSureMiddle();
        up(1);


    }

    public void hitBlue() {

        forSureMiddle();
        //down();
        juul.setPosition((DOWN_POSITION + UP_POSTION)/2);
        opMode.sleep(700);
        juul.setPosition(DOWN_POSITION);
        opMode.sleep(2000);

        if (getColor() == Color.BLUE)
            right();
        else if (getColor() == Color.RED)
            left();
        else {
            opMode.telemetry.addData("Not Found", null);
            opMode.telemetry.update();
            opMode.sleep(5000);
        }
        up(0.3);
        forSureMiddle();
        up(1);

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
