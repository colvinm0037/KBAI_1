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
    	LARGE
    }
    
    public enum Transformations {
    	NONE,
    	SIZE,
    	REGION,
    	ROTATION,
    	TEXTURE,
    	ADDSHAPE,
    	DELETESHAPE,
    	MIRRORING_XAXIS,
    	MIRRORING_YAXIS,
    	HALF_FILL
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
    	private Fills fillsA;
    	private Fills fillsB;
    	
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
    	int region = 0;
        Shapes shape = Shapes.NONE;    	
    	Textures texture = Textures.HOLLOW;
        Sizes size = Sizes.SMALL;
        Fills fills = Fills.NONE;
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
		
    }
    
    public class Diagram {
    	
    	public Diagram() {
    		
    	}
    	
    	String name = "";
    	boolean[][] matrix = new boolean[184][184];
    	List<Shape> shapeList = new ArrayList<Shape>();
    
    	public boolean isIdenticalMatch(Diagram d2) {
    		
    		// printMatrix(this.getMatrix());
    		
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
    	//System.out.println("Starting SOLVE");
    	
    	// NOTE: WHEN SUBMITTING REMEMBER TO MOVE FILES!
    	
    	// Long-term issues
    	// TODO: Need to be able to handle 3x3 Problems
    	// TODO: Many questions have overlapping shapes, what can I do to handle those? Traversal method doesn't work.
    	// TODO: Actually create an image/matrix of D so that I can check it visually
    	// TODO: Need to be able to recognize rectangles as shapes
    	// TODO: Need to be able to recognize diamonds as shapes
    	// TODO: Better fill logic would be nice 
    	// TODO: Need a comprehensive method of generating a solution, comparing it to available answers, and then
    	// going back and generating another solution if we didn't get a good enough answer that makes sense
    	
    	// TODO: Refactor code
    	// TODO: Move other classes into a different file
    	
    	// Short Term Issues
    	// What are the messiest parts of my code?
    	// Likely issues 
    	//   Problem 2, Considers rotated plus to be a pacman
    	//   Problem 5, with region, mirroring
    	//   Problem 6, with weird corner fills
    	//   Problem 8, with half fills
    	//  Maybe issues with not recognizing other basic shapes like diamond, octagon
    	
    	    	
    	if (!( problem.getName().equals("Basic Problem B-11"))) return -1;

    	System.out.println("Name: " + problem.getName() + ", Type: " + problem.getProblemType());
    	
    	HashMap<String, Diagram> diagramList = buildDiagramList(problem);
    	    
    	buildShapesInDiagrams(diagramList);
    
    	List<Transformation> transformations = buildAllTransformations(diagramList);
    	
    	Diagram D = generateSolutionDiagram(diagramList.get("A"), transformations);
    	
    	String chosenAnswer = determineFinalAnswer(diagramList, D);
    	
    	System.out.println("Finishing " + problem.getName() + " and returning: " + chosenAnswer);
    	return Integer.parseInt(chosenAnswer);    	
    }
    
    private void buildShapesInDiagrams(HashMap<String, Diagram> diagramList) {
    	
    	for (String name : Arrays.asList("A", "B", "C", "1", "2", "3", "4", "5", "6")) {
    		   		
    		System.out.println("\nLOOKING AT DIAGRAM " + name);
    		Diagram diagram = diagramList.get(name);
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
    
    private List<Transformation> buildAllTransformations(HashMap<String, Diagram> diagramList) {
    	
    	// Build up list of transformations between A->B and A->C
    	System.out.println("\nBuilding the transformations from A to B and from B to C");
    	List<Transformation> transformations = new ArrayList<Transformation>();
    	transformations.addAll(buildTransformations(diagramList.get("A"), diagramList.get("B")));
    	transformations.addAll(buildTransformations(diagramList.get("A"), diagramList.get("C")));
    	    	
    	// Print out transformations
    	System.out.println("These are all of the transformations from A -> B and A-> C");
    	for (Transformation t : transformations) {
    		
    		String shapeName = Shapes.NONE.toString();
    		if (t.getIndexOfShape() != -1) {
    			shapeName = diagramList.get("A").getShapeList().get(t.getIndexOfShape()).getShape().toString();
    		}
    		System.out.println(shapeName + ", " + t.getTransformation() + ", Index: " + t.getIndexOfShape());
    	}
    	
    	return transformations;
    }
    
    private String determineFinalAnswer(HashMap<String, Diagram> diagramList, Diagram D) {
    	
    	// Compare D to all of the available solutions
    	String chosenAnswer = "";
    	int lowestCount = Integer.MAX_VALUE;
    	int transformationCount = 0;
    	
    	System.out.println("\nComparing D to all of the available answers");
    	
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
        	
    		if (transformationCount < lowestCount) {
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
            	
        		if (transformationCount < lowestCount) {
        			lowestCount = transformationCount;
        			chosenAnswer = figure;
        			System.out.println("Updating chosen answer to " + figure + ", with transformation count: " + transformationCount);
        		}    		
        	}
    	}
    	
    	return chosenAnswer;    	
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
    	
    	Diagram solution = new Diagram();
    	List<Shape> shapeList = new ArrayList<Shape>();
    	solution.setName("D");

    	// Delete the shapes that are removed
    	for (int i = 0; i < A.getShapeList().size(); i++) {
    		
    		Shape shape = A.getShapeList().get(i);
    		
    		for (Transformation t : transformations) {
	    		
    			if (t.getIndexOfShape() != i) continue;
    			
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
    		System.out.println("This shape is a " + shape.getShape());
    		
    		for (Transformation t : transformations) {
	    
    			System.out.println(t.getTransformation().toString());
    			
    			if (t.getIndexOfShape() != i) continue;
    			
    			// TODO: Mirroring overwrites things, might cause issues
    			
    			if (t.getTransformation() == Transformations.MIRRORING_XAXIS) {
    				
    				Shape mirroredXWise = mirrorOverXAxis(shape);
    				
    				Textures texture = shape.getTexture();
    				shape = new Shape();
    				buildShape(mirroredXWise.getShapeMatrix(), shape);
    				shape.setTexture(texture);
    			}
    			
    			if (t.getTransformation() == Transformations.MIRRORING_YAXIS) {
    			
    				Shape mirroredYWise = mirrorOverYAxis(shape);
    				
    				Textures texture = shape.getTexture();
    				shape = new Shape();
    				buildShape(mirroredYWise.getShapeMatrix(), shape);
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
	    			
	    			shape.setRotation(t.getRotation());
	    		}
	    		
	    		if (t.getTransformation() == Transformations.REGION) {
	    			shape.setRegion((shape.getRegion() + t.getRegionB()) % 4);
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
    	for (Shape shape : solution.getShapeList()) {
    		System.out.println("Shape: " + shape.getShape() + ",  Rotation: " + shape.getRotation() + ", is solid: " + shape.isSolid() + ", isHollow: " + shape.isHollow() + ", HalfFills: " + shape.getFills() + ", Region: " + shape.getRegion() + ", Height: " + shape.getHeight() + ", Width: " + shape.getWidth());    		
    	}

    	return solution;
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
			
			transformations.addAll(sameShapeTransform(0, d1.getShapeList().get(0), d2.getShapeList().get(0)));
		}
    	
    	System.out.println("DONE BUILDING TRANSFORMATIONS, returning list of size: " + transformations.size());
    	return transformations;    	
    }
    
    private List<Transformation> sameShapeTransform(int indexOfShape, Shape s1, Shape s2) {
    	
    	List<Transformation> transformations = new ArrayList<Transformation>();
    	boolean isMirroring = false;
    	
    	// Note: For whatever reason, Mirroring seems to be more important that rotation, so check mirroring first and
    	// then only check for rotation there isn't a mirroring transformation occurring 
    	
    	// Check if shapes are mirrors of each other, don't bother with squares/circles
    	if (s1.getShape() != Shapes.SQUARE && s1.getShape() != Shapes.CIRCLE && s1.getShape() != Shapes.PLUS) {
    		
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
	    		if (s1.getRegion() != s2.getRegion()) {
					transformations.add(new Transformation(indexOfShape, s1.getRegion(), s2.getRegion()));
				}
    		}
    		
    		if (s1.getFills() != s2.getFills()) {
    			transformations.add(new Transformation(indexOfShape, s1.getFills(), s2.getFills()));
    		}			
    	}
    	
    	
		if (s1.getTexture() != s2.getTexture()) {
			transformations.add(new Transformation(indexOfShape, s1.getTexture(), s2.getTexture()));
		}
		
		if (s1.getSize() != s2.getSize()) {
			transformations.add(new Transformation(indexOfShape, s1.getSize(), s2.getSize()));
		}
		
		return transformations;
    }
    
    private Shape rotateShape(Shape shape, int rotation) {
    	
    	if (rotation % 90 != 0) {
    		return null;
    	}
    	
    	int degreesLeft = rotation;
    	
    	boolean[][] matrix = shape.getShapeMatrix();
    	while (degreesLeft > 0) {
    		matrix = rotateMatrix90Degrees(matrix);
    		degreesLeft -= 90;
    	}
    	    
    	Shape newShape = new Shape();
    	newShape.setShapeMatrix(matrix);
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
    	
    	discoverShapeType(shape);
    	shape.setHeight(shape.getBottomMostPixel().getY() - shape.getTopMostPixel().getY());
    	shape.setWidth(shape.getRightMostPixel().getX() - shape.getLeftMostPixel().getX());
    	shape.setCenter(new Pixel(shape.getLeftMostPixel().getX() + shape.getWidth()/2, shape.getTopMostPixel().getY() + shape.getHeight()/2));
    	shape.setRegion(findRegion(shape));
    	determineShapeFill(shape);
    	discoverHalfFill(shape);
    	
    	System.out.println("Shape is solid: " + shape.isSolid() + ", isHollow: " + shape.isHollow() + ", Texture: " 
    			+ shape.getTexture().toString() + ",  Region: " + shape.getRegion() + ", HalfFill: " + shape.getFills() 
    			+ ", Rotation: " + shape.getRotation() + ", Height: " + shape.getHeight() + ", Width: " + shape.getWidth());
    	return shape;
    }
    
    // TODO: This is a hacky fix designed to help solve problem B-08 with half filled objects
    private void discoverHalfFill(Shape shape) {
    	
    	int count = countPixels(shape);
    	int leftCount = 0;
    	int rightCount = 0;
    	int topCount = 0;
    	int bottomCount = 0;
    	
    	int topRightCount = 0;
    	int topLeftCount = 0;
    	int bottomRightCount = 0;
    	int bottomLeftCount = 0;
    	  	
    	// if left full and right empty then left fill
    	for (int i = 0; i < 184; i++) {
    		for (int j = 0; j < 184; j++) {
    			if (shape.getShapeMatrix()[i][j]) {
    				
    				if (i < 92) leftCount++;
    				if (i >= 92) rightCount++;
    				if (j < 92) topCount++;
    				if (j >= 92) bottomCount++;
    				
    				if (i < 92 && j < 92) topLeftCount++;
    				if (i < 92 && j >= 92) bottomLeftCount++;
    				if (i >= 92 && j < 92) topRightCount++;
    				if (i >= 92 && j >= 92) bottomRightCount++;
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
    
    private int findRegion(Shape shape) {
    	
    	// 0 1
    	// 2 3
    	
    	int region = 0;
    	
    	if (shape.getCenter().getY() >= 92) {
    		region += 2;
    	} 
    	
    	if (shape.getCenter().getX() >= 92) {
    		region += 1;
    	}
    	
    	return region;
    }
    
    // Take in an ambiguos shape and determine if it is a known shape
    private Shape discoverShapeType(Shape shape) {
    	
    	// Square: TopMost and BottomMost have same X, leftMost and RightMost have same Y
		// And top right is neabled
		if (shape.getTopMostPixel().getX() == shape.getBottomMostPixel().getX() 
				&& shape.getTopMostPixel().getY() == shape.getRightMostPixel().getY() 
				&& shape.getShapeMatrix()[shape.getRightMostPixel().getX()][shape.getBottomMostPixel().getY()]) {
			
			System.out.println("Shape is SQUARE");
			shape.setShape(Shapes.SQUARE);
			return shape;
		}
		
		// CIRCLE, PLUS, and PACMAN sign
		if (compareVals(shape.getTopMostPixel().getX(), shape.getBottomMostPixel().getX()) 
				&& compareVals(shape.getLeftMostPixel().getY(), shape.getRightMostPixel().getY())  
				&& !shape.getShapeMatrix()[shape.getRightMostPixel().getX()][shape.getTopMostPixel().getY()] 
				&& !shape.getShapeMatrix()[shape.getRightMostPixel().getX()][shape.getBottomMostPixel().getY()] 
				&& !shape.getShapeMatrix()[shape.getLeftMostPixel().getX()][shape.getTopMostPixel().getY()] 
				&& !shape.getShapeMatrix()[shape.getLeftMostPixel().getX()][shape.getBottomMostPixel().getY()]) {
		
			boolean isPlusSign = true;
			boolean isPacMan = true;
			int rowValue = 0;
			int columnValue = 0;
			
			// Is there any pixel above the leftMost and to the left of TopMost?
			for (int row = 0; row < shape.getLeftMostPixel().getY() - 5; row++) {
				for (int column = 0; column < shape.getTopMostPixel().getX() - 5; column++) {
					if (shape.getShapeMatrix()[row][column]) {
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
				System.out.println("Shape is PLUS");
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
				
				return shape;
			} else  if (isPlusSign && !isMirroredPlus){
				System.out.println("Shape is PACMAN with Zero rotation");
				shape.setShape(Shapes.PACMAN);
				return shape;
			}
			
			if (rowValue != 0 && columnValue != 0) {
								
				boolean foundTopRight = false;
				boolean foundBottomRight = false;
				boolean foundBottomLeft = false;
				int rowStart = 0;
				int colStart = 0;
				
				// Look in the top right region		
				rowStart = rowValue;
				colStart = 184 - columnValue;
				
				for (int i = 0; i < 10; i++) {
					if (shape.getShapeMatrix()[colStart][rowStart + i]) {
						foundTopRight = true;
					}
				}
		
				// Look at bottom left				
				rowStart = 184 - rowValue;
				colStart = columnValue;
				
				for (int i = 0; i < 10; i++) {
					if (shape.getShapeMatrix()[colStart][rowStart - i]) {
						foundBottomLeft = true;
					}
				}
				
				// Look at bottom right
				rowStart = 184 - rowValue;
				colStart = 184 - columnValue;
				
				for (int i = 0; i < 10; i++) {
					if (shape.getShapeMatrix()[colStart][rowStart - i]) {
						foundBottomRight = true;
					}
				}
							
				if (foundTopRight && foundBottomRight && !foundBottomLeft) {
					System.out.println("Shape is PACMAN with 90 rotation");
					shape.setShape(Shapes.PACMAN);
					shape.setRotation(90);
					return shape;
				}
				
				if (foundTopRight && !foundBottomRight && foundBottomLeft) {
					System.out.println("Shape is PACMAN with 180 rotation");
					shape.setShape(Shapes.PACMAN);
					shape.setRotation(180);
					return shape;
				}
				
				if (!foundTopRight && foundBottomRight && foundBottomLeft) {
					System.out.println("Shape is PACMAN with 270 rotation");
					shape.setShape(Shapes.PACMAN);
					shape.setRotation(270);
					return shape;
				}
			}
			
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
				return shape;
			}
			
			// Else it is a Circle
			System.out.println("Shape is CIRCLE");
			shape.setShape(Shapes.CIRCLE);
			return shape;
		}
			
		//   x This is the default right triangle with rotation of zero
		//  xx
		// xxx
		if (compareVals(shape.getTopMostPixel().getX(), shape.getRightMostPixel().getX()) 
				&& compareVals(shape.getLeftMostPixel().getY(), shape.getBottomMostPixel().getY()) 
				&& !shape.getShapeMatrix()[shape.getLeftMostPixel().getX()][shape.getTopMostPixel().getY()]) {
			
			System.out.println("Shape is Right triangle with zero rotation");
			shape.setShape(Shapes.RIGHT_TRIANGLE);
			return shape;
		}     
		
		// xxx
		//  xx
		//   x
		// And top right is enabled
		if (compareVals(shape.getRightMostPixel().getY(), shape.getLeftMostPixel().getY()) 
				&& compareVals(shape.getRightMostPixel().getX(), shape.getBottomMostPixel().getX())
				&& !shape.getShapeMatrix()[shape.getLeftMostPixel().getX()][shape.getBottomMostPixel().getY()]) {
			
			System.out.println("Shape is Right triangle with 90 degree rotation");
			shape.setShape(Shapes.RIGHT_TRIANGLE);
			shape.setRotation(90);
			return shape;
		}     
		
		// xxx
		// xx
		// x
		// And top right is enabled
		if (compareVals(shape.getTopMostPixel().getX(), shape.getBottomMostPixel().getX()) 
				&& compareVals(shape.getRightMostPixel().getY(), shape.getTopMostPixel().getY())
				&& !shape.getShapeMatrix()[shape.getRightMostPixel().getX()][shape.getBottomMostPixel().getY()]) {
			
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
				&& !shape.getShapeMatrix()[shape.getRightMostPixel().getX()][shape.getTopMostPixel().getY()]) {
			
			System.out.println("Shape is Right triangle with 270 degree rotation");
			shape.setShape(Shapes.RIGHT_TRIANGLE);
			shape.setRotation(270);
			return shape;
		}       				
	
		// Heart
		// TODO: this only works for 0 rotation Heart
		if ((shape.getTopMostPixel().getX() < shape.getBottomMostPixel().getX() - 10) 
				&& compareVals(shape.getRightMostPixel().getY(), shape.getLeftMostPixel().getY())) {
			
 
			int count = 0;
			for (int j = 0; j < 184; j++) {
				if (shape.getShapeMatrix()[j][shape.getBottomMostPixel().getY()]) {
					count++;
				}
			}
				
			System.out.println("SPECIALCOUNT: " + count);
			
			// If it is a diamond then it will only have a couple pixels in it's top row
			if (count < 5) {
				System.out.println("Shape is HEART");
				shape.setShape(Shapes.HEART);
				return shape;
			}
		}
    	
		
		
		
		// TODO: Add logic for basic rectangle
		
		//   x
		//  xxx
		// xxxxx
		if (compareVals(shape.getBottomMostPixel().getX(), shape.getLeftMostPixel().getX()) 
				&& compareVals(shape.getRightMostPixel().getY(), shape.getBottomMostPixel().getY()) 
				&& !shape.getShapeMatrix()[shape.getLeftMostPixel().getX()][shape.getTopMostPixel().getY()]
				&& !shape.getShapeMatrix()[shape.getRightMostPixel().getX()][shape.getTopMostPixel().getY()]) {
			
			System.out.println("Shape is Equalatiral Triangle");
			shape.setShape(Shapes.TRIANGLE);
			return shape;
		}
    	
		// TODO: Logic for other shapes, Heart, Star, 
		
		// TODO: Is the shape hollow or solid or striped?
		
		System.out.println("Unknown Shape");
		shape.setShape(Shapes.UNKNOWN);
    	return shape;
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
    	    	
    	if (shape.getShape() == Shapes.SQUARE) {    		
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
    		expectedPixelCount = (int) (shape.getWidth() * shape.getWidth() * .5);
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
    	
    	// triangle = length*width / 2
    	boolean isSolid = pixelCount >= expectedPixelCount - areaDelta; 
    	//System.out.println("PixelCount: " + pixelCount + ", expectedPixelCount: " + expectedPixelCount);
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
    

    private boolean isIdenticalMatch(Shape s1, Shape s2) {
		
    	// TODO: This should be more flexible
    	
    	int differenceCount = 0;
    	
		for (int i = 0; i < 184; i++) {
			for (int j = 0; j < 184; j++) {
				if (s1.getShapeMatrix()[i][j] != s2.getShapeMatrix()[i][j]) {
					differenceCount++;
				}
			}
		}    	
		System.out.println("Difference Count: " + differenceCount);
		
		if (differenceCount > 0) {
			
			System.out.println("Try to shift");
			
			differenceCount = 0;
			
			boolean[][] shift = new boolean[184][184];
			
			for (int i = 0; i < 184; i++) {
				for (int j = 1; j < 184; j++) {
					if (s1.getShapeMatrix()[i][j]) {
						shift[i][j - 1] = s1.getShapeMatrix()[i][j];						
					}
				}
			}
			
			for (int i = 0; i < 184; i++) {
				for (int j = 0; j < 184; j++) {
					if (shift[i][j] != s2.getShapeMatrix()[i][j]) {
						differenceCount++;
					}
				}
			}    	
			
			System.out.println("New Difference count: " + differenceCount);
			
		}
		
		return (differenceCount == 0);		
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
    
    private static boolean compareVals(int a, int b) {
    	if ( (a == b) || (Math.abs(a - b) <= DELTA)) {
    		return true;
    	}
    	return false;
    }
}
