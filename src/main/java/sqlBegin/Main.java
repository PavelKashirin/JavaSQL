package sqlBegin;

import java.util.Scanner;

public class Main {
    public static boolean work = true; //���������� ������

    public static void setWork(boolean work) {
        Main.work = work;
    }

    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver"); //�������� �������� JDBC ��� postgres
        } catch (ClassNotFoundException e) {
            System.out.println("Wrong Driver.class(((( in main");
        }
        SQLManager sqlManager = new SQLManager();
        Scanner scanner = new Scanner(System.in);
        System.out.println("For exit write: 'exit'");
        while (work) { //������������ ����
            System.out.print("Write sql request or '-table_name': ");
            String request = scanner.nextLine(); //���������� ������� � �������
            sqlManager.inWork(request); // �������� ������� ��� �������� � ����������
        }
    }
}
