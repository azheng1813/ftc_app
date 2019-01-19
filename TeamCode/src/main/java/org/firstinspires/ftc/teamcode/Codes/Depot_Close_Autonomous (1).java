package FINAL;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;


@Autonomous(name = "Depot_Close_Autonomous", group = "FINAL")
@Disabled

public class Depot_Close_Autonomous extends LinearOpMode {
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    
    public int MineralPosition = 0; // 2 = right, 1 = center, 0 = left

    private static final String VUFORIA_KEY = "AUgSzTz/////AAABmYpKZJuZpUD6ttGGKu0ipYlrGLvaQbdeuoKZs4uqiTn6pif5YRwExKq+5/vAcZleSU6ZoJ6goMB0+7uLjzPWWNfMQEMjWefTIk4lIEF9+A9mL/9CPPPjDLM1keWuBet3gSdPge7T3TRJYj8KrCbVJrNXLRZSd+HVlq9ot8blPxpt35YGEOnyjuRAmVKvRmRkyiZP6qq8/pciPtNd4jODPA0G37hbYyWzLWbbh+w7Q1KJD7PNt+Ec1ZG6+dKJ+GQJxPs9vVGgMolZZ/lVPlmIyRnEWlH0b8hQWL1UrkpT8JsV90BKOwRms/xz3gSPihg6Cb56kN/xXQjvlPPzB/QtAW0MtXHKBoJRt1G9Gj6vFZfR";

    private VuforiaLocalizer vuforia;

    private TFObjectDetector tfod;

    @Override
    public void runOpMode() {
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start tracking");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            /** Activate Tensor Flow Object Detection. */

            if (tfod != null) {
                tfod.activate();
            }
            while (opModeIsActive()) {
                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                      telemetry.addData("# Object Detected", updatedRecognitions.size());
                      if (updatedRecognitions.size() == 3) {
                        int goldMineralX = -1;
                        int silverMineral1X = -1;
                        int silverMineral2X = -1;
                        for (Recognition recognition : updatedRecognitions) {
                          if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                            goldMineralX = (int) recognition.getLeft();
                          } else if (silverMineral1X == -1) {
                            silverMineral1X = (int) recognition.getLeft();
                          } else {
                            silverMineral2X = (int) recognition.getLeft();
                          }
                        }
                        if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                          if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                            telemetry.addData("Gold Mineral Position", "Left");
                            MineralPosition = 0;
                            }
                            else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                            telemetry.addData("Gold Mineral Position", "Right");
                            MineralPosition = 2;
                            }
                          else {
                            telemetry.addData("Gold Mineral Position", "Center");
                            MineralPosition = 1;
                          }
                        }
                      }
                      telemetry.update();
                    }
                }
            }
        }

        if (tfod != null) {
            tfod.shutdown();
        }
    }

    /**
     * Initialize the Vuforia localization engine.
     */


    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */


        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    /**
     * Initialize the Tensor Flow Object Detection engine.
     */


    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
            "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }
    
    public int getValue(){
        return MineralPosition;
    }
}


class Drive2 extends LinearOpMode {
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
    
    Depot_Close_Autonomous tensorFlowMineralPosition = new Depot_Close_Autonomous();
    int MP = tensorFlowMineralPosition.getValue();
    // 2 = right, 1 = center, 0 = left
    
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
        //before tensorflow
        
        if(MP == 2){
            telemetry.addData("Right Position","It works!!");
            //right 
            encoderDrive(DRIVE_SPEED, -21, 21, 21, -21, 1); //right straif if gold is right
            encoderDrive(DRIVE_SPEED, 20, 20, 20, 20, 1); //forward to block for left and right
            encoderDrive(DRIVE_SPEED, -16, -16, -16, -16, 1); //back up 
            encoderDrive(DRIVE_SPEED, 21, -21, 21, -21, 1); //90 degree left turn for left and right
            encoderDrive(DRIVE_SPEED, 40, 40, 40, 40, 1); //forward so won't hit minerals (gold right)
            encoderDrive(DRIVE_SPEED, -10.5, 10.5, -10.5, 10.5, 1); //45ish degree right turn
            encoderDrive(DRIVE_SPEED, 15, 15, 15, 15, 1); //forward to wall for left and right
            encoderDrive(DRIVE_SPEED, -22, 22, -22, 22, 1); //pivot 90 degree right turn, face depot square (for left and right)
            encoderDrive(DRIVE_SPEED, 49, 49, 49, 49, 2); //forward and push mineral to square (for left and right)
            encoderDrive(DRIVE_SPEED, -4, -4, -4, -4, 1); //backup out of square (for left and right)
            encoderDrive(DRIVE_SPEED, -20.5, 20.5, -20.5, 20.5, 1); //90 degree right turn (for left and right)
            totemdrop.setPosition(0);
            encoderDrive(DRIVE_SPEED, 0, 0, 0, 0, 1); //wait
            totemdrop.setPosition(.75); //drop totem
            encoderDrive(DRIVE_SPEED, 15, 15, 15, 15, 1); //forward from square (for left and right)
            encoderDrive(DRIVE_SPEED, 16, -16, -16, 16, 1); //straif left to wall (for left and right)
        }
        else if (MP == 1){
            telemetry.addData("center Position","It works!!");
            //center
            encoderDrive(DRIVE_SPEED, 35, 35, 35, 35, 2); //forward to block for center
            encoderDrive(DRIVE_SPEED, -6, -6, -6, -6, 1); //back up from square
            encoderDrive(TURN_SPEED, -10.5, 10.5, -10.5, 10.5, 1); //45ish degree right turn to be perpendicular to wall
            encoderDrive(DRIVE_SPEED, 16, 16, 16, 16, 1); //forward to wall (center)
            totemdrop.setPosition(0);
            encoderDrive(DRIVE_SPEED, 0, 0, 0, 0, 1); //wait
            totemdrop.setPosition(.75); //drop totem
            encoderDrive(DRIVE_SPEED, -21, 21, -21, 21, 1); //90 degree right turn (center)
            encoderDrive(DRIVE_SPEED, 4, -4, -4, 4, 1); //left straif 
        }
        else{ //left position code
            telemetry.addData("Left Position","It works!!");
            encoderDrive(DRIVE_SPEED, 17, -17, -17, 17, 1); //left straif if gold is left
            encoderDrive(DRIVE_SPEED, 20, 20, 20, 20, 1); //forward to block for left and right
            encoderDrive(DRIVE_SPEED, -16, -16, -16, -16, 1); //back up 
            encoderDrive(DRIVE_SPEED, 21, -21, 21, -21, 1); //90 degree left turn for left and right
            encoderDrive(DRIVE_SPEED, 14, 14, 14, 14, 1); //forward so won't hit mineral (gold left)
            encoderDrive(DRIVE_SPEED, -10.5, 10.5, -10.5, 10.5, 1); //45ish degree right turn
            encoderDrive(DRIVE_SPEED, 14.5, 14.5, 14.5, 14.5, 1); //forward to wall for left and right
            encoderDrive(DRIVE_SPEED, -22, 22, -22, 22, 1); //pivot 90 degree right turn, face depot square (for left and right)
            encoderDrive(DRIVE_SPEED, 45, 45, 45, 45, 2); //forward and push mineral to square (for left and right)
            encoderDrive(DRIVE_SPEED, -4, -4, -4, -4, 1); //backup out of square (for left and right)
            encoderDrive(DRIVE_SPEED, -20.5, 20.5, -20.5, 20.5, 1); //90 degree right turn (for left and right)
            totemdrop.setPosition(0);
            encoderDrive(DRIVE_SPEED, 0, 0, 0, 0, 1); //wait
            totemdrop.setPosition(.75); //drop totem
            encoderDrive(DRIVE_SPEED, 15, 15, 15, 15, 1); //forward from square (for left and right)
            encoderDrive(DRIVE_SPEED, 16, -16, -16, 16, 1); //straif left to wall (for left and right)
        
        }
        //after tensorflow
        encoderDrive(DRIVE_SPEED, 64, 64, 64, 64, 3); //forward to crater
        encoderDrive(DRIVE_SPEED, 3, -3, -3, 3, 1); //left straif to hug wall
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

