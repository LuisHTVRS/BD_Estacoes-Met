package pacote1;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
public class ConexaoAntiga {
	private final String url = "jdbc:postgresql://localhost/Estacao-dados";
	private final String user = "postgres";
	private final String password = "aluno";
	public Connection connect() {
        Connection conn = null;
        Statement pstmt = null;

        try {
        	Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
            
            pstmt = conn.createStatement();
            
            pstmt.execute("INSERT INTO 'Estacao' ('Num_id', 'Alt, 'Lat', 'Info1', 'Info2') VALUES (1,1,1,1,1)");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

        return conn;
	}
}