package com.example.assignment2;

public class Mole {
    private boolean isVisible;   // Did the Ground squirrels appear in this hole

    public Mole() {
        this.isVisible = false;  // Initial state: Not present
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void show() {         // Ground squirrels appear
        isVisible = true;
    }

    public void hide() {         // The ground squirrels have disappeared.
        isVisible = false;
    }
}
