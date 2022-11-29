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
        
        public double getDriveRightBumper(){
            return drivePad.getRawAxis(GamepadConstants.RIGHT_BUMPER);
        }
        public double getDriveLeftBumper(){
            return drivePad.getRawAxis(GamepadConstants.LEFT_BUMPER);
        }
        
        public boolean getDriveAButton(){
            return drivePad.getRawButton(GamepadConstants.A_BUTTON);
        }
        public boolean getDriveBButton(){
            return drivePad.getRawButton(GamepadConstants.B_BUTTON);
        }
        public boolean getDriveXButton(){
            return drivePad.getRawButton(GamepadConstants.X_BUTTON);
        }
        public boolean getDriveYButton(){
            return drivePad.getRawButton(GamepadConstants.Y_BUTTON);
        }

        public boolean getDriveBackButton(){
            return drivePad.getRawButton(GamepadConstants.BACK_BUTTON);
        }
        public boolean getDriveStartButton(){
            return drivePad.getRawButton(GamepadConstants.START_BUTTON);
        }

        public boolean getDriveRightAnalogButton(){
            return drivePad.getRawButton(GamepadConstants.RIGHT_ANALOG_BUTTON);
        }
        public boolean getDriveLeftAnalogButton(){
            return drivePad.getRawButton(GamepadConstants.LEFT_ANALOG_BUTTON);
        }

public boolean getDriveRightR1Button(){
    return drivePad.getRawButton(GamepadConstants.RT);
}
public boolean getDriveLeftL1Button(){
    return drivePad.getRawButton(GamepadConstants.LT);
}
}