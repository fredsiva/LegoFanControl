# LegoFanControl
Experiment the control of a tri-phase electrical motor using a Triac and "slow-PWM" 

First tests will be done without any clue about the timing of the domestic 50hz input current, results are good enough, demonstrating there is no need to sync with the 50Hz input.

The Lego EV3 will send output 5V to the triac during a short period of time, and then wait for another period.  The Triac will only react if it senses 5V at the time the 220V is an "upward zero", and it will let one full wave of 220 go through (lasting 1/50s).
