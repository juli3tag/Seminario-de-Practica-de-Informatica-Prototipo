-- mantis_pm_schema_3fn.sql
-- Esquema final normalizado hasta 3FN para MANTIS-PM
CREATE DATABASE IF NOT EXISTS mantis_pm CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE mantis_pm;

CREATE TABLE tipo_sensor (
  tipo_sensor_id INT AUTO_INCREMENT PRIMARY KEY,
  codigo VARCHAR(50) NOT NULL UNIQUE,
  descripcion VARCHAR(200) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE severidad_alerta (
  severidad_id INT AUTO_INCREMENT PRIMARY KEY,
  codigo VARCHAR(20) NOT NULL UNIQUE,
  descripcion VARCHAR(100) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE prioridad_ot (
  prioridad_id INT AUTO_INCREMENT PRIMARY KEY,
  codigo VARCHAR(20) NOT NULL UNIQUE,
  descripcion VARCHAR(100) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE estado_ot (
  estado_id INT AUTO_INCREMENT PRIMARY KEY,
  codigo VARCHAR(20) NOT NULL UNIQUE,
  descripcion VARCHAR(100) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE empresa (
  empresa_id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(150) NOT NULL,
  descripcion TEXT NULL,
  creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE planta (
  planta_id INT AUTO_INCREMENT PRIMARY KEY,
  empresa_id INT NOT NULL,
  nombre VARCHAR(150) NOT NULL,
  ubicacion VARCHAR(200) NULL,
  creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_planta_empresa FOREIGN KEY (empresa_id)
    REFERENCES empresa(empresa_id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE equipo (
  equipo_id INT AUTO_INCREMENT PRIMARY KEY,
  planta_id INT NOT NULL,
  tag VARCHAR(80) NOT NULL,
  modelo VARCHAR(100) NULL,
  ubicacion VARCHAR(150) NULL,
  creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT uq_equipo_planta_tag UNIQUE (planta_id, tag),
  CONSTRAINT fk_equipo_planta FOREIGN KEY (planta_id)
    REFERENCES planta(planta_id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE usuario (
  usuario_id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(150) NOT NULL,
  correo VARCHAR(200) UNIQUE,
  rol VARCHAR(50) NULL,
  clave_hash VARCHAR(255) NULL,
  creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE sensor (
  sensor_id INT AUTO_INCREMENT PRIMARY KEY,
  uid_sensor VARCHAR(150) NOT NULL UNIQUE,
  nombre VARCHAR(150) NULL,
  tipo_sensor_id INT NOT NULL,
  equipo_id INT NOT NULL,
  instalado_en TIMESTAMP NULL,
  referencia TEXT NULL,
  creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_sensor_tipo FOREIGN KEY (tipo_sensor_id)
    REFERENCES tipo_sensor(tipo_sensor_id) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_sensor_equipo FOREIGN KEY (equipo_id)
    REFERENCES equipo(equipo_id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE lectura (
  lectura_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  lectura_uid VARCHAR(150) UNIQUE,
  sensor_id INT NOT NULL,
  valor DOUBLE NOT NULL,
  unidad VARCHAR(30) NULL,
  registrado_en TIMESTAMP NOT NULL,
  ingestion_ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  procesada BOOLEAN DEFAULT FALSE,
  payload_raw JSON NULL,
  creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_lectura_sensor FOREIGN KEY (sensor_id)
    REFERENCES sensor(sensor_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE INDEX idx_lectura_sensor_tiempo ON lectura (sensor_id, registrado_en);
CREATE INDEX idx_lectura_ingestion ON lectura (ingestion_ts);

CREATE TABLE config_alerta (
  config_id INT AUTO_INCREMENT PRIMARY KEY,
  empresa_id INT NULL,
  planta_id INT NULL,
  equipo_id INT NULL,
  tipo_sensor_id INT NULL,
  umbral_alto DOUBLE NULL,
  umbral_bajo DOUBLE NULL,
  ventana_segundos INT NULL,
  auto_crear_ot BOOLEAN DEFAULT FALSE,
  prioridad_default_id INT NULL,
  activo BOOLEAN DEFAULT TRUE,
  creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_config_empresa FOREIGN KEY (empresa_id)
    REFERENCES empresa(empresa_id) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT fk_config_planta FOREIGN KEY (planta_id)
    REFERENCES planta(planta_id) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT fk_config_equipo FOREIGN KEY (equipo_id)
    REFERENCES equipo(equipo_id) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT fk_config_tipo_sensor FOREIGN KEY (tipo_sensor_id)
    REFERENCES tipo_sensor(tipo_sensor_id) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT fk_config_prioridad FOREIGN KEY (prioridad_default_id)
    REFERENCES prioridad_ot(prioridad_id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE alerta (
  alerta_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  lectura_id BIGINT NULL,
  severidad_id INT NULL,
  mensaje TEXT NULL,
  creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  creado_por INT NULL,
  procesado BOOLEAN DEFAULT FALSE,
  CONSTRAINT fk_alerta_lectura FOREIGN KEY (lectura_id)
    REFERENCES lectura(lectura_id) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT fk_alerta_severidad FOREIGN KEY (severidad_id)
    REFERENCES severidad_alerta(severidad_id) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT fk_alerta_creado_por FOREIGN KEY (creado_por)
    REFERENCES usuario(usuario_id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE orden_trabajo (
  orden_id INT AUTO_INCREMENT PRIMARY KEY,
  alerta_id BIGINT NULL,
  equipo_id INT NOT NULL,
  creado_por INT NULL,
  prioridad_id INT NULL,
  estado_id INT NULL,
  descripcion TEXT NULL,
  creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  asignado_a INT NULL,
  cerrado_en TIMESTAMP NULL,
  CONSTRAINT fk_ot_alerta FOREIGN KEY (alerta_id)
    REFERENCES alerta(alerta_id) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT fk_ot_equipo FOREIGN KEY (equipo_id)
    REFERENCES equipo(equipo_id) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_ot_creado_por FOREIGN KEY (creado_por)
    REFERENCES usuario(usuario_id) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT fk_ot_prioridad FOREIGN KEY (prioridad_id)
    REFERENCES prioridad_ot(prioridad_id) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT fk_ot_estado FOREIGN KEY (estado_id)
    REFERENCES estado_ot(estado_id) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT fk_ot_asignado FOREIGN KEY (asignado_a)
    REFERENCES usuario(usuario_id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE auditoria_evento (
  evento_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  entidad VARCHAR(100) NULL,
  entidad_id VARCHAR(100) NULL,
  tipo_evento VARCHAR(50) NULL,
  detalle JSON NULL,
  realizado_por INT NULL,
  creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_auditoria_realizado_por FOREIGN KEY (realizado_por)
    REFERENCES usuario(usuario_id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE OR REPLACE VIEW vw_alertas_recientes AS
SELECT a.alerta_id, a.lectura_id, a.severidad_id, s.uid_sensor, l.valor, a.mensaje, a.creado_en
FROM alerta a
LEFT JOIN lectura l ON a.lectura_id = l.lectura_id
LEFT JOIN sensor s ON l.sensor_id = s.sensor_id
ORDER BY a.creado_en DESC
LIMIT 100;

-- Seeds
INSERT INTO tipo_sensor (codigo, descripcion) VALUES
  ('VIB', 'Sensor de vibración'),
  ('TMP', 'Sensor de temperatura'),
  ('COR', 'Sensor de corriente'),
  ('HRS', 'Sensor de horas de operación');

INSERT INTO severidad_alerta (codigo, descripcion) VALUES
  ('MEDIA', 'Severidad media'),
  ('ALTA', 'Severidad alta'),
  ('CRITICA', 'Severidad crítica');

INSERT INTO prioridad_ot (codigo, descripcion) VALUES
  ('BAJA', 'Baja prioridad'),
  ('MEDIA', 'Prioridad media'),
  ('ALTA', 'Alta prioridad'),
  ('URG', 'Urgente');

INSERT INTO estado_ot (codigo, descripcion) VALUES
  ('ABIERTA', 'Abierta'),
  ('EN_PROGRESO', 'En progreso'),
  ('CERRADA', 'Cerrada'),
  ('CANCELADA', 'Cancelada');

INSERT INTO empresa (nombre, descripcion) VALUES ('El Pampeano SRL', 'Empresa metalúrgica piloto');
INSERT INTO planta (empresa_id, nombre, ubicacion) VALUES (1, 'Planta Principal', 'Ciudad A');
INSERT INTO equipo (planta_id, tag, modelo, ubicacion) VALUES (1, 'Torno-A', 'Model-X', 'Sector 1');
INSERT INTO usuario (nombre, correo, rol, clave_hash) VALUES ('Admin Mantis', 'admin@elpampeano.local', 'ADMIN', NULL);
INSERT INTO sensor (uid_sensor, nombre, tipo_sensor_id, equipo_id, referencia) 
  VALUES ('sensor-abc-001', 'Sensor Vibracion A', (SELECT tipo_sensor_id FROM tipo_sensor WHERE codigo='VIB'), 1, 'Eje principal');

INSERT INTO config_alerta (empresa_id, tipo_sensor_id, umbral_alto, umbral_bajo, ventana_segundos, auto_crear_ot, prioridad_default_id, activo)
VALUES
 (1, (SELECT tipo_sensor_id FROM tipo_sensor WHERE codigo='VIB'), 100.0, NULL, 60, TRUE, (SELECT prioridad_id FROM prioridad_ot WHERE codigo='ALTA'), TRUE),
 (1, (SELECT tipo_sensor_id FROM tipo_sensor WHERE codigo='TMP'), 85.0, NULL, 60, FALSE, (SELECT prioridad_id FROM prioridad_ot WHERE codigo='MEDIA'), TRUE);
