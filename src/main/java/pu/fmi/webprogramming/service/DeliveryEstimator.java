package pu.fmi.webprogramming.service;

import org.springframework.stereotype.Component;
import pu.fmi.webprogramming.model.Delivery;
import pu.fmi.webprogramming.model.Warehouse;
import pu.fmi.webprogramming.model.Customer;

import java.time.LocalDateTime;

@Component
public class DeliveryEstimator {

  public LocalDateTime estimateArrivalTime(Delivery delivery) {

   

    if (delivery == null) {
      return null;
    }

    LocalDateTime createdAt = delivery.getCreatedAt();
    if (createdAt == null) {
      // If createdAt is not set, fallback to now
      createdAt = LocalDateTime.now();
    }

    Warehouse warehouse = delivery.getWarehouse();
    String warehouseCity = warehouse != null ? warehouse.getCity() : null;
    Customer customer = delivery.getCustomer();
    String customerCity = customer != null ? customer.getCity() : null;

    // Default: different cities -> +3 days, same city -> +1 day
    long daysToAdd = 3;
    if (warehouseCity != null && customerCity != null && warehouseCity.equals(customerCity)) {
      daysToAdd = 1;
    }

    LocalDateTime estimated = createdAt.plusDays(daysToAdd);

    // If no courier assigned, add 2 extra days
    if (delivery.getCourier() == null) {
      estimated = estimated.plusDays(2);
    }

    return estimated;
  }
}
