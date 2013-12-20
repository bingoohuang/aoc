package org.n3r.aoc.file.impl.input;

import org.n3r.aoc.utils.SftpDownloader;

import java.io.InputStream;

public class SftpInput extends FtpInput {
    public SftpInput() {
        ftpPort = 22;
    }

    @Override
    public InputStream read() {
        return getInputStream(new SftpDownloader());
    }
}
