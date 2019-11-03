public class SeeSawSim {

  /*
    Driver class to simulate See-Saw using 2 threads and 2 semaphores
  */

  public static void main( String[] args) {
    Semaphore Person_Data = new Semaphore(); //to synchronize updating of position
    Semaphore wilma_Data = new Semaphore();

    Thread fred_See = new Thread(new Fred());
    Thread wilma_Saw = new Thread(new Wilma());

    fred_See.start();
    wilma_Saw.start();

    fred_See.join();
    wilma_Saw.join();

  }


  private static class Fred implements Runnable {
    public void run() {
      int count = 0;

      while (count < 10) {

        Thread.sleep(1000);
        count ++;
      }
      //fred is seeing
      Person_Data.wait();
      //acquired access to Person_data so update Fred's information
      Person_Data.signal();

      Display_Data.wait();  //synchronizes data read and output to console
      //acquired permission to read and display data
      // display fred's information

      Display_Data.signal();

      //while running, Fred thread will use local class Person Fred that will be updated using shared resource
    }
  }
  private static class Wilma implements Runnable {
    public void run() {
    //wilma is sawing
    Person_Data.wait();
    //acquired access to Person_data so update wilma's information
    Person_Data.signal();

    Display_Data.wait();  //synchronizes data read and output to console
    //acquired permission to read and display data
    //display wilma's information

    Display_Data.signal();

    }
  }
}
