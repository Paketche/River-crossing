package tile;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

class TileButton extends JButton{

	private static final long serialVersionUID = 1L;

	public final Point location;
	private Tile tile;
	

	public TileButton(TileManager tm, Tile t, Point p){
		super(t.icon);
		tile = t;
		location = p;
		this.setPreferredSize(new Dimension(32,32));
		//setBorderPainted(false);
		addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				tm.act(location);
			}
		});
	}
	public TileButton(Tile t,Point p){
		super(t.icon);
		tile =t;
		location = p;
	}
	
	void setTile(Tile tile){
		this.tile = tile;
		setIcon(tile.icon);
	}
	
	Tile getTile(){
		return tile;
	}

}
