package com.sdehunt.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConnectionHelperTest {
    public static void main(String[] args) throws Exception {
        System.out.println(readStringFromDB());
    }

    public static String readStringFromDB() throws Exception{
        String str = "empty";
        //get the connection
        Connection connection = ConnectionHelper.getDBConnection();

        //verify the connection is successful
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT 'Success!' FROM DUAL;");
        while (rs.next()) {
            String id = rs.getString(1);
            str = id;
        }

        //close the connection
        stmt.close();
        connection.close();

        return str;
    }
}
