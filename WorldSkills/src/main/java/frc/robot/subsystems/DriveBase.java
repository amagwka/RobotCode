package frc.robot.subsystems;

import com.studica.frc.Servo;
import com.studica.frc.TitanQuad;
import com.studica.frc.TitanQuadEncoder;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class DriveBase extends SubsystemBase {
    //Motors
    private TitanQuad leftMotor;
    private TitanQuad rightMotor;
    private TitanQuad elevator;
    //Encoder
    private TitanQuadEncoder leftEncoder;
    private TitanQuadEncoder rightEncoder;
    private TitanQuadEncoder elevatorEncoder;
    //Servo
    private Servo clawL;
    private Servo clawR;
    private Servo cameraServo;
    //Shuffleboard
    private ShuffleboardTab tab = Shuffleboard.getTab("727Gamers");
    private NetworkTableEntry leftEncoderValue = tab.add("Left Encoder", 5).getEntry();
    private NetworkTableEntry rightEncoderValue = tab.add("Right Encoder", 5).getEntry();
    private NetworkTableEntry elevatorEncoderValue = tab.add("Elevator Encoder", 5).getEntry();

    public DriveBase () {
        //Motor
        leftMotor = new TitanQuad(Constants.TITAN_ID, Constants.M2);
        rightMotor = new TitanQuad(Constants.TITAN_ID, Constants.M0);
        elevator = new TitanQuad(Constants.TITAN_ID, Constants.M3);
        //Encoder
        leftEncoder = new TitanQuadEncoder(leftMotor, Constants.M2, Constants.wheelDistPerTick);
        rightEncoder = new TitanQuadEncoder(rightMotor, Constants.M0, Constants.wheelDistPerTick);
        elevatorEncoder = new TitanQuadEncoder(elevator, Constants.M3, Constants.ELEVATOR_DIST_TICK);
        //Servo
        clawL = new Servo(Constants.ServoL);
        clawR = new Servo(Constants.ServoR);
        cameraServo = new Servo(Constants.ServoCamera);
    }

    public void setCameraServo(double degree){
        cameraServo.set(degree);
    }

    //speed range -1 to 1 (0 stop)
    public void setDriverMotorSpeed(double leftSpeed, double rightSpeed) {
        leftMotor.set(-leftSpeed);
        rightMotor.set(rightSpeed);
    }

    public void setElevatorMotorSpeed(double speed)
    {
        elevator.set(speed);
    }

    public void setClawLeftServoPosition(double degrees)
    {
        clawL.setAngle(degrees);
    }
    
    public void setClawRightServoPosition(double degrees)
    {
        clawR.setAngle(degrees);
    }

    public void setClawServoPosition(double L, double R)
    {
        clawL.setAngle(L);
        clawR.setAngle(R);
    }

    //Distance traveled in mm
    public double getLeftEncoderDistance(){
        return leftEncoder.getEncoderDistance();
    }
    public double getRightEncoderDistance(){
        return rightEncoder.getEncoderDistance();
    }
    public double getElevatorEncoderDistance(){
        return elevatorEncoder.getEncoderDistance();
    }

    public void resetEncoder() {
        leftEncoder.reset();
        rightEncoder.reset();
    }

    @Override
    public void periodic(){
        leftEncoderValue.setValue(getLeftEncoderDistance());
        //leftEncoderValue.setDouble(getLeftEncoderDistance());
        rightEncoderValue.setDouble(getRightEncoderDistance());
        elevatorEncoderValue.setDouble(getElevatorEncoderDistance());
    }
}