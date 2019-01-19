package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "MainTeleOp (Blocks to Java)", group = "")
@Disabled
public class MainTeleOp extends LinearOpMode {

  private DcMotor frontRight;
  private DcMotor backRight;
  private DcMotor frontLeft;
  private DcMotor backLeft;
  private CRServo flipper;

  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  @Override
  public void runOpMode() {
    float strafeRight;
    float strafeLeft;

    frontRight = hardwareMap.dcMotor.get("frontRight");
    backRight = hardwareMap.dcMotor.get("backRight");
    frontLeft = hardwareMap.dcMotor.get("frontLeft");
    backLeft = hardwareMap.dcMotor.get("backLeft");
    flipper = hardwareMap.crservo.get("flipper");

    // Put initialization blocks here.
    frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
    backRight.setDirection(DcMotorSimple.Direction.REVERSE);
    waitForStart();
    while (opModeIsActive()) {
      // Power to drive
      frontRight.setPower(gamepad1.right_stick_y * 0.5);
      frontRight.setPower(gamepad1.right_stick_y * 0.75);
      backRight.setPower(gamepad1.right_stick_y * 0.75);
      frontLeft.setPower(gamepad1.left_stick_y * 0.75);
      backLeft.setPower(gamepad1.left_stick_y * 0.75);
      flipper.setPower(gamepad2.left_stick_y);
      // Strafing code
      strafeRight = gamepad1.right_trigger;
      strafeLeft = gamepad1.left_trigger;
      if (strafeRight != 0) {
        frontLeft.setPower(-(strafeRight * 0.8));
        frontRight.setPower(strafeRight * 0.8);
        backLeft.setPower(strafeRight * 0.8);
        backRight.setPower(-(strafeRight * 0.8));
      } else if (strafeLeft != 0) {
        frontLeft.setPower(strafeLeft * 0.8);
        frontRight.setPower(-(strafeLeft * 0.8));
        backLeft.setPower(-(strafeLeft * 0.8));
        backRight.setPower(strafeLeft * 0.8);
      }
      // Creep
      if (gamepad1.dpad_up) {
        frontRight.setPower(-0.4);
        backRight.setPower(-0.4);
        frontLeft.setPower(-0.4);
        backLeft.setPower(-0.4);
      } else if (gamepad1.dpad_down) {
        frontRight.setPower(0.4);
        backRight.setPower(0.4);
        frontLeft.setPower(0.4);
        backLeft.setPower(0.4);
      } else if (gamepad1.dpad_right) {
        frontRight.setPower(-0.4);
        backRight.setPower(-0.4);
      } else if (gamepad1.dpad_left) {
        frontLeft.setPower(-0.4);
        backLeft.setPower(-0.4);
      }
      if (gamepad1.x) {
        frontLeft.setPower(1);
        backLeft.setPower(1);
        frontRight.setPower(1);
        backRight.setPower(1);
        sleep(200);
        frontRight.setPower(1);
        backRight.setPower(1);
        frontLeft.setPower(-1);
        backLeft.setPower(-1);
        sleep(700);
      }
      telemetry.update();
    }
  }
}
