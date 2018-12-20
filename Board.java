import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.*;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact with one another. Take
 * time to understand how the timer interacts with the different methods and how it repaints the GUI
 * on every tick().
 */
@SuppressWarnings("serial")
public class Board extends JPanel {
	public boolean playing = false; // whether the game is running
	public static final String TXT_FILE = "files/savedGames.txt";
    public static final int INTERVAL = 35;
    public static int WIDTH;
    public static int HEIGHT;
    public static final int SIZE = 50;
    
    protected int[][] gameArray;
	private int activePlayer;
    private int x;
    private int lastX;
    private int y;
    private int lastY;
    
    private int pieceCount;
    private int WIN_LENGTH;
    private boolean dropping;
    private boolean fullGame;
    private Color firstColor;
    private Color secondColor;
    
    private String nickA;
    private String nickB;
    private JLabel status;
    
	
	public Board(int winNum, int width, int height, Color firstColor, Color secondColor, JLabel status) {
    	WIN_LENGTH = winNum;
		WIDTH = width;
    	HEIGHT = height;
    	this.setFirstColorTRUE(firstColor);
    	this.setSecondColorTRUE(secondColor);
    	setActivePlayer((int) (Math.random() * 2));
    	setActivePlayer(getActivePlayer() + 1);
    	
    	this.setStatus(status);
    	this.nickA = "Player A";
    	this.nickB = "Player B";
    	
		setBorder(BorderFactory.createLineBorder(Color.BLACK));

        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start();

        setFocusable(true);

        addMouseMotionListener(new MouseMotionListener() {			
			// This is the preview of the piece to be dropped
        	public void mouseMoved(MouseEvent m) {
				int temp = (int) (m.getX() / SIZE);
				if (temp != lastX && temp < gameArray.length) {
					gameArray[temp][gameArray[0].length - 1] = -getActivePlayer();
					gameArray[lastX][gameArray[0].length - 1] = 0;
					lastX = temp;
				}
				
			}

			@Override
			public void mouseDragged(MouseEvent m) {
				
			}
        	
        });
        
        
        addMouseListener(new MouseListener() {
        	@Override
			public void mouseClicked(MouseEvent m) {
				if (isDropping()) return;
				
				// Position of piece to be dropped
				setXValue((int) (m.getX() / SIZE));
				
				// Places piece on board
				if (gameArray[getXValue()][gameArray[0].length - 2] == 0) {
					gameArray[getXValue()][gameArray[0].length - 1] = getActivePlayer();
					
					if (getActivePlayer() == 1) {
						setActivePlayer(2);
					} else {
						setActivePlayer(1);
					}
					
					pieceCount++;
				}
				
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				for (int i = 0; i < gameArray.length; i++) {
					gameArray[i][gameArray[0].length - 1] = 0;
				}
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				
			}

			@Override
			public void mouseReleased(MouseEvent m) {

			}

			@Override
			public void mouseEntered(MouseEvent m) {
				int temp = Math.min(gameArray.length - 1, (int) (m.getX() / SIZE));
				gameArray[temp][gameArray[0].length - 1] = -getActivePlayer();
			}
        });
        

    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        gameArray = new int[WIDTH][HEIGHT];
        playing = true;
        getStatus().setText("Running...");
        requestFocusInWindow();
        y = 0;
        lastY = 0;
        pieceCount = 0;
        fullGame = false;
    }
    
    /**
     * Moves the active piece down one slot
     */
    public void move() {
    	if (y - 1 >= 0 && gameArray[getXValue()][y - 1] == 0 && gameArray[getXValue()][y] > 0) {
    		gameArray[getXValue()][y - 1] = gameArray[getXValue()][y];
    		gameArray[getXValue()][y] = 0;
    		y--;
    		setDropping(true);
    	} else {
    		setDropping(false);
    		lastY = y;
    		y = gameArray[0].length - 1;
    	}
    }
    
    
    /**
     * Checks if the current slot has an upper diagonal winning arrangement
     */
    public boolean checkUpDiag(int x, int y, int type) {
    	if (type == 0) return false;
    	int maxLength = 1;
    	for (int k = 1; k < Math.min(gameArray[0].length, gameArray.length); k++) {
    		if (x + k < gameArray.length && y + k < gameArray[0].length) {
    			if (gameArray[x + k][y + k] == type) {
    				maxLength++;
    			} else {
    				break;
    			}
    		}
    		if (maxLength >= WIN_LENGTH) return true;
    	}
    	
    	// Adds second half of pieces in the diagonal
    	for (int k = 1; k < Math.min(gameArray[0].length, gameArray.length); k++) {
    		if (x - k >= 0 && y - k >= 0) {
    			if (gameArray[x - k][y - k] == type) {
    				maxLength++;
    			} else {
    				break;
    			}
    		}
    		if (maxLength >= WIN_LENGTH) return true;
    	}    	
    	return false;
    }
    
    
    /**
     * Checks if the current slot has an downward diagonal winning arrangement
     */
    public boolean checkDownDiag(int x, int y, int type) {
    	if (type == 0) return false;
    	int maxLength = 1;
    	for (int k = 1; k < Math.min(gameArray[0].length, gameArray.length); k++) {
    		if (x + k < gameArray.length && y - k >= 0) {
    			if (gameArray[x + k][y - k] == type) {
    				maxLength++;
    			} else {
    				break;
    			}
    		}
    		if (maxLength >= WIN_LENGTH) return true;
    	}
    	
    	// Adds second half of pieces in the diagonal
    	for (int k = 1; k < Math.min(gameArray[0].length, gameArray.length); k++) {
    		if (x - k >= 0 && y + k < gameArray[0].length) {
    			if (gameArray[x - k][y + k] == type) {
    				maxLength++;
    			} else {
    				break;
    			}
    		}
    		if (maxLength >= WIN_LENGTH) return true;
    	}    	
    	return false;
    }
    
    
    /**
     * Checks if there are a winning number horizontally
     */
    public boolean checkHorizontal(int x, int y, int type) {
    	if (type == 0) return false;
    	int maxLength = 1;
    	
    	// Adds the horizontal pieces to the right
    	for (int k = 1; k < gameArray.length; k++) {
    		if (x + k < gameArray.length) {
    			if (gameArray[x + k][y] == type) {
    				maxLength++;
    			} else {
    				break;
    			}
    		}
    		if (maxLength >= WIN_LENGTH) return true;
    	}
    	
    	// Adds the horizontal pieces to the left of the given piece
    	for (int k = 1; k < gameArray.length; k++) {
    		if (x - k >= 0) {
    			if (gameArray[x - k][y] == type) {
    				maxLength++;
    			} else {
    				break;
    			}
    		}
    		if (maxLength >= WIN_LENGTH) return true;
    	}
    	
    	return false;
    }
    
    
    /**
     * Checks if there are a winning number vertically
     */
    public boolean checkVertical(int x, int y, int type) {
    	if (type == 0) return false;
    	int maxLength = 1;
    	
    	// Adds the pieces above
    	for (int k = 1; k < gameArray[0].length; k++) {
    		if (y + k < gameArray[0].length) {
    			if (gameArray[x][y + k] == type) {
    				maxLength++;
    			} else {
    				break;
    			}
    		}
    		if (maxLength >= WIN_LENGTH) return true;
    	}
    	
    	// Adds the pieces below
    	for (int k = 1; k < gameArray[0].length; k++) {
    		if (y - k >= 0) {
    			if (gameArray[x][y - k] == type) {
    				maxLength++;
    			} else {
    				break;
    			}
    		}
    		if (maxLength >= WIN_LENGTH) return true;
    	}
    	
    	return false;
    }
    
    
    /**
     * Checks if it is an end of game situation.
     */
    public void tryEndGame() {
		int type = gameArray[getXValue()][lastY];
		

		if (pieceCount == WIDTH * (HEIGHT - 1)) {
			playing = false;
			fullGame = true;
		}
			
		if (checkUpDiag(getXValue(), lastY, type) || checkDownDiag(getXValue(), lastY, type) ||
			checkHorizontal(getXValue(), lastY, type) || checkVertical(getXValue(), lastY, type)) {
			playing = false;
			fullGame = false;
		}
	}


    /**
     * This method is called every time the timer defined in the constructor triggers.
     */
    void tick() {
    	if (playing) {
    		move();
            repaint();
            getStatus().setText(getCurrPlayer() + "'s turn");
            if (!isDropping()) tryEndGame();
            
        } else if (fullGame) {
        	getStatus().setText("Tie Ball Game !");
        	Object[] options = { "Yeah Sure!", "Nah Bro" };
        	int choice = JOptionPane.showOptionDialog(null, "You tied bruh! Play again?", "Instructions",
        	             JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION,
        	null, options, options[0]);
        	if (choice == 0) {
        		if (getActivePlayer() == 1) {
        			setActivePlayer(2);
        		} else {
        			setActivePlayer(1);
        		}
        		reset();
        	} else {
        		System.exit(0);
        	}
        	
        	
        } else {
        	getStatus().setText("Congrats " + getCurrPlayer());
        	Object[] options = { "Yeah Sure!", "Nah Bro" };
        	int choice = JOptionPane.showOptionDialog(null, "Congrats " + getCurrPlayer()
        	             + "! Play again?", "Instructions",
        	             JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION,
        	null, options, options[0]);
        	if (choice == 0) {
        		if (getActivePlayer() == 1) {
        			setActivePlayer(2);
        		} else {
        			setActivePlayer(1);
        		}
        		reset();
        	} else {
        		System.exit(0);
        	}
        	
        }
    }
    
    public String getCurrPlayer() {
    	if (getActivePlayer() == 1) {
    		return nickA;
    	} else {
    		return nickB;
    	}
    }
    
    
    public void setPieceColors(Color c1, Color c2) {
    	this.setFirstColorTRUE(c1);
    	this.setSecondColorTRUE(c2);
    }
    
    public String colorToString(Color c) {
    	if (c.equals(Game.DBLUE)) {
    		return "Dark Blue";
    	} else if (c.equals(Game.LBLUE)) {
    		return "Light Blue";
    	} else if (c.equals(Color.RED)) {
    		return "Red"; 
    	} else if (c.equals(Color.YELLOW)) {
    		return "Yellow";
    	} else {
        	return "White";
    	}
    }
    
    public String getFirstColor() {
    	return colorToString(this.getFirstColorTRUE());
    }
    
    public String getSecondColor() {
    	return colorToString(this.getSecondColorTRUE());
    }
    
    public void setNickA(String nickA) {
    	if (nickA != this.nickA) this.nickA = nickA;
    }
    
    public void setNickB(String nickB) {
    	if (nickB != this.nickB) this.nickB = nickB;
    }
    
    
    public void paintPiece(int x, int y, Graphics g) {
    	switch (Math.abs(gameArray[x][y])) {  
        case 1:
        	g.setColor(getFirstColorTRUE());
        	break;
        case 2:
        	g.setColor(getSecondColorTRUE());
            break;
        default:
        	g.setColor(new Color(50,50,50));
        }
    	
    	if (y == gameArray[0].length - 1 && gameArray[x][y] == 0) return;
    	
    	
    	g.fillOval(SIZE * x, SIZE * (HEIGHT - y - 1), SIZE, SIZE);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < gameArray.length; i++) {
        	for (int j = 0; j < gameArray[i].length; j++) {
        		paintPiece(i, j, g);
        	}
        }
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH * SIZE, HEIGHT * SIZE);
    }
    
    public void save() {
    	try {
        	Writer out = new FileWriter(TXT_FILE);
        	
        	for (int i = 0; i < gameArray.length; i++) {
            	for (int j = 0; j < gameArray[i].length; j++) {
            		out.write(gameArray[i][j]);
            	}
            }
        	
        	out.write('\n');
        	
        	out.flush();
            out.close();
        } catch (IOException e) {
            System.out.println("Problem reading message");
        }
    }
    
    @SuppressWarnings("resource")
	public void load() {
    	try {
        	FileReader in = new FileReader(TXT_FILE);
        	
        	for (int i = 0; i < gameArray.length; i++) {
            	for (int j = 0; j < gameArray[i].length; j++) {
            		
            		int c = in.read();
            		
            		if (c == '\n') {
            			break;
            		}
            		gameArray[i][j] = c;
            	}
            }
        } catch (IOException e) {
            System.out.println("Problem reading message");
        }
    }

	public int getActivePlayer() {
		return activePlayer;
	}

	public void setActivePlayer(int activePlayer) {
		this.activePlayer = activePlayer;
	}

	public boolean isDropping() {
		return dropping;
	}

	public void setDropping(boolean dropping) {
		this.dropping = dropping;
	}

	public int getXValue() {
		return x;
	}

	public void setXValue(int x) {
		this.x = x;
	}

	public JLabel getStatus() {
		return status;
	}

	public void setStatus(JLabel status) {
		this.status = status;
	}

	public Color getFirstColorTRUE() {
		return firstColor;
	}

	public void setFirstColorTRUE(Color firstColor) {
		this.firstColor = firstColor;
	}

	public Color getSecondColorTRUE() {
		return secondColor;
	}

	public void setSecondColorTRUE(Color secondColor) {
		this.secondColor = secondColor;
	}

}
