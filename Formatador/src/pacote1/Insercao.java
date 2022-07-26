package pacote1;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/*
1. Estabelece uma conexão com o banco de dados (PosgreSQL, no caso) pelas variáveis 'url', 'senha'
e 'usuário', que têm o valor de seus respectivos dados.

2. Através da conexão estabelecida, o programa cria linhas na tabela 'estacao',
com os diferentes atributos pedidos.

*/
public class Insercao {
	private final String url = "jdbc:postgresql://localhost/Estacao-dados"; //1
	private final String user = "postgres"; //1
	private final String password = "aluno"; //1
	
	/*
    private static final String INSERT_EST_SQL = "INSERT INTO estacao" +
        "  (num_id, alt, lat, info1, info2, local) VALUES " +
        " (?, ?, ?, ?, ?, ?);"; //2 - Especifica os dados para a inserção.
    
    private static final String INSERT_INT_SQL = "INSERT INTO testecol" +
            "  (info1, info2, info3, info4, info5, info6, idpk) VALUES " +
            " (?, ?, ?, ?, ?, ?, ?);"; //2 - Especifica os dados para a inserção.
    
    private static final String INSERT_DBL_SQL = "INSERT INTO doubletable" +
            "  (doubletest) VALUES " +
            " (?);"; //2 - Especifica os dados para a inserção.
    
    private static final String INSERT_SEPEX_SQL = "INSERT INTO sepex_demonstracao" +
            "  (datahora, umidade, rad_sol, temperatura, idestacao) VALUES " +
            " (?, ?, ?, ?, ?);"; //2 - Especifica os dados para a inserção.
    
    private static final String INSERT_LOGDEF_SQL = "INSERT INTO testefin" +
            "  (datahora, info1, info2, info3, info4, info5, info6, info7, info8, info9, info10, info11, info12, info13, info14, info15, idestacao) VALUES " +
            " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"; //2 - Especifica os dados para a inserção.
    */

	private static final String INSERT_DADOS_SQL = "INSERT INTO dados" +
            "  (tensao_da_bateria, ponto_orvalho_inst, ponto_orvalho_min, ponto_orvalho_max, sensacao_termica_inst, sensacao_termica_min, sensacao_termica_max, pressao_atm_inst, pressao_atm_min, pressao_atm_max, precipitacao_15min, precipitacao_1h, precipitacao_24h, precipitacao_30min, umidade_inst, umidade_min, umidade_max, radiacao_inst, radiacao_min, radiacao_max, temperatura_inst, temperatura_min, temperatura_max, direcao_inst, direcao_10min, direcao_10min_min, direcao_10min_max, direcao_1h, direcao_2min, direcao_2min_min, direcao_2min_max, velocidade_inst, velocidade_10min, velocidade_10min_min, velocidade_10min_max, velocidade_1h, velocidade_2min, velocidade_2min_min, velocidade_2min_max, wawatext, time, id_est, data_e_hora) VALUES " +
            " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    public void insertRecord(Info[] arrayInfo, int tamanho, int id) throws SQLException { //1 e 2
        System.out.println(INSERT_DADOS_SQL); //2 - Imprime os dados que serão inseridos (para localizar o usuário, antes da inserção).
        //System.out.println(INSERT_LOGDEF_SQL); //2 - Imprime os dados que serão inseridos (para localizar o usuário, antes da inserção).
        
        try (Connection connection = DriverManager.getConnection(url, user, password); //1 - Tenta estabelecer a conexão com o servidor do BD (PostgreSQL, no caso), antes de executar a inserção. Passo indispensável.
        		
           PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DADOS_SQL)) { //2 - Gera uma conexão e prepara a tabela em questão.
           //PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SEPEX_SQL))
        	
        	/*
        	Código antigo - 
        	preparedStatement.setInt(1, 3);
            preparedStatement.setInt(2, 1);
            preparedStatement.setInt(3, 1);
            preparedStatement.setInt(4, 1);
            preparedStatement.setInt(5, 1);
            preparedStatement.setString(6, "cefet");
            */
        	
        	/*
        	Info a[] = new Info[6];
        	for(int i = 0; i<5; i++) {
        		a[i] = new Info();
               	a[i].typeof = "int";
               	a[i].setInteg((int) Math.floor(Math.random() * 1000));
        	}
        	a[5] = new Info();
        	a[5].typeof = "String";
        	a[5].setStr("Canabarro");
        	*/
        	
            for(int i = 0; i<tamanho; i++){ //2 - Insere os atributos selecionados pelo usuário em cada coluna (de acordo e na ordem especificada pelo 'INSERT_USERS_SQL').
            	if( arrayInfo[i].typeof == "String"){
            		/**
            		if((arrayInfo[i].getStr()).equals(null)) {
            			continuar depois: compara para saber se não há informações a receber
            		}
            		**/
            		preparedStatement.setString(i+1, (arrayInfo[i].getStr())); //PreparedStatement.setTipo() substitue os <?> na string
            		//de inserção SQL pelo objeto pedido, nesse caso o próximo item de ArrayInfo
            	}
            	else if( arrayInfo[i].typeof == "int"){
            		preparedStatement.setInt(i+1, (arrayInfo[i].getInteg()));
            	}
            	else if (arrayInfo[i].typeof == "double") {
            		preparedStatement.setDouble(i+1, (arrayInfo[i].getDbl()));
            	}
            	else if (arrayInfo[i].typeof == "none") {
            		preparedStatement.setObject(i+1, null); //Insere a falta de informação se não houver correspondente.
            	}
            	
            }
            preparedStatement.setInt(42, id); //Recebe numero da estação
       
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        	} catch (SQLException e) { //1 e 2 - Lida com possíveis erros na conexão ou inserção.

            printSQLException(e);
        }

    }

    public static void printSQLException(SQLException ex) { //1 e 2 - Imprime a mensagem de erro em questão.
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}

//Fonte: https://www.javaguides.net/2020/02/java-jdbc-postgresql-insert-example.html