package tile;

import javax.swing.ImageIcon;

class Plank extends Tile{

	// reference for planks if we have a long plank
	Plank[] adjecentPlanks = new Plank[2];
	// reference to the posts connected to the plank
	Post[] nodes = new Post[2];

	/*
	 * variables that help with the movement of the man traversed to signify that
	 * the path has already has been visited crumb to mark the path back to the
	 * root node
	 */
	private boolean traversed;
	private boolean crumb;
	
	//creates planks with a corresponding plank as the icon
	Plank(int ident){
		super(Plank.getAnIcon(ident), ident);
		traversed = crumb = false;
	}

	//Returns true if the plank has already been walked on/traversed
	boolean isTraversed(){
		return traversed;
	}

	//Sets the traversed marker for the plank
	void traverse(boolean t){
		this.traversed = t;
	}

	//Shows whether the plank is the way back to the starting post
	boolean hasCrumb(){
		return crumb;
	}

	// Sets the marker that shows whether the plank is a way back
	void leaveCrumb(boolean c){
		this.crumb = c;
	}

	// set a node reference in the array for connection posts
	void setNodes(Post p, int i){
		try{
			nodes[i] = p;
		} catch (NullPointerException e){
		}
	}

	// gets an icon corresponding to a constant fed to the method
	private static ImageIcon getAnIcon(int i){
		ImageIcon icon;
		switch (i) {
			case Tile.PLANK1:
				icon = new ImageIcon("src\\tile\\RiverCrossingGraphics\\plank2.jpg");
				break;
			case Tile.PLANK2:
				icon = new ImageIcon("src\\tile\\RiverCrossingGraphics\\plank1.jpg");
				break;
			default:
				icon = new ImageIcon("src\\tile\\RiverCrossingGraphics\\plank1.jpg");
		}
		return icon;
	}
}
