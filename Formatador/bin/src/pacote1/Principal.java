package pacote1;
import java.sql.SQLException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.nio.file.*;
import java.nio.file.FileSystems;
/*
Desenvolvedores: Alana Rodrigues Franzen e
Luís Henrique Batista Tavares dos Santos.
*/
public class Principal extends Thread{
	public static File dados = new File ("C:\\Users\\cefet\\Downloads\\TesteEntrada\\log.txt");
	//public static ArrayList listaDeEspera = new ArrayList();
	public static int num_estacao = 61;
	public static int tamanho = 16;
	public static int emCriacao; //Diz se há notificador sendo criado
	
	public void run() {
		notifica();
	}
	
	public static void notifica() {
           // Esse código testa o notificador
		   
		   
		   String pathS = dados.getAbsoluteFile().getParent(); //Acha o diretório do arquivo
		   Path path = Paths.get(pathS); //Converte String para Path
		   
		   Notificador not = new Notificador(); //Começa serviço que verá se novos arquivos de dados
		   // são adicionados pela estação na pasta
		   // Exemplo: arquivo do dia 8 acaba, estação faz arquivo do dia 9.
		   not.num_estacao = num_estacao;
		   not.verPasta(path);
		   
		   
		   //Para saber quando pasta é modificada: https://docs.oracle.com/javase/tutorial/essential/io/notification.html
		   
	}
	 public static void main(String[] argv) throws SQLException {
		 
		 Principal prin = new Principal();
		 prin.start(); //Inicia Thread novo no run() para o Notificador
		 
		 try {
			   String separador;
			   int[] coluna = new int[tamanho];
			   String[] partsOrg = new String[tamanho];
			   
			   Info[] infoCelula = new Info[tamanho];
			   try {
					Thread.sleep(10000);
				   }
			   catch (InterruptedException e) {
					e.printStackTrace();
			   }
			   
			   ArrayList al = (ArrayList) (Notificador.listaDeEspera).get(0);
			   num_estacao = (int) al.get(2);
			   Path tempdados = (Path) al.get(0);
			   File newDados = new File(tempdados.toString());
			   System.out.println("Veja os dados retirados: " + tempdados);
			   dados = newDados;
			   
			   Scanner myReader = new Scanner(dados);
			   /* 
			    Quase certeza que teremos que deslocar tudo abaixo para outra
			    classe/metodo que recebe pelo menos num_id e file
			    */
			   Leitor l = new Leitor();
               separador = l.getSeparador(num_estacao); //Acha o tipo de divisão de item usada pela estação
			   System.out.println("O separador é:" + separador);
			   coluna = l.getOrdem(num_estacao, tamanho);
			   for(int i = 0; i <tamanho; i++) {
				   System.out.println("Item " + i + " : " + coluna[i]);
			   }
			   
			   myReader.nextLine(); //Pula a primeira linha do arquivo de texto.
			   while (myReader.hasNextLine()) {
				   try {
					Thread.sleep(10000);
				   }
				   catch (InterruptedException e) {
					e.printStackTrace();
				   }
				   String dadosLinha = myReader.nextLine();
				   System.out.println("[" + dadosLinha + "]");
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
					   /* 
					   Codigo para lidar com entradas nulas 
					   */
					   if (partsOrg[i] == null || partsOrg[i].equals("/") || partsOrg[i].equals("//") || partsOrg[i].equals("///") || partsOrg[i].equals("////") || partsOrg[i].equals("/////") || partsOrg[i].equals("") || partsOrg[i].equals("nan")) {
						   if ( i == 2 || (i >= 4 && i <= 6) || i == 8 || i == 11 || i == 12 || i == 15) {
							   //infoCelula[i].typeof = "double";
							   // infoCelula[i].setDbl(0); // Pensar outra maneira que não afetará consulta e médias
						       infoCelula[i].typeof = "none";
						   }
						   else if (i == 1 || i == 3 || i == 7 || i == 9 || i == 10 || i == 13 || i == 14) {
							   //infoCelula[i].typeof = "int";
							   infoCelula[i].typeof = "none";
						   }
						   else if (i == 0) {
							   infoCelula[i].typeof = "String";
							   infoCelula[i].setStr(null);
						   }
					   }
					   /* Abaixo os 'i's correspondem as colunas no modelo padrão que
					    * utilizam esse tipo de váriavel.
					    */
					   else if( i == 2 || (i >= 4 && i <= 6) || i == 8 || i == 11 || i == 12 || i == 15) {
						   infoCelula[i].typeof = "double";
						   infoCelula[i].setDbl(Double.parseDouble(partsOrg[i]));
					   }
					   else if(i == 1 || i == 3 || i == 7 || i == 9 || i == 10 || i == 13 || i == 14) {
						   infoCelula[i].typeof = "int";
						   infoCelula[i].setInteg(Integer.parseInt(partsOrg[i]));
					   }
					   else if (i == 0){
						   infoCelula[i].typeof = "String";
						   infoCelula[i].setStr(partsOrg[i]);
					   }
				   }
				   Insercao createTableExample = new Insercao();
				   System.out.println(infoCelula.length);
				   createTableExample.insertRecord(infoCelula, infoCelula.length, num_estacao); //Tenta executar o método 'insertRecord', especificado na classe 'InsertRecordExample'.
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
	 }
	 
	 public static boolean insereOrdenado() {
		 //Lugar para colocar o principal do ordenar
		 return true;
	 }
	 
}