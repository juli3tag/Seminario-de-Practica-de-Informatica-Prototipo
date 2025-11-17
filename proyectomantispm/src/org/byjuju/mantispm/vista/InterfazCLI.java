package org.byjuju.mantispm.vista;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Scanner;

import org.byjuju.mantispm.dao.LecturaDAO;
import org.byjuju.mantispm.servicio.ServicioMantenimiento;

/**
 * Interfaz de consola simple para probar funcionalidades.
 */
public class InterfazCLI {

    private final ServicioMantenimiento servicio;
    private final LecturaDAO lecturaDAO;

    public InterfazCLI(ServicioMantenimiento servicio, LecturaDAO lecturaDAO) {
        this.servicio = servicio;
        this.lecturaDAO = lecturaDAO;
    }

    public void iniciar() {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== MANTIS-PM CLI ===");
        while (true) {
            System.out.println("1) Registrar lectura  2) Listar lecturas recientes  0) Salir");
            String opt = sc.nextLine();
            if ("0".equals(opt)) break;
            try {
                if ("1".equals(opt)) {
                    System.out.print("lectura_uid: ");
                    String uid = sc.nextLine();
                    System.out.print("sensor_id: ");
                    int sensorId = Integer.parseInt(sc.nextLine());
                    System.out.print("valor (double): ");
                    double valor = Double.parseDouble(sc.nextLine());
                    System.out.print("unidad: ");
                    String unidad = sc.nextLine();
                    servicio.registrarLectura(uid, sensorId, valor, unidad, Timestamp.from(Instant.now()), null);
                } else if ("2".equals(opt)) {
                    lecturaDAO.listarTodos().forEach(System.out::println);
                } else {
                    System.out.println("Opción inválida");
                }
            } catch (Exception ex) {
                System.err.println("Error: " + ex.getMessage());
            }
        }
        sc.close();
        System.out.println("CLI finalizada.");
    }
}
