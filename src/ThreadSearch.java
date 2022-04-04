import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ThreadSearch extends Thread {
	
	private final String pasta;
	private final String arquivo;
	private final String threadName = Thread.currentThread().getName();

	public ThreadSearch(String pasta,
						String arquivo) {
		this.arquivo = arquivo;
		this.pasta = pasta;
		
		start();
		
		try {
			join();
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	public void busca(String arquivo,
					  String pasta) {
		Path currentRelativePath = Paths.get(pasta);
		File dir = new File(currentRelativePath.toAbsolutePath().toString());
		File[] matches = dir.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith(arquivo);
			}
		});
		if (matches == null || matches.length == 0) {
			System.out.println("Não encontramos na thread: " + this.threadName);
			try {
				File[] dirFound = dir.listFiles(new FilenameFilter() {
					
					@Override
					public boolean accept(File dir, String name) {
						return dir.isDirectory();
					}
				});
				for(File oneDir : dirFound) {
					new ThreadSearch(oneDir.getAbsolutePath(), arquivo);
				}
			}catch(Exception e) {
				System.out.println("Arquivo não encontrado");
				e.getSuppressed();
			}
			
		}
		
        for(File f : matches) {
            System.out.println("Achei:" + f.getName());
            System.out.println("Na Thread:" + this.threadName);
            System.out.println(f.getAbsolutePath());
            NumberFormat formatter = new DecimalFormat("#0.00000");
            System.out.println("Encontrado em: " + 
            formatter.format(((System.currentTimeMillis() - Main.getStartTime()) / 1000d)) +
            " segundos");
            System.exit(0);
        }
	}
	@Override
	public void run() {
		busca(arquivo, pasta); 
		super.run();
	}
}
