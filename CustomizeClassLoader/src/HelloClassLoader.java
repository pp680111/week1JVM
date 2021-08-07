import java.io.*;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;

public class HelloClassLoader extends ClassLoader{
    public static void main(String[] args) {
        try {
            HelloClassLoader classLoader = new HelloClassLoader();
            Class helloClass = classLoader.findClass("Hello");
            Object helloObj = helloClass.newInstance();
            Method helloMethod = helloClass.getDeclaredMethod("hello");
            helloMethod.invoke(helloObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            byte[] byteCode = readXlassFile("Hello.xlass");
            decrypt(byteCode);
            return defineClass(name, byteCode, 0, byteCode.length);
        } catch (Exception e) {
            ClassNotFoundException ex = new ClassNotFoundException();
            ex.addSuppressed(e);
            throw ex;
        }

    }

    /**
     * 解密xlass文件中读取出来的二进制数据
     * @param encryptedBytes
     */
    private void decrypt(byte[] encryptedBytes) {
        if (encryptedBytes == null) {
            throw new IllegalArgumentException("argument is empty");
        }

        for (int i = 0; i < encryptedBytes.length; i++) {
            encryptedBytes[i] = (byte) (255 - encryptedBytes[i]);
        }
    }

    /**
     * 从指定路径读取出xlass文件中的内容
     * @param filePath 文件路径
     * @return xlass文件中的二进制数据
     * @throws IOException
     */
    private byte[] readXlassFile(String filePath) throws IOException, URISyntaxException {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("argument is empty");
        }

        URL fileUrl = this.getClass().getClassLoader().getResource(filePath);
        File xlassFile = new File(fileUrl.toURI());
        if (!xlassFile.exists()) {
            throw new FileNotFoundException(xlassFile.getAbsolutePath() + " not found");
        }
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(xlassFile))) {
            byte[] bytes = new byte[(int) xlassFile.length()];
            in.read(bytes);
            return bytes;
        } catch (IOException e) {
            throw e;
        }
    }
}
