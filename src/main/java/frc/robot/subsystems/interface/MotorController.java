public interface MotorController {
    void holonomicDrive(double x, double y, double z);
    void setMotorSpeeds(double leftSpeed, double rightSpeed, double backSpeed);
    void resetAllEncoders();
    void setLeftMotorSpeed(double speed);
    void setRightMotorSpeed(double speed);
    void setBackMotorSpeed(double speed);
    double getLeftEncoderDistance();
    double getRightEncoderDistance();
    double getBackEncoderDistance();
    double getAverageForwardEncoderDistance();
    void resetEncoders();
    void resetLeftEncoder();
    void resetRightEncoder();
    void resetBackEncoder();
}