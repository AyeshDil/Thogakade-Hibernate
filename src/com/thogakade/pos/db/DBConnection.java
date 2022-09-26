package com.thogakade.pos.db;

import java.sql.Connection;

public class DBConnection {
    // 1 Rule
    private static DBConnection dbConnection;
    private Connection connection;

    // 2 Rule
    private DBConnection(){}

    // 3 Rule
    public static DBConnection getInstance(){

    }

    public Connection getConnection(){
        return connection;
    }


}
