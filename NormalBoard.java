import java.awt.Color;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public class NormalBoard extends Board {

	public NormalBoard(int winNum, int width, int height, Color firstColor,
			Color secondColor, JLabel status) {
		super(winNum, width, height, firstColor, secondColor, status);
	}

}
