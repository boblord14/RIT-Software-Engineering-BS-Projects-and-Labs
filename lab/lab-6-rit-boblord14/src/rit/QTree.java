package rit;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * This class represents the Quadtree data structure used to compress raw
 * grayscale images and uncompress back.  Conceptually, the tree is
 * a collection of rit.QTNode's.  A rit.QTNode either holds a grayscale image
 * value (0-255), or QUAD_SPLIT, meaning the node is split into four
 * sub-nodes that are equally sized sub-regions that divide up the
 * current space.
 * <p>
 * To learn more about quadtrees:
 * https://en.wikipedia.org/wiki/Quadtree
 *
 * @author RIT CS
 * @author Ethan Patterson
 */
public class QTree {
    /**
     * the value of a node that indicates it is spplit into 4 sub-regions
     */
    public final static int QUAD_SPLIT = -1;

    /**
     * the root node in the tree
     */
    private QTNode root;

    /**
     * the square dimension of the tree
     */
    private int DIM;

    /**
     * the raw image
     */
    private int image[][];

    /**
     * the size of the raw image
     */
    private int rawSize;

    /**
     * the size of the compressed image
     */
    private int compressedSize;

    /**
     * Create an initially empty tree.
     */
    public QTree() {
        this.root = null;
        this.DIM = 0;
        this.image = null;
        this.rawSize = 0;
        this.compressedSize = 0;
    }

    /**
     * Get the images square dimension.
     *
     * @return the square dimension
     */
    public int getDim() {
        return this.DIM;
    }

    /**
     * Get the raw image.
     *
     * @return the raw image
     */
    public int[][] getImage() {
        return this.image;
    }

    /**
     * Get the size of the raw image.
     *
     * @return raw image size
     */
    public int getRawSize() {
        return this.rawSize;
    }

    /**
     * Get the size of the compressed image.
     *
     * @return compressed image size
     */
    public int getCompressedSize() {
        return this.compressedSize;
    }

    /**
     * Write the uncompressed image to the output file.  This routine is meant to be
     * called from a client after it has been uncompressed.
     *
     * This takes the data(2d array), converts it to a string, uses .replace to get rid of any remaining spaces, [, and ] in it,
     * splits it, puts it in an array, then puts it in an arraylist. From here it can just be slapped in the writer.
     *
     * NOTE: You'll have to close the rendered image application window to write the .raw file to the necessary directory.
     * I think the starter code was just written this way.
     *
     * @param outFile the name of the file to write the uncompressed image to
     * @throws IOException any errors involved with writing the file out
     * @throws QTException if the file has not been uncompressed yet
     * @rit.pre client has called uncompress() to uncompress the input file
     */
    public void writeUncompressed(String outFile) throws IOException, QTException {
        String data = Arrays.deepToString(image);
        data = data.replace("[","");
        data = data.replace("]","");
        data = data.replace(" ","");
        String[] values = data.split(",");
        FileWriter writer = new FileWriter(outFile, StandardCharsets.UTF_8);
        BufferedWriter buffer = new BufferedWriter(writer);
        for (int i=0;i<values.length;i++){
            buffer.write(values[i] + "\n");
        }
        buffer.close();
        writer.close();

    }

    /**
     * A private helper routine for parsing the compressed image into
     * a tree of nodes.  When parsing through the values, there are
     * two cases:
     * <p>
     * 1. The value is a grayscale color (0-255).  In this case
     * return a node containing the value.
     * <p>
     * 2. The value is QUAD_SPLIT.  The node must be split into
     * four sub-regions.  Each sub-region is attained by recursively
     * calling this routine.  A node containing these four sub-regions
     * is returned.
     *
     *
     * just parses the list, if the first value on it is -1, does case 2 above, and if the value isnt, does case 1.
     * as it checks each value(first on the list), it gets removed after being used, so the first value on the list is
     * always unchecked each time the parse function goes around.
     *
     * @param values the values in the compressed image
     * @return a node that encapsulates this portion of the compressed
     * image
     * @throws QTException if there are not enough values in the
     *                     compressed image
     */
    private QTNode parse(List<Integer> values) throws QTException {
        if (values.get(0)==(-1)){
            values.remove(0);
            QTNode node = new QTNode(-1, parse(values), parse(values), parse(values), parse(values));
            return node;
        } else{
            QTNode node = new QTNode(values.get(0));
            values.remove(0);
            return node;
        }
    }

    /**
     * This is the core routine for uncompressing an image stored in a tree
     * into its raw image (a 2-D array of grayscale values (0-255).
     * It is called by the public uncompress routine.
     * The main idea is that we are working with a tree whose root represents the
     * entire 2^n x 2^n image.  There are two cases:
     * <p>
     * 1. The node is not split.  We can write out the corresponding
     * "block" of values into the raw image array based on the size
     * of the region
     * <p>
     * 2. The node is split.  We must recursively call ourselves with the
     * the four sub-regions.  Take note of the pattern for representing the
     * starting coordinate of the four sub-regions of a 4x4 grid:
     * - upper left: (0, 0)
     * - upper right: (0, 1)
     * - lower left: (1, 0)
     * - lower right: (1, 1)
     * We can generalize this pattern by computing the offset and adding
     * it to the starting row and column in the appropriate places
     * (there is a 1).
     *
     * this breaks down the compressed version by checking if the node value is -1, and if so recursing to split into
     * the 4 nodes it therefore composes of. if its not -1, it means the entire section(determined by size) must be the
     * same color so then double for loops populate that section of the 2d array with whatever value it is.
     *
     * square root and some math things are used to split up the square into the quadrants and pull the starting coords
     *
     * @param node  the node to uncompress
     * @param size  the size of the square region this node represents
     * @param start the starting coordinate this row represents in the image
     */
    private void uncompress(QTNode node, int size, Coordinate start) {
        int len = (int) Math.sqrt(size);
        if (node.getVal() != (-1)){
            for(int i=0; i<len; i++){
                for (int j=0; j<len; j++){
                    image[start.getRow() + i][start.getCol() + j] = node.getVal();
                }
            }
        } else {
            uncompress(node.getUpperLeft(), (size/4), start);
            uncompress(node.getUpperRight(), (size/4), new Coordinate(start.getRow(), ((int)(Math.sqrt(size)/2) + start.getCol())));
            uncompress(node.getLowerLeft(), (size/4), new Coordinate((start.getRow() + (int)(Math.sqrt(size)/2)), start.getCol()));
            uncompress(node.getLowerRight(), (size/4), new Coordinate((start.getRow() + (int)(Math.sqrt(size)/2)), ((int)(Math.sqrt(size)/2) + start.getCol())));
        }
    }

    /**
     * Uncompress a RIT compressed file.  This is the public facing routine
     * meant to be used by a client to uncompress an image for displaying.
     * <p>
     * The file is expected to be 2^n x 2^n pixels.  The first line in
     * the file is its size (number of values).  The remaining lines are
     * the values in the compressed image, one per line, of "size" lines.
     * <p>
     * Once this routine completes, the raw image of grayscale values (0-255)
     * is stored internally and can be retrieved by the client using getImage().
     *
     * Reads in the file with a scanner, putting the values into an arraylist of strings called data.
     * the size(DIM) of the square is just square root of the size of the first data value here as thats the one which
     * handles the sizing. Afterwards that value is just chucked out of the arraylist as it would just get in the way of
     * the next part.
     *
     * After that it creates the 2d array image which is the storage "representation" of how the shape looks. Size of
     * both arrays is just DIM for each. After that the main quadtree is created via parse followed by using the
     * uncompress method on it, DIM, and the starting coordinates(always 0,0)
     *
     * @param filename the name of the compressed file
     * @throws IOException if there are issues working with the compressed file
     * @throws QTException if there are issues parsing the data in the file
     */
    public void uncompress(String filename) throws IOException, QTException {
        ArrayList<Integer> data = new ArrayList<Integer>();
        Scanner in = new Scanner(new File(filename));
        while (in.hasNextLine()) {
            String line = in.nextLine();
            data.add(Integer.parseInt(line));
        }
        this.DIM = (int) Math.sqrt(data.get(0));
        data.remove(0);
        this.image = new int[DIM][DIM];
        this.root = parse(data);
        uncompress(root,(DIM*DIM), new Coordinate(0,0));
    }

    /**
     * The private writer is a recursive helper routine that writes out the
     * compressed image.  It goes through the tree in preorder fashion
     * writing out the values of each node as they are encountered.
     *
     * To write out the compressed data, first the value of the current node is printed in the file, and then if the
     * value of said node is -1, it recurses over the 4 subnodes and does it for them. As each line is written the
     * compressed size is incremented by 1, and the writer is flushed. No clue if the flush does anything, the program
     * works fine as is and I dont want to retest everything if I make changes now.
     *
     * @param node   the current node in the tree
     * @param writer the writer to write the node data out to
     * @throws IOException if there are issues with the writer
     */
    private void writeCompressed(QTNode node, BufferedWriter writer) throws IOException {
        writer.write("\n" + String.valueOf(node.getVal()));
        this.compressedSize+=1;
        writer.flush();
        if (node.getVal() == (-1)){
            writeCompressed(node.getUpperLeft(), writer);
            writeCompressed(node.getUpperRight(), writer);
            writeCompressed(node.getLowerLeft(), writer);
            writeCompressed(node.getLowerRight(), writer);
        }
    }

    /**
     * Write the compressed image to the output file.  This routine is meant to be
     * called from a client after it has been compressed
     *
     * Just the framework for the private writecompressed. Creates the writer and the buffer, inserts the first line
     * into the file(the sizing one used to decompress it) and increments compressedSize by 1 for it.
     * After that the other writeCompressed method is called and that handles the rest. Then just both buffers get closed.
     * Idk if that does anything though.
     *
     * @param outFile the name of the file to write the compressed image to
     * @throws IOException any errors involved with writing the file out
     * @throws QTException if the file has not been compressed yet
     * @rit.pre client has called compress() to compress the input file
     */
    public void writeCompressed(String outFile) throws IOException, QTException {
        FileWriter writer = new FileWriter(outFile, StandardCharsets.UTF_8);
        BufferedWriter buffer = new BufferedWriter(writer);
        buffer.write(String.valueOf((int)Math.pow(this.DIM, 2)));
        this.compressedSize+=1;
        writeCompressed(root, buffer);
        buffer.close();
        writer.close();
    }

    /**
     * Check to see whether a region in the raw image contains the same value.
     * This routine is used by the private compress routine so that it can
     * construct the nodes in the tree.
     *
     * This one uses a cool trick I found online a while back. Using a hashset as it can't have duplicate values, the
     * color values of everything from the designated segment in image being tested are added to the HashSet uniqueVals.
     * At the end, if the size of uniqueVals is 1, that means there was no more than 1 unique value, as hashsets don't
     * store duplicate values, only unique ones.
     *
     * Then just a quick return of the test of the size being equal to 1.
     *
     * @param start the starting coordinate in the region
     * @param size  the size of the region
     * @return whether the region can be compressed or not
     */
    private boolean canCompressBlock(Coordinate start, int size) {
        Set<Integer> uniqueVals = new HashSet<>();
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                uniqueVals.add(image[start.getRow()+i][start.getCol()+j]);
            }
        }
        return (uniqueVals.size()==1);
    }

    /**
     * This is the core compression routine.  Its job is to work over a region
     * of the image and compress it.  It is a recursive routine with two cases:
     * <p>
     * 1. The entire region represented by this image has the same value, or
     * we are down to one pixel.  In either case, we can now create a node
     * that represents this.
     * <p>
     * 2. If we can't compress at this level, we need to divide into 4
     * equally sized sub-regions and call ourselves again.  Just like with
     * uncompressing, we can compute the starting point of the four sub-regions
     * by using the starting point and size of the full region.
     *
     * This uses the canCompressBlock from up above, with the square root of the size as the size passed here is the
     * total image size. If it can be compressed as is, this just returns a QTNode with the value as is in the image,
     * and no branches on it.
     * If not, a new QTNode with a value of -1 is made, and then recurses over the 4 subsections of this part of the image,
     * using the same sqrt maths as mentioned earlier to get proper coordiantes for them.
     *
     * @param start the start coordinate for this region
     * @param size  the size this region represents
     * @return a node containing the compression information for the region
     */
    private QTNode compress(Coordinate start, int size) {
        if (canCompressBlock(start, (int) Math.sqrt(size))){
            return new QTNode(image[start.getRow()][start.getCol()]);
        }else {
            return new QTNode(-1, compress(start, (size/4)),
                    compress(new Coordinate(start.getRow(), ((int)(Math.sqrt(size)/2) + start.getCol())), (size/4)),
                    compress(new Coordinate((start.getRow() + (int)(Math.sqrt(size)/2)), start.getCol()), (size/4)),
                    compress(new Coordinate((start.getRow() + (int)(Math.sqrt(size)/2)), ((int)(Math.sqrt(size)/2) + start.getCol())), (size/4)));
        }
    }

    /**
     * Compress a raw image into the RIT format.  This routine is meant to be
     * called by a client.  It is expected to be passed a file which represents
     * the raw image.  It is ASCII formatted and contains a series of grayscale
     * values (0-255).  There is one value per line, and 2^n x 2^n total lines.
     *
     * Reads in the file, adding the data to the arraylist of ints called data.
     * The rawSize is equivalent to the size of data, DIM is the square root of the size, and the image 2d array is made
     * with DIM for both array sizes again.  Image is populated by double for loops, using a bit of math to go along
     * data and populate the appropriate slots(i, j) in image.
     *
     * After that the root is created with the compress method.
     *
     * @param inputFile the raw image file name
     * @throws IOException if there are issues working with the file
     */
    public void compress(String inputFile) throws IOException {
        Scanner in = new Scanner(new File(inputFile));
        ArrayList<Integer> data = new ArrayList<Integer>();
        while (in.hasNextLine()) {
            String line = in.nextLine();
            data.add(Integer.parseInt(line));
        }
        this.rawSize = data.size();
        this.DIM = (int) Math.sqrt(data.size());
        this.image = new int[DIM][DIM];
        for (int i=0; i<this.DIM; i++){
            for(int j=0; j<this.DIM; j++){
                this.image[i][j] = data.get((DIM*i)+j);
            }
        }
        this.root = compress(new Coordinate(0,0), (DIM*DIM));
    }

    /**
     * A preorder (parent, left, right) traversal of a node.  It returns
     * a string which is empty if the node is null.  Otherwise
     * it returns a string that concatenates the current node's value
     * with the values of the 4 sub-regions (with spaces between).
     * This is a recursive process starting with the root and is similar
     * to how parsing works.
     *
     * Just checks if the node doesnt equal -1, and if it doesnt, returns the raw value. If it does, it means the node
     * has children and returns the parent followed by the UL, UR, LL, and LR children respectively through recursion.
     *
     * @param node the node being traversed on
     * @return the string of the node
     */
    private String preorder(QTNode node) {
        if (node.getVal() != (-1)){
            return String.valueOf(node.getVal());
        }
        return (String.valueOf(node.getVal()) + " " + preorder(node.getUpperLeft()) + " " + preorder(node.getUpperRight()) + " " + preorder(node.getLowerLeft()) + " " + preorder(node.getLowerRight()));
    }

    /**
     * Returns a string which is a preorder traversal of the tree.
     *
     * Checks if root is null, because intellij sucks and won't debug properly with a breakpoint while root is null, as
     * it uses toString for the name of the object in the debug screen and preordering doesn't work if the object is null.
     *
     * Otherwise, if there's actual values in root, it returns the QTree's values via preordering
     *
     * @return the qtree string representation
     */
    @Override
    public String toString() {
        if (this.root == null){
            return "Empty QTree";
        }
        return "QTree: " + preorder(this.root);
    }
}