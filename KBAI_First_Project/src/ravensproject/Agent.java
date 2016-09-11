package ravensproject;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * Your Agent for solving Raven's Progressive Matrices. You MUST modify this
 * file.
 * 
 * You may also create and submit new files in addition to modifying this file.
 * 
 * Make sure your file retains methods with the signatures:
 * public Agent()
 * public char Solve(RavensProblem problem)
 * 
 * These methods will be necessary for the project's main method to run.
 * 
 */
public class Agent {
	
	private static int DELTA = 3;
    /**
     * The default constructor for your Agent. Make sure to execute any
     * processing necessary before your Agent starts solving problems here.
     * 
     * Do not add any variables to this signature; they will not be used by
     * main().
     * 
     */
    public Agent() {
        
    }
    
    public enum Shapes {
    	
    	NONE,
    	UNKNOWN,
    	SQUARE,
    	CIRCLE,
    	TRIANGLE,
    	RIGHT_TRIANGLE,
    	PACMAN,
    	DIAMOND,
    	PENTAGON,
    	HEXAGON,
    	OCTAGON,
    	HEART,
    	STAR
    	
    }
    
    public enum Textures {
    	HOLLOW,
    	SOLID,
    	STRIPED
    }
    
    public enum Sizes {
    	SMALL,
    	MEDIUM,
    	LARGE
    }
    
    public enum Transformations {
    	NONE,
    	SIZE,
    	REGION,
    	ROTATION,
    	TEXTURE
    }
    
    public class Pixel {
    	
    	public Pixel (int x, int y) {
    		this.xValue = x;
    		this.yValue = y;
    	}
    	
    	int xValue = 0;
    	int yValue = 0;
    	
    	public int getX() {
    		return this.xValue;
    	}
    	public int getY() {
    		return this.yValue;
    	}
    }
    
    public class Transformation {
    	
    	// TODO: Need a way of building a list of Transformations
    	// Need to be able to say a Shape transformed from a square to a circle
    	// Need to be able to say a Shape had a property changed, went from small to medium
    	// Rotate, Change size, change texture, isDeleted, isCreated, changedRegion
    	
    	public Transformation(Sizes sizeA, Sizes sizeB) {
    		this.transformation = Transformations.SIZE;
    		this.sizeA = sizeA;
    		this.sizeB = sizeB;
    	}
    	
		public Transformation(int regionA, int regionB) {
			this.transformation = Transformations.REGION;
			this.regionA = regionA;
			this.regionB = regionB;
    	}
		
		public Transformation(int rotation) {
			this.transformation = Transformations.ROTATION;
			this.rotation = rotation;
		}

		public Transformation(Textures textureA, Textures textureB) {
			this.transformation = Transformations.TEXTURE;
			this.textureA = textureA;
			this.textureB = textureB;
		}
    	
		private Transformations transformation = Transformations.NONE;
    	private Sizes sizeA;
    	private Sizes sizeB;
    	private int regionA;
    	private int regionB;
    	private int rotation;
    	private Textures textureA;
    	private Textures textureB;
    	
		public Transformations getTransformation() {
			return transformation;
		}
		public Sizes getSizeA() {
			return sizeA;
		}
		public Sizes getSizeB() {
			return sizeB;
		}
		public int getRegionA() {
			return regionA;
		}
		public int getRegionB() {
			return regionB;
		}
		public Textures getTextureA() {
			return textureA;
		}
		public Textures getTextureB() {
			return textureB;
		}
		public int getRotation() {
			return rotation;
		}
    }
    
    public class Shape {
    	
    	boolean isHollow;
    	boolean isSolid;
    	boolean isStriped;
    	int height = 0;
    	int width = 0;
    	int region = 0;
        Shapes shape = Shapes.NONE;    	
    	Textures texture = Textures.HOLLOW;
        Sizes size = Sizes.SMALL;
        int rotation = 0;
    	
        // Top left is 0,0
        // Bottom right is 183, 183
    	Pixel topMostPixel = new Pixel(0, 183);
    	Pixel bottomMostPixel = new Pixel(0, 0);
    	Pixel leftMostPixel = new Pixel(183, 0);
    	Pixel rightMostPixel = new Pixel(0, 0);
    	Pixel center = new Pixel(0,0);
    	
    	
    	public Shapes getShape() {
			return shape;
		}
		public void setShape(Shapes square) {
			this.shape = square;
		}
		public boolean isHollow() {
			return isHollow;
		}
		public void setHollow(boolean isHollow) {
			this.isHollow = isHollow;
		}
		public boolean isSolid() {
			return isSolid;
		}
		public void setSolid(boolean isSolid) {
			this.isSolid = isSolid;
		}
		public boolean isStriped() {
			return isStriped;
		}
		public void setStriped(boolean isStriped) {
			this.isStriped = isStriped;
		}
		public int getHeight() {
			return height;
		}
		public void setHeight(int height) {
			this.height = height;
		}
		public int getWidth() {
			return width;
		}
		public void setWidth(int width) {
			this.width = width;
		}
		public Pixel getTopMostPixel() {
			return topMostPixel;
		}
		public void setTopMostPixel(Pixel topMostPixel) {
			this.topMostPixel = topMostPixel;
		}
		public Pixel getBottomMostPixel() {
			return bottomMostPixel;
		}
		public void setBottomMostPixel(Pixel bottomMostPixel) {
			this.bottomMostPixel = bottomMostPixel;
		}
		public Pixel getLeftMostPixel() {
			return leftMostPixel;
		}
		public void setLeftMostPixel(Pixel leftMostPixel) {
			this.leftMostPixel = leftMostPixel;
		}
		public Pixel getRightMostPixel() {
			return rightMostPixel;
		}
		public void setRightMostPixel(Pixel rightMostPixel) {
			this.rightMostPixel = rightMostPixel;
		}
		public Pixel getCenter() {
			return center;
		}
		public void setCenter(Pixel center) {
			this.center = center;
		}
		public int getRegion() {
			return region;
		}
		public void setRegion(int region) {
			this.region = region;
		}
		public Textures getTexture() {
			return texture;
		}
		public void setTexture(Textures texture) {
			this.texture = texture;
		}
		public Sizes getSize() {
			return size;
		}
		public void setSize(Sizes size) {
			this.size = size;
		}
		public int getRotation() {
			return rotation;
		}
		public void setRotation(int rotation) {
			this.rotation = rotation;
		}
		
    }
    
    public class Diagram {
    	
    	public Diagram() {
    		
    	}
    	
    	String name = "";
    	boolean[][] matrix = new boolean[184][184];
    	List<Shape> shapeList = new ArrayList<Shape>();
    
    	public boolean isIdenticalMatch(Diagram d2) {
    		
    		for (int i = 0; i < 184; i++) {
    			for (int j = 0; j < 184; j++) {
    				if (this.getMatrix()[i][j] != d2.getMatrix()[i][j]) {
    					return false;
    				}
    			}
    		}    		
    		return true;
    	}
    	
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	
		public boolean[][] getMatrix() {
			return matrix;
		}
	
		public List<Shape> getShapeList() {
			return shapeList;
		}
		public void setShapeList(List<Shape> shapeList) {
			this.shapeList = shapeList;
		}
		
    }
       
    
    /**
     * The primary method for solving incoming Raven's Progressive Matrices.
     * For each problem, your Agent's Solve() method will be called. At the
     * conclusion of Solve(), your Agent should return an int representing its
     * answer to the question: 1, 2, 3, 4, 5, or 6. Strings of these ints 
     * are also the Names of the individual RavensFigures, obtained through
     * RavensFigure.getName(). Return a negative number to skip a problem.
     * 
     * Make sure to return your answer *as an integer* at the end of Solve().
     * Returning your answer as a string may cause your program to crash.
     * @param problem the RavensProblem your agent should solve
     * @return your Agent's answer to this problem
     */
    public int Solve(RavensProblem problem) {
    	
    	// All logic in here is for a 2x2 matrix
    	
    	//problem.get
    	if (!(
    			//problem.getName().equals("Basic Problem B-01") 
    			//problem.getName().equals("Basic Problem B-02")  
    			  problem.getName().equals("Basic Problem B-03") 
    			)) return -1;
    	
    	System.out.println("Name: " + problem.getName() + ", Type: " + problem.getProblemType());
    	
    	HashMap<String, Diagram> diagramList = buildDiagramList(problem);
    	    	   	
    	// TODO: Build the shapes in each diagram
    	
    	// Assuming one shape in each diagram
    	// Build that shape, determine what type of shape it is, find its size and center
    	// Add the shape object to its diagram
    	
    	for (String name : Arrays.asList("A", "B", "C", "1", "2", "3", "4", "5", "6")) {
    		
    		System.out.println("Building the shape in Diagram " + name);
    		Diagram diagram = diagramList.get(name);
    		List<Shape> shapeList = new ArrayList<Shape>();
    		
    		Shape shape = new Shape();
    		
    		// When looping through pixels we start at top left and work right and down
    		for (int j = 0; j < 184; j++) {
    			System.out.println();
    			
    			for (int i = 0; i < 184; i++) {
    				
    				if (diagram.getMatrix()[i][j]) {
	    				System.out.print("X");
	    				if (j > shape.getBottomMostPixel().getY()) shape.setBottomMostPixel(new Pixel(i, j));
	    				if (j < shape.getTopMostPixel().getY()) shape.setTopMostPixel(new Pixel(i, j));
	    				if (i < shape.getLeftMostPixel().getX()) shape.setLeftMostPixel(new Pixel(i, j));
	    				if (i > shape.getRightMostPixel().getX()) shape.setRightMostPixel(new Pixel(i, j));		
	    				
    				} else {
    					System.out.print("_");
    				}
    			}
    		}
    		
    		System.out.println("TopMost: (" + shape.getTopMostPixel().getX() + ", " + shape.getTopMostPixel().getY() + ")");
			System.out.println("BottomMost: (" + shape.getBottomMostPixel().getX() + ", " + shape.getBottomMostPixel().getY() + ")");
			System.out.println("LeftMost: (" + shape.getLeftMostPixel().getX() + ", " + shape.getLeftMostPixel().getY() + ")");
			System.out.println("RightMost: (" + shape.getRightMostPixel().getX() + ", " + shape.getRightMostPixel().getY() + ")");

			buildShape(diagram, shape);
			shapeList.add(shape);
			diagram.setShapeList(shapeList);
    	}
    	
    	
    	// Build up list of transformations between A->B and A->C
    	System.out.println("Building the transformations from A to B and from B to C");
    	List<Transformation> transformations = new ArrayList<Transformation>();
    	transformations.addAll(buildTransformations(diagramList.get("A"), diagramList.get("B")));
    	transformations.addAll(buildTransformations(diagramList.get("A"), diagramList.get("C")));
    	    	   	
    	for (Transformation t : transformations) {
    		System.out.println(t.getTransformation());
    	}
    	
    	// Use transformations on A to generate D by applying all transformations to A
    	Diagram D = generateSolutionDiagram(diagramList.get("A"), transformations);
    	
    	// Compare D to all of the available solutions
    	String chosenAnswer = "";
    	int lowestCount = Integer.MAX_VALUE;
    	
    	System.out.println("Comparing D to all of the available answers");
    	
    	for (String figure : Arrays.asList("1", "2", "3", "4", "5", "6")) {
    		int transformationCount = buildTransformations(D, diagramList.get(figure)).size();
    		
    		if (transformationCount < lowestCount) {
    			lowestCount = transformationCount;
    			chosenAnswer = figure;
    			System.out.println("Updating chosen answer to " + figure + ", with transformation count: " + transformationCount);
    		}    		
    	}
    	
    	System.out.println("Finishing " + problem.getName() + " and returning: " + chosenAnswer);
    	
    	rotateDiagram(diagramList.get("3"), 90);
    	
    	return Integer.parseInt(chosenAnswer);    	
    }
    
    private Diagram generateSolutionDiagram(Diagram A, List<Transformation> transformations) {
    	
    	Diagram solution = new Diagram();
    	List<Shape> shapeList = new ArrayList<Shape>();
    	solution.setName("D");
    	
    	Shape shape = A.getShapeList().get(0);
    	
    	// TODO: Apply all of the transformations that we have onto this shape
    	
    	for (Transformation t : transformations) {
    		
    		// TODO: We are setting the rotation variable, but not actually rotating the image
    		if (t.getTransformation() == Transformations.ROTATION) {
    			shape.setRotation(t.getRotation());
    		}
    		
    		if (t.getTransformation() == Transformations.REGION) {
    			shape.setRegion(t.getRegionB());
    		}

			if (t.getTransformation() == Transformations.SIZE) {
				shape.setSize(t.getSizeB());
			}
			
			if (t.getTransformation() == Transformations.TEXTURE) {
				shape.setTexture(t.getTextureB());
			}
    		
    	}
    	
    	// TODO: Should actually set the pixel matrix for this diagram
    	
    	shapeList.add(shape);
    	solution.setShapeList(shapeList);
    	
    	return solution;
    }

	private HashMap<String, Diagram> buildDiagramList(RavensProblem problem) {
		
		HashMap<String, Diagram> diagramList = new HashMap<String, Diagram>();
    	
    	// Look at every figure in this problem
    	for(String figureName : problem.getFigures().keySet()) {

    		System.out.println(figureName);
    		
    		RavensFigure figure = problem.getFigures().get(figureName);
    		
    		// Create a new diagram object to represent this figure
    		Diagram diagram = new Diagram();
    		diagram.setName(figure.getName());
    		    		
    		BufferedImage figureAImage = null;
    		
        	try {
        		figureAImage = ImageIO.read(new File(figure.getVisual()));        	
        	} catch(Exception ex) {}

        	// Look at every pixel to build the 2d array and add it to the diagram object
        	for(int i = 0; i < figureAImage.getWidth(); i++) {
        		//System.out.println();
        		for(int j = 0; j < figureAImage.getHeight(); j++) {
        		
        			int thisPixel = figureAImage.getRGB(j,i);        		
        			if (thisPixel != -1) {
        				//System.out.print("X");
        				diagram.getMatrix()[j][i] = true;
        			} else {
        				//System.out.print("_");
        			}
        		}	
        	}
        	
        	System.out.println("Adding diagram: " + diagram.getName());
    		diagramList.put(diagram.getName(), diagram);
    	}
		return diagramList;
	}
    
    // Build the transformations that occur between two diagrams
    private List<Transformation> buildTransformations(Diagram d1, Diagram d2) {
    	
    	List<Transformation> transformations = new ArrayList<Transformation>();
    	
    	if (d1.isIdenticalMatch(d2)) {
    		System.out.println("These two diagrams are identical, returning empty transformation list");
    		return transformations;
    	}
    	
    	// Compare the shape in d1 with the shape in d2
    	Shape s1 = d1.getShapeList().get(0);
    	Shape s2 = d2.getShapeList().get(0);
    	
    	// TODO: Once we have multiple shapes in each diagram, then need a way to map shapes in d1 with shapes in d2
    	// TODO: I should set the list of transformations on the Shape object, so that when I have multiple shapes I
    	// can say certain Transformations apply to specific shapes. IsDeleted is the only weird case
    	if (s1.getShape() == s2.getShape()) {
    		
    		System.out.println("The shapes are the same, figuring out transformations");
    		
    		if (s1.getRegion() != s2.getRegion()) {
    			transformations.add(new Transformation(s1.getRegion(), s2.getRegion()));
    		}
    		
    		if (s1.getTexture() != s2.getTexture()) {
    			transformations.add(new Transformation(s1.getTexture(), s2.getTexture()));
    		}
    		
    		if (s1.getSize() != s2.getSize()) {
    			transformations.add(new Transformation(s1.getSize(), s2.getSize()));
    		}
    		
    		if (s1.getRotation() != s2.getRotation()) {
    			if (s1.getRotation() < s2.getRotation()) {
    				transformations.add(new Transformation(s2.getRotation() - s1.getRotation()));
    			} else {
    				transformations.add(new Transformation(360 - Math.abs(s1.getRotation() - s2.getRotation())));
    			}
    		}
    		
    	} else {
    		System.out.println("The shapes are different");
    		
    		
    	}
    	
    	return transformations;    	
    }
    
    private void rotateDiagram(Diagram d, int rotation) {
    	
    	if (rotation % 90 != 0) {
    		System.out.println("Can't rotate unless a factor of 90 degrees");
    	}
    	
    	int degreesLeft = rotation;
    	while (degreesLeft > 0) {
    		
    	}
    	
    }
    
    private void rotateDiagram90Degrees(Diagram d) {
    	
    	// TODO: Is it just a rotation of the first shape
		
    	System.out.println("Rotating " + d.getName());
    	
		// a b c	h d a
		// d e f    i e b
		// h i j    j f c
		
		// a: [0, 0] to [0. 2] [y, (width - 1) - x]
		// b: [0. 1] to [1, 2]
		// c: [0, 2] to [2, 2]
		
    	// d: [1, 0] to [0, 1] [y, width - 1) - x]
		// e: [1. 1] to [1, 1]
		// f: [1, 2] to [2, 1]
    	
    	// h: [2, 0] to [0, 0] [y, width - 1) - x]
    	// i: [2. 1] to [1, 0]
    	// j: [2, 2] to [2, 0]
    	
    	for (int y = 0; y < 184; y++) {
			System.out.println("");
			for (int x = 0; x < 184; x++) {
				if (d.getMatrix()[x][y]) {
					System.out.print("X");
				} else {
					System.out.print("_");
				}
			}
		}
    	
    	boolean[][] rotatedMatrix = new boolean[184][184];
    	
		// rotate 90 degrees
		for (int y = 0; y < 184; y++) {
			for (int x = 0; x < 184; x++) {
				
				if (d.getMatrix()[x][y]) {
					rotatedMatrix[y][184 - 1 - x] = true;
				}
			}
		}
    	
		for (int y = 0; y < 184; y++) {
			System.out.println("");
			for (int x = 0; x < 184; x++) {
				if (rotatedMatrix[x][y]) {
					System.out.print("X");
				} else {
					System.out.print("_");
				}
			}
		}
    }
    
    private Shape buildShape(Diagram diagram, Shape shape) {
    	
    	discoverShapeType(diagram, shape);
    	shape.setHeight(shape.getBottomMostPixel().getY() - shape.getTopMostPixel().getY());
    	shape.setWidth(shape.getRightMostPixel().getX() - shape.getLeftMostPixel().getX());
    	shape.setCenter(new Pixel(shape.getLeftMostPixel().getX() + shape.getWidth()/2, shape.getTopMostPixel().getY() + shape.getHeight()/2));
    	shape.setRegion(findRegion(shape));
    	
    	return shape;
    }
    
    private int findRegion(Shape shape) {
    	
    	//012
    	//345
    	//678
    	int region = 0;
    	
    	if (shape.getCenter().getY() >= 122) {
    		region += 6;
    	} else if (shape.getCenter().getY() >= 61) {
    		region += 3;
    	}
    	
    	if (shape.getCenter().getX() >= 122) {
    		region += 2;
    	} else if (shape.getCenter().getX() >= 61) {
    		region += 1;
    	}
    	
    	return region;
    }
    
    // Look at our diagram, assuming only one shape is in the diagram, try to classify it as a specific shape
    private Shape discoverShapeType(Diagram diagram, Shape shape) {
    	
    	// Square: TopMost and BottomMost have same X, leftMost and RightMost have same Y
		// And top right is neabled
		if (shape.getTopMostPixel().getX() == shape.getBottomMostPixel().getX() 
				&& shape.getLeftMostPixel().getY() == shape.getBottomMostPixel().getY() 
				&& diagram.getMatrix()[shape.getRightMostPixel().getX()][shape.getTopMostPixel().getY()]) {
			
			System.out.println("Shape is SQUARE");
			shape.setShape(Shapes.SQUARE);
			return shape;
		}
		
		// CIRCLE
		if (compareVals(shape.getTopMostPixel().getX(), shape.getBottomMostPixel().getX()) 
				&& compareVals(shape.getLeftMostPixel().getY(), shape.getRightMostPixel().getY())  
				&& !diagram.getMatrix()[shape.getRightMostPixel().getX()][shape.getTopMostPixel().getY()] 
				&& !diagram.getMatrix()[shape.getRightMostPixel().getX()][shape.getBottomMostPixel().getY()] 
				&& !diagram.getMatrix()[shape.getLeftMostPixel().getX()][shape.getTopMostPixel().getY()] 
				&& !diagram.getMatrix()[shape.getLeftMostPixel().getX()][shape.getBottomMostPixel().getY()]) {

			System.out.println("Shape is Circle");
			shape.setShape(Shapes.CIRCLE);
			return shape;
		}
			
		//   x This is the default right triangle with rotation of zero
		//  xx
		// xxx
		if (shape.getTopMostPixel().getX() == shape.getRightMostPixel().getX() 
				&& shape.getLeftMostPixel().getY() == shape.getBottomMostPixel().getY() 
				&& !diagram.getMatrix()[shape.getLeftMostPixel().getX()][shape.getTopMostPixel().getY()]) {
			
			System.out.println("Shape is Right triangle with zero rotation");
			shape.setShape(Shapes.RIGHT_TRIANGLE);
			return shape;
		}     
		
		// xxx
		//  xx
		//   x
		// And top right is enabled
		if (shape.getTopMostPixel().getX() == shape.getLeftMostPixel().getX() 
				&& shape.getRightMostPixel().getY() == shape.getBottomMostPixel().getY() 
				&& !diagram.getMatrix()[shape.getLeftMostPixel().getX()][shape.getBottomMostPixel().getY()]) {
			
			System.out.println("Shape is Right triangle with 90 degree rotation");
			shape.setShape(Shapes.RIGHT_TRIANGLE);
			shape.setRotation(90);
		}     
		
		// xxx
		// xx
		// x
		// And top right is neabled
		if (shape.getTopMostPixel().getX() == shape.getBottomMostPixel().getX() 
				&& shape.getRightMostPixel().getY() == shape.getTopMostPixel().getY() 
				&& !diagram.getMatrix()[shape.getRightMostPixel().getX()][shape.getBottomMostPixel().getY()]) {
			
			System.out.println("Shape is Right triangle with 180 degree rotation");
			shape.setShape(Shapes.RIGHT_TRIANGLE);
			shape.setRotation(180);
			return shape;
		}     
		
		// x
		// xx
		// xxx
		if (shape.getTopMostPixel().getX() == shape.getBottomMostPixel().getX() 
				&& shape.getRightMostPixel().getY() == shape.getBottomMostPixel().getY() 
				&& !diagram.getMatrix()[shape.getRightMostPixel().getX()][shape.getTopMostPixel().getY()]) {
			
			System.out.println("Shape is Right triangle with 270 degree rotation");
			shape.setShape(Shapes.RIGHT_TRIANGLE);
			shape.setRotation(270);
			return shape;
		}       				
	
		

		
		//   x
		//  xxx
		// xxxxx
		if (compareVals(shape.getBottomMostPixel().getX(), shape.getLeftMostPixel().getX()) 
				&& compareVals(shape.getRightMostPixel().getY(), shape.getBottomMostPixel().getY()) 
				&& !diagram.getMatrix()[shape.getLeftMostPixel().getX()][shape.getTopMostPixel().getY()]
				&& !diagram.getMatrix()[shape.getRightMostPixel().getX()][shape.getTopMostPixel().getY()]) {
			
			System.out.println("Shape is Equalatiral Triangle");
			shape.setShape(Shapes.TRIANGLE);
			return shape;
		}
    	
		// TODO: Logic for other shapes, Heart, Start, 
		
		// TODO: Is the shape hollow or solid or striped?
		
		System.out.println("Unknown Shape");
		shape.setShape(Shapes.UNKNOWN);
    	return shape;
    }
    
    // Compare two diagrams to see how similar they are
//    private static int compare(Diagram d1, Diagram d2) {
//    	
//    	// TODO: I can probably delete this method
//    	
//    	System.out.println("Comparing " + d1.getName() + " with " + d2.getName());
//    	
//    	int transformationCount = 0;
//    	
//    	if (d1.isIdenticalMatch(d2)) {
//    		
//    	} else {
//    		transformationCount = 2;
//    	}
//    	
//    	System.out.println("Returning transformationCount: " + transformationCount);
//    	return transformationCount;
//    }
    
    private static boolean compareVals(int a, int b) {
    	System.out.println("Comparing: " + a + ", " + b);
    	if ( (a == b) || (Math.abs(a - b) <= DELTA)) {
    		System.out.println("returning true");
    		return true;
    	}
    	System.out.println("returning false");
    	return false;
    }
}
