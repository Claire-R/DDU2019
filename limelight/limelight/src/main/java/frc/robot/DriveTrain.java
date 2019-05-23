/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.ControlMode;


public class DriveTrain extends Subsystem {
  WPI_TalonSRX leftmaster = new WPI_TalonSRX(0);
  WPI_TalonSRX rightmaster = new WPI_TalonSRX(1);
 

  WPI_VictorSPX left_1 = new WPI_VictorSPX(2);
  WPI_VictorSPX left_2 = new WPI_VictorSPX(3);
  WPI_VictorSPX right_1 = new WPI_VictorSPX(4);
  WPI_VictorSPX right_2 = new WPI_VictorSPX(5);

  StringBuilder sbuild = new StringBuilder();
  Joystick joy = new Joystick(0);
  int _smoothing = 0;

  @Override
  public void initDefaultCommand() {

     left_1.follow(leftmaster);
     left_2.follow(leftmaster);
     right_1.follow(rightmaster);
     right_2.follow(rightmaster);

     leftmaster.configFactoryDefault();
     left_1.configFactoryDefault();
     left_2.configFactoryDefault();
     rightmaster.configFactoryDefault();
     right_1.configFactoryDefault();
     right_2.configFactoryDefault();

     leftmaster.setSensorPhase(true);
     rightmaster.setSensorPhase(true);

     leftmaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.kPIDLoopIDX, Constants.kTimeoutMS);


     leftmaster.setInverted(false);
     rightmaster.setInverted(true);
     left_1.setInverted(false);
     left_2.setInverted(false);
     right_1.setInverted(true);
     right_2.setInverted(true);


     leftmaster.setStatusFramePeriod(10, Constants.kTimeoutMS);
     rightmaster.setStatusFramePeriod(10, Constants.kTimeoutMS);

     leftmaster.configNominalOutputForward(0, Constants.kTimeoutMS);
     rightmaster.configNominalOutputForward(0, Constants.kTimeoutMS);
     leftmaster.configNominalOutputReverse(0, Constants.kTimeoutMS);
     rightmaster.configNominalOutputReverse(0, Constants.kTimeoutMS);

     leftmaster.configPeakOutputForward(1, Constants.kTimeoutMS);
     rightmaster.configPeakOutputForward(1, Constants.kTimeoutMS);
     leftmaster.configPeakOutputReverse(-1, Constants.kTimeoutMS);
     rightmaster.configPeakOutputReverse(-1, Constants.kTimeoutMS);

     leftmaster.selectProfileSlot(Constants.kSlotIdx, Constants.kPIDLoopIDX);
     rightmaster.selectProfileSlot(Constants.kSlotIdx, Constants.kPIDLoopIDX);

     leftmaster.config_kF(Constants.kSlotIdx, Constants.kGains.kF, Constants.kTimeoutMS);
     leftmaster.config_kP(Constants.kSlotIdx, Constants.kGains.kP, Constants.kTimeoutMS);
     leftmaster.config_kI(Constants.kSlotIdx, Constants.kGains.kI, Constants.kTimeoutMS);
     leftmaster.config_kD(Constants.kSlotIdx, Constants.kGains.kD, Constants.kTimeoutMS);
     
     rightmaster.config_kF(Constants.kSlotIdx, Constants.kGains.kF, Constants.kTimeoutMS);
     rightmaster.config_kP(Constants.kSlotIdx, Constants.kGains.kP, Constants.kTimeoutMS);
     rightmaster.config_kI(Constants.kSlotIdx, Constants.kGains.kI, Constants.kTimeoutMS);
     rightmaster.config_kD(Constants.kSlotIdx, Constants.kGains.kD, Constants.kTimeoutMS);
     
      leftmaster.configMotionCruiseVelocity(15000, Constants.kTimeoutMS);
      rightmaster.configMotionCruiseVelocity(15000, Constants.kTimeoutMS);
      leftmaster.configMotionAcceleration(6000, Constants.kTimeoutMS);
      rightmaster.configMotionAcceleration(6000,Constants.kTimeoutMS);

      leftmaster.setSelectedSensorPosition(0,Constants.kPIDLoopIDX, Constants.kTimeoutMS);
      rightmaster.setSelectedSensorPosition(0,Constants.kPIDLoopIDX, Constants.kTimeoutMS);
      
  }

  public void stringbuild(){
    double leftYstick = -1.0 * joy.getY();

		if (Math.abs(leftYstick) < 0.10) { leftYstick = 0;} /* deadband 10% */



		/* Get current Talon SRX motor output */

		double motorOutput = leftmaster.getMotorOutputPercent();//get the out put of the motors



		/* Prepare line to print */

		sbuild.append("\tOut%:");

		sbuild.append(motorOutput);

		sbuild.append("\tVel:");

		sbuild.append(leftmaster.getSelectedSensorVelocity(Constants.kPIDLoopIDX));



		/**

		 * Peform Motion Magic when Button 1 is held,

		 * else run Percent Output, which can be used to confirm hardware setup.

		 */

		if (joy.getRawButton(1)) {
              // if joy stick button 1 is pressed

			/* Motion Magic */ 

			

			/*4096 ticks/rev * 10 Rotations in either direction */
// the target postion is 10 rotations (10 *4096) times by the feedback from the joystick
			double targetPos = leftYstick * 4096 * 10.0;

			leftmaster.set(ControlMode.MotionMagic, targetPos); // control mode is now motion magic (not diffdrive i guess)



			/* Append more signals to print when in speed mode */

			sbuild.append("\terr:"); // add to the string - some sort of command i guess?

			sbuild.append(leftmaster.getClosedLoopError(Constants.kPIDLoopIDX)); // get the error of the PIDloopIDX

			sbuild.append("\ttrg:"); // same thing as before but like different command?

			sbuild.append(targetPos); // add to the string - add the current target pos 

		} else {

			/* Percent Output */



			leftmaster.set(ControlMode.PercentOutput, leftYstick); // set the speed of the 

		}

		if(joy.getRawButton(2))

		{

			/* Clear sensor positions */

			leftmaster.getSensorCollection().setQuadraturePosition(0, 0);



			System.out.println("Voltage is: " + leftmaster.getBusVoltage());

		}



		if(joy.getRawButtonPressed(5))

		{

			/* Decrease smoothing */

			_smoothing--; // decrement the smoothing by 1 

			if(_smoothing < 0) _smoothing = 0;

		//	leftmaster.configMotionSCurveStrength(_smoothing);



			System.out.println("Smoothing is set to: " + _smoothing);// print what the smoothing  is 

		}

		if(joy.getRawButtonPressed(6))

		{

			/* Increase smoothing */

			_smoothing++;// increment the smoothing by 1

			if(_smoothing > 8) _smoothing = 8; // if the smoothing is larger than 8, set it back to 8 

		//	leftmaster.configMotionSCurveStrength(_smoothing);

			

			System.out.println("Smoothing is set to: " + _smoothing);// print what the smoothing is set to

		}
	/* Instrumentation */

   // Instrum.Process(leftmaster, sbuild);// 'intrumentation  provides the ability to add byte-code to existing compiled Java classes.'
                                         // i guess this is just allowing commands that normally wouldn't be a part of this code type ¯\_(ツ)_/¯  
// 
	}
}