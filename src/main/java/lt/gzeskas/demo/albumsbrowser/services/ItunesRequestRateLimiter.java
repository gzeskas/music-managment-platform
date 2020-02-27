package lt.gzeskas.demo.albumsbrowser.services;

import java.util.concurrent.atomic.AtomicLong;

public class ItunesRequestRateLimiter {
    private final AtomicLong counter = new AtomicLong(0);

    private final long maxRequestCount;

    public ItunesRequestRateLimiter(long maxRequestCount) {
        this.maxRequestCount = maxRequestCount;
    }

    public boolean isRequestAvailable() {
        if (counter.get() >= maxRequestCount) {
            return false;
        }
        counter.incrementAndGet();
        return true;
    }

    /**
     * On reset return last value
     * @return
     */
    public Long reset() {
        return counter.getAndSet(0);
    }

}
