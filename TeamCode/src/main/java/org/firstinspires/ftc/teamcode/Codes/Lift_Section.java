/*
Copyright 2018 FIRST Tech Challenge Team 10686

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package Autonomus_Parts;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Remove a @Disabled the on the next line or two (if present) to add this opmode to the Driver Station OpMode list,
 * or add a @Disabled annotation to prevent this OpMode from being added to the Driver Station
 */
@Autonomous

public class Lift_Section extends LinearOpMode {
    private Gyroscope imu;
    private DcMotor frontRight;
    private DcMotor frontLeft;
    private DcMotor backRight;
    private DcMotor backLeft;
    private DcMotor lift;
    private Servo totemdrop;
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
        
        telemetry.addData("Status", "Want a Sprite Cranberry");
        telemetry.update();
        
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        
        
        //encoderDrive(DRIVE_SPEED, 21, -21, 3); 90 Degree Turn counterclockwise
        encoderDrive(1, 0, 0, 0, 0, .01, 6);
        encoderDrive(DRIVE_SPEED, 0, 0, 0, 0, 0, 3);
        encoderDrive(DRIVE_SPEED, -7, 7, 7, -7, 0, .8);
        encoderDrive(DRIVE_SPEED, 8, 8, 8, 8, 0, .75);
        encoderDrive(DRIVE_SPEED, 7, -7, -7, 7, 0, .8);
        encoderDrive(.8, 10, 10, 10, 10, 0, .5);
        telemetry.addData("Path", "Complete");
        telemetry.update();
    }
    
    public void liftDrive(double speed, double liftInches, double timeout){
        
        int newTargetLift = 0;
        
            if(opModeIsActive()){
                
                newTargetLift = lift.getCurrentPosition() + (int)(liftInches * LIFT_DISTANCE);
                lift.setTargetPosition(newTargetLift);
                
                
                lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                
                runtime.reset();
                lift.setPower(Math.abs(speed));
                
            }
    }
    public void encoderDrive(double speed, double FrontRightInches, double FrontLeftInches, double BackRightInches, double BackLeftInches, double liftInches, double timeout){
        
        int newTargetFrontRight = 0;
        int newTargetFrontLeft = 0;
        int newTargetBackRight=0;
        int newTargetBackLeft=0;
        int newTargetLift = 0;
        
            if(opModeIsActive()){
                
                newTargetLift = lift.getCurrentPosition() + (int)(liftInches * LIFT_DISTANCE);
                newTargetFrontRight = frontRight.getCurrentPosition() + (int)(FrontRightInches * COUNTS_PER_INCH);
                newTargetFrontLeft = frontLeft.getCurrentPosition() + (int)(FrontLeftInches * COUNTS_PER_INCH);
                newTargetBackRight = backRight.getCurrentPosition() + (int)(BackRightInches * COUNTS_PER_INCH);
                newTargetBackLeft = backLeft.getCurrentPosition() + (int)(BackLeftInches * COUNTS_PER_INCH);
                lift.setTargetPosition(newTargetLift);
                frontRight.setTargetPosition(newTargetFrontRight);
                frontLeft.setTargetPosition(newTargetFrontLeft);
                backRight.setTargetPosition(newTargetBackRight);
                backLeft.setTargetPosition(newTargetBackLeft);
                
                lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                
                runtime.reset();
                frontRight.setPower(Math.abs(speed));
                frontLeft.setPower(Math.abs(speed));
                backRight.setPower(Math.abs(speed));
                backLeft.setPower(Math.abs(speed));
                lift.setPower(Math.abs(speed));
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
