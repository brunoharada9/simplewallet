package br.com.tolive.simplewallet.model;

import java.io.Serializable;

/**
 * Created by bruno.carvalho on 27/06/2014.
 */
public class MenuItem implements Serializable {
    private String title;
    private int icon;

    public MenuItem(){}

    //public NavDrawerItem(String title){
    //    this.title = title;
    //}


    public MenuItem(String title, int icon){
        this.title = title;
        this.icon = icon;
    }

    public String getTitle(){
        return this.title;
    }

    public int getIcon(){
        return this.icon;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }

}
