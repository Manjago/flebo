package flebo.logic;

import flebo.model.BookSearchRequest;

import java.util.concurrent.Flow;

public class IncomingPublisher implements Flow.Publisher<BookSearchRequest>{

    private Flow.Subscriber<? super BookSearchRequest> subscriber;

    @Override
    public void subscribe(Flow.Subscriber<? super BookSearchRequest> subscriber) {
        this.subscriber = subscriber;
    }

    public void send() {
        this.subscriber.onNext(new BookSearchRequest());
    }
}
