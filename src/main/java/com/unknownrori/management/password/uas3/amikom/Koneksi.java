/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unknownrori.management.password.uas3.amikom;

import java.sql.*;
import java.util.Optional;

/**
 *
 * @author UnknownRori
 */
public class Koneksi {
    private Connection connection = null;
    
    //String server = "jdbc:mysql://localhost:3306/";
    String server = "localhost:3306";
    String db = "";
    String user = "root";
    String pass = "";
    
    Koneksi(String server, String db, String user, String password) throws ClassNotFoundException  {
        Class.forName("com.mysql.cj.jdbc.Driver");

        this.server = server;
        this.db = db;
        this.user = user;
        this.pass = password;
    }
    
    Optional<Connection> connect() {
        if (this.connection == null) {
            String connectionUrl = "jdbc:mysql://"+ this.server +"/" + this.db;
            try {
                DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            this.connection = (Connection) 
                    DriverManager.getConnection(connectionUrl, this.user, this.pass);
            } catch (Exception ex) {
                return Optional.empty();
            }
        }
        
        return Optional.of(this.connection);
    }
}

