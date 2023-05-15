package frc.robot.subsystems;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.networktables.NetworkTableEntry;

public class ShuffleboardSystem {
    private ShuffleboardTab tab;
    private NetworkTableEntry leftEncoderEntry;
    private NetworkTableEntry rightEncoderEntry;
    private NetworkTableEntry backEncoderEntry;
    private NetworkTableEntry forwardForceEntry;
    private NetworkTableEntry navXEntry;

    public ShuffleboardSystem() {
        this.tab = Shuffleboard.getTab("Training");
        this.leftEncoderEntry = tab.add("Left Encoder", 0).withWidget(BuiltInWidgets.kNumberBar).getEntry();
        this.rightEncoderEntry = tab.add("Right Encoder", 0).withWidget(BuiltInWidgets.kNumberBar).getEntry();
        this.backEncoderEntry = tab.add("Back Encoder", 0).withWidget(BuiltInWidgets.kNumberBar).getEntry();
        this.forwardForceEntry = tab.add("Resulted Forward", 0).withWidget(BuiltInWidgets.kNumberBar).getEntry();
        this.navXEntry = tab.add("NavX Yaw", 0).withWidget(BuiltInWidgets.kNumberBar).getEntry();
    }

    public void updateEncoderEntries(double leftEncoderValue, double rightEncoderValue, double backEncoderValue) {
        leftEncoderEntry.setDouble(leftEncoderValue);
        rightEncoderEntry.setDouble(rightEncoderValue);
        backEncoderEntry.setDouble(backEncoderValue);
    }

    public void updateForwardForceEntry(double forwardForce) {
        forwardForceEntry.setDouble(forwardForce);
    }

    public void updateNavXEntry(double angle) {
        navXEntry.setDouble(angle);
    }
}
