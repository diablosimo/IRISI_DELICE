package sample.util;

import java.sql.*;

public class ConnectDB {
    private static String urlstring = "jdbc:mysql://localhost:3306/cmdrestaurant?serverTimezone=UTC";
    private static String driverName = "com.mysql.cj.jdbc.Driver";
    private static String username = "root";
    private static String password = "root";
    private static Connection con;


    public static Connection getConnection() {
        try {
            Class.forName(driverName);
            try {
                con = DriverManager.getConnection(urlstring, username, password);
            } catch (SQLException ex) {
                System.out.println("Failed to create the database connection.");
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("Driver not found.");
        }
        return con;
    }

    public static Statement createStatement() throws SQLException{
        return getConnection().createStatement();
    }

    public static ResultSet executeQuery(String sql)throws SQLException{
        return createStatement().executeQuery(sql);
    }

    public static PreparedStatement execute(String sql) throws SQLException {
        return getConnection().prepareCall(sql);
    }

    public static PreparedStatement prepare(String sql) throws SQLException {
        return getConnection().prepareStatement(sql);
    }
    public static PreparedStatement prepareWithKey(String sql) throws SQLException {
        return getConnection().prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
    }

    public static int executePreparedStatment(PreparedStatement stmt)throws SQLException{
        return stmt.executeUpdate();
    }
    public static int getKeyExecutePreparedStatment(PreparedStatement stmt)throws SQLException{
        return stmt.executeUpdate();
    }

    public static int executeUpdate(String sql)throws SQLException{
        return createStatement().executeUpdate(sql);
    }

    public static int executeUpdateID(String sql) throws SQLException{
        return createStatement().executeUpdate(sql,createStatement().RETURN_GENERATED_KEYS);
    }

    public static Long getKeyExecutePreparedStatmentWithKey(PreparedStatement stmt)throws SQLException{
        stmt.executeUpdate();
        Long key=null;
        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                key= generatedKeys.getLong(1);
            }
            else {
                throw new SQLException("Creating failed, no ID obtained.");
            }
        }
        return key;
    }


    public static void close()throws SQLException{
        getConnection().close();
    }
    public static String exec(String query) {
        Connection connection = getConnection();
        Statement statement = null;
        String result = "";
        try {
            //connection = getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(query);
            result = "Success";
        } catch (SQLException e) {
            e.printStackTrace();
            result = e.getMessage();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
