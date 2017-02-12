package me.guigarciazinho.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import me.guigarciazinho.models.PlayerGame;
import me.guigarciazinho.principal.Main;

public class BancoDeDados {
	private Main plugin;
	private Connection con = null;
	private PreparedStatement ps;
	private PlayerGame player = null;
	private int resultado;

	public BancoDeDados(Main main) {
		plugin = main;
	}

	public void conectar() {
		final String driver = "com.mysql.jdbc.Driver";
		final String url = "jdbc:mysql://"+plugin.getConfig().getString("Urlconf")+"/servidor";

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, plugin.getConfig().getString("User"), plugin.getConfig().getString("Senha"));
		} catch (ClassNotFoundException erro) {
			System.out.println("Driver não encontrado." + erro.toString());
		} catch (SQLException erro) {
			System.out.println("Ocorreu um erro ao tentar se conectar com o banco de dados." + erro.toString());
		}
	}

	public int getVitorias(UUID id) {
		try {

			String sql = "SELECT * FROM KamiX1 WHERE id = ?";
			ps = con.prepareStatement(sql);
			ps.setObject(1, id.toString());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				resultado = rs.getInt("vitorias");
				ps.close();
				rs.close();
				return resultado;
			} else {
				ps.close();
				rs.close();
				return 0;
			}
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
		return 0;
	}

	public int getDerrotas(UUID id) {
		try {
			String sql = "SELECT * FROM KamiX1 WHERE id = ?";
			ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				resultado = rs.getInt("derrotas");
				ps.close();
				rs.close();
				return resultado;
			} else {
				ps.close();
				rs.close();
				return 0;
			}
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
		return 0;
	}

	public void getDados(UUID id) {
		player = plugin.game.getPlayerGame(id);
		player.setCarregado();
		player.setDerrotas(getDerrotas(id));
		player.setVitorias(getVitorias(id));
	}

	public boolean gerenciarVencedor(UUID idVencedor, String nomeVencedor) {
		try {
			String sql = "SELECT * FROM KamiX1 WHERE id = ?";
			ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				atualizarDadosVencedor(idVencedor);
				ps.close();
				rs.close();
				return false;
			} else {
				String sql2 = "insert into KamiX1" + " (id, nick, vitorias, derrotas)" + " values" + " (?, ?, ?, ?);";
				ps = con.prepareStatement(sql2);
				ps.setObject(1, idVencedor.toString());
				ps.setString(2, nomeVencedor);
				ps.setInt(3, 1);
				ps.setInt(4, 0);
				ps.executeUpdate();
				ps.close();
				rs.close();
				return true;
			}
		} catch (SQLException e) {
			System.out.println(e.toString());
			return false;
		}
	}

	public boolean gerenciarPerdedor(UUID idPerdedor, String nomePerdedor) {
		try {
			String sql = "SELECT * FROM KamiX1 WHERE id = ?";
			ps = con.prepareStatement(sql);
			ps.setObject(1, idPerdedor.toString());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				atualizarDadosPerdedor(idPerdedor);
				ps.close();
				rs.close();
				return false;
			} else {
				String sql2 = "insert into KamiX1" + " (id, nick, vitorias, derrotas)" + " values" + " (?, ?, ?, ?);";
				ps = con.prepareStatement(sql2);
				ps.setObject(1, idPerdedor.toString());
				ps.setString(2, nomePerdedor);
				ps.setInt(3, 0);
				ps.setInt(4, 1);
				ps.executeUpdate();
				ps.close();
				rs.close();
				return true;
			}
		} catch (SQLException e) {
			System.out.println(e.toString());
			return false;
		}
	}

	public void atualizarDadosVencedor(UUID idVencedor) {
		try {
			String sql = "UPDATE KamiX1" + " SET vitorias = vitorias + 1 " + "WHERE id = ?;";
			ps = con.prepareStatement(sql);
			ps.setObject(1, idVencedor.toString());
			ps.executeUpdate();
			ps.close();
			player = plugin.game.getPlayerGame(idVencedor);
			if (player.getCarregado()) {
				player.setVitorias(player.getVitorias() + 1);
			}

		} catch (SQLException e) {
			System.out.println(e.toString());

		}
	}

	public void atualizarDadosPerdedor(UUID idPerdedor) {
		try {
			String sql = "UPDATE KamiX1" + " SET vitorias = vitorias + 1 " + "WHERE id = ?;";
			ps = con.prepareStatement(sql);
			ps.setObject(1, idPerdedor.toString());
			ps.executeUpdate();
			ps.close();
			player = plugin.game.getPlayerGame(idPerdedor);
			if (player.getCarregado()) {
				player.setVitorias(player.getDerrotas() + 1);
			}
			System.out.println("Sucesso");

		} catch (SQLException e) {
			System.out.println(e.toString());
		}
	}

}
