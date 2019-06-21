package com.antagonisticapple;

import java.util.concurrent.Semaphore;

public class Voucher {

    private static final Semaphore semaphore = new Semaphore(1, true);

    static void acquire() {
        System.out.println("acquired");
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void release() {
        System.out.println("released");
        semaphore.release();
    }
}
