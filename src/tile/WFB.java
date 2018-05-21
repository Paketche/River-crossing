package tile;

import javax.swing.ImageIcon;

class WFB extends Tile{

	WFB(int ident){
		super(WFB.getAnIcon(ident), ident);
	}

	//get an icon corresponding to a constant fed to the methods
	private static ImageIcon getAnIcon(int ident){
		ImageIcon icon;
		switch (ident) {
			case Tile.WATER:
				icon = new ImageIcon("RiverCrossingGraphics\\water1.jpg");
				break;
			case Tile.BANK1:
				icon = new ImageIcon("RiverCrossingGraphics\\bank1.jpg");
				break;
			case Tile.BANK2:
				icon = new ImageIcon("RiverCrossingGraphics\\bank2.jpg");
				break;
			case Tile.FISH1:
				icon = new ImageIcon("RiverCrossingGraphics\\fish1.jpg");
				break;
			case Tile.FISH2:
				icon = new ImageIcon("RiverCrossingGraphics\\fish2.jpg");
				break;
			case Tile.FISH3:
				icon = new ImageIcon("RiverCrossingGraphics\\fish3.jpg");
				break;
			default:
				icon = new ImageIcon("RiverCrossingGraphics\\water1.jpg");
		}
		return icon;
	}
}
