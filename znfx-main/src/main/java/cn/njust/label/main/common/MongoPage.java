package cn.njust.label.main.common;

import java.util.List;

//mongodb分页查询对象
public class MongoPage<T> {
    private long currentPage;//第几页
    private long pageSize;//每页多少数据
    private long total;//数据库总共有多少数据
    private List<T> list;//查询到的数据

    public MongoPage(long currentPage, long pageSize, long total, List<T> list) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.total = total;
        this.list = list;
    }

    public MongoPage() {
    }

    public long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
