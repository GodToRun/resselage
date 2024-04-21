package yongbi.server.script;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.python.core.PyCode;
import org.python.util.PythonInterpreter;

import yongbi.client.ResourceLoader;
import yongbi.server.ServerInstance;

public class ScriptManager {
	private static final ExecutorService POOL = Executors.newCachedThreadPool();
	public static void run(ServerInstance srv, String file) {
		PythonInterpreter pi = new PythonInterpreter();
		POOL.submit(() -> {
			pi.set("self", new ScriptFunc(srv));
			String path = ResourceLoader.game + "script/" + file;
			try {
				FileReader fr = new FileReader(path);
				String str = "";
				int c;
				while ((c = fr.read()) > 0) {
					str += (char)c;
				}
				PyCode pyCode = pi.compile(str);
				//PyCode pyCode = pi.compile("self.sayOK(u'치맛속')");
				fr.close();
				pi.exec(pyCode);
			} catch (IOException e) {
				e.printStackTrace();
			}
			pi.close();
		});
		
	}
}
