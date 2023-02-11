package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Swerve;

public class SwerveCMD extends CommandBase {
    private final XboxController xDrive = new XboxController(0);
    private Swerve mSwerve;
    private DoubleSupplier input, input2;

    public SwerveCMD(DoubleSupplier x, DoubleSupplier y) {
        input = x;
        input2 = y;
        mSwerve = Swerve.getInstance();
        addRequirements(mSwerve);
    }

    @Override
    public void initialize() {}    

    @Override
    public void execute() {
        mSwerve.setSpeeds(input.getAsDouble(), input2.getAsDouble());
        xDrive.setRumble(RumbleType.kBothRumble, 1);
    }

    @Override
    public void end(boolean interrupted) {}

    @Override
    public boolean isFinished() {
        return false;
    }
}
