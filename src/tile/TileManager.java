package tile;

import java.awt.Dimension;
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

	public TileButton[][] tileButtons = new TileButton[MAP_HEIGTH][MAP_WIDTH];
	private TileHolder inventory = new TileHolder();

	/**
	 * 
	 * @param l
	 */
	public TileManager(Level l){
		super(new GridLayout(TileManager.MAP_HEIGTH, TileManager.MAP_WIDTH));
		setPreferredSize(new Dimension(640, 480));
		level = l.levelNo;
		// get the tiles from from the level object and put them in a tile button
		// adds the tile button to the interface
		for (int i = 0; i < l.tiles.length; i++){
			for (int j = 0; j < l.tiles[i].length; j++){

				tileButtons[i][j] = new TileButton(this, Tile.getTile(l.tiles[i][j]), new Point(j, i));
				tileButtons[i][j].getTile().setTileButton(tileButtons[i][j]);

				if (compareTo(tileButtons[i][j].getTile().identifier, Tile.STUMP1_MAN, Tile.STUMP2_MAN, Tile.STUMP3_MAN,
						Tile.PLANK1_MAN, Tile.PLANK2_MAN)){
					// set the location of the man if you can't find them
					man = new Point(j, i);
				}
				add(tileButtons[i][j]);
			}
		}
		setBoard();
		mendPlanks();
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
		switch (pressedTile.getTile().identifier) {
			case Tile.WATER:
				if (inventory.isFull() && isManNear(p)){
					putPlank(p);
				}
				break;
			case Tile.PLANK1:
			case Tile.PLANK2:
				if (!inventory.isFull() && isManNear(p)){
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
			tileButtons[p.y][p.x].setTile(Tile.getTile(Tile.PLANK1));
			tileButtons[p.y][p.x].getTile().setTileButton(tileButtons[p.y][p.x]);

		}
		// else put a horizontal plank in that place
		else{
			tileButtons[p.y][p.x].setTile(Tile.getTile(Tile.PLANK2));
			tileButtons[p.y][p.x].getTile().setTileButton(tileButtons[p.y][p.x]);
		}
		setBoard();
		inventory.empty();
	}

	private void takePlank(Point p){
		// put the tile that you are taking in the inventory and replace it with a
		// water tile
		inventory.putaTile(tileButtons[p.y][p.x].getTile());

		Plank plank = compareTo(tileButtons[p.y][p.x].getTile().identifier, Tile.PLANK1, Tile.PLANK2)
				? (Plank) tileButtons[p.y][p.x].getTile() : null;

		for (int i = 0; i < plank.adjecentPlanks.length && plank.adjecentPlanks[i] != null; i++){
			// we change all of the adjacent planks into water
			// save the position of the adjacent plank because we lose it when we
			// change the tile
			Point point = plank.adjecentPlanks[i].getTileButton().location;
			// change the tile in adjacent plank's tile button
			plank.adjecentPlanks[i].getTileButton().setTile(Tile.getTile(Tile.WATER));
			// after you have changed the tile in the button reference the button in
			// the new tile that you have put
			tileButtons[point.y][point.x].getTile().setTileButton(tileButtons[point.y][point.x]);
		}

		// remove the references of this plank in the stumps on both its sides
		tileButtons[p.y][p.x].setTile(Tile.getTile(Tile.WATER));
		tileButtons[p.y][p.x].getTile().setTileButton(tileButtons[p.y][p.x]);
		setBoard();
		mendPlanks();
	}

	private void travel(Point p){
		Post destination = (Post) tileButtons[p.y][p.x].getTile();
		Post root = (Post) tileButtons[man.y][man.x].getTile();
		Post current = root;
		System.out.println(man);
		travel: while (current != destination){
			System.out.println();
			System.out.println(current.getTileButton().location);
			/*
			 * this for loop scans through the edges of the current post. If it finds
			 * an edge that is not traversed it moves to the other node of the edge
			 * (the node that is not the current one)
			 */
			for (int i = 0; i < current.edges.length; i++){
				if (current.edges[i] == null){
					System.out.println("skipping");
					continue;
				}
				if (!current.edges[i].isTraversed()){
					current.edges[i].traverse(true);

					for (int j = 0; j < current.edges[i].adjecentPlanks.length
							&& current.edges[i].adjecentPlanks[j] != null; j++){
						current.edges[i].adjecentPlanks[j].traverse(true);
					}
					// we look at the nodes of the edge we are about to traverse
					for (int j = 0; j < current.edges[i].nodes.length; j++){
						if (current.edges[i].nodes[j] == null){
							continue;
						}
						// and we assign the other node of the edge to current(this way we
						// "traverse" the edge)
						if (current != current.edges[i].nodes[j]){
							current.edges[i].leaveCrumb(true);
							for (int j1 = 0; j1 < current.edges[i].adjecentPlanks.length
									&& current.edges[i].adjecentPlanks[j1] != null; j1++){
								current.edges[i].adjecentPlanks[j1].traverse(true);
							}
							current = current.edges[i].nodes[j];
							break;
						}
					}
					// we continue because if have moved the man once we don't need to
					// enter the next part of the loop
					continue travel;
				}
				System.out.println("is TRaveled" + current.edges[i].getTileButton().location);
			}
			/*
			 * the idea is that if the previous for loop exits without continuing to
			 * another iteration of the while loop that means that the man can't
			 * travel from the current node anywhere else but back(because all of the
			 * node's edges are traversed or there aren't any)
			 */
			if (current == root){
				break travel;
			}

			/*
			 * if the method reaches this section it means that it has to return to
			 * the previous node and search a different path from there
			 */
			for (int i = 0; i < current.edges.length; i++){
				if (current.edges[i] == null){// because edges are not put one after the
																			// other in the array of edges
					continue;
				}
				if (current.edges[i].hasCrumb()){
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
							current = current.edges[i].nodes[j];
							break;
						}
					}
				}
			}
		}
		if (current != root){
			// change the tile of the starting position to a one without a man
			tileButtons[man.y][man.x].setTile(Tile.getTile(tileButtons[man.y][man.x].getTile().identifier - 1));
			tileButtons[man.y][man.x].getTile().setTileButton(tileButtons[man.y][man.x]);
			// change the location of the man put a tile with a man in the location
			man = current.getTileButton().location;
			current.getTileButton().setTile(Tile.getTile(tileButtons[p.y][p.x].getTile().identifier + 1));
			tileButtons[man.y][man.x].getTile().setTileButton(tileButtons[man.y][man.x]);
		}
		/*
		 * reset the board. connect the new substituted tiles(inefficient algorithm
		 * (was way too lazy)) reset the planks to not traversed and take any crumbs
		 * this prepares the board for a new travel
		 * 
		 * for (int i = 0; i < tileButtons.length; i++){ for (int j = 0; j <
		 * tileButtons[i].length; j++){ if (tileButtons[i][j].getTile() == current
		 * && current != root){
		 * tileButtons[i][j].setTile(Tile.getTile(tileButtons[p.y][p.x].getTile().
		 * identifier + 1,tileButtons[i][j])); man = new Point(j, i); } } }
		 */
		setBoard();
		mendPlanks();

		System.out.println(man);
		for (int i = 0; i < tileButtons.length; i++){
			for (int j = 0; j < tileButtons[i].length; j++){
				if (tileButtons[i][j].getTile().identifier == Tile.PLANK1){
					Plank plank = (Plank) tileButtons[i][j].getTile();
					System.out.println(plank.nodes[0]);
					System.out.println(plank.nodes[1]);
					System.out.println();
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

		switch (currentPlank.identifier) {// check if the planks if vertical or so
																			// that it cuts the scanning process in
																			// half
			case Tile.PLANK1:
			case Tile.PLANK1_MAN:
				above: {
					// setting the node above
					if ((p.y - 1) >= 0 && compareTo(tileButtons[p.y - 1][p.x].getTile().identifier, Tile.STUMP1, Tile.STUMP1_MAN,
							Tile.STUMP2, Tile.STUMP2_MAN, Tile.STUMP3, Tile.STUMP3_MAN)){
						currentPlank.setNodes((Post) tileButtons[p.y - 1][p.x].getTile(), 0);
						break above;
					} else{
						currentPlank.setNodes(null, 0);

					}
					if ((p.y - 1) >= 0 && compareTo(tileButtons[p.y - 1][p.x].getTile().identifier, Tile.PLANK1)){
						Plank pl = (Plank) tileButtons[p.y - 1][p.x].getTile();
						int i = 0;
						while (i < 2 && currentPlank.adjecentPlanks[i] != null){
							i++;
						}
						if (i < 2)
							currentPlank.adjecentPlanks[i] = pl;
					}
				}
				below: {
					// setting the node below
					if ((p.y + 1) < TileManager.MAP_HEIGTH && compareTo(tileButtons[p.y + 1][p.x].getTile().identifier,
							Tile.STUMP1, Tile.STUMP1_MAN, Tile.STUMP2, Tile.STUMP2_MAN, Tile.STUMP3, Tile.STUMP3_MAN)){
						currentPlank.setNodes((Post) tileButtons[p.y + 1][p.x].getTile(), 1);
						break below;
					} else{
						currentPlank.setNodes(null, 1);
					}
					if ((p.y + 1) < TileManager.MAP_HEIGTH
							&& compareTo(tileButtons[p.y + 1][p.x].getTile().identifier, Tile.PLANK1)){
						Plank pl = (Plank) tileButtons[p.y + 1][p.x].getTile();
						int i = 0;
						while (i < 2 && currentPlank.adjecentPlanks[i] != null){
							i++;
						}
						if (i < 2)
							currentPlank.adjecentPlanks[i] = pl;
					}
				}
				break;
			case Tile.PLANK2:
			case Tile.PLANK2_MAN:
				rigth: {
					// setting the node on the right
					if ((p.x + 1) < TileManager.MAP_WIDTH && compareTo(tileButtons[p.y][p.x + 1].getTile().identifier,
							Tile.STUMP1, Tile.STUMP1_MAN, Tile.STUMP2, Tile.STUMP2_MAN, Tile.STUMP3, Tile.STUMP3_MAN)){
						currentPlank.setNodes((Post) tileButtons[p.y][p.x + 1].getTile(), 0);
						break rigth;
					} else{
						currentPlank.setNodes(null, 0);
					}
					if ((p.x + 1) < TileManager.MAP_WIDTH
							&& compareTo(tileButtons[p.y][p.x + 1].getTile().identifier, Tile.PLANK2)){
						Plank pl = (Plank) tileButtons[p.y][p.x + 1].getTile();
						int i = 0;
						while (i < 2 && currentPlank.adjecentPlanks[i] != null){
							i++;
						}
						if (i < 2)
							currentPlank.adjecentPlanks[i] = pl;
					}
				}
				left: {
					// setting the node on the left
					if ((p.x - 1) >= 0 && compareTo(tileButtons[p.y][p.x - 1].getTile().identifier, Tile.STUMP1, Tile.STUMP1_MAN,
							Tile.STUMP2, Tile.STUMP2_MAN, Tile.STUMP3, Tile.STUMP3_MAN)){
						currentPlank.setNodes((Post) tileButtons[p.y][p.x - 1].getTile(), 1);
						break left;
					} else{
						currentPlank.setNodes(null, 1);
					}
					if ((p.x - 1) >= 0 && compareTo(tileButtons[p.y][p.x - 1].getTile().identifier, Tile.PLANK2)){
						Plank pl = (Plank) tileButtons[p.y][p.x - 1].getTile();
						int i = 0;
						while (i < 2 && currentPlank.adjecentPlanks[i] != null){
							i++;
						}
						if (i < 2)
							currentPlank.adjecentPlanks[i] = pl;
					}
				}
				break;
		}
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
			return;
		}
		// set the reference to the tile below
		if ((p.y + 1) < TileManager.MAP_HEIGTH
				&& compareTo(tileButtons[p.y + 1][p.x].getTile().identifier, Tile.PLANK1, Tile.PLANK1_MAN)){
			currentPost.setEdge((Plank) tileButtons[p.y + 1][p.x].getTile(), 0);
		} else{
			currentPost.setEdge(null, 0);
		}

		// set the reference to the tile on the right
		if ((p.x + 1) < TileManager.MAP_WIDTH
				&& compareTo(tileButtons[p.y][p.x + 1].getTile().identifier, Tile.PLANK2, Tile.PLANK2_MAN)){
			currentPost.setEdge((Plank) tileButtons[p.y][p.x + 1].getTile(), 1);
		} else{
			currentPost.setEdge(null, 1);
		}

		// set the reference to the tile above
		if ((p.y - 1) >= 0 && compareTo(tileButtons[p.y - 1][p.x].getTile().identifier, Tile.PLANK1, Tile.PLANK1_MAN)){
			currentPost.setEdge((Plank) tileButtons[p.y - 1][p.x].getTile(), 2);
		} else{
			currentPost.setEdge(null, 2);
		}

		// set the reference to the tile on the left
		if ((p.x - 1) >= 0 && compareTo(tileButtons[p.y][p.x - 1].getTile().identifier, Tile.PLANK2, Tile.PLANK2_MAN)){
			currentPost.setEdge((Plank) tileButtons[p.y][p.x - 1].getTile(), 3);
		} else{
			currentPost.setEdge(null, 3);
		}
	}

	private void setBoard(){
		for (int i = 0; i < tileButtons.length; i++){
			for (int j = 0; j < tileButtons[i].length; j++){
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

	private boolean isManNear(Point p){
		// is tile below a man
		if ((p.y + 1) < TileManager.MAP_HEIGTH && compareTo(tileButtons[p.y + 1][p.x].getTile().identifier, Tile.STUMP1_MAN,
				Tile.STUMP2_MAN, Tile.STUMP3_MAN)){
			return true;
		}
		// is tile on the right a man
		if ((p.x + 1) < TileManager.MAP_WIDTH && compareTo(tileButtons[p.y][p.x + 1].getTile().identifier, Tile.STUMP1_MAN,
				Tile.STUMP2_MAN, Tile.STUMP3_MAN)){
			return true;
		}
		// is tile below a man
		if ((p.y - 1) >= 0 && compareTo(tileButtons[p.y - 1][p.x].getTile().identifier, Tile.STUMP1_MAN, Tile.STUMP2_MAN,
				Tile.STUMP3_MAN)){
			return true;
		}
		// is tile on the left a man
		if ((p.x - 1) >= 0 && compareTo(tileButtons[p.y][p.x - 1].getTile().identifier, Tile.STUMP1_MAN, Tile.STUMP2_MAN,
				Tile.STUMP3_MAN)){
			return true;
		}
		return false;
	}

	private void mendPlanks(){
		// this is the most disgusting method i have ever done. I mean look at the
		// referencing i have done. Do you even reference m8

		// go trough the whole board
		for (int i = 0; i < tileButtons.length; i++){
			for (int j = 0; j < tileButtons[i].length; j++){
				if (compareTo(tileButtons[i][j].getTile().identifier, Tile.PLANK1, Tile.PLANK2)){
					// when you find a plank cast it so we can work on it as a Plank
					Plank plank = (Plank) tileButtons[i][j].getTile();

					for (int k = 0; k < plank.adjecentPlanks.length && plank.adjecentPlanks[k] != null; k++){
						// go through the plank's adjacent planks and copy their nodes to
						// the current one
						for (int l = 0; l < plank.adjecentPlanks[k].nodes.length; l++){
							if (plank.adjecentPlanks[k].nodes[l] == null){
								continue;
							}
							plank.nodes[l] = plank.adjecentPlanks[k].nodes[l];
						}
						// go through the plank's adjacent planks' adjacent planks
						for (int l = 0; l < plank.adjecentPlanks[k].adjecentPlanks.length
								&& plank.adjecentPlanks[k].adjecentPlanks[l] != null; l++){

							// copy their nodes to the current one
							for (int m = 0; m < plank.adjecentPlanks[k].adjecentPlanks[l].nodes.length; m++){
								if (plank.adjecentPlanks[k].adjecentPlanks[l].nodes[m] == null){
									continue;
								}
								plank.nodes[m] = plank.adjecentPlanks[k].adjecentPlanks[l].nodes[m];
							}
							// for instance if we had a 3 piece plank we would want the
							// leftmost plank to reference the rightmost plank
							if (plank.adjecentPlanks[k].adjecentPlanks[l] != plank){
								int z = 0;
								while (z < 2 && plank.adjecentPlanks[z] != null){
									z++;
								}
								if (z < 2){
									plank.adjecentPlanks[z] = plank.adjecentPlanks[k].adjecentPlanks[l];
								}
							}
						}
					}
				}
			}
		}
		// but it works
	}

	public static boolean compareTo(int arg1, int... args){
		for (int x : args){
			if (arg1 == x){
				return true;
			}
		}
		return false;
	}
}
