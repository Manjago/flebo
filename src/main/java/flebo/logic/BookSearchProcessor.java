package flebo.logic;

import flebo.model.BookSearchRequest;
import flebo.model.BookSearchResponse;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.atomic.AtomicInteger;

public class BookSearchProcessor
        implements Flow.Processor<BookSearchRequest, BookSearchResponse>{

    private Flow.Subscription subscription;
    private AtomicInteger counter = new AtomicInteger(5);

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(BookSearchRequest item) {
        System.out.println("got " + item);
        int decrementAndGet = counter.decrementAndGet();
        if (decrementAndGet > 0) {
            subscription.request(1);
        }
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void subscribe(Flow.Subscriber<? super BookSearchResponse> subscriber) {

    }
}
