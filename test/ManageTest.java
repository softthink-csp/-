package main.java.com.homework.test;

import main.java.com.homework.manage.Manager;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ManageTest {

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            test(10);
        }
    }

    public static void test(int slot) throws InterruptedException {

        Manager manager = new Manager(10, slot);
        CountDownLatch latch = new CountDownLatch(10);
        ExecutorService executors = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executors.execute(() -> {
                for (int j = 0; j < 100000; j++) {
                    String key = TestUtil.generatorRandomString(10);
                    String value = "";
                    manager.put(key, value);
                }
                latch.countDown();
            });
        }
        latch.await();
        List<Integer> all = manager.getAllNode();
        int[] totalNums = new int[10];
        for (int i = 0; i < 10; i++) {
            totalNums[i] = all.get(i);
        }

        System.out.println(getStd(totalNums));
    }

    private static double getStd(int[] nums) {
        int total = 0;
        for (int val : nums) {
            total += val;
        }
        double average = total / 10;
        double variance = 0;
        for (int val : nums) {
            variance += (val - average) * (val - average);
        }
        return Math.sqrt(variance / 10);
    }

}
