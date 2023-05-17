package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Training;

public class ForwardEncoder extends CommandBase {

    private double setpointB;
    private double epsilonYaw;
    private boolean debug;

    PIDController pidZAxis;

    double[] motors = new double[] { 0, 0, 0 };
    private double out;

    private boolean firstExecution = true;
    private double startTime;

    public ForwardEncoder(double setpoint, double epsilonYaw, boolean debug) {
        setpointB = setpoint;
        this.epsilonYaw = epsilonYaw;
        this.debug = debug;
        addRequirements(RobotContainer.train);
        pidZAxis = new PIDController(0.012, 0.003, 0.0);
        pidZAxis.setTolerance(epsilonYaw);
        pidZAxis.setIntegratorRange(-300, 300);
    }

    @Override
    public void initialize() {
        pidZAxis.setSetpoint(setpointB);
        startTime = Timer.getFPGATimestamp();
        firstExecution = true;
    }

    @Override
    public void execute() {
        double currentTime = Timer.getFPGATimestamp();
        if ((currentTime - startTime >= 0.1 || firstExecution) && debug) {
            setpointB = RobotContainer.train.getShuffleboardSystem().getAdditionalValueOutput().getDouble(0);
            pidZAxis.setSetpoint(setpointB);
            updatePID();
            startTime = currentTime;
            firstExecution = false;
        }
        out = pidZAxis.calculate(RobotContainer.train.getMotorSystem().getAverageForwardEncoderDistance(), setpointB);
        RobotContainer.train.getMotorSystem().holonomicDrive(0.0, MathUtil.clamp(out, -0.3, 0.3), 0.0);
        if (debug) {
            RobotContainer.train.getShuffleboardSystem().updateTestString(String.format("S: %.2f O: %.2f A: %.2f",
                    setpointB, out, RobotContainer.train.getSensorSystem().getAngle()));
        }
    }

    @Override
    public void end(boolean interrupted) {
        RobotContainer.train.getMotorSystem().setMotorSpeeds(0., 0., 0.);
        RobotContainer.train.getMotorSystem().resetEncoders();
        pidZAxis.close();
    }

    void updatePID() {
        pidZAxis.setPID(RobotContainer.train.getShuffleboardSystem().getP().getDouble(0),
                RobotContainer.train.getShuffleboardSystem().getI().getDouble(0),
                RobotContainer.train.getShuffleboardSystem().getD().getDouble(0));
    }

    @Override
    public boolean isFinished() {
        return pidZAxis.atSetpoint();
    }
}
