package frc.robot.subsystems;

//WPI imports
import edu.wpi.first.wpilibj2.command.SubsystemBase;

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
            shuffleboardSystem.updateNavXEntry(sensorSystem.getYaw());
        }
        x++;
    }
}