package com.marmot.intrepid.naturalhealer.data;

import java.util.concurrent.Executor;

public class BDDExecutor implements Executor {
    public void execute(Runnable r) {
        new Thread(r).start();
    }

}
