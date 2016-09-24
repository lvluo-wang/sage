package me.icymint.sage.base.spec.internal.api;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import me.icymint.sage.base.spec.entity.BaseJobEntity;

import java.util.List;

/**
 * Created by daniel on 2016/9/24.
 */
public interface BatchJob<E extends BaseJobEntity<E>> {
    List<E> getRecords(PageBounds pageBounds);

    void updateNextScanTime(Long id);

    void handle(Long record) throws Exception;
}
