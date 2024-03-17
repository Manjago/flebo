package flebo.module.datachannel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventQueueTest {

    private EventQueue eventQueue;

    @BeforeEach
    void setUp() {
        eventQueue = new EventQueue().init(null);
    }

    @Test
    void justPush() {
        //given
        final List<Map.Entry<Object, Object>> polledEvents = new ArrayList<>();
        final String queueName = "test";
        eventQueue.push(1L, "string 1", queueName);
        eventQueue.push(0L, "string 0", queueName);

        //when
        eventQueue.poll(polledEvents::add, queueName);
        eventQueue.poll(polledEvents::add, queueName);
        eventQueue.poll(polledEvents::add, queueName);

        //then
        assertEquals(2, polledEvents.size());
        assertEquals(0L, polledEvents.get(0).getKey());
        assertEquals("string 0", polledEvents.get(0).getValue());
        assertEquals(1L, polledEvents.get(1).getKey());
        assertEquals("string 1", polledEvents.get(1).getValue());
    }

    @Test
    void testDlq() {
        //given
        final List<Map.Entry<Object, Object>> polledEvents = new ArrayList<>();
        final List<Map.Entry<Object, Object>> errEvents = new ArrayList<>();
        final String queueName = "test";
        final String dlqName = "dlq";
        eventQueue.push(1L, "string 1", queueName);
        eventQueue.push(0L, "string 0", queueName);

        //when
        eventQueue.poll(polledEvents::add, queueName, dlqName);
        eventQueue.poll(it -> {
            throw new IllegalStateException("test");
        }, queueName, dlqName);
        eventQueue.poll(polledEvents::add, queueName);

        eventQueue.poll(errEvents::add, dlqName);

        //then
        assertEquals(1, polledEvents.size());
        assertEquals(0L, polledEvents.get(0).getKey());
        assertEquals("string 0", polledEvents.get(0).getValue());

        assertEquals(1, errEvents.size());
        assertEquals(1L, errEvents.get(0).getKey());
        assertEquals("string 1", errEvents.get(0).getValue());
    }

    @Test
    void testDlqTransformer() {
        //given
        final List<Map.Entry<Object, Object>> polledEvents = new ArrayList<>();
        final List<Map.Entry<Object, Object>> errEvents = new ArrayList<>();
        final String queueName = "test";
        final String dlqName = "dlq";
        eventQueue.push(1L, "string 1", queueName);
        eventQueue.push(0L, "string 0", queueName);

        //when
        eventQueue.poll(polledEvents::add, queueName, dlqName);
        eventQueue.poll(it -> {
            throw new IllegalStateException("test");
        }, queueName, dlqName, entry -> {
            final Long key = (Long) entry.getKey();
            final String value = (String) entry.getValue();
            return new AbstractMap.SimpleImmutableEntry<>(key * 10, value + "!");
        });
        eventQueue.poll(polledEvents::add, queueName);

        eventQueue.poll(errEvents::add, dlqName);

        //then
        assertEquals(1, polledEvents.size());
        assertEquals(0L, polledEvents.get(0).getKey());
        assertEquals("string 0", polledEvents.get(0).getValue());

        assertEquals(1, errEvents.size());
        assertEquals(10L, errEvents.get(0).getKey());
        assertEquals("string 1!", errEvents.get(0).getValue());
    }
}