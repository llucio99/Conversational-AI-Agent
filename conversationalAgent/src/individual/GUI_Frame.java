package individual;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class GUI_Frame extends JFrame {
	private GUI_Panel panel1;		//declare panel object
	
	public GUI_Frame() {
		panel1 = new GUI_Panel();
		frameSetup();
	}
	
	public GUI_Panel getPanel1() {
		return panel1;
	}

	private void frameSetup() {
		this.setContentPane(panel1);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//exit on close
		this.setSize(820, 340);			//dimensions of the frame window
		this.setVisible(true);
	}
}
