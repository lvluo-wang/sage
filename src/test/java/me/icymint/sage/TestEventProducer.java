package me.icymint.sage;

import me.icymint.sage.base.spec.internal.api.EventProducer;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.junit.Assert.assertFalse;

/**
 * Created by daniel on 2016/9/30.
 */
public class TestEventProducer implements EventProducer<String, TestEvent> {
    @Override
    public Class<String> resultClass() {
        return String.class;
    }

    @Override
    public TestEvent apply(String s) {
        assertFalse(TransactionSynchronizationManager.isActualTransactionActive());
        return new TestEvent().setMessage(s);
    }
}
