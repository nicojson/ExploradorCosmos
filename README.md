# 🌌 Explorador del Cosmos

**Explorador del Cosmos** es una aplicación de escritorio desarrollada con JavaFX que permite explorar la vasta colección de imágenes y videos de la NASA. Busca, visualiza y gestiona tu contenido espacial favorito con una interfaz moderna e intuitiva.

![Interfaz Principal](C:/Users/godin/.gemini/antigravity/brain/f2c89071-ff75-4525-8e46-4f5f5f6d5d9f/app_interface_main_1764019492681.png)

## ✨ Características Principales

- 🔍 **Búsqueda Avanzada**: Busca en la biblioteca de imágenes y videos de la NASA con filtros por año y palabras clave
- 🎬 **Soporte Multimedia**: Visualiza tanto imágenes como videos con reproductor integrado
- ⭐ **Favoritos**: Guarda y organiza tu contenido espacial favorito
- 👥 **Sistema de Usuarios**: Registro, autenticación y gestión de perfiles
- 🔐 **Panel de Administración**: Gestión de usuarios con privilegios de administrador
- 📄 **Reportes PDF**: Genera reportes personalizados de tus búsquedas y favoritos
- 🌙 **Interfaz Moderna**: Diseño oscuro inspirado en el cosmos con efectos visuales atractivos

## 🛠️ Tecnologías Utilizadas

- **Java 17**: Lenguaje de programación principal
- **JavaFX 21.0.6**: Framework para la interfaz gráfica
- **Gradle**: Gestión de dependencias y construcción del proyecto
- **SQLite**: Base de datos local
- **Retrofit**: Cliente HTTP para la API de NASA
- **iText PDF**: Generación de reportes PDF
- **BCrypt**: Encriptación de contraseñas

## 📋 Requisitos del Sistema

- **Java Development Kit (JDK) 17** o superior
- **JavaFX 21.0.6** (se descarga automáticamente con Gradle)
- **Gradle 8.x** (incluido con wrapper)
- **Sistema Operativo**: Windows, macOS o Linux
- **Memoria RAM**: Mínimo 4 GB recomendado
- **Conexión a Internet**: Requerida para acceder a la API de NASA

## 🚀 Inicio Rápido

### Instalación

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/nicojson/ExploradorCosmos.git
   cd ExploradorCosmos
   ```

2. **Compilar el proyecto**
   ```bash
   # En Windows
   .\gradlew.bat build
   
   # En macOS/Linux
   ./gradlew build
   ```

3. **Ejecutar la aplicación**
   ```bash
   # En Windows
   .\gradlew.bat run
   
   # En macOS/Linux
   ./gradlew run
   ```

### Configuración Inicial

Al ejecutar la aplicación por primera vez:

1. Se creará automáticamente la base de datos `explorador_del_cosmos.db`
2. Se configurará un usuario administrador por defecto
3. Podrás registrar nuevos usuarios desde la pantalla de inicio

## 📚 Documentación

Para instrucciones detalladas de instalación y uso, consulta la documentación completa:

- **[Manual de Instalación](https://nicojson.github.io/ExploradorCosmos/manual-instalacion)**: Guía paso a paso para instalar y configurar la aplicación
- **[Guía de Usuario](https://nicojson.github.io/ExploradorCosmos/guia-usuario)**: Aprende a utilizar todas las funcionalidades

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Si deseas contribuir:

1. Haz un fork del proyecto
2. Crea una rama para tu característica (`git checkout -b feature/nueva-caracteristica`)
3. Realiza tus cambios y haz commit (`git commit -am 'Agrega nueva característica'`)
4. Sube los cambios a tu fork (`git push origin feature/nueva-caracteristica`)
5. Abre un Pull Request

## 📝 Licencia

Este proyecto está licenciado bajo la Licencia MIT. Consulta el archivo [LICENSE](LICENSE) para más detalles.

## 👨‍💻 Autor

Desarrollado por **nicojson** para el proyecto final de Tópicos Avanzados de Programación - TECNM Campus Celaya

## 🙏 Agradecimientos

- **NASA** por proporcionar acceso gratuito a su increíble biblioteca de imágenes y videos a través de su API pública
- **OpenJFX** por el framework JavaFX
- Comunidad de código abierto por las excelentes bibliotecas utilizadas

## 🔗 Enlaces Útiles

- [Repositorio en GitHub](https://github.com/nicojson/ExploradorCosmos)
- [Documentación](https://nicojson.github.io/ExploradorCosmos/)
- [NASA Image and Video Library](https://images.nasa.gov/)
- [Reportar un problema](https://github.com/nicojson/ExploradorCosmos/issues)

---

⭐ Si este proyecto te resultó útil, considera darle una estrella en GitHub
