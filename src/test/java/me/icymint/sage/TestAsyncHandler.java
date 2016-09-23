package me.icymint.sage;

import me.icymint.sage.base.spec.api.AsyncEventHandler;
import me.icymint.sage.base.spec.entity.BaseEvent;
import org.springframework.stereotype.Service;

/**
 * Created by daniel on 2016/9/23.
 */
@Service
public class TestAsyncHandler implements AsyncEventHandler<BaseEvent> {

    @Override
    public void handle(BaseEvent event) {
        System.out.printf("Event %s finished\n", event);
    }
}
