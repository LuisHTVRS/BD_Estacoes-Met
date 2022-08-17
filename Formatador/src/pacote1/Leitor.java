package pacote1;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;

public class Leitor {
	private final String url = "jdbc:postgresql://localhost/Estacao-dados";
	private final String user = "postgres";
	private final String password = "aluno";
	ResultSet rs = null;
	PreparedStatement p = null;
	private final String[] campos = {"umidade", "temperatura_min", "radiacao_sol"};
	// Conterá todos os campos de ordem do banco
	
	
	public String getSeparacao(int num_id) {
		try (Connection connection = DriverManager.getConnection(url, user, password)){
			String sql = "select * from testeleitura";
	        p = connection.prepareStatement(sql);
	        rs = p.executeQuery();
	        while (rs.next()) {
	            if (rs.getInt("num_id") == num_id) {
		            String sep = rs.getString("separador");
		            return sep;
	            	
	            }
	        }
		} catch (SQLException e ) {
			System.out.println(e);
		}
		return null;
	}
	
	public int[] getOrdem(int num_id, int tamanho) {
		try (Connection connection = DriverManager.getConnection(url, user, password)){
			String sql = "select * from testeleitura";
			int[] listaOrdem = new int[tamanho];
	        p = connection.prepareStatement(sql);
	        rs = p.executeQuery();
	        while (rs.next()) {
	            if (rs.getInt("num_id") == num_id) {
	            	for (int i = 0; i < tamanho; i++) {
			            listaOrdem[i] = rs.getInt("info" + (i+1));
			            System.out.println("Algo(Não lembro) "+i+ " recuperada: " + listaOrdem[i]);
			            /*
			            listaOrdem[i] = rs.getInt(campos[i])
			             */
	            	}
	            	return listaOrdem;
	            }
	        }
		} catch (SQLException e ) {
			System.out.println(e);
		}
		return null;
	}
}