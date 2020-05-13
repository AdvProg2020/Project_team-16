package View.exceptions;

public class OutOfRangeInputException extends Exception {
    public OutOfRangeInputException(int limit){
        super(String.format("Length of input is out of range.It must be at most %d characters",limit));
    }
}
