package cn.njust.label.main.service.impl;

import org.springframework.data.domain.Sort;

import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.io.Serializable;

/**
 * @program:
 * @description:
 * @
 **/
public class MongoPageable implements Serializable, Pageable {
    @Override
    public int getNumberOfPages() {
        return 0;
    }

    @Override
    public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
        return null;
    }

    @Override
    public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
        return null;
    }
}
