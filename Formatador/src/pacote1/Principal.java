package pacote1;
import java.sql.SQLException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
/*
Desenvolvedores: Alana Rodrigues Franzen e
Luís Henrique Batista Tavares dos Santos.
*/
public class Principal {
	/*
	Código antigo:
	public static void main (String [] args) {
	Conexao c = new Conexao();
	c.connect();
	}
	*/
	 public static void main(String[] argv) throws SQLException {
		 try {
			   File dados = new File ("C:\\Users\\cefet\\Downloads\\log.txt");
			   Scanner myReader = new Scanner(dados);
			   String separador;
			   int num_estacao = 2;
			   int tamanho = 11;
			   int[] coluna = new int[tamanho];
			   String[] partsOrg = new String[tamanho];
			   
			   Info[] infoCelula = new Info[tamanho]; 			   
			   /* 
			    Quase certeza que teremos que deslocar tudo abaixo para outra
			    classe/metodo que recebe pelo menos num_id e file
			    */
			   Leitor l = new Leitor();
               separador = l.getSeparacao(num_estacao);
			   System.out.println("O separador é:" + separador);
			   coluna = l.getOrdem(num_estacao, tamanho);
			   for(int i = 0; i <tamanho; i++) {
				   System.out.println("Item " + i + " : " + coluna[i]);
			   }
			   
			   myReader.nextLine(); //Pula a primeira linha do arquivo de texto.
			   while (myReader.hasNextLine()) {
				   String dadosLinha = myReader.nextLine();
				   System.out.println(dadosLinha);
				   //Separador de string por símbolo:
				   //https://stackoverflow.com/questions/3481828/how-to-split-a-string-in-java#:~:text=Just%20use%20the%20appropriately%20named%20method%3A%20String%23split()%20.&text=Note%20that%20this%20takes%20a,escape%20special%20characters%20if%20necessary.
				   String[] parts = dadosLinha.split(separador);
				   for(int i = 0; i<parts.length ; i++) {
					   System.out.println(parts[i]);
				   }
				   for(int i=0; i<parts.length; i++) {
					   System.out.println("coluna"+ (coluna[i]));
					   partsOrg[coluna[i]-1] = parts[i];
					   
				   }
				   for(int i = 0; i< partsOrg.length; i++) {
					   System.out.println("Informação inserida: " + partsOrg[i]);
					   System.out.println("Coluna dela: " + i);
					   infoCelula[i] = new Info();
					   /* Codigo para lidar com entradas nulas*/
					   if (partsOrg[i].equals("/") || partsOrg[i].equals("//") || partsOrg[i].equals("///") || partsOrg[i].equals("////") || partsOrg[i].equals("/////") || partsOrg[i].equals("") || partsOrg[i].equals("nan")) {
						   if (i == 0 || i == 2 || (i >= 5 && i <= 9) || i == 11) {
							   infoCelula[i].typeof = "double";
							   infoCelula[i].setDbl(0);
						   }
						   else if (i == 1 || i == 3 || i == 4) {
							   infoCelula[i].typeof = "int";
							   infoCelula[i].setInteg(0);
						   }
						   else if (i == 10) {
							   infoCelula[i].typeof = "String";
							   infoCelula[i].setStr(null);
						   }
					   }
					   /* Abaixo os 'i's correspondem as colunas no modelo padrão que
					    * utilizam esse tipo de váriavel.
					    */
					   else if(i == 0 || i == 2 || (i >= 5 && i <= 9)) {
						   infoCelula[i].typeof = "double";
						   infoCelula[i].setDbl(Double.parseDouble(partsOrg[i]));
					   }
					   else if(i == 1 || i == 3 || i == 4) {
						   infoCelula[i].typeof = "int";
						   infoCelula[i].setInteg(Integer.parseInt(partsOrg[i]));
					   }
					   else if (i == 10){
						   infoCelula[i].typeof = "String";
						   infoCelula[i].setStr(partsOrg[i]);
					   }
				   }
				   Insercao createTableExample = new Insercao();
				   createTableExample.insertRecord(infoCelula, infoCelula.length); //Tenta executar o método 'insertRecord', especificado na classe 'InsertRecordExample'.
			   }
			   
			   myReader.close();
			   System.out.println("Programa terminado.");
               
			   /*
			   myReader.close();
			   Insercao createTableExample = new Insercao();
			   createTableExample.insertRecord(infoCelula, tamanho); //Tenta executar o método 'insertRecord', especificado na classe 'InsertRecordExample'.
               */
                

			 } catch (FileNotFoundException e) {
			       System.out.println("An error occurred.");
			       e.printStackTrace();
			 }
	 /*
		 InsertRecordExample createTableExample = new InsertRecordExample();
		 createTableExample.insertRecord(); //Tenta executar o método 'insertRecord', especificado na classe 'InsertRecordExample'.
	 */
	 }
}