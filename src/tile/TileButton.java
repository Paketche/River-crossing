package tile;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

class TileButton extends JButton{

	private static final long serialVersionUID = 1L;

	// location of the button in the tile manager that is going to be placed in
	public final Point location;
	// The tile that the button holds 
	private Tile tile;
	
	// constructor for TileManager
	TileButton(TileManager tm, Tile t, Point p){
		super(t.icon);
		tile = t;
		location = p;
		this.setPreferredSize(new Dimension(32, 32));
		setBorderPainted(false);
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				tm.act(location);
			}
		});
	}
	
	//Constructor for TileHolder
	TileButton(Tile t, Point p){
		super(t.icon);
		tile = t;
		location = p;
	}

	// Changes the tile and the icon of the button
	void setTile(Tile tile){
		this.tile = tile;
		setIcon(tile.icon);
	}
	
	// return the reference of the tile
	Tile getTile(){
		return tile;
	}

}
