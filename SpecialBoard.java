import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class SpecialBoard extends Board {
	public SpecialBoard(int winNum, int width, int height, Color firstColor,
			Color secondColor, JLabel status) {
		super(winNum, width, height, firstColor, secondColor, status);
		
		addKeyListener(new KeyAdapter() {
        	public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                	deleteRandom();
                    
                } 
            } 
        });
    }

	public boolean rowContains (int y) {
		for (int i = 0; i < gameArray.length; i++) {
			if (y >= 0 && y < gameArray[0].length && gameArray[i][y] > 0) return true;
		}
		
		return false;
	}
	
	public void deleteRandom() {
		int spot = (int) ((gameArray[0].length - 1) * Math.random());
		while (!rowContains(spot)) {
			spot--;
			if (spot < 0) return;
		}
		
		Set<Integer> allSpots = new HashSet<Integer>();
		for (int i = 0; i < gameArray.length; i++) {
			allSpots.add(i);
		}
		int xSpot = (int) (gameArray.length * Math.random());
		
		for (int i = 0; i < gameArray.length; i++) {
			if (xSpot + i >= gameArray.length) break;
			
			if (gameArray[xSpot + i][spot] > 0) {
				gameArray[xSpot + i][spot] = 3;
				return;
			} else {
				allSpots.add(i);
			}
		}
		
		for (int i = 0; i < gameArray.length; i++) {
			if (xSpot - i < 0) break;
			
			if (gameArray[xSpot - i][spot] > 0) {
				gameArray[xSpot - i][spot] = 3;
				return;
			} else {
				allSpots.add(i);
			}
		}
	}
	
	
	public void paintPiece(int x, int y, Graphics g) {
    	switch (Math.abs(gameArray[x][y])) {  
        case 1:
        	g.setColor(getFirstColorTRUE());
        	break;
        case 2:
        	g.setColor(getSecondColorTRUE());
            break;
        case 3:
        	g.setColor(new Color(252, 118, 106));
        	break;
        default:
        	g.setColor(new Color(50,50,50));
        }
    	
    	
    	if (y == gameArray[0].length - 1 && gameArray[x][y] == 0) return;
    	
    	
    	g.fillOval(SIZE * x, SIZE * (HEIGHT - y - 1), SIZE, SIZE);
    }

	
}
