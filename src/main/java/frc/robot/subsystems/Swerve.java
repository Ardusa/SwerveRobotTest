package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.drive.RobotDriveBase.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Swerve extends SubsystemBase {
    private final TalonSRX Swerve1 = new TalonSRX(7);
    private final CANSparkMax Swerve2 = new CANSparkMax(14, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);

    private static Swerve Instance;

    public static Swerve getInstance() {
        if (Instance == null) {
            Instance = new Swerve();
        }
        return Instance;
    }

    public Swerve() {}

    public void setSpeeds(double input, double input2) {
        Swerve1.set(ControlMode.Position, input);
        Swerve2.set(input2);
    }
}
