package consumerproducer;

import java.util.LinkedList;
import java.util.Random;

//public class WaitNotifyTest {
//    public static final int MERCHANT_COUNT = 4;
//    public static final int BUYER_COUNT = 6;
//    public static final int LIMIT = 20;
//    public static Basket basket = new Basket();
//
//    public static void main(String[] args) {
//
//        for (int i = 0; i < MERCHANT_COUNT; i++) {
//            int finalI = i;
//            new Thread(() -> {
//                Merchant merchant = new Merchant(finalI);
//                int index = 0;
//                while(true) {
//                    merchant.produce("bread" + index);
//                    Random random = new Random();
//                    long time = 1000 + random.nextInt(10);
//                    try {
//                        Thread.sleep(time);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }).start();
//        }
//        for (int i = 0; i < BUYER_COUNT; i++) {
//            int finalI = i;
//            new Thread(() -> {
//                Buyer buyer = new Buyer(finalI);
//                while(true) {
//                    buyer.buy();
//                    Random random = new Random();
//                    long time = 2000 + random.nextInt(10);
//                    try {
//                        Thread.sleep(time);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//        }
//    }
//
//    public static class Basket {
//        LinkedList<String> buffer = new LinkedList<>();
//        public synchronized void produce(int producerNum, String bread) {
//            while (buffer.size() >= LIMIT) {
//                try {
//                    wait();
//                    System.out.println("basket full, waiting");
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            buffer.offer(bread);
//            System.out.println("Merchant_" + producerNum + " offered a bread " + bread + ", " + buffer.size() + " bread rest in basket.");
//            notify();
//        }
//
//        public void buy(int producerNum) {
//            while (buffer.isEmpty()) {
//                try {
//                    wait();
//                    System.out.println("basket empty, waiting");
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            String bread = buffer.poll();
//            System.out.println("Buyer_" + producerNum + " bought a bread " + bread + ", " + buffer.size() + " bread rest in basket.");
//            notify();
//        }
//    }
//
//    public static class Merchant {
//        int num;
//        public Merchant(int num) {
//            this.num = num;
//        }
//        public void produce(String bread) {
//            basket.produce(num, bread);
//        }
//    }
//
//    public static class Buyer {
//        int num;
//        public Buyer(int num) {
//            this.num = num;
//        }
//        public void buy() {
//            basket.buy(num);
//        }
//    }
//}


public class WaitNotifyTest {
    public static final int MERCHANT_COUNT = 4;
    public static final int BUYER_COUNT = 6;
    public static final int LIMIT = 20;
    public static final LinkedList<String> basket = new LinkedList<>();

    public static void main(String[] args) {

        for (int i = 0; i < MERCHANT_COUNT; i++) {
            int finalI = i;
            new Thread(() -> {
                Merchant merchant = new Merchant(finalI);
                int index = 0;
                while(true) {
                    synchronized (basket) {
                        merchant.produce("bread" + index);
                    }
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
        for (int i = 0; i < BUYER_COUNT; i++) {
            int finalI = i;
            new Thread(() -> {
                Buyer buyer = new Buyer(finalI);
                while(true) {
                    synchronized (basket) {
                        buyer.buy();
                    }
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
            while (basket.size() >= LIMIT) {
                try {
                    basket.wait();
                    System.out.println("basket full, waiting");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            basket.offer(bread);
            System.out.println("Merchant_" + num + " offered a bread " + bread + ", " + basket.size() + " bread rest in basket.");
            basket.notify();
        }
    }

    public static class Buyer {
        int num;
        public Buyer(int num) {
            this.num = num;
        }
        public void buy() {
            while (basket.isEmpty()) {
                try {
                    basket.wait();
                    System.out.println("basket empty, waiting");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            String bread = basket.poll();
            System.out.println("Buyer_" + num + " bought a bread " + bread + ", " + basket.size() + " bread rest in basket.");
            basket.notify();
        }
    }
}
