package frc.robot.subsystems;

import frc.robot.SwerveModule;
import frc.robot.CustomWpilib.CustomSwerveDriveOdometry;
import frc.robot.Utils.Vector2D;
import frc.robot.Constants;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;

import com.ctre.phoenix.sensors.Pigeon2;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Swerve extends SubsystemBase {
    public CustomSwerveDriveOdometry swerveOdometry;
    public SwerveModule[] mSwerveMods;
    public Pigeon2 gyro;
    // The Swerve class should not hold the vision systems, this is a great way to end up in dependecy hell
    // Use double suppliers or something instead and keep vision in robot container...
    public static Swerve mInstance;

    public static Swerve getInstance() {
        if(mInstance == null) {
            mInstance = new Swerve();
        }
        return mInstance;
    }

    public Swerve() {
        gyro = new Pigeon2(3);
        zeroGyro();

        mSwerveMods = new SwerveModule[] {
            new SwerveModule(1, Constants.Swerve.Mod0.constants),
            new SwerveModule(0, Constants.Swerve.Mod1.constants),
            new SwerveModule(2, Constants.Swerve.Mod2.constants),
            new SwerveModule(3, Constants.Swerve.Mod3.constants)
        };

        /* By pausing init for a second before setting module offsets, we avoid a bug with inverting motors.
         * See https://github.com/Team364/BaseFalconSwerve/issues/8 for more info.
         */
        Timer.delay(1.0);
        resetModulesToAbsolute();

        swerveOdometry = new CustomSwerveDriveOdometry(Constants.Swerve.swerveKinematics, getYaw(), getModulePositions());
    }

    public void drive(Translation2d translation, double rotation, boolean fieldRelative, boolean isOpenLoop) {
        SwerveModuleState[] swerveModuleStates =
            Constants.Swerve.swerveKinematics.toSwerveModuleStates(
                fieldRelative ? ChassisSpeeds.fromFieldRelativeSpeeds(
                                    translation.getX(), 
                                    translation.getY(), 
                                    rotation, 
                                    getYaw()
                                )
                                : new ChassisSpeeds(
                                    translation.getX(), 
                                    translation.getY(), 
                                    rotation)
                                );
        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, Constants.Swerve.maxSpeed);

        for(SwerveModule mod : mSwerveMods){
            mod.setDesiredState(swerveModuleStates[mod.moduleNumber], isOpenLoop);
        }
    }    

    /* Used by SwerveControllerCommand in Auto */
    public void setModuleStates(SwerveModuleState[] desiredStates) {
        SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, Constants.Swerve.maxSpeed);
        
        for(SwerveModule mod : mSwerveMods){
            mod.setDesiredState(desiredStates[mod.moduleNumber], false);
        }
    }    

    public Pose2d getPose() {
        return swerveOdometry.getPoseMeters();
    }

    public void resetOdometry(Pose2d pose) {
        swerveOdometry.resetPosition(getYaw(), getModulePositions(), pose);
    }

    public SwerveModuleState[] getModuleStates(){
        SwerveModuleState[] states = new SwerveModuleState[4];
        for(SwerveModule mod : mSwerveMods){
            states[mod.moduleNumber] = mod.getState();
        }
        return states;
    }

    public SwerveModulePosition[] getModulePositions(){
        SwerveModulePosition[] positions = new SwerveModulePosition[4];
        for(SwerveModule mod : mSwerveMods){
            positions[mod.moduleNumber] = mod.getPosition();
        }
        return positions;
    }

    public void zeroGyro(){
        gyro.setYaw(0);
    }

    public double getGyroAngle(){
        return Math.abs(gyro.getYaw() % 360);
    }

    public double getPitchAngle() {
        return gyro.getRoll() + 1.31;
    }

    public Rotation2d getYaw() {
        return (Constants.Swerve.invertGyro) ? Rotation2d.fromDegrees(360 - gyro.getYaw()) : Rotation2d.fromDegrees(gyro.getYaw());
    }

    public void resetModulesToAbsolute(){
        for(SwerveModule mod : mSwerveMods){
            mod.resetToAbsolute();
        }
    }


    // public void update() {
    //     if(mVision.targetVisible(LimelightState.leftLimelight) && mVision.targetVisible(LimelightState.rightLimelight)) {
    //         v1.set(mVision.getPosition(LimelightState.leftLimelight).x, mVision.getPosition(LimelightState.leftLimelight).y);
    //         v2.set(mVision.getPosition(LimelightState.rightLimelight).x, mVision.getPosition(LimelightState.rightLimelight).y);
    //         current.set(getPose().getX(), getPose().getY()); 

    //         if(Utils.withinRange(v1, current) && Utils.withinRange(v2, current)) {
    //             Vector2D meanV = new Vector2D((v1.x + v2.x) / 2, (v1.y + v2.y) / 2);
    //             updateWithLimelight(meanV);
    //         } else if(Utils.withinRange(v1, current)) { 
    //             updateWithLimelight(v1);
    //         } else if(Utils.withinRange(v2, current)) {
    //             updateWithLimelight(v2);
    //         } else {
    //             updateOdometry();
    //         }

    //     } else if(mVision.targetVisible(LimelightState.leftLimelight)) {
    //         v1.set(mVision.getPosition(LimelightState.leftLimelight).x, mVision.getPosition(LimelightState.leftLimelight).y);
    //         current.set(getPose().getX(), getPose().getY());
            
    //         if(Utils.withinRange(v1, current)) {
    //             updateWithLimelight(v1);
    //         } else {
    //             updateOdometry();
    //         }
    //     } else if(mVision.targetVisible(LimelightState.rightLimelight)) {
    //         v2.set(mVision.getPosition(LimelightState.rightLimelight).x, mVision.getPosition(LimelightState.rightLimelight).y);
    //         current.set(getPose().getX(), getPose().getY());

    //         if(Utils.withinRange(v2, current)) {
    //             updateWithLimelight(v2);
    //         } else {
    //             updateOdometry();
    //         }
    //     } else {
    //         updateOdometry();
    //     }
    // }

    public void update() {
        updateOdometry();
    }

    public void updateOdometry() {
        swerveOdometry.update(getYaw(), getModulePositions()); 
    }

    public void updateWithLimelight(Vector2D target) {
        swerveOdometry.setPoseMeters(new Pose2d(target.x, target.y, getPose().getRotation()));
    }

    @Override
    public void periodic(){
        update();
        for(SwerveModule mod : mSwerveMods){
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Cancoder", mod.getCanCoder().getDegrees());
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Integrated", mod.getPosition().angle.getDegrees());
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Velocity", mod.getState().speedMetersPerSecond);    
        }
    }
}