package org.polyscape.test.auto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.polyscape.event.Event;
import org.polyscape.event.EventBus;
import org.polyscape.event.EventMetadata;
import org.polyscape.event.IEvent;

/**
 * @author Madmegsox1
 * @since 13/08/2023
 */

public class EventBusTest {

    @Test
    public void initTest(){
        EventBus bus = new EventBus();
        Assertions.assertNotNull(bus);
    }

    @Test
    public void eventPushTest(){
        EventBus bus = new EventBus();

        IEvent<TestEvent> event = e -> {
            Assertions.assertEquals(e.result, 1);
        };


        TestEvent.addEvent(event, new EventMetadata(TestEvent.class, 0));

        bus.postEvent(new TestEvent(1));
    }



    static class TestEvent extends Event<TestEvent>{

        public final int result;

        public TestEvent(int i){
            this.result = i;
        }
    }

}
