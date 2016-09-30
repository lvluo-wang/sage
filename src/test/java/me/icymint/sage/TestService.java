package me.icymint.sage;

import me.icymint.sage.base.spec.annotation.NotifyEvent;
import me.icymint.sage.base.spec.annotation.NotifyInTransactionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.junit.Assert.assertFalse;

/**
 * Created by daniel on 2016/9/30.
 */
@Service
public class TestService {
    @Autowired
    TestService testService;


    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public String sendMessageHolder(String message) {
        assertFalse(TransactionSynchronizationManager.isActualTransactionActive());
        return testService.sentMessage(message);
    }


    @Transactional
    @NotifyEvent(eventProducerClass = TestEventProducer.class)
    @NotifyInTransactionEvent(eventProducerClass = TestEventInTransactionProducer.class)
    public String sentMessage(String hello) {
        return hello;
    }
}
