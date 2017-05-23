import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import tile.*;

public class UI extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel northPanel = new JPanel();// panel for the high and current scores 
	private JPanel eastPanel = new JPanel(new BorderLayout());// panel for the level buttons and inventory
	private JPanel startp = new JPanel();//panel for the level buttons (goes in eastPanel)
	public final TileManager tileManager;
	//buttons for the levels
	private JButton lvl1;
	private JButton lvl2;
	private JButton lvl3;
	
	
	// stop watch to count the time passed
	private StopWatch sW = new StopWatch();

	// holds the number for the current level
	int level;
	
	// holds the scores for the different levels
	private int[] highestScore = { 100, 75, 50 };

	private JLabel highestS = new JLabel();
	private JLabel timer = new JLabel();

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
			if (!frame.getTimerState())
				frame.getTimer().setText("Time: " + frame.getSWread());
			// if the has been won and there is a new high score we want to change it
			if (frame.tileManager.isWon()){
				frame.newScore();
				frame.tileManager.unWin();// we want to set the new score only once; and
																	// this stops a bug where when you start the
																	// game again your high score is 0
			}
		}
	}

	/**
	 * Create the frame.
	 */
	public UI(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setSize(360, 460);
		//set content pane
		contentPane = new JPanel(new BorderLayout());
		setContentPane(contentPane);
		
		//give an initial level to the ui and add it
		tileManager = new TileManager(new Level(2));
		level = tileManager.getLevel();
		contentPane.add(tileManager, BorderLayout.CENTER);

		// adding the button for the first level
		lvl1 = new JButton("Level 1");
		lvl1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				level = 1;
				sW.start();// start the watch
				
				tileManager.loadNewLevel(new Level(1));
				level = tileManager.getLevel();
				synchronized (this){
					stopTimer = false;
				}

				// set the highest score for the corresponding level
				highestS.setText("Fastest run: " + Integer.toString(highestScore[level - 1]) + " sec   ");
			}
		});
		startp.add(lvl1);

		// adding the button for the second level
		lvl2 = new JButton("Level 2");
		lvl2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				level = 2;
				sW.start();// start the watch

				tileManager.loadNewLevel(new Level(2));
				level = tileManager.getLevel();
				synchronized (this){
					stopTimer = false;
				}

				// set the highest score for the corresponding level
				highestS.setText("Fastest run: " + Integer.toString(highestScore[level - 1]) + " sec   ");
			}
		});
		startp.add(lvl2);
		/*
		 * //adding the button for the second level lvl3 = new JButton("Level 3");
		 * lvl3.addActionListener(new ActionListener() { public void
		 * actionPerformed(ActionEvent e){ sW.start();
		 * 
		 * tileManager.loadNewLevel(new Level(30)); synchronized (this){ stopTimer =
		 * false; } highestS.setText("Fastest run: "+
		 * Integer.toString(highestScore)+" sec   "); } }); startp.add(lvl3);
		 */
		startp.setPreferredSize(new Dimension(64, 150));
		eastPanel.add(startp, BorderLayout.CENTER);
		// add the labels for the highest score
		northPanel.add(highestS);
		northPanel.add(timer);

		// inventory = tileManager.inventory;
		// inventory.setPreferredSize(new Dimension(64,66));
		eastPanel.add(tileManager.inventory, BorderLayout.SOUTH);
		contentPane.add(eastPanel, BorderLayout.EAST);
		contentPane.add(northPanel, BorderLayout.NORTH);
		sW.start();
	}

	public void newScore(){
		if (sW.getCurrentTime() < highestScore[level - 1]){
			highestScore[level - 1] = sW.getCurrentTime();
		}
		highestS.setText("Fastest run: " + Integer.toString(highestScore[level - 1]) + "sec   ");
		synchronized (this){
			stopTimer = true;
		}
	}

	public String getSWread(){
		int seconds = (sW.getCurrentTime());
		return Integer.toString(seconds);
	}

	public JLabel getTimer(){
		return timer;
	}

	public boolean getTimerState(){
		return stopTimer;
	}
}
