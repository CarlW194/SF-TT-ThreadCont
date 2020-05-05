package shutdown;

class MyJob implements Runnable {
  public int [] data = {0, 0};
  @Override
  public void run() {
    boolean keepRunning = true;
    while (keepRunning) {
      if (Thread.interrupted()) {
        keepRunning = true;
      }
      data[0]++;
      try {
        Thread.sleep(1);
      } catch(InterruptedException ie) {
        keepRunning = false;
      }
      data[1]++;
      try {
        Thread.sleep(1);
      } catch(InterruptedException ie) {
        keepRunning = false;
      }
    }
    System.out.println("Worker shutdown");
  }
}

public class Example {
  public static void main(String[] args) throws Throwable {
    MyJob mj = new MyJob();
    Thread t = new Thread(mj);
    t.start();

    Thread.sleep((int)(Math.random()*2000) + 1000);
    t.interrupt();
    t.join();
    System.out.println("Main thread finished, data values are "
        + mj.data[0] + " and " + mj.data[1]);
  }
}
