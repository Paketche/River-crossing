package tile;

import javax.swing.ImageIcon;

class WFB extends Tile{

	public WFB(int ident){
		super(WFB.getAnIcon(ident), ident);
	}


	private static ImageIcon getAnIcon(int ident){
		ImageIcon icon;
		switch (ident) {
			case Tile.WATER:
				icon = new ImageIcon("src\\tile\\RiverCrossingGraphics\\water1.jpg");
				break;
			case Tile.BANK1:
				icon = new ImageIcon("src\\tile\\RiverCrossingGraphics\\bank1.jpg");
				break;
			case Tile.BANK2:
				icon = new ImageIcon("src\\tile\\RiverCrossingGraphics\\bank2.jpg");
				break;
			case Tile.FISH1:
				icon = new ImageIcon("src\\tile\\RiverCrossingGraphics\\fish1.jpg");
				break;
			case Tile.FISH2:
				icon = new ImageIcon("src\\tile\\RiverCrossingGraphics\\fish2.jpg");
				break;
			case Tile.FISH3:
				icon = new ImageIcon("src\\tile\\RiverCrossingGraphics\\fish3.jpg");
				break;
			default:
				icon = new ImageIcon("src\\tile\\RiverCrossingGraphics\\water1.jpg");
		}
		return icon;
	}
}
