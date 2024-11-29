package org.vaskozov.lab4.service;

import jakarta.ejb.Singleton;
import org.vaskozov.lab4.lib.AreaChecker;

@Singleton
public class InAreaChecker implements InAreaCheckerInterface {
    @Override
    public boolean check(double x, double y, double r) {
        return (new AreaChecker(x, y, r)).isInArea();
    }
}
