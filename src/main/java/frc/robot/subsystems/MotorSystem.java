package frc.robot.subsystems;

import com.studica.frc.TitanQuad;
import com.studica.frc.TitanQuadEncoder;

import frc.robot.C;

public class MotorSystem {
    private TitanQuad leftMotor;
    private TitanQuad rightMotor;
    private TitanQuad backMotor;
    public TitanQuadEncoder leftEncoder;
    public TitanQuadEncoder rightEncoder;
    public TitanQuadEncoder backEncoder;

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
    
        // Normalize speeds if they exceed 1
        double max = Math.max(Math.max(Math.abs(rightSpeed), Math.abs(leftSpeed)), Math.abs(backSpeed));
        if (max > 1) {
            rightSpeed /= max;
            leftSpeed /= max;
            backSpeed /= max;
        }
    /*
        // Get encoder speeds and directions
        double leftEncoderSpeed = leftEncoder.getSpeed();
        double rightEncoderSpeed = rightEncoder.getSpeed();
        boolean leftDirection = leftEncoder.getDirection();
        boolean rightDirection = rightEncoder.getDirection();
    
        // Compute absolute ratios
        double leftRatio = Math.abs(leftEncoderSpeed / leftSpeed);
        double rightRatio = Math.abs(rightEncoderSpeed / rightSpeed);
    
        // Adjust speeds to maintain equivalent ratios, if necessary
        if (leftRatio != rightRatio) {
            if (leftRatio > rightRatio) {
                leftSpeed = (rightRatio / leftRatio) * leftSpeed;
            } else {
                rightSpeed = (leftRatio / rightRatio) * rightSpeed;
            }
        }
    
        // Ensure directions match, adjusting speed sign if necessary
        if (leftDirection && leftSpeed < 0) leftSpeed = -leftSpeed;
        if (!leftDirection && leftSpeed > 0) leftSpeed = -leftSpeed;
        if (rightDirection && rightSpeed < 0) rightSpeed = -rightSpeed;
        if (!rightDirection && rightSpeed > 0) rightSpeed = -rightSpeed;
    */
        // Set motor speeds
        leftMotor.set(leftSpeed);
        rightMotor.set(rightSpeed);
        backMotor.set(backSpeed);
    }
    

    public double getLeftEncoderDistance() {
        return leftEncoder.getEncoderDistance();
    }

    public double getRightEncoderDistance() {
        return rightEncoder.getEncoderDistance();
    }

    public double getBackEncoderDistance() {
        return backEncoder.getEncoderDistance();
    }

    public double getAverageSideEncoderDistance() {
        double sideEncoderDist = (getLeftEncoderDistance() - getRightEncoderDistance()) / 2.0;
        return (sideEncoderDist + getBackEncoderDistance()) / 2.0;
    }

    public void holonomicDrive(double x, double y, double z, double forwardShift, double sideShift) {
        double rightSpeed = x * 0.5 - Math.sqrt(3) / 2 * y + z + forwardShift + sideShift;
        double leftSpeed = x * 0.5 + Math.sqrt(3) / 2 * y + z + forwardShift + sideShift;
        double backSpeed = -x + z - sideShift;

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

    public double getAverageForwardEncoderDistance() {
        return (getLeftEncoderDistance() - getRightEncoderDistance()) * 0.5;
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
