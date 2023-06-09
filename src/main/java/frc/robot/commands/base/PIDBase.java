package frc.robot.commands.base;

import edu.wpi.first.wpilibj2.command.CommandBase;
abstract public class PIDBase extends CommandBase{
    
  protected final edu.wpi.first.wpilibj.controller.PIDController m_controller;
  
  protected java.util.function.DoubleSupplier m_measurement;
  
  protected java.util.function.DoubleSupplier m_setpoint;
  
  protected java.util.function.DoubleConsumer m_useOutput;
  
  public PIDBase(edu.wpi.first.wpilibj.controller.PIDController controller, java.util.function.DoubleSupplier measurementSource, java.util.function.DoubleSupplier setpointSource, java.util.function.DoubleConsumer useOutput, edu.wpi.first.wpilibj2.command.Subsystem... requirements) {
  }
  
  public PIDBase(edu.wpi.first.wpilibj.controller.PIDController controller, java.util.function.DoubleSupplier measurementSource, double setpoint, java.util.function.DoubleConsumer useOutput, edu.wpi.first.wpilibj2.command.Subsystem... requirements) {
  }
  
  public void initialize() {
  }
  
  public void execute() {
  }
  
  public void end(boolean interrupted) {
  }
  
  public edu.wpi.first.wpilibj.controller.PIDController getController() {
    return null;
  }
  
  private static double lambda$new$0(double setpoint) {
    return 0;
  }

}