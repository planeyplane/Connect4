import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class Game implements Runnable {
	public static final Color LBLUE = new Color(135,206,235);
    public static final Color DBLUE = new Color(0,0,205);
    public static Set<String> possibleColors;
    public static Color firstColor;
    public static Color secondColor;
    
	@Override
	public void run() {
		possibleColors = new HashSet<String>();
		possibleColors.add("Dark Blue");
		possibleColors.add("Light Blue");
		possibleColors.add("Red");
		possibleColors.add("Yellow");
		
		
        final JFrame frame = new JFrame("Connect4");
        frame.setLayout(new BorderLayout());
        frame.setLocation(300, 150);

        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);
        
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        firstColor = LBLUE;
        secondColor = DBLUE;
       
        Object[] gameType = { "Single Player", "Two Player", "SPECIAL" };
    	int choice = JOptionPane.showOptionDialog(null, "Which kind of game do you want",
    			"Game Type", JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION,
    	null, gameType, gameType[0]);
    	
    	
    	final Board board;
    	
    	if (choice == 0) {
    		board = new BoardAI(4, 7, 8, firstColor, secondColor, status);
    	} else if (choice == 1) {
    		board = new NormalBoard(4, 7, 8, firstColor, secondColor, status);
    	} else {
    		board = new SpecialBoard(4, 7, 8, firstColor, secondColor, status);
    	}
        
        frame.add(board, BorderLayout.CENTER);

        final JButton resetButton = new JButton("New Game");
        final JButton instructionsButton = new JButton("Instruction");
        final JButton colorButton = new JButton("Choose Colors");
        
        final JButton nicknameButtonA = new JButton("Player A Nickname");
        final JButton nicknameButtonB = new JButton("Player B Nickname");
        
        final JButton save = new JButton("Save");
        final JButton load = new JButton("Load");
        
        
        final JPanel helperToolbar = new JPanel();
        final JPanel toolbar = new JPanel();
        helperToolbar.add(toolbar);
        toolbar.add(resetButton);
        toolbar.add(Box.createRigidArea(new Dimension(0,50)));
        toolbar.add(instructionsButton);
        toolbar.add(Box.createRigidArea(new Dimension(0,50)));
        toolbar.add(colorButton);
        toolbar.add(Box.createRigidArea(new Dimension(0,50)));
        
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.PAGE_AXIS));
        toolbar.setAlignmentX(Component.CENTER_ALIGNMENT);
        toolbar.add(nicknameButtonA);
        toolbar.add(Box.createRigidArea(new Dimension(0,50)));
        toolbar.add(nicknameButtonB);
        toolbar.add(Box.createRigidArea(new Dimension(0,50)));
        
        toolbar.add(save);
        toolbar.add(load);
        
        frame.add(helperToolbar, BorderLayout.EAST);

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	board.reset();
            }
        });
        
        instructionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	JOptionPane.showMessageDialog(frame, 
            			"Just play Connect 4! If a person gets 4 in a row. \n then they win!"
            			+ "When choosing a nickname, if you choose one that already exists"
            			+ " your old nickname will be kept.\n\n"
            			+ "*SPECIAL ONLY* If you press enter, there is a chance it randomly puts"
            			+ "a piece out of play");
            	
            }
        });
        
        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	possibleColors.add(board.getFirstColor());
            	possibleColors.add(board.getSecondColor());
            	board.setPieceColors(getColor("Player 1"), getColor("Player 2"));
            }
        });
        
        nicknameButtonA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String nickA = JOptionPane.showInputDialog(frame, 
                        "Please enter the number of nickname", 
                        "Nickname A", JOptionPane.PLAIN_MESSAGE);
            	board.setNickA(nickA);
            }
        });
        
        nicknameButtonB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String nickB = JOptionPane.showInputDialog(frame, 
                        "Please enter the number of nickname", 
                        "Nickname B", JOptionPane.PLAIN_MESSAGE);
            	board.setNickB(nickB);
            }
        });
        
        
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	board.save();
            }
        });
        
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	board.load();
            }
        });
        
        
        
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        

        // Start game
        board.reset();
    }
	
	public Color getColor(String nickname) {
    	Object[] colors = possibleColors.toArray();
    	String selectedColor = (String) JOptionPane.showInputDialog(null,
    	"Choose a color " + nickname, "Input",
    	JOptionPane.INFORMATION_MESSAGE, null,
    	colors, colors[0]);
    	possibleColors.remove(selectedColor);
    	
    	switch (selectedColor) {  
        case "Dark Blue":
        	return DBLUE;
        case "Light Blue":
        	return LBLUE;
        case "Red":
        	return Color.RED;
        case "Yellow":
        	return Color.YELLOW;
        default:
        	return Color.WHITE;
        
        }
    	
    }

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Game());
	}

}
