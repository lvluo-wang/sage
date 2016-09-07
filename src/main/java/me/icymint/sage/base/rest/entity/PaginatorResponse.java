package me.icymint.sage.base.rest.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by daniel on 16/9/3.
 */
public class PaginatorResponse<E> {
    private Integer totalCount;
    private Integer totalPages;
    private Integer page;
    private Integer limit;
    private List<E> items;
    private Integer startRow;
    private Integer endRow;
    private Integer offset;
    private Integer[] slider;
    private Integer prePage;
    private Integer nextPage;
    @JsonProperty("isFirstPage")
    private Boolean isFirstPage;
    private Boolean hasNextPage;
    private Boolean hasPrePage;
    @JsonProperty("isLastPage")
    private Boolean isLastPage;

    public Integer getTotalCount() {
        return totalCount;
    }

    public PaginatorResponse setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
        return this;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public PaginatorResponse setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public Integer getPage() {
        return page;
    }

    public PaginatorResponse setPage(Integer page) {
        this.page = page;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public PaginatorResponse setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public List<E> getItems() {
        return items;
    }

    public PaginatorResponse setItems(List<E> items) {
        this.items = items;
        return this;
    }

    public Integer getStartRow() {
        return startRow;
    }

    public PaginatorResponse setStartRow(Integer startRow) {
        this.startRow = startRow;
        return this;
    }

    public Integer getEndRow() {
        return endRow;
    }

    public PaginatorResponse setEndRow(Integer endRow) {
        this.endRow = endRow;
        return this;
    }

    public Integer getOffset() {
        return offset;
    }

    public PaginatorResponse setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    public Integer[] getSlider() {
        return slider;
    }

    public PaginatorResponse setSlider(Integer[] slider) {
        this.slider = slider;
        return this;
    }

    public Integer getPrePage() {
        return prePage;
    }

    public PaginatorResponse setPrePage(Integer prePage) {
        this.prePage = prePage;
        return this;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public PaginatorResponse setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
        return this;
    }

    public Boolean getHasNextPage() {
        return hasNextPage;
    }

    public PaginatorResponse setHasNextPage(Boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
        return this;
    }

    public Boolean getHasPrePage() {
        return hasPrePage;
    }

    public PaginatorResponse setHasPrePage(Boolean hasPrePage) {
        this.hasPrePage = hasPrePage;
        return this;
    }

    public PaginatorResponse setFirstPage(Boolean firstPage) {
        isFirstPage = firstPage;
        return this;
    }

    public PaginatorResponse setLastPage(Boolean lastPage) {
        isLastPage = lastPage;
        return this;
    }


    public static <E> PaginatorResponse<E> of(List<E> list) {
        return of(list, Function.identity());
    }

    public static <T, R> PaginatorResponse<R> of(List<T> list, Function<T, R> apply) {
        if (list == null) {
            return null;
        }
        Paginator paginator;
        if (list instanceof PageList) {
            paginator = ((PageList) list).getPaginator();
        } else {
            int size = list.size();
            paginator = new Paginator(1, size, size);
        }
        PaginatorResponse<R> entity = new PaginatorResponse<>();
        entity.setTotalCount(paginator.getTotalCount());
        entity.setTotalPages(paginator.getTotalPages());
        entity.setPage(paginator.getPage());
        entity.setLimit(paginator.getLimit());
        entity.setItems(list.stream().map(apply).collect(Collectors.toList()));
        entity.setStartRow(paginator.getStartRow());
        entity.setEndRow(paginator.getEndRow());
        entity.setOffset(paginator.getOffset());
        entity.setSlider(paginator.getSlider());
        entity.setPrePage(paginator.getPrePage());
        entity.setNextPage(paginator.getNextPage());
        entity.setFirstPage(paginator.isFirstPage());
        entity.setHasNextPage(paginator.isHasNextPage());
        entity.setHasPrePage(paginator.isHasPrePage());
        entity.setLastPage(paginator.isLastPage());
        return entity;
    }
}
