//import java.util.*;

/*
  semaphore class that controls access of shared resource
  between two processes
*/
public class Semaphore {
  privates static final int resource_Count;

  public Semaphore() {
    this.resource_Count = 1;
  }

  public Semaphore(int resource_Count) {
    this.resource_Count = resource_Count;
  }

  public void wait(resource_Count) {
    while (resource_Count <= 0) {
      //wait
    }
    resource_Count--;
  }
  public void signal(resource_Count) {
      resource_Count++;
  }
}

/*
Semaphore fred_Data
Semaphore wilma_Data


fred_See {
  //thread for updating fred see information

  wait (fred_Data)
  //update and output fred_Data based on time and boolean
  signal (fred_Data)
  wait (wilma_Data)
  //update and output wilma data based on time and boolean
  signal (wilma_Data)
}

wilma_Saw {
  //while wilma.position != 0

  //then

  wait (fred_Data)
  //update and output fred_Data
  signal (fred_Data)
  wait (wilma_Data)
  //update and output wilma data
  signal (wilma_Data)
}
you have person class wilma and fred with private var: position, and velocity




initial conditions:
time = 0;
fred.pos = 1 wilma.pos = 7
velocity = 1, boolean fred_Going_Up = 1;






*/





//letWilmaGo

//letFredGo
