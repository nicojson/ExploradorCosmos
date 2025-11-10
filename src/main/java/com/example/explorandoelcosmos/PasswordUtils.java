package com.example.explorandoelcosmos;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    /**
      Crea un hash de una contraseña en texto plano usando BCrypt.
      @param plainTextPassword La contraseña a hashear.
      @return El hash de la contraseña.
     */
    public static String hashPassword(String plainTextPassword) {
        // BCrypt.gensalt() genera un "salt" aleatorio. El segundo parámetro es el "work factor",
        // que controla cuánto tiempo se tarda en hashear. Un valor de 12 es un buen punto de partida.
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(12));
    }

    /**
      Verifica si una contraseña en texto plano coincide con un hash guardado.

      @param plainTextPassword La contraseña introducida por el usuario.
      @param hashedPassword    El hash guardado en la base de datos.
      @return true si la contraseña coincide, false en caso contrario.
     */
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}
