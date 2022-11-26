package frc.robot.gamepad;

//Import the joystick class
import edu.wpi.first.wpilibj.Joystick;

public class OI
{
    //Create the joystick
    Joystick drivePad;

    public OI()
    {
        //initialize the joystick 
        drivePad = new Joystick(GamepadConstants.DRIVE_USB_PORT);
    }

    /**
     * Drive Controller
     */

        /**
         * @return the y-axis value from the drivePad right joystick
         */
        public double getRightDriveY()
        {
            double joy = drivePad.getRawAxis(GamepadConstants.RIGHT_ANALOG_Y);
            if(Math.abs(joy) < 0.05)
                return 0.0;
            else  
                return joy;
        }

        /**
         * @return the x-axis value from the drivePad right Joystick
         */
        public double getRightDriveX()
        {
            double joy = drivePad.getRawAxis(GamepadConstants.RIGHT_ANALOG_X);
            if(Math.abs(joy) < 0.05)
                return 0.0;
            else
                return joy;
        }

        /**
         * @return the y-axis value from the drivePad left joystick
         */
        public double getLeftDriveY()
        {
            double joy = drivePad.getRawAxis(GamepadConstants.LEFT_ANALOG_Y);
            if(Math.abs(joy) < 0.05)
                return 0.0;
            else  
                return joy;
        }
    
        /**
         * @return the x-axis value from the drivePad left Joystick
         */
        public double getLeftDriveX()
        {
            double joy = drivePad.getRawAxis(GamepadConstants.LEFT_ANALOG_X);
            if(Math.abs(joy) < 0.05)
                return 0.0;
            else
                return joy;
        }

        /**
         * @return a true or false depending on the input
         */
        public double getDriveR2()
        {
            double joy = drivePad.getRawAxis(GamepadConstants.R2);
            if(Math.abs(joy) < 0.05)
                return 0.0;
            else
                return joy;
        }

        /**
         * @return a true or false depending on the input
         */
        public boolean getDriveR1()
        {
            return drivePad.getRawButton(GamepadConstants.R1);
        }

        /**
         * @return a true or false depending on the input
         */
        public int getDriveDPad()
        {
            return drivePad.getPOV();
        }

        /**
         * @return a true or false depending on the input
         */
        public double getDriveL2()
        {
            double joy = drivePad.getRawAxis(GamepadConstants.L2);
            if(Math.abs(joy) < 0.05)
                return 0.0;
            else
                return joy;
        }

        /**
         * @return a true or false depending on the input
         */
        public boolean getDriveL1()
        {
            return drivePad.getRawButton(GamepadConstants.L1);
        }

        /**
         * @return a true or false depending on the input
         */
        public boolean getDriveXButton()
        {
            return drivePad.getRawButton(GamepadConstants.X_BUTTON);
        }

        /**
         * @return a true or false depending on the input
         */
        public boolean getDriveYButton()
        {
            return drivePad.getRawButton(GamepadConstants.Y_BUTTON);
        }

        /**
         * @return a true or false depending on the input
         */
        public boolean getDriveBButton()
        {
            return drivePad.getRawButton(GamepadConstants.B_BUTTON);
        }

        /**
         * @return a true or false depending on the input
         */
        public boolean getDriveAButton()
        {
            return drivePad.getRawButton(GamepadConstants.A_BUTTON);
        }
}