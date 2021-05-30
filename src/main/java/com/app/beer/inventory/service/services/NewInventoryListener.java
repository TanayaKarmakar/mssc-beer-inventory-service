package com.app.beer.inventory.service.services;

import com.app.beer.inventory.service.config.JmsConfig;
import com.app.beer.inventory.service.domain.BeerInventory;
import com.app.beer.inventory.service.repositories.BeerInventoryRepository;
import com.app.common.events.NewInventoryEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author t0k02w6 on 27/05/21
 * @project mssc-beer-inventory-service
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class NewInventoryListener {
    private final BeerInventoryRepository beerInventoryRepository;

    @JmsListener(destination = JmsConfig.NEW_INVENTORY_QUEUE)
    public void listen(NewInventoryEvent newInventoryEvent) {
        log.debug("Got Inventory: " + newInventoryEvent.toString());

        BeerInventory beerInventory = new BeerInventory();
        beerInventory.setBeerId(newInventoryEvent.getBeerDto().getId());
        beerInventory.setUpc(newInventoryEvent.getBeerDto().getUpc());
        beerInventory.setQuantityOnHand(newInventoryEvent.getBeerDto().getQuantityOnHand());

        beerInventoryRepository.save(beerInventory);
    }
}
