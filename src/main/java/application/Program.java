package application;

import model.enums.VehicleCategory;
import model.enums.VehicleType;
import service.MonthlyService;
import service.ParkingService;

import java.util.Scanner;


public class Program {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int cont =1;
        String plate;
        VehicleType type = null;
        int gate;

        while (cont!= 0){
            System.out.println("BEM VINDO AO ESTACIONAMENTO!");
            System.out.println("----------------------------");
            System.out.println("Oque deseja fazer:");
            System.out.println("(1) Cadastrar Mensalista.");
            System.out.println("(2) Entrar no estacionamento.");
            System.out.println("(3) Sair do estacionamento.");
            System.out.println("(4) Cadastrar Caminhão de entrega.");
            System.out.println("(0) Fechar o Sitema.");
            cont= sc.nextInt();

            switch (cont){
                case 1:
                    System.out.println("Qual a placa do Veículo: ");
                    plate = sc.next();

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

                case 2:
                    System.out.println("Qual a placa do veiculo:");
                    plate = sc.next();
                    System.out.println("Em qual cancela esta:");
                    System.out.println("(1)(2)(3)(4)(5)(6)(7)(8)(9)(10)");
                    gate = sc.nextInt();

                    ParkingService parkingService = new ParkingService();
                    if(parkingService.entryVehicleMonthly(plate, gate)){
                        break;
                    }
                    System.out.println("Qual o tipo do veiculo:");
                    System.out.println("(1)Moto.");
                    System.out.println("(2)Carro.");
                    System.out.println("(3)Caminhão.");
                    System.out.println("(4)Van.");
                    System.out.println("(5)Veiculo Publico.");
                    int checkType = sc.nextInt();

                    switch (checkType){
                        case 1:
                            type = VehicleType.MOTO;
                            break;
                        case 2:
                            type= VehicleType.CARRO;
                            break;
                        case 3:
                            type= VehicleType.CAMINHAO;
                            break;
                        case 4:
                            type= VehicleType.VAN;
                            break;
                        case 5:
                            type = VehicleType.VEICULO_PUBLICO;
                    }

                    System.out.println("Seu veiculo e publico");
                    System.out.println("(1)Sim. (2)Não.");
                    int checkCateg = sc.nextInt();

                    if (checkCateg == 1){
                        parkingService.entryVehiclePublic(plate, VehicleCategory.PUBLICO, gate);
                    }

                    parkingService.entryVehicle(plate, gate, type, VehicleCategory.AVULSO);
                    break;
                case 0:
                    break;
            }

        }

    }
}
