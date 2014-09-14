
package net.cattaka.libgeppa.exception;

public class NotImplementedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NotImplementedException() {
        super();
    }

    public NotImplementedException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NotImplementedException(String detailMessage) {
        super(detailMessage);
    }

    public NotImplementedException(Throwable throwable) {
        super(throwable);
    }

}
