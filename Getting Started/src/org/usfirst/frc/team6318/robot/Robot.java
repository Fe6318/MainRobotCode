/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team6318.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *  */
public class Robot extends IterativeRobot {
	//speed controllers for main motors
	private SpeedController m_FrontLeftController = new Spark(0);
	private SpeedController m_RearLeftController = new Spark(1);
	private SpeedController m_FrontRightController = new Spark(2);
	private SpeedController m_RearRightController = new Spark(3);
	
	//speed controller groups
	private SpeedControllerGroup m_LeftGroup = new SpeedControllerGroup(m_FrontLeftController,m_RearLeftController);
	private SpeedControllerGroup m_RightGroup = new SpeedControllerGroup(m_FrontRightController,m_RearRightController);
	
	//drive system
	private DifferentialDrive m_robotDrive
	= new DifferentialDrive(m_LeftGroup, m_RightGroup);
	
	//other motors
	private SpeedController m_LiftMotor = new Talon(4);
	private SpeedController m_TiltMotor = new VictorSP(5);
	
	//encoders
	//private Encoder m_LiftEncoder = new Encoder(0, 1, true, Encoder.EncodingType.k4X);
	//private Encoder m_LiftEncoder = new Encoder(0, 1, true);
	private Encoder m_tiltEncoder = new Encoder(0, 1, true);
	
	//Joysticks
	private Joystick m_XBoxController = new Joystick(0);
	private Joystick m_LogitechController = new Joystick(1);
	
	//timer
	private Timer m_timer = new Timer();
	
	//pneumatics
	private Compressor m_compressor = new Compressor();
	private DoubleSolenoid m_solenoid = new DoubleSolenoid(0,1);
	
	//gyro
	public ADXRS450_Gyro m_Gyro = new ADXRS450_Gyro();
	static float kP = (float) 0.23;
	
	//smart dashboard
	SmartDashboard m_SmartDashboard;
	
	//select program
	//SendableChooser<String> autoChooser;
	
	int step = 0;
	/**
	 * This function is ran when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		//we need to set all these to inverted so it actually goes forward
		//m_FrontLeftController.setInverted(true);
		//m_RearLeftController.setInverted(true);
		//m_FrontRightController.setInverted(true);
		//m_RearRightController.setInverted(true);
		
		//set up the encoder for the lift motor
		//m_LiftEncoder.setMaxPeriod(.1);
		//m_LiftEncoder.setMinRate(10);
		//m_LiftEncoder.setDistancePerPulse(7);
		//m_LiftEncoder.setReverseDirection(true);
		//m_LiftEncoder.setSamplesToAverage(7);
		
		//m_LiftEncoder.setReverseDirection(true);
		
		
		//set up tilt counter
		//m_TiltTrigger.setLimitsRaw(2048, 3200);
		//m_TiltTrigger.setFiltered(true);
		//m_TiltCounter.setSemiPeriodMode(false);
		//m_TiltCounter.setReverseDirection(false);
		
		//m_TiltCounter.set		
		//setting up compressor
		//m_compressor.stop();
		m_compressor.start();
		m_compressor.setClosedLoopControl(true);
		
		//set up solenoid
		m_solenoid.set(DoubleSolenoid.Value.kReverse);
		
		m_tiltEncoder.reset();
		
		m_robotDrive.setSafetyEnabled(true);
		m_robotDrive.stopMotor();
		
		//smart dashboard
		m_SmartDashboard = new SmartDashboard();
		
		m_SmartDashboard.putString("Chosen Program", "L");
		
		//set up program selector
		//autoChooser = new SendableChooser<String>();
		//autoChooser.setName("Auto Chooser");
		//autoChooser.addDefault("L", "L");
		//autoChooser.addObject("C", "C");
		//autoChooser.addObject("R","R");
		//autoChooser.addObject("Auto line only","Auto line only");
		
		
	}

	/**
	 * This function runs once each time the robot enters autonomous mode.
	 */
	@Override
	public void autonomousInit() {
		m_solenoid.set(DoubleSolenoid.Value.kReverse);
		m_timer.reset();
		m_timer.start();
		step = 1;
		m_Gyro.reset();
		m_robotDrive.stopMotor();
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		
		Scheduler.getInstance().run();
		
		double angle = m_Gyro.getAngle();
		String gameData;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		String chosenProgram = m_SmartDashboard.getString("Chosen Program", "L").toUpperCase();
		System.out.println("Program Chosen: " + chosenProgram);
		System.out.println("Game Data: " + gameData);
		//System.out.print(m_timer.get());
		//System.out.print(" ");
		//System.out.print(step);
		//System.out.print(" ");
		
		//System.out.println("Encoder: " + m_tiltEncoder.get());
		
		if(step == 0) {
			autonomousInit();
		}
		
		
		if(gameData.length() > 0) {
			if(chosenProgram.equals("L")) {
				if(gameData.charAt(0) == 'L') {
					
					if(m_timer.get() >= 2.3 && step == 1) {
						step = 2;
						//m_LiftMotor.stopMotor();
						m_timer.stop();
						m_timer.reset();
					}
					
					if(m_Gyro.getAngle() > 79 && step == 2) {
						//m_robotDrive.arcadeDrive(0.01, -(angle - 90) * 0.001);
						m_robotDrive.stopMotor();
						Timer.delay(0.5);
						
						m_LiftMotor.stopMotor();
						
						m_Gyro.reset();
						step = 3;
						m_timer.start();
					}
					
					if(m_timer.get() > 1 && step == 3) {
						m_timer.stop();
						m_timer.reset();
						m_TiltMotor.stopMotor();
						m_robotDrive.stopMotor();
						step = 4;
					}
					
					if(m_timer.get() > 0.3 && step == 4) {
						m_TiltMotor.stopMotor();
						m_robotDrive.stopMotor();
						
						step = 5;
					}
					
					if(m_tiltEncoder.get() > -120) {
						m_TiltMotor.set(0.75);
					} else {
						m_TiltMotor.stopMotor();
					}
					//if (step == 1) {
					//	  m_robotDrive.arcadeDrive(0.75, -angle * kP); 
					//} else if(step == 2) {
					//		m_robotDrive.arcadeDrive(0.01,0.5);
					//} else if(step == 3) { 
					//	m_robotDrive.arcadeDrive(0.6, -angle * kP);
					//} else {
					//	m_robotDrive.stopMotor(); // stop robot
					//}
					
					switch(step) {
						case 1:
							m_robotDrive.arcadeDrive(0.75, -angle * kP);
							m_LiftMotor.set(-1);
							break;
							
						case 2: 
							m_robotDrive.arcadeDrive(0.01,0.5);
							m_LiftMotor.set(-1);
							
							break;
							
						case 3:
							m_robotDrive.arcadeDrive(0.6, -angle * kP);
							
							break;
						case 4:
							m_solenoid.set(DoubleSolenoid.Value.kForward);
						default:
							m_LiftMotor.stopMotor();
							m_TiltMotor.stopMotor();
							m_robotDrive.stopMotor();
							break;
					}
				} else {
					if(m_timer.get() < 2.3) {
						m_robotDrive.arcadeDrive(0.75, -angle * kP);
						m_LiftMotor.set(-1);
					} else {
						m_robotDrive.stopMotor();
						m_LiftMotor.stopMotor();
					}
				}
			} else if(chosenProgram.equals("C")) {
				if(m_timer.get() < 2.3) {
					m_robotDrive.arcadeDrive(0.75, -angle * kP);
					m_LiftMotor.set(-1);
				} else {
					m_robotDrive.stopMotor();
					m_LiftMotor.stopMotor();
				}
				
			} else if(chosenProgram.equals("R")) {
				if(gameData.charAt(0) == 'R') {
					
					if(m_timer.get() >= 2.3 && step == 1) {
						step = 2;
						//m_LiftMotor.stopMotor();
						m_timer.stop();
						m_timer.reset();
					}
					
					if(m_Gyro.getAngle() < -79 && step == 2) {
						//m_robotDrive.arcadeDrive(0.01, -(angle - 90) * 0.001);
						m_robotDrive.stopMotor();
						Timer.delay(0.5);
						//m_LiftMotor.stopMotor();
						m_Gyro.reset();
						step = 3;
						m_timer.start();
					}
					
					if(m_timer.get() > 1 && step == 3) {
						m_timer.stop();
						m_timer.reset();
						m_TiltMotor.stopMotor();
						m_LiftMotor.stopMotor();
						m_robotDrive.stopMotor();
						step = 4;
					}
					
					if(m_timer.get() > 0.3 && step == 4) {
						m_TiltMotor.stopMotor();
						m_robotDrive.stopMotor();
						
						step = 5;
					}
					
					if(m_tiltEncoder.get() > -120) {
						m_TiltMotor.set(0.75);
					} else {
						m_TiltMotor.stopMotor();
					}
					
					//if (step == 1) {
					//	  m_robotDrive.arcadeDrive(0.75, -angle * kP); 
					//} else if(step == 2) {
					//		m_robotDrive.arcadeDrive(0.01,0.5);
					//} else if(step == 3) { 
					//	m_robotDrive.arcadeDrive(0.6, -angle * kP);
					//} else {
					//	m_robotDrive.stopMotor(); // stop robot
					//}
					
					switch(step) {
						case 1:
							m_robotDrive.arcadeDrive(0.75, -angle * kP);
							m_LiftMotor.set(-1);
							
							break;
							
						case 2: 
							m_robotDrive.arcadeDrive(0.01,-0.5);
							m_LiftMotor.set(-1);
							break;
							
						case 3:
							m_robotDrive.arcadeDrive(0.6, -angle * kP);
							m_LiftMotor.set(-1);
							break;
						case 4:
							m_solenoid.set(DoubleSolenoid.Value.kForward);
						default:
							m_LiftMotor.stopMotor();
							m_TiltMotor.stopMotor();
							m_robotDrive.stopMotor();
							break;
					}
				} else {
					if(m_timer.get() < 2.3) {
						m_robotDrive.arcadeDrive(0.75, -angle * kP);
						m_LiftMotor.set(-1);
					} else {
						m_robotDrive.stopMotor();
						m_LiftMotor.stopMotor();
					}
				}
			} else {
				m_robotDrive.stopMotor();
				
			}
		}
	}
	
	/**
	 * This function is called once each time the robot enters teleoperated mode.
	 */
	@Override
	public void teleopInit() {
		//m_tiltEncoder.reset();
	}
	
	/**
	 * This function is called periodically during teleoperated mode.
	 */
	@Override
	public void teleopPeriodic() {
		
		boolean Stopped = false;
		
		while(isOperatorControl() && isEnabled()) {
			
			if(m_XBoxController.getRawButton(7) && m_XBoxController.getRawButton(8)) {
				Stopped = true;
			}
			
			m_robotDrive.setExpiration(1);
			
				if(!Stopped) {

					//set the robot speed to the raw axis of the control stick
					m_robotDrive.tankDrive(-m_LogitechController.getRawAxis(1) * 0.85,-m_LogitechController.getRawAxis(5) * 0.85);
					
					//check if we need to move the lift
					if(m_XBoxController.getRawAxis(2) > 0.25 ) {
						m_LiftMotor.set(1);
					} else if(m_XBoxController.getRawAxis(3) > 0.25) {
						m_LiftMotor.set(-1);
					} else {
						m_LiftMotor.stopMotor();
					}
					
					//System.out.println(m_LiftEncoder.getRaw());
					//System.out.println("Tilt Encoder: " + m_tiltEncoder.get());
					
					//check if we need to tilt
					if(m_XBoxController.getRawButton(5)) { //down
						m_TiltMotor.set(0.75);
					} else if(m_XBoxController.getRawButton(6)) { //up
						m_TiltMotor.set(-0.75);
					}  else {
						m_TiltMotor.stopMotor();
					}
					
					
					//System.out.print("Button-1: ");
					//System.out.print(m_XBoxController.getRawButton(1));
					//System.out.print(" Sol: ");
					//System.out.println(m_solenoid.get());
					
					//check if we need to move the grabber
					if(m_XBoxController.getRawButton(1)) {
						m_solenoid.set(DoubleSolenoid.Value.kForward);
					} else if(m_XBoxController.getRawButton(2)) {
						m_solenoid.set(DoubleSolenoid.Value.kReverse);
					} else {
						m_solenoid.set(DoubleSolenoid.Value.kOff);
					}
					
				} else {
					m_robotDrive.stopMotor();
					
					m_LiftMotor.set(0);
					m_TiltMotor.set(0);
					
					m_compressor.stop();
					
					m_solenoid.set(DoubleSolenoid.Value.kForward);
				}
				
			Timer.delay(0.05);
			
			}
		}
	
	
	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
		
	}
}
