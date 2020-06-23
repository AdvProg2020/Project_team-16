package View.Controllers;

import java.util.Random;

import static View.Controllers.Content.COLORS;

public class Advertise {

    private String getRandomColor() {
        return COLORS[new Random().nextInt(COLORS.length)];
    }
}
