package tile;

import javax.swing.ImageIcon;

class Post extends Tile{

	// a reference to all of the planks that connect to this post
	Plank[] edges = new Plank[4];

	// creates planks with a corresponding plank as the icon
	public Post(int ident){
		super(Post.getAnIcon(ident), ident);
	}
	
	//set a reference to a plank that is next to the post;
	void setEdge(Plank pl, int i){
		edges[i] = pl;
	}
	
	// get an image icon corresponding to a constant fed to the method
	private static ImageIcon getAnIcon(int i){
		ImageIcon icon;
		switch (i) {
			case Tile.STUMP1:
				icon = new ImageIcon("src\\tile\\RiverCrossingGraphics\\stump1.jpg");
				break;
			case Tile.STUMP1_MAN:
				icon = new ImageIcon("src\\tile\\RiverCrossingGraphics\\stump1_man.jpg");
				break;
			case Tile.STUMP2:
				icon = new ImageIcon("src\\tile\\RiverCrossingGraphics\\stump2.jpg");
				break;
			case Tile.STUMP2_MAN:
				icon = new ImageIcon("src\\tile\\RiverCrossingGraphics\\stump2_man.jpg");
				break;
			case Tile.STUMP3:
				icon = new ImageIcon("src\\tile\\RiverCrossingGraphics\\stump3.jpg");
				break;
			case Tile.STUMP3_MAN:
				icon = new ImageIcon("src\\tile\\RiverCrossingGraphics\\stump3_man.jpg");
				break;
			default:
				icon = new ImageIcon("src\\tile\\RiverCrossingGraphics\\stump1.jpg");
		}
		return icon;
	}
}
