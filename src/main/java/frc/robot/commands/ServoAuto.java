package frc.robot.commands;

import com.studica.frc.Servo;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.RobotContainer;
import frc.robot.gamepad.OI;
import frc.robot.subsystems.Training;

public class ServoAuto extends CommandBase {

    private static final Training train = RobotContainer.train;
    private int pin;
    private int angle;
    private int i=0;
    Servo servo;
    public ServoAuto(int p, int angle) {
        addRequirements(train);
        pin = p;
        this.angle = angle;
    }

    @Override
    public void initialize() {
        servo = new Servo(pin);
    }
    @Override
    public void execute() {
        servo.setAngle(angle);
        i++;
    }

    @Override
    public void end(boolean interrupted) {
        servo.close();
        train.getMotorSystem().setMotorSpeeds(0., 0., 0.);
    }

    @Override
    public boolean isFinished() {
        return i>=50;
    }
}