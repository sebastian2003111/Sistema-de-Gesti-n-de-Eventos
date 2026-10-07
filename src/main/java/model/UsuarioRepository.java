package model;

import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository implements IUsuarioRepository {
    private static UsuarioRepository instance;
    private List<Usuario> usuarios;

    private UsuarioRepository() {
        usuarios = new ArrayList<>();
        // Usuarios quemados en memoria para pruebas
        usuarios.add(new Usuario("admin", "admin@admin.com", "admin123", "Administrador"));
        usuarios.add(new Usuario("operador", "operador@gmail.com", "operador123", "Operador"));
    }

    public static UsuarioRepository getInstance() {
        if (instance == null) {
            instance = new UsuarioRepository();
        }
        return instance;
    }

    @Override
    public Usuario autenticar(String correo, String password) {
        for (Usuario u : usuarios) {
            if (u.getCorreo().equals(correo) && u.getPassword().equals(password)) {
                return u; // Retorna el usuario si coinciden las credenciales
            }
        }
        return null; // Retorna nulo si no se encuentra o la contraseña es incorrecta
    }

    @Override
    public void registrar(Usuario usuario) {
        usuarios.add(usuario);
    }

    @Override
    public boolean existeUsuario(String username) {
        return usuarios.stream().anyMatch(u -> u.getUsername().equals(username));
    }
}
