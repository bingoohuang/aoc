package org.n3r.aoc.file.impl.input;

import org.apache.commons.lang3.StringUtils;
import org.n3r.aoc.AocContext;
import org.n3r.aoc.PropertiesAware;
import org.n3r.aoc.file.Input;
import org.n3r.aoc.utils.Aocs;
import org.n3r.aoc.utils.FtpDownloader;
import org.n3r.aoc.utils.TimeSpanParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.n3r.aoc.utils.Aocs.checkRequired;
import static org.n3r.aoc.utils.Aocs.substitute;

public class FtpInput implements Input, PropertiesAware {
    protected String ftpHost;
    protected int ftpPort = 21;
    protected String ftpPass;
    protected String ftpUser;
    protected String ftpRemote;
    private String ftpLocal;
    private long maxRetryTimeInMillis;
    private long retrySleepTimeInMillis;
    Logger logger = LoggerFactory.getLogger(FtpInput.class);
    private AocContext aocContext;

    @Override
    public InputStream read() {
        return getInputStream(new FtpDownloader());
    }

    protected InputStream getInputStream(FtpDownloader ftpDownloader) {
        long startTime = System.currentTimeMillis();
        File local = StringUtils.isNotEmpty(ftpLocal)
                ? new File(substitute(aocContext, ftpLocal)) : Aocs.tempFile();

        String realRemote = substitute(aocContext, ftpRemote);
        while (true) {

            boolean ok = ftpDownloader
                    .connect(ftpHost, ftpPort)
                    .login(ftpUser, ftpPass)
                    .remote(realRemote).local(local)
                    .download();

            if (ok) {
                logger.info("ftp dowloaded {} to local {}", realRemote, local);
                return toInputStream(local);
            }

            if (expireRetryMaxDuration(startTime)) {
                logger.error("expired reties {}", realRemote);
                throw new RuntimeException("DownFile Error");
            }

            logger.warn("waiting {} milliseconds and then retry remote {}", retrySleepTimeInMillis, realRemote);
            Aocs.sleepMilis(retrySleepTimeInMillis);
        }
    }

    private boolean expireRetryMaxDuration(long startTime) {
        long currentTimeInMillis = System.currentTimeMillis();
        return currentTimeInMillis - startTime > maxRetryTimeInMillis;
    }

    protected FileInputStream toInputStream(File local) {
        try {
            return new FileInputStream(local);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void startup(AocContext aocContext) {
        this.aocContext = aocContext;
        logger.info("startup {}:{}", ftpHost, ftpPort);
    }

    @Override
    public void shutdown() {
        logger.info("shutdown {}:{}", ftpHost, ftpPort);
    }

    @Override
    public void setProperties(Properties rootProperties, Properties properties) {
        ftpHost = properties.getProperty("host");
        checkRequired(ftpHost, "host");
        ftpPort = Integer.parseInt(properties.getProperty("port", "21"));

        ftpUser = properties.getProperty("user");
        checkRequired(ftpUser, "user");
        ftpPass = properties.getProperty("password");
        checkRequired(ftpPass, "password");

        ftpRemote = properties.getProperty("remote");
        checkRequired(ftpRemote, "remote");
        ftpLocal = properties.getProperty("local");

        String tempMaxRetryTimeInMillis = properties.getProperty("maxRetryTime", "1h");
        maxRetryTimeInMillis = TimeSpanParser.parse(tempMaxRetryTimeInMillis, TimeUnit.MILLISECONDS);

        String retrySleepTime = properties.getProperty("retrySleepTime", "60s");
        retrySleepTimeInMillis = TimeSpanParser.parse(retrySleepTime, TimeUnit.MILLISECONDS);
    }
}
