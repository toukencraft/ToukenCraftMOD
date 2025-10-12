package com.github.toukencraft.toukencraft.item.equipment;

import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.equipment.*;
import java.util.Map;


public class TousouMaterials {
    public static final ArmorMaterial TOUSOU_BRONZE_MATERIAL = new ArmorMaterial(
            30,
            Maps.newEnumMap(Map.of(ArmorType.CHESTPLATE, 10)),
            8,
            SoundEvents.ARMOR_EQUIP_IRON,
            0,
            0,
            ItemTags.COPPER_ORES,  // TODO 実装されたらREPAIRS_COPPER_ARMORに更新する
            TousouAssets.TOUSOU_BRONZE_KEY
    );
    public static final ArmorMaterial TOUSOU_SILVER_MATERIAL = new ArmorMaterial(
            48,
            Maps.newEnumMap(Map.of(ArmorType.CHESTPLATE, 15)),
            9,
            SoundEvents.ARMOR_EQUIP_IRON,
            1,
            0,
            ItemTags.REPAIRS_IRON_ARMOR,
            TousouAssets.TOUSOU_SILVER_KEY
    );
    public static final ArmorMaterial TOUSOU_GOLD_MATERIAL = new ArmorMaterial(
            66,
            Maps.newEnumMap(Map.of(ArmorType.CHESTPLATE, 20)),
            10,
            SoundEvents.ARMOR_EQUIP_IRON,
            2,
            0,
            ItemTags.REPAIRS_GOLD_ARMOR,
            TousouAssets.TOUSOU_GOLD_KEY
    );
}

/*
NOTE 各項目をどう決めたか
  durability:
    胴[11],鉄[15],ダイヤ[33]では差が大きいため、鉄をBRONZEの基準とする。
    装備箇所が1箇所だけのため、倍率は2倍とする。 (スロットごとの基本値 * 素材による倍率 で決まる)
    BRONZE: 鉄[15] * 2 = 30
    SILVER: 24 * 2 = 48
    GOLD: ダイヤ[33] * 2 = 66
  defense:
    全身装備の防御点と同じになるように設定した。
    BRONZE: 胴[10]
    SILVER: 鉄[15]
    GOLD: ダイヤ[20]
  enchantmentValue:
    それぞれの適正値を用いた。
    BRONZE: 胴[8]
    SILVER: 鉄[9]
    GOLD: ダイヤ[10]
  equipSound:
  toughness:
    ダイヤ[2]であったため、順に{0,1,2}とした。
  knockbackResistance:
    ネザライト以外は0のため、0とした。
*/