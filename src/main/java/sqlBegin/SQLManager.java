package sqlBegin;

import java.sql.*;

public class SQLManager {
    private final String URL = "jdbc:postgresql://localhost:5432/Example";
    private final String USERNAME = "postgres";
    private final String PASSWORD = "postgres";

    public void inWork(String request) {
        if (request.equals("exit")) {
            Main.setWork(false); // ��� ������ �� ������������ ����� while �� ����� ��� System.exit, �� ��� ����� �����������)
        } else if (request.trim().charAt(0) == '-') { //������� ������ ���������� �������
            getTableView(request.substring(1));
        } else requestSQL(request.trim()); //��������� ������
    }

    public void requestSQL(String request) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD); //��������� ������� Connection
             Statement statement = connection.createStatement()) { //��������� ������� Statement
            if (request.startsWith("select") | request.startsWith("SELECT")) { //���� ���� select �� ���������� �������
                printTable(statement, request);
            } else {
                int result = statement.executeUpdate(request); // �������� �������� ������� � ������� ����� ���������� �����
                System.out.println("Lines was change: " + result);
            }

        } catch (SQLException e) {
            System.out.println("Wrong request or code mistake :) try again");
        }
    }

    public void getTableView(String tableName) {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from public.").append(tableName); //���� ������ ������� ��� ����������� ��������� �������
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            printTable(statement, sb.toString());

        } catch (SQLException e) {
            System.out.println("Table not found");
        }
    }

    private void printTable(Statement statement, String request) throws SQLException {
        ResultSet resultSet = statement.executeQuery(request); // ��������� ���������� �������
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData(); // ��������� ���������� � �������
        int[] lengthOfCells = getLengthOfCell(statement, request); //��������� ������� �������� �����
        printTittle(resultSetMetaData, lengthOfCells); //����������� ��������� �������
        printTableData(resultSet, resultSetMetaData, lengthOfCells); //����������� �������
    }

    private void printTittle(ResultSetMetaData rsMeta, int[] sizes) {
        StringBuilder sb = new StringBuilder();
        try {
            System.out.print("|");
            for (int i = 0; i < rsMeta.getColumnCount(); i++) {
                for (int j = 0; j <= sizes[i]; j++)
                    System.out.print("-");
                System.out.print("|");
            }
            System.out.println();

            System.out.print("|");
            for (int i = 0; i < rsMeta.getColumnCount(); i++) {
                sb.delete(0, sb.length()); //��������� '�������' ����� ������������ ������ ��� ������
                sb.append("%").append(sizes[i] + 1).append(".").append(sizes[i]).append("s|"); //������������ ������
                System.out.format(sb.toString(), rsMeta.getColumnName(i + 1)); //� ������ ��������
            }
            System.out.println();

            System.out.print("|");
            for (int i = 0; i < rsMeta.getColumnCount(); i++) { //���������� ����� ���������� ���������� �� �������� �������
                for (int j = 0; j <= sizes[i]; j++)
                    System.out.print("=");
                System.out.print("|");
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println("Exception from tittle draw");
        }
    }

    private void printTableData(ResultSet rs, ResultSetMetaData rsMeta, int[] sizes) {
        StringBuilder sb = new StringBuilder();
        try {
            while (rs.next()) {
                System.out.print("|");
                for (int i = 0; i < rsMeta.getColumnCount(); i++) {
                    sb.delete(0, sb.length());
                    sb.append("%").append(sizes[i] + 1).append(".").append(sizes[i]).append("s|");
                    System.out.format(sb.toString(), rs.getString(i + 1));
                }
                System.out.println();

                System.out.print("|");
                for (int i = 0; i < rsMeta.getColumnCount(); i++) {
                    for (int j = 0; j <= sizes[i]; j++)
                        System.out.print("-");
                    System.out.print("|");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Exception from table draw");
        }
    }

    private int[] getLengthOfCell(Statement statement, String request) {
        int[] result = new int[1];
        try {
            ResultSet rs = statement.executeQuery(request);
            ResultSetMetaData rsMeta = rs.getMetaData();
            int[] lengthCells = new int[rsMeta.getColumnCount()];
            while (rs.next())
                for (int i = 0; i < rsMeta.getColumnCount(); i++)
                    if (lengthCells[i] < rs.getString(i + 1).length())
                        lengthCells[i] = rs.getString(i + 1).length();
            for (int i = 0; i < rsMeta.getColumnCount(); i++)
                if (lengthCells[i] < rsMeta.getColumnName(i + 1).length())
                    lengthCells[i] = rsMeta.getColumnName(i + 1).length();
            result = lengthCells;

        } catch (SQLException e) {
            System.out.println("Exception from getLengthOfCell");
        }
        return result;
    }


}
