package frc.robot.subsystems;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.networktables.NetworkTableEntry;

public class ShuffleboardSystem {
    public ShuffleboardTab tab;
    private NetworkTableEntry leftEncoderEntry;
    private NetworkTableEntry rightEncoderEntry;
    private NetworkTableEntry backEncoderEntry;
    private NetworkTableEntry forwardForceEntry;
    private NetworkTableEntry additionalValueOutput;
    private NetworkTableEntry ultrasonic;
    private NetworkTableEntry P;
    private NetworkTableEntry I;
    private NetworkTableEntry D;
    private NetworkTableEntry additionalTextOutput;
    private NetworkTableEntry navXEntry;

    public ShuffleboardSystem() {
        this.tab = Shuffleboard.getTab("Training");
        this.leftEncoderEntry = tab.add("Left Encoder", 0).withWidget(BuiltInWidgets.kNumberBar).getEntry();
        this.rightEncoderEntry = tab.add("Right Encoder", 0).withWidget(BuiltInWidgets.kNumberBar).getEntry();
        this.backEncoderEntry = tab.add("Back Encoder", 0).withWidget(BuiltInWidgets.kNumberBar).getEntry();
        this.ultrasonic = tab.add("Ultrasonic", 0).withWidget(BuiltInWidgets.kNumberBar).getEntry();
        this.forwardForceEntry = tab.add("Resulted Forward", 0).withWidget(BuiltInWidgets.kNumberBar).getEntry();
        this.additionalValueOutput = tab.add("Test value", 0).withWidget(BuiltInWidgets.kTextView).getEntry();
        this.P = tab.add("P", 0).withWidget(BuiltInWidgets.kTextView).getEntry();
        this.I = tab.add("I", 0).withWidget(BuiltInWidgets.kTextView).getEntry();
        this.D = tab.add("D", 0).withWidget(BuiltInWidgets.kTextView).getEntry();
        this.additionalTextOutput = tab.add("Text value", "None at this moment :)").getEntry();
        this.navXEntry = tab.add("NavX Yaw", 0).withWidget(BuiltInWidgets.kNumberBar).getEntry();
    }

    public void updateEncoderEntries(double leftEncoderValue, double rightEncoderValue, double backEncoderValue) {
        leftEncoderEntry.setDouble(leftEncoderValue);
        rightEncoderEntry.setDouble(rightEncoderValue);
        backEncoderEntry.setDouble(backEncoderValue);
    }

    public void updateForwardForceEntry(double forwardForce) {
        forwardForceEntry.forceSetDouble(forwardForce);
    }

    public void updateNavXEntry(double angle) {
        navXEntry.setDouble(angle);
    }

    public void updateTestValue(double value) {
        additionalValueOutput.setDouble(value);
    }

    /**
     * @return the additionalValueOutput
     */
    public NetworkTableEntry getAdditionalValueOutput() {
        return additionalValueOutput;
    }



    /**
     * @return the p
     */
    public NetworkTableEntry getP() {
        return P;
    }

    /**
     * @return the i
     */
    public NetworkTableEntry getI() {
        return I;
    }

    public NetworkTableEntry getD() {
        return D;
    }

    public void setUltrasonic(double value) {
        ultrasonic.setDouble(value);
    }

    public void updateTestString(String text) {
        additionalTextOutput.setString(text);
    }
}
