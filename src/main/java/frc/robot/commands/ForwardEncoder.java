package frc.robot.commands;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Training;

public class ForwardEncoder extends CommandBase {
    private static final Training train = RobotContainer.train;

    private double setpointDistanceArgument=0;
    private double setpointDistance=0;
    private double smoothLimit=0.05;
    PIDController pidYAxis;


    public ForwardEncoder(double setpointDistanceArg, double epsilonDistance,boolean delta) {
        setpointDistanceArgument=setpointDistanceArg;
        
        addRequirements(train);

        pidYAxis = new PIDController(0.01, 0.1,0.0);
        pidYAxis.setTolerance(epsilonDistance);
    }

    @Override
    public void initialize() {
        setpointDistance = train.getAverageForwardEncoderDistance()+setpointDistanceArgument;
        }

    @Override
    public void execute() {
            train.holonomicDrive(0.0, MathUtil
                    .clamp(pidYAxis.calculate(train.getAverageForwardEncoderDistance(), setpointDistance), -smoothLimit, smoothLimit),0.0);
            if(smoothLimit<1){
            smoothLimit=smoothLimit+0.05;
            }
    }
    @Override
    public void end(boolean interrupted) {
        //train.setDriveMotorSpeeds(0.0, 0.0, 0.0);
    }
    @Override
    public boolean isFinished() {
        return pidYAxis.atSetpoint();
    }
}