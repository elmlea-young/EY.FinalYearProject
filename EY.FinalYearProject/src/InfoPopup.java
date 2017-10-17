

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JOptionPane.*;
import javax.swing.text.DefaultCaret;

// a message box window that displays information to the user upon pressing the 'info' buttons around the application

public class InfoPopup {
	private JFrame mainFrame;
	JTextArea textArea;
	JPanel textPanel;

	public InfoPopup(){
		prepareGUI();
	}

	public static void main(String[] args){
		InfoPopup mainWindow = new InfoPopup(); 
	}

	private void prepareGUI(){
		mainFrame = new JFrame("Home");
		mainFrame.setSize(800,500); // much smaller than the main window
		mainFrame.setLocationRelativeTo(null);
		mainFrame.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel closePanel = new JPanel();
		mainFrame.getContentPane().add(closePanel, BorderLayout.SOUTH);

		JButton closebtn = new JButton("Close");
		// add a close button
		closebtn.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		closebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.setVisible(false);
			}

		});
		closePanel.add(closebtn);

		textPanel = new JPanel();
		mainFrame.getContentPane().add(textPanel, BorderLayout.CENTER);
		textPanel.setLayout(new BorderLayout(0, 0));

		textArea = new JTextArea();
		textPanel.add(textArea);
		textArea.setMargin(new Insets(10, 10, 10, 10));
		textArea.setEditable(false);
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		textArea.setText("Text Goes here.....");

		JScrollPane scroller = 
				new JScrollPane(textArea, 
						JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
						JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);	


		textPanel.add(scroller);

		DefaultCaret caret = (DefaultCaret) textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent){
				mainFrame.setVisible(false);
			}        
		});  

		mainFrame.setVisible(true);  
	}

	// method called from other classes to displa the box with text that is sent as a parameter
	public void changeText(String s){
		textArea.setText(s);
	}
}