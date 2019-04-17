package com.bytehonor.sdk.boot.hbase.error;

public class HbaseSdkException extends RuntimeException {

    private static final long serialVersionUID = 5857002720266522490L;
    
    public HbaseSdkException() {
        super();
    }

    public HbaseSdkException(String message) {
        super(message);
    }

    public HbaseSdkException(String message, Throwable cause) {
        super(message, cause);
    }

    public HbaseSdkException(Throwable cause) {
        super(cause);
    }

}
