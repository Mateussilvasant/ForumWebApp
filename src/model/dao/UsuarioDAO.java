package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Usuario;

public class UsuarioDAO implements IUsuarioDAO
{

    @Override
    public void cadastrarUsuario(Usuario u) throws Exception
    {
	try (Connection c = ConnectionFactory.getConnection())
	{

	    String sql = "INSERT INTO usuario(login,email,nome,senha,pontos) VALUES((?),(?),(?),(?),(?))";

	    PreparedStatement ps = c.prepareStatement(sql);

	    ps.setString(1, u.getLogin());
	    ps.setString(2, u.getEmail());
	    ps.setString(3, u.getNome());
	    ps.setString(4, u.getSenha());
	    ps.setInt(5, u.getPontos());

	    if (ps.executeUpdate() == 0)
	    {
		throw new Exception("N�o foi poss�vel cadastrar usu�rio!");
	    }

	} catch (SQLException e)
	{
	    throw new Exception("Ocorreu um erro interno!", e);
	}
    }

    @Override
    public Usuario consultarUsuario(String login) throws Exception
    {
	Usuario usuario = null;

	try (Connection c = ConnectionFactory.getConnection())
	{

	    String sql = "SELECT * FROM usuario WHERE login = (?)";
	    PreparedStatement ps = c.prepareStatement(sql);

	    ps.setString(1, login);
	    ResultSet result = ps.executeQuery();

	    if (result.next())
	    {
		usuario = new Usuario();
		usuario.setEmail(result.getString("email"));
		usuario.setLogin(result.getString("login"));
		usuario.setNome(result.getString("nome"));
		usuario.setSenha(result.getString("senha"));
		usuario.setPontos(result.getInt("pontos"));
	    } else
	    {
		throw new Exception("N�o foi poss�vel recuperar o usu�rio! ");
	    }

	} catch (SQLException e)
	{
	    throw new Exception("Ocorreu um erro interno!", e);
	}

	return usuario;
    }

    @Override
    public void adicionarPontos(String login, int pontos) throws Exception
    {
	try (Connection c = ConnectionFactory.getConnection())
	{

	    String sql = "UPDATE usuario SET pontos = pontos + ? WHERE login = ?";
	    PreparedStatement ps = c.prepareStatement(sql);

	    ps.setInt(1, pontos);
	    ps.setString(2, login);

	    if (ps.executeUpdate() == 0)
	    {
		throw new Exception("N�o foi poss�vel adicionar os pontos!");
	    }

	} catch (SQLException e)
	{
	    throw new Exception("Ocorreu um erro interno!", e);
	}
    }

    @Override
    public List<Usuario> getRanking() throws Exception
    {
	List<Usuario> rankingUsuarios = new ArrayList<>();

	try (Connection c = ConnectionFactory.getConnection())
	{

	    String sql = "SELECT * FROM USUARIO ORDER BY pontos DESC ";
	    PreparedStatement ps = c.prepareStatement(sql);

	    ResultSet result = ps.executeQuery();

	    while (result.next())
	    {
		Usuario usuario = new Usuario();
		usuario.setEmail(result.getString("email"));
		usuario.setLogin(result.getString("login"));
		usuario.setNome(result.getString("nome"));
		usuario.setSenha(result.getString("senha"));
		usuario.setPontos(result.getInt("pontos"));
		rankingUsuarios.add(usuario);
	    }

	} catch (SQLException e)
	{
	    throw new Exception("Ocorreu um erro interno!", e);
	}

	return rankingUsuarios;
    }

    public Usuario realizarLogin(String login, String senha) throws Exception
    {
	Usuario usuario = null;

	try (Connection c = ConnectionFactory.getConnection())
	{

	    String sql = "SELECT * FROM usuario WHERE login = (?) AND senha = (?)";
	    PreparedStatement ps = c.prepareStatement(sql);

	    ps.setString(1, login);
	    ps.setString(2, senha);

	    ResultSet result = ps.executeQuery();

	    if (result.next())
	    {
		usuario = new Usuario();
		usuario.setEmail(result.getString("email"));
		usuario.setLogin(result.getString("login"));
		usuario.setNome(result.getString("nome"));
		usuario.setSenha(result.getString("senha"));
		usuario.setPontos(result.getInt("pontos"));
	    } else
	    {
		throw new Exception("Usu�rio n�o existe, tente com outro! ");
	    }

	} catch (SQLException e)
	{
	    throw new Exception("Ocorreu um erro interno!", e);
	}

	return usuario;
    }

}