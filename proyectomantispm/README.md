# MANTIS-PM (Prototipo) - Proyecto completo
### 📘 Descripción general del proyecto

MANTIS-PM es un prototipo operacional desarrollado en Java para el monitoreo de sensores, la detección de condiciones anómalas y la gestión de órdenes de trabajo de mantenimiento.
El sistema integra captura de datos, aplicación de reglas configurables, persistencia en una base de datos MySQL y una interfaz de usuario por consola.

Este repositorio contiene la versión presentada en el cuarto trabajo práctico (AP4), donde se consolidan los avances realizados en entregas anteriores e incorpora los requisitos específicos del módulo.

El objetivo del proyecto es demostrar el diseño e implementación de un sistema informático realista, empleando buenas prácticas de programación orientada a objetos, arquitectura por capas, patrón DAO y persistencia con MySQL.

### 📂 Contenido del repositorio

- src/org/byjuju/mantispm/

Código fuente completo organizado en paquetes:

- model → entidades de dominio
- dao → acceso a datos (patrón DAO)
- servicio → lógica de negocio y reglas
- vista → interfaz por consola (CLI)
- principal → punto de entrada (Main)

- MantisBD.sql - Script SQL para crear la base de datos, tablas, relaciones y datos iniciales.

### 🧱 Arquitectura general

El prototipo adopta una arquitectura en capas, compuesta por:

1. Capa de dominio (Modelos). Representa las entidades principales: sensores, lecturas, alertas, órdenes de trabajo, configuraciones, etc.

2. Capa de acceso a datos (DAO). Implementa conexión a MySQL, consultas, inserciones y actualizaciones. Se utiliza JDBC, manejo adecuado de excepciones y separación clara entre lógica de negocio y persistencia.

3. Capa de servicios. Contiene las reglas que procesan lecturas, generan alertas y crean órdenes de trabajo cuando corresponde.

4. Capa de presentación (CLI). Un menú interactivo que permite operar el sistema de forma simple y verificar los casos de uso principales.

### 🧪 Funcionalidades disponibles

El prototipo permite verificar los principales casos de uso definidos en el análisis:

- Registrar lecturas manuales desde la interfaz CLI.
- Procesar lecturas pendientes y aplicar reglas configuradas.
- Generar alertas automáticas según umbrales.
- Crear órdenes de trabajo (manuales o automáticas).
- Consultar historial de lecturas, alertas y órdenes.
