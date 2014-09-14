package net.cattaka.libgeppa.binder.async;

public class AsyncInterfaceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AsyncInterfaceException() {
        super();
    }

    public AsyncInterfaceException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public AsyncInterfaceException(String detailMessage) {
        super(detailMessage);
    }

    public AsyncInterfaceException(Throwable throwable) {
        super(throwable);
    }
    

}
