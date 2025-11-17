package org.byjuju.mantispm.principal;

import java.sql.Timestamp;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.byjuju.mantispm.dao.AlertaDAO;
import org.byjuju.mantispm.dao.ConfigAlertaDAO;
import org.byjuju.mantispm.dao.LecturaDAO;
import org.byjuju.mantispm.dao.OrdenTrabajoDAO;
import org.byjuju.mantispm.dao.SensorDAO;
import org.byjuju.mantispm.modelo.Lectura;
import org.byjuju.mantispm.servicio.ProcesadorAlertas;
import org.byjuju.mantispm.servicio.ServicioMantenimiento;
import org.byjuju.mantispm.vista.InterfazCLI;

/**
 * Clase main para ejecutar la aplicación en modo demo / CLI
 */
public class Main {
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public static void main(String[] args) {
        // Instanciar DAOs UNA VEZ
        SensorDAO sensorDAO = new SensorDAO();
        LecturaDAO lecturaDAO = new LecturaDAO();
        AlertaDAO alertaDAO = new AlertaDAO();
        OrdenTrabajoDAO ordenDAO = new OrdenTrabajoDAO();
        ConfigAlertaDAO configDAO = new ConfigAlertaDAO();

        // Servicios
        ServicioMantenimiento servicio = new ServicioMantenimiento(lecturaDAO);
        ProcesadorAlertas procesador = new ProcesadorAlertas(lecturaDAO, configDAO, alertaDAO, ordenDAO);

        // Vista (CLI)
        InterfazCLI cli = new InterfazCLI(servicio, lecturaDAO);

        Scanner sc = new Scanner(System.in);
        boolean running = true;
        boolean schedulerRunning = false;

        while (running) {
            printMenu();
            String opt = sc.nextLine().trim();
            switch (opt) {
                case "1":
                    System.out.println("Iniciando Interfaz CLI...");
                    try {
                        cli.iniciar();
                    } catch (Exception e) {
                        System.err.println("Error en CLI: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;

                case "2":
                    runInsercionYProcesamientoDemo(sc, lecturaDAO, procesador);
                    break;

                case "3":
                    System.out.println("Ejecutando procesador de alertas (una vez)...");
                    try {
                        procesador.procesarLecturasNoProcesadas();
                        System.out.println("Procesamiento finalizado.");
                    } catch (Exception ex) {
                        System.err.println("Error ejecutando procesador: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                    break;

                case "4":
                    if (!schedulerRunning) {
                        System.out.print("Intervalo de ejecución en segundos (ej: 60): ");
                        String sInterval = sc.nextLine().trim();
                        int interval = 60;
                        try { interval = Integer.parseInt(sInterval); } catch (NumberFormatException nfe) { /* usar default */ }
                        scheduler.scheduleAtFixedRate(() -> {
                            try {
                                System.out.println("[Scheduler] Ejecutando procesador...");
                                procesador.procesarLecturasNoProcesadas();
                            } catch (Exception e) {
                                System.err.println("[Scheduler] Error en procesador: " + e.getMessage());
                                e.printStackTrace();
                            }
                        }, 0, interval, TimeUnit.SECONDS);
                        schedulerRunning = true;
                        System.out.println("Scheduler iniciado cada " + interval + " segundos.");
                    } else {
                        System.out.println("Scheduler ya está corriendo.");
                    }
                    break;

                case "5":
                    if (schedulerRunning) {
                        scheduler.shutdownNow();
                        try {
                            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                                System.err.println("Scheduler no terminó en tiempo, forzando shutdown.");
                            }
                        } catch (InterruptedException ie) { /* ignore */ }
                        System.out.println("Scheduler detenido.");
                        schedulerRunning = false;
                    } else {
                        System.out.println("Scheduler no está corriendo.");
                    }
                    break;

                case "6":
                    printEstadoYAyuda();
                    break;

                case "0":
                    System.out.println("Saliendo... cerrando recursos.");
                    running = false;
                    break;

                default:
                    System.out.println("Opción inválida. Elegí un número del menú.");
            }
        }

        if (!scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
        sc.close();
        System.out.println("Aplicación finalizada. ¡Listo!");
    }

    private static void printMenu() {
        System.out.println("\n=== MANTIS-PM - Consola Unificada ===");
        System.out.println("1) Iniciar Interfaz CLI (modo interactivo)");
        System.out.println("2) Inserción de lectura de prueba y procesar (demo)");
        System.out.println("3) Ejecutar procesador de alertas (una vez)");
        System.out.println("4) Iniciar scheduler del procesador (periodic)");
        System.out.println("5) Detener scheduler / Estado / Queries útiles");
        System.out.println("6) Mostrar queries de verificación / ayuda");
        System.out.println("0) Salir");
        System.out.print("Elegí una opción: ");
    }

    private static void runInsercionYProcesamientoDemo(Scanner sc, org.byjuju.mantispm.dao.LecturaDAO lecturaDAO, ProcesadorAlertas procesador) {
        try {
            System.out.println("=== Inserción de lectura de prueba ===");
            System.out.print("SensorId (ej: 1): ");
            String sSensor = sc.nextLine().trim();
            int sensorId = 1;
            try { sensorId = Integer.parseInt(sSensor); } catch (NumberFormatException nfe) { /* default 1 */ }

            System.out.print("Valor de prueba (ej: 999.0): ");
            String sValor = sc.nextLine().trim();
            double valorPrueba = 999.0;
            try { valorPrueba = Double.parseDouble(sValor); } catch (NumberFormatException nfe) { /* default 999 */ }

            System.out.print("Unidad (ej: °C): ");
            String unidad = sc.nextLine().trim();
            if (unidad.isEmpty()) unidad = "u";

            String lecturaUid = "demo-uid-" + System.currentTimeMillis();
            Lectura l = new Lectura();
            l.setLecturaUid(lecturaUid);
            l.setSensorId(sensorId);
            l.setValor(valorPrueba);
            l.setUnidad(unidad);
            l.setRegistradoEn(new Timestamp(System.currentTimeMillis()));
            l.setProcesada(false);
            l.setPayloadRaw(null);

            lecturaDAO.insertar(l);
            System.out.println("Lectura insertada con lectura_id = " + l.getLecturaId() + " y lectura_uid = " + lecturaUid);

            System.out.println("=== Ejecutando procesador de alertas ===");
            procesador.procesarLecturasNoProcesadas();
            System.out.println("=== Procesamiento finalizado ===");
            System.out.println("Verificá en la BD la alerta/OT para lectura_uid = " + lecturaUid);
        } catch (java.sql.SQLIntegrityConstraintViolationException dup) {
            System.err.println("LECTURA DUPLICADA: ya existe una lectura con ese lectura_uid.");
        } catch (Exception ex) {
            System.err.println("Error durante demo inserción/procesamiento: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static void printEstadoYAyuda() {
        System.out.println("\n--- Queries útiles para verificación (MySQL Workbench) ---");
        System.out.println("Ver lecturas recientes:");
        System.out.println("SELECT * FROM lectura ORDER BY registrado_en DESC LIMIT 20;");
        System.out.println("Ver alertas recientes:");
        System.out.println("SELECT * FROM alerta ORDER BY creado_en DESC LIMIT 20;");
        System.out.println("Ver ordenes de trabajo recientes:");
        System.out.println("SELECT * FROM orden_trabajo ORDER BY creado_en DESC LIMIT 20;");
        System.out.println("Buscar por lectura_uid:");
        System.out.println("SELECT * FROM lectura WHERE lectura_uid = 'demo-uid-xxxxx';");
        System.out.println("---------------------------------------------------------\n");
    }
}
