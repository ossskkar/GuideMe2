package com.nctu.guideme;

public class NumberToText {

	private String text;
	
	/* Constructor */
	public NumberToText(){
	}

	/* Convert number to text */
	public String Convert(int number){

		text="";
		
		if (number==1)
			text="one";
		else if (number==2)
			text="two";
		else if (number==3)
			text="three";
		else if (number==4)
			text="four";
		else if (number==5)
			text="five";
		else if (number==6)
			text="six";
		else if (number==7)
			text="seven";
		else if (number==8)
			text="eight";
		else if (number==9)
			text="nine";
		else if (number==10)
			text="ten";
		else if (number==11)
			text="eleven";
		else if (number==12)
			text="twelve";
		else if (number==13)
			text="thirdteen";
		else if (number==14)
			text="fourteen";
		else if (number==15)
			text="fifteen";
		else if (number==16)
			text="sixteen";
		else if (number==17)
			text="seventeen";
		else if (number==18)
			text="eighteen";
		else if (number==19)
			text="nineteen";
		else if (number==20)
			text="twenty";
		
		return text;
	}
}
