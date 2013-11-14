package com.nctu.guideme;

public class DegreeToText extends BaseActivity{

	private String text;
	
	/* Constructor */
	public DegreeToText(){
	}
	
	public String Convert(int degree){

		text="";
		
		if ((degree>350) || (degree<10))
			text="N";
		else if ((degree>=10) && (degree<80))
			text="NE";
		else if ((degree>=80) && (degree<100))
			text="E";
		else if ((degree>=100) && (degree<170))
			text="SE";
		else if ((degree>=170) && (degree<190))
			text="S";
		else if ((degree>=190) && (degree<260))
			text="SW";
		else if ((degree>=260) && (degree<280))
			text="W";
		else if ((degree>=280) && (degree<350))
			text="NW";
		
		return text;
	}
}
