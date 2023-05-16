package frc.robot.subsystems;

import com.studica.frc.TitanQuad;
import com.studica.frc.TitanQuadEncoder;

import frc.robot.C;

public class MotorSystem {
    private TitanQuad leftMotor;
    private TitanQuad rightMotor;
    private TitanQuad backMotor;
    private TitanQuadEncoder leftEncoder;
    private TitanQuadEncoder rightEncoder;
    private TitanQuadEncoder backEncoder;

    public MotorSystem(int leftMotorID, int rightMotorID, int backMotorID) {
        this.leftMotor = new TitanQuad(C.TITAN_ID, leftMotorID);
        this.rightMotor = new TitanQuad(C.TITAN_ID, rightMotorID);
        this.backMotor = new TitanQuad(C.TITAN_ID, backMotorID);
        this.leftEncoder = new TitanQuadEncoder(leftMotor, leftMotorID, 1);
        this.rightEncoder = new TitanQuadEncoder(rightMotor, rightMotorID, 1);
        this.backEncoder = new TitanQuadEncoder(backMotor, backMotorID, 1);
    }

    public void setMotorSpeeds(double leftSpeed, double rightSpeed, double backSpeed) {
        leftMotor.set(leftSpeed);
        rightMotor.set(rightSpeed);
        backMotor.set(backSpeed);
    }

    public void setLeftMotorSpeed(double speed) {
        leftMotor.set(speed);
    }

    public void setRightMotorSpeed(double speed) {
        rightMotor.set(speed);
    }

    public void setBackMotorSpeed(double speed) {
        backMotor.set(speed);
    }

    public void holonomicDriveOLD(double x, double y, double z) {
        double rightSpeed = ((((x / 3) - (y / Math.sqrt(3))) * Math.sqrt(3)) + z);
        double leftSpeed = ((((x / 3) + (y / Math.sqrt(3))) * Math.sqrt(3)) + z);
        double backSpeed = ((-x) + z);

        double max = Math.abs(rightSpeed);
        if (Math.abs(leftSpeed) > max)
            max = Math.abs(leftSpeed);
        if (Math.abs(backSpeed) > max)
            max = Math.abs(backSpeed);

        if (max > 1) {
            rightSpeed /= max;
            leftSpeed /= max;
            backSpeed /= max;
        }

        leftMotor.set(leftSpeed);
        rightMotor.set(rightSpeed);
        backMotor.set(backSpeed);
    }

    public void holonomicDrive(double x, double y, double z) {
        double rightSpeed = x * 0.5 - Math.sqrt(3) / 2 * y + z;
        double leftSpeed = x * 0.5 + Math.sqrt(3) / 2 * y + z;
        double backSpeed = -x + z;

        double max = Math.max(Math.max(Math.abs(rightSpeed), Math.abs(leftSpeed)), Math.abs(backSpeed));
        if (max > 1) {
            rightSpeed /= max;
            leftSpeed /= max;
            backSpeed /= max;
        }
        leftMotor.set(leftSpeed);
        rightMotor.set(rightSpeed);
        backMotor.set(backSpeed);
    }

    public double getLeftEncoderDistance() {
        return leftEncoder.getRaw();
    }

    public double getRightEncoderDistance() {
        return rightEncoder.getRaw() * -1;
    }

    public double getBackEncoderDistance() {
        return backEncoder.getRaw();
    }

    public double getAverageForwardEncoderDistance() {
        return (getLeftEncoderDistance() + getRightEncoderDistance()) * 0.5;
    }

    public void resetLeftEncoder() {
        leftEncoder.reset();
    }

    public void resetRightEncoder() {
        rightEncoder.reset();
    }

    public void resetBackEncoder() {
        backEncoder.reset();
    }

    public void resetEncoders() {
        leftEncoder.reset();
        rightEncoder.reset();
        backEncoder.reset();
    }
}
