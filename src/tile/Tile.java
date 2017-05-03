package tile;

import javax.swing.ImageIcon;

abstract class Tile{

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

	public final int identifier;
	public final int priority;
	public final int priorityHierarchy;
	public final ImageIcon icon;

	// private boolean accessible;
	// private boolean isAPath;

	protected Tile(ImageIcon icon, int i, int pH, int p){
		this.icon = icon;
		identifier = i;
		priorityHierarchy = pH;
		priority = p;
	}

	public static Tile getTile(int ident){
		Tile t;
		
		switch (ident) {
			case 0:
			case 1:
			case 2:
			case 13:
			case 14:
			case 15:
				t = new WFB(ident);
				break;
			case 3:
			case 4:
			case 5:
			case 6:
				t = new Plank(ident);
				break;
			case 7:
			case 8:
			case 9:
			case 10:
			case 11:
			case 12:
				t = new Post(ident);
				break;
			default: 
				t = new WFB(ident);
			
		}
	
		return t;
	}

}
