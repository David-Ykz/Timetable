package timetableProgram;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * ChoiceFrame.Java
 * UI Window which allows user to choose specific actions to perform
 * @authors Anthony Tecsa, Brian Zhang
 * ICS4UE
 * @version 1.0, January 25 2022
 */
public class ChoiceFrame extends JFrame {

	final private Color menuBarColor = new Color(33,46,21);
	final private Color buttonColor = new Color(46,64,29);
	private int studentNumber;
	private BufferedImage logo;
	private BufferedImage student;
	JLabel l = new JLabel("RHHS");
	JLabel l2 = new JLabel("Enter indvidual student number");
	JButton b = new JButton("Back");
	JButton masterTButton = new JButton("Master Schedule");
	JButton individualTButton = new JButton("Indv. Timetable");
	JButton allTButton = new JButton("All Timetable's");
	JTextField t = new JTextField("");


	public ChoiceFrame() {

		setTitle("Timetable System");
		setSize(1250, 750);  
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		loadImages();

		GraphicsPanel panel = new GraphicsPanel();
		panel.setLayout(null);

		this.setResizable(false);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.requestFocusInWindow();


		b.setBackground(buttonColor);
		b.setForeground(Color.WHITE);
		b.setFocusPainted(false);
		b.setFont(new Font("Tahoma", Font.BOLD, 12));
		b.addActionListener(new BackButtonListener());
		b.setBounds(10,605,150,100); 

		individualTButton.setBackground(buttonColor);
		individualTButton.setForeground(Color.WHITE);
		individualTButton.setFocusPainted(false);
		individualTButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		individualTButton.addActionListener(new individualTButtonListener());
		individualTButton.setBounds(1000,125,150,100); 

		masterTButton.setBackground(buttonColor);
		masterTButton.setForeground(Color.WHITE);
		masterTButton.setFocusPainted(false);
		masterTButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		masterTButton.addActionListener(new masterTButtonListener());
		masterTButton.setBounds(1000,290,150,100); 

		allTButton.setBackground(buttonColor);
		allTButton.setForeground(Color.WHITE);
		allTButton.setFocusPainted(false);
		allTButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		allTButton.addActionListener(new allTButtonListener());
		allTButton.setBounds(1000,455,150,100); 

		l.setBounds(100,-25,200,150);
		l.setForeground(Color.WHITE);
		l.setFont(new Font("Britannic Bold", Font.BOLD, 30));


		l2.setBounds(795,115,200,50);
		t.setBounds(810,165,150,50);

		panel.add(l2);
		panel.add(t);
		panel.add(b);
		panel.add(masterTButton);
		panel.add(individualTButton);
		panel.add(allTButton);

		panel.add(l);
		getContentPane().add(panel);

	}

	public class BackButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			new MenuFrame();
			dispose();
		}
	}
	public class masterTButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Main.saveMasterTimetable();

			final JOptionPane pane = new JOptionPane("Master Timetable Saved");
			final JDialog d = pane.createDialog((JFrame )null, "Timetable System");
			d.setSize(200, 125);
			d.setLocationRelativeTo(null);
			d.setVisible(true);
		}
	}
	public class individualTButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			studentNumber = Integer.parseInt(t.getText());
			Main.saveTimetableFromNumber(studentNumber);
			t.setText("");

			final JOptionPane pane = new JOptionPane("      Student Number Read\n                "+studentNumber);
			final JDialog d = pane.createDialog((JFrame) null, "Timetable System");
			d.setSize(200, 125);
			d.setLocationRelativeTo(null);
			d.setVisible(true);
		}
	}
	public class allTButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Main.saveStudentTimetables();

			final JOptionPane pane = new JOptionPane("Student Timetables Saved");
			final JDialog d = pane.createDialog((JFrame )null, "Timetable System");
			d.setSize(200, 125);
			d.setLocationRelativeTo(null);
			d.setVisible(true);
		}
	}

	public void loadImages() {
		try {
			logo = ImageIO.read(new File("C:\\Users\\hocke\\Downloads\\Timetable-main (2)\\Timetable-main\\logo3.png"));
			student = ImageIO.read(new File("C:\\Users\\hocke\\Downloads\\Timetable-main (2)\\Timetable-main\\student.jpg"));
		} catch (Exception ex){} 
	}

	public class GraphicsPanel extends JPanel {

		/**
		 * Constructs the graphical panel for the user to use
		 */
		public GraphicsPanel() {
			setPreferredSize(new Dimension(1250, 850));
			setFocusable(true);
			requestFocusInWindow();
			this.addMouseListener(new MyMouseListener());
			this.addMouseMotionListener(new MyMouseListener());
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
			g.setColor(menuBarColor);
			g.fillRect(0, 600, 1250, 150);
			g.fillRect(0, 0, 1250, 100);
			g.fillRect(80,180,540,340);
			g.setColor(new Color(242, 195, 150));
			g.fillRect(700, 100, 550, 500);

			g.drawImage(logo,0,0,100,100,null);
			g.drawImage(student,100,200,500,300,null);

		}
	}



	private static class MyMouseListener implements MouseListener, MouseMotionListener {
		public void mouseClicked(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		/**
		 * mousePressed
		 * This method is invoked when the mouse button has been pressed on a component
		 * @param e, MouseEvent indicates that an action occurred
		 */
		public void mousePressed(MouseEvent e) {
		}

		/**
		 * mouseReleased
		 * This method is invoked when the mouse button has been released 
		 * @param e, MouseEvent indicates that an action occurred
		 */
		public void mouseReleased(MouseEvent e) {
		}

		/**
		 * mouseDragged
		 * This method is invoked when a mouse button is pressed on a component and then dragged
		 * @param e, MouseEvent indicates that an action occurred
		 */
		public void mouseDragged(MouseEvent e) {
		}

		public void mouseMoved(MouseEvent e) {
		}
	}

}
