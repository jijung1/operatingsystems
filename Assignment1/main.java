/*
  CPSC380 Operating Systems
  Project 1
  Jin Jung
  10/22/19

  Main.java file for testing multithreaded Sudoko Solution checker
*/
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
public class main {
  public static SudokuCheck checker;
  public static void main(String[] args) {
    if (args.length != 0 && args.length < 2) {
      checker = new SudokuCheck(args[0]);
    //  Thread completionCheck = new Thread(new SudokuCompletion());
      Thread checkRow = new Thread(new SudokuRow());
      Thread checkColumn = new Thread(new SudokuColumn());
      Thread checkSubGrid = new Thread(new SudokuSubGrid());
      checkRow.start();
      checkColumn.start();
      checkSubGrid.start();
      try {
        checkRow.join();
        checkColumn.join();
        checkSubGrid.join();
      }
      catch (InterruptedException e) {
        System.out.println("Interrupted Exception");
        System.out.println(e.getMessage());
      }
      finally {
        System.out.println("\nOriginal File: ");
        checker.printmatrix(checker.originalMatrix);
        checker.printupdatedduplicatematrix();
        System.out.println("Solution: ");
        checker.printmatrix(checker.getMatrix());
      }
    }
    else {
      System.out.println("Invalid input! Please enter file name when running program: i.e. [java main] [file.txt]");
      System.exit(1);
    }
  }
  private static class SudokuSubGrid implements Runnable {
    public void run() {
      for (int i = 0 ; i < 9; ++i) {
        while (!checker.goodSubGrids[i]) {
          checker.checkSubGrid();
        }
      }
    }
  }
  private static class SudokuRow implements Runnable {
    public void run() {
      checker.checkRow();
    }
  }
  private static class SudokuColumn implements Runnable {
    public void run() {
      checker.checkColumn();
    }
  }

}
