# MANTIS-PM (Prototipo) - Proyecto completo

Contenido:
- Código fuente Java en `src/org/byjuju/mantispm/...`
- Script SQL `MantisBD.sql` para crear la base de datos y seeds (MySQL)
- Estructura: modelos, DAOs, servicios, vista (CLI) y main.

## Requisitos
- Java JDK 11+ instalado
- MySQL 5.7+ o 8.x (compatible con JSON)
- MySQL Workbench (opcional, para ejecutar el script SQL)
- (Opcional) Maven para manejar dependencias
- MySQL Connector/J (si compilas sin Maven): `mysql-connector-java-X.Y.Z.jar`

## Instrucciones rápidas (sin Maven)
1. Crear la base de datos:
   - Abrir MySQL Workbench o consola mysql.
   - Ejecutar el script `MantisBD.sql` (archivo incluido) para crear la base y datos iniciales.

2. Ajustar credenciales:
   - Editar `src/org/byjuju/mantispm/dao/ConexionBD.java` si tus credenciales difieren (usuario/clave/host).

3. Compilar:
   - Descargar el driver JDBC (mysql-connector-java).
   - Compilar desde la raíz del proyecto:
     ```
     javac -cp ".:path/to/mysql-connector-java.jar" -d out $(find src -name "*.java")
     ```
     En Windows usa `;` en lugar de `:` en el classpath.

4. Ejecutar:
   - Lanzar la aplicación:
     ```
     java -cp "out:./path/to/mysql-connector-java.jar" org.byjuju.mantispm.principal.Main
     ```

## Instrucciones con Maven (recomendado)
1. Crear un `pom.xml` con dependencia a `mysql-connector-java`.
2. Ejecutar:
   ```
   mvn compile
   mvn exec:java -Dexec.mainClass="org.byjuju.mantispm.principal.Main"
   ```

## Qué probar
- Opción 1 del menú: registrar lecturas manualmente.
- Opción 2: demo de inserción y procesamiento (inserta lectura y procesa alertas).
- Opción 3: ejecutar procesador manualmente.
- Revisar tablas: `SELECT * FROM lectura ORDER BY registrado_en DESC LIMIT 20;`

## Notas
- La conexión por defecto usa usuario `root` y password `hola`. Cambialo en `ConexionBD.java`.
- El prototipo usa JDBC directo y DAOs; es sencillo de entender y está preparado para migrar a un framework (Spring/Hibernate) si se desea.
- Para pruebas intensivas con muchas lecturas, considerar particionado o particionado por fecha en `lectura`.

