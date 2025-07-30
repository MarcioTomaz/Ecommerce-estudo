package com.ec.ecommercev3.DTO;

import com.ec.ecommercev3.Entity.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartAddDTO {

    private List<Item> items;
}
