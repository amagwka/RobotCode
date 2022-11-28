package frc.robot.gamepad;

//Import the joystick class
import edu.wpi.first.wpilibj.Joystick;

public class OI
{
    //Create the joystick
    Joystick drivePad;

    public OI(){
        //initialize the joystick 
        drivePad = new Joystick(GamepadConstants.DRIVE_USB_PORT);
    }

    
        public double getRightDriveY(){
            double joy = drivePad.getRawAxis(GamepadConstants.RIGHT_ANALOG_Y);
            if(Math.abs(joy) < 0.05)
                return 0.0;
            else  
                return joy;
        }

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


        public boolean getDriveR1(){
            return drivePad.getRawButton(GamepadConstants.R1);
        }


        public int getDriveDPad(){
            return drivePad.getPOV();
        }


        public boolean getDriveL1(){
            return drivePad.getRawButton(GamepadConstants.L1);
        }

        
        public boolean getDriveXButton(){
            return drivePad.getRawButton(GamepadConstants.X_BUTTON);
        }

        
        public boolean getDriveYButton(){
            return drivePad.getRawButton(GamepadConstants.Y_BUTTON);
        }

        
        public boolean getDriveBButton(){
            return drivePad.getRawButton(GamepadConstants.B_BUTTON);
        }
        
        public boolean getDriveAButton(){
            return drivePad.getRawButton(GamepadConstants.A_BUTTON);
        }
}