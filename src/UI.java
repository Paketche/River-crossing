import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tile.*;

public class UI extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	// panel for the high and current scores
	private JPanel northPanel = new JPanel();
	// panel for the level buttons and inventory
	private JPanel eastPanel = new JPanel(new BorderLayout());
	// panel for entering a name
	private JPanel southPanel = new JPanel(new BorderLayout());

	// panel for the level buttons (goes ineastPanel)
	private JPanel startp = new JPanel();
	public final TileManager tileManager;
	// buttons for the levels
	private JButton lvl1;
	private JButton lvl2;
	private JButton lvl3;
	private JTextField textField;

	// stop watch to count the time passed
	private StopWatch sW = new StopWatch();

	// holds the number for the current level
	private int level;

	// holds the names of the high score holders
	private String[] names = { "Jeff", "John", "Cena" };
	// holds the scores for the different levels
	private int[] highestScore = { 100, 75, 50 };

	private JLabel highestS = new JLabel();
	private JLabel timer = new JLabel();
	private JLabel enterName = new JLabel("Enter your name here:");

	// holds the state of the timer whether its on or not
	private boolean stopTimer = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args){
		UI frame = new UI();
		frame.setVisible(true);
		// synchronized (frame){
		while (true){
			// if the game has been won we want to stop updating the time on the ui
			if (!frame.getTimerState() && !frame.tileManager.isWon())
				frame.elapseTime();
			// if the has been won and there is a new high score we want to change it
			if (frame.tileManager.isWon()){
				frame.stopTimer();
			}
		}
	}

	/**
	 * Create the frame.
	 */
	public UI(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setSize(360, 480);
		// set content pane
		contentPane = new JPanel(new BorderLayout());
		setContentPane(contentPane);

		// give an initial level to the ui and add it
		tileManager = new TileManager(new Level(2));
		level = tileManager.getLevel();
		contentPane.add(tileManager, BorderLayout.CENTER);

		// adding the button for the first level
		lvl1 = new JButton("Level 1");
		lvl1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				buttonFunction(1);
			}
		});
		startp.add(lvl1);

		// adding the button for the second level
		lvl2 = new JButton("Level 2");
		lvl2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				buttonFunction(2);
			}
		});
		startp.add(lvl2);

		// adding the button for the third level
		lvl3 = new JButton("Level 3");
		lvl3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				buttonFunction(3);
			}
		});
		startp.add(lvl3);

		startp.setPreferredSize(new Dimension(64, 150));
		eastPanel.add(startp, BorderLayout.CENTER);
		eastPanel.add(tileManager.inventory, BorderLayout.SOUTH);
		// add the labels for the highest score
		northPanel.add(highestS);
		northPanel.add(timer);

		textField = new JTextField();
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				if (tileManager.isWon())
					newScore();
			}
		});
		southPanel.add(enterName, BorderLayout.WEST);
		southPanel.add(textField, BorderLayout.CENTER);

		contentPane.add(southPanel, BorderLayout.SOUTH);
		contentPane.add(eastPanel, BorderLayout.EAST);
		contentPane.add(northPanel, BorderLayout.NORTH);
		highestS.setText("Fastest run: " + Integer.toString(highestScore[level - 1]) + " secs by " + names[level - 1]);
		sW.start();
	}

	private void newScore(){
		// sets the new score if there is one
		if (sW.getCurrentTime() < highestScore[level - 1]){
			highestScore[level - 1] = sW.getCurrentTime();
			names[level - 1] = textField.getText();
		}
		highestS.setText("Fastest run: " + Integer.toString(highestScore[level - 1]) + " secs by " + names[level - 1]);
		stopTimer = true;// stop the stop watch
		tileManager.unWin();// set the game condition to not won
	}

	/**
	 * REturns a string representation between when the stop watch was started and
	 * when this string was called
	 * 
	 * @return Stop watch time
	 */
	public String getSWread(){
		return Integer.toString(sW.getCurrentTime());
	}

	private void buttonFunction(int i){
		sW.start();// start the watch
		tileManager.loadNewLevel(new Level(i));
		level = tileManager.getLevel();
		synchronized (this){
			stopTimer = false;
		}
		// set the highest score for the corresponding level
		highestS.setText("Fastest run: " + Integer.toString(highestScore[level - 1]) + " secs by " + names[level - 1]);
	}

	/**
	 * changes the timer label to the passed time since the start of the stop
	 * watch
	 */
	public void elapseTime(){
		timer.setText("Time: " + getSWread());
	}
	/**
	 * Returns true if the stop watch is running
	 * @return Stop watch state
	 */
	public boolean getTimerState(){
		return stopTimer;
	}
	/**
	 * Stops the timer
	 */
	public void stopTimer(){
		stopTimer = true;
	}
}
