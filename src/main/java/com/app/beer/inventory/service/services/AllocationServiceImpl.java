package com.app.beer.inventory.service.services;

import com.app.beer.inventory.service.domain.BeerInventory;
import com.app.beer.inventory.service.repositories.BeerInventoryRepository;
import com.app.common.model.BeerOrderDto;
import com.app.common.model.BeerOrderLineDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author t0k02w6 on 08/06/21
 * @project mssc-beer-inventory-service
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AllocationServiceImpl implements AllocationService{
    private final BeerInventoryRepository beerInventoryRepository;

    @Override
    public Boolean allocateOrder(BeerOrderDto beerOrderDto) {
        AtomicInteger totalOrdered = new AtomicInteger();
        AtomicInteger totalAllocated = new AtomicInteger();

        beerOrderDto.getBeerOrderLines().forEach(beerOrderLine -> {
            int orderedQty = Objects.isNull(beerOrderLine.getOrderQuantity()) ? 0: beerOrderLine.getOrderQuantity();
            int allocatedQty = Objects.isNull(beerOrderLine.getQuantityAllocated()) ? 0: beerOrderLine.getQuantityAllocated();

            if(orderedQty - allocatedQty > 0) {
                allocateBeerOrderLine(beerOrderLine);
            }
            totalOrdered.set(totalOrdered.get() + beerOrderLine.getOrderQuantity());
            totalAllocated.set(totalAllocated.get() + allocatedQty);
        });
        return totalOrdered.get() == totalAllocated.get();
    }

    @Override
    public void deallocateOrder(BeerOrderDto beerOrderDto) {
        beerOrderDto.getBeerOrderLines().forEach(beerOrderLineDto -> {
            BeerInventory beerInventory = new BeerInventory();
            beerInventory.setBeerId(beerOrderLineDto.getBeerId());
            beerInventory.setUpc(beerOrderLineDto.getUpc());
            beerInventory.setQuantityOnHand(beerOrderLineDto.getQuantityAllocated());

            BeerInventory savedInventory = beerInventoryRepository.save(beerInventory);

            log.debug("Saved Inventory for beer upc: " + savedInventory.getUpc() + " inventory id: " + savedInventory.getId());
        });
    }

    private void allocateBeerOrderLine(BeerOrderLineDto beerOrderLineDto) {
        List<BeerInventory> beerInventoryList = beerInventoryRepository.findAllByUpc(beerOrderLineDto.getUpc());

        beerInventoryList.forEach(beerInventory -> {
            int inventory = Objects.isNull(beerInventory.getQuantityOnHand()) ? 0: beerInventory.getQuantityOnHand();
            int orderQty = Objects.isNull(beerOrderLineDto.getOrderQuantity()) ? 0: beerOrderLineDto.getOrderQuantity();
            int allocatedQty = Objects.isNull(beerOrderLineDto.getQuantityAllocated()) ? 0: beerOrderLineDto.getQuantityAllocated();
            int qtyToAllocate = orderQty - allocatedQty;

            if (inventory >= qtyToAllocate) { // full allocation
                inventory = inventory - qtyToAllocate;
                beerOrderLineDto.setQuantityAllocated(orderQty);
                beerInventory.setQuantityOnHand(inventory);

                beerInventoryRepository.save(beerInventory);
            } else if (inventory > 0) { //partial allocation
                beerOrderLineDto.setQuantityAllocated(allocatedQty + inventory);
                beerInventory.setQuantityOnHand(0);

            }

            if (beerInventory.getQuantityOnHand() == 0) {
                beerInventoryRepository.delete(beerInventory);
            }
        });
    }
}
