package frc.robot.subsystems;

import com.studica.frc.Servo;
import com.studica.frc.ServoContinuous;
import com.studica.frc.TitanQuad;
import com.studica.frc.TitanQuadEncoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.C;

public class OMS extends SubsystemBase {

    private TitanQuad R_lift;
    private Servo gripper;
    private ServoContinuous lift;

    private TitanQuadEncoder R_liftEncoder;

    public OMS() {
        R_lift = new TitanQuad(C.TITAN_ID, C.MOTOR_ROTATE_LIFT);
        gripper = new Servo(C.SERVO_GRIPPER);
        
        lift = new ServoContinuous(C.SERVO_LIFT);

        R_liftEncoder = new TitanQuadEncoder(R_lift, C.MOTOR_ROTATE_LIFT, 1); //0.429179
    }

    public void setLiftSpeed(double speed) {
        lift.setSpeed(speed);
    }

    public void setGripperPosition(double degrees) {
        gripper.setAngle(degrees);
    }

    public void resetEncoders() {
        R_liftEncoder.reset();
    }

    @Override
    public void periodic() {
    }

	public void setGripper2Position(double r_LiftSpeed) {
        
	}
}