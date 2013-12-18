package org.n3r.aoc.file;

import org.apache.commons.io.IOUtils;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Random;

public class TestUtils {

    public static String IoTmpDir = System.getProperty("java.io.tmpdir");
    public static Random random = new Random();


    /**
     * Choose an available port
     */
    public static int choosePort() {
        try {
            ServerSocket serverSocket = new ServerSocket(0);
            int localPort = serverSocket.getLocalPort();
            IOUtils.closeQuietly(serverSocket);
            return localPort;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create a temporary directory
     */
    public static File tempDir() {
        File f = new File(IoTmpDir, "aoc-" + random.nextInt(1000000));
        f.mkdirs();
        f.deleteOnExit();
        return f;
    }

    /**
     * Create a temporary file
     */
    public static File tempFile(File dir) {
        File f;
        try {
            f = File.createTempFile("aoc", ".tmp", dir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        f.deleteOnExit();
        return f;
    }

    public static String Letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public static String Digits = "0123456789";
    public static String LettersAndDigits = Letters + Digits;

    /* A consistent random number generator to make tests repeatable */
    public static Random seededRandom = new Random(192348092834L);

    /**
     * Generate a random string of letters and digits of the given length
     *
     * @param len The length of the string
     * @return The random string
     */
    public static String randomString(int len) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < len; ++i)
            b.append(LettersAndDigits.charAt(seededRandom.nextInt(LettersAndDigits.length())));

        return b.toString();
    }

    public static FtpServer startFtpServer(String homePath, int port, String user, String pass) {
        FtpServerFactory serverFactory = new FtpServerFactory();
        ListenerFactory listenerFactory = new ListenerFactory();
        listenerFactory.setPort(port);

        serverFactory.addListener("default", listenerFactory.createListener());
        serverFactory.setUserManager(new FtpUserManager(homePath, user, pass));

        FtpServer ftpServer = serverFactory.createServer();
        try {
            ftpServer.start();
            return ftpServer;
        } catch (FtpException e) {
            throw new RuntimeException(e);
        }
    }
}
