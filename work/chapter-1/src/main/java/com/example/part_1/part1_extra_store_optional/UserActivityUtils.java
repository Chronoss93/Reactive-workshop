package com.example.part_1.part1_extra_store_optional;


import com.example.annotations.Complexity;
import com.example.annotations.Optional;
import rx.Observable;

import static com.example.annotations.Complexity.Level.MEDIUM;

public class UserActivityUtils {

    @Optional
    @Complexity(MEDIUM)
    public static Observable<Product> findMostExpansivePurchase(Observable<Order> ordersHistory) {
        // TODO: flatten all Products inside Orders and using reduce find one with the highest price

        return ordersHistory.flatMap(order -> Observable.from(order.getProductsIds()))
                .map(prId -> ProductsCatalog.findById(prId))
                .reduce((productNow, productNext) -> {

                    int compare = Long.compare(productNow.getPrice(), productNext.getPrice());
                    if (compare == 0) {
                        return productNow;
                    } else if (compare == 1) {
                        return productNow;
                    } else return productNext;
                });
    }
}
