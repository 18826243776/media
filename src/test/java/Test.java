import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.security.KeyStore;

/**
 * @version 1.0
 * @author: 千里明月
 * @date: 2019/8/26 14:47
 */
public class Test {
    public static void main(String[] args) throws IOException {
//        URL url = new URL("https://www.baidu.com");
//        InputStream inputStream = getHttpsConnection(url).getInputStream();
//        byte[] buf = new byte[1024];
//        int read = inputStream.read(buf);
//        while (read != -1) {
//            System.out.println(new String(buf));
//            read=inputStream.read(buf);
//        }
        String defaultType = KeyStore.getDefaultType();
        System.out.println(defaultType);
    }

    /**
     * 获取https连接
     * @param url
     * @return
     * @throws IOException
     */
    public static HttpsURLConnection getHttpsConnection(URL url) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        return connection;
    }
}
