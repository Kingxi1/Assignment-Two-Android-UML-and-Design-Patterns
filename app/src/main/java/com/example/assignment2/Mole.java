package com.example.assignment2;
import android.widget.ImageView;

public class Mole {
    //UML Field
    private int index;                //The number of this hamster (0 to 8)
    private ImageView imageView;      //The corresponding ImageView
    private boolean isVisible;        // Is the marmot currently displayed?
    // UML constructor: Mole(index: int, imageView: ImageView)
    public Mole(int index, ImageView imageView) {
        this.index = index;
        this.imageView = imageView;
        this.isVisible = false;
    }
    public int getIndex() {
        return index;
    }
    public ImageView getImageView() {
        return imageView;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

}
