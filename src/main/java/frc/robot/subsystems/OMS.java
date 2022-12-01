package frc.robot.subsystems;

import java.util.Map;

import com.studica.frc.Servo;
import com.studica.frc.ServoContinuous;
import com.studica.frc.TitanQuad;
import com.studica.frc.TitanQuadEncoder;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.C;

public class OMS extends SubsystemBase {
    /**
     * Motors
     */
    private TitanQuad R_lift;
    private Servo gripper, R_gripper;
    private ServoContinuous lift;

    /**
     * Sensors
     */
    private TitanQuadEncoder R_liftEncoder;

    /**
     * Shuffleboard
     */
    private ShuffleboardTab tab = Shuffleboard.getTab("OMS");
    private NetworkTableEntry R_liftEncoderValue = tab.add("Rotate_lift Encoder", 0).getEntry();

    private NetworkTableEntry gripperValue = tab.add("gripper", 150).withWidget(BuiltInWidgets.kNumberSlider)
    .withProperties(Map.of("min", 0, "max", 300)).getEntry();

    private NetworkTableEntry R_gripperValue = tab.add("Rotate_gripper", 150).withWidget(BuiltInWidgets.kNumberSlider)
    .withProperties(Map.of("min", 0, "max", 300)).getEntry();

    private NetworkTableEntry liftValue = tab.add("lift", 0).withWidget(BuiltInWidgets.kNumberSlider)
    .withProperties(Map.of("min", -1, "max", 1)).getEntry();

    private NetworkTableEntry R_liftValue = tab.add("Rotate_lift", 0).withWidget(BuiltInWidgets.kNumberSlider)
    .withProperties(Map.of("min", -1, "max", 1)).getEntry();

    private NetworkTableEntry LiftEncoder = tab.add("LiftEncoder", 0).withWidget(BuiltInWidgets.kNumberBar).getEntry();


    public OMS() {
        R_lift = new TitanQuad(C.TITAN_ID, C.MOTOR_ROTATE_LIFT);
        gripper = new Servo(C.SERVO_GRIPPER);
        lift = new ServoContinuous(C.SERVO_LIFT);
        R_gripper = new Servo(C.SERVO_GRIPPER_ROTATE);
        
        
        
        R_liftEncoder = new TitanQuadEncoder(R_lift, C.MOTOR_ROTATE_LIFT, 1); //0.429179
    }

    public void setR_liftMotorSpeed(double speed) {
        R_lift.set(speed);
    }

    public double getR_liftEncoderDistance() {
        return R_liftEncoder.getEncoderDistance();
    }


    public void setGripperPosition(double degrees) {
        gripper.setAngle(degrees);
    }

    public void setR_gripperPosition(double degrees) {
        R_gripper.setAngle(degrees);
    }

    public void setLiftSpeed(double speed) {
        lift.setSpeed(speed);
    }

    public void resetEncoders() {
        R_liftEncoder.reset();
    }

    @Override
    public void periodic() {
        /*
        R_liftEncoderValue.setDouble(getR_liftEncoderDistance());
        setGripperPosition(gripperValue.getDouble(0.0));
        setLiftSpeed(liftValue.getDouble(0.0));
        setR_gripperPosition(R_gripperValue.getDouble(0.0));

        setR_liftMotorSpeed(R_liftValue.getDouble(0.0));*/
        LiftEncoder.setDouble(getR_liftEncoderDistance());
    }
}