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
    	
    	
    	
    }
    
    public class Shape {
    	
    	boolean isHollow;
    	boolean isSolid;
    	boolean isStriped;
    	int height = 0;
    	int width = 0;
            	
    	Pixel topMostPixel = new Pixel(0, 0);
    	Pixel bottomMostPixel = new Pixel(0, 183);
    	Pixel leftMostPixel = new Pixel(183, 0);
    	Pixel rightMostPixel = new Pixel(0, 0);
    	
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
    	if (!problem.getName().equals("Basic Problem B-01")) return -1;
    	System.out.println("Name: " + problem.getName() + ", Type: " + problem.getProblemType());
    	
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
        	for(int i = 0 ; i < figureAImage.getWidth() ; i++) {
        		for(int j = 0 ; j < figureAImage.getHeight() ; j++) {
        		
        			int thisPixel = figureAImage.getRGB(i,j);        		
        			if (thisPixel != -1) {
        				diagram.getMatrix()[i][j] = true;
        				
//        				if (j <= diagram.getBottomMostPixel().getY()) diagram.setBottomMostPixel(new Pixel(i, j));
//        				if (j >= diagram.getTopMostPixel().getY()) diagram.setTopMostPixel(new Pixel(i, j));
//        				if (i <= diagram.getLeftMostPixel().getX()) diagram.setLeftMostPixel(new Pixel(i, j));
//        				if (i >= diagram.getRightMostPixel().getX()) diagram.setRightMostPixel(new Pixel(i, j));
        				        				
        			}		
        		}	
        	}
        	
//        	diagram.setHeight(diagram.getTopMostPixel().getY() - diagram.getBottomMostPixel().getY());
//        	diagram.setWidth(diagram.getRightMostPixel().getX() - diagram.getLeftMostPixel().getX());
        	
        	System.out.println("Adding diagram: " + diagram.getName());
    		diagramList.put(diagram.getName(), diagram);
    	}
    	    	   	
    	// Build up list of transformations 
    	List<Transformation> transformations = new ArrayList<Transformation>();
    	transformations.addAll(buildTransformations(diagramList.get("A"), diagramList.get("B")));
    	transformations.addAll(buildTransformations(diagramList.get("A"), diagramList.get("C")));
    	    	   	
    	// Use transformations on A to generate D by applying all transformations to A
    	Diagram D = diagramList.get("A");
    	
    	String chosenAnswer = "";
    	int lowestCount = Integer.MAX_VALUE;
    	
    	for (String figure : Arrays.asList("1", "2", "3", "4", "5", "6")) {
    		int transformationCount = compare(D, diagramList.get("1"));
    		
    		if (transformationCount < lowestCount) {
    			lowestCount = transformationCount;
    			chosenAnswer = figure;
    		}    		
    	}
    	
    	return Integer.parseInt(chosenAnswer);    	
    }
    
    // Build the transformations that occur between two diagrams
    private static List<Transformation> buildTransformations(Diagram d1, Diagram d2) {
    	
    	List<Transformation> transformations = new ArrayList<Transformation>();
    	
    	if (d1.isIdenticalMatch(d2)) return transformations;
    	
    	return transformations;    	
    }
    
    // Compare two diagrams to see how similar they are
    private static int compare(Diagram d1, Diagram d2) {
    	
    	int transformationCount = 0;
    	
    	if (d1.isIdenticalMatch(d2)) return transformationCount;
    	
    	return transformationCount;
    }
}
