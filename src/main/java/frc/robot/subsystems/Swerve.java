package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Swerve extends SubsystemBase {
    private final TalonSRX Swerve1 = new TalonSRX(7);

    private static Swerve Instance;

    public static Swerve getInstance() {
        if (Instance == null) {
            Instance = new Swerve();
        }
        return Instance;
    }

    public Swerve() {}

    public void setSpeeds(double input) {
        Swerve1.set(ControlMode.PercentOutput, input);
    }
}
