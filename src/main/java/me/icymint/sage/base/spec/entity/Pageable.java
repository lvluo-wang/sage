package me.icymint.sage.base.spec.entity;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

import java.util.List;

/**
 * Created by daniel on 2016/10/5.
 */
public class Pageable extends PageBounds {
    private final int totalCount;

    public Pageable(int page, int size, List<Order> orders, int total) {
        super(page, size, orders, total < 0);
        this.totalCount = total;
    }

    public int getTotalCount() {
        return totalCount;
    }
}
