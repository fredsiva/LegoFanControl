import lejos.hardware.motor.RCXMotor;

import lejos.hardware.port.Port;
import lejos.utility.Delay;


public class LongPwmRelay extends RCXMotor implements Runnable {
	int duration_on, duration_off;
	boolean on = false;
	boolean run = true;
	
	public LongPwmRelay(Port port) {
		super(port);
	}
	
	public void setLongPWM(int on, int off) {
		this.setPower(100);
		
		duration_on = on;
		duration_off = off;
	}

	public void setOn() {
		on = true;
	}

	public void setOff() {
		on=false;
	}

	public void stopThread() {
		run = false;
	}
	
	public void run() {
		while(run == true) {
			if (on) {
				if (duration_on > 0) {
					super.backward();
					Delay.msDelay(duration_on);
				}

				if (duration_off > 0) {
					super.stop();
					Delay.msDelay(duration_off);
				}
			} else {
				super.stop();
				Delay.msDelay(10);
				
			}
		}
	}
}
