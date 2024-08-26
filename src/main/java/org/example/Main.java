package org.example;

import db.DB;

public class Main {
    public static void main(String[] args) {
        DB.getConn();
        DB.closeConnection();
    }
}