package com.inventra.dto;

public class DashboardAnalytics {

    private long totalProducts;
    private long totalStock;
    private long lowStockCount;
    private long totalStockIn;
    private long totalStockOut;

    public DashboardAnalytics(long totalProducts,
                              long totalStock,
                              long lowStockCount,
                              long totalStockIn,
                              long totalStockOut) {
        this.totalProducts = totalProducts;
        this.totalStock = totalStock;
        this.lowStockCount = lowStockCount;
        this.totalStockIn = totalStockIn;
        this.totalStockOut = totalStockOut;
    }

    public long getTotalProducts() { return totalProducts; }
    public long getTotalStock() { return totalStock; }
    public long getLowStockCount() { return lowStockCount; }
    public long getTotalStockIn() { return totalStockIn; }
    public long getTotalStockOut() { return totalStockOut; }
}
