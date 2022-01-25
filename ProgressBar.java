import java.awt.Graphics;
import java.awt.Color;

/**
 * [ProgressBar.java]
 * Progress bar to show how much algorithm has progressed
 * @author Brian Zhang
 * @version 1, January 25, 2022
 */

class ProgressBar {
	private final int HEIGHT = 75;
	private final int WIDTH = 1000;
	private double percentage;
	private final int x;
	private final int y;

	ProgressBar(int x, int y) {
		this.x = x;
		this.y = y;
		this.percentage = 0;
	}

	public void update(int currentGeneration, int generationCap) {
		// Updates the percentage based on generation of algorithm
		this.percentage = (double) currentGeneration / generationCap;
	}

	// Draws progress bar to the JPanel
	protected void draw(Graphics g) {
		int percentageBarSize = (int) (WIDTH * this.percentage);
		g.setColor(Color.GREEN);
		g.fillRect(x, y, percentageBarSize, HEIGHT);
		g.setColor(Color.WHITE);
		g.drawRect(x, y, WIDTH, HEIGHT);
	}
}