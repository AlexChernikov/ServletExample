package servlets;

import java.sql.*;

class DBAdmin {
    private static final String DB_DRIVER = "org.postgresql.Driver";
    private static final String DB_CONNECTION = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";

    private static Connection getDBConnection() {
        Connection dbConnection = null;
        try {
            Class.forName(DB_DRIVER);
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER,DB_PASSWORD);
            return dbConnection;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }
        return dbConnection;
    }

    public static void createUserProblemsTable() throws SQLException {
        Connection dbConnection = null;
        Statement statement = null;

        String createTableSQL = "CREATE TABLE public.\"USERS_PROBLEMS\"\n" +
                "(\n" +
                "   \"PHONE_NUMBER\" character(11), \n" +
                "   \"PROBLEM_MESSAGE\" character varying(300)\n" +
                ")";

        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();

            // выполнить SQL запрос
            statement.execute(createTableSQL);
            System.out.println("Table \"USERS_PROBLEMS\" is created!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }
        }
    }

    public static void insertIntoTable(String  phoneNumber, String message) throws SQLException {
        Connection dbConnection = null;
        Statement statement = null;

        String insertTableSQL = "INSERT INTO public.\"USERS_PROBLEMS\""
                + "(\"PHONE_NUMBER\", \"PROBLEM_MESSAGE\") " + "VALUES"
                + "('"+phoneNumber+"','"+message+"')";
        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();

            // выполнить SQL запрос
            statement.executeUpdate(insertTableSQL);
            System.out.println("Entry is added into \"USERS_PROBLEMS\"!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }
        }
    }

    public static ResultSet selectAllFromTable() {
        Connection dbConnection = null;
        Statement statement = null;

        ResultSet rs = null;

        String selectTableSQL = "SELECT \"PHONE_NUMBER\",\"PROBLEM_MESSAGE\" from public.\"USERS_PROBLEMS\"";
        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();

            rs = statement.executeQuery(selectTableSQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rs;
    }
}