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

@SuppressWarnings("serial")
public class BoardAI extends Board {
	private int winNum;
	public BoardAI(int winNum, int width, int height, Color firstColor, Color secondColor, JLabel status) {
		super(winNum, width, height, firstColor, secondColor, status);
		this.winNum = winNum;
    }

    
    public int findUpDiagLength(int x, int y) {
    	int type = 0;
    	
    	if (x + 1 < gameArray.length && y + 1 < gameArray[0].length && gameArray[x + 1][y + 1] != 0) {
    		type = gameArray[x + 1][y + 1];
    	} else if (x - 1 >= 0 && y - 1 >= 0 && gameArray[x - 1][y - 1] != 0) {
    		type = gameArray[x - 1][y - 1];
    	} else {
    		return 1;
    	}
    	
    	int maxLength = 1;
    	for (int k = 1; k < Math.min(gameArray[0].length, gameArray.length); k++) {
    		if (x + k < gameArray.length && y + k < gameArray[0].length) {
    			if (gameArray[x + k][y + k] == type) {
    				maxLength++;
    			} else {
    				break;
    			}
    		}
    	}
    	
    	for (int k = 1; k < Math.min(gameArray[0].length, gameArray.length); k++) {
    		if (x - k >= 0 && y - k >= 0) {
    			if (gameArray[x - k][y - k] == type) {
    				maxLength++;
    			} else {
    				break;
    			}
    		}
    	}    	
    	
    	return maxLength;
    }
    
    
    public int findDownDiagLength(int x, int y) {
    	int type = 0;
    	
    	if (x + 1 < gameArray.length && y - 1 >= 0 && gameArray[x + 1][y - 1] != 0) {
    		type = gameArray[x + 1][y - 1];
    	} else if (x - 1 >= 0 && y + 1 < gameArray[0].length && gameArray[x - 1][y + 1] != 0) {
    		type = gameArray[x - 1][y + 1];
    	} else {
    		return 1;
    	}
    	
    	int maxLength = 1;
    	for (int k = 1; k < Math.min(gameArray[0].length, gameArray.length); k++) {
    		if (x + k < gameArray.length && y - k >= 0) {
    			if (gameArray[x + k][y - k] == type) {
    				maxLength++;
    			} else {
    				break;
    			}
    		}
    	}
    	
    	for (int k = 1; k < Math.min(gameArray[0].length, gameArray.length); k++) {
    		if (x - k >= 0 && y + k < gameArray[0].length) {
    			if (gameArray[x - k][y + k] == type) {
    				maxLength++;
    			} else {
    				break;
    			}
    		}
    	}    	
    	
    	return maxLength;
    }
    
    
    public int findHorizontalLength(int x, int y) {
    	int type = 0;
    	if (y < 0) return 0;
    	
    	if (x + 1 < gameArray.length && gameArray[x + 1][y] != 0) {
    		type = gameArray[x + 1][y];
    	} else if (x - 1 >= 0 && gameArray[x - 1][y] != 0) {
    		type = gameArray[x - 1][y];
    	} else {
    		return 1;
    	}
    	
    	int maxLength = 1;
    	for (int k = 1; k < gameArray.length; k++) {
    		if (x + k < gameArray.length) {
    			if (gameArray[x + k][y] == type) {
    				maxLength++;
    			} else {
    				break;
    			}
    		}
    	}
    	
    	for (int k = 1; k < gameArray.length; k++) {
    		if (x - k >= 0) {
    			if (gameArray[x - k][y] == type) {
    				maxLength++;
    			} else {
    				break;
    			}
    		}
    	}
    	
    	return maxLength;
    }
    
    public int findVerticalLength(int x, int y) {
    	int type = 0;
    	
    	if (y + 1 < gameArray[0].length && gameArray[x][y + 1] != 0) {
    		type = gameArray[x][y + 1];
    	} else if (y - 1 >= 0 && gameArray[x][y - 1] != 0) {
    		type = gameArray[x][y - 1];
    	} else {
    		return 1;
    	}
    	
    	int maxLength = 1;
    	for (int k = 1; k < gameArray[0].length; k++) {
    		if (y + k < gameArray[0].length) {
    			if (gameArray[x][y + k] == type) {
    				maxLength++;
    			} else {
    				break;
    			}
    		}
    	}
    	
    	for (int k = 1; k < gameArray[0].length; k++) {
    		if (y - k >= 0) {
    			if (gameArray[x][y - k] == type) {
    				maxLength++;
    			} else {
    				break;
    			}
    		}
    	}
    	
    	return maxLength;
    }
    
    // Gets the spot a drop will be at
    public int findPotentialDrop(int x) {
    	for (int i = 0; i < gameArray[0].length - 1; i++) {
    		if (gameArray[x][i] == 0) {
    			return i;
    		}
    	}
    	
    	return -1;
    }
    
    
    public int findMax() {
    	int maxPos = -1;
    	int maxSize = 0;
    	for (int i = 0; i < gameArray.length; i++) {
    		int y = findPotentialDrop(i);
    		
    		if (y < 0) continue;
    		int size = Math.max(Math.max(Math.max(findUpDiagLength(i,y), findHorizontalLength(i,y)),
    				findVerticalLength(i,y)), findDownDiagLength(i,y));
    		if (size > maxSize) {
    			maxSize = size;
    			maxPos = i;
    		} else if (size == maxSize) {
    			if (Math.random() < .5) {
    				maxSize = size;
        			maxPos = i;
    			}
    		}
    	}
    	
    	return maxPos;
    	
    }
    
    
    public int hFindUpDiagLength(int x, int y, int type) {
    	int maxLength = 1;
    	for (int k = 1; k < Math.min(gameArray[0].length, gameArray.length); k++) {
    		if (x + k < gameArray.length && y + k < gameArray[0].length) {
    			if (gameArray[x + k][y + k] == type || gameArray[x + k][y + k] == type * 10) {
    				maxLength++;
    			} else {
    				break;
    			}
    		}
    	}
    	
    	for (int k = 1; k < Math.min(gameArray[0].length, gameArray.length); k++) {
    		if (x - k >= 0 && y - k >= 0) {
    			if (gameArray[x - k][y - k] == type || gameArray[x - k][y - k] == type * 10) {
    				maxLength++;
    			} else {
    				break;
    			}
    		}
    	}    	
    	
    	return maxLength;
    }
    
    
    public int hFindDownDiagLength(int x, int y, int type) {
    	int maxLength = 1;
    	for (int k = 1; k < Math.min(gameArray[0].length, gameArray.length); k++) {
    		if (x + k < gameArray.length && y - k >= 0) {
    			if (gameArray[x + k][y - k] == type || gameArray[x + k][y - k] == type * 10) {
    				maxLength++;
    			} else {
    				break;
    			}
    		}
    	}
    	
    	for (int k = 1; k < Math.min(gameArray[0].length, gameArray.length); k++) {
    		if (x - k >= 0 && y + k < gameArray[0].length) {
    			if (gameArray[x - k][y + k] == type || gameArray[x - k][y + k] == type * 10) {
    				maxLength++;
    			} else {
    				break;
    			}
    		}
    	}    	
    	
    	return maxLength;
    }
    
    
    public int hFindHorizontalLength(int x, int y, int type) {
    	if (y < 0) return 0;
    	
    	int maxLength = 1;
    	for (int k = 1; k < gameArray.length; k++) {
    		if (x + k < gameArray.length) {
    			if (gameArray[x + k][y] == type || gameArray[x + k][y] == type * 10) {
    				maxLength++;
    			} else {
    				break;
    			}
    		}
    	}
    	
    	for (int k = 1; k < gameArray.length; k++) {
    		if (x - k >= 0) {
    			if (gameArray[x - k][y] == type || gameArray[x - k][y] == type * 10) {
    				maxLength++;
    			} else {
    				break;
    			}
    		}
    	}
    	
    	return maxLength;
    }
    
    public int hFindVerticalLength(int x, int y, int type) {
    	int maxLength = 1;
    	for (int k = 1; k < gameArray[0].length; k++) {
    		if (y + k < gameArray[0].length) {
    			if (gameArray[x][y + k] == type || gameArray[x][y + k] == type * 10) {
    				maxLength++;
    			} else {
    				break;
    			}
    		}
    	}
    	
    	for (int k = 1; k < gameArray[0].length; k++) {
    		if (y - k >= 0) {
    			if (gameArray[x][y - k] == type || gameArray[x][y - k] == type * 10) {
    				maxLength++;
    			} else {
    				break;
    			}
    		}
    	}
    	
    	return maxLength;
    }
    
    public int findMaxHard() {
    	int maxPos = -1;
    	int maxSize = 0;
    	for (int i = 0; i < gameArray.length; i++) {
    		int y1 = findPotentialDrop(i);
    		if (y1 < 0) continue;
    		gameArray[i][y1] = 10;
    		
    		for (int s = 1; s <= 2; s++) {
	    		int size = Math.max(Math.max(Math.max(hFindUpDiagLength(i,y1, s),
	    				hFindHorizontalLength(i,y1, s)), hFindVerticalLength(i,y1, s)),
	    				hFindDownDiagLength(i,y1, s));
	    		if (size >= winNum) {
	    			gameArray[i][y1] = 0;
	    			return i;
    		}
    		
    		for (int j = 0; j < gameArray.length; j++) {
    			int y2 = findPotentialDrop(j);
    			if (y2 < 0) continue;
    			gameArray[j][y2] = 20;
        		
        		for (int k = 0; k < gameArray.length; k++) {
        			int y3 = findPotentialDrop(k);
            		
            		if (y3 < 0) {
            			gameArray[i][y1] = 0;
                		gameArray[j][y2] = 0;
            			continue;
            		}
            		
            		int tempSize = Math.max(Math.max(Math.max(hFindUpDiagLength(i, y3, s),
            				hFindHorizontalLength(i,y3, s)), hFindVerticalLength(i, y3, s)),
            				hFindDownDiagLength(i, y3, s));
            		
            		if (tempSize >= winNum && s == 2) {
            			gameArray[i][y1] = 0;
                		gameArray[j][y2] = 0;
            			continue;
            		}
            		
            		size = tempSize;
            		if (size > maxSize) {
            			maxSize = size;
            			maxPos = i;
            		} else if (size == maxSize) {
            			
            			if (Math.random() < .5 && s == 1) {
            				maxSize = size;
                			maxPos = i;
            			}
            		}
            		
            		gameArray[i][y1] = 0;
            		gameArray[j][y2] = 0;
        		}
    		}
    		}
    		
    		
    	}
    	
    	
    	return maxPos;
    }
    
    
    
    public boolean hasBegunDropping() {
    	if (gameArray[getXValue()][gameArray[0].length - 1] != 0) return false;
    	else return true;
    }
    
    public void dropPiece() {
    	if (getActivePlayer() == 1 && hasBegunDropping() && !isDropping()) {
    		setXValue(findMax());
    		gameArray[getXValue()][gameArray[0].length - 1] = getActivePlayer();
    		setActivePlayer(2);
    	}
    }
    
    public void hDropPiece() {
    	if (getActivePlayer() == 1 && hasBegunDropping() && !isDropping()) {
    		setXValue(findMaxHard());
    		gameArray[getXValue()][gameArray[0].length - 1] = getActivePlayer();
    		setActivePlayer(2);
    	}
    }
    
    
    void tick() {
    	if (playing) {
    		hDropPiece();
    		move();
            repaint();
            getStatus().setText(getCurrPlayer() + "'s turn");
            if (!isDropping()) tryEndGame();
            
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
}
 