package me.icymint.sage;

import me.icymint.sage.base.spec.internal.api.EventHandler;
import me.icymint.sage.base.spec.entity.BaseEvent;
import org.springframework.stereotype.Service;

/**
 * Created by daniel on 2016/9/23.
 */
@Service
public class TestAsyncHandler implements EventHandler<BaseEvent> {

    @Override
    public void accept(BaseEvent event) {
        System.out.printf("Event %s finished\n", event);
    }
}
