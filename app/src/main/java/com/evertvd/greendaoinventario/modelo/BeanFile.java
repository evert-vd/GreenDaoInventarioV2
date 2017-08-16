package com.evertvd.greendaoinventario.modelo;

/**
 * Created by evertvd on 20/07/2017.
 */

public class BeanFile implements Comparable<BeanFile> {
    private String name;
    private String data;
    private String path;


    public BeanFile(String n, String d, String p) {
        name = n;
        data = d;
        path = p;
    }

    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }

    public String getPath() {
        return path;
    }

    public int compareTo(BeanFile o) {
        if (this.name != null)
            return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
        else
            throw new IllegalArgumentException();
    }
}
