package pacote1;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;

public class Leitor {
	// Classe que lê informações do banco de dados
	private final String url = "jdbc:postgresql://localhost/Estacao-dados";
	private final String user = "postgres";
	private final String password = "aluno";
	ResultSet rs = null;
	PreparedStatement p = null;
	private final String[] campos = {"umidade", "temperatura_min", "radiacao_sol"};
	// Conterá todos os campos de ordem do banco
	
	/*
	 * 
	 */
	
	
	public String getCampo(int num_id, String campoPedido) { //Acha um campo do banco de dados da tabela de estações
		try (Connection connection = DriverManager.getConnection(url, user, password)){
			String sql = "select * from estacoes";
	        p = connection.prepareStatement(sql);
	        rs = p.executeQuery();
	        while (rs.next()) {
	            if (rs.getInt("id") == num_id) {
		            String result = rs.getString(campoPedido);
		            return result;
	            }
	        }
		} catch (SQLException e ) {
			System.out.println(e);
		}
		return null;
	}
	
	public String getSeparador(int num_id) { //Função para facilitar programação
		return getCampo(num_id, "separador");
	}
	
	public String getPasta(int num_id) {
		return getCampo(num_id, "pasta");
	}
	
	public int[] getOrdem(int num_id, int tamanho) {
		try (Connection connection = DriverManager.getConnection(url, user, password)){
			String sql = "select * from estacoes";
			int[] listaOrdem = new int[tamanho];
	        p = connection.prepareStatement(sql);
	        rs = p.executeQuery();
	        System.out.println("Chegou na função getOrdem()");
	        while (rs.next()) {
	        	System.out.println("Entrou na próxima linha");
	            if (rs.getInt("id") == num_id) {
	            	System.out.println("Achou a estação de mesmo id");
	            	//Mudar relação abaixo para ser Informação -> Item ao invés de Item -> Informação
	            	for (int i = 0; i < tamanho; i++) {
	            		System.out.println("Aqui:" + i);
	            		listaOrdem[i] = 1;
	            		System.out.println(listaOrdem[i]);
			            listaOrdem[i] = rs.getInt("col" + (i+1)); //Editando aqui !!!!!
			            System.out.println("Coluna real do item "+i+ " recuperada: " + listaOrdem[i]);
			            /*
			            O item tem tal posição de coluna no banco
			            
			            O valor da coluna 0 dirá em qual coluna o item 0 ficará
			            Valor i+1 --> item irá para coluna i+1
			            (Banco usa |N* e java |N)
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
	
	public ArrayList getListaPastas() {
		try (Connection connection = DriverManager.getConnection(url, user, password)){
			String sql = "select * from estacoes";
	        p = connection.prepareStatement(sql);
	        rs = p.executeQuery();
	        ArrayList relacaoPastaEstacao = new ArrayList();
	        while (rs.next()) {
	        	/*
	            if (rs.getInt("idestacao") == num_id) {
		            String result = rs.getString(campoPedido);
		            return result;
	            }
	            */
	        	ArrayList linha = new ArrayList();
	        	linha.add(rs.getInt("id"));
	        	linha.add(rs.getString("pasta"));
	        	
	        	relacaoPastaEstacao.add(linha);
	        }
	        for(int i = 0; i<relacaoPastaEstacao.size(); i++) {
	        	ArrayList b = (ArrayList) relacaoPastaEstacao.get(i);
	        	int c = (int) b.get(0);
	        	System.out.println("{" + c + "}  {" + (String) b.get(1) + "}");
	        }
	        return relacaoPastaEstacao;
		} catch (SQLException e ) {
			System.out.println(e);
		}
		return null;
	}
}