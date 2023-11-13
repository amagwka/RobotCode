package frc.robot.subsystems;

//WPI imports
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;

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
    public SensorSystem getSensorSystem() {
        return this.sensorSystem;
    }
    public ShuffleboardSystem getShuffleboardSystem() {
        return this.shuffleboardSystem;
    }

    @Override
    public void periodic() {
        if (x % 10==0) {
            shuffleboardSystem.updateEncoderEntries(motorSystem.getLeftEncoderDistance(), motorSystem.getRightEncoderDistance(), motorSystem.getBackEncoderDistance());
            shuffleboardSystem.updateForwardForceEntry(motorSystem.getAverageForwardEncoderDistance());
            shuffleboardSystem.updateNavXEntry(sensorSystem.getAngle());
            shuffleboardSystem.setIR(sensorSystem.getSonic1Distance(), sensorSystem.getIR2Distance());
            
            //shuffleboardSystem.updateTestString(String.format("1: %d 2: %d 3: %d 4: %d", sensorSystem.getCobraRawValue(0),sensorSystem.getCobraRawValue(1),sensorSystem.getCobraRawValue(2),sensorSystem.getCobraRawValue(3)));
            //shuffleboardSystem.updateTestString(String.format("1: %f",sensorSystem.getIR1Distance()));
            //shuffleboardSystem.updateTest2String(RobotContainer.i);
        }
        //sensorSystem.FilterUpdate();
        //shuffleboardSystem.updateTest2String(String.format("%f", sensorSystem.gyro.getWorldLinearAccelX()));

        x++;
    }
}