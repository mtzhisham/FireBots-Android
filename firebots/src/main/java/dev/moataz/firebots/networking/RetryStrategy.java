package dev.moataz.firebots.networking;

class RetryStrategy {
    private static final int DEFAULT_RETRIES = 3;
    private static final long DEFAULT_WAIT_TIME_IN_MILLI = 10001;

    private int numberOfRetries;
    private int numberOfTriesLeft;
    private long timeToWait;

    RetryStrategy() {
        this(DEFAULT_RETRIES, DEFAULT_WAIT_TIME_IN_MILLI);
    }

    private RetryStrategy(int numberOfRetries,
                          long timeToWait) {
        this.numberOfRetries = numberOfRetries;
        numberOfTriesLeft = numberOfRetries;
        this.timeToWait = timeToWait;
    }

    /**
     * @return true if there are tries left
     */
    boolean shouldRetry() {
        return numberOfTriesLeft > 0;
    }

    void errorOccured()  {
        try {
            numberOfTriesLeft--;
            if (!shouldRetry()) {
                throw new Exception("Retry Failed: Total " + numberOfRetries
                        + " attempts made at interval " + getTimeToWait()
                        + "ms");
            }
            waitUntilNextTry();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private long getTimeToWait() {
        return timeToWait;
    }

    private void waitUntilNextTry() {
        try {
            Thread.sleep(getTimeToWait());
        } catch (InterruptedException ignored) {
        }
    }
}
