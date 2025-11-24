# 📥 Manual de Instalación - Explorador del Cosmos

Esta guía te ayudará a instalar y configurar **Explorador del Cosmos** en tu sistema paso a paso.

## 📋 Requisitos Previos

Antes de comenzar con la instalación, asegúrate de contar con lo siguiente:

### Requisitos de Hardware

- **Procesador**: Intel Core i3 o equivalente (mínimo)
- **RAM**: 4 GB mínimo, 8 GB recomendado
- **Espacio en Disco**: Al menos 500 MB libres
- **Resolución de Pantalla**: Mínimo 1280x720 píxeles

### Requisitos de Software

- **Sistema Operativo**: Windows 10/11, macOS 10.15+, o Linux (Ubuntu 20.04+)
- **Java Development Kit (JDK)**: Versión 17 o superior
- **Conexión a Internet**: Requerida para:
  - Descargar dependencias durante el build
  - Acceder a la API de NASA
  - Cargar imágenes y videos

## ☕ Instalación de Java

### Windows

1. **Descargar el JDK**
   - Visita [https://adoptium.net/](https://adoptium.net/)
   - Descarga **Eclipse Temurin JDK 17** para Windows (archivo `.msi`)

2. **Instalar el JDK**
   - Ejecuta el archivo descargado
   - Sigue el asistente de instalación
   - Marca la opción "Set JAVA_HOME variable" durante la instalación

3. **Verificar la instalación**
   ```powershell
   java -version
   ```
   Deberías ver algo como:
   ```
   openjdk version "17.0.x" 2023-xx-xx
   ```

### macOS

1. **Usando Homebrew** (recomendado)
   ```bash
   brew install openjdk@17
   ```

2. **O descarga manual**
   - Visita [https://adoptium.net/](https://adoptium.net/)
   - Descarga Eclipse Temurin JDK 17 para macOS
   - Instala el archivo `.pkg`

3. **Verificar la instalación**
   ```bash
   java -version
   ```

### Linux (Ubuntu/Debian)

```bash
# Actualizar repositorios
sudo apt update

# Instalar OpenJDK 17
sudo apt install openjdk-17-jdk

# Verificar instalación
java -version
```

## 📦 Descarga del Proyecto

### Opción 1: Clonar con Git (Recomendado)

Si tienes Git instalado:

```bash
# Clonar el repositorio
git clone https://github.com/nicojson/ExploradorCosmos.git

# Entrar al directorio
cd ExploradorCosmos
```

### Opción 2: Descarga Directa

1. Ve a [https://github.com/nicojson/ExploradorCosmos](https://github.com/nicojson/ExploradorCosmos)
2. Haz clic en el botón verde **"Code"**
3. Selecciona **"Download ZIP"**
4. Extrae el archivo ZIP en la ubicación deseada
5. Abre una terminal en el directorio extraído

## 🔨 Compilación del Proyecto

El proyecto utiliza **Gradle** con wrapper incluido, por lo que no necesitas instalar Gradle por separado.

### Windows

```powershell
# Dar permisos de ejecución (si es necesario)
# En PowerShell como administrador:
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser

# Compilar el proyecto
.\gradlew.bat build

# Si encuentras errores, intenta limpiar primero
.\gradlew.bat clean build
```

### macOS/Linux

```bash
# Dar permisos de ejecución
chmod +x gradlew

# Compilar el proyecto
./gradlew build

# Si encuentras errores, intenta limpiar primero
./gradlew clean build
```

> **⏱️ Nota**: La primera compilación puede tardar varios minutos, ya que Gradle descargará todas las dependencias necesarias (JavaFX, SQLite, Retrofit, etc.).

## 🚀 Ejecución de la Aplicación

Una vez compilado el proyecto exitosamente:

### Windows

```powershell
.\gradlew.bat run
```

### macOS/Linux

```bash
./gradlew run
```

La aplicación se iniciará y verás la pantalla de inicio de sesión.

## 🗄️ Configuración de la Base de Datos

La aplicación crea automáticamente la base de datos SQLite la primera vez que se ejecuta:

- **Ubicación**: `explorador_del_cosmos.db` en el directorio raíz del proyecto
- **Tablas creadas automáticamente**:
  - `users` - Usuarios del sistema
  - `publications` - Publicaciones guardadas
  - `favorites` - Favoritos de cada usuario
  - `app_config` - Configuración de la aplicación

> **✅ No necesitas hacer nada manualmente**. La aplicación se encarga de toda la configuración inicial.

## 👤 Usuario Administrador Inicial

Al ejecutar la aplicación por primera vez, se crea automáticamente un usuario administrador:

- **Usuario**: (se define durante el primer setup)
- **Contraseña**: (se define durante el primer setup)

> **🔒 Importante**: Cambia la contraseña del administrador después del primer inicio de sesión por seguridad.

## ⚙️ Configuración de la API de NASA

La aplicación utiliza la API pública de NASA, que no requiere una clave API para uso básico. Sin embargo, puedes obtener una clave personal para evitar límites de velocidad:

1. **Obtener una API Key** (opcional pero recomendado)
   - Visita [https://api.nasa.gov/](https://api.nasa.gov/)
   - Completa el formulario para obtener tu clave gratuita
   - La recibirás por correo electrónico inmediatamente

2. **Configurar la API Key en la aplicación**
   - Inicia sesión como administrador
   - Ve al menú de configuración
   - Ingresa tu API Key de NASA

> **📝 Nota**: Sin una API Key personal, la aplicación utilizará la clave demo (`DEMO_KEY`), que tiene un límite de 30 solicitudes por hora por dirección IP.

## 🛠️ Solución de Problemas Comunes

### Error: "JAVA_HOME is not set"

**Solución en Windows**:
```powershell
# Verificar dónde está instalado Java
where java

# Establecer JAVA_HOME (ajusta la ruta según tu instalación)
setx JAVA_HOME "C:\Program Files\Eclipse Adoptium\jdk-17.x.x.x-hotspot"
```

**Solución en macOS/Linux**:
```bash
# Agregar al archivo ~/.bashrc o ~/.zshrc
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
```

### Error al compilar: "Could not resolve dependencies"

**Causa**: Problemas de conexión a internet o repositorios Maven.

**Solución**:
```bash
# Limpiar caché de Gradle
./gradlew clean --refresh-dependencies

# Intentar compilar nuevamente
./gradlew build
```

### Error: "JavaFX runtime components are missing"

**Causa**: JavaFX no se descargó correctamente.

**Solución**:
```bash
# Limpiar y reconstruir
./gradlew clean
./gradlew build --refresh-dependencies
```

### La aplicación se cierra inmediatamente

**Posibles causas**:
1. Java no está instalado correctamente
2. Versión incorrecta de Java (menor a 17)

**Solución**:
```bash
# Verificar versión de Java
java -version

# Debe mostrar versión 17 o superior
# Si no, reinstala Java siguiendo la sección "Instalación de Java"
```

### Error de base de datos al iniciar

**Solución**:
1. Cierra la aplicación completamente
2. Elimina los archivos de base de datos:
   - `explorador_del_cosmos.db`
   - `explorador_del_cosmos.db-shm`
   - `explorador_del_cosmos.db-wal`
3. Vuelve a ejecutar la aplicación

> **⚠️ Advertencia**: Esto eliminará todos los datos guardados (usuarios, favoritos, etc.).

### Problemas de conexión con la API de NASA

**Verificaciones**:
1. Asegúrate de tener conexión a internet
2. Verifica que no haya un firewall bloqueando la aplicación
3. Intenta acceder a [https://images.nasa.gov/](https://images.nasa.gov/) en tu navegador

## 📦 Creación de un Ejecutable (Opcional)

Si deseas crear un ejecutable standalone:

```bash
# Crear distribución con jlink
./gradlew jlink

# El ejecutable se creará en:
# build/distributions/app-{platform}.zip
```

Extrae el archivo ZIP y ejecuta el binario `app` dentro de la carpeta `bin`.

## 🔄 Actualización de la Aplicación

Para actualizar a una versión más reciente:

1. **Con Git**:
   ```bash
   git pull origin main
   ./gradlew clean build
   ```

2. **Sin Git**:
   - Descarga la nueva versión desde GitHub
   - Reemplaza los archivos (mantén tu base de datos `explorador_del_cosmos.db`)
   - Compila nuevamente

## 📞 Soporte

Si encuentras algún problema durante la instalación:

1. Consulta la sección de **Solución de Problemas** arriba
2. Revisa los [Issues en GitHub](https://github.com/nicojson/ExploradorCosmos/issues)
3. Crea un nuevo issue describiendo tu problema detalladamente

## ✅ Verificación de Instalación Exitosa

Si lograste:
- ✅ Compilar el proyecto sin errores
- ✅ Ejecutar la aplicación
- ✅ Ver la pantalla de inicio de sesión
- ✅ Crear un usuario y acceder al sistema

**¡Felicitaciones! La instalación fue exitosa.**

Continúa con la [Guía de Usuario](guia-usuario.md) para aprender a utilizar todas las funcionalidades de la aplicación.

---

[← Volver al inicio](index.md) | [Siguiente: Guía de Usuario →](guia-usuario.md)
