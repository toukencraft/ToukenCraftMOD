package com.github.toukencraft.toukencraft.data;

import com.github.toukencraft.toukencraft.item.equipment.TousouMaterials;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;

public enum TousouEnum {
    BRONZE("tousou_bronze", TousouMaterials.BRONZE, new Item.Properties()),
    SILVER("tousou_silver", TousouMaterials.SILVER, new Item.Properties()),
    GOLD("tousou_gold", TousouMaterials.GOLD, new Item.Properties());

    public final String identifier;
    public final ArmorMaterial material;
    public final Item.Properties properties;

    TousouEnum(String identifier, ArmorMaterial material, Item.Properties properties) {
        this.identifier = identifier;
        this.material = material;
        this.properties = properties;
    }
}
