package org.n3r.aoc.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FtpDownloader {
    private int port;
    private String host;
    private String user;
    private String pass;
    private String remoteFile;
    private File localFile;


    public FtpDownloader remote(String remoteFile) {
        this.remoteFile = remoteFile;
        return this;
    }

    public FtpDownloader local(File localFile) {
        this.localFile = localFile;
        return this;
    }


    /**
     * Download file from ftp server.
     *
     * @return true if ok.
     */
    public boolean download() {
        FTPClient ftpClient = new FTPClient();
        FileOutputStream fos = null;

        try {
            ftpClient.connect(host, port);
            ftpClient.login(user, pass);

            fos = new FileOutputStream(localFile);

            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            return ftpClient.retrieveFile(remoteFile, fos);
        } catch (IOException e) {
            throw new RuntimeException("ftp download error", e);
        } finally {
            IOUtils.closeQuietly(fos);
            try {
                ftpClient.disconnect();
            } catch (Exception e) {
                // Ignore
            }
        }
    }

    public FtpDownloader connect(String host) {
        this.host = host;
        return this;
    }

    public FtpDownloader login(String user, String pass) {
        this.user = user;
        this.pass = pass;
        return this;
    }

    public FtpDownloader connect(String host, int port) {
        this.host = host;
        this.port = port;
        return this;
    }

    public String getUser() {
        return user;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getPass() {
        return pass;
    }

    public File getLocalFile() {
        return localFile;
    }

    public String getRemoteFile() {
        return remoteFile;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
