package frc.robot.gamepad;

//Import the joystick class
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;

public class OI
{
    //Create the joystick
    Joystick drivePad;

    public OI(){
        drivePad = new Joystick(GamepadConstants.DRIVE_USB_PORT);
    }
        public double getRightDriveY(){
            double joy = drivePad.getRawAxis(GamepadConstants.RIGHT_ANALOG_Y);
            if(Math.abs(joy) < 0.05)
                return 0.0;
            else  
                return joy;
        }

        /**
         * @return the x-axis value from the drivePad right Joystick
         */
        public double getRightDriveX(){
            double joy = drivePad.getRawAxis(GamepadConstants.RIGHT_ANALOG_X);
            if(Math.abs(joy) < 0.05)
                return 0.0;
            else
                return joy;
        }
        public double getLeftDriveY(){
            double joy = drivePad.getRawAxis(GamepadConstants.LEFT_ANALOG_Y);
            if(Math.abs(joy) < 0.05)
                return 0.0;
            else  
                return joy;
        }
        public double getLeftDriveX(){
            double joy = drivePad.getRawAxis(GamepadConstants.LEFT_ANALOG_X);
            if(Math.abs(joy) < 0.05)
                return 0.0;
            else
                return joy;
        }

        /**
         * @return a true or false depending on the input
         */
        public boolean getDriveRightBumper()
        {
            return drivePad.getRawButton(GamepadConstants.RIGHT_BUMPER);
        }

        /**
         * @return a true or false depending on the input
         */
        public double getDriveLeftBumper()
        {
            return drivePad.getRawAxis(5);
        }

        /**
         * @return a true or false depending on the input
         */
        public boolean getDriveAButton()
        {
            return drivePad.getRawButton(GamepadConstants.A_BUTTON);
        }

        /**
         * @return a true or false depending on the input
         */
        public boolean getDriveBackButton()
        {
            return drivePad.getRawButton(GamepadConstants.BACK_BUTTON);
        }

        /**
         * @return a true or false depending on the input
         */
        public boolean getDriveStartButton()
        {
            return drivePad.getRawButton(GamepadConstants.START_BUTTON);
        }

        /**
         * @return a true or false depending on the input
         */
        public boolean getDriveRightAnalogButton()
        {
            return drivePad.getRawButton(GamepadConstants.RIGHT_ANALOG_BUTTON);
        }

        /**
         * @return a true or false depending on the input
         */
        public boolean getDriveLeftAnalogButton()
        {
            return drivePad.getRawButton(GamepadConstants.LEFT_ANALOG_BUTTON);
        }
}   