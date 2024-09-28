import org.techguard.conexao.Conexao;
import org.techguard.dao.UsuarioDao;
import org.techguard.tabelas.Usuario;

public class Principal {
    public static void main(String[] args) {
        Usuario user = new Usuario();
        UsuarioDao usuarioDao = new UsuarioDao();
        user.setIdUsuario(4);
        user.setNomeUsuario("Ana");
        user.setSenhaUsuario("1234");
        user.setCpf("01234567891011");
        user.setEmailUsuario("ana@gmail.com");
        user.setTelUsuario("(11)90001-2094");
        user.setFkEmpresa(1);
        user.setFkTipoUsuario(1);

        new UsuarioDao().cadastrarUsuario(user);

        usuarioDao.selectUsuario();
    }

}
