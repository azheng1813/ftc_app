package Java;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
//import com.qualcomm.robotcore.hardware.DcMotorSimple$Direction;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp

public class Main_TeleOp extends LinearOpMode {
    private Gyroscope imu;
    private Gyroscope imu_1;
    private DcMotor frontRight;
    private DcMotor frontLeft;
    private DcMotor backRight;
    private DcMotor backLeft;
    private DcMotor scoopArm;
    private DcMotor reachArm;
    private DcMotor flipper;
    private DcMotor lift;
    private Blinker expansion_Hub_2;
    private Blinker expansion_Hub_3;
    private Servo totemDrop;
    
    @Override
    public void runOpMode() {
        imu = hardwareMap.get(Gyroscope.class, "imu");
        imu_1 = hardwareMap.get(Gyroscope.class, "imu 1");
        //flipper = hardwareMap.get(CRServo.class, "flipper");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        scoopArm = hardwareMap.get(DcMotor.class, "scoopArm");
        reachArm = hardwareMap.get(DcMotor.class, "reachArm");
        lift = hardwareMap.get(DcMotor.class, "lift");
        totemDrop = hardwareMap.get(Servo.class, "totemDrop");
        
        expansion_Hub_2 = hardwareMap.get(Blinker.class, "Expansion Hub 2");
        expansion_Hub_3 = hardwareMap.get(Blinker.class, "Expansion Hub 3");
        
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        lift.setDirection(DcMotor.Direction.REVERSE);
        
        
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();
        while (opModeIsActive()) {
            float left_power = -gamepad1.left_stick_y;
            float right_power = -gamepad1.right_stick_y;
            float right_straif = gamepad1.right_trigger;
            float left_straif = gamepad1.left_trigger;
            frontRight.setPower(right_power);
            frontLeft.setPower(left_power);
            backRight.setPower(right_power);
            backLeft.setPower(left_power);
            if(left_straif > 0){
            frontRight.setPower(left_straif);
            frontLeft.setPower(-left_straif);
            backRight.setPower(-left_straif);
            backLeft.setPower(left_straif);
            }
            if(right_straif > 0){
            frontRight.setPower(-right_straif);
            frontLeft.setPower(right_straif);
            backRight.setPower(right_straif);
            backLeft.setPower(-right_straif);
            }
            if(gamepad1.dpad_up){
                frontRight.setPower(.4);
                frontLeft.setPower(.4);
                backRight.setPower(.4);
                backLeft.setPower(.4);
            }
            if(gamepad1.dpad_down){
                frontRight.setPower(-.4);
                frontLeft.setPower(-.4);
                backRight.setPower(-.4);
                backLeft.setPower(-.4);
            }
            if(gamepad1.dpad_right){
                frontRight.setPower(.5);
                backRight.setPower(.5);
            }
            if(gamepad1.dpad_left){
                frontLeft.setPower(.5);
                backLeft.setPower(.5);
            }
            lift.setPower(gamepad2.left_stick_y);
            if(gamepad2.dpad_up){
                totemDrop.setPosition(.9);
            }
        }
    }
}
