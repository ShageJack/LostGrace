package shagejack.lostgrace.registries.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.RegistryObject;
import shagejack.lostgrace.contents.item.blackKnife.BlackKnifeItem;
import shagejack.lostgrace.contents.item.blood.ScabItem;
import shagejack.lostgrace.contents.item.chalk.ChalkItem;
import shagejack.lostgrace.contents.item.goldenSeed.GoldenSeedItem;
import shagejack.lostgrace.contents.item.memoryOfGrace.MemoryOfGraceItem;
import shagejack.lostgrace.contents.item.record.DiesIraeRecordItem;
import shagejack.lostgrace.contents.item.record.KappaRecordItem;
import shagejack.lostgrace.contents.item.spellBook.SpellBookItem;
import shagejack.lostgrace.contents.item.trinaCrystalBall.TrinaCrystalBallFullItem;
import shagejack.lostgrace.contents.item.trinaCrystalBall.TrinaCrystalBallItem;
import shagejack.lostgrace.registries.AllRarities;
import shagejack.lostgrace.registries.AllTabs;
import shagejack.lostgrace.registries.fluid.AllFluids;

import static net.minecraft.world.item.Items.BUCKET;

public class AllItems {

    public static final RegistryObject<Item> memoryOfGrace = new ItemBuilder()
            .properties(properties -> properties.stacksTo(1).rarity(AllRarities.LEGENDARY))
            .name("memory_of_grace")
            .build(MemoryOfGraceItem::new);

    public static final RegistryObject<Item> goldenSeed = new ItemBuilder()
            .properties(properties -> properties.stacksTo(1).fireResistant().rarity(AllRarities.LEGENDARY))
            .name("golden_seed")
            .build(GoldenSeedItem::new);

    public static final RegistryObject<Item> blackKnife = new ItemBuilder()
            .properties(properties -> properties.stacksTo(1).fireResistant().rarity(AllRarities.TRANSCENDENT).setNoRepair())
            .name("black_knife")
            .build(BlackKnifeItem::new);

    public static final RegistryObject<Item> profaneBloodBucket = new ItemBuilder()
            .properties(properties -> properties.stacksTo(1).craftRemainder(BUCKET))
            .name("profane_blood_bucket")
            .build(properties -> new BucketItem(AllFluids.profaneBlood.asFluidSupplier(), properties));

    public static final RegistryObject<Item> sacredBloodBucket = new ItemBuilder()
            .properties(properties -> properties.stacksTo(1).craftRemainder(BUCKET))
            .noTab()
            .name("sacred_blood_bucket")
            .build(properties -> new BucketItem(AllFluids.sacredBlood.asFluidSupplier(), properties));

    public static final RegistryObject<Item> dreamBucket = new ItemBuilder()
            .properties(properties -> properties.stacksTo(1).craftRemainder(BUCKET))
            .name("dream_bucket")
            .build(properties -> new BucketItem(AllFluids.dream.asFluidSupplier(), properties));

    public static final RegistryObject<Item> scab = new ItemBuilder()
            .properties(properties -> properties.food(new FoodProperties.Builder().alwaysEat().fast().nutrition(1).saturationMod(0.5f)
                    .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 200, 0), 0.1f)
                    .effect(() -> new MobEffectInstance(MobEffects.HEAL, 1, 0), 0.35f)
                    .effect(() -> new MobEffectInstance(MobEffects.HARM, 1, 0), 0.05f)
                    .effect(() -> new MobEffectInstance(MobEffects.POISON, 40, 0), 0.02f)
                    .effect(() -> new MobEffectInstance(MobEffects.WITHER, 40, 0), 0.01f)
                    .build())
            )
            .name("scab")
            .build(ScabItem::new);

    public static final RegistryObject<Item> brokenDream = new ItemBuilder()
            .properties(properties -> properties)
            .name("broken_dream")
            .build();

    public static final RegistryObject<Item> dreamShard = new ItemBuilder()
            .properties(properties -> properties)
            .name("dream_shard")
            .build();

    public static final RegistryObject<Item> sweetDreamAshes = new ItemBuilder()
            .properties(properties -> properties)
            .name("sweet_dream_ashes")
            .build();

    public static final RegistryObject<Item> dreambrew = new ItemBuilder()
            .properties(properties -> properties.stacksTo(1))
            .name("dreambrew")
            .build();

    public static final RegistryObject<Item> netherStarShard = new ItemBuilder()
            .properties(properties -> properties)
            .name("nether_star_shard")
            .build();

    public static final RegistryObject<Item> trinaCrystalBall = new ItemBuilder()
            .properties(properties -> properties.stacksTo(1).rarity(AllRarities.LEGENDARY))
            .name("trina_crystal_ball")
            .build(TrinaCrystalBallItem::new);

    public static final RegistryObject<Item> trinaCrystalBallFull = new ItemBuilder()
            .properties(properties -> properties.stacksTo(1).rarity(AllRarities.LEGENDARY))
            .name("trina_crystal_ball_full")
            .build(TrinaCrystalBallFullItem::new);

    public static final RegistryObject<Item> crystalBall = new ItemBuilder()
            .properties(properties -> properties.stacksTo(1))
            .name("crystal_ball")
            .build();

    public static final RegistryObject<Item> chalk = new ItemBuilder()
            .properties(properties -> properties.stacksTo(1).durability(128))
            .name("chalk")
            .build(ChalkItem::new);

    public static final RegistryObject<Item> spellBookIntroduction = new ItemBuilder()
            .properties(properties -> properties.stacksTo(1))
            .name("spell_book_introduction")
            .build(SpellBookItem::new);

    public static final RegistryObject<Item> musicDiscDiesIrae = new ItemBuilder()
            .properties(properties -> properties.stacksTo(1).tab(AllTabs.tabMain).rarity(Rarity.EPIC))
            .name("music_disc_dies_irae")
            .build(DiesIraeRecordItem::new);

    public static final RegistryObject<Item> musicDiscKappa = new ItemBuilder()
            .properties(properties -> properties.stacksTo(1).tab(AllTabs.tabMain).rarity(Rarity.EPIC))
            .name("music_disc_kappa")
            .build(KappaRecordItem::new);
}
