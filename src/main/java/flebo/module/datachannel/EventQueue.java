package flebo.module.datachannel;

import flebo.model.IncomingRawMessage;
import org.h2.mvstore.MVMap;
import org.h2.mvstore.MVStore;
import org.h2.mvstore.tx.Transaction;
import org.h2.mvstore.tx.TransactionMap;
import org.h2.mvstore.tx.TransactionStore;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.Callable;

public class EventQueue {

    private MVStore mvStore = MVStore.open(null);
    private TransactionStore ts;

    public void init() {
        mvStore = MVStore.open(null);
        TransactionStore ts = new TransactionStore(mvStore);
        ts.init();
    }

    public void poll(Callable<Object> apply) {
        final Transaction transaction = ts.begin();
        TransactionMap<Object, Object> internal = transaction.openMap("internal");
        Map.Entry<Object, Object> objectObjectEntry = internal.firstEntry();
        internal.remove(objectObjectEntry.getKey());
        transaction.commit();
    }

}
