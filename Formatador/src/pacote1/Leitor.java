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
}