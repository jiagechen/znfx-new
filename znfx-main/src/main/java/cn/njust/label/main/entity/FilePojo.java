package cn.njust.label.main.entity;

public class FilePojo { //大文件分片所需类 --- 用于接收前端发送的分片信息
    private String key;
    private String fileName;
    private Long shardIndex;
    private Long shardSize;
    private Long shardTotal;
    private Long size;
    private String suffix;
    @Override
    public String toString() {
        return "FilePojo{" +
                "key='" + key + '\'' +
                ", fileName='" + fileName + '\'' +
                ", shardIndex=" + shardIndex +
                ", shardSize=" + shardSize +
                ", shardTotal=" + shardTotal +
                ", size=" + size +
                ", suffix='" + suffix + '\'' +
                '}';
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getShardIndex() {
        return shardIndex;
    }

    public void setShardIndex(Long shardIndex) {
        this.shardIndex = shardIndex;
    }

    public Long getShardSize() {
        return shardSize;
    }

    public void setShardSize(Long shardSize) {
        this.shardSize = shardSize;
    }

    public Long getShardTotal() {
        return shardTotal;
    }

    public void setShardTotal(Long shardTotal) {
        this.shardTotal = shardTotal;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public FilePojo(String key, String fileName, Long shardIndex, Long shardSize, Long shardTotal, Long size, String suffix) {
        this.key = key;
        this.fileName = fileName;
        this.shardIndex = shardIndex;
        this.shardSize = shardSize;
        this.shardTotal = shardTotal;
        this.size = size;
        this.suffix = suffix;
    }

    public FilePojo() {
    }
}
