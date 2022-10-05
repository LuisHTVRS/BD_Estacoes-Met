package pacote1;

import java.nio.file.*;
import java.nio.file.FileSystems;
import java.util.concurrent.TimeUnit;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Notificador {
	public static ArrayList listaDeEspera = new ArrayList();
	public int num_estacao = 61;
	
	public void verPasta (Path p) {
		try {
			WatchService watchService =  p.getFileSystem().newWatchService();
			WatchKey watchKey = p.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);
			System.out.println("Notificador ativado.");
			
			while (true) {
				//O 'for' verificando se ainda há eventos nesse arquivo
				//Pode acontecer logo no ínicio, mas permanece para testes.
				for (WatchEvent<?> event : watchKey.pollEvents()) {

					// Acha o nome do arquivo do evento
					WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;

					Path fileName = pathEvent.context();
					Path dir = (Path) watchKey.watchable();
					Path fullPath = dir.resolve((Path) event.context());
					System.out.println(fullPath);

					// Verifica tipo do evento
					WatchEvent.Kind<?> kind = event.kind();
					
					ArrayList x = new ArrayList(); //Nova lista para conter arquivo e tipo de edição
					x.add(fullPath);

					// Faz o necessário de acordo com o tipo
					if (kind == StandardWatchEventKinds.ENTRY_CREATE) {

						System.out.println("A new file is created : " + fileName);
						//Move para a lista de espera
						x.add("criado");
					}
			
					if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {

						System.out.println("A file has been modified: " + fileName);
						x.add("modificado");
					}
					
					//Verifica se o arquivo já está na lista de inserção
					boolean repetido = false;
					for(int i = 0; i < listaDeEspera.size(); i++) {
						ArrayList al = (ArrayList) listaDeEspera.get(i);
						Path tempPath = (Path) al.get(0);
						if (fullPath.equals(tempPath)) {
							repetido = true;
						}
					}
					
					if (repetido == false) {
						x.add(num_estacao);
						listaDeEspera.add(x); // Adiciona lista de arquivo e tipo em uma lista de espera
					}
					
					
					//Imprime lista de espera para própositos de teste
					for(int i = 0; i < listaDeEspera.size(); i++) {
						ArrayList m = (ArrayList) listaDeEspera.get(i);
						Path one = (Path) m.get(0);
						String two = (String) m.get(1);
						int three = (int) m.get(2);
						System.out.println(" [" + one + "] [" + two + "] [" + three + "]");
					}
						
				}

				// STEP8: Reset the watch key everytime for continuing to use it for further event polling
				boolean valid = watchKey.reset();
				if (!valid) {
					break;
				}
				
			}
		}
		catch(Exception e) {
			System.out.println(e + "errou " + p);
		}
	}
	

}