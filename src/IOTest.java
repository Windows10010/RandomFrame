import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class IOTest {
	static File file = null;
	FileWriter fw = null;
	public IOTest() {
		this.file = new File(this.getClass().getResource("").getPath() + "hop.txt");
		try {
			this.fw = new FileWriter(file,true);
			System.out.println("SUCCESS!\n" + this.getClass().getResource("").getPath());
			fw.write("hop\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("hop");
			System.exit(1);
		}finally {
			try {
				this.fw.close();
				System.out.println("closed");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new IOTest();
	}

}
