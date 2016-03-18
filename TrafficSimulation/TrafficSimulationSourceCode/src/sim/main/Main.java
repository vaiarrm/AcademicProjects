package sim.main;

import sim.ui.TextUI;
import sim.ui.UI;

public class Main {
	
	public static void main(String [] args){
		
		UI ui = new TextUI();
		Control control = new Control(ui);
		control.run();    
	}

}
