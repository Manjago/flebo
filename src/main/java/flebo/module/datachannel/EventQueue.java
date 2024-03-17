package flebo.module.datachannel;

import lombok.extern.slf4j.Slf4j;
import org.h2.mvstore.MVStore;
import org.h2.mvstore.tx.Transaction;
import org.h2.mvstore.tx.TransactionMap;
import org.h2.mvstore.tx.TransactionStore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

@Slf4j
public class EventQueue {

    private TransactionStore transactionStore;

    public EventQueue init(@Nullable String dbFileName) {
        final MVStore mvStore = MVStore.open(dbFileName);
        transactionStore = new TransactionStore(mvStore);
        transactionStore.init();
        return this;
    }

    public void poll(@NotNull Consumer<Map.Entry<Object, Object>> consumer, @NotNull String queueName) {
        poll(consumer, queueName, null, null);
    }

    public void poll(@NotNull Consumer<Map.Entry<Object, Object>> consumer, @NotNull String queueName, @NotNull String dlqName) {
        poll(consumer, queueName, dlqName, null);
    }

    public void poll(@NotNull Consumer<Map.Entry<Object, Object>> consumer, @NotNull String queueName, @Nullable String dlq, @Nullable UnaryOperator<Map.Entry<Object, Object>> dlgTransformer) {
        final Transaction pollTransaction = transactionStore.begin();
        final TransactionMap<Object, Object> transactionMap = pollTransaction.openMap(queueName);

        final Map.Entry<Object, Object> event = transactionMap.firstEntry();
        if (event == null) {
            pollTransaction.rollback();
            return;
        }
        transactionMap.remove(event.getKey());
        pollTransaction.commit();

        try {
            consumer.accept(event);
        } catch (Exception ex) {
            if (dlq != null) {
                final Transaction dlqTransaction = transactionStore.begin();
                final TransactionMap<Object, Object> dlqMap = dlqTransaction.openMap(dlq);

                final Map.Entry<Object, Object> dlqEvent;
                if (dlgTransformer != null) {
                    dlqEvent = dlgTransformer.apply(event);
                } else {
                    dlqEvent = event;
                }

                dlqMap.put(dlqEvent.getKey(), dlqEvent.getValue());
                dlqTransaction.commit();
            }
        }
    }

    public void push(@NotNull Object key, @NotNull Object value, @NotNull String queueName) {
        final Transaction transaction = transactionStore.begin();
        final TransactionMap<Object, Object> transactionMap = transaction.openMap(queueName);
        transactionMap.put(key, value);
        transaction.commit();
    }

}
