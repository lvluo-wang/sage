package me.icymint.sage;

import me.icymint.sage.base.spec.api.EventProducer;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.junit.Assert.assertTrue;

/**
 * Created by daniel on 2016/9/30.
 */
public class TestEventInTransactionProducer implements EventProducer<String, TestEvent> {
    @Override
    public Class<String> resultClass() {
        return String.class;
    }

    @Override
    public TestEvent apply(String s) {
        assertTrue(TransactionSynchronizationManager.isActualTransactionActive());
        return new TestEvent().setMessage(s + " is in transaction");
    }
}
