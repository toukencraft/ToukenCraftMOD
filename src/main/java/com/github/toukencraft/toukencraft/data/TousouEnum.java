package com.github.toukencraft.toukencraft.data;


import com.github.toukencraft.toukencraft.item.equipment.TousouMaterials;
import net.minecraft.world.item.equipment.ArmorMaterial;

/** 刀装の一覧 */
public enum TousouEnum {
    BRONZE("tousou_bronze", TousouMaterials.TOUSOU_BRONZE_MATERIAL),
    SILVER("tousou_silver", TousouMaterials.TOUSOU_SILVER_MATERIAL),
    GOLD("tousou_gold", TousouMaterials.TOUSOU_GOLD_MATERIAL);

    public final String identifier;
    public final ArmorMaterial material;

    TousouEnum(String identifier, ArmorMaterial material) {
        this.identifier = identifier;
        this.material = material;
    }
}
