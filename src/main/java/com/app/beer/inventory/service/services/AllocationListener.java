package com.app.beer.inventory.service.services;

import com.app.beer.inventory.service.config.JmsConfig;
import com.app.common.events.AllocateOrderRequest;
import com.app.common.events.AllocateOrderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * @author t0k02w6 on 08/06/21
 * @project mssc-beer-inventory-service
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AllocationListener {
    private final AllocationService allocationService;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.ALLOCATE_ORDER_QUEUE)
    public void listen(AllocateOrderRequest allocateOrderRequest) {
        AllocateOrderResult result = new AllocateOrderResult();
        result.setBeerOrderDto(allocateOrderRequest.getBeerOrderDto());

        try {
            Boolean allocationResult = allocationService.allocateOrder(allocateOrderRequest.getBeerOrderDto());
            if(allocationResult) {
                result.setPendingInventory(false);
            } else {
                result.setPendingInventory(true);
            }
            result.setAllocationError(false);
        } catch (Exception ex) {
            log.error("Allocation failed for Order Id:" + allocateOrderRequest.getBeerOrderDto().getId());
            result.setAllocationError(true);
        }

        jmsTemplate.convertAndSend(JmsConfig.ALLOCATE_ORDER_RESPONSE_QUEUE, result);
    }
}
