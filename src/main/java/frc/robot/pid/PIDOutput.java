package frc.robot.pid;

public class PIDOutput{
    private double clampedOutput;
    private double output;

    public PIDOutput(double clampedOutput, double output){
        this.clampedOutput = clampedOutput;
        this.output = output;
    }

    public double getClampedOutput(){
        return clampedOutput;
    }

    public double getOutput(){
        return output;
    }
}
