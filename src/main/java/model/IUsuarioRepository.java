package model;

public interface IUsuarioRepository {
    Usuario autenticar(String username, String password);
    void registrar(Usuario usuario);
    boolean existeUsuario(String username);
}
