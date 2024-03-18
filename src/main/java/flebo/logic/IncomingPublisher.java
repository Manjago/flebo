package flebo.logic;

import flebo.model.IncomingRawRequest;

import java.util.concurrent.Flow;

public class IncomingPublisher implements Flow.Publisher<IncomingRawRequest>{

    private Flow.Subscriber<? super IncomingRawRequest> subscriber;

    @Override
    public void subscribe(Flow.Subscriber<? super IncomingRawRequest> subscriber) {
        this.subscriber = subscriber;
    }

    public void send() {
        this.subscriber.onNext(new IncomingRawRequest("gg"));
    }
}
