package me.icymint.sage.base.rest.entity;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

import java.util.List;

/**
 * Created by daniel on 16/9/4.
 */
public class PaginatorBound extends PageBounds {

    public PaginatorBound(int page, int size, List<Order> orders, boolean containsTotal) {
        super(page, size, orders, containsTotal);
    }

    public PaginatorBound(int page, int size) {
        super(page, size);
    }

}
