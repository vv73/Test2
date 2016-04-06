import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) throws IOException {
		ServerSocket server = new ServerSocket(8080);
		while (true) {
			System.out.println(System.getProperty("user.dir"));
			System.out.println("Waiting client");
			Socket socket = server.accept();
			System.out.println("Clent accepted");
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			byte[] array = new byte[100];
			in.read(array);
			String fileName = getFileName(new String(array));
			System.out.println(fileName);
			byte[] file = getFile(fileName);
			if (file != null) {
				out.write(("HTTP/1.0 200 Ok\n" + "Content-type: "
						+ getContentType(fileName) + "\nContent-length: "
						+ file.length + "\n\n").getBytes());
				out.write(file);
			} else {
				out.write(("HTTP/1.0 404 File Not Found\n\n").getBytes());
			}
		}
	}

	static byte[] getFile(String fileName) throws IOException {
		File file = new File(fileName);
		byte[] content = null;
		if (file.exists()) {
			content = new byte[(int) file.length()];
			FileInputStream fin = new FileInputStream(file);
			fin.read(content);

		}
		return content;

	}

	static String getFileName(String query) {
		String fileName = System.getProperty("user.dir").replace('\\', '/') +  query.split(" ")[1];
		if (fileName.charAt(fileName.length() - 1) == '/')
			fileName += "index.html";
		return fileName;
	}

	static String getContentType(String fileName) {
		System.out.println(fileName);
		String[] array = fileName.split("\\.");
		String ext = array[array.length - 1];
		String contentType = "text/plain";
		if (ext.equals("htm") || ext.equals("html")) {
			contentType = "text/html";
		}
		if (ext.equals("gif")) {
			contentType = "image/gif";
		}
		if (ext.equals("jpg")) {
			contentType = "image/jpeg";
		}
		return contentType;
	}
}
