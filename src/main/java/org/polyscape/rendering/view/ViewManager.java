package org.polyscape.rendering.view;

import java.util.ArrayList;

public final class ViewManager {
    private final ArrayList<View> views;

    private View currentView;

    public ViewManager(){
        views = new ArrayList<>();
    }

    public void addView(View view){
        views.add(view);
    }

    public void setView(String name){
        currentView = views.stream()
                .filter(v -> v.getClass().getSimpleName().toUpperCase().replace("VIEW", "").equals(name.toUpperCase()))
                .findFirst().get();
    }

    public View getCurrentView(){
        return currentView;
    }





}
