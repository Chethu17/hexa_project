package Exception;

public class PaymentFailedException extends Throwable {
    public PaymentFailedException(String message) {

        super(message);
    }
}
