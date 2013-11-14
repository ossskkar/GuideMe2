package com.nctu.guideme;

public class FindOrientation {

	private String text;	
	
	/* Constructor */
	public FindOrientation(){
	}

	/* Function to find the closest orientation direction, that is LEFT or RIGHT */
	public String Find(int currentAngle, int correctAngle){
		
		/* Variables to control boundaries */
		int leftGap, rightGap;
		
		/* Calculate gaps */		
		if (currentAngle>correctAngle){
			leftGap=currentAngle-correctAngle;
			rightGap=(359-currentAngle)+correctAngle;
		}
		else { 
			leftGap=(359-correctAngle)+currentAngle;
			rightGap=correctAngle-currentAngle;
		}

		/* Find shortest gap */
		if (leftGap<=rightGap) 
			text="left";
		else 
			text="right";

		return text;
	}
}
