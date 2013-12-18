package org.n3r.aoc.utils;

import com.jcraft.jsch.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Vector;

public class SftpDownloader extends FtpDownloader {
    @Override
    public boolean download() {
        Session session = null;
        Channel channel = null;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(getUser(), getHost(), getPort());
            session.setPassword(getPass());
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp c = (ChannelSftp) channel;
            if (!remoteFileExists(c)) return false;

            c.get(getRemoteFile(), getLocalFile().getAbsolutePath());
            return true;
        } catch (JSchException e) {
            throw new RuntimeException(e);
        } catch (SftpException e) {
            throw new RuntimeException(e);
        } finally {
            if (channel != null) channel.disconnect();
            if (session != null) session.disconnect();
        }
    }


    private boolean remoteFileExists(ChannelSftp channelSftp) throws SftpException {
        String path = FilenameUtils.getFullPath(getRemoteFile());
        path = StringUtils.isEmpty(path) ? "." : path;
        String name = FilenameUtils.getName(getRemoteFile());
        Vector<ChannelSftp.LsEntry> remoteFiles = channelSftp.ls(path);
        for (ChannelSftp.LsEntry entry : remoteFiles)
            if (entry.getFilename().equals(name)) return true;

        return false;
    }
}
