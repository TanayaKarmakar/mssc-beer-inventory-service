package com.app.beer.inventory.service.services;

import com.app.common.models.BeerOrderDto;

/**
 * @author t0k02w6 on 08/06/21
 * @project mssc-beer-inventory-service
 */
public interface AllocationService {
    Boolean allocateOrder(BeerOrderDto beerOrderDto);

    void deallocateOrder(BeerOrderDto beerOrderDto);
}
