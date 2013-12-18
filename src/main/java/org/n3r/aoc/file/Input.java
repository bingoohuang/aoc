package org.n3r.aoc.file;

import org.n3r.aoc.AocContext;
import org.n3r.aoc.LifeCycle;

import java.io.InputStream;

public interface Input extends LifeCycle{
    InputStream read(AocContext aocContext);
}
