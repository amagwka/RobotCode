package frc.robot.subsystems;

//WPI imports
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.C;
import frc.robot.gamepad.OI;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;

import java.util.Map;

//Java imports

//Vendor imports
import com.kauailabs.navx.frc.AHRS;
import com.studica.frc.TitanQuad;
import com.studica.frc.TitanQuadEncoder;
import com.studica.frc.Cobra;
import com.studica.frc.Servo;
import com.studica.frc.ServoContinuous;



public class Training extends SubsystemBase {
    private MotorSystem motorSystem;
    private SensorSystem sensorSystem;
    private ShuffleboardSystem shuffleboardSystem;
    private int x = 0;

    public Training(MotorSystem motorSystem, SensorSystem sensorSystem, ShuffleboardSystem shuffleboardSystem) {
        this.motorSystem = motorSystem;
        this.sensorSystem = sensorSystem;
        this.shuffleboardSystem = shuffleboardSystem;
    }
    
    public MotorSystem getMotorSystem() {
        return this.motorSystem;
    }
    
    @Override
    public void periodic() {
        if (x % 6==0) {
            // setServoAngle();
            //setServoSpeed();
            // sharpIR.setDouble(getIRDistance());
            // ultraSonic.setDouble(getSonicDistance(true));
            // cobraRaw.setDouble(getCobraRawValue(0)); //Just going to use channel 0 for
            // cobraVoltage.setDouble(getCobraVoltage(0));
            //setLeftMotorSpeed(LeftMotor.getDouble(0));
            //setRightMotorSpeed(RightMotor.getDouble(0));
            //setBackMotorSpeed(BackMotor.getDouble(0));
            LeftEncoder.setDouble(getLeftEncoderDistance());
            RightEncoder.setDouble(getRightEncoderDistance());
            BackEncoder.setDouble(getBackEncoderDistance());
            ForwardForce.setDouble(getAverageForwardEncoderDistance());
            //navX.setDouble(getAngle());
        }
        x++;
    }
}