# 📅 Sistema de Gestión de Eventos

Un sistema de escritorio robusto y moderno desarrollado en **Java Swing**, diseñado para cubrir el ciclo de vida completo de la administración de eventos. Este proyecto permite planificar fechas, gestionar recursos y actividades, registrar asistentes con despacho automático de invitaciones por correo electrónico y exportar informes a Excel, todo bajo una arquitectura sólida **MVC (Modelo-Vista-Controlador)** y persistencia de datos mediante **SQLite**.

---

## 🚀 Características Principales

*   **Persistencia Local Autónoma:** Base de datos **SQLite** embebida (`gestion_eventos.db`), sin necesidad de servidores externos como XAMPP o MySQL.
*   **Interfaz Gráfica Moderna:** UI estilizada con **FlatLaf (Dark Theme)**, bordes redondeados y tipografía moderna que optimiza la experiencia de usuario.
*   **Arquitectura MVC Estricta:** Separación absoluta entre la lógica de negocio (`model/`), la interfaz gráfica (`view/`) y el flujo de control (`controller/`).
*   **Seguridad Criptográfica:** Hashing de contraseñas mediante **SHA-256 con Salt aleatorio de 16 bytes** (`SecurityUtil`), impidiendo el almacenamiento de claves en texto plano.
*   **Control de Accesos por Roles:** Tres niveles de privilegios claramente delimitados: **Administrador**, **Operador** e **Invitado**.
*   **Notificaciones por Correo Electrónico:** Despacho asíncrono en segundo plano (`Jakarta Mail`) con plantilla HTML responsiva al registrar participantes.
*   **Exportación Avanzada a Excel y CSV:** Exportación nativa a Libro de Excel (`.xls`) con diseño estilizado y columnas preconfiguradas, compatible con Microsoft Excel y WPS Office.
*   **Empaquetado Nativo Windows:** Generación de ejecutable `.exe` independiente y portable (`jpackage`).

---

## 🛠️ Tecnologías Utilizadas

*   **Lenguaje:** Java (JDK 17+)
*   **Gestor de Dependencias:** Maven
*   **Interfaz de Usuario:** Java Swing con temas modernos FlatLaf (v3.4.1)
*   **Base de Datos:** SQLite (v3.45.2.0)
*   **Servicio de Correo:** Jakarta Mail (v2.0.1)
*   **Selector de Fechas y Horas:** LGoodDatePicker (v11.2.1)
*   **Pruebas Unitarias:** JUnit 5 (Jupiter v5.10.2)

---

## 🧱 Arquitectura del Sistema (MVC)

El proyecto está estructurado rigurosamente en capas:

1.  **Modelo (`model/`):**
    *   **Entidades:** `Evento`, `Usuario`, `Participante`, `Actividad`, `Recurso`, `Notificacion`.
    *   **Repositorios (`*Repository`):** Ejecutan operaciones SQL parametrizadas para evitar inyecciones SQL. Propagan excepciones de base de datos para garantizar la integridad transaccional.
    *   **Servicios (`*Service`):** Lógica de negocio y validación de reglas de datos (ej. verificación de campos obligatorios, fechas y horas no nulas).
    *   **EmailService:** Gestión asíncrona de envío de correos SMTP en segundo plano (`CompletableFuture`).
2.  **Vista (`view/`):**
    *   Componentes visuales puros (`JFrame`, `JPanel`, `JButton`, `JTextField`). No contienen lógica de negocio ni consultas a base de datos.
3.  **Controlador (`controller/`):**
    *   Manejan eventos de usuario, coordinan la comunicación entre vistas y servicios, y aplican las restricciones de seguridad por rol.
4.  **Configuración (`config/`):**
    *   `DatabaseConnection.java`: Singleton para la conexión JDBC a SQLite y autogeneración de esquema (DDL).
    *   `EmailConfig.java`: Gestor de credenciales y propiedades del servidor SMTP (`email_config.properties`).
    *   `SecurityUtil.java`: Generador y verificador de hashes SHA-256 con sal criptográfica.

---

## 🧩 Módulos del Sistema

El sistema cuenta con **8 módulos funcionales** accesibles desde la barra lateral del Dashboard:

### 1. 🏠 Inicio (Dashboard Principal)
*   Resumen de bienvenida interactivo. Muestra el usuario activo, su rol y métricas rápidas.

### 2. 📅 Gestión de Eventos
*   **Crear Evento:** Formulario validado para registrar eventos (Nombre, Descripción, Tipo, Lugar, Fecha y Hora).
*   **Consultar / Modificar:** Búsqueda por ID. Permite actualizar datos y cambiar estados ("Programado", "En Curso", "Finalizado"). La eliminación y el cambio de estado están reservados para el Administrador.
*   **Listar Eventos:** Vista tabular general de todos los eventos.

### 3. 🗓️ Calendario
*   Cuadrícula mensual interactiva que posiciona los eventos en sus respectivos días con códigos de color según su estado.
*   Panel lateral con indicador del próximo evento programado.

### 4. 👥 Participantes
*   Registro de asistentes vinculados a un evento.
*   **Envío automático de invitación:** Al registrar un participante, el sistema despacha una confirmación digital por correo electrónico.
*   **Exportación a Excel / CSV:** Permite descargar el listado de participantes en formato `.xls` estilizado o `.csv` estándar.

### 5. 📦 Recursos y Actividades
*   **Actividades:** Cronograma interno del evento (Nombre, Horario, Responsable).
*   **Recursos:** Inventario logístico requerido (Tipo, Cantidad, Evento asignado).

### 6. 📊 Informes (Dashboard Estadístico)
*   Métricas en tiempo real: total de eventos, eventos finalizados, en proceso y programados.
*   Barras de progreso visuales con distribución porcentual por tipo y estado.

### 7. 🔔 Notificaciones
*   Buzón de auditoría interna del sistema. Registra eventos creados, actualizaciones de estado y despachos de correo.

### 8. ⚙️ Configuración
*   Personalización de tema visual (Modo Oscuro / Claro).
*   Cambio de contraseña del usuario actual con validación de fortaleza.
*   **Configuración SMTP:** Ajuste de remitente y contraseña de aplicación para envío real de correos.
*   **Mantenimiento:** Copias de seguridad completas de la base de datos SQLite (`.db`) y restauración.

---

## 🔐 Matriz de Roles y Permisos

| Módulo / Acción | Administrador | Operador | Invitado |
| :--- | :---: | :---: | :---: |
| **Inicio y Navegación** | ✅ Total | ✅ Total | ✅ Consulta Pública |
| **Consultar Calendario y Eventos** | ✅ Total | ✅ Total | ✅ Solo Lectura |
| **Crear y Actualizar Eventos** | ✅ Total | ✅ Total | ❌ Requiere Login |
| **Cambiar Estado de Eventos** | ✅ Total | ❌ Bloqueado | ❌ Requiere Login |
| **Eliminar Eventos** | ✅ Total | ❌ Bloqueado | ❌ Requiere Login |
| **Gestionar Participantes (con Email)** | ✅ Total | ✅ Total | ❌ Requiere Login |
| **Exportar a Excel / CSV** | ✅ Total | ✅ Total | ❌ Requiere Login |
| **Gestionar Recursos y Actividades** | ✅ Total | ✅ Total | ❌ Requiere Login |
| **Ver Informes Estadísticos** | ✅ Total | ✅ Total | ❌ Requiere Login |
| **Configuración del Servidor SMTP** | ✅ Total | ❌ Bloqueado | ❌ Requiere Login |
| **Respaldos y Restauración de BD** | ✅ Total | ❌ Bloqueado | ❌ Requiere Login |

---

## 🚪 Flujo de Inicio y Credenciales

1.  **Arranque Predeterminado:** La aplicación se abre directamente en el **Dashboard en modo Invitado**.
2.  **Iniciar Sesión:** Para desbloquear las funciones operativas o administrativas, haz clic en el botón **`🚪 Iniciar Sesión`** ubicado al final de la barra lateral izquierda.
3.  **Credenciales Iniciales Precargadas:**
    *   **Administrador (Super-Admin):**
        *   **Correo:** `admin@admin.com`
        *   **Contraseña:** `admin123`
    *   **Operador:**
        *   **Correo:** `operador@gmail.com`
        *   **Contraseña:** `operador123`
4.  **Seguridad en el Auto-registro:**
    *   **Registro de Operadores:** Abierto para colaboradores; requiere correo con terminación `@gmail.com` y contraseña con requisitos de seguridad.
    *   **Registro de Administradores:** Protegido contra escalamiento de privilegios. Requiere obligatoriamente la **Clave Maestra de Autorización del Sistema** (`ADMIN2026*`) y correo institucional `@admin.com`.

---

## ⚙️ Guía de Ejecución

### Opción 1: Ejecutable Portátil Windows (Sin instalar Java ni Maven)
1. Navega a la carpeta `dist/SistemaGestionEventos/`.
2. Ejecuta **`SistemaGestionEventos.exe`**.
3. *(Opcional)* Puedes distribuir el archivo comprimido `dist/SistemaGestionEventos_Portable.zip`.

### Opción 2: Desde el Código Fuente con Maven
1. Abre una terminal en la raíz del proyecto.
2. Ejecutar las pruebas unitarias:
   ```bash
   mvn test
   ```
3. Compilar y empaquetar el proyecto:
   ```bash
   mvn clean package
   ```
4. Ejecutar la aplicación:
   ```bash
   java -jar target/SistemaGestionEventos-1.0-SNAPSHOT-jar-with-dependencies.jar
   ```

---
*Documentación oficial actualizada para el Sistema de Gestión de Eventos.*
