package com.app.beer.inventory.service.services;

import com.app.beer.inventory.service.config.JmsConfig;
import com.app.common.events.DeallocateOrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author t0k02w6 on 08/06/21
 * @project mssc-beer-inventory-service
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class DeallocationListener {
    private final AllocationService allocationService;

    @JmsListener(destination = JmsConfig.DEALLOCATE_ORDER_QUEUE)
    public void listen(DeallocateOrderRequest request){
        allocationService.deallocateOrder(request.getBeerOrderDto());
    }

}
