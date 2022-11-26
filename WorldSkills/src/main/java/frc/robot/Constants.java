package frc.robot;

public final class Constants {
    /* CAN ID */
    public static final int TITAN_ID = 42;

    /*
    DriveBase
    */
        //Motors
        public static final int M0 = 2; //Right Motor
        public static final int M2 = 3; //Left Motor

    /*
    Encoders
    */
        //Onmi Wheels Radius
        public static final double wheelRadius = 50;
        //Encoder pulse pre revolution
        public static final double pulsePerRevolution = 1440;
        //Gear ratio between encoder and wheel
        public static final double gearRatio = 2/2;
        //Pulse per revolution of wheel
        public static final double wheelPulseRatio = pulsePerRevolution * gearRatio;
        //Distance per tick
        public static final double wheelDistPerTick = (Math.PI * 2 * wheelRadius) / wheelPulseRatio;

    /*
    Elevator Constants
    */
        //Motors
        public static final int M3 = 0; //Elevator motor
        public static final int ServoL = 6; //Claw servo
        public static final int ServoR = 9;
        public static final int ServoCamera = 7;

        public static final double pulleyRadius         = 7.85; //mm

        // Encoder pulses per revolution
        public static final double pulsePerRevElevator  = 1440;

        // Gear ratio between encoder and pulley
        public static final double elevatorGearRatio    = 2/1;

        // Pulse per revolution of pulley
        public static final double pulleyPulseRatio     = pulsePerRevElevator * elevatorGearRatio;

        // Distance per tick
        public static final double ELEVATOR_DIST_TICK   = (Math.PI * 2 * pulleyRadius) / pulleyPulseRatio;

        //Circle Servo
        public static final double RobotSide = 441;//in mm
        public static final double RobotRadius = (RobotSide * 1.73205081 * 2) / 6;
        public static final double RobotCircleLenght = 2 * Math.PI * RobotRadius;
        public static final int CircleServo = 5;
        public static final int OpeningServo = 9;
}
