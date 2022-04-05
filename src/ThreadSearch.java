import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ThreadSearch extends Thread {
	
	// Variáveis privadas da classe
	private final String pasta;
	private final long tamanho;
	private final String arquivo;
	private final String extensao;
	private final String threadName = Thread.currentThread().getName();

	// Construtor da classe que também realiza o start da thread e o join na main
	public ThreadSearch(String pasta,
						String arquivo,
						long tamanho,
						String extensao) {
		this.arquivo = arquivo;
		this.pasta = pasta;
		this.tamanho = tamanho;
		this.extensao = extensao;
		
		start();
		
		try {
			join();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	// Função de busca
	public void busca() {
		// Faz o parse do Path que o arquivo via ser buscado
		Path currentRelativePath = Paths.get(pasta);
		// Transforma o path em um arquivo para que possa ser manipulado
		File dir = new File(currentRelativePath.toAbsolutePath().toString());
		// Filtra os arquivos de acordo com parametros
		File[] matches = dir.listFiles(new FilenameFilter() {
			
			// Parametros de aceitacao
			@Override
			public boolean accept(File dir, String name) {
				return (name.contains(arquivo) || dir.length() == tamanho || name.endsWith(extensao));
			}
		});
		
		// Se nao for encontrado o arquivo desejado, o codigo ira entrar na condicao
		if (matches == null || matches.length == 0) {
			System.out.println("Não encontramos na thread: " + this.threadName);
			try {
				// Filtra somente os diretorios
				File[] dirFound = dir.listFiles(new FilenameFilter() {
					
					@Override
					public boolean accept(File dir, String name) {
						return dir.isDirectory();
					}
				});
				// Faz uma chamada recursiva onde a classe se instancia para cada diretorio dentro do path em que estamos
				for(File oneDir : dirFound) {
					new ThreadSearch(oneDir.getAbsolutePath(), arquivo, tamanho, extensao);
				}
			}catch(Exception e) {
				System.out.println("Arquivo não encontrado");
				e.getSuppressed();
			}
			
		}
		
		int i = 0;
		// Executa quando encontra alguma resposta que satisfaz as condicoes
        for(File f : matches) {
            System.out.println("Arquivo encontrado:" + f.getName());
            System.out.println("Na Thread: " + this.threadName);
            System.out.println("Na Pasta: " + f.getAbsolutePath());
            NumberFormat formatter = new DecimalFormat("#0.00000");
            System.out.println("Encontrado em: " + 
            formatter.format(((System.currentTimeMillis() - Main.getStartTime()) / 1000d)) +
            " segundos");
            // Trava a execucao no ultimo elemento
            if(i++ == matches.length - 1){
                System.exit(0);
            }
        }
	}
	@Override
	public void run() {
		busca(); 
		super.run();
	}
}
