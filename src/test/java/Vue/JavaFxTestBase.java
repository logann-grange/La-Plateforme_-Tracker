package Vue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.BeforeAll;

import javafx.application.Platform;

public abstract class JavaFxTestBase {

    private static final AtomicBoolean STARTED = new AtomicBoolean(false);

    @BeforeAll
    static void initJavaFxToolkit() throws InterruptedException {
        if (STARTED.compareAndSet(false, true)) {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(latch::countDown);
            if (!latch.await(5, TimeUnit.SECONDS)) {
                throw new IllegalStateException("JavaFX toolkit initialization timed out");
            }
        }
    }
}
