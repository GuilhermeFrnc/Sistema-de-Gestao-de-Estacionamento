package application;

import model.dao.DaoFactory;

public class Program {
    public static void main(String[] args) {

        Long existingMonthly = DaoFactory.checkMonthy("1234567");

        System.out.println(existingMonthly);
    }
}
