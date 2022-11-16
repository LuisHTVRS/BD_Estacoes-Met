package pacote1;
import java.sql.SQLException; //Imports para exceção do banco de dados

import java.util.Scanner; //Imports para arquivo
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.*;
import java.nio.file.FileSystems;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import java.util.Date; //Imports para data
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;

/*
Desenvolvedores: Alana Rodrigues Franzen e
Luís Henrique Batista Tavares dos Santos.
*/

public class Principal extends Thread {
	public static File dados = new File ("C:\\Users\\cefet\\Downloads\\Est1\\log-EST1.txt");
	//public static ArrayList listaDeEspera = new ArrayList();
	public static int num_estacao = 1; //Numero static para que Main possa se comunicar com o novo thread
	public static int tamanho = 18;
	public static boolean emCriacao = false; //Diz se há notificador sendo criado
	
	public void run() { //Função chamada na criação do novo Thread
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
		   not.verPasta(path); //Define qual pasta o notificador vigiará.
		   emCriacao = false;
		   
		   //Para saber quando pasta é modificada: https://docs.oracle.com/javase/tutorial/essential/io/notification.html
		   
	}
	
	public static void todosNotifica() { //Cria todos os notificadores de diferentes estacoes
		Leitor l = new Leitor();
		//try {
			ArrayList listaPasta = l.getListaPastas(); //Acha ArrayList de ArrayLists com campos de
			//id da estacao e pasta dela, por linha
			for(int i = 0; i < listaPasta.size(); i++) {
				ArrayList linha = (ArrayList) listaPasta.get(i);
				num_estacao = (int) linha.get(0);
				String pasta = (String) linha.get(1);
				emCriacao = true;
				dados = new File(pasta);
				Principal prin = new Principal();
				prin.start(); //Inicia Thread novo no run() para o Notificador
				try {
					Thread.sleep(5000);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		//}
		//catch (Exception e){
			//System.out.println(e);
		//}
	}
	
	//----------------------------
	
	public static void main(String[] argv) throws SQLException {
		todosNotifica();
		//salvaArquivo(1, "C:\\Users\\cefet\\Downloads\\TesteEntrada\\Dados_2.txt");
		
		while(true) {
			
			try {
				Thread.sleep(10000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(!(Notificador.listaDeEspera).isEmpty()) {
				// PROBLEMA:
				// Armazenar em um arquivo de texto
				// Como algumas estações criam diversos arquivos ao longo do tempo, deletar linhas
				// de lembrete dos outros arquivos da estação quando um novo for visto na pasta
				// ou quando não forem atualizadas por mais de um mês.
				
				ArrayList al = (ArrayList) (Notificador.listaDeEspera).get(0);
				num_estacao = (int) al.get(2);
				Path tempdados = (Path) al.get(0);
				File newDados = new File(tempdados.toString());
				System.out.println("Veja os dados retirados: " + tempdados);
				insereOrdenado(newDados, num_estacao);
				//botar escritura aqui
				(Notificador.listaDeEspera).remove(0);
			}
			
		}
	 }
	
	 //----------------------------
	 
	 public static boolean insereOrdenado(File dados, int num_estacao) {
		 //Lugar para colocar o principal do ordenar
		 try {
			   String separador;
			   int[] coluna = new int[tamanho];
			   String[] partsOrg = new String[tamanho];
			   Info[] infoCelula = new Info[tamanho];
			   
			   Scanner myReader = new Scanner(dados);
			   
			   Leitor l = new Leitor();
			   separador = l.getSeparador(num_estacao); //Acha o tipo de divisão de item usada pela estação
			   System.out.println("O separador é:" + separador);
			   
			   coluna = l.getOrdem(num_estacao, tamanho);
			   for(int i = 0; i <tamanho; i++) {
				   System.out.println("Item " + i + " : " + coluna[i]);
			   }
			   
			   int temp = continuarLeitura(dados);
			   
			   System.out.println("---------| última linha: " + temp + " |---------");
			   
			   int linhaAtual;
			   
			   if(temp == 0) {
				   myReader.nextLine();
				   linhaAtual = 1;
			   }
			   else {
				   for(int i = 0; i < temp; i++) {//Pula a primeira linha do arquivo de texto e as linhas em memoria
					   myReader.nextLine();
				   }
				   linhaAtual = temp;
			   }
			   
			   
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
				   if (!dadosLinha.equals(null)) { //Apesar de não ocorrerem com estações reais,
					   //linhas de teste nulas podem causar problemas com salvaArquivo() posteriormente.
					   linhaAtual += 1;
				       String[] parts = dadosLinha.split(separador); //Problema: linhas vazias contam como linhas lidas...
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
							   if ( i == 2 || (i >= 4 && i <= 6) || (i >= 8 && i <= 11) || i == 16 || i == 17) {
								   //infoCelula[i].typeof = "double";
								   // infoCelula[i].setDbl(0); // Pensar outra maneira que não afetará consulta e médias
							       infoCelula[i].typeof = "none";
							   }
							   else if (i == 3 || i == 7 || (i >= 12 && i <= 15) || i == 18) {
								   //infoCelula[i].typeof = "int";
								   infoCelula[i].typeof = "none";
							   }
							   else if (i == 0 || i == 1) {
								   infoCelula[i].typeof = "String";
								   infoCelula[i].setStr(null);
							   }
						   }
						   /* Abaixo os 'i's correspondem as colunas no modelo padrão que
						    * utilizam esse tipo de váriavel.
						    */
						   else if( i == 2 || (i >= 4 && i <= 6) || (i >= 8 && i <= 11) || i == 16 || i == 17) {
							   infoCelula[i].typeof = "double";
							   infoCelula[i].setDbl(Double.parseDouble(partsOrg[i]));
						   }
						   else if(i == 3 || i == 7 || (i >= 12 && i <= 15) || i == 18) {
							   infoCelula[i].typeof = "int";
							   infoCelula[i].setInteg(Integer.parseInt(partsOrg[i]));
						   }
						   else if (i == 0 || i == 1) {
							   infoCelula[i].typeof = "String";
							   infoCelula[i].setStr(partsOrg[i]);
						   }
					   }
					   Insercao createTableExample = new Insercao();
					   System.out.println(infoCelula.length);
					   try {
						   createTableExample.insertRecord(infoCelula, infoCelula.length, num_estacao); //Tenta executar o método 'insertRecord', especificado na classe 'InsertRecordExample'.
					   }
					   catch(Exception e){
						   e.printStackTrace();
					   }
			       }
				   String  caminhoDados = dados.getAbsolutePath();
				   salvaArquivo(linhaAtual, caminhoDados);
			   }
			   
			   myReader.close();
			   System.out.println("Atualização terminada.");
			   return true;
             
			   /*
			   		myReader.close();
			   		Insercao createTableExample = new Insercao();
			   		createTableExample.insertRecord(infoCelula, tamanho); //Tenta executar o método 'insertRecord', especificado na classe 'InsertRecordExample'.
             */
              

		 } catch (Exception e) {
			   e.printStackTrace();
			   return false;
		 }
		 
	 }
	 
	 public static void salvaArquivo (int linha, String caminho) {
		 Path caminhover = Paths.get("C:\\Users\\cefet\\Downloads\\Memoria\\Memoria_linhas.txt");
		 ArrayList conteudo = new ArrayList();
		 //caminho = "C:\\Users\\cefet\\Downloads\\TesteEntrada\\Dados_2.txt"; //No momento testando com um txt imaginario
		 
		 
		 /*
		 try {
			 Date d2 = new Date();
			 d2 = new SimpleDateFormat("dd/mm/yyyy")
	                 .parse(strDate);
			 System.out.println("{Data é: " + d2 + " --- e teste: }" + d2);
		 }
		 catch(ParseException e) {
			 System.out.println(e);
		 }
		 
		 // Usar esse tipo de formatação para interpretar datas posteriormente.
		 */
		 
	     try {
	         // Cria um escritor para o arquivo de texto.
	    	 System.out.println(caminhover);
	    	 File arquivo = new File("C:\\Users\\cefet\\Downloads\\Memoria\\Memoria_linhas.txt");
	    	 Scanner reader = new Scanner(arquivo);
    		 boolean repetido = false;
    		 String sM = "\t"; //Variavel separadorMemoria
	    	 
	    	 DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss"); //Define formato da string com data
			 Date d = new Date(); //Cria data atual, já que construtor é nulo
			 String data = dateFormat.format(d); //Formata para o padrão
	    	 
	         System.out.println(reader.hasNextLine());
	         conteudo.add("Últimas linhas lidas:");
	         
	         reader.nextLine();
	         while(reader.hasNextLine()) {
	        	 String dadosLinha = reader.nextLine();
	        	 if (dadosLinha.equals("")) {
	        		 break;
	        	 }
	        	 String[] parts = dadosLinha.split(sM);
	        	 
	        	 for(int j = 0; j < parts.length; j++) {
	        		 System.out.println("[" + parts[j] + "] "); //Não deu split
	        		 System.out.println("Chegou no parts " + j);
	        	 }
	        	 System.out.println("part[0] = " + parts[0] + " e caminho = " + caminho);
	        	 if (parts[0].equals(caminho)){ //Com o mesmo caminho, atualiza a linha e a data.
	        		 parts[1] = Integer.toString(linha);
					 parts[2] = data;
					 repetido = true;
	        	 }
	        	 System.out.println("Parts[0]: "+ parts[0] + "|| Parts[1]: " + parts[1] + "|| Parts[2]: " + parts[2]);
	        	 dadosLinha = parts[0] + sM + parts[1] + sM + parts[2];
	        	 conteudo.add(dadosLinha);
	         }
	         if (repetido == false) {
	        	 String dadosLinha = caminho + sM + linha + sM + data;
	        	 conteudo.add(dadosLinha);
	         }
	         for (int i = 0; i < conteudo.size(); i++) { //Teste
	        	 String a = (String) conteudo.get(i);
	        	 System.out.println(a);
	         }
	         //escritor.write();
	         Files.write(caminhover, conteudo, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
	         
	         // Closing the file writing connection
	         //escritor.close();
	         reader.close();
	
	         // Display message for successful execution of program on the console
	         System.out.println("\nFile is created successfully with the content.");
	     }
	     catch (IOException e) {
	         System.out.print(e.getMessage());
	     }
	 }
	 
	 public static int continuarLeitura(File d) {
	     File memorias = new File("C:\\Users\\cefet\\Downloads\\Memoria\\Memoria_linhas.txt");
		 String sM = "\t";
		try {
			Scanner reader = new Scanner(memorias);
			reader.nextLine();
			 while(reader.hasNextLine()) {
	        	 String dadosLinha = reader.nextLine();
	        	 if (dadosLinha.equals("")) {
	        		 break;
	        	 }
	        	 String[] parts = dadosLinha.split(sM);
	        	 if (parts[0].equals(d.getAbsolutePath())) {
	        		 return Integer.parseInt(parts[1]);
	        	 }
			 }
			 reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return 0; //Caso não tenha o caminho na memória
	}
}