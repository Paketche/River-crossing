package tile;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JButton;
import javax.swing.JPanel;

class TileHolder extends JPanel{

	private static final long serialVersionUID = 1L;

	// tile buttons that represent the inventory
	TileButton[] tileButtons = new TileButton[3];

	// check whether the inventory has planks in it
	boolean isFull(){
		for (int i = 0; i < tileButtons.length && tileButtons[i] != null; i++){
			if (tileButtons[i].getTile().identifier != 0){
				return true;
			}
		}
		return false;
	}

	// puts a plank in the inventory
	void putaTile(Tile tile){
		// check if the tile has been passed is a plank and exit of not
		Plank plank = TileManager.compareTo(tile.identifier, Tile.PLANK1, Tile.PLANK2) ? (Plank) tile : null;
		if (plank == null){
			return;
		}
		// if the plank is long we want to place as many planks in the inventory as
		// the plank is long
		for (int i = 0; i < plank.adjecentPlanks.length && plank.adjecentPlanks[i] != null; i++){
			this.tileButtons[i + 1].setIcon(Tile.getTile(Tile.PLANK1).icon);
			this.tileButtons[i + 1].setTile(Tile.getTile(Tile.PLANK1));
		}

		this.tileButtons[0].setTile(Tile.getTile(Tile.PLANK1));
	}

	// flushes the inventory
	void empty(){
		for (int i = 0; i < tileButtons.length; i++){
			this.tileButtons[i].setTile(Tile.getTile(Tile.WATER));
		}
	}

	// creates an and populates it with buttons and tiles in the buttons(the
	// buttons are not active, and just for purposes)
	TileHolder(){
		super(new GridLayout(3, 2));
		setPreferredSize(new Dimension(64, 96));

		for (int i = 0; i < tileButtons.length; i++){
			tileButtons[i] = new TileButton(Tile.getTile(Tile.WATER), new Point(0, i));
			// only 3 of 6 tiles are used so we just fill the other one to make the ui
			// look better
			add(tileButtons[i]);
			JButton b = new JButton(Tile.getTile(Tile.WATER).icon);
			b.setBorderPainted(false);
			add(b);
		}

	}
}
