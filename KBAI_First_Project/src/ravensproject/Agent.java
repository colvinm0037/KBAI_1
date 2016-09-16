package ravensproject;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    	TEXTURE,
    	ADDSHAPE,
    	DELETESHAPE
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
    	    	
    	public Transformation(int index, Sizes sizeA, Sizes sizeB) {
    		this.transformation = Transformations.SIZE;
    		this.indexOfShape = index;
    		this.sizeA = sizeA;
    		this.sizeB = sizeB;
    	}
    	
		public Transformation(int index, int regionA, int regionB) {
			this.transformation = Transformations.REGION;
			this.indexOfShape = index;
			this.regionA = regionA;
			this.regionB = regionB;
    	}
		
		public Transformation(int index, int rotation) {
			this.transformation = Transformations.ROTATION;
			this.indexOfShape = index;
			this.rotation = rotation;
		}

		public Transformation(int index, Textures textureA, Textures textureB) {
			this.transformation = Transformations.TEXTURE;
			this.indexOfShape = index;
			this.textureA = textureA;
			this.textureB = textureB;
		}
		
		public Transformation(boolean added, Shape shape) {
			if (added) {
				this.transformation = Transformations.ADDSHAPE;
			} else {
				this.transformation = Transformations.DELETESHAPE;
			}
			this.shape = shape;
		}
    	
		private Transformations transformation = Transformations.NONE;
		private int indexOfShape = -1;
    	private Sizes sizeA;
    	private Sizes sizeB;
    	private int regionA;
    	private int regionB;
    	private int rotation;
    	private Textures textureA;
    	private Textures textureB;
    	private Shape shape;
    	
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
		public Shape getShape() {
			return shape;
		}
		public int getIndexOfShape() {
			return indexOfShape;
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
    	boolean[][] shapeMatrix;
        
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
		public boolean[][] getShapeMatrix() {
			return shapeMatrix;
		}
		public void setShapeMatrix(boolean[][] shapeMatrix) {
			this.shapeMatrix = shapeMatrix;
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
    	System.out.println("Starting SOLVE");
    	
    	// TODO: Rotation stuff is mostly written but not really implemented
    	// TODO: Recognizing multiple shapes works! Need to finish integrating this logic.
    	// TODO: For multiple shapes, need to implement logic for mapping similar shapes from A to B
    	
    	// Multiple shapes are working well, basic 4 - 9 all fail
    	
    	if (!problem.getName().startsWith("Basic Problem B") && !problem.getName().startsWith("Challenge Problem B")) return -1;
    	
//    	if (!( 
//    			  problem.getName().equals("Basic Problem B-12") 
//    			)) return -1;
    	
    	System.out.println("Name: " + problem.getName() + ", Type: " + problem.getProblemType());
    	
    	HashMap<String, Diagram> diagramList = buildDiagramList(problem);
    	    	   	
    	for (String name : Arrays.asList("A", "B", "C", "1", "2", "3", "4", "5", "6")) {
    		
    		System.out.println("Looking at Diagram " + name);
    		Diagram diagram = diagramList.get(name);
    		List<Shape> shapeList = new ArrayList<Shape>();
    		
    		List<boolean[][]> shapeMatrices = discoverShapes(diagram);
    		
    		for (boolean[][] matrix : shapeMatrices) {
    			System.out.println("Creating a new Shape for this diagram");
	    		Shape shape = new Shape();
	    		
	    		// When looping through pixels we start at top left and work right and down
	    		for (int j = 0; j < 184; j++) {
	    			for (int i = 0; i < 184; i++) {
	    				if (matrix[i][j]) {
		    				if (j > shape.getBottomMostPixel().getY()) shape.setBottomMostPixel(new Pixel(i, j));
		    				if (j < shape.getTopMostPixel().getY()) shape.setTopMostPixel(new Pixel(i, j));
		    				if (i < shape.getLeftMostPixel().getX()) shape.setLeftMostPixel(new Pixel(i, j));
		    				if (i > shape.getRightMostPixel().getX()) shape.setRightMostPixel(new Pixel(i, j));		
	    				}
	    			}
	    		}
	    		
	    		System.out.println("TopMost: (" + shape.getTopMostPixel().getX() + ", " + shape.getTopMostPixel().getY() + ")");
				System.out.println("BottomMost: (" + shape.getBottomMostPixel().getX() + ", " + shape.getBottomMostPixel().getY() + ")");
				System.out.println("LeftMost: (" + shape.getLeftMostPixel().getX() + ", " + shape.getLeftMostPixel().getY() + ")");
				System.out.println("RightMost: (" + shape.getRightMostPixel().getX() + ", " + shape.getRightMostPixel().getY() + ")");
	
				buildShape(diagram, shape);
				shape.setShapeMatrix(matrix);
				shapeList.add(shape);				
	    	}
	    	
    		diagram.setShapeList(shapeList);
    	}
    	
    	// Build up list of transformations between A->B and A->C
    	System.out.println("Building the transformations from A to B and from B to C");
    	List<Transformation> transformations = new ArrayList<Transformation>();
    	transformations.addAll(buildTransformations(diagramList.get("A"), diagramList.get("B")));
    	transformations.addAll(buildTransformations(diagramList.get("A"), diagramList.get("C")));
    	    	   	
    	System.out.println("*** These are all of the transformations from A -> B and A-> C");
    	for (Transformation t : transformations) {
    		
    		if (t == null) System.out.println("OUCH");
    		
    		String shapeName = Shapes.NONE.toString();
    		if (t.getIndexOfShape() != -1) {
    			shapeName = diagramList.get("A").getShapeList().get(t.getIndexOfShape()).getShape().toString();
    		}
    		System.out.println(shapeName + ", " + t.getTransformation());
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
    
    // For a diagram, create a new matrix for each shape that has only that single shape on the matrix
    private List<boolean[][]> discoverShapes(Diagram masterDiagram) {
    	
    	boolean[][] checkedPixels = new boolean[184][184];
    	List<boolean[][]> matrixList = new ArrayList<boolean[][]>();
    	
    	for (int j = 0; j < 184; j++) {
    		for (int i = 0; i < 184; i++) {

    			if (!checkedPixels[i][j]) {
    				if (masterDiagram.getMatrix()[i][j]) {
    					matrixList.add(traverseShapeInMatrix(masterDiagram.getMatrix(), checkedPixels, new Pixel(i, j)));
    				}
    			}    				
    		}
    	}
    	
    	return matrixList;    	
    }
    
    private boolean[][] traverseShapeInMatrix(boolean[][] masterMatrix, boolean[][] checkedPixels, Pixel start) {
    	
    	System.out.println("Beginning traverseShapeInMatrix with starting pixel (" + start.getX() + ", " + start.getY() + ")");
    	
    	boolean[][] newShapeMatrix = new boolean[184][184];
    	
    	List<Pixel> recentPixels = new ArrayList<Pixel>();
    	recentPixels.add(start);
    	
    	while (recentPixels.size() > 0) {
    		
    		List<Pixel> newPixelList = new ArrayList<Pixel>();
    		
    		for (Pixel p : recentPixels) {
    			
    			if (!checkedPixels[p.getX()][p.getY()]) {
    				checkedPixels[p.getX()][p.getY()] = true;
    			} else {
    				continue;
    			}
    			
    			if (masterMatrix[p.getX()][p.getY()]) {
    				
    				newShapeMatrix[p.getX()][p.getY()] = true;
    				
    				// Add left if not already checked
    				if (p.getX() > 1 && !checkedPixels[p.getX() - 1][p.getY()]) {
    					newPixelList.add(new Pixel(p.getX() - 1, p.getY()));
    				}
    				
    				// Add right if not already checked
    				if (p.getX() < 182 && !checkedPixels[p.getX() + 1][p.getY()]) {
    					newPixelList.add(new Pixel(p.getX() + 1, p.getY()));
    				}
    				
    				// Add below if not already checked
    				if (p.getY() < 182 && !checkedPixels[p.getX()][p.getY() + 1]) {
    					newPixelList.add(new Pixel(p.getX(), p.getY() + 1));
    				}
    				
    				// Add above if not already checked
    				if (p.getY() > 1 && !checkedPixels[p.getX()][p.getY() - 1]) {
    					newPixelList.add(new Pixel(p.getX(), p.getY() - 1));
    				}
    				
    			}
    		}
    		
    		recentPixels.clear();
    		recentPixels.addAll(newPixelList);
    	}
    	
//    	for (int j = 0; j < 184; j++) {
//    		System.out.println();
//    		for (int i = 0; i < 184; i++) {
//    			if (newShapeMatrix[i][j]) {
//	    			System.out.print("X");
//    			} else {
//    				System.out.print("_");
//    			}
//    		}
//    	}
    	
    	return newShapeMatrix;
    }
    
    
    private Diagram generateSolutionDiagram(Diagram A, List<Transformation> transformations) {
    	
    	System.out.println("Beginning generateSolution");
    	
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
			
			if (t.getTransformation() == Transformations.ADDSHAPE) {
				shapeList.add(t.getShape());
    		}
    	
    		if (t.getTransformation() == Transformations.DELETESHAPE) {
    			// TODO: Need to delete the shape
    		}
    		
    	}
    	
    	// TODO: Should actually set the pixel matrix for this diagram
    	
    	System.out.println("Done generating the shape in D");
    	System.out.println("Shape: " + shape.getShape());
    	System.out.println("Region: " + shape.getRegion());
    	System.out.println("Size: " + shape.getSize());
    	System.out.println("Rotation: " + shape.getRotation());
    	
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
        	
        	//System.out.println("Adding diagram: " + diagram.getName());
    		diagramList.put(diagram.getName(), diagram);
    	}
		return diagramList;
	}
    
    // Build the transformations that occur between two diagrams
    private List<Transformation> buildTransformations(Diagram d1, Diagram d2) {
    	
    	System.out.println("Beginning buildTransformations, comparing " + d1.getName() + " with " + d2.getName());
    	
    	List<Transformation> transformations = new ArrayList<Transformation>();
    	
    	if (d1.isIdenticalMatch(d2)) {
    		System.out.println("These two diagrams are identical, returning empty transformation list");
    		return transformations;
    	}
    	
    	Map<Integer, Integer> mapping = new HashMap<Integer, Integer>();
		List<Integer> matchedShapes = new ArrayList<Integer>();
    	
    		
		// In terms of building a mapping, an object can move position, rotate, or change texture
		
		// TODO: This needs to be tested!
		// TODO: I still need to think through how these transformations work, shouldn't transformations be related to shapes?
		
		// TODO: For now, don't worry about shapes changing size
		
		// For each shape in d1
		for (int i = 0; i < d1.getShapeList().size(); i++) {
		
			Shape baseShape = d1.getShapeList().get(i);
			
			int indexOfBestMatch = 0;
    		Shape partnerShape = null;
			
    		// Look at all shapes in d2
    		for (int j = 0; j < d2.getShapeList().size(); j++) {
    			
    			// If we have already matched up this shape then skip it
    			if (matchedShapes.contains(j)) continue;
    			
    			Shape compareShape = d2.getShapeList().get(j);
    			
    			// If the shape types match then map them
    			if (baseShape.getShape() == compareShape.getShape()) {
    			
    				// Are they about the same size?
    				if ( (Math.abs(baseShape.getWidth() - compareShape.getWidth()) < 5) 
    						&& (Math.abs(baseShape.getHeight() - compareShape.getHeight()) < 5)) {	    					
    					partnerShape = compareShape;
    					indexOfBestMatch = j;
    				}
    			}
    		}
    		
    		if (partnerShape != null) {
    			matchedShapes.add(indexOfBestMatch);
    			mapping.put(i, indexOfBestMatch);
    			
    		} else {
    			mapping.put(i, -1);
    		}
		}
    	

    	// Create transformations for every shape in d1
    	for (Map.Entry<Integer, Integer> entry : mapping.entrySet()) {
    	    Integer key = entry.getKey();
    	    Integer value = entry.getValue();
    	    
    	    if (value == -1) {
    	    	
    	    	// This shape not mapped to any other shapes, add a delete shape transformation
    	    	System.out.println("Creating a DELETE Transformation");
    	    	transformations.add(new Transformation(false, d1.getShapeList().get(key)));
    	    	
    	    } else {    	    	
    	    	Transformation t = sameShapeTransform(key, d1.getShapeList().get(key), d2.getShapeList().get(value));
    	    	if (t != null) transformations.add(t);    	    	
    	    }
    	}
    	
    	// Need to check for any remaining shapes in d2 that didn't get mapped to a shape in d1
		for (int i = 0; i < d2.getShapeList().size(); i++) {
		
			if (matchedShapes.contains(i)) continue;
			System.out.println("Creating an ADD transformation");
			transformations.add(new Transformation(true, d2.getShapeList().get(i)));		
		}
    	
    	System.out.println("Done building transformations, returning list of size: " + transformations.size());
    	return transformations;    	
    }
    
    private Transformation sameShapeTransform(int indexOfShape, Shape s1, Shape s2) {
    	
    	Transformation transformation = null;
    	
    	if (s1.getRegion() != s2.getRegion()) {
			transformation = new Transformation(indexOfShape, s1.getRegion(), s2.getRegion());
		}
		
		if (s1.getTexture() != s2.getTexture()) {
			transformation = new Transformation(indexOfShape, s1.getTexture(), s2.getTexture());
		}
		
		if (s1.getSize() != s2.getSize()) {
			transformation = new Transformation(indexOfShape, s1.getSize(), s2.getSize());
		}
		
		if (s1.getRotation() != s2.getRotation()) {
			if (s1.getRotation() < s2.getRotation()) {
				transformation = new Transformation(indexOfShape, s2.getRotation() - s1.getRotation());
			} else {
				transformation = new Transformation(indexOfShape, 360 - Math.abs(s1.getRotation() - s2.getRotation()));
			}
		}
    	
		return transformation;
    }
    
    private void rotateDiagram(Diagram d, int rotation) {
    	
    	if (rotation % 90 != 0) {
    		System.out.println("Can't rotate unless a factor of 90 degrees");
    	}
    	
    	int degreesLeft = rotation;
   
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
    	determineShapeFill(diagram, shape);
    	
    	System.out.println("Shape is solid: " + shape.isSolid() + ", isHollow: " + shape.isHollow());
    	
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
				&& shape.getTopMostPixel().getY() == shape.getRightMostPixel().getY() 
				&& diagram.getMatrix()[shape.getRightMostPixel().getX()][shape.getBottomMostPixel().getY()]) {
			
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
		if (compareVals(shape.getTopMostPixel().getX(), shape.getRightMostPixel().getX()) 
				&& compareVals(shape.getLeftMostPixel().getY(), shape.getBottomMostPixel().getY()) 
				&& !diagram.getMatrix()[shape.getLeftMostPixel().getX()][shape.getTopMostPixel().getY()]) {
			
			System.out.println("Shape is Right triangle with zero rotation");
			shape.setShape(Shapes.RIGHT_TRIANGLE);
			return shape;
		}     
		
		// xxx
		//  xx
		//   x
		// And top right is enabled
		if (compareVals(shape.getTopMostPixel().getX(), shape.getLeftMostPixel().getX()) 
				&& compareVals(shape.getRightMostPixel().getY(), shape.getBottomMostPixel().getY())
				&& !diagram.getMatrix()[shape.getLeftMostPixel().getX()][shape.getBottomMostPixel().getY()]) {
			
			System.out.println("Shape is Right triangle with 90 degree rotation");
			shape.setShape(Shapes.RIGHT_TRIANGLE);
			shape.setRotation(90);
		}     
		
		// xxx
		// xx
		// x
		// And top right is neabled
		if (compareVals(shape.getTopMostPixel().getX(), shape.getBottomMostPixel().getX()) 
				&& compareVals(shape.getRightMostPixel().getY(), shape.getTopMostPixel().getY())
				&& !diagram.getMatrix()[shape.getRightMostPixel().getX()][shape.getBottomMostPixel().getY()]) {
			
			System.out.println("Shape is Right triangle with 180 degree rotation");
			shape.setShape(Shapes.RIGHT_TRIANGLE);
			shape.setRotation(180);
			return shape;
		}     
		
		// x
		// xx
		// xxx
		if (compareVals(shape.getTopMostPixel().getX(), shape.getBottomMostPixel().getX())
				&& compareVals(shape.getRightMostPixel().getY(), shape.getBottomMostPixel().getY())
				&& !diagram.getMatrix()[shape.getRightMostPixel().getX()][shape.getTopMostPixel().getY()]) {
			
			System.out.println("Shape is Right triangle with 270 degree rotation");
			shape.setShape(Shapes.RIGHT_TRIANGLE);
			shape.setRotation(270);
			return shape;
		}       				
	
//    	Building the shape in Diagram B
//    	TopMost: (28, 28)
//    	BottomMost: (28, 155)
//    	LeftMost: (28, 28)
//    	RightMost: (155, 155)
//    	returning true
//    	Shape is Right triangle with 270 degree rotation
//    	
//    	Building the shape in Diagram 4
//    	TopMost: (26, 26)
//    	BottomMost: (26, 157)
//    	LeftMost: (26, 26)
//    	RightMost: (157, 154)
//    	

		
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
    
    private void determineShapeFill(Diagram diagram, Shape shape) {
    	
    	// for square and circle draw a line from left to right and top to bottom to see if all solid
    	
    	
    	if (shape.getShape() == Shapes.SQUARE || shape.getShape() == Shapes.CIRCLE) {
    		
    		// Look at every pixel from left edge to right edge in middle line
    		int yValue = shape.getBottomMostPixel().getY() - (shape.getHeight() / 2);
    		for (int i = shape.getLeftMostPixel().getX(); i <= shape.getRightMostPixel().getX(); i++) {
    			
    			if (!diagram.getMatrix()[i][yValue]) {
    				shape.setHollow(true);
    				return;
    			}
    		}
    		
    		// Look at every pixel from top to bottom in middle line
    		int xValue = shape.getRightMostPixel().getX() - (shape.getWidth() / 2);
    		for (int i = shape.getTopMostPixel().getY(); i <= shape.getBottomMostPixel().getY(); i++) {
    			
    			if (!diagram.getMatrix()[xValue][i]) {
    				shape.setHollow(true);
    				return;
    			}
    		}
    		
    		shape.setSolid(true);
    		return;
    	}
    	
    	if (shape.getShape() == Shapes.RIGHT_TRIANGLE) {
    	    determineFillForRightTriangle(diagram, shape);
    	}
    	
    	// TODO: Don't worry about partially filled shapes for now
    	
    }
    
    private void determineFillForRightTriangle(Diagram diagram, Shape shape) {
    	
    	boolean solidVertical = false;
    	boolean solidHorizontal = false;
    	
    	if (shape.getRotation() == 0) {
    		solidVertical = checkVerticalTriangleLine(diagram, shape, true);
			solidHorizontal = checkHorizontalTriangleLine(diagram, shape, true);
		} else if (shape.getRotation() == 90) {
			solidVertical = checkVerticalTriangleLine(diagram, shape, false);
			solidHorizontal = checkHorizontalTriangleLine(diagram, shape, true);    			
		} else if (shape.getRotation() == 180) {
			solidVertical = checkVerticalTriangleLine(diagram, shape, false);
			solidHorizontal = checkHorizontalTriangleLine(diagram, shape, false);
		} else if (shape.getRotation() == 270) {
			solidVertical = checkVerticalTriangleLine(diagram, shape, true);
			solidHorizontal = checkHorizontalTriangleLine(diagram, shape, false);
		}
    	
		System.out.println("vertical: " + solidVertical + ", horizontal: " + solidHorizontal);
		
    	if (solidVertical && solidHorizontal) {
    		System.out.println("The triangle is SOLID");
    		shape.setSolid(true);
    	} else if (!solidVertical && !solidHorizontal) {
    		System.out.println("The triangle is HOLLOW");
    		shape.setHollow(true);
    	} else {
    		System.out.println("The triangle is STRIPED");
    		shape.setStriped(true);
    	}
    	
    }
    
    private boolean checkVerticalTriangleLine(Diagram diagram, Shape shape, boolean topToBottom) {
    	
    	boolean hitEdge = false;
    	boolean isSolidLine = true;
    	int xValue = shape.getRightMostPixel().getX() - (shape.getWidth() / 2);
    	
    	if (topToBottom) {
			for (int i = shape.getTopMostPixel().getY(); i <= shape.getBottomMostPixel().getY(); i++) {		
				if (!hitEdge) {
					if (diagram.getMatrix()[xValue][i]) {
						hitEdge = true;					
					}
					continue;
				}
				if (!diagram.getMatrix()[xValue][i]) {
					isSolidLine = false;
					break;
				}
			}
    	} else {
			for (int i = shape.getBottomMostPixel().getY(); i >= shape.getTopMostPixel().getY(); i--) {		
				if (!hitEdge) {
					if (diagram.getMatrix()[xValue][i]) {
						hitEdge = true;					
					}
					continue;
				}
				if (!diagram.getMatrix()[xValue][i]) {
					isSolidLine = false;
					break;
				}
			}
    	}
		
		return isSolidLine;
    }
    
    private boolean checkHorizontalTriangleLine(Diagram diagram, Shape shape, boolean leftToRight) {
    	
	 	boolean hitEdge = false;
    	boolean isSolidLine = true;
    	int yValue = shape.getBottomMostPixel().getY() - (shape.getHeight() / 2);
    	
    	if (leftToRight) {
			for (int i = shape.getLeftMostPixel().getX(); i <= shape.getRightMostPixel().getX(); i++) {					
				if (!hitEdge) {
					if (diagram.getMatrix()[i][yValue]) {
						hitEdge = true;					
					}
					continue;
				}
				
				if (!diagram.getMatrix()[i][yValue]) {
					isSolidLine = false;
					break;
				}
			}
    	} else {
			for (int i = shape.getRightMostPixel().getX(); i >= shape.getLeftMostPixel().getX(); i--) {				
				if (!hitEdge) {
					if (diagram.getMatrix()[i][yValue]) {
						hitEdge = true;
					}
					continue;
				}
				if (!diagram.getMatrix()[i][yValue]) {
					isSolidLine = false;
					break;
				}
			}
    	}
		
		return isSolidLine;
    }
    
    private static boolean compareVals(int a, int b) {
    	if ( (a == b) || (Math.abs(a - b) <= DELTA)) {
    		return true;
    	}
    	return false;
    }
}
