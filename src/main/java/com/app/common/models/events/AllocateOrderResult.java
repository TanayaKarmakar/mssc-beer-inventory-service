package com.app.common.models.events;

import com.app.common.models.BeerOrderDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author t0k02w6 on 08/06/21
 * @project mssc-beer-inventory-service
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllocateOrderResult {
    private BeerOrderDto beerOrderDto;
    private Boolean allocationError = false;
    private Boolean pendingInventory = false;
}
