import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
class Pair{
	int x,y;
	public Pair(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	public int getx() {
		return x;
	}

	public int gety() {
		return y;
	}
} 

class MyComparator implements Comparator<Pair> {
	public int compare(final Pair a, final Pair b) {
		return (new Integer(a.getx())).compareTo(new Integer(b.getx()));
	}
}

class DIJKSTRA extends JFrame implements ActionListener, MouseListener {
	int V, INF;
	ArrayList<Pair> vertexMapping;
	ArrayList<ArrayList<Pair>> adjList;
	ArrayList<Pair> l;
	JButton b2, b;
	TextField t1, t2, t3, t4, t5;
	JLabel l4, l5, l6, l7;

	public DIJKSTRA() {
		V = 0;
		INF = 9999999;
		vertexMapping = new ArrayList<Pair>();
		adjList = new ArrayList<ArrayList<Pair>>();
		l = new ArrayList<Pair>();
		setTitle("Where's My Path?");
		setVisible(true);
		setExtendedState(JFrame.MAXIMIZED_BOTH); //Screen size
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final JPanel p = new JPanel();
		p.addMouseListener(this);
		final Color c = Color.white; //Add Image here
		p.setBackground(c);
		setLayout(new FlowLayout());
		p.setPreferredSize(new Dimension(getWidth(), 550)); //Green Panel size
		add(p); // same as .pack() in tkinter
		final JPanel p1 = new JPanel(); //Green panel ke niche wala panel
		p1.setLayout(null);
		p1.setPreferredSize(new Dimension(getWidth(), 100));
		// p1.setBackground(Color.BLACK);

		//Lower Panel buttons and labels
		JLabel l1, l2, l3;
		l1 = new JLabel("Your Path will be directed using the Dijkstra's Algorithm");
		l2 = new JLabel("Enter Source: ");
		l3 = new JLabel("Enter Sink: ");
		l1.setBounds(550, 10, 450, 10); //getwidth() means the width of the whole screen
		l2.setBounds(20, 30, 100, 10);
		l3.setBounds(300, 30, 100, 10);
		t1 = new TextField(10);
		t2 = new TextField(10);
		t1.setBounds(120, 27, 100, 20);
		t2.setBounds(380, 27, 100, 20);
		p1.add(l1);
		p1.add(l2);
		p1.add(t1);
		p1.add(t2);
		p1.add(l3);
		b = new JButton("Find Your Shortest Path!");
		b.setBounds(220, 65, 170, 25);
		p1.add(b);
		l4 = new JLabel("ADD EDGE");
		l5 = new JLabel("From:");
		l6 = new JLabel("To:");
		l7 = new JLabel("Weight:");
		t3 = new TextField(10);
		t4 = new TextField(10);
		t5 = new TextField(10);
		l4.setBounds(1150, 25, 100, 10);
		l5.setBounds(930, 45, 50, 20);
		t3.setBounds(980, 45, 70, 20);
		l6.setBounds(1090, 45, 20, 20);
		t4.setBounds(1120, 45, 70, 20);
		l7.setBounds(1225, 45, 50, 20);
		t5.setBounds(1280, 45, 70, 20);
		p1.add(l4);
		p1.add(l5);
		p1.add(t3);
		p1.add(l6);
		p1.add(l7);
		p1.add(t4);
		p1.add(t5);
		b2 = new JButton("ADD EDGE");
		b2.setBounds(1250, 70, 100, 25);
		p1.add(b2);
		add(p1);

		//On clicking the buttons, below actionPerformed method has the action to be performed
		b2.addActionListener(this);
		b.addActionListener(this);
	}

	
	//Lower Panel End
	public void actionPerformed(final ActionEvent e) {
		if (e.getSource() == b2) { //if the click source was from button b2
			final int from = Integer.valueOf(t3.getText()); //Extract text from t3
			final int to = Integer.valueOf(t4.getText());
			final int weight = Integer.valueOf(t5.getText());
			//Validation
			if (weight < 0 || to > vertexMapping.size() || from > vertexMapping.size() || to < 0 || from < 0) {
				t3.setText("INVALID");
				t4.setText("INVALID");
				t5.setText("INVALID");
				return;
			}
			//To set the text to NULL again in textfield
			t3.setText("");
			t4.setText("");
			t5.setText("");

			//After the mouseClicked event, get the physical coordinates of the nodes.
			//e.g vertexMapping = {{2,3},{5,6}}  it means "0" node has coordinate {2,3}, "1" has {5,6}
			final int x1 = vertexMapping.get(from).getx();
			final int y1 = vertexMapping.get(from).gety();
			final int x2 = vertexMapping.get(to).getx();
			final int y2 = vertexMapping.get(to).gety();

			
			final Graphics g = getGraphics();
			
			final int midx = (x1 + x2) / 2;
			final int midy = (y1 + y2) / 2;

			g.drawLine(x1, y1, x2, y2); //Line drawn

			//The color of node is to be retained, because node we created earlier were just in RAM, we need to display them always
			//We always connect only two nodes with one edge

			//Node 1 (from)
			
			g.setColor(new Color(190, 255, 190));
			g.fillOval(x1 - 50 / 2, y1 - 50 / 2, 50, 50);
			g.setColor(Color.BLACK); //For font
			g.drawOval(x1 - 50 / 2, y1 - 50 / 2, 50, 50);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			g.drawString(new Integer(from).toString(), x1 - 5, y1 + 5); //Again, write the node number into the nodes

			//Node 2 (to)
			g.setColor(new Color(190, 255, 190));
			g.fillOval(x2 - 50 / 2, y2 - 50 / 2, 50, 50);
			g.setColor(Color.BLACK);
			g.drawOval(x2 - 50 / 2, y2 - 50 / 2, 50, 50);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			g.drawString(new Integer(to).toString(), x2 - 5, y2 + 5);

			//Drawing the edge
			g.drawString(new Integer(weight).toString(), midx - 5, midy + 5); //Writing edge weight on the edge, mid is used to give padding to
																			  //the text (edge weight) on the edge, edgeweight is written in mid
			
			// g[u].push_back(make_pair(v,w)) and g[v].push_back(make_pair(u,w))  ;) will be used in dijkstra algorithm below
			adjList.get(from).add(new Pair(to, weight));
			adjList.get(to).add(new Pair(from, weight));
		}

		//We clicked the button to find the shortest path
		 else if (e.getSource() == b) {
			final int source = Integer.valueOf(t1.getText());
			final int sink = Integer.valueOf(t2.getText());
			if (source < 0 || source > V || sink < 0 || sink > V) {
				t1.setText("INVALID");
				t2.setText("INVALID");
			}

			//Call
			shortestPath(source, sink);
		} 
	}

	//When we click on the main panel
	public void mouseClicked(final MouseEvent e) {
		// System.out.println(V);
		adjList.add(new ArrayList<Pair>());
		final Integer i = new Integer(V);

		//Find the physical coordinates in the panel
		final int x = e.getX();
		final int y = e.getY();

		//Add them to the vector of pairs (coordinates)
		vertexMapping.add(new Pair(x, y));

		//Size of node rectangle
		final int width = 50, height = 50;

		final String text = i.toString();
		final Graphics g = getGraphics();

		//Color of the nodes
		g.setColor(Color.blue);

		//where to color?
		g.fillOval(x-width/2, y - height / 2, width, height);

		g.setColor(Color.white);  //This black color is set latest, so it will be used in drawString, means the text inside the node
		g.drawOval(x - width / 2, y - height / 2, width, height); //Frame of circle
		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g.drawString(text, x - 5, y + 5); //padding of text inside the node
		V++;  //next number to be filled in next node
	}

	public void mouseEntered(final MouseEvent e) {
	}

	public void mouseExited(final MouseEvent e) {
	}

	public void mousePressed(final MouseEvent e) {
	}

	public void mouseReleased(final MouseEvent e) {
	}

	//Invert the color of shortest path nodes, will be called after dijktra is executed
	public void invertNode(final int vertex) {
		final Pair p = vertexMapping.get(vertex);
		final int x = p.getx();
		final int y = p.gety();
		final int width = 50, height = 50;
		final Graphics g = getGraphics();
		g.setColor(Color.BLACK);
		g.fillOval(x - width / 2, y - height / 2, width, height);
		g.setColor(Color.RED);
		g.drawOval(x - width / 2, y - height / 2, width, height);
		g.setColor(Color.WHITE);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g.drawString((new Integer(vertex)).toString(), x - 5, y + 5);
	}
	//Invert the color of shortest path edges, will be called after dijktra is executed
	public void invertEdge(final int vertex, final int par) {
		Graphics g = getGraphics();
		
		final Pair p = vertexMapping.get(vertex);
		final int x = p.getx();
		final int y = p.gety();
		g.setColor(Color.RED);
		final Pair p1 = vertexMapping.get(par);
		final int x1 = p1.getx();
		final int y1 = p1.gety();
		
		g.drawLine(x, y, x1, y1);
	}

	//Will be used to prune the edges, sink is passed as parameter to it, now apply the recursion you will get it easily
	//think, it is like the find function in disjoint set union
	public void printParentEdges(final int vertex, final int[] parent) {
		if (parent[vertex] == -1) {
			return;
		}
		printParentEdges(parent[vertex], parent);
		invertEdge(vertex, parent[vertex]);
	}

	//Same as above, but it is used to invert and prune the nodes, and we will color it invertly
	public void printParent(final int vertex, final int[] parent) {
		if (parent[vertex] == -1) {
			invertNode(vertex);
			return;
		}
		printParent(parent[vertex], parent);
		invertNode(vertex);
	}


	//DIJKSTRA
	public void shortestPath(final int src, final int sink) {

		//Initialise all the arrays
		final int[] dist = new int[V];
		final int[] parent = new int[V];
		final boolean[] doneWith = new boolean[V];
		for (int i = 0; i < V; i++) {
			dist[i] = INF;
			doneWith[i] = false;
			parent[i] = -1;
		}
		//src distance is always 0
		dist[src] = 0;
		final Comparator<Pair> comparator = new MyComparator();
		final PriorityQueue<Pair> q = new PriorityQueue<Pair>(comparator); //This will keep the pair which has least weight in increasing order
		q.add(new Pair(0, src)); //{distance, vertex}
		while (q.size() != 0) {
			final Pair p = q.poll(); //pop()
			final int u = p.gety(); //vertex
			if (doneWith[u])
				continue;
			doneWith[u] = true;
			final Iterator itr = adjList.get(u).iterator(); // all the connected vertices with the current vertex
			while (itr.hasNext()) {        //graph is like this {u, {v,w},{v1,w1}} means array of vector of pairs
				final Pair pr = (Pair) itr.next();
				final int v = pr.getx(); //child coordinate
				final int weight = pr.gety(); //weight
				if (!doneWith[v] && dist[v] > dist[u] + weight) {
					dist[v] = dist[u] + weight;
					parent[v] = u;
					q.add(new Pair(dist[v], v));
				}
			}
		}
		printParentEdges(sink, parent);
		printParent(sink, parent);
	}
}

class PathFinder {
	public static void main(final String str[]) {
		new DIJKSTRA();
	}
}