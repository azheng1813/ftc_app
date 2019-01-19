package Autonomus_Parts;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous


public class Sampling extends LinearOpMode{
    private Gyroscope imu;
    private DcMotor frontRight;
    private DcMotor frontLeft;
    private DcMotor backRight;
    private DcMotor backLeft;
    private Servo totemdrop;
    private DcMotor lift;
    private Blinker expansion_Hub_3;
    private ElapsedTime runtime = new ElapsedTime();
    
    static final double     COUNTS_PER_MOTOR_REV    = 280 ;
    static final double     DRIVE_GEAR_REDUCTION    = 2.5 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = ((COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.14159265));
    static final double     VERTICAL_DISTANCE       = 0.3125;
    static final double     LIFT_DISTANCE           = 896;//(COUNTS_PER_MOTOR_REV / VERTICAL_DISTANCE);

    static final double DRIVE_SPEED = 1;
    static final double TURN_SPEED = 0.5;
    
    @Override
    public void runOpMode() {
        imu = hardwareMap.get(Gyroscope.class, "imu");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        lift = hardwareMap.get(DcMotor.class, "lift");
        totemdrop = hardwareMap.get(Servo.class, "totemDrop" );
        expansion_Hub_3 = hardwareMap.get(Blinker.class, "Expansion Hub 3");
        
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        lift.setDirection(DcMotor.Direction.REVERSE);
        
        telemetry.addData("Status", "Resetting Encoders");
        telemetry.update();
        
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        
        
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        //telemetry.addData("Path0", "Starting at %7d : %7d", )
        
        telemetry.addData("Status", "We bouta get it started");
        telemetry.update();
        
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        
        encoderDrive(DRIVE_SPEED, 14.5, 14.5, 14.5, 14.5, 1);
        int x = 1;
        
        if(x == 1){//left
            encoderDrive(DRIVE_SPEED, 19, -19, -19, 19, 1); //left straif
            encoderDrive(DRIVE_SPEED, 20, 20, 20, 20, 1); //forward to block for left and right
            encoderDrive(DRIVE_SPEED, -16, -16, -16, -16, 1); //back up 
            encoderDrive(DRIVE_SPEED, 21, -21, 21, -21, 1); //90 degree left turn for left and right
        }
        if else(x == 2){//center
            encoderDrive(DRIVE_SPEED, 20, 20, 20, 20, 1); 
            encoderDrive(DRIVE_SPEED, )
        }
    }
    
    public void encoderDrive(double speed, double FrontRightInches, double FrontLeftInches, double BackRightInches, double BackLeftInches, double timeout){
        
        int newTargetFrontRight = 0;
        int newTargetFrontLeft = 0;
        int newTargetBackRight=0;
        int newTargetBackLeft=0;
        
            if(opModeIsActive()){
                
                newTargetFrontRight = frontRight.getCurrentPosition() + (int)(FrontRightInches * COUNTS_PER_INCH);
                newTargetFrontLeft = frontLeft.getCurrentPosition() + (int)(FrontLeftInches * COUNTS_PER_INCH);
                newTargetBackRight = backRight.getCurrentPosition() + (int)(BackRightInches * COUNTS_PER_INCH);
                newTargetBackLeft = backLeft.getCurrentPosition() + (int)(BackLeftInches * COUNTS_PER_INCH);
                frontRight.setTargetPosition(newTargetFrontRight);
                frontLeft.setTargetPosition(newTargetFrontLeft);
                backRight.setTargetPosition(newTargetBackRight);
                backLeft.setTargetPosition(newTargetBackLeft);
                
                frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                
                runtime.reset();
                frontRight.setPower(Math.abs(speed));
                frontLeft.setPower(Math.abs(speed));
                backRight.setPower(Math.abs(speed));
                backLeft.setPower(Math.abs(speed));
            }

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive() && runtime.seconds() < timeout) {
            telemetry.addData("Path1", "Running to %7d :%7d", newTargetFrontRight, newTargetFrontLeft, newTargetBackRight, newTargetBackLeft);
            telemetry.addData("Path2","Running front at %7d :%7d", frontRight.getCurrentPosition(), frontLeft.getCurrentPosition());
            telemetry.addData("Path2","Running back at %7d :%7d", backRight.getCurrentPosition(), backLeft.getCurrentPosition());
            //telemetry.addData("Path3","Running at %7d :%7d", frontRight.getPower(), frontLeft.getPower());
            telemetry.update();
            }
            
                frontRight.setPower(0);
                frontLeft.setPower(0);
                backRight.setPower(0);
                backLeft.setPower(0);
                lift.setPower(0);
                
                frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    
}
