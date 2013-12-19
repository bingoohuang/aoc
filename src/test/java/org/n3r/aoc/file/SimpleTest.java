package org.n3r.aoc.file;

import com.google.common.base.Charsets;
import com.sleepycat.je.Database;
import org.apache.commons.io.FileUtils;
import org.apache.ftpserver.FtpServer;
import org.junit.Test;
import org.n3r.aoc.AocContext;
import org.n3r.aoc.file.impl.filter.text.TextFileFilter;
import org.n3r.aoc.file.impl.input.DirectInput;
import org.n3r.aoc.file.impl.input.FileInput;
import org.n3r.aoc.file.impl.input.FtpInput;
import org.n3r.aoc.file.impl.output.BdbOutput;
import org.n3r.aoc.file.impl.output.DirectOutput;
import org.n3r.aoc.utils.Bdbs;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SimpleTest {
    @Test
    public void directInDirectOut() {
        Properties config = new Properties();
        config.put("alias." + DirectInput.class.getSimpleName(), DirectInput.class.getName());
        config.put("input", "@" + DirectInput.class.getSimpleName() + "(context)");
        config.put("context.text", "a,b,c,d\na1,b1,c1");

        config.put("filter", "@" + TextFileFilter.class.getName() + "(text)");
        config.put("text.data.fields.split", ",");
        config.put("text.data.fields.from", "A,B,C");
        config.put("text.data.fields.to", "A,C");

        config.put("output", "@" + DirectOutput.class.getName());

        AocContext aocContext = new AocContext();

        Processor processor = Processor.fromProperties(config).aocContext(aocContext);

        processor.process();

        DirectOutput output = (DirectOutput) processor.output();
        assertThat(output.getContent(), is("A,C\na,c\na1,c1\n"));
    }

    @Test
    public void fileInputDirectOut() {
        Properties config = new Properties();
        config.put("input", "@" + FileInput.class.getName() + "(file)");
        config.put("file.path", "classpath:org/n3r/aoc/file/test.txt");

        config.put("filter", "@" + TextFileFilter.class.getName() + "(text)");
        config.put("text.data.fields.split", ",");
        config.put("text.data.fields.from", "A,B,C");
        config.put("text.data.fields.to", "A,C");

        config.put("output", "@" + DirectOutput.class.getName());

        Processor processor = Processor.fromProperties(config);

        processor.process();

        DirectOutput output = (DirectOutput) processor.output();
        assertThat(output.getContent(), is("A,C\na,c\na1,c1\n"));
    }

    @Test
    public void directInBdbOutput() {
        Properties config = new Properties();
        config.put("input", "@" + DirectInput.class.getName() + "(context)");
        config.put("context.text", "a,b,c,d\na1,b1,c1");

        config.put("filter", "@" + TextFileFilter.class.getName() + "(text)");
        config.put("text.data.fields.split", ",");
        config.put("text.data.fields.from", "A,B,C");
        config.put("text.data.fields.to", "A,C");

        config.put("output", "@" + BdbOutput.class.getName() + "(bdb)");
        String dbPath = TestUtils.tempDir().getAbsolutePath();
        config.put("bdb.dbpath", dbPath);
        String dbName = TestUtils.randomString(10);
        config.put("bdb.dbname", dbName);

        Processor processor = Processor.fromProperties(config);

        processor.process();

        Database database = Bdbs.openDb(dbPath, dbName, false);
        String value = Bdbs.get(database, "a");
        assertThat(value, is("[\"a\",\"c\"]"));
        value = Bdbs.get(database, "a1");
        assertThat(value, is("[\"a1\",\"c1\"]"));
        Bdbs.closeDbAndEvn(database);
    }

    @Test
    public void ftpInDirectOutput() throws IOException {
        int ftpPort = TestUtils.choosePort();
        File remoteFile = new File("./20131217.remote.txt");
        remoteFile.deleteOnExit();
        new File("./20131217.local.txt").deleteOnExit();

        FileUtils.writeStringToFile(remoteFile, "a,b,c,d\na1,b1,c1", Charsets.UTF_8);
        FtpServer ftpServer = TestUtils.startFtpServer(".", ftpPort, "user", "pass");


        Properties config = new Properties();
        config.put("alias." + FtpInput.class.getSimpleName(), FtpInput.class.getName());
        config.put("input", "@" + FtpInput.class.getSimpleName() + "(ftp)");
        config.put("ftp.host", "127.0.0.1");
        config.put("ftp.port", "" + ftpPort);
        config.put("ftp.user", "user");
        config.put("ftp.password", "pass");
        config.put("ftp.remote", "${checkDay}.remote.txt");
        config.put("ftp.local", "${checkDay}.local.txt");

        config.put("filter", "@" + TextFileFilter.class.getName() + "(text)");
        config.put("text.data.fields.split", ",");
        config.put("text.data.fields.from", "A,B,C");
        config.put("text.data.fields.to", "A,C");

        config.put("output", "@" + DirectOutput.class.getName());

        AocContext aocContext = new AocContext();
        aocContext.setCheckDay("20131217");

        Processor processor = Processor.fromProperties(config).aocContext(aocContext);
        processor.process();

        DirectOutput output = (DirectOutput) processor.output();
        assertThat(output.getContent(), is("A,C\na,c\na1,c1\n"));

        ftpServer.stop();
    }
}
