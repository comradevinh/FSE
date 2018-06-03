package application;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.*; //to calculate the trigonometry; for the ball relecting off surfaces
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import java.awt.geom.RectangularShape; //used to make the blocks & paddle
import java.io.File;
import javax.sound.sampled.*; //will be used for sound
import java.awt.geom.Rectangle2D;

public class GameMenu extends JFrame implements ActionListener{
	
	GamePanel menuScreen;
	private MenuButton[] buttons= new MenuButton[3];
	private String names;  // used for CardLayout names
	
	public GameMenu(String name) { //constructor
		super("Massey Quest!");
		
		ImageIcon title;
		
		menuScreen=new GamePanel();//creating the panel
		menuScreen.setLayout(null); //setting the display
		menuScreen.setLocation(0,0);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menuScreen.setSize(800,600);
		
		Timer MenuTimer; //timer that will be used to time everything
		this.names=name;
		
    	for (int i=0;i<buttons.length;i++) { //making buttons en masse
    		buttons[i]= new MenuButton(new Dimension(500,75),new Point(50,300+100*i),this);
    	}
		for (MenuButton button: buttons) {
			menuScreen.add(button);
		}

		// Setup title image
		JLabel logo = new JLabel(new ImageIcon("Images/Massey Quest Logo"));
		menuScreen.add(logo);
		
		add(menuScreen);
		
		setVisible(true);
		
		MenuTimer= new Timer(15,this); //timer value could be changed later
		MenuTimer.start();
	}
	
    public void actionPerformed(ActionEvent evt){

		if (menuScreen!=null){
			//menuScreen.control();
			repaint();
			menuScreen.repaint();
		}


    }
    
    public static void main(String[] args) {
    	GameMenu gamerun = new GameMenu("IDK");
    }

}

class GamePanel extends JPanel{
	
	public GamePanel() {
		setSize(800,600);
	}
	
	public void paintComponent(Graphics g){

		//System.out.println("Painting");
		super.paintComponent(g);
		
		Image logo= new ImageIcon("Images/Massey Quest Logo.png").getImage();
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 800, 600);
		g.drawImage(logo,10,0,this);
	}
	
}

class MenuButton extends JButton{
	private Dimension dimension;
	private Point position;
	private String Name;
	
	public MenuButton(Dimension measurements, Point point, ActionListener makeItWorkLol) {
		this.dimension=measurements;
		this.position= point;		
		this.addActionListener(makeItWorkLol); //MAKES THE BUTTONS WORK YAY
		
		//setting up the graphics
		
		ImageIcon normalButton;
		ImageIcon hoverButton;
		ImageIcon pressButton;
		
		//this.setBorderPainted(false);
		//this.setBorder(null);
		//this.setMargin(new Insets(0, 0, 0, 0));
		//this.setContentAreaFilled(false);
		
		pressButton=new ImageIcon(new ImageIcon(String.format("Images/Press Button.png")).getImage());
		normalButton=new ImageIcon(new ImageIcon(String.format("Images/Normal Button.png")).getImage());
		hoverButton=new ImageIcon(new ImageIcon(String.format("Images/Hover Button.png")).getImage());
		
		this.setIcon(normalButton); //making it so that the buttons look different in different situations
		this.setRolloverIcon(hoverButton);
		this.setPressedIcon(pressButton);
		
	}
}
