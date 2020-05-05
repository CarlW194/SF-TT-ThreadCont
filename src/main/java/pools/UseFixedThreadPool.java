package pools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class MyJob implements Runnable {
  private static int nextId = 0;
  private String myId = " Job " + nextId++;

  @Override
  public void run() {
    System.out.println(Thread.currentThread().getName()
      + myId + " starting...");
    try {
      Thread.sleep((int)(Math.random() * 2000) + 2000);
    } catch (InterruptedException ie) {}
    System.out.println(Thread.currentThread().getName()
      + myId + " ending...");
  }
}
public class UseFixedThreadPool {
  public static void main(String[] args) {
    ExecutorService es = Executors.newFixedThreadPool(2);
    for (int i = 0; i < 10; i++) {
      es.submit(new MyJob());
    }
    System.out.println("Jobs submitted...");
    es.shutdown();
//    es.submit(new MyJob());
  }
}
