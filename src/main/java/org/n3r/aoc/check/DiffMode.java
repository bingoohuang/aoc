package org.n3r.aoc.check;

/**
 * Difference State.
 */
public enum DiffMode {
    /**
     * Order only exists on left data.
     */
    OnlyLeft("LO"),
    /**
     * Order exists both on left and right data but with differences.
     */
    Diff("LR"),
    /**
     * Order exists both on left and right without differences.
     */
    Balance("OO"),
    /**
     * Order only exixts on right data.
     */
    OnlyRight("OR");


    private final String name;

    DiffMode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
