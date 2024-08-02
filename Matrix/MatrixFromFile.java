// Import for Buffered Reader and related io classes
import java.io.*;

/*
This replit demonstrates how to read values from a file
and use them to configure parameters within the program
The advantage of this is it allows you to change values
without needing to re-compile.

*********** YOUR TASK *************
The file as currently written only gets two attribute values
from the file.  Study what I already shared with you and use it
as a pattern to get the rest of the values, then use them
to print out a matrix based on the values in the file

A few Things to know:

1) A File Object can be created with a file name String

2) A BufferedReader Object can be created from the file object
and allows to read one line of text from the file at a time as
a String.
EX:  String line = input.readLine();
(where input is a BufferedReader)

3) The String split() method is useful to break a single
line String into multiple 'tokens' that represent
property names or values.

4) Some properties will need to be converted to their data
type using Wrapper class methods:
EX:   Integer.parseInt("5") --> 5
      Boolean.parseBoolean("false") --> false

5) Once all instance values have been initialized, use
these values within the printMatrix() method to print
the 2D array according to specification.
If randomElements is set to false, all elements can
be set to zero and randVal properties can be ignored




If the Values in the Properties File look like this:
_________________
label = 2D Array Random Two Digit Nums
numRows = 10
numCols = 15
randomElements = true
minRandVal = 10
maxRandVal = 99
_______________________

The output schould look something like this:

2D Array Random Two Digit Nums
86 48 95 80 59 47 44 67 75 93 53 44 49 26 38
24 46 26 77 20 12 43 15 59 65 73 81 16 69 38
37 85 51 73 20 71 27 77 71 22 15 84 90 17 41
80 71 39 46 36 17 69 50 59 44 45 17 46 40 30
47 73 96 42 15 50 35 10 52 79 93 91 88 53 82
33 64 11 34 70 68 45 13 45 10 52 35 31 13 50
52 85 89 24 52 25 89 98 46 91 46 77 90 55 28
19 47 61 80 17 17 74 83 59 79 94 54 46 95 95
68 97 92 58 46 24 69 17 77 40 44 13 30 73 40
91 31 38 91 86 46 43 92 54 95 74 45 81 68 67


If the Values in the Properties File look like this:
_________________
label = 2D Array Zero Values
numRows = 10
numCols = 15
randomElements = false
minRandVal = 10
maxRandVal = 99
_______________________

The output schould look something like this:

2D Array Zero Values
0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
0 0 0 0 0 0 0 0 0 0 0 0 0 0 0

NOTE:  If random elements set to false, the values
for minRandVal and maxRandVal will be ignored
instead, all elements set to zero
*/

public class MatrixFromFile{

  private String label;
  private int numRows;
  private int numCols;
  private boolean randomElements;
  private int minRandVal;
  private int maxRandVal;

	public MatrixFromFile(String fileName){

		  loadPropertiesFromFile(fileName);

	} // end main


  public void loadPropertiesFromFile(String fileName){
    File name = new File(fileName);  // File should be in same directory

	try
		{   //This object does the reading
			BufferedReader input = new BufferedReader(new FileReader(fileName)); //create file reader

      System.out.println("Here");
      String[] propertyNames = {"label","numRows","numCols","randomElements","minRandVal","maxRandVal"}; // you need to add to this
      String[] valuesAsStrings = new String[propertyNames.length]; //hold values assosciated with fields
      for (int i = 0; i < propertyNames.length; i++){
  			String line = input.readLine(); // get a single line
        String[] tokens = line.split("="); // tokens[0] == prop. name, tokens[1] == prop. value as String
        if (tokens[0].trim().equals(propertyNames[i])) // Note trim is used to remove unwanted spaces
            valuesAsStrings[i] = tokens[1].trim();
        else
          System.out.println("Property Name "+propertyNames[i]+" not found");

      }
      label = valuesAsStrings[0];   // label is a String, so set value
      numRows = Integer.parseInt(valuesAsStrings[1]);  //convert to int to store field
      numCols = Integer.parseInt(valuesAsStrings[2]);
      randomElements = Boolean.parseBoolean(valuesAsStrings[3]);
      minRandVal = Integer.parseInt(valuesAsStrings[4]);
      maxRandVal = Integer.parseInt(valuesAsStrings[5]);
		}
		catch (IOException io) // catch errors
		{
			System.err.println("Exception =>"+io.getMessage());
		}


  }
  public void printMatrix(){
     System.out.println("\n"+label);
     for(int r = 0; r < numRows; r++)
     {
		 for(int c = 0; c < numCols;c++)
		 if(randomElements)
		 {
		 	int rand = (int)(Math.random()*(maxRandVal - minRandVal+1))+minRandVal;
		 	System.out.print(rand+" ");
		}
		else
		{
		System.out.print("0");
	}
   }
   	System.out.println();
  }

  public static void main(String[] args) {
    MatrixFromFile mff = new MatrixFromFile("ArrayProperties.txt");
    mff.printMatrix();
  }

}