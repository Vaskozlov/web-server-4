package org.vaskozov.lab4.lib;

public class AreaChecker {
    private final double x;
    private final double y;
    private final double r;
    private final double halfR;

    public AreaChecker(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.halfR = r / 2;
    }

    private boolean isInFirstQuarter() {
        if (x >= 0 && y >= 0) {
            return (x <= r && y <= halfR - x / 2);
        }

        return false;
    }

    private boolean isInSecondQuarter() {
        return false; //NOSONAR
    }

    private boolean isInThirdQuarter() {
        if (x < 0 && y <= 0) {
            return Math.sqrt(x * x + y * y) <= halfR;
        }

        return false;
    }

    private boolean isInFourthQuarter() {
        if (x >= 0 && y < 0) {
            return (x <= r && -y <= halfR);
        }

        return false;
    }

    public boolean isInArea() {
        return isInFirstQuarter()
                || isInSecondQuarter()
                || isInThirdQuarter()
                || isInFourthQuarter();
    }
}
