package com.sos.dailyschedule.job;

public class TEST {

	public String boolExp;
	
	  public void replace(String s1, String s2) {
			 boolExp = " " + boolExp + " ";
			 s1 = " " + s1 + " ";
			 s2 = " " + s2 + " ";
		     boolExp = boolExp.replaceAll(s1, s2);
		     boolExp = boolExp.trim();
		     System.out.println(boolExp);
		   }
	  
	public static void main(String[] args) {
		TEST t = new TEST();
		t.boolExp = "A and A.1";

		t.replace("A", "true");
		t.replace("A.1", "true");
		

	}

}
