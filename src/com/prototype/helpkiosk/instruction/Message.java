package com.prototype.helpkiosk.instruction;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Message {
	
	private Instruction[] messageInstruction = new Instruction[4];
	
	
	public Message(){
		populateInstruction();
	}

/*
 * TODO: update with Message instructions
 */
	private void populateInstruction() {
		ImageIcon img = new ImageIcon("img/cloudtag.png");
		JLabel tagCloud = new JLabel(img);
		messageInstruction[0] = new Instruction
				("<html>Tap Contacts <img src=\"http://i.imgur.com/XMA779I.png\" width=\"40\" height=\"40\"> on the Apps screen</html>", 
						false, new int[]{0}, "addContact", 0, true);
		
		messageInstruction[1] = new Instruction
				("<html>Tap <img src=\"http://i.imgur.com/bkvC2B6.png\" width=\"40\" height=\"40\"> and select a storage location.</html>", 
						false, new int[]{0}, "addContact", 1, true);
		
		messageInstruction[2] = new Instruction("<html>Enter contact information.</html>", 
				true, new int[]{1,11,12,13,2}, "addContact", 2, false);
		
		messageInstruction[3] = new Instruction("<html>Tap SAVE</html>", 
				true, new int[]{3,4,5,6}, "addContact", 3, false);
	}
	
	public Instruction[] getInstruction(){
		return messageInstruction;
	}
	
	
	
}
