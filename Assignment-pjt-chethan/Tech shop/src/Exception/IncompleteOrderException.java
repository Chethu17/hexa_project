package Exception;

public class IncompleteOrderException extends Throwable {
    public IncompleteOrderException(String message) {
        super(message);
    }
}
