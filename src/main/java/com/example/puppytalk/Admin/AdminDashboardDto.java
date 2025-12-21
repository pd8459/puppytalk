package com.example.puppytalk.Admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminDashboardDto {
    private Long totalSales;
    private long totalOrders;
    private long totalMembers;
    private long totalCancels;
}
