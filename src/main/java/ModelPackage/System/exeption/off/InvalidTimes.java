package ModelPackage.System.exeption.off;

public class InvalidTimes extends Exception {
    public InvalidTimes(){
        super("One OF Times Or Both Aren't Valid");
    }
}

