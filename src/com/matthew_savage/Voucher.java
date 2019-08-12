package com.matthew_savage;

import java.util.concurrent.Semaphore;

public class Voucher {

    private static final Semaphore semaphore = new Semaphore(1, true);

    static void acquire() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Logging.logError(e.toString());
        }
    }

    static void release() {
        semaphore.release();
    }
}
