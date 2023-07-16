package org.polyscape.rendering.view;


import org.polyscape.font.Font;
import org.polyscape.Engine;

public abstract class View implements IView {

    protected Font titleFont = null;
    protected Font paraFont = null;


    public View(){
        initView();
        viewFonts();
    }


    public void viewFonts(){
        titleFont = Engine.getFontRenderer().getFont("Title");
        paraFont = Engine.getFontRenderer().getFont("Para");
    }

    public void initView(){}

}
