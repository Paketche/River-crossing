package tile;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JButton;
import javax.swing.JPanel;

class TileHolder extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	TileButton[] tileButtons = new TileButton[3];

	boolean isFull(){
		for (int i = 0; i < tileButtons.length; i++){
			// System.out.println(i + " " + tileButtons[i] == null);
		}
		for (int i = 0; i < tileButtons.length && tileButtons[i] != null; i++){
			if (tileButtons[i].getTile().identifier != 0){
				return true;
			}
		}
		return false;
	}

	void putaTile(Tile tile){
		Plank plank = TileManager.compareTo(tile.identifier, Tile.PLANK1, Tile.PLANK2) ? (Plank) tile : null;
		if (plank == null){
			return;
		}
		for (int i = 0; i < plank.adjecentPlanks.length && plank.adjecentPlanks[i] != null; i++){
			this.tileButtons[i+1].setIcon(Tile.getTile(Tile.PLANK1).icon);
			this.tileButtons[i + 1].setTile(Tile.getTile(Tile.PLANK1));
		}

		this.tileButtons[0].setTile(Tile.getTile(Tile.PLANK1));
	}

	void empty(){
		for (int i = 0; i < tileButtons.length; i++){
			this.tileButtons[i].setTile(Tile.getTile(Tile.WATER));
		}
	}

	public TileHolder(){
		super(new GridLayout(3, 2));
		setPreferredSize(new Dimension(64, 96));
	

		for (int i = 0; i < tileButtons.length; i++){
			tileButtons[i] = new TileButton(Tile.getTile(Tile.WATER), new Point(0, i));
			add(tileButtons[i]);
			add(new JButton(Tile.getTile(Tile.WATER).icon));
		}

	}
}
