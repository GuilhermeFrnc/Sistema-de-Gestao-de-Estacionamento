package application;

import service.MonthlyService;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int cont =1;

        while (cont!= 0){
            System.out.println("BEM VINDO AO ESTACIONAMENTO!");
            System.out.println("----------------------------");
            System.out.println("Oque deseja fazer:");
            System.out.println("(1) Cadastrar Mensalista.");
            System.out.println("(0) Fechar o Sitema.");
            cont= sc.nextInt();

            switch (cont){
                case 1:
                    System.out.println("Qual a placa do Veículo: ");
                    String plate = sc.next();

                    System.out.println("Qual tipo do Veículo:");
                    System.out.println("(1) Carro.");
                    System.out.println("(2) Moto.");
                    int model = sc.nextInt();

                    MonthlyService monthlyService = new MonthlyService();

                    try {
                        monthlyService.registerMonthly(plate, model);
                        System.out.println("Mensalista cadastrado com sucesso.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Erro ao cadastrar mensalista: " + e.getMessage());
                    }
                    break;

                case 0:
                    break;
            }

        }

    }
}
