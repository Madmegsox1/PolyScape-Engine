package org.polyscape;

import org.polyscape.ui.UiEngine;

public class Loader {
    public static void main(String[] args){

        UiEngine uiEngine = new UiEngine();

        uiEngine.init();
        UiEngine.getScreenManager().setCurrentUi(0, "ProjectScreen");
        uiEngine.render();
    }
}
