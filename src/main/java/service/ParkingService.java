package service;


import model.dao.DaoFactory;
import model.dao.VehicleDAO;
import model.entities.Parking;
import model.entities.Vehicle;
import model.enums.VehicleCategory;
import model.enums.VehicleType;


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
        return false;
    }

    public void ticketEntry(Parking parking, Integer[] slot) {
        System.out.println("TICKET:");
        System.out.println();
        System.out.println("Data de entrada: " + parking.getFormattedDate());
        System.out.println("Horario da entrada: " + parking.getFormattedTime());
        System.out.println("Cancela em que entrou: " + parking.getIdGate());
    }
}
