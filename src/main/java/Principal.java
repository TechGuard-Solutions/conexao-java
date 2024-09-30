import org.techguard.dao.UsuarioDao;
import org.techguard.tabelas.Usuario;

public class Principal {
    public static void main(String[] args) {
        // Criação do objeto da classe usuario
        Usuario user = new Usuario();
        // Criação do objeto da classe usuarioDAO (Data Access Object)
        UsuarioDao usuarioDao = new UsuarioDao();
        // Setando os insert para o banco
        user.setIdUsuario(5);
        user.setNomeUsuario("Samara");
        user.setSenhaUsuario("1234");
        user.setCpf("01234567891011");
        user.setEmailUsuario("samara@gmail.com");
        user.setTelUsuario("(11)92211-2094");
        user.setFkEmpresa(1);
        user.setFkTipoUsuario(1);

        // Comando que adiciona os dados no banco
        new UsuarioDao().cadastrarUsuario(user);
        // Comando para fazer o select no terminal
        usuarioDao.selectUsuario();
    }

}
