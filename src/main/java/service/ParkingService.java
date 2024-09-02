package service;


import model.dao.DaoFactory;
import model.dao.VehicleDAO;
import model.entities.Parking;
import model.entities.Vehicle;
import model.enums.VehicleCategory;
import model.enums.VehicleType;

import java.util.Date;
import java.util.concurrent.TimeUnit;


public class ParkingService {
    public Boolean entryVehicleMonthly(String plate, Integer gate) {

        Long idVehicleMonthly = DaoFactory.getIdVehicleMonthly(plate);
        if (idVehicleMonthly != null) {
            Vehicle vehicle = DaoFactory.getVehicleDb(idVehicleMonthly);

            if (!validateGateEntry(gate, vehicle.getType())) {
                return false;
            }

            Integer[] slot = null;
            if (vehicle.getType() == VehicleType.MOTO) {
                slot = DaoFactory.checkSlotMoto(vehicle.getCategory());
            } else if (vehicle.getType() == VehicleType.CARRO) {
                slot = DaoFactory.checkSlotCar(vehicle.getCategory());
            }

            if (slot == null || slot.length == 0) {
                throw new IllegalStateException("Não há vagas disponíveis para o veículo do tipo ");
            }

            if (DaoFactory.searchVehicleRegistration(idVehicleMonthly)) {
                throw new IllegalArgumentException("O veículo com placa " + plate + " já está dentro do estacionamento.");
            }
            Parking parking = new Parking(idVehicleMonthly, gate);
            Long idParking = DaoFactory.entryParking(parking);

            DaoFactory.occupySlots(slot);
            DaoFactory.insertSlotOccupy(idParking, slot);
            System.out.println("Entrada do veículo com placa " + plate + " foi bem-sucedida.");
            return true;
        }
        return false;
    }

    public void entryVehiclePublic(String plate, VehicleCategory category, Integer gate) {
        if (!validateGateEntry(gate, VehicleType.VEICULO_PUBLICO)) {
            return;
        }

        VehicleDAO vehicleDao = DaoFactory.checkVehicle();
        Long idVehicle = vehicleDao.getVehicleByPlate(plate);

        if (idVehicle == null) {
            Vehicle vehicle = new Vehicle(plate, VehicleType.VEICULO_PUBLICO, category);
            idVehicle = DaoFactory.createVehicle(vehicle);
        }

        if (DaoFactory.searchVehicleRegistration(idVehicle)) {
            throw new IllegalArgumentException("O veículo com placa " + plate + " já está dentro do estacionamento.");
        }

        Parking parking = new Parking(idVehicle, gate);
        DaoFactory.entryParking(parking);

        System.out.println("Entrada do veículo com placa " + plate + " foi bem-sucedida.");
    }

    public void entryVehicle(String plate, Integer gate, VehicleType type, VehicleCategory category) {
        if (!validateGateEntry(gate, type)) {
            return;
        }
        Integer[] slot = null;
        switch (type) {
            case MOTO:
                slot = DaoFactory.checkSlotMoto(VehicleCategory.AVULSO);
                break;
            case CARRO:
                slot = DaoFactory.checkSlotCar(VehicleCategory.AVULSO);
                break;
            case CAMINHAO:
                slot = DaoFactory.checkSlotTruck(VehicleCategory.AVULSO);
                break;
            default:
                throw new IllegalArgumentException("Tipo de veículo não suportado: " + type);
        }

        if (slot == null || slot.length == 0) {
            throw new IllegalStateException("Não há vagas disponíveis para o veículo do tipo " + type);
        }


        VehicleDAO vehicleDao = DaoFactory.checkVehicle();
        Long idVehicle = vehicleDao.getVehicleByPlate(plate);


        if (idVehicle == null) {
            Vehicle vehicle = new Vehicle(plate, type, category);
            idVehicle = DaoFactory.createVehicle(vehicle);
        }

        if (DaoFactory.searchVehicleRegistration(idVehicle)) {
            throw new IllegalArgumentException("O veículo com placa " + plate + " já está dentro do estacionamento.");
        }

        Parking parking = new Parking(idVehicle, gate);
        Long idParking = DaoFactory.entryParking(parking);

        DaoFactory.occupySlots(slot);
        DaoFactory.insertSlotOccupy(idParking, slot);

        if (category == VehicleCategory.AVULSO) {
            ticketEntry(DaoFactory.getParkingEntryById(idParking), slot);
        }
        System.out.println();
        System.out.println("Entrada do veículo com placa " + plate + " foi bem-sucedida.");
    }


    public void exitVehicle(String plate, int gate){
        Long vehicleId = DaoFactory.getVehicleIdByPlate(plate);
        Vehicle vehicle = DaoFactory.getVehicleDb(vehicleId);

        validateGateExit(gate,vehicle.getType());//fazer validacao


        Long idParking = DaoFactory.getIdParking(vehicleId);
        DaoFactory.updateParkingExit(vehicleId, gate);
        Integer[] slots = DaoFactory.checkAndDisassociateSlotsFromParking(idParking);
        DaoFactory.unoccupySlots(slots);

        if(vehicle.getCategory()==VehicleCategory.MENSALISTA || vehicle.getCategory() == VehicleCategory.PUBLICO || vehicle.getCategory() == VehicleCategory.CAMINHAO_ENTREGA){
            System.out.println("Veiculo Saiu.");
            return;
        }

        Parking parkingEntry = DaoFactory.getParkingEntryById(idParking);
        Parking parkingExit = DaoFactory.getParkingExitById(idParking);

        Double val = calculateValue(parkingEntry.getDate(), parkingExit.getDate());
        DaoFactory.updateParkingValue(idParking, val);

        ticketExit(parkingEntry, parkingExit, val);

        System.out.println("Veiculo Saiu.");
    }

    public Double calculateValue(Date dateEntry, Date dateExit){
        final double RATE_PER_MINUTE = 0.10;
        final double MINIMUM_FEE = 5.00;

        long differenceInMillis = dateExit.getTime() - dateEntry.getTime();
        long differenceInMinutes = TimeUnit.MILLISECONDS.toMinutes(differenceInMillis);

        double totalFee = differenceInMinutes * RATE_PER_MINUTE;

        return Math.max(totalFee, MINIMUM_FEE);
    }



    public boolean validateGateEntry(int gate, VehicleType type) {

        try {
            switch (type) {
                case CARRO, VEICULO_PUBLICO -> {
                    if (gate < 1 || gate > 5) {
                        throw new IllegalArgumentException("Esse veículo só pode entrar nas cancelas de 1 a 5.");
                    }
                }
                case MOTO -> {
                    if (gate != 5) {
                        throw new IllegalArgumentException("Esse veículo só pode entrar na cancela 5.");
                    }
                }
                case CAMINHAO, VAN -> {
                    if (gate != 1) {
                        throw new IllegalArgumentException("Esse veículo só pode entrar na cancela 1.");
                    }
                }
            }
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean validateGateExit(int gate, VehicleType type) {
        try {
            switch (type) {
                case CARRO, VEICULO_PUBLICO, CAMINHAO, VAN -> {
                    if (gate < 6 || gate > 10) {
                        throw new IllegalArgumentException("Esse veículo só pode sair pelas cancelas de 6 a 10.");
                    }
                }
                case MOTO -> {
                    if (gate != 10) {
                        throw new IllegalArgumentException("Esse veículo só pode sair pela cancela 10.");
                    }
                }
            }
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void ticketExit(Parking parking, Parking parkingExit, Double value){
        System.out.println("TICKET:");
        System.out.println();
        System.out.println("Data de entrada: " + parking.getFormattedDate());
        System.out.println("Horario da entrada: " + parking.getFormattedTime());
        System.out.println("Cancela em que entrou: " + parking.getIdGate());
        System.out.println();
        System.out.println("Data da saida: "+ parkingExit.getFormattedDate());
        System.out.println("Horario da saida: "+ parkingExit.getFormattedTime());
        System.out.println("Cancela que saiu: " + parkingExit.getIdGate());
        System.out.println("Valor que pagou: "+ value);
    }

    public void ticketEntry(Parking parking, Integer[] slot) {
        System.out.println("TICKET:");
        System.out.println();
        System.out.println("Data de entrada: " + parking.getFormattedDate());
        System.out.println("Horario da entrada: " + parking.getFormattedTime());
        System.out.println("Cancela em que entrou: " + parking.getIdGate());

        for (int i = 0; i < slot.length; i++) {
            System.out.println("Vaga " + (i + 1) + ": " + slot[i]);
        }
    }
}
