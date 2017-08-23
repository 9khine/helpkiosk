package com.prototype.helpkiosk.instruction;

public class Phone {

	private Instruction[] phoneInstruction = new Instruction[4];


	public Phone(){
		populateInstruction("makingCalls");
	}

	private void populateInstruction(String input) {
		if (input.equals("makingCalls")) {
			makingCallInstruction();
		} else if (input.equals("receiveCalls")) {
			receivingCallInstruction();
		}
	}

	private void makingCallInstruction() {
		phoneInstruction[0] = new Instruction
				("<html>Tap Phone on the Apps screen</html>", 
						false, new int[]{0}, "makingCalls", 0, true);

		phoneInstruction[1] = new Instruction
				// TODO: replace image with keypad image
				("<html>Enter the phone number. If the keys does not appear on the screen, "
						+ "tap <img src=\"http://i.imgur.com/bkvC2B6.png\" width=\"40\" height=\"40\"> top open the keypad.</html>", 
						false, new int[]{0}, "makingCalls", 1, true);

		phoneInstruction[2] = new Instruction("<html>Tap <img src=\"http://i.imgur.com/bkvC2B6.png\" width=\"40\" height=\"40\"> "
				+ "to make a voice call, or tap <img src=\"http://i.imgur.com/bkvC2B6.png\" width=\"40\" height=\"40\"> "
				+ "to make a video call.</html>", 
				true, new int[]{1,11,12,13,2}, "makingCalls", 2, false);
	}

	private void receivingCallInstruction() {
		
		// TODO
		phoneInstruction[0] = new Instruction
				("<html>Tap Phone on the Apps screen</html>", 
						false, new int[]{0}, "receiveCalls", 0, true);

		phoneInstruction[1] = new Instruction
				// TODO: replace image with keypad image
				("<html>Enter the phone number. If the keys does not appear on the screen, "
						+ "tap <img src=\"http://i.imgur.com/bkvC2B6.png\" width=\"40\" height=\"40\"> top open the keypad.</html>", 
						false, new int[]{0}, "receiveCalls", 1, true);

		phoneInstruction[2] = new Instruction("<html>Tap <img src=\"http://i.imgur.com/bkvC2B6.png\" width=\"40\" height=\"40\"> "
				+ "to make a voice call, or tap <img src=\"http://i.imgur.com/bkvC2B6.png\" width=\"40\" height=\"40\"> "
				+ "to make a video call.</html>", 
				true, new int[]{1,11,12,13,2}, "receiveCalls", 2, false);
	}

	public Instruction[] getInstruction(){
		return phoneInstruction;
	}



}
