package org.n3r.aoc.check.impl.diff;

public class EqualsUtils {
    public static final double DELTA = 0.00000001;

    public static boolean equalsTo(Number d1, Number d2) {
        return equalsTo(d1.doubleValue(), d2.doubleValue());
    }

    public static boolean equalsTo(Number d1, double d2) {
        return equalsTo(d1.doubleValue(), d2);
    }

    public static boolean equalsTo(double d1, double d2) {
        if (Double.compare(d1, d2) == 0) return true;
        if ((Math.abs(d1 - d2) <= DELTA)) return true;

        return false;
    }

    public static boolean equalsTo(Object leftValue, Object rightValue) {
        if (leftValue instanceof Number && rightValue instanceof Number)
            return equalsTo((Number) leftValue, (Number) rightValue);

        if (leftValue instanceof Number) {
            try {
                double rightDouble = Double.parseDouble("" + rightValue);
                return equalsTo((Number) leftValue, rightDouble);
            } catch (NumberFormatException e) { /* Ignore */ }
        } else if (rightValue instanceof Number) {
            try {
                double leftDouble = Double.parseDouble("" + leftValue);
                return equalsTo((Number) rightValue, leftDouble);
            } catch (NumberFormatException e) { /* Ignore */ }
        }

        if (leftValue != null) return leftValue.equals(rightValue);
        if (rightValue != null) return rightValue.equals(leftValue);
        return true;
    }
}
