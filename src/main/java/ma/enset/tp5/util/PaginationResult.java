package ma.enset.tp5.util;

import java.util.List;

public class PaginationResult<T> {
    private final List<T> data;
    private final int page;
    private final int size;
    private final long totalElements;

    public PaginationResult(List<T> data, int page, int size, long totalElements) {
        this.data = data;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
    }

    public List<T> getData() { return data; }
    public int getPage() { return page; }
    public int getSize() { return size; }
    public long getTotalElements() { return totalElements; }
    public int getTotalPages() { return (int) Math.ceil((double) totalElements / size); }
}
