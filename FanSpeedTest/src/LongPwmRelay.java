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
	
	/**
	 * One 220V cycle at 50hz lasts 20ms
	 * If we turn motor on during 40ms and stops during 60ms, it will result in 2 cycles ON, and 3 cycles OFF, so 40% power.
	 * 
	 * @param on	the time in msec to turn motor on
	 * @param off	the time in msec to turn motor off
	 */
	public void setLongPWM(int on, int off, int mult) {
		this.setPower(100);	// this is the "short" PWM, or high-frequency, which we do not use.
		
		duration_on = on*mult;
		duration_off = off*mult;
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
