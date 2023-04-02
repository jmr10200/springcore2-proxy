package hello.proxy.app.componentscan;

public class ModifyFailException extends RuntimeException {

    public ModifyFailException() {
        super();
    }

    public ModifyFailException(String message) {
        super(message);
    }

    public ModifyFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
