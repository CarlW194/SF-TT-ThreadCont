package stamped;

import java.util.concurrent.locks.StampedLock;

public class Example {
  private static int a;
  private static int b;

  private static StampedLock lock = new StampedLock();

  public static void main(String[] args) throws Throwable {
    Thread t = new Thread(()-> {
      for (int i = 0; i< 10_000; i++) {
        long l = lock.writeLock();
        if (l != 0) {
          a++;
          try {
            Thread.sleep(1);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          b++;
          try {
            Thread.sleep(1);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          lock.unlock(l);
        }
      }
      System.out.println("writer finished...");
    });
    t.start();

    Thread t2 = new Thread(()->{
      int last = 0;
      while (!Thread.interrupted()) {
        long l = lock.tryOptimisticRead();
        int a1 = a;
        int b1 = b;
        if(lock.validate(l)) {
          if (a1 != b1 || a1 < last) System.out.println("ERROR!!");
          last = a;
        }
      }
      System.out.println("Reader thread exiting...");
    });
    t2.start();
    System.out.println("Jobs started");
    t.join();
    t2.interrupt();
    t2.join();
  }
}
