/*
  CPSC380 Operating Systems
  Project 1
  Jin Jung
  10/22/19

  SudokuCheck class for implementing necessary methods to read in data, analyze, modify, and output the correct solution
*/

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SudokuCheck {
  //declare class variables
  public static int[][] matrix; //this matrix will be edited to the correct solution
  public static int[][] originalMatrix; //to display original matrix at the end
  private String data; //var to store file readin data
  private String filename; //path and/or name of test file
  public int[][] rowdup;
  public int[][] rowmv;
  public int[][] columndup;
  public int[][] columnmv;
  public boolean[] hasrowmv;
  public boolean[] hascolumnmv;
  public boolean[] goodSubGrids;

  //main constructor
  SudokuCheck(String filename){
    this.filename = filename;
    this.data = this.fileread(filename);
    this.matrix = this.initializeMatrix(this.data);
    this.originalMatrix = this.initializeMatrix(this.data);
    this.rowdup = new int[9][9];
    this.rowmv = new int[9][9];
    this.columndup = new int[9][9];
    this.columnmv = new int[9][9];
    this.hasrowmv = new boolean[9];
    this.hascolumnmv = new boolean[9];
    this.goodSubGrids = new boolean[9];
    for (int i = 0; i < 9; ++i) {
      this.goodSubGrids[i] = false;
      this.hasrowmv[i] = false;
      this.hascolumnmv[i] = false;
      for(int j = 0; j < 9; ++j) {
        this.rowdup[i][j] = 0;
        this.rowmv[i][j] = 0;
        this.columndup[i][j] = 0;
        this.columnmv[i][j] = 0;
      }
    }
  }
  //empty constructor is never used
  SudokuCheck()
  {}
  //copy constructor
  SudokuCheck(SudokuCheck inputData) throws Exception
  {
    if(inputData == null)
    {
      System.out.println("Error!");
      System.exit(0);
    }
    this.filename = inputData.getFileName();
    this.data = inputData.getData();
    this.matrix = inputData.initializeMatrix(inputData.getFileName());
    this.rowdup = inputData.rowdup;
    this.rowmv = inputData.rowmv;
    this.columndup = inputData.columndup;
    this.columnmv = inputData.columnmv;
  }
  //clone
  public SudokuCheck Clone() throws Exception {
    return new SudokuCheck(this);
  }
  //setget int[][] matrix
  public void setMatrix (String data) {
      this.matrix = this.initializeMatrix(data);
  }
  public int[][] getMatrix() {
    return this.matrix;
  }
  public void setFileName(String filename) {
    this.filename = filename;
  }
  public String getFileName() {
    return this.filename;
  }
  public void setData(String data) {
    this.data = data;
  }
  public String getData()
  {
    return this.data;
  }
/*
check grids 0 through 8 where grid[0] {i:0-2,j:0-2} grid[1] {i:0-2,j:3-5} grid[2] {i:0-2,j:6-8}...
checkSubGrid() will be the only thread to actually modify the matrix to the correct solution by calling the findSolution() method
*/
public void checkSubGrid() {
  ArrayList<Integer> testarr = new ArrayList<Integer>();
  //check all 9 subgrids
  for (int w = 0; w < 9; ++w) {
    testarr.clear();
    for (int z = 0; z < 9; ++z) {
      testarr.add(z+1);
    }
    if (w < 3) {
      for (int i = 0; i < 3; ++i) {
        for (int j = (3*w); j < 3+(3*w); ++j) {
          if (testarr.contains(this.matrix[i][j])) {
            testarr.remove((Integer)this.matrix[i][j]);
          }
        }
      }
      if (testarr.isEmpty()) {
        this.goodSubGrids[w] = true;
      }
    }
    else if (w < 6) {
      for (int i = 3; i < 6; ++i) {
        for (int j = (3*(w-3)); j < (3+(3*(w-3))); ++j) {
          if (testarr.contains(this.matrix[i][j])) {
            testarr.remove((Integer)this.matrix[i][j]);
          }
        }
      }
      if (testarr.isEmpty()) {
        this.goodSubGrids[w] = true;
      }
    }
    else {
      for (int i = 6; i < 9; ++i) {
        for (int j = (3*(w-6)); j < (3+(3*(w-6))); ++j) {
          if (testarr.contains(this.matrix[i][j])) {
            testarr.remove((Integer)this.matrix[i][j]);
          }
        }
      }
      if (testarr.isEmpty()) {
        this.goodSubGrids[w] = true;
      }
    }
  }
  this.findSolution();
}
//check rows for duplicates
public void checkRow() {
  for (int i = 0; i < 9; ++i) { //for each row
    for (int j = 0; j < 8; ++j) { //for each column
      int pivot = this.matrix[i][j];
      for (int k = j+1; k < 9; ++k) {
        if (this.matrix[i][k] == pivot) {
          if (this.hasrowmv[i] == false) {
            this.hasrowmv[i] = true;
          }
          this.rowdup[i][k] = this.matrix[i][k];
          this.rowdup[i][j] = this.matrix[i][j];
        }
      }
      //check rows for missing values
      ArrayList<Integer> testarr = new ArrayList<Integer>();
      int z;
      int v = 0;
      for(int w = 0; w < 9; ++w ) {
        testarr.clear();
        for (z = 0; z < 9; ++z) {
          testarr.add(z+1);
        }
        if (hasrowmv[w] == true) {
          for (int ii = 0; ii < 9; ++ii) {
            if (testarr.contains(this.matrix[w][ii])) {
              testarr.remove((Integer)this.matrix[w][ii]);
            }
          }
          while(!testarr.isEmpty()) {
            this.rowmv[w][v] = testarr.get(0);
            v++;
            testarr.remove(0);
          }
        }
      }
    }
  }
}
//check columns for duplicate values
public void checkColumn() {
  for (int j = 0; j < 9; ++j) { //for each column
    for (int i = 0; i < 8; ++i) { //for each row
      int pivot = this.matrix[i][j];
      for (int k = i+1; k < 9; ++k) {
        if (this.matrix[k][j] == pivot) {
          if (this.hascolumnmv[j] == false) {
            this.hascolumnmv[j] = true;
          }
          this.columndup[k][j] = this.matrix[k][j];
          this.columndup[i][j] = this.matrix[i][j];
        }
      }
      //check columns for missing values
      ArrayList<Integer> testarr = new ArrayList<Integer>();
      int z;
      int v;
      for(int w = 0; w < 9; ++w ) {
        v = 0;
        testarr.clear();
        for (z = 0; z < 9; ++z) {
          testarr.add(z+1);
        }
        if (hascolumnmv[w] == true) {
          for (int ii = 0; ii < 9; ++ii) {
            if (testarr.contains(this.matrix[ii][w])) {
              testarr.remove((Integer)this.matrix[ii][w]);
            }
          }
          while(!testarr.isEmpty()) {
            this.columnmv[v][w] = testarr.get(0);
            v++;
            testarr.remove(0);
          }
        }
      }
    }
  }
}
/*
findSolution() compares matching values within this.columndup and this.rowdup and
replace the intersection with matching values within this.rowmv and this.columnmv
this is the only method that modifies the soltion matrix
*/
public void findSolution() {
  for (int i = 0; i < 9; ++i) {
    for (int j = 0; j < 9; ++j) {
      if (hasrowmv[i] == true) {
        if(hascolumnmv[j] == true) {
          for (int k = 0; k < 9; ++k) {
            for (int w = 0; w < 9; ++w) {
              if (rowmv[i][k] == columnmv[w][j] && rowmv[i][k] != 0) {
                this.matrix[i][j] = rowmv[i][k];
              }
            }
          }
        }
      }
    }
  }
}
  //takes in testfile name, reads from file and returns a String of file contents
  public static String fileread(String filename) {
    try {
      BufferedReader inputstream = new BufferedReader(new FileReader(filename));
      String data = "";
      String line = "";
        while ((line = inputstream.readLine()) != null) {
          data += line;
        }
      inputstream.close();
      return data;
    }
    catch (FileNotFoundException e) { System.out.println("FNF");}
    catch (IOException e2) {System.out.println("IOE");}
    return null;
  }
  public static int[][] initializeMatrix (String data) {
    int row = 0;
    int column = 0;
    int[][] testMatrix = new int[9][9];
    for (row =0; row < 9; ++row) {
      for (column=0; column < 9; ++column) {
        if (!(row == 8 && column == 8)) {
          int comma = data.indexOf(',');
          testMatrix[row][column] = Integer.parseInt(data.substring(comma-1,comma));
          data = data.substring(comma+1, data.length());
        }
        //if last [row][column]
        else {
          testMatrix[row][column] = Integer.parseInt(data);
          break;
        }
      }
    }
    return testMatrix;
  }
  //displays a matrix with only the cells with errors found. All other cells hold value of 0
  public void printupdatedduplicatematrix() {
    int[][] updatedmatrix = new int[9][9];
    for (int i = 0; i < 9; ++i) {
      for(int j = 0; j < 9; ++j) {
        if (rowdup[i][j] != 0) {
          updatedmatrix[i][j] = rowdup[i][j];
        }
        if (columndup[i][j] != 0) {
          updatedmatrix[i][j] = columndup[i][j];
        }
      }
    }
    for (int b = 0; b < 9; ++b) {
      for (int c = 0; c < 9; ++c) {
        if (updatedmatrix[b][c] != 0) {
          System.out.println("Duplicate value: " + updatedmatrix[b][c] + " on Row: " + (b+1) + " Colulmn: " + (c+1));
        }
      }
    }
    System.out.println("\nIsolated Problem Cells: ");
    this.printmatrix(updatedmatrix);
  }

  public static void printmatrix(int[][] matrix) {
    int i=0, j=0;
    System.out.println("--------------------");
    for (i = 0; i < 9; ++i) {
      for (j = 0; j < 9; ++j) {
        System.out.print(matrix[i][j] + " ");
      }
      System.out.print("\n");
    }
    System.out.println("--------------------");
  }
}
