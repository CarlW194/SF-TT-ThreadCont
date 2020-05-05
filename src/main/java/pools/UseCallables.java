package pools;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

class MyCallableJob implements Callable<String> {
  private static int nextId = 0;
  private String myId = " Job " + nextId++;

  @Override
  public String call() throws Exception {
    System.out.println(Thread.currentThread().getName()
      + myId + " starting...");
    try {
      Thread.sleep((int)(Math.random() * 2000) + 2000);
      if (Math.random() > 0.7) {
        throw new SQLException("Database broke!! " + myId);
      }
    } catch (InterruptedException ie) {}
    System.out.println(Thread.currentThread().getName()
      + myId + " ending...");
    return "Return value from " + myId;
  }
}

public class UseCallables {
  public static void main(String[] args) {
    ExecutorService es = Executors.newFixedThreadPool(2);
    List<Future<String>> handles = new ArrayList<>();

    for (int i = 0; i < 10; i++) {
      handles.add(es.submit(new MyCallableJob()));
    }
    System.out.println("Jobs submitted...");
    es.shutdown(); // close input queue, run all jobs to completion
//    es.shutdownNow(); // close input queue, empty input queue,
//    run current jobs to completion, send interrupts to current jobs

    handles.get(5).cancel(true);

    while (handles.size() > 0) {
      Iterator<Future<String>> iter = handles.iterator();
      while (iter.hasNext()) {
        Future<String> handle = iter.next();
        if (handle.isDone()) {
          try {
            String result = handle.get();
            System.out.println("Job returned: " + result);
          } catch (InterruptedException e) {
            e.printStackTrace();
          } catch (ExecutionException e) {
//            e.printStackTrace();
            System.out.println("Execution exception... "
                + e.getCause().getMessage());
          } catch (CancellationException ce) {
            System.out.println("A job was canceled");
          }
          iter.remove();
        }
      }
    }
    System.out.println("all jobs finished...");
  }
}
