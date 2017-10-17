  import java.util.*;
  
  // Person object. Each person in the algorithms have a preference list and who they're engaged to
  public class Person {
  
  //---------------Methods-----------------//
	int[] preferences; // list of preferences
	int engagedTo; // who they're engaged to ( -1 is no one)


	// constructor
	public Person(int pairs){

		preferences = new int[pairs];
		engagedTo = -1;
		for (int i = 0; i < pairs; i++){
			preferences[i] = -1;
		}
	}

	// ***** get and set methods ****
	public int getPartner(){
		return this.engagedTo;
	}

	public void setPartner(int newPartner){
		this.engagedTo = newPartner;
	}

	public int[] getPreferences(){
		return this.preferences;
	}

	public void setPreferences(int[] newPrefs){
		this.preferences = newPrefs;
	}
}