# 🔄 Gestor de Copias de Seguridad FTP con Java 🔐

## 📜 Descripción
Este proyecto implementa un **sistema de copias de seguridad automatizadas** utilizando **Java y FTP**, asegurando que:
- 📤 **Los archivos nuevos o modificados en la carpeta local se cifran y suben al servidor FTP.**
- 🗑 **Los archivos eliminados en la carpeta local se mueven a una carpeta `history/` en el servidor FTP en lugar de ser eliminados.**
- 📥 **Los archivos pueden descargarse y descifrarse automáticamente desde el servidor FTP.**
- 🕒 **Cada versión modificada de un archivo se almacena en `history/`, manteniendo un registro histórico.**

Además, el sistema **optimiza la sincronización utilizando hilos** para mejorar el rendimiento y la velocidad de ejecución.  

---

### 🎥 **Demo y Explicación en Video**
📺 **Mira la explicación completa del proyecto en YouTube:**  
[🔗 Enlace al Video](https://youtu.be/jbk6G0VsXVk) 

---

## 🎯 **Objetivos**
✅ **Sincronización de archivos** entre una carpeta local y un servidor FTP.  
✅ **Cifrado de archivos con AES** antes de su subida al servidor.  
✅ **Gestión eficiente de la sincronización usando hilos** para mejorar el rendimiento.  
✅ **Mantenimiento de un historial** de versiones anteriores de los archivos.  
✅ **Descarga y descifrado de archivos y sus versiones previas.**  

---

## 🛠 **Tecnologías Utilizadas**
- ☕ **Java** (Gestión de archivos, hilos y cifrado)
- 📡 **Apache Commons Net** (Para conexión y operaciones FTP)
- 🔐 **AES (Advanced Encryption Standard)** (Protección de archivos antes de la subida)
- 🏗 **Programación Multihilo** (Mejora del rendimiento y eficiencia)

---

## ⚙️ **Instalación y Configuración**

### 🔽 **1. Clona el repositorio**
```sh
git clone https://github.com/jrs0407/ProyectUT4-5.git
```

### 💾 **2. Configura el Servidor FTP**
1. **Instala FileZilla Server** o cualquier otro servidor FTP compatible.
2. **Crea una carpeta en el servidor FTP**, por ejemplo:  
   - `C:/CarpetaFTP/`
3. **Configura un usuario y contraseña de acceso en el FTP**:
   - En FileZilla Server, ve a *Edit > Users* y agrega un usuario.
   - Asigna la carpeta `CarpetaFTP` como su directorio principal.
   - Habilita todos los permisos para permitir la sincronización.
     

### 🔧 **3. Configura las Credenciales FTP en el Código**
En las clases `SubirArchivoHilo.java`, `BorrarArchivoHilo.java` y `DescargarArchivoFTP.java`, **modifica las credenciales de conexión**:

```java
private static final String FTP_SERVER = "localhost";  // Cambia si usas un servidor externo
private static final int FTP_PORT = 21;               // Puerto FTP (por defecto es 21)
private static final String FTP_USER = "usuario";     // Usuario configurado en el servidor FTP
private static final String FTP_PASS = "contraseña";  // Contraseña del usuario FTP
```

### 🚀 **4. Ejecuta el Sincronizador**
#### **Compila los archivos Java:**
Ejecuta la clase `SincronizadorFTP.java`

### 🚀 **5. Ejecuta el Sincronizador**
#### **Descargar y Recuperar Archivos:**
Ejecuta la clase `DescargarArchivoFTP.java`

---

### 🎯 **📌 Ejemplo de Uso**

#### 🏗 **1. Subida de Archivos**
- Crea o modifica un archivo en `C:/CarpetaLocal/`.
- El sistema lo **cifra** y lo sube al FTP automáticamente.
- Si ya existe, la versión anterior se **mueve a `history/`** en el FTP.

#### 🗑 **2. Eliminación de Archivos**
- Borra un archivo de `C:/CarpetaLocal/`.
- En el FTP, el archivo **NO se elimina**, sino que se **mueve a `history/` con un sufijo `_deleted`**.

#### 📥 **3. Descarga de Archivos**
- Ejecuta `DescargarArchivoFTP.java`.
- Escribe el nombre del archivo a descargar.
- Opción de descargar **todas las versiones anteriores**.
- El archivo se **descifra automáticamente** y se guarda en `C:/CarpetaDescargas/`.

---

### 📌 **Propuestas de Mejora** 
🔹 **Crear una interfaz gráfica para gestionar la sincronización.**  
🔹 **Integración con servicios en la nube (Google Drive, Dropbox).**  
