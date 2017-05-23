package tile;

import javax.swing.ImageIcon;

public abstract class Tile{

	public static final int WATER = 0;
	public static final int BANK1 = 1;
	public static final int BANK2 = 2;
	public static final int PLANK1 = 3;
	public static final int PLANK1_MAN = 4;
	public static final int PLANK2 = 5;
	public static final int PLANK2_MAN = 6;
	public static final int STUMP1 = 7;
	public static final int STUMP1_MAN = 8;
	public static final int STUMP2 = 9;
	public static final int STUMP2_MAN = 10;
	public static final int STUMP3 = 11;
	public static final int STUMP3_MAN = 12;
	public static final int FISH1 = 13;
	public static final int FISH2 = 14;
	public static final int FISH3 = 15;
	
	final int identifier;// holds a value that helps identify what the tile is(WATER, PLANK, STUMP etc.)
	final ImageIcon icon;//holds the image icon
	private TileButton tileButton;// reference to the tile button that this tile is held in

	// creates a tile object and sets an icon and an identifier
	protected Tile(ImageIcon icon, int i){
		this.icon = icon;
		identifier = i;
	}

	/** 
	 * set the reference to the tile button that this tile is held in
	 * @param tileButton
	 */
	public void setTileButton(TileButton tileButton){
		this.tileButton = tileButton;
	}

	/** 
	 * returns the reference to the tile button that this tile is held in
	 * @return
	 */
	public TileButton getTileButton(){
		return this.tileButton;
	}

	/**
	 * Constructs and returns a tile object.
	 * 
	 * Use one of the static constants of the class to get a tile with
	 * corresponding attributes and icon. The method returns: WFB, Plank, Post.
	 * All of the classes returned are subclasses of Tile and hold their special
	 * features.
	 * 
	 * @param ident
	 * @return
	 */
	public static Tile getTile(int ident){
		Tile t;
		switch (ident) {
			case Tile.WATER:
			case Tile.BANK1:
			case Tile.BANK2:
			case Tile.FISH1:
			case Tile.FISH2:
			case Tile.FISH3:
				t = new WFB(ident);
				break;
			case Tile.PLANK1:
			case Tile.PLANK1_MAN:
			case Tile.PLANK2:
			case Tile.PLANK2_MAN:
				t = new Plank(ident);
				break;
			case Tile.STUMP1:
			case Tile.STUMP1_MAN:
			case Tile.STUMP2:
			case Tile.STUMP2_MAN:
			case Tile.STUMP3:
			case Tile.STUMP3_MAN:
				t = new Post(ident);
				break;
			default:
				t = new Post(ident);
		}

		return t;
	}

}
