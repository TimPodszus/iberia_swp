package de.uol.swp.common;

public class Configuration {
    static final int DEFAULT_PORT = 8899;

    private Configuration(){
    }

    public static int getDefaultPort(){
        return DEFAULT_PORT;
    }
}
