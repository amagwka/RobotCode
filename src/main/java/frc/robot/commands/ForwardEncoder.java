package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.RobotContainer;

// Command for controlling the forward motion of the robot
public class ForwardEncoder extends CommandBase {

    // Setpoint for the forward motion
    private double setpoint;

    private double setpointYaw;
    // Debug mode flag
    private boolean debug;

    // PID Controllers for the forward motion and the angle
    PIDController pidYAxis;
    PIDController pidZAxis;

    // Counters for checking the conditions
    private int atSetpointCounter = 0;
    private int encoderStuckCounter = 0;

    // Last encoder value to detect the stuck encoder
    private double lastEncoderValue = 0.0;

    private boolean firstExecution = true;
    private double startTime;

    // Constructor
    public ForwardEncoder(double setpoint, double epsilon, boolean debug) {
        // Multiply by 40 to convert from meters to encoder ticks
        this.setpoint = setpoint * 40;
        this.debug = debug;

        // Add dependencies
        addRequirements(RobotContainer.train);

        // Initialize the PID Controllers
        pidYAxis = new PIDController(0.012, 0.0, 0.0);
        pidYAxis.setTolerance(epsilon);
        pidYAxis.setIntegratorRange(-0.2, 0.2);

        pidZAxis = new PIDController(0.13, 0.0, 0.0);
        pidZAxis.setIntegratorRange(-0.2, 0.2);
    }

    // Called when the command is initially scheduled
    @Override
    public void initialize() {
        pidYAxis.setSetpoint(setpoint);
        setpointYaw = RobotContainer.train.getSensorSystem().getYaw();
        pidZAxis.setSetpoint(setpointYaw);
        startTime = Timer.getFPGATimestamp();
        firstExecution = true;
    }

    // Called every time the scheduler runs while the command is scheduled
    @Override
    public void execute() {
        double currentTime = Timer.getFPGATimestamp();
        double currentEncoderValue = RobotContainer.train.getMotorSystem().getAverageForwardEncoderDistance();

        updateSetpointIfNeeded(currentTime, currentEncoderValue);
        checkAndCorrectStuckEncoder(currentEncoderValue);
        executePIDControl(currentEncoderValue);
    }

    // Called once the command ends or is interrupted
    @Override
    public void end(boolean interrupted) {
        RobotContainer.train.getMotorSystem().setMotorSpeeds(0., 0., 0.);
        RobotContainer.train.getMotorSystem().resetEncoders();
        pidYAxis.close();
        pidZAxis.close();
    }

    // Update the setpoint if needed based on the current time and encoder value
    void updateSetpointIfNeeded(double currentTime, double currentEncoderValue) {
        if ((currentTime - startTime >= 0.1 || firstExecution) && debug) {
            setpoint = RobotContainer.train.getShuffleboardSystem().getAdditionalValueOutput().getDouble(0)*40;
            pidYAxis.setSetpoint(setpoint * 40);
            updatePID();
            startTime = currentTime;
            firstExecution = false;
        }
    }

    // Check if the encoder value is stuck, and enable the integral part of PID if needed
    void checkAndCorrectStuckEncoder(double currentEncoderValue) {
        if (currentEncoderValue == lastEncoderValue && !pidYAxis.atSetpoint()) {
            encoderStuckCounter++;
            if (encoderStuckCounter >= 10) {
                enableIntegral();
                encoderStuckCounter = 0;
            }
        } else {
            encoderStuckCounter = 0;
        }
        lastEncoderValue = currentEncoderValue;
    }

    // Execute the PID control and update the debug string
    void executePIDControl(double currentEncoderValue) {
        double outY = pidYAxis.calculate(currentEncoderValue, setpoint);
        double outZ = pidZAxis.calculate(RobotContainer.train.getSensorSystem().getYaw(), pidZAxis.getSetpoint());

        RobotContainer.train.getMotorSystem().holonomicDrive(0.0,
                MathUtil.clamp(outY, -0.6, 0.6), 0.0, MathUtil.clamp(outZ, -0.6, 0.6));

        if (debug) {
            RobotContainer.train.getShuffleboardSystem().updateTestString(String.format("S: %.2f O: %.2f A: %.2f",
                    setpoint, outY, RobotContainer.train.getSensorSystem().getYaw()));
        }
    }

    // Update the PID gains from the Shuffleboard
    void updatePID() {
        pidYAxis.setPID(RobotContainer.train.getShuffleboardSystem().getP().getDouble(0),
                RobotContainer.train.getShuffleboardSystem().getI().getDouble(0),
                RobotContainer.train.getShuffleboardSystem().getD().getDouble(0));
    }

    // Enable the integral control by resetting the PID and setting the I gain
    void enableIntegral() {
        pidYAxis.reset();
        pidYAxis.setI(0.002);
    }

    // Return true if the command is finished
    @Override
    public boolean isFinished() {
        if (pidYAxis.atSetpoint() && !debug) {
            atSetpointCounter++;
            if (atSetpointCounter >= 10) {
                return true;
            }
        } else {
            atSetpointCounter = 0;
        }
        return false;
    }
}
