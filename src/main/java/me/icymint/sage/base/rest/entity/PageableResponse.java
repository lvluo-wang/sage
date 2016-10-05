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
public class PageableResponse<E> {
    private Integer totalCount;
    private Integer totalPages;
    private Integer page;
    private Integer size;
    private E[] items;
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

    public static <E> PageableResponse<E> of(List<E> list) {
        return of(list, Function.identity());
    }

    @SuppressWarnings("unchecked")
    public static <T, R> PageableResponse<R> of(List<T> list, Function<T, R> apply) {
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
        PageableResponse<R> entity = new PageableResponse<>();
        entity.setTotalCount(paginator.getTotalCount());
        entity.setTotalPages(paginator.getTotalPages());
        entity.setPage(paginator.getPage());
        entity.setSize(paginator.getLimit());
        entity.setItems((R[]) list
                .stream()
                .map(apply)
                .collect(Collectors.toList())
                .toArray());
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

    public Integer getTotalCount() {
        return totalCount;
    }

    public PageableResponse setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
        return this;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public PageableResponse setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public Integer getPage() {
        return page;
    }

    public PageableResponse setPage(Integer page) {
        this.page = page;
        return this;
    }

    public Integer getSize() {
        return size;
    }

    public PageableResponse setSize(Integer size) {
        this.size = size;
        return this;
    }

    public E[] getItems() {
        return items;
    }

    public PageableResponse setItems(E[] items) {
        this.items = items;
        return this;
    }

    public Integer getStartRow() {
        return startRow;
    }

    public PageableResponse setStartRow(Integer startRow) {
        this.startRow = startRow;
        return this;
    }

    public Integer getEndRow() {
        return endRow;
    }

    public PageableResponse setEndRow(Integer endRow) {
        this.endRow = endRow;
        return this;
    }

    public Integer getOffset() {
        return offset;
    }

    public PageableResponse setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    public Integer[] getSlider() {
        return slider;
    }

    public PageableResponse setSlider(Integer[] slider) {
        this.slider = slider;
        return this;
    }

    public Integer getPrePage() {
        return prePage;
    }

    public PageableResponse setPrePage(Integer prePage) {
        this.prePage = prePage;
        return this;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public PageableResponse setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
        return this;
    }

    public Boolean getHasNextPage() {
        return hasNextPage;
    }

    public PageableResponse setHasNextPage(Boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
        return this;
    }

    public Boolean getHasPrePage() {
        return hasPrePage;
    }

    public PageableResponse setHasPrePage(Boolean hasPrePage) {
        this.hasPrePage = hasPrePage;
        return this;
    }

    public PageableResponse setFirstPage(Boolean firstPage) {
        isFirstPage = firstPage;
        return this;
    }

    public PageableResponse setLastPage(Boolean lastPage) {
        isLastPage = lastPage;
        return this;
    }
}
