package com.bimbiya.server.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DashboardResponseDTO {
    private Long deleteUsers;
    private Long activeUsers;
    private Long inactiveUsers;
    private Long rejectedOrders;
    private Long pendingOrders;
    private Long completedOrders;
    private Long processingOrders;
    private Long shippedOrders;
    private Long todayOrders;
}