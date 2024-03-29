package ravensproject;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
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
	
	private static int DELTA = 15;
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
    	UNKNOWN_1,
    	UNKNOWN_2,
    	UNKNOWN_3,
    	UNKNOWN_4,
    	UNKNOWN_5,
    	UNKNOWN_6,
    	UNKNOWN_7,
    	UNKNOWN_8,
    	UNKNOWN_9,
    	SQUARE,
    	RECTANGLE,
    	CIRCLE,
    	TRIANGLE,
    	RIGHT_TRIANGLE,
    	PACMAN,
    	DIAMOND,
    	PENTAGON,
    	HEXAGON,
    	OCTAGON,
    	HEART,
    	STAR,
    	PLUS
    }
    
    public enum Textures {
    	HOLLOW,
    	SOLID,
    	STRIPED
    }
    
    public enum Fills {
    	NONE,
    	LEFT,
    	RIGHT,
    	TOP,
    	BOTTOM,
    	TOP_RIGHT,
    	TOP_LEFT,
    	BOTTOM_RIGHT,
    	BOTTOM_LEFT
    }
    
    public enum Sizes {
    	SMALL,
    	MEDIUM,
    	LARGE,
    	EXTRA_LARGE
    }
    
    public enum Transformations {
    	NONE,
    	SIZE,    
    	ROTATION,
    	TEXTURE,
    	ADDSHAPE,
    	DELETESHAPE,
    	MIRRORING_XAXIS,
    	MIRRORING_YAXIS,
    	HALF_FILL,
    	SHIFT_X,
    	SHIFT_Y
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
	
    	public Transformation(int index, boolean xShift, int centerX, int centerY) {
    		if (xShift) {
    			this.transformation = Transformations.SHIFT_X;
    			this.centerX = centerX;
    		} else {
    			this.transformation = Transformations.SHIFT_Y;
    			this.centerY = centerY;
    		}
    		this.indexOfShape = index;
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
		
		public Transformation(int index, boolean added, Shape shape) {
			if (added) {
				this.transformation = Transformations.ADDSHAPE;
			} else {
				this.transformation = Transformations.DELETESHAPE;
			}
			this.indexOfShape = index;
			this.shape = shape;
		}
		
		public Transformation(int index, boolean xAxis) {
			
			if (xAxis) {
				this.transformation = Transformations.MIRRORING_XAXIS;
			} else {
				this.transformation = Transformations.MIRRORING_YAXIS;
			}
			this.indexOfShape = index;
		}
    	
		public Transformation(int index, Fills fillsA, Fills fillsB) {
			this.transformation = Transformations.HALF_FILL;
			this.fillsA = fillsA;
			this.fillsB = fillsB;
			this.indexOfShape = index;
		}

    	private List<Integer> regionsA = new ArrayList<Integer>();
    	private List<Integer> regionsB = new ArrayList<Integer>();
		private Transformations transformation = Transformations.NONE;
		private int indexOfShape = -1;
    	private Sizes sizeA;
    	private Sizes sizeB;
    	private List<Integer> xRegions = new ArrayList<Integer>();
    	private List<Integer> yRegions = new ArrayList<Integer>();
    	private int rotation;
    	private Textures textureA;
    	private Textures textureB;
    	private Shape shape;
    	private Fills fillsA;
    	private Fills fillsB;
    	private int centerX;
    	private int centerY;
    	
		public int getCenterX() {
			return centerX;
		}
		public void setCenterX(int centerX) {
			this.centerX = centerX;
		}
		public int getCenterY() {
			return centerY;
		}
		public void setCenterY(int centerY) {
			this.centerY = centerY;
		}
		public Transformations getTransformation() {
			return transformation;
		}
		public Sizes getSizeA() {
			return sizeA;
		}
		public Sizes getSizeB() {
			return sizeB;
		}
		public List<Integer> getXRegions() {
			return xRegions;
		}
		public List<Integer> getYRegions() {
			return yRegions;
		}		
		public List<Integer> getRegionsA() {
			return regionsA;
		}
		public List<Integer> getRegionsB() {
			return regionsB;
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
		public Fills getFillsA() {
			return fillsA;
		}
		public Fills getFillsB() {
			return fillsB;
		}
    }
    
    public class Shape {
    	
    	boolean isHollow;
    	boolean isSolid;
    	boolean isStriped;
    	boolean isCentered;
    	int height = 0;
    	int width = 0;

        Shapes shape = Shapes.NONE;    	
    	Textures texture = Textures.HOLLOW;
        Sizes size = Sizes.SMALL;
        Fills fills = Fills.NONE;
        int rotation = 0;
    	boolean[][] shapeMatrix;
        
        // Top left is 0,0, Bottom right is 183, 183
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
			if (this.isHollow) this.texture = Textures.HOLLOW;
		}
		public boolean isSolid() {
			return isSolid;
		}
		public void setSolid(boolean isSolid) {
			this.isSolid = isSolid;
			if (this.isSolid) this.texture = Textures.SOLID;
		}
		public boolean isStriped() {
			return isStriped;
		}
		public void setStriped(boolean isStriped) {
			this.isStriped = isStriped;
			if (this.isStriped) this.texture = Textures.STRIPED;
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
			
			if ( (center.getX() > 86 && center.getX() < 98) 
					&& (center.getY() > 86 && center.getY() < 98) ) {
				
				this.isCentered = true;
			}
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
		public Fills getFills() {
			return fills;
		}
		public void setFills(Fills fills) {
			this.fills = fills;
		}
		public boolean isCentered() {
			return isCentered;
		}
		public void setCentered(boolean isCentered) {
			this.isCentered = isCentered;
		}		
		public boolean isUnknownShape() {
			
			if (shape == Shapes.UNKNOWN_1 || shape == Shapes.UNKNOWN_2 || shape == Shapes.UNKNOWN_3 || shape == Shapes.UNKNOWN_4
	    			|| shape == Shapes.UNKNOWN_5 || shape == Shapes.UNKNOWN_6 || shape == Shapes.UNKNOWN_7 || shape == Shapes.UNKNOWN_8
	    			|| shape == Shapes.UNKNOWN_9 ) {
				return true;
			}
			return false;
		}		
		public String toString() {
			return "Shape: " + shape + ", Texture: " + texture.toString() + ", HalfFill: " + fills + ", Size: " + size + ", Rotation: " + rotation + ", Center: (" + 
					center.getX() + ", " + center.getY() + "), Height: " + height + ", Width: " + width;
		}
    }
    
    public class Diagram {
    	 	
    	String name = "";
    	boolean[][] matrix = new boolean[184][184];
    	List<Shape> shapeList = new ArrayList<Shape>();
    	int pixelCount = 0;
    	
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
		public int getPixelCount() {
			return pixelCount;
		}
		public void setPixelCount(int pixelCount) {
			this.pixelCount = pixelCount;
		}
    }
           
    private List<Shape> unknownShapes = new ArrayList<Shape>();
    private boolean disableMirroring = false;
    private boolean disableSizing = true;
    private boolean disableDeletionIndexing = true;
    
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
    	
    	// NOTE: WHEN SUBMITTING REMEMBER TO MOVE FILES!
    	
    	// Long-term issues
    	// TODO: Need to be able to handle 3x3 Problems
    	// TODO: Many questions have overlapping shapes, what can I do to handle those? Traversal method doesn't work.
    	// TODO: Actually create an image/matrix of D so that I can check it visually
    	// TODO: Need to be able to recognize rectangles as shapes
    	// TODO: Need a comprehensive method of generating a solution, comparing it to available answers, and then
    	// going back and generating another solution if we didn't get a good enough answer that makes sense
    	// TODO: Refactor code
    	// TODO: PACMAN Shapes are considered unknown when they have 45 degree rotations     	
    	
    	
    	// ** Project 3 **
    	
    	// Problem Set D (Harder?)
    	// 01: Simple (Working)
    	// 02: Shape1 -> Shape2 -> Shape3 -> Shape1 (Not working)
    	// 03: Pattern1 -> Pattern2 -> Pattern3 -> Pattern1 (Working)
    	// 04: One shape changes, other doesn't (Not working, but should be it seems like?)
    	// 05: Hard question (Working)
    	// 06: Basic, adding and removing shapes (Not Working)
    	// 07: Multiple patterns, like #2, but more complex (Not working)
    	// 08: Shape order like #2, but texture changes (Not working)
    	// 09: Shape order like #2, but also with addition/subtraction of pattern (Not working)
    	// 10: Shape order like #2, but two over lapping strings of shapes (Not working)
    	// 11: Keep shape outline, but invert contents (Not working)
    	// 12: Shape order like #2, but changing number of shapes. (Not working)
    	
    	// Ideas for D:
    	
    	// Problem Set E (Easier?)
    	// 01: Adding shapes together
    	// 02: Adding shapes together
    	// 03: Adding shapes together, with some overlap
    	// 04: Subtracting shapes
    	// 05: Shapes changing
    	// 06: Adding shapes together, in a strange order
    	// 07: Adding and subtracting 
    	// 08: Adding and subtracting
    	// 09: Shapes changing with odd reasoning, could work by splitting diagrams in two (Not working)
    	// 10: Pixel subtraction
    	// 11: Pixels that exist in first two are what the third is
    	// 12: Shape deletion depending on what is in the next frame

    	// All of the problems in these two problems sets are easily handled by only looking at horizontal relationships. Some 
    	// could use the benefit of looking the diagonal relationship.
    	
    	// Basic Problems
    	// 1 - Star and pentagon are same type of unknown
    	
    	// Challenge Problems
    	// 1 - Triangles only work with zero rotation
    	
    	// if (!( problem.getName().equals("Basic Problem D-04"))) return -1;

    	disableMirroring = false;
    	disableSizing = true;
    	disableDeletionIndexing = true;
    	unknownShapes.clear();
    	
    	boolean isThreeByThree = false;
    	if (problem.getProblemType().equals("3x3")) {
    		isThreeByThree = true;
    		disableMirroring = true;
    		disableSizing = false;
    		disableDeletionIndexing = false;
    	}
    	
    	System.out.println("Name: " + problem.getName() + ", Type: " + problem.getProblemType());
    	
    	HashMap<String, Diagram> diagramList = buildDiagramList(problem);
    	    
    	buildShapesInDiagrams(diagramList, isThreeByThree);
    
    	String chosenAnswer = "";
    	if (!isThreeByThree) {    		
    		chosenAnswer = determineFinalAnswerFor2x2(diagramList);
    	} else {
    		chosenAnswer = determineFinalAnswerFor3x3(diagramList);
    	}
         	
    	System.out.println("Finishing " + problem.getName() + " and returning: " + chosenAnswer);
    	return Integer.parseInt(chosenAnswer);    	
    }
    
    // Build a list of Shapes in each Matrix Node
    private void buildShapesInDiagrams(HashMap<String, Diagram> diagramList, boolean isThreeByThree) {
    	
    	for (String name : Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "1", "2", "3", "4", "5", "6", "7", "8")) {
    		   		
    		System.out.println("\nLOOKING AT DIAGRAM " + name);
    		Diagram diagram = diagramList.get(name);
    		
    		if (diagram == null) continue;
    		diagram.setPixelCount(countPixels(diagram));
    		
    		List<Shape> shapeList = new ArrayList<Shape>();
    		
    		List<boolean[][]> shapeMatrices = discoverShapes(diagram);
    		
    		for (boolean[][] matrix : shapeMatrices) {
    			System.out.println("Creating a new Shape for this diagram");
	    		Shape shape = new Shape();
				buildShape(matrix, shape);
				shapeList.add(shape);				
	    	}
	    	
    		diagram.setShapeList(shapeList);
    	}	
    }
    
    // TODO: This area is messy
    private String determineFinalAnswerFor2x2(HashMap<String, Diagram> diagramList) {
    	
    	// Compare D to all of the available solutions
    	
    	// Build Transformations between A->B and A->C
    	List<Transformation> bTransformations = buildTransformations(diagramList.get("A"), diagramList.get("B"));
    	printTransformations(bTransformations, diagramList.get("A"));
    	List<Transformation> cTransformations = buildTransformations(diagramList.get("A"), diagramList.get("C"));
    	printTransformations(cTransformations, diagramList.get("A"));
    	
    	List<Transformation> bcTransformations = new ArrayList<Transformation>();
    	bcTransformations.addAll(bTransformations);
    	bcTransformations.addAll(cTransformations);
    	
    	// Generate solutions by applying transformations from A->B onto C, and from A->C onto B
    	Diagram Db = generateSolutionDiagram(diagramList.get("C"), bTransformations);
    	Diagram Dc = generateSolutionDiagram(diagramList.get("B"), cTransformations);
    	Diagram Dbc = generateSolutionDiagram(diagramList.get("A"), bcTransformations);
    	
    	System.out.println("\nComparing D to all of the available answers");
    	
    	ArrayList<Diagram> solutionDiagrams = new ArrayList<Diagram>();
    	solutionDiagrams.add(Db);
    	solutionDiagrams.add(Dc);

    	boolean onlyDeletes = true;
    	for (Transformation t : bcTransformations) {
    		if (t.getTransformation() != Transformations.DELETESHAPE) {
    			onlyDeletes = false;
    			break;
    		}
    	}
    	
    	solutionDiagrams.add(Dbc);
    	
    	String chosenAnswer = "";
    	int lowestCount = Integer.MAX_VALUE;
    	int transformationCount = 0;
    	
    	// TODO: Crappy code to loop through 3 different strategies for determining final answer
    	for (int i = 0; i < 3; i++) {

    		Diagram D = solutionDiagrams.get(i);
    		
    		if (i == 2) {
    			if (!(onlyDeletes || lowestCount > 0)) {
    				continue;
    			}
    		}
    		
    		for (String figure : Arrays.asList("1", "2", "3", "4", "5", "6")) {
	    		
	    		System.out.println("Comparing D to " + figure);	    		
	    		if (D.isIdenticalMatch(diagramList.get(figure))) {
	    			System.out.println("***Is Identical Match");
	    			chosenAnswer = figure;
	    			break;
	    		}
	    		
	        	List<Transformation> transformations2 = new ArrayList<Transformation>();        	
	        	transformations2.addAll(buildTransformations(D, diagramList.get(figure)));
	        	
	        	transformationCount = transformations2.size();
	    		System.out.println("TransformationCount: " + transformationCount);
	    		System.out.println("*** These are all of the transformations from D -> " + figure);
	        	for (Transformation t : transformations2) System.out.println(t.getTransformation());
	        	
	    		if (transformationCount <= lowestCount) {
	    			lowestCount = transformationCount;
	    			chosenAnswer = figure;
	    			System.out.println("Updating chosen answer to " + figure + ", with transformation count: " + transformationCount);
	    		}    		
	    	}
	    	
	    	System.out.println("First Chosen Answer: " + chosenAnswer);
	    	
	    	// We didn't find a perfect fit
	    	if (transformationCount > 0) {
	    		
	    		System.out.println("\n\nTRYING an Alternate Strategy");
	    		
	    		for (String figure : Arrays.asList("1", "2", "3", "4", "5", "6")) {
	        		
	        		System.out.println("Comparing D to " + figure);
	        		
	        		if (D != null && D.getShapeList() != null && D.getShapeList().size() > 0) {
	        		
	        			if (D.getShapeList().get(0).getFills() != Fills.NONE) {
			    			
		    				if (D.getShapeList().get(0).getFills() == Fills.TOP_RIGHT) D.getShapeList().get(0).setFills(Fills.BOTTOM_LEFT);
		    				else if (D.getShapeList().get(0).getFills() == Fills.TOP_LEFT) D.getShapeList().get(0).setFills(Fills.BOTTOM_RIGHT);
		    				else if (D.getShapeList().get(0).getFills() == Fills.BOTTOM_LEFT) D.getShapeList().get(0).setFills(Fills.TOP_RIGHT);
		    				else if (D.getShapeList().get(0).getFills() == Fills.BOTTOM_RIGHT) D.getShapeList().get(0).setFills(Fills.TOP_LEFT);
		    				
		    				if (D.getShapeList().get(0).getFills() == Fills.RIGHT) D.getShapeList().get(0).setFills(Fills.LEFT);
		    				else if (D.getShapeList().get(0).getFills() == Fills.TOP) D.getShapeList().get(0).setFills(Fills.BOTTOM);
		    				else if (D.getShapeList().get(0).getFills() == Fills.LEFT) D.getShapeList().get(0).setFills(Fills.RIGHT);
		    				else if (D.getShapeList().get(0).getFills() == Fills.BOTTOM) D.getShapeList().get(0).setFills(Fills.TOP);
		    			
	        			}		
	        		}
	        		
	            	List<Transformation> transformations2 = new ArrayList<Transformation>();        	
	            	transformations2.addAll(buildTransformations(D, diagramList.get(figure)));
	            	
	            	transformationCount = transformations2.size();
	        		System.out.println("TransformationCount: " + transformationCount);
	        		System.out.println("*** These are all of the transformations from D -> " + figure);
	            	for (Transformation t : transformations2) System.out.println(t.getTransformation());
	            	
	        		if (transformationCount <= lowestCount) {
	        			lowestCount = transformationCount;
	        			chosenAnswer = figure;
	        			System.out.println("Updating chosen answer to " + figure + ", with transformation count: " + transformationCount);
	        		}    		
	        	}
	    	}
    	}
    	
    	if (lowestCount > 0) {
    		String countingSolution = countingStrategy2x2(diagramList);
    		if (countingSolution != null) chosenAnswer = countingSolution;    		
    	}
    	
    	return chosenAnswer;    	
    }
    
    private String countingStrategy(HashMap<String, Diagram> diagramList) {
    	
    	System.out.println("Attempting to find solution with counting strategy");		
    	String answer = null;
    	boolean increasing = false;
    	
    	// Project 3 logic
    	    	
		// IF A + B = C and D + E = F then do G + H, but don't add overlapping pixels
		
		int abOverlap = countOverlappingPixels(diagramList.get("A"), diagramList.get("B"));
		int deOverlap = countOverlappingPixels(diagramList.get("D"), diagramList.get("E"));
		int ghOverlap = countOverlappingPixels(diagramList.get("G"), diagramList.get("H"));
		
		int adOverlap = countOverlappingPixels(diagramList.get("A"), diagramList.get("D"));
		int beOverlap = countOverlappingPixels(diagramList.get("B"), diagramList.get("E"));
		int cfOverlap = countOverlappingPixels(diagramList.get("C"), diagramList.get("F"));
		
		// Case #1: C is all the shapes in A plus the shapes in B, but with any shapes in both A and B excluded from C
		// For problem E-7 
				
		// Adding a and b and removing the overlap completely. Have to do 2*overlap because overlap is in both diagram!
		int expectedPixelCountInC = diagramList.get("A").getPixelCount() + diagramList.get("B").getPixelCount() - abOverlap*2;
		int expectedPixelCountInF = diagramList.get("D").getPixelCount() + diagramList.get("E").getPixelCount() - deOverlap*2;
		int expectedPixelCountInG = diagramList.get("A").getPixelCount() + diagramList.get("D").getPixelCount() - adOverlap*2;
		int expectedPixelCountInH = diagramList.get("B").getPixelCount() + diagramList.get("E").getPixelCount() - beOverlap*2;
		
		int expectedCountOne = 0;
		int expectedCountTwo = 0;
		int delta = 150;
		
    	if ( (Math.abs(expectedPixelCountInC - diagramList.get("C").getPixelCount()) < delta
    			&& Math.abs(expectedPixelCountInF - diagramList.get("F").getPixelCount()) < delta)
    		||
    			(Math.abs(expectedPixelCountInG - diagramList.get("G").getPixelCount()) < delta
    					&& Math.abs(expectedPixelCountInH - diagramList.get("H").getPixelCount()) < delta) ) {
    	
    		System.out.println("CASE #1: Shapes in A + Shapes in B - Shapes in both");
    		expectedCountOne = diagramList.get("G").getPixelCount() + diagramList.get("H").getPixelCount() - ghOverlap*2;
    		expectedCountTwo = diagramList.get("C").getPixelCount() + diagramList.get("F").getPixelCount() - cfOverlap*2;    		
    	}
	
    	// Case #2: C is the stuff in A + B, but remember not to count overlap twice
    	expectedPixelCountInC = diagramList.get("A").getPixelCount() + diagramList.get("B").getPixelCount() - abOverlap;
    	expectedPixelCountInF = diagramList.get("D").getPixelCount() + diagramList.get("E").getPixelCount() - deOverlap;
    	expectedPixelCountInG = diagramList.get("A").getPixelCount() + diagramList.get("D").getPixelCount() - adOverlap;
    	expectedPixelCountInH = diagramList.get("B").getPixelCount() + diagramList.get("E").getPixelCount() - beOverlap;
    							
    	if ( (Math.abs(expectedPixelCountInC - diagramList.get("C").getPixelCount()) < delta
    			&& Math.abs(expectedPixelCountInF - diagramList.get("F").getPixelCount()) < delta)
    		||
    			(Math.abs(expectedPixelCountInG - diagramList.get("G").getPixelCount()) < delta
    					&& Math.abs(expectedPixelCountInH - diagramList.get("H").getPixelCount()) < delta) ) {
    	
    		System.out.println("CASE #2: Shapes in A + Shapes in B = C");
    		expectedCountOne = diagramList.get("G").getPixelCount() + diagramList.get("H").getPixelCount() - ghOverlap;
    		expectedCountTwo = diagramList.get("C").getPixelCount() + diagramList.get("F").getPixelCount() - cfOverlap;    		
    	}
    	
    	// Case #3: C is the stuff in A minus the stuff in B
    	expectedPixelCountInC = diagramList.get("A").getPixelCount() - diagramList.get("B").getPixelCount();
    	expectedPixelCountInF = diagramList.get("D").getPixelCount() - diagramList.get("E").getPixelCount();
    	expectedPixelCountInG = diagramList.get("A").getPixelCount() - diagramList.get("D").getPixelCount();
    	expectedPixelCountInH = diagramList.get("B").getPixelCount() - diagramList.get("E").getPixelCount();
    							
    	if ( (Math.abs(expectedPixelCountInC - diagramList.get("C").getPixelCount()) < delta
    			&& Math.abs(expectedPixelCountInF - diagramList.get("F").getPixelCount()) < delta)
    		||
    			(Math.abs(expectedPixelCountInG - diagramList.get("G").getPixelCount()) < delta
    					&& Math.abs(expectedPixelCountInH - diagramList.get("H").getPixelCount()) < delta) ) {
    	
    		System.out.println("CASE #3: Shapes in A - Shapes in B = C");
    		expectedCountOne = diagramList.get("G").getPixelCount() - diagramList.get("H").getPixelCount();
    		expectedCountTwo = diagramList.get("C").getPixelCount() - diagramList.get("F").getPixelCount();    		
    	}
    	
    	// Case #4: B = C + A - Overlap(A, C)
    	// For this case, we can't write C as a function of A and B. Instead we have an equation, and we can try plugging in each
    	// of the available answers until we find the one that fits.
    	
    	int expectedPixelCountInB = diagramList.get("A").getPixelCount() + diagramList.get("C").getPixelCount() - countOverlappingPixels(diagramList.get("A"), diagramList.get("C"));
    	int expectedPixelCountInE1 = diagramList.get("D").getPixelCount() + diagramList.get("F").getPixelCount() - countOverlappingPixels(diagramList.get("D"), diagramList.get("F"));
     	int expectedPixelCountInD = diagramList.get("A").getPixelCount() + diagramList.get("G").getPixelCount() - countOverlappingPixels(diagramList.get("A"), diagramList.get("G"));
    	int expectedPixelCountInE2 = diagramList.get("B").getPixelCount() + diagramList.get("H").getPixelCount() - countOverlappingPixels(diagramList.get("B"), diagramList.get("H"));
    	    							
    	if ( (Math.abs(expectedPixelCountInB - diagramList.get("B").getPixelCount()) < delta
    			&& Math.abs(expectedPixelCountInE1 - diagramList.get("E").getPixelCount()) < delta)
    		||
    			(Math.abs(expectedPixelCountInD - diagramList.get("D").getPixelCount()) < delta
    					&& Math.abs(expectedPixelCountInE2 - diagramList.get("E").getPixelCount()) < delta) ) {
    	
    		System.out.println("CASE #4: B = A + C + Overlap(A, C)");
    		
    		int lowestDifference = Integer.MAX_VALUE;
    		
    		System.out.println("This case uses an equation. Plugging each answer into the equation to find the one that fits the best.");
    		for (String answerToCheck : Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8")) {

    			// H = I + G - overlap(I, G)  [where I is the answer we are checking]
    			int calculatedSizeOfH1 = diagramList.get(answerToCheck).getPixelCount() + diagramList.get("G").getPixelCount() - countOverlappingPixels(diagramList.get(answerToCheck), diagramList.get("G"));
    			int difference = Math.abs(diagramList.get("H").getPixelCount() - calculatedSizeOfH1);
    			System.out.println("Checking figure " + answerToCheck + " with size: " + diagramList.get(answerToCheck).getPixelCount() + ", difference from expected answer: " + difference);
    			
    			if (difference < lowestDifference) {
    				lowestDifference = difference;
    				answer = answerToCheck;
    			}
    		}
    		
       		for (String answerToCheck : Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8")) {

       			// F = I + C - overlap(C, I)
    			int calculatedSizeOfH2 = diagramList.get(answerToCheck).getPixelCount() + diagramList.get("C").getPixelCount() - countOverlappingPixels(diagramList.get(answerToCheck), diagramList.get("C"));
    			int difference = Math.abs(diagramList.get("H").getPixelCount() - calculatedSizeOfH2);
    			System.out.println("Checking figure " + answerToCheck + " with size: " + diagramList.get(answerToCheck).getPixelCount() + ", difference from expected answer: " + difference);
    			
    			if (difference < lowestDifference) {
    				lowestDifference = difference;
    				answer = answerToCheck;
    			}
    		}
    		
    		System.out.println("Found the best fit with answer: " + answer);
    		return answer;    		
    	}
    	
    	// If none of the Project 3 strategies were used
    	if (expectedCountOne == 0) {
    		
    		// Project 2 logic
    		
    		// If the shapes grow from left to right and top to bottom
	    	if (diagramList.get("B").getPixelCount() > diagramList.get("A").getPixelCount() 
	    			&& diagramList.get("C").getPixelCount() > diagramList.get("B").getPixelCount() 
	    			&& diagramList.get("D").getPixelCount() > diagramList.get("A").getPixelCount()
	    			&& diagramList.get("G").getPixelCount() > diagramList.get("D").getPixelCount()
	    			&& diagramList.get("H").getPixelCount() > diagramList.get("G").getPixelCount()
	    			&& diagramList.get("F").getPixelCount() > diagramList.get("C").getPixelCount()) {
	    		
	    		increasing = true;
	    		System.out.println("Valid scenario, increasing");    	
	    		
	    	} // If the shapes shrink from left to right and top to bottom
	    	else if (diagramList.get("B").getPixelCount() < diagramList.get("A").getPixelCount() 
	    			&& diagramList.get("C").getPixelCount() < diagramList.get("B").getPixelCount() 
	    			&& diagramList.get("D").getPixelCount() < diagramList.get("A").getPixelCount()
	    			&& diagramList.get("G").getPixelCount() < diagramList.get("D").getPixelCount()
	    			&& diagramList.get("H").getPixelCount() < diagramList.get("G").getPixelCount()
	    			&& diagramList.get("F").getPixelCount() < diagramList.get("C").getPixelCount()) {
					
	    		increasing = false;
				System.out.println("Valid scenario, decreasing");
					
	    	} else {
	    		System.out.println("Not attempting");
	    		return answer;
	    	}
	    	
	    	int difference = 0;    	
	    	int expectedCount = 0;
	    	
	    	if (increasing) {
	    		difference = diagramList.get("H").getPixelCount() - diagramList.get("G").getPixelCount();
	    		expectedCount = diagramList.get("H").getPixelCount() + difference;
	    	} else {
	    		difference = diagramList.get("G").getPixelCount() - diagramList.get("H").getPixelCount();
	    		expectedCount = diagramList.get("H").getPixelCount() - difference;
	    	}
	    	
	    	System.out.println("Difference: " + difference + ", expectedCount: " + expectedCount);

	    	// Find the diagram with the closest number of pixels to our expected number
	    	int diff = Integer.MAX_VALUE;
	    	for (String figure : Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8")) {
	    		
	    		if (Math.abs(expectedCount - diagramList.get(figure).getPixelCount()) < diff) {
	    			diff = Math.abs(expectedCount - diagramList.get(figure).getPixelCount());
	    			answer = figure;
	    		}
	    	}
	    	
	    	System.out.println("The final diff is: " + diff + " with figure: " + answer);
	    	
	    	return answer;
    	}
    	    	
    	answer = findBestMatchPixelCountMatch(expectedCountOne, expectedCountTwo, diagramList);
    	return answer;    	    	
    }
    
    private String findBestMatchPixelCountMatch(int expectedCountOne, int expectedCountTwo, HashMap<String, Diagram> diagramList) {
    	
    	System.out.println("ExpectedCountOne: " + expectedCountOne + ", expectedCountTwo: " + expectedCountTwo);
    	
    	// Given two expected values, find the answer diagram that has the closest number of pixels to each one.
    	// Whichever diagram matches up with the smallest difference is returned.
    	
    	// Find the diagram with the closest number of pixels to our expected number
    	String answer1 = "";
    	String answer2 = "";
    	int diff1 = Integer.MAX_VALUE;
    	int diff2 = Integer.MAX_VALUE;
    	
    	// TODO: This method does way more than intended, refactor.
    	
    	// Crazy idea, only look at good answers. Remove any of the answer diagrams that are identical to C, F, G, or H
    	List<String> answersToCheck = new ArrayList<String>();
    	
    	// For each answer
    	for (String answer : Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8")) {
    		
    		// Check to see if the answer diagram is identical to C, F, G or H, and skip it if it is an exact match
    		boolean isMatch = doesAnswerMatchDiagramInList(diagramList, answer, Arrays.asList("C", "F", "G", "H"));
    		
    		if (!isMatch) {
				answersToCheck.add(answer);
			}
    	}
    	
    	// When filtering answers, if we have two answers with the identical number of pixels, then we will always pick the first one.
    	// There should be some way to remove whichever one is worse.
    	// Idea: If one of them is identical to an existing diagram, remove it?
    	
    	// Find every pixel count that more than one answer diagram has
    	HashSet<Integer> duplicates = findDuplicatedPixelCounts(diagramList, answersToCheck);
    	
    	// For answer diagrams with the same pixel count, we only want to look at one
    	List<String> duplicatesToSkip = findDuplicateDiagramsToSkip(diagramList, answersToCheck, duplicates);
    	
    	// Update the list of answers to look at
    	removeDuplicateAnswersFromList(answersToCheck, duplicatesToSkip);
    	    	
    	// Find the diagram that matches closest to the first expected pixel count
    	for (String figure : answersToCheck) {
    		
    		System.out.println("Pixel Count " + figure + ": " + diagramList.get(figure).getPixelCount());
    		
    		if (Math.abs(expectedCountOne - diagramList.get(figure).getPixelCount()) < diff1) {
    			diff1 = Math.abs(expectedCountOne - diagramList.get(figure).getPixelCount());
    			answer1 = figure;
    		}
    	}
    	
    	// Find the diagram that matches closest to the second expected pixel count
    	for (String figure : answersToCheck) {
    		if (Math.abs(expectedCountTwo - diagramList.get(figure).getPixelCount()) < diff2) {
    			diff2 = Math.abs(expectedCountTwo - diagramList.get(figure).getPixelCount());
    			answer2 = figure;
    		}
    	}
    	
    	System.out.println("The final difference for expectedCountOne is: " + diff1 + " with figure: " + answer1);
    	System.out.println("The final difference for expectedCountTwo is: " + diff2 + " with figure: " + answer2);
    	
    	// Return whichever figure matched up the best
    	if (diff1 < diff2) {
    		return answer1;
    	} else {
    		return answer2;
    	}
    }

	private void removeDuplicateAnswersFromList(List<String> answersToCheck, List<String> duplicatesToSkip) {
		
		List<String> tempList = new ArrayList<String>();
    	tempList.addAll(answersToCheck);
    	answersToCheck.clear();
    	
    	for (String figure : tempList) {    		
    		if (!duplicatesToSkip.contains(figure)) {
    			answersToCheck.add(figure);
    		}    		
    	}
	}

	private List<String> findDuplicateDiagramsToSkip(HashMap<String, Diagram> diagramList, List<String> answersToCheck,	HashSet<Integer> duplicates) {
		
		List<String> duplicatesToSkip = new ArrayList<String>();
    	
    	// For each pixel count that we have more than one diagram with that number of pixels
    	for (int pixelCount : duplicates) {
    		System.out.println("Duplicate pixelCount: " + pixelCount);
    		
    		// Look at all of the diagrams with that many pixels
    		for (String answer : answersToCheck) {
    			if (diagramList.get(answer).getPixelCount() != pixelCount) continue;
    			
    			// Not making this super robust.
    			// If any of these are replicas of existing diagrams then skip them
    			boolean isMatch = doesAnswerMatchDiagramInList(diagramList, answer, Arrays.asList("A", "B", "D", "E"));
        		
        		if (isMatch) {
        			duplicatesToSkip.add(answer);
    			}
    		}
    	}
    	
		return duplicatesToSkip;
	}

	private HashSet<Integer> findDuplicatedPixelCounts(
			HashMap<String, Diagram> diagramList, List<String> answersToCheck) {
		HashSet<Integer> duplicates = new HashSet<Integer>();
    	
    	// for each remaining answer, find all of the answers that have the same number of pixels
    	for (String figure : answersToCheck) {
    		
    		for (String figure2 : answersToCheck) {
    			
    			if (figure.equals(figure2)) continue;
    			
    			if (diagramList.get(figure).getPixelCount() == diagramList.get(figure2).getPixelCount()) {
    				duplicates.add(diagramList.get(figure).getPixelCount());
    			}
    		}
    	}
		return duplicates;
	}

	private boolean doesAnswerMatchDiagramInList(HashMap<String, Diagram> diagramList, String answer, List<String> diagramsToCompareAgainst) {
		
		boolean isMatch = false;
		
		// Compare it to C F G and H
		for (String diagram : diagramsToCompareAgainst) {
			
			int overlap = countOverlappingPixels(diagramList.get(answer), diagramList.get(diagram));
			int diagramCount = diagramList.get(diagram).getPixelCount();
			int answerCount = diagramList.get(answer).getPixelCount();
			
			// System.out.println("Answer: " + answer + ", Diagram: " + diagram + ", Overlap: " + overlap + ", answerCount: " + answerCount + ", diagramCount: " + diagramCount);
			
			// If the two diagrams have about the same pixel count and the overlap is close to the number of pixels then skip it.
			// Note: Some diagrams look identical but are slightly different, hence the lazy math here. Perfect precision doesn't work.
			if (Math.abs(answerCount - diagramCount) < 100 && Math.abs(Math.min(answerCount, diagramCount) - overlap) < 100) {
				isMatch = true;
				System.out.println("Skipping " + answer + " because " + diagram + " matches it.");
				break;
			}
		}
		return isMatch;
	}
    
    private int countOverlappingPixels(Diagram d1, Diagram d2) {
    	
    	int overlapCount = 0;
    	
    	for (int j = 0; j < 184; j++) {
    		for (int i = 0; i < 184; i++) {
    			if (d1.getMatrix()[i][j] && d2.getMatrix()[i][j]) {
    				overlapCount++;
    			}    				
    		}
    	}
    
    	return overlapCount;
    }
    
    private String countingStrategy2x2(HashMap<String, Diagram> diagramList) {
    	
    	System.out.println("Attempting to find solution with counting strategy");
		
    	String answer = null;
    	boolean increasing = false;
    	
    	// If the shapes grow from left to right and top to bottom
    	if (diagramList.get("B").getPixelCount() > diagramList.get("A").getPixelCount() 
    			&& diagramList.get("C").getPixelCount() > diagramList.get("A").getPixelCount()) {
    		
    		increasing = true;
    		System.out.println("Valid scenario, increasing");
    		
    	} // If the shapes shrink from left to right and top to bottom
    	else if (diagramList.get("B").getPixelCount() < diagramList.get("A").getPixelCount() 
    			&& diagramList.get("C").getPixelCount() < diagramList.get("A").getPixelCount()) {
				
    		increasing = false;
			System.out.println("Valid scenario, decreasing");
				
		} else {
    		System.out.println("Not attempting");
    		return answer;
    	}
    	
    	int difference = 0;    	
    	int expectedCount = 0;
    	
    	if (increasing) {
    		difference = diagramList.get("B").getPixelCount() - diagramList.get("A").getPixelCount();
    		expectedCount = diagramList.get("C").getPixelCount() + difference;
    	} else {
    		difference = diagramList.get("A").getPixelCount() - diagramList.get("B").getPixelCount();
    		expectedCount = diagramList.get("C").getPixelCount() - difference;
    	}
    	
    	System.out.println("Difference: " + difference + ", expectedCount: " + expectedCount);
    	
    	int diff = Integer.MAX_VALUE;
    	
    	for (String figure : Arrays.asList("1", "2", "3", "4", "5", "6")) {
    		
    		if (Math.abs(expectedCount - diagramList.get(figure).getPixelCount()) < diff) {
    			diff = Math.abs(expectedCount - diagramList.get(figure).getPixelCount());
    			answer = figure;
    		}
    	}
    	
    	System.out.println("The final diff is: " + diff + " with figure: " + answer);
    	
    	return answer;
    }
    
    private String determineFinalAnswerFor3x3(HashMap<String, Diagram> diagramList) {
    	
    	System.out.println("Determining answer for 3x3 Problem");
    	
    	// TODO: Counting strategy works for 11/12 of the problems in set E
    	// To solve the problems in Set D I need to try completely different things.
    	
    	// Most of these problems involve sequences, so I build up the picture of each diagram which I already have,
    	// then I really need brand new logic instead of building transformations. Or I need a different method of building transformations.
    	
    	// If every problem has the same number of shapes then that is a good hint.
    	
    	boolean sameNumberOfShapes = false;
    	
    	// Or if a,b,c each have only one unique shape, and d-e-f only have one unique shape
    	
    	int aUnique = findNumberOfShapeTypesInAThatArentInB(diagramList.get("A"), diagramList.get("B")).size();
    	int bUnique = findNumberOfShapeTypesInAThatArentInB(diagramList.get("B"), diagramList.get("C")).size();
       	int cUnique = findNumberOfShapeTypesInAThatArentInB(diagramList.get("C"), diagramList.get("B")).size();
    	int dUnique = findNumberOfShapeTypesInAThatArentInB(diagramList.get("D"), diagramList.get("E")).size();
    	int eUnique = findNumberOfShapeTypesInAThatArentInB(diagramList.get("E"), diagramList.get("F")).size();
       	int fUnique = findNumberOfShapeTypesInAThatArentInB(diagramList.get("F"), diagramList.get("E")).size();
       	int gUnique = findNumberOfShapeTypesInAThatArentInB(diagramList.get("G"), diagramList.get("H")).size();
       	int hUnique = findNumberOfShapeTypesInAThatArentInB(diagramList.get("H"), diagramList.get("G")).size();
       	   	
    	if (aUnique == bUnique && bUnique == cUnique && cUnique == dUnique  && dUnique == eUnique 
    			&& eUnique == fUnique && fUnique == gUnique && gUnique == hUnique && hUnique == 1) {
    		
    		System.out.println("All have one unique shape ");
    		sameNumberOfShapes = true;
    	}    	
    	
    	if (sameNumberOfShapes) {
    		System.out.println("Same number of shapes");
    		String answer = seriesDetermination(diagramList);
    		if (answer != null) return answer;
    	}
    	
       	// Counting solution
    	System.out.println("Checking shape counting strategy");
    	String shapeCountingSolution = shapeCountingStrategy(diagramList);
    	if (shapeCountingSolution != null) {
    		return shapeCountingSolution;
    	}
    	
     	// Attempt the counting strategy, if it is valid then use that as the answer
    	String countingSolution = countingStrategy(diagramList);
    	if (countingSolution != null) {
    		return countingSolution;
    	}
    	
    	System.out.println("Checking three of a kind strategy");
    	String threeOfAKindSolution = threeOfAKindStrategy(diagramList);
    	if (threeOfAKindSolution != null) {
    		return threeOfAKindSolution;
    	}
    	
    	// If the list of answers only has one solution that isn't in the matrix diagram then just use that?
    	List<Diagram> uniqueDiagrams = new ArrayList<Diagram>();
    	for (String answer : Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8")) {
    	
    		boolean containsAnswer = false;
        	for (String diagram : Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H")) {
        		if (Math.abs(diagramList.get(answer).getPixelCount() - diagramList.get(diagram).getPixelCount()) < 100) {
        			containsAnswer = true;
        			break;
        		}
        	}
        	if (!containsAnswer) uniqueDiagrams.add(diagramList.get(answer));
    	}
    	
    	if (uniqueDiagrams.size() == 1) {
    		System.out.println("Only found one unique answer for this problem. Using that.");
    		return uniqueDiagrams.get(0).getName();
    	}
    	
    	System.out.println("Attempting the normal strategy");
    	
    	// Build Transformations between A->B and A->C
    	System.out.println();
    	List<Transformation> abTransformations = buildTransformations(diagramList.get("A"), diagramList.get("B"));
    	printTransformations(abTransformations, diagramList.get("A"));
    	List<Transformation> bcTransformations = buildTransformations(diagramList.get("B"), diagramList.get("C"));
    	printTransformations(bcTransformations, diagramList.get("B"));
    	    	
    	// Generate solutions by applying transformations from A->B onto G, and from A->C onto H
    	Diagram GH = generateSolutionDiagram(diagramList.get("G"), abTransformations);
    	Diagram I = generateSolutionDiagram(GH, bcTransformations);
    	
    	System.out.println("\nComparing D to all of the available answers");
    
    	String chosenAnswer = "";
    	int lowestCount = Integer.MAX_VALUE;
    	int transformationCount = 0;
    		
		for (String figure : Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8")) {
    		
    		System.out.println("Comparing I to " + figure);	    		
    		
        	List<Transformation> transformations2 = new ArrayList<Transformation>();        	
        	transformations2.addAll(buildTransformations(I, diagramList.get(figure)));
        	
        	transformationCount = transformations2.size();
    		System.out.println("TransformationCount: " + transformationCount);
    		System.out.println("*** These are all of the transformations from D -> " + figure);
        	for (Transformation t : transformations2) System.out.println(t.getTransformation());
        	
    		if (transformationCount <= lowestCount) {
    			lowestCount = transformationCount;
    			chosenAnswer = figure;
    			System.out.println("Updating chosen answer to " + figure + ", with transformation count: " + transformationCount);
    		}    		
    	}
		
		if (lowestCount > 0) {
			System.out.println("	ATTEMPTING A->D + D->G Strategy");
			System.out.println("Currently, the chosen answer is " + chosenAnswer + ", with transformation count: " + transformationCount);

			// Build transformations for A -> D and from D -> G and then apply them to C
			
			List<Transformation> adTransformations = buildTransformations(diagramList.get("A"), diagramList.get("D"));
	    	printTransformations(adTransformations, diagramList.get("A"));
	    	List<Transformation> dgTransformations = buildTransformations(diagramList.get("D"), diagramList.get("G"));
	    	printTransformations(dgTransformations, diagramList.get("D"));
	    	    	
	    	// Generate solutions by applying transformations from A->B onto G, and from A->C onto H
	    	Diagram CF = generateSolutionDiagram(diagramList.get("C"), adTransformations);
	    	I = generateSolutionDiagram(CF, dgTransformations);
	    	
	    	System.out.println("\nComparing D to all of the available answers");
	    		
			for (String figure : Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8")) {
	    		
	    		System.out.println("Comparing I to " + figure);	    		
	    		
	        	List<Transformation> transformations2 = new ArrayList<Transformation>();        	
	        	transformations2.addAll(buildTransformations(I, diagramList.get(figure)));
	        	
	        	transformationCount = transformations2.size();
	    		System.out.println("TransformationCount: " + transformationCount);
	    		System.out.println("*** These are all of the transformations from D -> " + figure);
	        	for (Transformation t : transformations2) System.out.println(t.getTransformation());
	        	
	    		if (transformationCount <= lowestCount) {
	    			lowestCount = transformationCount;
	    			chosenAnswer = figure;
	    			System.out.println("Updating chosen answer to " + figure + ", with transformation count: " + transformationCount);
	    		}    		
	    	}
		}
		
		if (lowestCount > 0) {
			
			System.out.println("	ATTEMPTING mini 2x2 Strategy");
			System.out.println("Currently, the chosen answer is " + chosenAnswer + ", with transformation count: " + transformationCount);
			// Build transformations for E -> F and from E -> H and then apply them to E
			
	    	List<Transformation> fTransformations = buildTransformations(diagramList.get("E"), diagramList.get("F"));
	    	printTransformations(fTransformations, diagramList.get("E"));
	    	List<Transformation> hTransformations = buildTransformations(diagramList.get("E"), diagramList.get("H"));
	    	printTransformations(hTransformations, diagramList.get("E"));
	    	
	    	List<Transformation> fhTransformations = new ArrayList<Transformation>();
	    	bcTransformations.addAll(fTransformations);
	    	bcTransformations.addAll(hTransformations);
	    	
	    	I = generateSolutionDiagram(diagramList.get("E"), fhTransformations);
	    	
	    	System.out.println("\nComparing D to all of the available answers");
	    		
			for (String figure : Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8")) {
	    		
	    		System.out.println("Comparing I to " + figure);	    		
	    		
	        	List<Transformation> transformations2 = new ArrayList<Transformation>();        	
	        	transformations2.addAll(buildTransformations(I, diagramList.get(figure)));
	        	
	        	transformationCount = transformations2.size();
	    		System.out.println("TransformationCount: " + transformationCount);
	    		System.out.println("*** These are all of the transformations from D -> " + figure);
	        	for (Transformation t : transformations2) System.out.println(t.getTransformation());
	        	
	    		if (transformationCount <= lowestCount) {
	    			lowestCount = transformationCount;
	    			chosenAnswer = figure;
	    			System.out.println("Updating chosen answer to " + figure + ", with transformation count: " + transformationCount);
	    		}    		
	    	}
		}
		
		if (lowestCount >= 0) {
			
			// Take transformations from A -> C and A-> G and apply them to A
			
			System.out.println("\n\n\n\nAttempting the corner strategy");
			System.out.println("Currently, the chosen answer is " + chosenAnswer + ", with transformation count: " + transformationCount);
			
			// Build Transformations between A->C and A->G
	    	System.out.println();
	    	List<Transformation> acTransformations = buildTransformations(diagramList.get("A"), diagramList.get("C"));
	    	printTransformations(acTransformations, diagramList.get("A"));
	    	List<Transformation> agTransformations = buildTransformations(diagramList.get("A"), diagramList.get("G"));
	    	printTransformations(agTransformations, diagramList.get("A"));
	    	    	
	    	List<Transformation> cgTransformations = new ArrayList<Transformation>();
	    	cgTransformations.addAll(acTransformations);
	    	cgTransformations.addAll(agTransformations);
	    	
	    	I = generateSolutionDiagram(diagramList.get("A"), cgTransformations);
	    		    	
	    	System.out.println("\nComparing I to all of the available answers");
	    		
			for (String figure : Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8")) {
	    		
	    		System.out.println("Comparing I to " + figure);	    		
	    		
	        	List<Transformation> transformations2 = new ArrayList<Transformation>();        	
	        	transformations2.addAll(buildTransformations(I, diagramList.get(figure)));
	        	
	        	transformationCount = transformations2.size();
	    		System.out.println("TransformationCount: " + transformationCount);
	    		System.out.println("*** These are all of the transformations from D -> " + figure);
	        	for (Transformation t : transformations2) System.out.println(t.getTransformation());
	        	
	    		if (transformationCount <= lowestCount) {
	    			lowestCount = transformationCount;
	    			chosenAnswer = figure;
	    			System.out.println("Updating chosen answer to " + figure + ", with transformation count: " + transformationCount);
	    		}    		
	    	}
		}
    	
    	System.out.println("First Chosen Answer: " + chosenAnswer);
 	
    	if (transformationCount > 4) return "-1";
    	
    	return chosenAnswer;
    }
    
    private String shapeCountingStrategy(HashMap<String, Diagram> diagramList) {
    	
    	String answer = null;
    	
    	boolean onlyOneTypeOfShapeInEachDiagram = true;
    	HashSet<Shapes> shapesFound = new HashSet<Shapes>();
    	for (String name : Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H")) {
    		
    		if (diagramList.get(name).getShapeList().size() == 0) {
    			System.out.println("No shapes in this diagram");
    			return null;
    		}
    		
    		boolean sameShapes = true;
    		Shape initialShape = diagramList.get(name).getShapeList().get(0);
    		
    		System.out.println("Diagram: " + name + ", initialShape: " + initialShape.getShape());
    		for (Shape shape : diagramList.get(name).getShapeList()) {
    			shapesFound.add(shape.getShape());
    			if (shape.getShape() != initialShape.getShape()) {
    				
    				System.out.println(shape.getShape() + " != " + initialShape.getShape());
    				
    				sameShapes = false;
    				break;
    			}
    		}
    		
    		if (!sameShapes) {
    			onlyOneTypeOfShapeInEachDiagram = false;
    			break;
    		}
    	}
    	
    	System.out.println("OnlyOneTypeOfShapeInEachDiagram: " + onlyOneTypeOfShapeInEachDiagram);
    	System.out.println("NumberOfShapesFound: " + shapesFound.size());
    	
    	if (!onlyOneTypeOfShapeInEachDiagram) return answer;
    	if (shapesFound.size() != 3) return answer;

    	// We can also figure out what shape type I should be
    	Shapes missingShape = null;
    	Textures missingTexture = null;
    	for (Shapes shapes : shapesFound) {
    		
    		boolean shapeFound = false;
    		if (diagramList.get("G").getShapeList().get(0).getShape() == shapes
    				|| diagramList.get("H").getShapeList().get(0).getShape() == shapes) {
    			shapeFound = true;
    		}
    		
    		if (!shapeFound) {
    			missingShape = shapes;
    			break;
    		}
    	}
    	
    	System.out.println("Missing shape is " + missingShape);
    	
    	// Can we figure out how many shapes should exist
    	HashSet<Integer> firstRowCounts = new HashSet<Integer>();
    	HashSet<Integer> secondRowCounts = new HashSet<Integer>();
    	HashSet<Integer> thirdRowCounts = new HashSet<Integer>();
    	
    	for (String name : Arrays.asList("A", "B", "C")) firstRowCounts.add(diagramList.get(name).getShapeList().size());
    	for (String name : Arrays.asList("D", "E", "F")) secondRowCounts.add(diagramList.get(name).getShapeList().size());
    	for (String name : Arrays.asList("G", "H")) thirdRowCounts.add(diagramList.get(name).getShapeList().size());
    	
    	// Confirm that the each diagram in the first row has a different number of shapes, and the 2nd row also has diagrams with a matching
    	// number of shapes
    	if (!firstRowCounts.equals(secondRowCounts)) return answer;

    	int missingNumberOfShapes = 0;
    	if (firstRowCounts.size() == 3) {

    		// If the third row contains two of these values then we can figure out how many shapes should be in I
        	int matches = 0;
        	for (Integer numberOfShapes : firstRowCounts) {
        		if (thirdRowCounts.contains(numberOfShapes)) {
        			matches++;
        		} else {
        			missingNumberOfShapes = numberOfShapes;
        		}	
        	}
        	
        	if (matches != 2) return answer;
    	
    	} else if (firstRowCounts.size() == 2) {
    		
    		// Problem D-08
    		// TODO: This code is pretty hacky and poorly documented, but gets the job done.
    		
    		int doubleCount = 0;
    		int singleCount = 0;
    		
    		// There are only two counts, find the one that is doubled 
    		if (diagramList.get("A").getShapeList().size() == diagramList.get("B").getShapeList().size()) {
    			doubleCount = diagramList.get("A").getShapeList().size();
    			singleCount = diagramList.get("C").getShapeList().size();
    		} else if (diagramList.get("B").getShapeList().size() == diagramList.get("C").getShapeList().size()) {
    			doubleCount = diagramList.get("B").getShapeList().size();
    			singleCount = diagramList.get("A").getShapeList().size();
    		} else if (diagramList.get("A").getShapeList().size() == diagramList.get("C").getShapeList().size()) {
    			doubleCount = diagramList.get("C").getShapeList().size();
    			singleCount = diagramList.get("B").getShapeList().size();
    		}
    		
    		// doubleCount is the number of shapes the should be repeated
    		
    		int count = 0;
    		for (String figure : Arrays.asList("G", "H")) {
    			if (diagramList.get(figure).getShapeList().size() == doubleCount) {
    				count++;
    			}
    		}
    		
    		if (count == 2) {
    			missingNumberOfShapes = singleCount;
    		} else {
    			missingNumberOfShapes = doubleCount;
    		}
    		
    		// Oh wait, we also need to find the missing texture
    		if (missingNumberOfShapes == doubleCount) {
    			if (diagramList.get("G").getShapeList().size() == 1) {
    				if (diagramList.get("G").getShapeList().get(0).getTexture() == Textures.SOLID) {
    					missingTexture = Textures.HOLLOW;
    				} else {
    					missingTexture = Textures.SOLID;
    				}
    			}
    			
    			if (diagramList.get("H").getShapeList().size() == 1) {
    				if (diagramList.get("H").getShapeList().get(0).getTexture() == Textures.SOLID) {
    					missingTexture = Textures.HOLLOW;
    				} else {
    					missingTexture = Textures.SOLID;
    				}
    			}
    		}
    	}    
    	
    	// Find the solution that has the expected number of shapes and where the shapes are the right type of shape
    	System.out.println("Looking for diagram with " + missingNumberOfShapes + " shapes of type " + missingShape + " and missingTexture: " + missingTexture);
    	
    	for (String figure : Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8")) {
    		
    		if (diagramList.get(figure).getShapeList().size() != missingNumberOfShapes) continue;
    		
    		boolean allShapesCorrectType = true;
    		for (Shape shape : diagramList.get(figure).getShapeList()) {
    			if (shape.getShape() != missingShape) {
    				allShapesCorrectType = false;
    			}
    			if (missingTexture != null && missingTexture != shape.getTexture()) {
    				allShapesCorrectType = false;
    			}
    		}
    		
    		if (allShapesCorrectType) {
    			answer = figure;
    			break;
    		}
    	}
    	
    	return answer;
    }
    
    private String threeOfAKindStrategy(HashMap<String, Diagram> diagramList) {
    	
    	// If there are three diagrams in the top row, and those three are repeated in the 2nd row, then the third row must also contain all three
    	
    	String answer = null;
    	
    	// If there are three distinct images in A-B-C, and those three are repeated in D-E-F, and two of them are in G-H, then the remaining
    	// one must be the answer to I
    	// This is tricky because identical images usually arent identical
    	
    	List<Integer> firstRowCounts = Arrays.asList(diagramList.get("A").getPixelCount(), diagramList.get("B").getPixelCount(), diagramList.get("C").getPixelCount());
    	List<Diagram> middleRow = Arrays.asList(diagramList.get("D"), diagramList.get("E"), diagramList.get("F"));
    	boolean containsAllThree = true;

    	for (Integer pixelCount : firstRowCounts) {
    		System.out.println("Checking if DEF contains: " + pixelCount);
    		boolean containsThisCount = false;
	    	for (Diagram diagram : middleRow) {
	    		if (Math.abs(diagram.getPixelCount() - pixelCount) < 100) {
	    			containsThisCount = true;
	    		}
	    	}
	    	if (!containsThisCount) containsAllThree = false;
    	}

    	List<Diagram> matches = new ArrayList<Diagram>();
    	int missingPixelCount = 0;
    	
    	// DEF contains all three of the images in ABC
    	// Confirm that GH contains 2/3 of the same images, and keep track of the value that's missing
    	if (containsAllThree) {
    		// if GH contains 2 of the three then find the remaining one
    		for (Integer pixelCount : firstRowCounts) {
    			boolean containsThisCount = false;
    			for (Diagram diagram : Arrays.asList(diagramList.get("G"), diagramList.get("H"))) {
    				if (Math.abs(diagram.getPixelCount() - pixelCount) < 100) {
    	    			matches.add(diagram);
    	    			containsThisCount = true;
    	    		} 
    			}
    			if (!containsThisCount) missingPixelCount = pixelCount;
    		}
    	}
    	
    	for (Diagram d : matches) System.out.println(d.getName() + ", " + d.getPixelCount());
    	
    	int difference = Integer.MAX_VALUE;
    	    	
    	if (matches.size() == 2) {
    	
    		System.out.println("Contains all three, has two matches, missingPixelCount: " + missingPixelCount);
    		
    		// Find the answer with the closest pixel count
        	for (String figure : Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8")) {
        		
        		System.out.println("Pixel Count " + figure + ": " + diagramList.get(figure).getPixelCount());
        		
        		if (Math.abs(missingPixelCount - diagramList.get(figure).getPixelCount()) < difference) {
        			difference = Math.abs(missingPixelCount - diagramList.get(figure).getPixelCount());
        			answer = figure;
        		}
        	}
    	}
    	
    	return answer;
    }
    
    private List<Shape> findNumberOfShapeTypesInAThatArentInB(Diagram A, Diagram B) {
    	
    	List<Shape> uniqueShapes = new ArrayList<Shape>();
    	
    	// for each shape that is in a but not in b
    	for (Shape aShape : A.getShapeList()) {
    		
    		boolean isUnique = true;
    		
    		for (Shape bShape : B.getShapeList()) {
    			
    			if (aShape.getShape().equals(bShape.getShape())) {
    				isUnique = false;
    				break;
    			}
    		}
    		
    		if (isUnique) uniqueShapes.add(aShape);
    	}
    	
    	return uniqueShapes;
    }
    
    private String seriesDetermination(HashMap<String, Diagram> diagramList) {
    	
    	// Each diagram only has one unique shape (compared to the diagram in front of it, or behind it in the case of C and F and G)
    	
    	HashMap<Shapes, Shapes> shapesMapping = new HashMap<Shapes, Shapes>();
    	
    	// A - B, B - C, D - E, E - F, G - H
		
    	List<Shape> aUniqueShapes = findNumberOfShapeTypesInAThatArentInB(diagramList.get("A"), diagramList.get("B"));
    	List<Shape> bUniqueShapes = findNumberOfShapeTypesInAThatArentInB(diagramList.get("B"), diagramList.get("C"));
    	List<Shape> cUniqueShapes = findNumberOfShapeTypesInAThatArentInB(diagramList.get("C"), diagramList.get("B"));
    	List<Shape> dUniqueShapes = findNumberOfShapeTypesInAThatArentInB(diagramList.get("D"), diagramList.get("E"));
    	List<Shape> eUniqueShapes = findNumberOfShapeTypesInAThatArentInB(diagramList.get("E"), diagramList.get("F"));
    	List<Shape> fUniqueShapes = findNumberOfShapeTypesInAThatArentInB(diagramList.get("F"), diagramList.get("E"));
    	List<Shape> gUniqueShapes = findNumberOfShapeTypesInAThatArentInB(diagramList.get("G"), diagramList.get("H"));
    	List<Shape> hUniqueShapes = findNumberOfShapeTypesInAThatArentInB(diagramList.get("H"), diagramList.get("G"));
    	
		shapesMapping.put(aUniqueShapes.get(0).getShape(), bUniqueShapes.get(0).getShape());
		shapesMapping.put(bUniqueShapes.get(0).getShape(), cUniqueShapes.get(0).getShape());
		shapesMapping.put(dUniqueShapes.get(0).getShape(), eUniqueShapes.get(0).getShape());
		shapesMapping.put(eUniqueShapes.get(0).getShape(), fUniqueShapes.get(0).getShape());
		shapesMapping.put(gUniqueShapes.get(0).getShape(), hUniqueShapes.get(0).getShape());
    	
		Shapes expectedShape = null;
		
		for (Map.Entry<Shapes, Shapes> entry : shapesMapping.entrySet()) {
			Shapes key = entry.getKey();
	    	Shapes value = entry.getValue();
	    	System.out.println("The shape: " + key + ", turns into the shape: " + value);
	    	
	    	if (key.equals(hUniqueShapes.get(0).getShape())) {
	    		expectedShape = value;
	    		System.out.println("The shape in H goes to: " + value);
	    	}
		}
    			
		// Find the answer based off what we've found 
		List<Diagram> possibleAnswers = new ArrayList<Diagram>();
		for (String answer : Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8")) {
			
			System.out.println("Answer: " + answer + ", size: " + diagramList.get(answer).getShapeList().size() + ", shape: " +
					diagramList.get(answer).getShapeList().get(0).getShape() + ", texture: " + diagramList.get(answer).getShapeList().get(0).getTexture());
			
			// Does this answer contain the expected shape
			boolean containsExpectedShape = false;
			
			for (Shape shape : diagramList.get(answer).getShapeList()) {
				if (shape.getShape().equals(expectedShape) &&
						shape.getTexture().equals(hUniqueShapes.get(0).getTexture())) {
					
					containsExpectedShape = true;
					break;
				}
			}
			
			// If this answer contains the expected shape and has the same number of shapes as H then select that as the answer
			if (containsExpectedShape) {
				System.out.println("The answer: " + answer + " contain the expectedShape.");
				if (diagramList.get("H").getShapeList().size() == diagramList.get(answer).getShapeList().size()) {
					possibleAnswers.add(diagramList.get(answer));
				}
			}			
		}
		
		if (possibleAnswers.size() == 1) {
			return possibleAnswers.get(0).getName();
		} else if (possibleAnswers.size() > 1) {
			
			// If each solution has multiple shapes, then the overlapping shape should be the same
			boolean allContainMultipleShapes = true;
			for (Diagram d : possibleAnswers) {
				if (d.getShapeList().size() < 2) allContainMultipleShapes = false;
			}
			
			if (allContainMultipleShapes) {
				
				Shapes shapeToMatch = null;
				
				for (Shape shape : diagramList.get("G").getShapeList()) {
					for (Shape shape2 : diagramList.get("H").getShapeList()) {
						
						
						if (shape.getShape().equals(shape2.getShape())) shapeToMatch = shape.getShape();
					}
				}
				
				System.out.println("The shape to match is: " + shapeToMatch);
				
				if (shapeToMatch == null) {
					System.out.println("Couldn't find a shape to match, just returning the first best option");
					return possibleAnswers.get(0).getName();
				}
				
				// Otherwise find the answer that also contains this shape
				for (Diagram d : possibleAnswers) {
					for (Shape shape : d.getShapeList()) {
						if (shape.getShape().equals(shapeToMatch)) {
							System.out.println("Found matching shape, returning this diagram");
							return d.getName();
						}
					}
				}
				
				return possibleAnswers.get(0).getName();
			}
			
			
			int lowestValue = Integer.MAX_VALUE;
			String answer = "";
			
			for (Diagram d : possibleAnswers) {
			
				System.out.println("Potential answer: " + d.getName());
				
				// HACKY, find the diagram the overlaps the best with E
				int value = d.getPixelCount() - countOverlappingPixels(diagramList.get("E"), d);
				
				if (value < lowestValue) {
					lowestValue = value;
					answer = d.getName();
				}
			}
		
			return answer;
			//return possibleAnswers.get(0).getName();
		}
		
    	return null;
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
    				if (p.getX() > 0 && !checkedPixels[p.getX() - 1][p.getY()]) {
    					newPixelList.add(new Pixel(p.getX() - 1, p.getY()));
    				}
    				
    				// Add right if not already checked
    				if (p.getX() < 183 && !checkedPixels[p.getX() + 1][p.getY()]) {
    					newPixelList.add(new Pixel(p.getX() + 1, p.getY()));
    				}
    				
    				// Add below if not already checked
    				if (p.getY() < 183 && !checkedPixels[p.getX()][p.getY() + 1]) {
    					newPixelList.add(new Pixel(p.getX(), p.getY() + 1));
    				}
    				
    				// Add above if not already checked
    				if (p.getY() > 0 && !checkedPixels[p.getX()][p.getY() - 1]) {
    					newPixelList.add(new Pixel(p.getX(), p.getY() - 1));
    				}    				
    			}
    		}
    		
    		recentPixels.clear();
    		recentPixels.addAll(newPixelList);
    	}
    	
    	return newShapeMatrix;
    }
        
    private Diagram generateSolutionDiagram(Diagram A, List<Transformation> transformations) {
    	
    	System.out.println("\n---Beginning generateSolution to generate our final answer!");    	
    	// TODO: If there is only one shape A, then rotation should be applied to all shapes in D
    	
    	Diagram solution = new Diagram();
    	List<Shape> shapeList = new ArrayList<Shape>();
    	solution.setName("D");

    	for (Transformation t : transformations) System.out.print(t.getTransformation() + "-" + t.getIndexOfShape() + ", ");
    	System.out.println();
    	
    	// Delete the shapes that are removed
    	for (int i = 0; i < A.getShapeList().size(); i++) {
    		
    		Shape shape = A.getShapeList().get(i);
    		
    		for (Transformation t : transformations) {
	    		
    			if (t.getIndexOfShape() != i && !disableDeletionIndexing) continue;
    			
	    		if (t.getTransformation() == Transformations.DELETESHAPE) {
	    			System.out.println("Delete this shape");
	    			shape = null;
	    			break;
	    		}
	    	}
    		
    		if (shape != null) shapeList.add(shape);
    	}
    	    	
    	// Add in the shapes that are created
    	for (Transformation t : transformations) {
    		if (t.getTransformation() == Transformations.ADDSHAPE) {
    			System.out.println("Adding Shape " + t.getShape().getShape());
				shapeList.add(t.getShape());
			}
    	}
    	
    	List<Shape> finalList = new ArrayList<Shape>();
    	
    	// Modify the remaining shapes
    	for (int i = 0; i < shapeList.size(); i++) {
    		
    		Shape shape = shapeList.get(i);
    		System.out.println("This shape is a " + shape.getShape() + ", " + shape.toString());
    		
    		for (Transformation t : transformations) {
	    
    			System.out.println(t.getTransformation().toString());
    			
    			if (t.getIndexOfShape() != i && !(A.getShapeList().size() == 1 && t.getTransformation() == Transformations.ROTATION)) continue;
    			
    			// TODO: Mirroring overwrites things, might cause issues
    			
    			if (t.getTransformation() == Transformations.MIRRORING_XAXIS) {
    				
    				Shape mirroredXWise = mirrorOverXAxis(shape);
    				
    				Textures texture = shape.getTexture();
    				shape = new Shape();
    				shape = buildShape(mirroredXWise.getShapeMatrix(), shape);
    				shape.setTexture(texture);
    			}
    			
    			if (t.getTransformation() == Transformations.MIRRORING_YAXIS) {
    			
    				Shape mirroredYWise = mirrorOverYAxis(shape);
    				
    				Textures texture = shape.getTexture();
    				shape = new Shape();
    				shape = buildShape(mirroredYWise.getShapeMatrix(), shape);
    				shape.setTexture(texture);
    			}	
    	    		
	    		// TODO: We are setting the rotation variable, but not actually rotating the image
	    		if (t.getTransformation() == Transformations.ROTATION) {
	    			
	    			if (shape.getFills() != Fills.NONE) {
		    			// If rotating, change half fill
		    			int rotation = t.getRotation();
		    			
		    			while (rotation >= 90) {
		    				
		    				if (shape.getFills() == Fills.TOP_RIGHT) shape.setFills(Fills.TOP_LEFT);
		    				else if (shape.getFills() == Fills.TOP_LEFT) shape.setFills(Fills.BOTTOM_LEFT);
		    				else if (shape.getFills() == Fills.BOTTOM_LEFT) shape.setFills(Fills.BOTTOM_RIGHT);
		    				else if (shape.getFills() == Fills.BOTTOM_RIGHT) shape.setFills(Fills.TOP_RIGHT);
		    				
		    				if (shape.getFills() == Fills.RIGHT) shape.setFills(Fills.TOP);
		    				else if (shape.getFills() == Fills.TOP) shape.setFills(Fills.LEFT);
		    				else if (shape.getFills() == Fills.LEFT) shape.setFills(Fills.BOTTOM);
		    				else if (shape.getFills() == Fills.BOTTOM) shape.setFills(Fills.RIGHT);
		    				
		    				rotation -= 90;
		    			}
		    			System.out.println("Fill has been updated to: " + shape.getFills());
	    			}
	    			
	    			System.out.println("This is a rotation transformation with rotation: " + t.getRotation());
	    			shape.setRotation((shape.getRotation() + t.getRotation()) % 360);
	    		}

	    		if (t.getTransformation() == Transformations.SHIFT_X) {
	    			shape.setCenter(new Pixel(t.getCenterX(), shape.getCenter().getY()));
	    		}
	    		
	    		if (t.getTransformation() == Transformations.SHIFT_Y) {
	    			shape.setCenter(new Pixel(shape.getCenter().getX(), t.getCenterY()));
	    		}
	    		
				if (t.getTransformation() == Transformations.SIZE) {
					shape.setSize(t.getSizeB());
				}
				
				if (t.getTransformation() == Transformations.TEXTURE) {
					shape.setTexture(t.getTextureB());
				}
				
				if (t.getTransformation() == Transformations.HALF_FILL) {
					shape.setFills(t.getFillsB());
				}
	    	}    		
    		
    		finalList.add(shape);
    	}
    	solution.getShapeList().addAll(finalList);
    	
    	System.out.println("Done generating the diagram D ****");
    	for (Shape shape : solution.getShapeList()) System.out.println(shape.toString());
    	return solution;
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
		List<Integer> matchedShapes = new ArrayList<Integer>(); // Contains the shapes in d2 that have been mapped
    	    		
		// In terms of building a mapping, an object can move position, rotate, or change texture
		// TODO: For now, don't worry about shapes changing size
		// TODO: If there is only one shape in each diagram and they are mirrors of each other then we should skip 

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
    				
    				// TODO: They don't have to be the same size, but should match up right
    				// Are they about the same size?
    				//if ( (Math.abs(baseShape.getWidth() - compareShape.getWidth()) < 5) 
    				//		&& (Math.abs(baseShape.getHeight() - compareShape.getHeight()) < 5)) {	   
    				// }
    				
    				// TODO: Do at better job determine best match
    				if (partnerShape != null) {
    					
    					// We have already matched this shape up, is this shape a better match?
    					
    					// If the texture is a better match then choose this one
    					if (baseShape.getTexture() == compareShape.getTexture() && baseShape.getTexture() != partnerShape.getTexture()) {
    						partnerShape = compareShape;
        					indexOfBestMatch = j;
    					}
    					
    					// If it is closer in size then choose this one
    					int currentWidthDiff = Math.abs(baseShape.getWidth() - partnerShape.getWidth());
    					int currentHeightDiff = Math.abs(baseShape.getHeight() - partnerShape.getHeight());
    					int newWidthDiff = Math.abs(baseShape.getWidth() - compareShape.getWidth());
    					int newHeightDiff = Math.abs(baseShape.getHeight() - compareShape.getHeight());
    					
    					if (newWidthDiff < currentWidthDiff && newHeightDiff < currentHeightDiff) {
    						partnerShape = compareShape;
        					indexOfBestMatch = j;
    					}
    					
    				} else {
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
    	    System.out.println("Key: " + key + ", Value: " + value);
    	 
    	}
		
    	// Create transformations for every shape in d1
    	for (Map.Entry<Integer, Integer> entry : mapping.entrySet()) {
    	    Integer key = entry.getKey();
    	    Integer value = entry.getValue();
    	    
    	    if (value == -1) {
    	    	
    	    	// This shape not mapped to any other shapes, add a delete shape transformation
    	    	System.out.println("Creating a DELETE Transformation");
    	    	transformations.add(new Transformation(key, false, d1.getShapeList().get(key)));
    	    	
    	    } else {    	    	
    	    	transformations.addAll(sameShapeTransform(key, d1.getShapeList().get(key), d2.getShapeList().get(value)));    	    	    	    	
    	    }
    	}
    	
    	// Need to check for any remaining shapes in d2 that didn't get mapped to a shape in d1
		for (int i = 0; i < d2.getShapeList().size(); i++) {
		
			if (matchedShapes.contains(i)) continue;
			System.out.println("Creating an ADD transformation");
			transformations.add(new Transformation(-1, true, d2.getShapeList().get(i)));		
		}
		
		// TODO: This is a bit of a hack to make it work
		// If both diagrams have only one shape and the types don't match up then we need to preserve other transformations
		if ( (d1.getShapeList().size() == 1 && d2.getShapeList().size() == 1)
				&& (d1.getShapeList().get(0).getShape() != d2.getShapeList().get(0).getShape()) ) {
			
			// Since the shapes don't match, we created a Delete transformation AND a Add transformation.
			// However, we want to keep the other transformations, such as rotation and region changes
			
			boolean containsDeletes = false;
			boolean containsMirroring = false;
			
			for (Transformation t : transformations) {
				if (t.getTransformation() == Transformations.DELETESHAPE) {
					containsDeletes = true;
					break;
				}
			}
			
			List<Transformation> newTransforms = sameShapeTransform(0, d1.getShapeList().get(0), d2.getShapeList().get(0));
			
			for (Transformation t : newTransforms) {
				if (t.getTransformation() == Transformations.MIRRORING_XAXIS 
						|| t.getTransformation() == Transformations.MIRRORING_YAXIS) {
					containsMirroring = true;
					break;
				}
			}
			
			if (containsDeletes && containsMirroring) {
				
				// This means there is only 1 shape in both diagrams. The shapes are different types, but are mirrors of each other.
				// In this case we disregard deleting and adding shapes and only use mirroring, etc.		
				transformations.clear();
			} 
			
			transformations.addAll(newTransforms);
		}
    	
    	System.out.println("DONE BUILDING TRANSFORMATIONS, returning list of size: " + transformations.size());
    	return transformations;    	
    }
    
    private List<Transformation> sameShapeTransform(int indexOfShape, Shape s1, Shape s2) {
    	
    	System.out.println("STARTING SAME SHAPE TRANSFORM");
    	
    	List<Transformation> transformations = new ArrayList<Transformation>();
    	boolean isMirroring = false;
    	
    	// Note: For whatever reason, Mirroring seems to be more important that rotation, so check mirroring first and
    	// then only check for rotation there isn't a mirroring transformation occurring 
    	
    	// Check if shapes are mirrors of each other, don't bother with squares/circles
    	if (s1.getShape() != Shapes.SQUARE && s1.getShape() != Shapes.CIRCLE && s1.getShape() != Shapes.PLUS 
    			&& s1.getShape() != Shapes.OCTAGON && s1.getShape() != Shapes.DIAMOND && s1.getShape() != Shapes.HEART) {
    		
    		// Disabling mirroring for 3x3 Problems
    		if (!disableMirroring) {
	    		Shape mirroredXWise = mirrorOverXAxis(s2);
	        	Shape mirroredYWise = mirrorOverYAxis(s2);
	        	
	        	buildShape(mirroredXWise.getShapeMatrix(), mirroredXWise);
	        	buildShape(mirroredYWise.getShapeMatrix(), mirroredYWise);
	        	
	        	if (s1.getShape() == mirroredXWise.getShape() && s1.getRotation() == mirroredXWise.getRotation()) {
	            	// x-wise mirror
	        		isMirroring = true;
	            	transformations.add(new Transformation(indexOfShape, true));
	        	}
	        	
	        	if (s1.getShape() == mirroredYWise.getShape() && s1.getRotation() == mirroredYWise.getRotation()) {
	            	// y-wise mirror
	        		isMirroring = true;
	            	transformations.add(new Transformation(indexOfShape, false));        	
	        	}		
    		}
    	}
    	
    	// Only bother checking if we don't have a Mirroring
    	if (!isMirroring) {
    		
    		if (s1.getRotation() != s2.getRotation()) {
    			
    			if (s1.getRotation() < s2.getRotation()) {
    				transformations.add(new Transformation(indexOfShape, s2.getRotation() - s1.getRotation()));
    			} else {
    				transformations.add(new Transformation(indexOfShape, 360 - Math.abs(s1.getRotation() - s2.getRotation())));
    			}    			
    		}
    		
    		// If they are both centered then don't bother with region 
    		if (!s1.isCentered() || !s2.isCentered()) {
    		
    			// Build Shift type transformations
    			if (Math.abs(s1.getCenter().getX() - s2.getCenter().getX()) > 10) {    				
    				transformations.add(new Transformation(indexOfShape, true, s2.getCenter().getX(), -1));
    			}
    			
    			if (Math.abs(s1.getCenter().getY() - s2.getCenter().getY()) > 10) {
    				transformations.add(new Transformation(indexOfShape, false, -1, s2.getCenter().getY()));
    			}
    		}
    		
    		if (s1.getFills() != s2.getFills()) {
    			transformations.add(new Transformation(indexOfShape, s1.getFills(), s2.getFills()));
    		}			
    	}    	
    	
		if (s1.getTexture() != s2.getTexture()) {
			transformations.add(new Transformation(indexOfShape, s1.getTexture(), s2.getTexture()));
		}
		
		if (s1.getSize() != s2.getSize() && !disableSizing) {
			transformations.add(new Transformation(indexOfShape, s1.getSize(), s2.getSize()));
		}
		
		return transformations;
    }
    
    private Shape rotateShape(Shape shape, int rotation) {
    	
    	if (rotation % 90 != 0) return null;
    	
    	int degreesLeft = rotation;
    	
    	boolean[][] matrix = shape.getShapeMatrix();
    	while (degreesLeft > 0) {
    		matrix = rotateMatrix90Degrees(matrix);
    		degreesLeft -= 90;
    	}
    	    
    	Shape newShape = new Shape();
    	newShape.setShapeMatrix(matrix);    	
		findAndSetEdgePixels(matrix, newShape);
		// printEdgePixels(newShape);
		return newShape;
    }
    
    private boolean[][] rotateMatrix90Degrees(boolean[][] matrix) {
    	
    	// printMatrix(matrix)    	
    	boolean[][] rotatedMatrix = new boolean[184][184];
    	
		// rotate 90 degrees
		for (int y = 0; y < 184; y++) {
			for (int x = 0; x < 184; x++) {				
				if (matrix[x][y]) {
					rotatedMatrix[y][184 - 1 - x] = true;
				}
			}
		}
    	
		// printMatrix(rotatedMatrix);		
		return rotatedMatrix;
    }
    
    private Shape buildShape(boolean[][] matrix, Shape shape) {    	
		shape.setShapeMatrix(matrix);
		findAndSetEdgePixels(matrix, shape);
		printEdgePixels(shape);
    	discoverShapeType(shape);
    	determineShapeFill(shape);
    	discoverHalfFill(shape);    	
    	determineShapeSize(shape);
    	System.out.println(shape.toString());
    	return shape;
    }

	private void findAndSetEdgePixels(boolean[][] matrix, Shape shape) {
    	 
		//When looping through pixels we start at top left and work right and down
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
		
		shape.setHeight(shape.getBottomMostPixel().getY() - shape.getTopMostPixel().getY());
    	shape.setWidth(shape.getRightMostPixel().getX() - shape.getLeftMostPixel().getX());
    	shape.setCenter(new Pixel(shape.getLeftMostPixel().getX() + shape.getWidth()/2, shape.getTopMostPixel().getY() + shape.getHeight()/2));
	}
	
	private void determineShapeSize(Shape shape) {		
		if (shape.getWidth() < 50 && shape.getHeight() < 50) {
			shape.setSize(Sizes.SMALL);
		} else if (shape.getWidth() < 100 && shape.getHeight() < 100) {
			shape.setSize(Sizes.MEDIUM);
		} else if (shape.getWidth() < 150 && shape.getHeight() < 150) {
			shape.setSize(Sizes.LARGE);
		} else {
			shape.setSize(Sizes.EXTRA_LARGE);
		}		
	}
	
    // Take in an ambiguous shape and determine if it is a known shape
    private Shape discoverShapeType(Shape shape) {
    	
    	if (determineIfRectangular(shape)) {}
		else if (determineIfCircleOrRelated(shape)) {}
    	else if (determineIfTriangle(shape)) {}
		else if (determineIfHeart(shape)) {}
		else {
    		handleUnknownShape(shape);
    	}
		return shape;
    }
    
    private boolean determineIfRectangular(Shape shape) {    	 
    	// Square: TopMost and BottomMost have same X, leftMost and RightMost have same Y
    	// And top right is enabled    	    	
    	if (shape.getTopMostPixel().getX() == shape.getBottomMostPixel().getX() 
				&& shape.getTopMostPixel().getY() == shape.getRightMostPixel().getY() 
				&& shape.getShapeMatrix()[shape.getRightMostPixel().getX()][shape.getBottomMostPixel().getY()]) {
						
			if (compareVals(shape.getWidth(), shape.getHeight())) {
				shape.setShape(Shapes.SQUARE);
			} else {
				shape.setShape(Shapes.RECTANGLE);
			}
			
			System.out.println("SHAPE IS " + shape.getShape());
			return true;
		}		
    	return false;
    }
    
    // TODO: This method is a huge giant mess
    private boolean determineIfCircleOrRelated(Shape shape) {
    
    	if (compareVals(shape.getTopMostPixel().getX(), shape.getBottomMostPixel().getX()) 
				&& compareVals(shape.getLeftMostPixel().getY(), shape.getRightMostPixel().getY())  
				&& !shape.getShapeMatrix()[shape.getRightMostPixel().getX()][shape.getTopMostPixel().getY()] 
				&& !shape.getShapeMatrix()[shape.getRightMostPixel().getX()][shape.getBottomMostPixel().getY()] 
				&& !shape.getShapeMatrix()[shape.getLeftMostPixel().getX()][shape.getTopMostPixel().getY()] 
				&& !shape.getShapeMatrix()[shape.getLeftMostPixel().getX()][shape.getBottomMostPixel().getY()]) {
		
			boolean isPlusSign = true;
			int rowValue = 0;
			int columnValue = 0;
			
			// TODO: Pacmans are considered unknown if they have a 45 degree rotation
			
			// Is there any pixel above the leftMost and to the left of TopMost?
			for (int row = shape.getTopMostPixel().getY(); row < shape.getLeftMostPixel().getY() - 5; row++) {
				for (int column = shape.getLeftMostPixel().getX(); column < shape.getTopMostPixel().getX() - 5; column++) {
					
					if (shape.getShapeMatrix()[column][row]) {
						isPlusSign = false;
						rowValue = row;
						columnValue = column;
						break;
					}
				}
				if (isPlusSign == false) break;
			}
		
			// This logic works for all the pacmans except for when the top left of pacman is open
			Shape mirrorShape = mirrorOverXAxis(shape);
			
			boolean isMirroredPlus = true;
			int pixelCount = 0;
			// Is there any pixel above the leftMost and to the left of TopMost?
			for (int row = 0; row < shape.getLeftMostPixel().getY() - 5; row++) {
				for (int column = 0; column < shape.getTopMostPixel().getX() - 5; column++) {
					if (mirrorShape.getShapeMatrix()[row][column]) {
						pixelCount++;
						if (pixelCount > 5) {
							isMirroredPlus = false;
							break;
						}						
					}
				}
				if (isMirroredPlus == false) break;
			}

			System.out.println("IsPlusSign: " + isPlusSign + ", isMirroredPlus: " + isMirroredPlus);
			if (isPlusSign && isMirroredPlus) {
				shape.setShape(Shapes.PLUS);
				
				int count = 0;
				
				// But wait, it might actually be a plus with a 45 degree rotation
				for (int j = 0; j < 184; j++) {
					if (shape.getShapeMatrix()[shape.getTopMostPixel().getY()][j]) {
						count++;
					}
				}
			
				if (count < 7) {
					shape.setRotation(45);
				}

				System.out.println("Shape is PLUS with rotation: " + shape.getRotation());
				return true;
			} else  if (isPlusSign && !isMirroredPlus){
				System.out.println("Shape is PACMAN with Zero rotation");
				shape.setShape(Shapes.PLUS); // TODO: I CHANGED THIS FROM PACMAN TO PLUS
				return true;
			}
			
//			if (rowValue != 0 && columnValue != 0) {
//								
//				boolean foundTopRight = false;
//				boolean foundBottomRight = false;
//				boolean foundBottomLeft = false;
//				int rowStart = 0;
//				int colStart = 0;
//				
//				// Look in the top right region		
//				rowStart = rowValue;
//				colStart = 184 - columnValue;
//				
//				for (int i = 0; i < 10; i++) {
//					if (shape.getShapeMatrix()[colStart][rowStart + i]) {
//						foundTopRight = true;
//					}
//				}
//		
//				// Look at bottom left				
//				rowStart = 184 - rowValue;
//				colStart = columnValue;
//				
//				for (int i = 0; i < 10; i++) {
//					if (shape.getShapeMatrix()[colStart][rowStart - i]) {
//						foundBottomLeft = true;
//					}
//				}
//				
//				// Look at bottom right
//				rowStart = 184 - rowValue;
//				colStart = 184 - columnValue;
//				
//				for (int i = 0; i < 10; i++) {
//					if (shape.getShapeMatrix()[colStart][rowStart - i]) {
//						foundBottomRight = true;
//					}
//				}
//							
//				if (foundTopRight && foundBottomRight && !foundBottomLeft) {
//					System.out.println("Shape is PACMAN with 90 rotation");
//					shape.setShape(Shapes.PACMAN);
//					shape.setRotation(90);
//					return true;
//				}
//				
//				if (foundTopRight && !foundBottomRight && foundBottomLeft) {
//					System.out.println("Shape is PACMAN with 180 rotation");
//					shape.setShape(Shapes.PACMAN);
//					shape.setRotation(180);
//					return true;
//				}
//				
//				if (!foundTopRight && foundBottomRight && foundBottomLeft) {
//					System.out.println("Shape is PACMAN with 270 rotation");
//					shape.setShape(Shapes.PACMAN);
//					shape.setRotation(270);
//					return true;
//				}
//			}
			
			// It might actually be a diamond 
			int count = 0;
			for (int j = 0; j < 184; j++) {
				if (shape.getShapeMatrix()[j][shape.getTopMostPixel().getY()]) {
					count++;
				}
			}
						
			// If it is a diamond then it will only have a couple pixels in it's top row
			if (count < 5) {
				System.out.println("Shape is DIAMOND");
				shape.setShape(Shapes.DIAMOND);
				return true;
			}
						
			// It might actually be a Octagon
			// Check to see if both the top and bottom most pixels are left of center
			if ( (shape.getTopMostPixel().getX() < shape.getLeftMostPixel().getX() + (shape.getWidth() / 2) - DELTA)
					&& (shape.getBottomMostPixel().getX() < shape.getLeftMostPixel().getX() + (shape.getWidth() / 2) - DELTA) ) {
				
				System.out.println("Shape is an OCTAGON");
				shape.setShape(Shapes.OCTAGON);
				return true;
			}
			
			// Else it is a Circle
			System.out.println("Shape is CIRCLE");
			shape.setShape(Shapes.CIRCLE);
			return true;
			
    	}
		return false;
    }
    
    private boolean determineIfTriangle(Shape shape) {
    	int rotation = 0;
		while (rotation < 360) {
	
			//System.out.println("ROTATING: " + rotation);
			Shape rotatedShape = rotateShape(shape, rotation);
			
			// Right triangle
			if (compareVals(rotatedShape.getTopMostPixel().getX(), rotatedShape.getRightMostPixel().getX()) 
					&& compareVals(rotatedShape.getLeftMostPixel().getY(), rotatedShape.getBottomMostPixel().getY()) 
					&& !rotatedShape.getShapeMatrix()[rotatedShape.getLeftMostPixel().getX()][rotatedShape.getTopMostPixel().getY()]) {
				
				System.out.println("Shape is Right triangle with rotation: " + rotation);
				shape.setShape(Shapes.RIGHT_TRIANGLE);
				shape.setRotation(rotation);
				return true;
			}     
			
			// Equilatiral Triangle
			if (compareVals(rotatedShape.getBottomMostPixel().getX(), rotatedShape.getLeftMostPixel().getX()) 
					&& compareVals(rotatedShape.getRightMostPixel().getY(), rotatedShape.getBottomMostPixel().getY())
					&& compareVals(rotatedShape.getLeftMostPixel().getY(), rotatedShape.getBottomMostPixel().getY()) 
					&& !rotatedShape.getShapeMatrix()[rotatedShape.getLeftMostPixel().getX()][rotatedShape.getTopMostPixel().getY()]
					&& !rotatedShape.getShapeMatrix()[rotatedShape.getRightMostPixel().getX()][rotatedShape.getTopMostPixel().getY()]) {
				
				System.out.println("Shape is Equalatiral Triangle with rotation: " + rotation);
				shape.setShape(Shapes.TRIANGLE);
				shape.setRotation(rotation);
				return true;
			}

			rotation += 90;
		}
		return false;
    }
    
    private boolean determineIfHeart(Shape shape) {
    	int rotation = 0;
		while (rotation < 360) {
	
			//System.out.println("ROTATING: " + rotation);
			Shape rotatedShape = rotateShape(shape, rotation);
		
			// Heart
			if ((rotatedShape.getTopMostPixel().getX() < rotatedShape.getBottomMostPixel().getX() - 10) 
					&& compareVals(rotatedShape.getRightMostPixel().getY(), rotatedShape.getLeftMostPixel().getY())) {
				
				int count = 0;
				for (int j = 0; j < 184; j++) {
					if (rotatedShape.getShapeMatrix()[j][rotatedShape.getBottomMostPixel().getY()]) {
						count++;
					}
				}
				
				// If it is a diamond then it will only have a couple pixels in it's top row
				if (count < 5) {
					System.out.println("Shape is HEART");
					shape.setShape(Shapes.HEART);
					return true;
				}
			}
	    
			rotation += 90;
		}
		return false;
    }
    
    private void handleUnknownShape(Shape shape) {
    	    	
		System.out.println("This is going to be an unknown shape");
		for (Shape unknown : unknownShapes) {
			
			int rotation = 0;
			while (rotation < 360) {
		
				Shape rotatedShape = rotateShape(shape, rotation);
				
				// If this shape matches one the unknown shapes then use that
				if (compareVals(Math.abs(rotatedShape.getBottomMostPixel().getX() - rotatedShape.getTopMostPixel().getX()), Math.abs(unknown.getBottomMostPixel().getX() - unknown.getTopMostPixel().getX()))
						&& compareVals(Math.abs(rotatedShape.getBottomMostPixel().getX() - rotatedShape.getLeftMostPixel().getX()), Math.abs(unknown.getBottomMostPixel().getX() - unknown.getLeftMostPixel().getX()))
						&& compareVals(Math.abs(rotatedShape.getBottomMostPixel().getX() - rotatedShape.getRightMostPixel().getX()), Math.abs(unknown.getBottomMostPixel().getX() - unknown.getRightMostPixel().getX()))
						&& compareVals(Math.abs(rotatedShape.getTopMostPixel().getX() - rotatedShape.getLeftMostPixel().getX()), Math.abs(unknown.getTopMostPixel().getX() - unknown.getLeftMostPixel().getX()))
						&& compareVals(Math.abs(rotatedShape.getTopMostPixel().getX() - rotatedShape.getRightMostPixel().getX()), Math.abs(unknown.getTopMostPixel().getX() - unknown.getRightMostPixel().getX()))
						&& compareVals(Math.abs(rotatedShape.getLeftMostPixel().getX() - rotatedShape.getRightMostPixel().getX()), Math.abs(unknown.getLeftMostPixel().getX() - unknown.getRightMostPixel().getX()))
						) {
							
							System.out.println("Setting shape type to " + unknown.getShape() + " With Rotation: " + rotation);
							shape.setShape(unknown.getShape());
							shape.setRotation(rotation);
							return;					
				}
				
				
				rotation += 90;
			}
			
		}
		
		if (unknownShapes.size() == 0) shape.setShape(Shapes.UNKNOWN_1);
		else if (unknownShapes.size() == 1) shape.setShape(Shapes.UNKNOWN_2);
		else if (unknownShapes.size() == 2) shape.setShape(Shapes.UNKNOWN_3);
		else if (unknownShapes.size() == 3) shape.setShape(Shapes.UNKNOWN_4);
		else if (unknownShapes.size() == 4) shape.setShape(Shapes.UNKNOWN_5);
		else if (unknownShapes.size() == 5) shape.setShape(Shapes.UNKNOWN_6);
		else if (unknownShapes.size() == 6) shape.setShape(Shapes.UNKNOWN_7);
		else if (unknownShapes.size() == 7) shape.setShape(Shapes.UNKNOWN_8);
		else shape.setShape(Shapes.UNKNOWN_9);
		
		unknownShapes.add(shape);
		System.out.println("UNKNOWN SHAPE: " + shape.getShape() + " with rotation: " + shape.getRotation());
    }
    
    private void determineShapeFill(Shape shape) {    	
    	if (isShapeSolid(shape)) {
    		shape.setSolid(true);
    	} else {
    		shape.setHollow(true);
    	}
    }
    
    private boolean isShapeSolid(Shape shape) {
    	
    	int pixelCount = countPixels(shape);    	
    	int expectedPixelCount = 0;
    	int areaDelta = 25;
    	    	
    	if (shape.getShape() == Shapes.SQUARE || shape.getShape() == Shapes.RECTANGLE) {    		
    		expectedPixelCount = shape.getWidth() * shape.getHeight();
    	} else if (shape.getShape() == Shapes.CIRCLE) {    		
    		expectedPixelCount = (int) (Math.PI * (Math.pow((shape.getWidth() / 2), 2)));
    	} else if (shape.getShape() == Shapes.PACMAN) {
    		expectedPixelCount = (int) (Math.PI * (Math.pow((shape.getWidth() / 2), 2)) * .75);
    	} else if (shape.getShape() == Shapes.RIGHT_TRIANGLE) {
    		expectedPixelCount = (int) (shape.getHeight() * shape.getWidth() * .5);
    	} else if (shape.getShape() == Shapes.TRIANGLE) {
    		expectedPixelCount = (int) (shape.getHeight() * shape.getWidth() * .5);
    	} else if (shape.getShape() == Shapes.DIAMOND) {
    		expectedPixelCount = (int) ( (shape.getWidth() + 2) * (shape.getWidth() + 2) * .5);
    	} else if (shape.getShape() == Shapes.PLUS) {
    		
    		int edgeWidth = 0;
			for (int j = 0; j < 184; j++) {
				if (shape.getShapeMatrix()[j][shape.getTopMostPixel().getY()]) {
					edgeWidth++;
				}
			}
						
			// TODO: If is is rotated 45 degrees then this won't work
			
    		expectedPixelCount = (edgeWidth * shape.getWidth() * 2) - (edgeWidth*edgeWidth);
			
    	} else {
    		
    		// Otherwise if the pixel count is over 40% the bounding box then call it solid
    		expectedPixelCount = (int) (shape.getWidth() * shape.getWidth() * .4);
    	}
    	
    	boolean isSolid = pixelCount >= expectedPixelCount - areaDelta; 
    	//System.out.println("PixelCount: " + pixelCount + ", expectedPixelCount: " + expectedPixelCount);
    	//printEdgePixels(shape);
    	return isSolid;
    }
    
    private Shape mirrorOverXAxis(Shape shape) {
    
    	Shape mirroredShape = new Shape();
    	boolean[][] mirroredMatrix = new boolean[184][184];
    	
    	// printMatrix(shape.getShapeMatrix());
    	for (int row = 0; row < 184; row++) {
    		for (int column = 0; column < 184; column++) {
    			
    			if (shape.getShapeMatrix()[row][column]) {
    				mirroredMatrix[183 - row][column] = true;	
    			}
    		}
    	}
    	
    	// System.out.println();
    	// printMatrix(mirroredMatrix);
    	mirroredShape.setShapeMatrix(mirroredMatrix);
    	return mirroredShape;
    }
    
    private Shape mirrorOverYAxis(Shape shape) {
    	
    	Shape mirroredShape = new Shape();
    	boolean[][] mirroredMatrix = new boolean[184][184];
    	
    	//printMatrix(shape.getShapeMatrix());
    	
    	for (int row = 0; row < 184; row++) {
    		for (int column = 0; column < 184; column++) {
    			
    			if (shape.getShapeMatrix()[row][column]) {
    				mirroredMatrix[row][183 - column] = true;	
    			}
    		}
    	}
    	
    	//printMatrix(mirroredMatrix);	    	
    	mirroredShape.setShapeMatrix(mirroredMatrix);
    	return mirroredShape;    	
    }
      
    private int countPixels(Shape shape) {
    	
    	int count = 0;
    	for (int j = 0; j < 184; j++) {
        		for (int i = 0; i < 184; i++) {
        			if (shape.getShapeMatrix()[i][j]) {
    	    			count++;
        			}
        		}
        	}
    	
    	return count;
    }
    
    private int countPixels(Diagram diagram) {
    
    	int count = 0;
	    for (int j = 0; j < 184; j++) {
			for (int i = 0; i < 184; i++) {
				if (diagram.getMatrix()[i][j]) {
					count++;
				}    				
			}
		}
	    
	    return count;
    }
    
    private void discoverHalfFill(Shape shape) {
    	
    	// If the shape is solid then clearly can't be half-filled. If it is unknown then don't bother.
    	if (shape.isSolid() || shape.isUnknownShape()) {
    		return;
    	}
    	
    	int count = countPixels(shape);
    	int leftCount = 0;
    	int rightCount = 0;
    	int topCount = 0;
    	int bottomCount = 0;
    	
    	int topRightCount = 0;
    	int topLeftCount = 0;
    	int bottomRightCount = 0;
    	int bottomLeftCount = 0;
    	  	
    	int xLine = 92;
    	int yLine = 92;
    	
    	if (!shape.isCentered()) {
    		xLine = shape.getCenter().getX();
    		yLine = shape.getCenter().getY();
    	}
    	
    	// if left full and right empty then left fill
    	for (int i = 0; i < 184; i++) {
    		for (int j = 0; j < 184; j++) {
    			if (shape.getShapeMatrix()[i][j]) {
    				
    				if (i < xLine) leftCount++;
    				if (i >= xLine) rightCount++;
    				if (j < yLine) topCount++;
    				if (j >= yLine) bottomCount++;
    				
    				if (i < xLine && j < yLine) topLeftCount++;
    				if (i < xLine && j >= yLine) bottomLeftCount++;
    				if (i >= xLine && j < yLine) topRightCount++;
    				if (i >= xLine && j >= yLine) bottomRightCount++;
    			}
    		}
    	}
    	
    	double treshold = count * .67;
    	if (rightCount > treshold && bottomCount > treshold) {
    		shape.setFills(Fills.BOTTOM_RIGHT);
    	} else if (rightCount > treshold && topCount > treshold) {
    		shape.setFills(Fills.TOP_RIGHT);
    	} else if (leftCount > treshold && bottomCount > treshold) {
    		shape.setFills(Fills.BOTTOM_LEFT);
    	} else if (leftCount > treshold && topCount > treshold) {
    		shape.setFills(Fills.TOP_LEFT);
    	} else if (leftCount > treshold) {
    		shape.setFills(Fills.LEFT);
    	} else if (rightCount > treshold) {
    		shape.setFills(Fills.RIGHT);
    	} else if (topCount > treshold) {
    		shape.setFills(Fills.TOP);
    	} else if (bottomCount > treshold) {
    		shape.setFills(Fills.BOTTOM);
    	}  
    	
    	// TODO: This is a hacky fix to solve square shapes with diagonal lines
    	if ( (shape.getShape() == Shapes.CIRCLE || shape.getShape() == Shapes.SQUARE) && shape.isCentered()
    			&& shape.getFills() != Fills.TOP && shape.getFills() != Fills.BOTTOM && shape.getFills() != Fills.LEFT && shape.getFills() != Fills.RIGHT) {
    		
    		if ((topRightCount + bottomLeftCount) > (topLeftCount + bottomRightCount + 100)) {
    			shape.setRotation(0);
    		} else if ((topLeftCount + bottomRightCount) > (topRightCount + bottomLeftCount + 100)) {
    			shape.setRotation(90);
    		}     		
    	}
    }
    
    private HashMap<String, Diagram> buildDiagramList(RavensProblem problem) {
		
		HashMap<String, Diagram> diagramList = new HashMap<String, Diagram>();
    	
    	// Look at every figure in this problem
    	for(String figureName : problem.getFigures().keySet()) {
    		
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
        		for(int j = 0; j < figureAImage.getHeight(); j++) {
        		
        			int thisPixel = figureAImage.getRGB(j,i);        		
        			if (thisPixel != -1) {
        				diagram.getMatrix()[j][i] = true;
        			}
        		}	
        	}
        	
    		diagramList.put(diagram.getName(), diagram);
    	}
		return diagramList;
	}
    
    private void printMatrix(boolean[][] matrix) {
    	 for (int j = 0; j < 184; j++) {
       		System.out.println();
       		for (int i = 0; i < 184; i++) {
       			if (matrix[i][j]) {
   	    			System.out.print("X");
       			} else {
       				System.out.print("_");
       			}
       		}
       	}
    }
    
    private void printEdgePixels(Shape shape) {
    	System.out.println("TopMost: (" + shape.getTopMostPixel().getX() + ", " + shape.getTopMostPixel().getY() + ")");
		System.out.println("BottomMost: (" + shape.getBottomMostPixel().getX() + ", " + shape.getBottomMostPixel().getY() + ")");
		System.out.println("LeftMost: (" + shape.getLeftMostPixel().getX() + ", " + shape.getLeftMostPixel().getY() + ")");
		System.out.println("RightMost: (" + shape.getRightMostPixel().getX() + ", " + shape.getRightMostPixel().getY() + ")");
    }
    
    private void printTransformations(List<Transformation> transformations, Diagram diagram) {
    	System.out.println("\nThese are all of the transformations from A -> B and A-> C");
    	for (Transformation t : transformations) {
    		
    		String shapeName = Shapes.NONE.toString();
    		if (t.getIndexOfShape() != -1) {
    			shapeName = diagram.getShapeList().get(t.getIndexOfShape()).getShape().toString();
    		}
    		System.out.println(shapeName + ", " + t.getTransformation() + ", Index: " + t.getIndexOfShape());
    	}
    	System.out.println();
    }
    
    private static boolean compareVals(int a, int b) {
    	if ( (a == b) || (Math.abs(a - b) <= DELTA)) {
    		return true;
    	}
    	return false;
    }
}