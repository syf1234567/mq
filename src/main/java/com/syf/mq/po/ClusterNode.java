package com.syf.mq.po;

public class ClusterNode {
    private long diskFree;//磁盘空闲
    private long diskFreeLimit;
    private long fdUsed;//句柄使用数
    private long fdTotal;
    private long socketsUsed;//Socket 使用数
    private long socketsTotal;
    private long memoryUsed;//内存使用值
    private long memoryLimit;
    private long procUsed;//Erlang进程使用数
    private long procTotal;

    @Override
    public String toString() {
        return "ClusterNode{" +
                "diskFree=" + diskFree +
                ", diskFreeLimit=" + diskFreeLimit +
                ", fdUsed=" + fdUsed +
                ", fdTotal=" + fdTotal +
                ", socketsUsed=" + socketsUsed +
                ", socketsTotal=" + socketsTotal +
                ", memoryUsed=" + memoryUsed +
                ", memoryLimit=" + memoryLimit +
                ", procUsed=" + procUsed +
                ", procTotal=" + procTotal +
                '}';
    }
}
