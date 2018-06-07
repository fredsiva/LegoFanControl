import lejos.utility.Delay;
import lejos.utility.TextMenu;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.RCXMotor;
import lejos.hardware.port.MotorPort;

public class DTestFan {
	private LongPwmRelay theMotor;
	
	public static void main(String[] args) {
		DTestFan obj = new DTestFan();

		obj.startMenu();				
	}
	

	
	public void testNormalPwm() {
		int power = 0, b; 
		RCXMotor motor = new RCXMotor(MotorPort.C);
		LCD.clear();

		LCD.drawString("Test Moteur C", 1, 1);
		LCD.drawString("Power = " + power + "  ", 1, 2);

		LCD.drawString("Enter=Power PWM", 0, 3);
		LCD.drawString("L=Run", 0,4);
		LCD.drawString("Dw=Float UP=stop", 0, 6);
		LCD.drawString("ESC = Quit", 0, 7);
		
		while(Button.getButtons() !=Button.ID_ESCAPE) {

			b = Button.readButtons();

			if (b != 0) {
				if (b==Button.ID_DOWN)
					motor.flt();
				else if (b==Button.ID_LEFT) {
					motor.backward();
				}
				else if (b==Button.ID_UP) {
					motor.stop();
				}
				else if (b==Button.ID_ENTER) {
					power += 10;
					if (power > 100)
						power = 0;

					LCD.drawString("Power = " + power + "  ", 1, 2);

					motor.setPower(power);		// Power 80 means 8/100s ON and 2/100s OFF
				}

				// wait Button is released
				// while (Button.readButtons() != 0) 	Delay.msDelay(10);
		
			}

			Delay.msDelay(10);
		}
	}
	
	public void testLongPwm() {
		int b, mult = 1;
		int power = 80;	// From 0 (=off) to 100 (Full Power)
		Thread relayThread;
		
		theMotor = new LongPwmRelay(MotorPort.C);
		theMotor.setOff();
		theMotor.setLongPWM(power, 100 - power, 1);		// Power 80 means 8/100s (=80ms) ON and 2/100s (=20ms) OFF

		relayThread = new Thread(theMotor);
		relayThread.start();
		
		LCD.clear();

		LCD.drawString("Test Moteur C", 1, 1);
		LCD.drawString("Pow=" + power + " mult=" + mult + " ", 1, 2);

		LCD.drawString("L=incr pow R=dec", 0, 4);
		LCD.drawString("Up=Run", 0,5);
		LCD.drawString("Dw=Float", 0, 6);
		LCD.drawString("ESC = Quit", 0, 7);
		
		while(Button.getButtons() !=Button.ID_ESCAPE) {

			b = Button.readButtons();

			if (b != 0) {
				if (b==Button.ID_DOWN)
					theMotor.setOff();
				else if (b==Button.ID_UP) {
					theMotor.setOn();
				}
				else if (b==Button.ID_RIGHT) {
					power += 10;
					if (power > 100)
						power = 100;

					LCD.drawString("Pow=" + power + " mult=" + mult + " ", 1, 2);
					theMotor.setLongPWM(power/10, 10-(power/10), mult);		// Power 80 means 8/100s ON and 2/100s OFF
				}
				else if (b==Button.ID_LEFT) {
					power -= 10;
					if (power < 0)
						power = 0;

					LCD.drawString("Pow=" + power + " mult=" + mult + " ", 1, 2);
					theMotor.setLongPWM(power/10, 10-(power/10), mult);		// Power 80 means 8/100s ON and 2/100s OFF
				}
				else if (b==Button.ID_ENTER) {
					mult +=1;
					if (mult > 10)
						mult = 1;

					LCD.drawString("Pow=" + power + " mult=" + mult + " ", 1, 2);
					theMotor.setLongPWM(power/10, 10-(power/10), mult);		// Power 80 means 8/100s ON and 2/100s OFF
				}

				// wait Button is released
				while (Button.readButtons() != 0) 	Delay.msDelay(10);
		
			}

			Delay.msDelay(10);
		}

		theMotor.stopThread();

		
	}	

	private int startMenu() {
		TextMenu startMenu;
		int selection = 1;
		String[] menuChoice= new String[] { "Quit", "Normal PWM", "Long PWM"};
		
		startMenu = new TextMenu(menuChoice, 1, "Test PWM");
		
		while (selection != 0) {
			LCD.clear();
			selection = startMenu.select(1);
			
			if (selection == 2)
				// Go to run() in case of timeout or if "Run" selected 
				testLongPwm();
			else if (selection == 1) {
				testNormalPwm();
			}
		}
		
		return -1;
	}
	
}
