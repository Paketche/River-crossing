package tile;

import java.awt.GridLayout;
import java.awt.Point;
import javax.swing.JPanel;

public class TileManager extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int MAP_HEIGTH = 13;
	public static final int MAP_WIDTH = 9;

	public final int level;
	private Point man;

	private TileButton[][] tileButtons = new TileButton[MAP_HEIGTH][MAP_WIDTH];
	private TileHolder inventory = new TileHolder();
	private TileHolder mouseBuffer = new TileHolder();

	/**
	 * 
	 * @param l
	 */
	public TileManager(Level l){
		super(new GridLayout(TileManager.MAP_HEIGTH, TileManager.MAP_WIDTH));
		level = l.levelNo;
		// get the tiles from from the level object and put them in a tile button
		// adds the tile button to the interface
		for (int i = 0; i < l.tiles.length; i++){
			for (int j = 0; j < l.tiles[i].length; j++){
				tileButtons[i][j] = new TileButton(this, l.tiles[i][j], new Point(j, i));
				// System.out.println("Copying in TM " + i + " " + j + " " +
				// tileButtons[i][j].getTile().identifier);
				if (compareTo(tileButtons[i][j].getTile().identifier, Tile.STUMP1_MAN, Tile.STUMP2_MAN, Tile.STUMP3_MAN,
						Tile.PLANK1_MAN, Tile.PLANK2_MAN)){
					man = new Point(j, i);
				}
				add(tileButtons[i][j]);
			}
		}
		for (int i = 0; i < tileButtons.length; i++){
			for (int j = 0; j < tileButtons[i].length; j++){
				switch (tileButtons[i][j].getTile().identifier) {
					case Tile.PLANK1:
					case Tile.PLANK1_MAN:
					case Tile.PLANK2:
					case Tile.PLANK2_MAN:
						// System.out.println("Setting the nodes on plank: " + i + " " + j +
						// " " + tileButtons[i][j].getTile().identifier);
						setNodes(new Point(j, i));
						break;
					case Tile.STUMP1:
					case Tile.STUMP1_MAN:
					case Tile.STUMP2:
					case Tile.STUMP2_MAN:
					case Tile.STUMP3:
					case Tile.STUMP3_MAN:
						System.out
								.println("Setting the edges on node: " + i + " " + j + " " + tileButtons[i][j].getTile().identifier);
						setEdges(new Point(j, i));
						break;
				}
			}
		}
	}

	/**
	 * Does an action depending on the kind of the pressed button
	 * 
	 * @param p
	 */
	public void act(Point p){
		// we get the coordinates of the pressed button and depending on what the
		// tile in the button is the manager acts differently
		TileButton pressedTile = tileButtons[p.y][p.x];
		System.out.println("pressed button is " + pressedTile.getTile().identifier);
		System.out.println("Mouse buffer is full: " + mouseBuffer.isFull());
		System.out.println("inventory is full: " + inventory.isFull());
		switch (pressedTile.getTile().identifier) {
			case Tile.WATER:
				if (mouseBuffer.isFull()){
					System.out.println("putting plank");
					putPlank(p);
				}
				break;
			case Tile.PLANK1:
			case Tile.PLANK2:
				if (!mouseBuffer.isFull() && !inventory.isFull()){
					System.out.println("taking plank");
					takePlank(p);
				}
				break;
			case Tile.STUMP1:
			case Tile.STUMP2:
			case Tile.STUMP3:
				travel(p);
				break;
		}
	}

	private void putPlank(Point p){
		// if the tile above or below the button that we are putting in a plank is a
		// poll we are going to put vertical plank
		if (compareTo(tileButtons[p.y + 1][p.x].getTile().identifier, Tile.STUMP1, Tile.STUMP1_MAN, Tile.STUMP2,
				Tile.STUMP2_MAN, Tile.STUMP3, Tile.STUMP3_MAN)
				|| compareTo(tileButtons[p.y - 1][p.x].getTile().identifier, Tile.STUMP1, Tile.STUMP1_MAN, Tile.STUMP2,
						Tile.STUMP2_MAN, Tile.STUMP3, Tile.STUMP3_MAN)){
			System.out.println("putting a vertical plank");
			tileButtons[p.y][p.x].setTile(Tile.getTile(Tile.PLANK1));
			// connect the new plank to the posts
			setEdges(new Point(p.x, p.y + 1));
			setEdges(new Point(p.x, p.y - 1));
		}
		// else put a horizontal plank in that place
		else{
			tileButtons[p.y][p.x].setTile(Tile.getTile(Tile.PLANK2));
			System.out.println("putting a horizontal");
			// connect the new plank to the posts
			setEdges(new Point(p.x + 1, p.y));
			setEdges(new Point(p.x - 1, p.y));
		}
		setNodes(p);
		inventory.empty();
	}

	private void takePlank(Point p){
		// put the tile that you are taking in the inventory and replace it with a
		// water tile
		inventory.putaTile(tileButtons[p.y][p.x].getTile());

		// remove the references of this plank in the stumps on both its sides
		switch (tileButtons[p.y][p.x].getTile().identifier) {
			case Tile.PLANK1:
				setEdges(new Point(p.x, p.y + 1));
				setEdges(new Point(p.x, p.y - 1));
				break;
			case Tile.PLANK2:
				setEdges(new Point(p.x + 1, p.y));
				setEdges(new Point(p.x - 1, p.y));
		}
		tileButtons[p.y][p.x].setTile(Tile.getTile(Tile.WATER));
	}

	private void travel(Point p){
		System.out.println("destination : " + p.y + " " + p.x);
		Post destination = (Post) tileButtons[p.y][p.x].getTile();
		System.out.println("destination: "+destination.toString());
		System.out.println("man at : " + man.y + " " + man.x);
		Post root = (Post) tileButtons[man.y][man.x].getTile();
		Post current = root;
		int z =0;
		travel: while (current != destination&& z<10){
			/*
			 * this for loop scans through the edges of the current post. If it finds
			 * an edge that is not traversed it moves to the other node of the edge
			 * (the node that is not the current one)
			 */
			System.out.println();
			System.out.println("Attempt to travel");
			for (int i = 0; i < current.edges.length; i++){
				System.out.println("going trough edges");
				if (current.edges[i] == null){
					System.out.println("edge is null: skipping");
					System.out.println("iteration :" + i);
					continue;
				}
				if (!current.edges[i].isTraversed()){
					System.out.println("trying to traverse edge " + i);
					current.edges[i].traverse(true);
					// we look at the nodes of the edge we are about to traverse
						for (int j = 0; j < current.edges[i].nodes.length; j++){
							if (current.edges[i].nodes[j] == null){
								continue;
							}
							// and we assign the other node of the edge to current(this way we
							// "traverse" the edge)
							if (current != current.edges[i].nodes[j]){
								System.out.println("Traversing edge");
								current.edges[i].leaveCrumb(true);
								current = current.edges[i].nodes[j];
								System.out.println("current:" +current.toString());
								System.out.println("destination: "+destination.toString());
								break;
							}
						}
					// we continue because if have moved the man once we don't need to
					// enter the next part of the loop
					System.out.println("continuing");
					continue travel;
				}
				System.out.println("Edge is traversed");
			}
			System.out.println("exited loop");
			/*
			 * the idea is that if the previous for loop exits without continuing to
			 * another iteration of the while loop that means that the man can't
			 * travel from the current node anywhere else but back(because all of the
			 * node's edges are traversed or there aren't any)
			 */
			if (current == root){
				System.out.println("breaking the travel");
				break travel;
			}
			/*
			 * if the method reaches this section it means that it has to return to
			 * the previous node and search a different path from there
			 */
			for (int i = 0; i < current.edges.length; i++){
				System.out.println("trying to go back");
				if (current.edges[i] == null){// because edges are not put one after the
																			// other in the array of edges
					continue;
				}
				if (current.edges[i].hasCrumb()){
					System.out.println("has crub taking;it back ");
					current.edges[i].leaveCrumb(false);
					// we look at the nodes of the edge we are about to traverse
					for (int j = 0; j < current.edges[i].nodes.length; j++){
						if (current.edges[i].nodes[j] == null){// because edges are not put
																										// one after the other in
																										// the array of edges
							continue;
						}
						// and we assign the other node of the edge
						if (current != current.edges[i].nodes[j]){
							System.out.println("traveling back");
							current = current.edges[i].nodes[j];
							break;
						}
					}
				}
			}
			z++;
		}
		tileButtons[man.y][man.x].setTile(Tile.getTile(tileButtons[man.y][man.x].getTile().identifier-1));
		setEdges(new Point(man.x,man.y));
		// System.out.println(" exited travel loop");
		/*
		 * reset the planks to not traversed and take any crumbs this prepares the
		 * board for a new travel
		 */
		/*
		for (int i = 0; i < tileButtons.length; i++){
			for (int j = 0; j < tileButtons[i].length; j++){
				if (tileButtons[i][j].getTile() == current){
					tileButtons[i][j].setTile(Tile.getTile(tileButtons[p.y][p.x].getTile().identifier+1));
					man = new Point(j, i);
				}
				// we check if the buttons holds a plank tile
				if (compareTo(tileButtons[i][j].getTile().identifier, Tile.PLANK1, Tile.PLANK2)){
					Plank plank = (Plank) tileButtons[i][j].getTile();
					plank.traverse(false);
					plank.leaveCrumb(false);
				}
			}
		}
		*/
		for (int i = 0; i < tileButtons.length; i++){
			for (int j = 0; j < tileButtons[i].length; j++){
				if (tileButtons[i][j].getTile() == current){
					tileButtons[i][j].setTile(Tile.getTile(tileButtons[p.y][p.x].getTile().identifier+1));
					man = new Point(j, i);
				}
				switch (tileButtons[i][j].getTile().identifier) {
					case Tile.PLANK1:
					case Tile.PLANK1_MAN:
					case Tile.PLANK2:
					case Tile.PLANK2_MAN:
						Plank plank = (Plank) tileButtons[i][j].getTile();
						plank.traverse(false);
						plank.leaveCrumb(false);
						setNodes(new Point(j, i));
						break;
					case Tile.STUMP1:
					case Tile.STUMP1_MAN:
					case Tile.STUMP2:
					case Tile.STUMP2_MAN:
					case Tile.STUMP3:
					case Tile.STUMP3_MAN:
						setEdges(new Point(j, i));
						break;
				}
			}
		}
	}

	private void setNodes(Point p){
		/*
		 * scans the two ends of the plank. Gets their references if there is a
		 * stump/post there and stores it in the current planks's array of
		 * references to surrounding planks
		 */
		Plank currentPlank = (Plank) tileButtons[p.y][p.x].getTile();
		System.out.println(
				"Setting nodes on tile " + tileButtons[p.y][p.x].getTile().identifier + " location: " + p.y + " " + p.x);

		switch (currentPlank.identifier) {// check if the planks if vertical or so
																			// that it cuts the scanning process in
																			// half
			case Tile.PLANK1:
			case Tile.PLANK1_MAN:
				// setting the node above
				if ((p.y - 1) >= 0 && compareTo(tileButtons[p.y - 1][p.x].getTile().identifier, Tile.STUMP1, Tile.STUMP1_MAN,
						Tile.STUMP2, Tile.STUMP2_MAN, Tile.STUMP3, Tile.STUMP3_MAN)){
					System.out.println("Setting node above " + p.y + " " + p.x);
					System.out.println("Tile above is " + tileButtons[p.y - 1][p.x].getTile().identifier);
					currentPlank.setNodes((Post) tileButtons[p.y - 1][p.x].getTile(), 0);
				} else{
					currentPlank.setNodes(null, 0);
				}
				// setting the node below
				if ((p.y + 1) < TileManager.MAP_HEIGTH && compareTo(tileButtons[p.y + 1][p.x].getTile().identifier, Tile.STUMP1,
						Tile.STUMP1_MAN, Tile.STUMP2, Tile.STUMP2_MAN, Tile.STUMP3, Tile.STUMP3_MAN)){
					System.out.println("Setting node below " + p.y + " " + p.x);
					System.out.println("Tile below is " + tileButtons[p.y + 1][p.x].getTile().identifier);
					currentPlank.setNodes((Post) tileButtons[p.y + 1][p.x].getTile(), 1);
				} else{
					currentPlank.setNodes(null, 1);
				}
				break;
			case Tile.PLANK2:
			case Tile.PLANK2_MAN:
				// setting the node on the right
				if ((p.x + 1) < TileManager.MAP_WIDTH && compareTo(tileButtons[p.y][p.x + 1].getTile().identifier, Tile.STUMP1,
						Tile.STUMP1_MAN, Tile.STUMP2, Tile.STUMP2_MAN, Tile.STUMP3, Tile.STUMP3_MAN)){
					System.out.println("Setting node on the right " + p.y + " " + p.x);
					System.out.println("Tile on the right is " + tileButtons[p.y][p.x + 1].getTile().identifier);
					currentPlank.setNodes((Post) tileButtons[p.y][p.x + 1].getTile(), 0);
				} else{
					currentPlank.setNodes(null, 0);
				}

				// setting the node on the left
				if ((p.x - 1) >= 0 && compareTo(tileButtons[p.y][p.x - 1].getTile().identifier, Tile.STUMP1, Tile.STUMP1_MAN,
						Tile.STUMP2, Tile.STUMP2_MAN, Tile.STUMP3, Tile.STUMP3_MAN)){
					System.out.println("Setting node on the left " + p.y + " " + p.x);
					System.out.println("Tile on the left is " + tileButtons[p.y][p.x - 1].getTile().identifier);
					currentPlank.setNodes((Post) tileButtons[p.y][p.x - 1].getTile(), 1);
				} else{
					currentPlank.setNodes(null, 1);
				}
				break;
		}
		for (int i = 0; i < currentPlank.nodes.length; i++){
			if (currentPlank.nodes[i] != null){
				System.out.println("there is a node at " + i);
			}
		}
		System.out.println();
	}

	private void setEdges(Point p){
		/*
		 * scans all of the tiles around the current post. Gets their references if
		 * there is a plank there and stores it in the current post's array of
		 * references to surrounding planks
		 * 
		 * when we call the setEdge method of the post we pass in a second parameter
		 * which corresponds to the index of the array in the post class and: 0 -the
		 * tile above 1 - the tile on the right 2 - the tile below 3 - the tile on
		 * the left
		 */
		Post currentPost = compareTo(tileButtons[p.y][p.x].getTile().identifier, Tile.PLANK1, Tile.PLANK1_MAN, Tile.PLANK2,
				Tile.PLANK2_MAN) ? null : (Post) tileButtons[p.y][p.x].getTile();
		if (currentPost == null){// in the put/take a plank method we are passing
															// coordinates that might now point to a node
			System.out.println("returning");
			return;
		}
		System.out.println(
				"Setting edges on tile " + tileButtons[p.y][p.x].getTile().identifier + " location: " + p.y + " " + p.x);
		// set the reference to the tile below
		if ((p.y + 1) < TileManager.MAP_HEIGTH
				&& compareTo(tileButtons[p.y + 1][p.x].getTile().identifier, Tile.PLANK1, Tile.PLANK1_MAN)){
			currentPost.setEdge((Plank) tileButtons[p.y + 1][p.x].getTile(), 0);
			System.out.println("Setting node below " + p.y + " " + p.x);
			System.out.println("Tile below is " + tileButtons[p.y + 1][p.x].getTile().identifier);
		} else{
			currentPost.setEdge(null, 0);
		}

		// set the reference to the tile on the right
		if ((p.x + 1) < TileManager.MAP_WIDTH
				&& compareTo(tileButtons[p.y][p.x + 1].getTile().identifier, Tile.PLANK2, Tile.PLANK2_MAN)){
			currentPost.setEdge((Plank) tileButtons[p.y][p.x + 1].getTile(), 1);
			System.out.println("Setting node rigth " + p.y + " " + p.x);
			System.out.println("Tile rigth is " + tileButtons[p.y][p.x + 1].getTile().identifier);
		} else{
			currentPost.setEdge(null, 1);
		}

		// set the reference to the tile above
		if ((p.y - 1) >= 0 && compareTo(tileButtons[p.y - 1][p.x].getTile().identifier, Tile.PLANK1, Tile.PLANK1_MAN)){
			currentPost.setEdge((Plank) tileButtons[p.y - 1][p.x].getTile(), 2);
			System.out.println("Setting node above " + p.y + " " + p.x);
			System.out.println("Tile above is " + tileButtons[p.y - 1][p.x].getTile().identifier);
		} else{
			currentPost.setEdge(null, 2);
		}

		// set the reference to the tile on the left
		if ((p.x - 1) >= 0 && compareTo(tileButtons[p.y][p.x - 1].getTile().identifier, Tile.PLANK2, Tile.PLANK2_MAN)){
			System.out.println("Setting node left " + p.y + " " + p.x);
			System.out.println("Tile left is " + tileButtons[p.y][p.x - 1].getTile().identifier);
			currentPost.setEdge((Plank) tileButtons[p.y][p.x - 1].getTile(), 3);
		} else{
			currentPost.setEdge(null, 3);
		}

		for (int i = 0; i < currentPost.edges.length; i++){
			if (currentPost.edges[i] != null){
				System.out.println("there is an edge at " + i);
			}
		}
		System.out.println();
	}

	private boolean compareTo(int arg1, int... args){
		for (int x : args){
			if (arg1 == x){
				return true;
			}
		}
		return false;
	}
}
