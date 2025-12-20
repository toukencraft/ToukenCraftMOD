package com.github.toukencraft.toukencraft.item.equipment;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;

public class TousouProperties {
    public static final Item.Properties TOUSOU_BRONZE_PROPERTIES =
            new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(30));
    public static final Item.Properties TOUSOU_SILVER_PROPERTIES =
            new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(48));
    public static final Item.Properties TOUSOU_GOLD_PROPERTIES =
            new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(66));
}
