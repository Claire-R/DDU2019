package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.DriveTrain;

public class Robot extends TimedRobot {

  public static DriveTrain drivetrain;

  @Override
  public void robotInit() {
    drivetrain = new DriveTrain();
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {

    
  }

  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

}
