package com.github.toukencraft.toukencraft.data;

import com.github.toukencraft.toukencraft.item.equipment.TousouMaterials;
import com.github.toukencraft.toukencraft.item.equipment.TousouProperties;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;

public enum TousouEnum {
    BRONZE("tousou_bronze", TousouMaterials.TOUSOU_BRONZE_MATERIAL, TousouProperties.TOUSOU_BRONZE_PROPERTIES),
    SILVER("tousou_silver", TousouMaterials.TOUSOU_SILVER_MATERIAL, TousouProperties.TOUSOU_SILVER_PROPERTIES),
    GOLD("tousou_gold", TousouMaterials.TOUSOU_GOLD_MATERIAL, TousouProperties.TOUSOU_GOLD_PROPERTIES);

    public final String identifier;
    public final Holder<ArmorMaterial> material;
    public final Item.Properties properties;

    TousouEnum(String identifier, Holder<ArmorMaterial> material, Item.Properties properties) {
        this.identifier = identifier;
        this.material = material;
        this.properties = properties;
    }
}
