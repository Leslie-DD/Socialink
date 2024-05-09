package consumerproducer;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class SemaphoreTest {

    public static Semaphore full = new Semaphore(20);
    public static Semaphore empty = new Semaphore(0);
    public static Semaphore mutex = new Semaphore(1);

    public static final LinkedList<String> basket = new LinkedList<>();

    public static void main(String[] args) {
        for (int i = 0; i < 6; i++) {
            int finalI = i;
            new Thread(() -> {
                Merchant merchant = new Merchant(finalI);
                int index = 0;
                while(true) {
                    merchant.produce("bread" + index);
                    index++;
                    Random random = new Random();
                    long time = 1000 + random.nextInt(10);
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            new Thread(() -> {
                Buyer buyer = new Buyer(finalI);
                while(true) {
                    buyer.buy();
                    Random random = new Random();
                    long time = 1000 + random.nextInt(10);
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static class Merchant {
        int num;
        public Merchant(int num) {
            this.num = num;
        }
        public void produce(String bread) {
            try {
                full.acquire();
                mutex.acquire();

                basket.offer(bread);
                System.out.println("Merchant_" + num + " offered a bread " + bread + ", " + basket.size() + " bread rest in basket.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                mutex.release();
                empty.release();
            }
        }
    }

    public static class Buyer {
        int num;
        public Buyer(int num) {
            this.num = num;
        }
        public void buy() {
            try {
                empty.acquire();
                mutex.acquire();
                String bread = basket.poll();
                System.out.println("Buyer_" + num + " bought a bread " + bread + ", " + basket.size() + " bread rest in basket.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                mutex.release();
                full.release();
            }
        }
    }
}
