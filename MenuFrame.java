import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * [MenuFrame.java]
 * Main menu UI of program
 * @author Anthony Tecsa, Brian Zhang
 * ICS4UE
 * @version 1.0, January 25 2022
 */

public class MenuFrame extends JFrame{

	private BufferedImage schoolImage;
	private BufferedImage logo;
	private boolean aboutButtonPressed = false;
	private String fileName = ""; 
	private final Color menuBarColor = new Color(33,46,21);
	private final Color buttonColor = new Color(46,64,29);
	JButton b = new JButton("Output");
	JButton b2 = new JButton("About Us");
    JButton b3 = new JButton("Enter");
	JLabel l = new JLabel("RICHMOND HILL HIGH SCHOOL");
	JLabel l2 = new JLabel("TIMETABLE SYSTEM");
	JLabel l3 = new JLabel("RHHS");
	JTextField t = new JTextField("Enter StudentCourseRequestFile.csv");
	
	public MenuFrame() {
		setTitle("Timetable System");
		setSize(1250, 750);  
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

		loadImages();
		GraphicsPanel panel = new GraphicsPanel();
		panel.setLayout(null);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.requestFocusInWindow();

		b.setBackground(buttonColor);
		b.setForeground(Color.WHITE);
		b.setFocusPainted(false);
		b.setFont(new Font("Tahoma", Font.BOLD, 12));
		b.addActionListener(new CalculateButtonListener());
		b.setBounds(450,605,150,100); 

		b2.setBackground(buttonColor);
		b2.setForeground(Color.WHITE);
		b2.setFocusPainted(false);
		b2.setFont(new Font("Tahoma", Font.BOLD, 12));
		b2.addActionListener(new AboutButtonListener());
		b2.setBounds(605,605,150,100); 

		b3.setBackground(Color.GRAY);
		b3.setForeground(Color.WHITE);
		b3.setFocusPainted(false);
		b3.setFont(new Font("Tahoma", Font.BOLD, 12));
		b3.addActionListener(new EnterButtonListener());
		b3.setBounds(740,420,100,35); 
		b3.setBorder(BorderFactory.createRaisedBevelBorder());
		
		l.setBounds(260,150,850,200);
		l.setForeground(Color.WHITE);
		l.setFont(new Font("Tahoma", Font.BOLD, 45));

		l2.setBounds(410,215,500,200);
		l2.setForeground(Color.WHITE);
		l2.setFont(new Font("Tahoma", Font.PLAIN, 45));

		l3.setBounds(100,-25,200,150);
		l3.setForeground(Color.WHITE);
		l3.setFont(new Font("Britannic Bold", Font.BOLD, 30));
		
		t.setBounds(390,420,350,35);
		
	    panel.add(t);
		panel.add(l);
		panel.add(l2);
		panel.add(l3);
		panel.add(b);
		panel.add(b2);
		panel.add(b3);

		getContentPane().add(panel);
		this.setVisible(true);
	}



	public void loadImages() {
		try {
			schoolImage = ImageIO.read(new File("school3.png"));
			logo = ImageIO.read(new File("logo3.png"));
		} catch (Exception ex){} 
	}

	public class CalculateButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			new ChoiceFrame();
			dispose();
		}
	}

	public class AboutButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(aboutButtonPressed) {
				aboutButtonPressed = false;
			} else {
				aboutButtonPressed = true;
			}
		}
	}
	
	public class EnterButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			fileName = t.getText();
	        t.setText("");
			
	        Main.startAlgorithm();
	        
	        final JOptionPane pane = new JOptionPane("                  File Read");
			final JDialog d = pane.createDialog((JFrame) null, "Timetable System");
			d.setSize(200, 125);
			d.setLocationRelativeTo(null);
			d.setVisible(true);
		}
	}

	public class GraphicsPanel extends JPanel {

		/**
		 * Constructs the graphical panel for the user to use
		 */
		public GraphicsPanel() {
			setPreferredSize(new Dimension(1250, 850));
			setFocusable(true);
			requestFocusInWindow();
		}
		/**
		 * paintComponent
		 * This method draws and updates the screen repeatedly
		 * @param g, Graphics for drawing
		 */
		public void paintComponent(Graphics g) { 
			super.paintComponent(g); 
			draw(g);
			repaint();
		}
		/**
		 * draw
		 * Draws the entire menu screen
		 * @param g, Graphics for drawing
		 */
		public void draw(Graphics g) {
			g.drawImage(schoolImage,0,0,1250,600,null);
			g.setColor(menuBarColor);
			g.fillRect(0, 600, 1250, 150);
			g.drawImage(logo,0,0,100,100,null);
			g.setColor(Color.WHITE);
			g.drawString("Filename: " + fileName,  400, 480);

			if(aboutButtonPressed) {
				g.setColor(Color.BLACK);
				//g.fillRect(350,500,570,50);
				g.setColor(Color.WHITE);
				g.setFont(new Font("Tahoma", Font.PLAIN, 16));
				g.drawString("Authors: Allen Liu, Anthony Tecsa, Blair Wang, Brian Zhang, David Ye",  362, 555);

			}
		}
	}

	public String getFileName() {
		return fileName;
	}

}
