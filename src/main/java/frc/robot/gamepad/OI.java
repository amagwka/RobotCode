package frc.robot.gamepad;

import java.lang.reflect.Array;
import java.util.ArrayList;

//Import the joystick class
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.RobotContainer;

public class OI
{
    //Create the joystick
    Joystick drivePad;
    ArrayList<Double> arr=new ArrayList<Double>();
    
    public OI(){
        drivePad = new Joystick(GamepadConstants.DRIVE_USB_PORT);
    }
    /*public double getPadNumber(){
        for(int i=0;i<100;i++){
            if(drivePad.getRawAxis(i) != 0){
                RobotContainer.train.tab.add(String.format("Values %d",i),drivePad.getRawAxis(i));
                Timer.delay(0.1);
            }
        }
        return 0.0;
    }*/
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
            return drivePad.getRawAxis(2);
        }

        public int getPOVDegree(){
            return drivePad.getPOV();
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