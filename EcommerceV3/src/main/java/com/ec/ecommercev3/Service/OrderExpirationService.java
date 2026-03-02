package com.ec.ecommercev3.Service;

import com.ec.ecommercev3.Service.Workers.OrderExpirationWorker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class OrderExpirationService {

    private static final int BATCH_SIZE = 500;

    // Quantas tasks podem rodar ao mesmo tempo
    private static final int MAX_CONCURRENCY = 50;

    private static final int MAX_BATCHES_PER_RUN = 10;

    private final Semaphore semaphore = new Semaphore(MAX_CONCURRENCY);
    private final AtomicBoolean running = new AtomicBoolean(false);

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderExpirationWorker orderExpirationWorker;

    @Autowired
    @Qualifier("vtExecutor")
    private AsyncTaskExecutor vtExecutor;

    @Scheduled(fixedRate = 300000)
    public void verifyOrderExpiration() {

        // evita disparar outra rodada se a anterior ainda está rodando
        if (!running.compareAndSet(false, true)) {
            return;
        }

        try {
            int batchesProcessed = 0;

            while (batchesProcessed < MAX_BATCHES_PER_RUN) {

                List<Long> orderIds = orderService.findWaitingForPaymentOrderIds(BATCH_SIZE);

                if (orderIds.isEmpty()) {
                    break;
                }

                List<Future<?>> futures = new ArrayList<>(orderIds.size());

                for (Long id : orderIds) {
                    semaphore.acquireUninterruptibly();

                    Future<?> f = vtExecutor.submit(() -> {
                        try {
                            System.out.println(Thread.currentThread());
                            orderExpirationWorker.expireIfNeeded(id);
                        } finally {
                            semaphore.release();
                        }
                        return null;
                    });

                    futures.add(f);
                }

                // espera o batch inteiro terminar antes de pegar o próximo
                for (Future<?> f : futures) {
                    try {
                        f.get();
                    } catch (Exception e) {
                    }
                }

                batchesProcessed++;
            }

        } finally {
            running.set(false);
        }
    }
}
