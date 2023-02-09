package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Swerve;

public class SwerveCMD extends CommandBase {
    private Swerve mSwerve;
    private DoubleSupplier input;

    public SwerveCMD(Swerve mSwerve, DoubleSupplier x) {
        input = x;

        mSwerve = Swerve.getInstance();
        addRequirements(mSwerve);
    }

    @Override
    public void initialize() {}    

    @Override
    public void execute() {
        mSwerve.setSpeeds(input.getAsDouble());
    }

    @Override
    public void end(boolean interrupted) {}

    @Override
    public boolean isFinished() {
        return false;
    }
}
