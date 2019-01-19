/**package Java;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
@Override
public class AutonomousXTensorFlow2 extends LinearOpMode {
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
    
    static final double DRIVE_SPEED = 1;
    static final double TURN_SPEED = 0.5;
    
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    
    private static final String VUFORIA_KEY = "AUgSzTz/////AAABmYpKZJuZpUD6ttGGKu0ipYlrGLvaQbdeuoKZs4uqiTn6pif5YRwExKq+5/vAcZleSU6ZoJ6goMB0+7uLjzPWWNfMQEMjWefTIk4lIEF9+A9mL/9CPPPjDLM1keWuBet3gSdPge7T3TRJYj8KrCbVJrNXLRZSd+HVlq9ot8blPxpt35YGEOnyjuRAmVKvRmRkyiZP6qq8/pciPtNd4jODPA0G37hbYyWzLWbbh+w7Q1KJD7PNt+Ec1ZG6+dKJ+GQJxPs9vVGgMolZZ/lVPlmIyRnEWlH0b8hQWL1UrkpT8JsV90BKOwRms/xz3gSPihg6Cb56kN/xXQjvlPPzB/QtAW0MtXHKBoJRt1G9Gj6vFZfR";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    
    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
            "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }
    
    public void liftDrive(double speed, double liftInches, double timeout){
        int newTargetLift = 0;
            if(opModeIsActive()){
                newTargetLift = lift.getCurrentPosition() + (int)(liftInches);
                lift.setTargetPosition(newTargetLift);
                lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                runtime.reset();
                lift.setPower(Math.abs(speed));
            }
    }
    
}**/
