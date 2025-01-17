package disintegration.content;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.Rand;
import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import arc.util.Time;
import disintegration.DTVars;
import disintegration.entities.DTGroups;
import disintegration.entities.abilities.PortableBlockAbility;
import disintegration.entities.bullet.BlockBulletType;
import disintegration.entities.bullet.ConnectBulletType;
import disintegration.entities.bullet.RandomSpreadBulletType;
import disintegration.entities.bullet.WarpBulletType;
import disintegration.graphics.DTShaders;
import disintegration.graphics.Pal2;
import disintegration.world.blocks.campaign.OrbitalLaunchPad;
import disintegration.world.blocks.campaign.SpaceStationLaunchPad;
import disintegration.world.blocks.debug.BlackHoleBlock;
import disintegration.world.blocks.debug.DPSBlock;
import disintegration.world.blocks.debug.DebugBlock;
import disintegration.world.blocks.debug.ShaderTestBlock;
import disintegration.world.blocks.defence.RepairDroneStation;
import disintegration.world.blocks.defence.ShardWall;
import disintegration.world.blocks.defence.turrets.ElectricTowerTurret;
import disintegration.world.blocks.distribution.MultiOverflowGate;
import disintegration.world.blocks.effect.FloorBuilder;
import disintegration.world.blocks.environment.ConnectFloor;
import disintegration.world.blocks.heat.ConsumeHeatProducer;
import disintegration.world.blocks.heat.HeatConduit;
import disintegration.world.blocks.heat.HeatConduitRouter;
import disintegration.world.blocks.heat.AttributeHeatProducer;
import disintegration.world.blocks.laser.LaserDevice;
import disintegration.world.blocks.laser.LaserReactor;
import disintegration.world.blocks.laser.LaserReflector;
import disintegration.world.blocks.payload.*;
import disintegration.world.blocks.power.RotateSolarGenerator;
import disintegration.world.blocks.power.SpaceSolarGenerator;
import disintegration.world.blocks.power.SpreadGenerator;
import disintegration.world.blocks.production.CatalyzeCrafter;
import disintegration.world.blocks.production.PortableDrill;
import disintegration.world.blocks.production.PortableDrillUnloadPoint;
import disintegration.world.blocks.production.Quarry;
import disintegration.world.blocks.units.ReconstructPlatform;
import disintegration.world.blocks.units.UnitAssemblerConstructModule;
import disintegration.world.blocks.units.UnitAssemblerInterfaceModule;
import disintegration.world.draw.DrawAllRotate;
import disintegration.world.draw.DrawFusion;
import disintegration.world.draw.DrawLaser;
import disintegration.world.draw.DrawTurbine;
import mindustry.Vars;
import mindustry.content.*;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.bullet.*;
import mindustry.entities.effect.*;
import mindustry.entities.part.*;
import mindustry.entities.pattern.ShootBarrel;
import mindustry.entities.pattern.ShootSpread;
import mindustry.gen.Bullet;
import mindustry.gen.Entityc;
import mindustry.gen.LegsUnit;
import mindustry.gen.Sounds;
import mindustry.graphics.CacheLayer;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.*;
import mindustry.type.unit.MissileUnitType;
import mindustry.world.Block;
import mindustry.world.blocks.defense.ShieldWall;
import mindustry.world.blocks.defense.ShockMine;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.environment.StaticWall;
import mindustry.world.blocks.environment.SteamVent;
import mindustry.world.blocks.liquid.Conduit;
import mindustry.world.blocks.payloads.Constructor;
import mindustry.world.blocks.payloads.PayloadDeconstructor;
import mindustry.world.blocks.payloads.PayloadLoader;
import mindustry.world.blocks.payloads.PayloadUnloader;
import mindustry.world.blocks.power.Battery;
import mindustry.world.blocks.power.ConsumeGenerator;
import mindustry.world.blocks.power.PowerNode;
import mindustry.world.blocks.power.ThermalGenerator;
import mindustry.world.blocks.production.*;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.blocks.units.Reconstructor;
import mindustry.world.blocks.units.UnitFactory;
import mindustry.world.consumers.ConsumeCoolant;
import mindustry.world.consumers.ConsumeItemExplode;
import mindustry.world.consumers.ConsumeItemFlammable;
import mindustry.world.consumers.ConsumeLiquid;
import mindustry.world.draw.*;
import mindustry.world.meta.Attribute;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.BuildVisibility;
import mindustry.world.meta.Env;

import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Lines.stroke;
import static mindustry.type.ItemStack.with;

public class DTBlocks {
    //TODO check bundles
    static Rand rand = new Rand();
    public static Block
    //environment
            iceWater, greenIce, greenFloor, spaceStationFloor, spaceStationFloorFixed, floatIce,
            obsidianFloor,
            ethyleneVent,
            greenIceWall, obsidianWall,
        //ore
            oreIridium, oreIron, oreSilver,
    //defence
            iridiumWall, iridiumWallLarge,
            steelWall, steelWallLarge,
            ironWall, ironWallLarge,
            conductionAlloyWall, conductionAlloyWallLarge,
            nitrideWall, nitrideWallLarge,
            projectorWall,
    //transport
            ironConveyor, alternateOverflowGate, ironSorter, invertedIronSorter, ironRouter, ironJunction, ironItemBridge,
            iridiumConveyor,
    //liquid
            magnetizedConduit,
    //storage
            corePedestal, corePillar, spaceStationCore,
    //heat
            heatConduit, heatConduitRouter,
            burningHeater, thermalHeater,
    //laser
            laserDevice, laserReflector, laserRouter, laserSource,
    //factory
            nitrideSynthesizer,
            boiler, airCompressor, electrolyser, siliconRefiner, graphiteCompressor, steelSmelter, steelBlastFurnace, conductionAlloySmelter, magnetismAlloySmelter, surgeFurnace,
            algalPond,
            centrifuge,
    //payload
            payloadAccelerator, payloadDecelerator, magnetizedPayloadRail, magnetizedPayloadRailShort, payloadRedirector, payloadRedirectorPoint, payloadSeparator, payloadForkLeft, payloadForkRight, payloadForkPoint,
            payloadConstructor, largePayloadConstructor, payloadDeconstructor, payloadLoader, payloadUnloader, //payloadPropulsionTower,
    //power
            neoplasmGenerator, excitationReactor, ventTurbine, turbineGenerator, spaceSolarPanel, rotateSolarPanel,
            ironPowerNode, ironPowerNodeLarge, powerCapacitor,
    //units
            asemblerConstructModule, assemblerExpandInterfaceModule,
            unitFabricator,
            additiveRefabricator, multiplicativeRefabricator, exponentialRefabricatingPlatform, tetrativeRefabricatingPlatform,
    //turrets
            overheat, impact,
            fracture, dissolve,
            blade, encourage,
            aegis, permeation,
            holy,
            shard, aerolite, sparkover, focus, axe, ambush,
            voltage,
            condense, blaze,
        //test
            sabre,
    //drills
            quarry, pressureDrill, stiffDrill, rockExtractor,
            portableDrill,
    //effect
            blastMine,
            repairDroneStation,
    //campaign
            spaceStationLaunchPad, orbitalLaunchPad,
    //debug
            sandboxBlock, dpsBlock, editorBlock, cheatBlock, shaderTestBlock, blackHoleBlock, blackHoleClearBlock
            ;
    public static void load() {
        Blocks.ice.mapColor.add(0.1f, 0.1f, 0.2f);
        Blocks.snow.mapColor.add(0.1f, 0.1f, 0.2f);

        Blocks.malign.requirements(Category.turret, with(DTItems.nitride, 120, Items.carbide, 400, Items.beryllium, 2000, Items.silicon, 800, Items.graphite, 800, Items.phaseFabric, 300));
        Blocks.smite.requirements(Category.turret, with(DTItems.nitride, 160, Items.oxide, 200, Items.surgeAlloy, 400, Items.silicon, 800, Items.carbide, 500, Items.phaseFabric, 300));
        Blocks.lustre.requirements(Category.turret, with(DTItems.nitride, 90, Items.silicon, 250, Items.graphite, 200, Items.oxide, 50, Items.carbide, 90));

        Blocks.liquidContainer.requirements(Category.liquid, with(Items.lead, 10, Items.metaglass, 15));
        Blocks.liquidTank.requirements(Category.liquid, with(Items.lead, 30, Items.metaglass, 40));

        Blocks.payloadConveyor.requirements(Category.units, with(Items.graphite, 10, Items.lead, 10));
        Blocks.payloadRouter.requirements(Category.units, with(Items.graphite, 15, Items.lead, 10));

        Blocks.melter.requirements(Category.crafting, with(Items.lead, 35, Items.graphite, 45));

        Blocks.sporePress.envEnabled |= Env.spores;

        Blocks.slagCentrifuge.buildVisibility = BuildVisibility.shown;
        Blocks.slagCentrifuge.consumeLiquid(Liquids.slag, 30f / 60f);
        ((GenericCrafter) Blocks.slagCentrifuge).outputLiquid = new LiquidStack(Liquids.gallium, 5f / 60f);
        //region environment
        greenIce = new Floor("green-ice"){{
            dragMultiplier = 0.35f;
            speedMultiplier = 0.9f;
            attributes.set(Attribute.water, 0.4f);
            attributes.set(DTAttribute.algae, 1f);
            albedo = 0.6f;
        }};

        if(false)floatIce = new Floor("float-ice"){{
            dragMultiplier = 0.35f;
            speedMultiplier = 0.9f;
            attributes.set(Attribute.water, 0.4f);
            albedo = 0.65f;
        }};
        greenFloor = new Floor("green-floor"){{
            variants = 0;
            canShadow = false;
            placeableOn = false;
            solid = true;
        }};

        spaceStationFloor = new ConnectFloor("space-station-floor"){{
            variants = 0;
            blendGroup = Blocks.empty;
            connects = Seq.with(this);
        }};
        spaceStationFloorFixed = new ConnectFloor("space-station-floor-fixed"){{
            variants = 0;
            blendGroup = Blocks.empty;
            connects = Seq.with(spaceStationFloor.asFloor(), this);
        }};
        ((ConnectFloor)spaceStationFloor.asFloor()).connects.add(spaceStationFloorFixed.asFloor());

        iceWater = new Floor("ice-water"){{
            speedMultiplier = 0.5f;
            variants = 3;
            status = StatusEffects.wet;
            statusDuration = 90f;
            liquidDrop = Liquids.water;
            isLiquid = true;
            cacheLayer = CacheLayer.water;
            albedo = 0.9f;
            supportsOverlay = true;
            wall = Blocks.iceWall;
        }};

        obsidianFloor = new Floor("obsidian-floor"){};
        greenIceWall = new StaticWall("green-ice-wall"){{
            greenIce.asFloor().wall = this;
            albedo = 0.6f;
        }};

        obsidianWall = new StaticWall("obsidian-wall"){{
            obsidianFloor.asFloor().wall = this;
        }};

        oreIridium = new OreBlock("ore-iridium", DTItems.iridium){{
            oreDefault = true;
            oreThreshold = 0.828f;
            oreScale = 10f;
        }};

        oreIron = new OreBlock("ore-iron", DTItems.iron){{
            oreDefault = true;
            oreThreshold = 0.828f;
            oreScale = 23.952381f;
        }};

        oreSilver = new OreBlock("ore-silver", DTItems.silver){{
            oreDefault = true;
            oreThreshold = 0.864f;
            oreScale = 24.904762f;
        }};

        ethyleneVent = new SteamVent("ethylene-vent"){{
            parent = blendGroup = Blocks.ice;
            effect = DTFx.ethyleneVentSteam;
            attributes.set(Attribute.steam, 1f);
        }};

        //endregion
        //region defence

        iridiumWall = new ShardWall("iridium-wall"){{
            shardChance = 0.1f;
            shard = DTBullets.shard;
            size = 1;
            scaledHealth = 650;
            armor = 2f;
            requirements(Category.defense, with(DTItems.iridium, 8));
        }};
        iridiumWallLarge = new ShardWall("iridium-wall-large"){{
            shardChance = 0.1f;
            shard = DTBullets.shard;
            size = 2;
            scaledHealth = 650;
            armor = 2f;
            requirements(Category.defense, with(DTItems.iridium, 24));
        }};

        steelWall = new Wall("steel-wall"){{
            size = 1;
            scaledHealth = 600;
            armor = 8f;
            requirements(Category.defense, with(DTItems.steel, 8));
        }};
        steelWallLarge = new Wall("steel-wall-large"){{
            size = 2;
            scaledHealth = 600;
            armor = 8f;
            requirements(Category.defense, with(DTItems.steel, 24));
        }};

        ironWall = new Wall("iron-wall"){{
            size = 1;
            scaledHealth = 450;
            armor = 8f;
            requirements(Category.defense, with(DTItems.iron, 8));
        }};
        ironWallLarge = new Wall("iron-wall-large"){{
            size = 2;
            scaledHealth = 450;
            armor = 8f;
            requirements(Category.defense, with(DTItems.iron, 24));
        }};
        conductionAlloyWall = new Wall("conduction-alloy-wall"){{
            size = 1;
            scaledHealth = 600;
            armor = 5f;
            requirements(Category.defense, with(DTItems.conductionAlloy, 8));
        }};

        conductionAlloyWall = new Wall("conduction-alloy-wall-large"){{
            size = 2;
            scaledHealth = 600;
            armor = 5f;
            requirements(Category.defense, with(DTItems.conductionAlloy, 24));
        }};

        projectorWall = new ShieldWall("projector-wall"){{
            requirements(Category.defense, ItemStack.with(DTItems.iron, 10, Items.silicon, 20, DTItems.magnetismAlloy, 5));
            consumePower(3f / 60f);

            outputsPower = false;
            hasPower = true;
            consumesPower = true;
            conductivePower = true;

            scaledHealth = 700;
            armor = 10f;
            size = 2;
        }};

        nitrideWall = new ShardWall("nitride-wall"){{
            shardChance = 0.2f;
            size = 1;
            scaledHealth = 1000;
            armor = 15f;
            shard = DTBullets.nitrideLaser;
            requirements(Category.defense, with(DTItems.nitride, 8));
        }};

        nitrideWallLarge = new ShardWall("nitride-wall-large"){{
            shardChance = 0.2f;
            size = 2;
            scaledHealth = 1000;
            armor = 15f;
            shard = DTBullets.nitrideLaser;
            requirements(Category.defense, with(DTItems.nitride, 24));
        }};
        //endregion
        //region transport
        ironConveyor = new Conveyor("iron-conveyor"){{
            requirements(Category.distribution, with(DTItems.iron, 1));
            health = 45;
            speed = 0.05f;
            displayedSpeed = 7f;
            buildCostMultiplier = 2f;
        }};

        alternateOverflowGate = new MultiOverflowGate("alternate-overflow-gate"){{
            requirements(Category.distribution, with(DTItems.iron, 3));
            health = 60;
            saveConfig = true;
        }};
        ironSorter = new Sorter("iron-sorter"){{
            requirements(Category.distribution, with(DTItems.iron, 3));
            health = 60;
        }};
        invertedIronSorter = new Sorter("inverted-iron-sorter"){{
            requirements(Category.distribution, with(DTItems.iron, 3));
            health = 60;
            invert = true;
        }};
        ironRouter = new Router("iron-router"){{
            requirements(Category.distribution, with(DTItems.iron, 3));
            speed = 5f;
            buildCostMultiplier = 4f;
        }};

        ironJunction = new Junction("iron-junction"){{
            requirements(Category.distribution, with(DTItems.iron, 2));
            speed = 20;
            capacity = 6;
            health = 30;
            buildCostMultiplier = 6f;
        }};

        ironItemBridge = new BufferedItemBridge("iron-bridge-conveyor"){{
            requirements(Category.distribution, with(DTItems.iron, 12));
            fadeIn = moveArrows = false;
            range = 5;
            speed = 74f;
            arrowSpacing = 6f;
            bufferCapacity = 14;
        }};
        iridiumConveyor = new Conveyor("iridium-conveyor"){{
            requirements(Category.distribution, with(DTItems.iron, 1));
            health = 65;
            speed = 0.09f;
            displayedSpeed = 13f;
            buildCostMultiplier = 2f;
        }};

        //endregion
        //region liquid
        magnetizedConduit = new Conduit("magnetized-conduit"){{
            requirements(Category.liquid, with(DTItems.silver, 2, Items.metaglass, 1));
            liquidCapacity = 16f;
            liquidPressure = 1.025f;
            health = 90;
        }};
        //endregion
        //region storage
        corePedestal = new CoreBlock("core-pedestal"){{
            requirements(Category.effect, BuildVisibility.editorOnly, with(DTItems.iron, 1300));
            alwaysUnlocked = true;

            isFirstTier = true;
            unitType = DTUnitTypes.separate;
            health = 1300;
            itemCapacity = 4000;
            size = 3;

            generateIcons = false;

            unitCapModifier = 8;
        }};

        corePillar = new CoreBlock("core-pillar"){{
            requirements(Category.effect, with(DTItems.iron, 2000, DTItems.silver, 1500, Items.lead, 1700));

            unitType = DTUnitTypes.attract;
            health = 3000;
            itemCapacity = 8000;
            size = 4;

            generateIcons = false;

            unitCapModifier = 12;
        }};

        spaceStationCore = new CoreBlock("space-station-core"){{
            requirements(Category.effect, BuildVisibility.editorOnly, with(DTItems.iron, 1300));
            alwaysUnlocked = true;

            isFirstTier = true;
            unitType = DTUnitTypes.spaceStationDrone;
            health = 1300;
            itemCapacity = 4000;
            size = 3;

            generateIcons = false;

            unitCapModifier = 25;
        }
            @Override
            public boolean canReplace(Block other){
                return false;
            }};
        //endregion
        //region heat
        heatConduit = new HeatConduit("heat-conduit"){{
            researchCostMultiplier = 10f;
            size = 1;
            drawer = new DrawBlock(){};
            requirements(Category.crafting, with(DTItems.iron, 2));
        }};

        heatConduitRouter = new HeatConduitRouter("heat-conduit-router"){{
            researchCostMultiplier = 10f;
            size = 1;
            drawer = new DrawBlock(){};
            requirements(Category.crafting, with(DTItems.iron, 2));
        }};

        burningHeater = new ConsumeHeatProducer("burning-heater"){{
            requirements(Category.crafting, with(DTItems.iron, 90, Items.lead, 70));

            researchCostMultiplier = 4f;

            rotateDraw = false;
            size = 2;
            craftTime = 90;
            heatOutput = 4f;
            itemCapacity = 10;
            updateEffect = Fx.generatespark;
            updateEffectChance = 0.01f;

            ambientSound = Sounds.smelter;
            ambientSoundVolume = 0.06f;

            consume(new ConsumeItemFlammable());
            consume(new ConsumeItemExplode());

            drawer = new DrawMulti(new DrawAllRotate(), new DrawHeatOutput());
        }};
        thermalHeater = new AttributeHeatProducer("thermal-heater"){{
            size = 2;
            requirements(Category.crafting, with(DTItems.silver, 50, DTItems.iron, 70, Items.silicon, 40));
            heatOutput = 3f;
            baseEfficiency = 0f;
            drawer = new DrawMulti(new DrawAllRotate(), new DrawHeatOutput());
        }};
        //endregion
        //region laser
        laserSource = new LaserDevice("laser-source"){{
            range = 10;
            health = 400;
            laserOutput = 1000;
            drawer = new DrawMulti(new DrawAllRotate(1), new DrawLaser());
            requirements(Category.crafting, BuildVisibility.sandboxOnly, with());
        }};

        laserDevice = new LaserDevice("laser-device"){{
           range = 7;
           health = 200;
           laserOutput = 2;
           drawer = new DrawMulti(new DrawAllRotate(1), new DrawLaser());
           consumePower(3);
           requirements(Category.crafting, with(DTItems.iron, 30, Items.silicon, 20, Items.graphite, 50));
        }};

        laserReflector = new LaserReflector("laser-reflector"){{
            range = 7;
            split = false;
            rotateDraw = false;
            health = 200;
            drawer = new DrawMulti(new DrawRegion(""), new DrawRegion("-arrow"){{
                buildingRotate = true;
            }}, new DrawLaser());
            requirements(Category.crafting, with(DTItems.iron, 30, Items.silicon, 20, Items.graphite, 50));
        }};

        laserRouter = new LaserReflector("laser-router"){{
            range = 7;
            split = true;
            rotateDraw = false;
            health = 200;
            drawer = new DrawMulti(new DrawRegion(""), new DrawRegion("-arrow"){{buildingRotate = true;}}, new DrawLaser());
            requirements(Category.crafting, with(DTItems.iron, 30, Items.silicon, 20, Items.graphite, 50));
        }};
        //endregion
        //region factory
        nitrideSynthesizer = new HeatCrafter("nitride-synthesizer"){{
            size = 4;
            requirements(Category.crafting, with(Items.silicon, 105, Items.oxide, 90, Items.graphite, 100, Items.beryllium, 140, Items.carbide, 60));
            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawLiquidTile(Liquids.gallium),
                    new DrawLiquidTile(Liquids.nitrogen),
                    new DrawParticles(){{
                        color = Color.valueOf("ededed");
                        alpha = 0.5f;
                        particleSize = 3f;
                        particles = 10;
                        particleRad = 9f;
                        particleLife = 200f;
                        particleSizeInterp = Interp.one;
                    }},
                    new DrawDefault(),
                    new DrawHeatInput(),
                    new DrawHeatRegion("-heat-top")
            );
            ambientSound = Sounds.extractLoop;
            ambientSoundVolume = 0.08f;

            liquidCapacity = 80f;

            craftTime = 120f;
            consumeLiquids(LiquidStack.with(Liquids.gallium, 0.07f, Liquids.nitrogen, 0.12f, Liquids.hydrogen, 0.04f));
            heatRequirement = 6f;
            outputItem = new ItemStack(DTItems.nitride, 1);
        }};

        boiler = new HeatCrafter("boiler"){{
            requirements(Category.crafting, with(DTItems.iron, 65, Items.silicon, 40, Items.graphite, 60));
            outputLiquid = new LiquidStack(DTLiquids.steam, 12f / 60f);
            size = 2;
            heatRequirement = 4f;
            maxEfficiency = 2f;
            rotateDraw = false;
            hasLiquids = true;
            outputsLiquid = true;
            envEnabled = Env.any;
            ambientSound = DTSounds.boiler;
            ambientSoundVolume = 0.01f;
            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawLiquidTile(Liquids.water),
                    new DrawBubbles(Color.valueOf("7693e3")){{
                        sides = 10;
                        recurrence = 3f;
                        spread = 6;
                        radius = 1.5f;
                        amount = 20;
                    }},
                    new DrawLiquidTile(DTLiquids.steam){},
                    new DrawDefault(),
                    new DrawHeatInput()
            );
            liquidCapacity = 24f;
            craftTime = 120;

            consumeLiquid(Liquids.water, 12f / 60f);
        }};

        airCompressor = new GenericCrafter("air-compressor"){{
            requirements(Category.crafting, with(DTItems.silver, 60, DTItems.iron, 180, Items.silicon, 150));
            size = 2;
            hasLiquids = true;

            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(Liquids.nitrogen, 4.1f), new DrawDefault(), new DrawHeatInput(),
                    new DrawParticles(){{
                        color = Color.valueOf("d4f0ff");
                        alpha = 0.6f;
                        particleSize = 4f;
                        particles = 10;
                        particleRad = 12f;
                        particleLife = 140f;
                    }});

            researchCostMultiplier = 1.1f;
            liquidCapacity = 40f;
            consumePower(0.5f);
            ambientSound = Sounds.extractLoop;
            ambientSoundVolume = 0.06f;

            outputLiquid = new LiquidStack(Liquids.nitrogen, 8/60f);

            researchCost = with(Items.silicon, 2000, Items.oxide, 900, Items.beryllium, 2400);
        }};

        electrolyser = new GenericCrafter("electrolyser"){{
            requirements(Category.crafting, with(Items.silicon, 50, Items.graphite, 40, Items.beryllium, 130, Items.tungsten, 80));
            size = 3;

            researchCostMultiplier = 1.2f;
            craftTime = 10f;
            rotate = true;
            invertFlip = true;

            hasLiquids = true;

            liquidCapacity = 50f;

            consumeLiquid(Liquids.water, 12f / 60f);
            consumePower(1f);

            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawLiquidTile(Liquids.hydrogen),
                    new DrawAllRotate(),
                    new DrawGlowRegion(){{
                        alpha = 0.7f;
                        color = Color.valueOf("c4bdf3");
                        glowIntensity = 0.3f;
                        glowScale = 6f;
                    }}
            );

            ambientSound = Sounds.electricHum;
            ambientSoundVolume = 0.08f;
            squareSprite = false;

            outputLiquids = LiquidStack.with(DTLiquids.oxygen, 4f / 60, Liquids.hydrogen, 8f / 60);
            liquidOutputDirections = new int[]{4, 2};
        }};

        siliconRefiner = new GenericCrafter("silicon-refiner"){{
            requirements(Category.crafting, with(DTItems.iron, 60));
            craftEffect = Fx.smeltsmoke;
            outputItem = new ItemStack(Items.silicon, 2);
            craftTime = 60f;
            size = 2;
            hasPower = true;
            hasLiquids = false;
            float scl = 0.8f;
            drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("ffef99")){{
                flameRadius *= scl;
                flameRadiusIn *= scl;
                flameRadiusMag *= scl;
                flameRadiusInMag *= scl;
                lightSinMag *= scl;
            }});
            ambientSound = Sounds.smelter;
            ambientSoundVolume = 0.07f;

            consumeItems(with(Items.coal, 2, Items.sand, 3));
            consumePower(0.50f);
        }};

        graphiteCompressor = new GenericCrafter("graphite-compressor"){{
            requirements(Category.crafting, with(DTItems.iron, 50));

            craftEffect = DTFx.compressSmoke;
            outputItem = new ItemStack(Items.graphite, 3);
            craftTime = 240f;
            size = 2;
            hasItems = true;

            consumeItem(Items.coal, 5);
        }};

        steelSmelter = new GenericCrafter("steel-smelter"){{
            requirements(Category.crafting, with(DTItems.iron, 60, Items.graphite, 30, Items.silicon, 30, Items.metaglass, 20));
            craftEffect = Fx.smeltsmoke;
            outputItem = new ItemStack(DTItems.steel, 1);
            craftTime = 60f;
            size = 2;
            hasPower = hasItems = true;
            drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("ffef99")));
            ambientSound = Sounds.smelter;
            ambientSoundVolume = 0.07f;

            consumeItems(with(DTItems.iron, 1, Items.coal, 1));
            consumePower(0.60f);
        }};

        steelBlastFurnace = new HeatCrafter("steel-blast-furnace"){{
            requirements(Category.crafting, with(DTItems.steel, 100, Items.graphite, 190, Items.silicon, 50, DTItems.iron, 90, DTItems.silver, 20));
            craftEffect = new MultiEffect(Fx.producesmoke, new RadialEffect(DTFx.blastFurnaceSmoke, 4, 90f, 5f)) ;
            outputItem = new ItemStack(DTItems.steel, 12);
            craftTime = 240f;
            size = 5;
            hasItems = true;
            drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("ffef99")), new DrawHeatInput());
            ambientSound = Sounds.smelter;
            ambientSoundVolume = 0.07f;
            heatRequirement = 15;
            maxEfficiency = 2;
            itemCapacity = 30;

            consumeItems(with(DTItems.iron, 8, Items.coal, 4));
        }};

        conductionAlloySmelter = new CatalyzeCrafter("conduction-alloy-smelter"){{
            requirements(Category.crafting, with(Items.silicon, 80, Items.lead, 90, DTItems.silver, 90, DTItems.iron, 70));
            craftEffect = Fx.smeltsmoke;
            outputItem = new ItemStack(DTItems.conductionAlloy, 1);
            craftTime = 60*2f;
            catalysisEfficiency = 1;
            size = 3;
            catalystItem = new ItemStack(DTItems.iridium, 1);
            hasPower = true;
            itemCapacity = 20;
            drawer = new DrawMulti(new DrawDefault(), new DrawFlame());

            consumePower(4f);
            consumeItems(with(Items.lead, 4, Items.silicon, 3));
        }};
        magnetismAlloySmelter = new GenericCrafter("magnetism-alloy-smelter"){{
            requirements(Category.crafting, with(Items.silicon, 150, Items.lead, 160, DTItems.silver, 90, DTItems.iron, 170, DTItems.steel, 30));
            craftEffect = new Effect(50, e -> {
                float len = 8f;
                for(int i = 0; i < 4; i++){
                    float x = Geometry.d4x(i), y = Geometry.d4y(i);
                    Draw.color(Pal2.lightningWhite);
                    Drawf.tri(e.x + x * len, e.y + y * len, 7f * e.foutpow(), 10f * e.finpow(), i * 90);
                    Drawf.tri(e.x + x * len, e.y + y * len, 7f * e.foutpow(), 4f * e.finpow(), i * 90 + 180);
                }
            });
            hasPower = true;
            craftTime = 100f;
            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawCrucibleFlame(){{
                flameColor = Color.valueOf("ceecff");
                midColor = Color.valueOf("a9e9ff");
                particleRad = 12f;
                flameRad = 6f;
            }}, new DrawDefault());
            consumePower(5f);
            consumeItems(with(Items.silicon, 1, DTItems.silver, 2));
            consumeLiquid(Liquids.nitrogen, 0.2f);
            size = 3;
            outputItem = new ItemStack(DTItems.magnetismAlloy, 2);
        }};

        surgeFurnace = new GenericCrafter("surge-furnace"){{
            requirements(Category.crafting, with(Items.silicon, 100, Items.lead, 120, DTItems.iridium, 90));
            craftEffect = Fx.smeltsmoke;
            outputItem = new ItemStack(Items.surgeAlloy, 1);
            craftTime = 75f;
            size = 4;
            hasPower = true;
            itemCapacity = 20;
            drawer = new DrawMulti(new DrawDefault(), new DrawFlame(){{
                lightRadius = 160f;
            }});

            consumePower(4f);
            consumeItems(with(DTItems.silver, 3, Items.lead, 4, Items.silicon, 3));
        }};
        algalPond = new AttributeCrafter("algal-pond"){{
            requirements(Category.production, with(DTItems.iron, 120, Items.lead, 110, Items.graphite, 90, Items.silicon, 100));
            size = 9;
            baseEfficiency = 1f;
            attribute = DTAttribute.algae;
            boostScale = 1.5f;
            liquidCapacity = 120f;
            rotate = true;
            regionRotated1 = 3;
            invertFlip = true;
            liquidOutputDirections = new int[]{1, 3};
            group = BlockGroup.liquids;
            consumeLiquid(Liquids.water, 0.4f);
            outputLiquids = LiquidStack.with(DTLiquids.oxygen, 0.1f, DTLiquids.algalWater, 0.4f);
            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawLiquidTile(Liquids.water),
                    new DrawLiquidTile(DTLiquids.algalWater),
                    new DrawRegion(),
                    new DrawLiquidOutputs()
            );
        }};

        centrifuge = new Separator("centrifuge"){{
            requirements(Category.crafting, with(Items.surgeAlloy, 40, Items.lead, 140, Items.silicon, 70));
            results = with(
                    DTItems.iron, 3,
                    DTItems.silver, 2,
                    DTItems.iridium, 1
            );
            hasPower = true;
            craftTime = 5f;
            size = 3;
            itemCapacity = 40;

            consumePower(5f);
            consumeLiquid(Liquids.slag, 0.15f);

            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(), new DrawBlurSpin("-spinner", 12), new DrawDefault());
        }};
        //endregion
        //region payload
        /*payloadPropulsionTower = new PayloadMassDriver("payload-propulsion-tower"){{
            requirements(Category.units, with(Items.thorium, 300, Items.silicon, 200, Items.plastanium, 200, Items.phaseFabric, 50));
            size = 5;
            reload = 130f;
            chargeTime = 100f;
            range = 1000f;
            maxPayloadSize = 3.5f;
            consumePower(6f);
        }};*/

        payloadAccelerator = new VelocityPayloadConveyor("payload-accelerator"){{
            conductivePower = true;
            consumePower(1f);
            size = 3;
            force = 1f;
            rotate = true;
            acceptsPayload = true;
            outputsPayload = true;
            hasShadow = false;
            drawer = new DrawMulti(
                    new DrawRegion("-shadow"){{
                        buildingRotate = true;
                    }},
                    new DrawAllRotate()
            );
            requirements(Category.units, with(Items.silicon, 10, DTItems.silver, 10, DTItems.magnetismAlloy, 5));
        }};

        payloadDecelerator = new PayloadDecelerator("payload-decelerator"){{
            size = 3;
            rotate = true;
            force = 0f;
            acceptsPayload = true;
            outputsPayload = true;
            hasShadow = false;
            drawer = new DrawMulti(
                    new DrawRegion("-shadow"){{
                        buildingRotate = true;
                    }},
                    new DrawAllRotate()
            );
            requirements(Category.units, with(Items.silicon, 10, DTItems.silver, 10, DTItems.magnetismAlloy, 5));
        }};

        payloadRedirector = new PayloadRedirector("payload-redirector"){{
            size = 1;
            rotate = true;
            solid = true;
            update = true;
            replaceable = false;
            drawer = new DrawMulti(
                    new DrawRegion("-shadow"){{
                        buildingRotate = true;
                    }},
                    new DrawAllRotate()
            );
            requirements(Category.units, with(Items.silicon, 10, DTItems.silver, 10, DTItems.magnetismAlloy, 5));
        }};

        payloadForkLeft = new PayloadFork("payload-fork-left"){{
            size = 1;
            rotate = true;
            replaceable = false;
            points = new Point2[]{
                    new Point2(-4, 0),
                    new Point2(-4, -4)
            };
            drawer = new DrawMulti(
                    new DrawRegion("-shadow"){{
                        buildingRotate = true;
                    }},
                    new DrawAllRotate()
            );
            sortOutputIndex = 1;
            requirements(Category.units, with(Items.silicon, 10, DTItems.silver, 10, DTItems.magnetismAlloy, 5));
        }};

        payloadForkRight = new PayloadFork("payload-fork-right"){{
            size = 1;
            rotate = true;
            replaceable = false;
            points = new Point2[]{
                    new Point2(-4, 0),
                    new Point2(-4, 4)
            };
            drawer = new DrawMulti(
                    new DrawRegion("-shadow"){{
                        buildingRotate = true;
                    }},
                    new DrawAllRotate()
            );
            sortOutputIndex = 1;
            requirements(Category.units, with(Items.silicon, 10, DTItems.silver, 10, DTItems.magnetismAlloy, 5));
        }};

        payloadSeparator = new PayloadFork("payload-separator"){{
            size = 1;
            rotate = true;
            replaceable = false;
            points = new Point2[]{
                    new Point2(-4, 0),
                    new Point2(-4, -4),
                    new Point2(-4, 4)
            };
            drawer = new DrawMulti(
                    new DrawRegion("-shadow"){{
                        buildingRotate = true;
                    }},
                    new DrawAllRotate()
            );
            sortOutputIndex = 0;
            requirements(Category.units, with(Items.silicon, 10, DTItems.silver, 10, DTItems.magnetismAlloy, 5));
        }};

        payloadRedirectorPoint = new PayloadRedirectorPoint("payload-redirector-point"){{
            size = 1;
            rotate = true;
            force = 0f;
            replaceable = false;
            acceptsPayload = true;
            outputsPayload = true;
            requirements(Category.units, BuildVisibility.hidden, with());
        }};
        payloadForkPoint = new PayloadForkPoint("payload-fork-point"){{
            size = 1;
            rotate = true;
            force = 0f;
            replaceable = false;
            acceptsPayload = true;
            outputsPayload = true;
            requirements(Category.units, BuildVisibility.hidden, with());
        }};
        ((PayloadRedirector)payloadRedirector).point = payloadRedirectorPoint;
        ((PayloadFork)payloadForkLeft).point = payloadForkPoint;
        ((PayloadFork)payloadForkRight).point = payloadForkPoint;
        ((PayloadFork)payloadSeparator).point = payloadForkPoint;
        magnetizedPayloadRail = new VelocityPayloadConveyor("magnetized-payload-rail"){{
            size = 3;
            rotate = true;
            force = 0f;
            acceptsPayload = true;
            outputsPayload = true;
            hasShadow = false;
            drawer = new DrawMulti(
                    new DrawRegion("-shadow"){{
                        buildingRotate = true;
                    }},
                    new DrawAllRotate()
            );
            requirements(Category.units, with(Items.silicon, 10, DTItems.silver, 10, DTItems.magnetismAlloy, 5));
        }};

        magnetizedPayloadRailShort = new VelocityPayloadConveyor("magnetized-payload-rail-short"){{
            size = 1;
            rotate = true;
            force = 0f;
            acceptsPayload = true;
            outputsPayload = true;
            hasShadow = false;
            drawer = new DrawMulti(
                    new DrawRegion("-shadow"){{
                        buildingRotate = true;
                    }},
                    new DrawAllRotate()
            );
            requirements(Category.units, with(Items.silicon, 10, DTItems.silver, 10, DTItems.magnetismAlloy, 5));
        }};

        payloadDeconstructor = new PayloadDeconstructor("payload-deconstructor"){{
            requirements(Category.units, with(Items.lead, 250, Items.silicon, 200, Items.graphite, 250));
            itemCapacity = 250;
            consumePower(3f);
            size = 5;
            deconstructSpeed = 2f;
        }};

        payloadConstructor = new Constructor("payload-constructor"){{
            requirements(Category.units, with(Items.silicon, 50, Items.lead, 70, Items.graphite, 50));
            hasPower = true;
            consumePower(2f);
            size = 3;
        }};

        largePayloadConstructor = new Constructor("large-payload-constructor"){{
            requirements(Category.units, with(Items.silicon, 100, Items.lead, 150, Items.graphite, 50, Items.phaseFabric, 40));
            hasPower = true;
            consumePower(2f);
            maxBlockSize = 4;
            minBlockSize = 3;
            size = 5;
        }};

        payloadLoader = new PayloadLoader("payload-loader"){{
            requirements(Category.units, with(Items.graphite, 50, Items.silicon, 50, Items.lead, 100));
            hasPower = true;
            consumePower(2f);
            size = 3;
        }};

        payloadUnloader = new PayloadUnloader("payload-unloader"){{
            requirements(Category.units, with(Items.graphite, 50, Items.silicon, 50, Items.lead, 100));
            hasPower = true;
            consumePower(2f);
            size = 3;
        }};
        //endregion
        //region power
        neoplasmGenerator = new SpreadGenerator("neoplasm-generator"){{
            requirements(Category.power, with(Items.tungsten, 500, Items.carbide, 100, Items.oxide, 150, Items.silicon, 400, Items.phaseFabric, 200));

            size = 4;
            liquidCapacity = 30f;
            squareSprite = false;

            spreadLiquids = new Liquid[]{Liquids.neoplasm};
            spreadMinWarmup = 0.5f;

            consumeLiquid(Liquids.neoplasm, 5f / 60f);

            powerProduction = 900 / 60f;

            ambientSound = Sounds.bioLoop;
            ambientSoundVolume = 0.2f;

            spreadAmount = 0.03f;

            spreadTarget = b -> b.liquids != null && !Mathf.zero(b.liquids.get(Liquids.water));

            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawLiquidTile(Liquids.neoplasm, 3f),

                    new DrawBubbles(Pal.neoplasm2),

                    new DrawCells(){{
                        color = Color.valueOf("c33e2b");
                        particleColorFrom = Color.valueOf("e8803f");
                        particleColorTo = Color.valueOf("8c1225");
                        particles = 50;
                        range = 7f;
                    }},
                    new DrawCircles(){{
                        color = Color.valueOf("CF582F").a(0.8f);
                        strokeMax = 0.6f;
                        radius = 55f / 4f;
                        amount = 4;
                        timeScl = 200f;
                    }},
                    new DrawDefault()
            );
        }};
        excitationReactor = new LaserReactor("excitation-reactor"){{
            size = 5;
            scaledHealth = 100;
            maxLaser = 200f;
            warmupSpeed = 0.003f;
            powerProduction = 120f;
            coolantPower = 0.5f;

            heating = 0.01f;

            consumeLiquid(DTLiquids.liquidCrystal, heating / coolantPower).update(false);
            liquidCapacity = 3f;
            explosionMinWarmup = 0.5f;

            ambientSound = Sounds.tractorbeam;
            ambientSoundVolume = 0.13f;
            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawPlasma(), new DrawSoftParticles(){{
                alpha = 0.2f;
                particleRad = 12f;
                particleSize = 9f;
                particleLife = 100f;
                particles = 10;
            }}, new DrawDefault(), new DrawFusion());
            requirements(Category.power, with(DTItems.iron, 300, Items.silicon, 200, Items.graphite, 300, Items.surgeAlloy, 100));
        }};

        ventTurbine = new ThermalGenerator("vent-turbine"){{
            requirements(Category.power, with(DTItems.iron, 60));
            attribute = Attribute.steam;
            displayEfficiencyScale = 1f / 9f;
            minEfficiency = 9f - 0.0001f;
            powerProduction = 5f / 9f;
            displayEfficiency = false;
            generateEffect = DTFx.ethyleneGenerate;
            effectChance = 0.04f;
            size = 3;
            ambientSound = Sounds.hum;
            ambientSoundVolume = 0.06f;

            drawer = new DrawMulti(new DrawDefault(), new DrawBlurSpin("-rotator", 0.6f * 9f){{
                blurThresh = 0.01f;
            }});

            hasLiquids = true;
            outputLiquid = new LiquidStack(Liquids.water, 5f / 60f / 9f);
            liquidCapacity = 20f;
            fogRadius = 3;
            researchCost = with(DTItems.iron, 15);
        }};

        turbineGenerator = new ConsumeGenerator("turbine-generator"){{
            requirements(Category.power, with(DTItems.iron, 60));
            size = 3;
            powerProduction = 1000/60f + 0.01f;
            rotate = true;
            consumeLiquid(DTLiquids.steam, 36f/60f);
            outputLiquid = new LiquidStack(Liquids.water, 32f/60f);
            liquidCapacity = 60f;
            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawRegion("-rod"){{
                        buildingRotate = true;
                    }},
                    new DrawTurbine(),
                    new DrawLiquidTile(DTLiquids.steam),
                    new DrawAllRotate(0)
            );
        }};
        spaceSolarPanel = new SpaceSolarGenerator("space-solar-panel"){{
            requirements(Category.power, with(DTItems.spaceStationPanel, 60));
            powerProduction = 5f;
            rotate = true;
            expandSpeed = 0.05f;
            rotateDraw = false;
            envEnabled = envRequired = Env.space;
            spaceFloor = new Floor[]{Blocks.space.asFloor(), Blocks.empty.asFloor()};
            spaceRect = new Rect(1, -1, 13, 3);
        }};
        rotateSolarPanel = new RotateSolarGenerator("rotate-solar-panel"){{
            requirements(Category.power, with(DTItems.iron, 150,Items.silicon, 140, Items.graphite, 100));
            size = 3;
            powerProduction = 1.4f;
        }};

        ironPowerNode = new PowerNode("iron-power-node"){{
            requirements(Category.power, with(DTItems.iron, 1, Items.lead, 3));
            maxNodes = 10;
            laserRange = 6;
        }};

        ironPowerNodeLarge = new PowerNode("iron-power-node-large"){{
            requirements(Category.power, with(DTItems.silver, 5, Items.lead, 10, Items.silicon, 3));
            size = 2;
            maxNodes = 15;
            laserRange = 15f;
        }};

        powerCapacitor = new Battery("power-capacitor"){{
            requirements(Category.power, with(DTItems.silver, 5, Items.lead, 20, Items.silicon, 10));
            size = 2;
            consumePowerBuffered(16000f);
            baseExplosiveness = 1f;
        }};
        //units
        asemblerConstructModule = new UnitAssemblerConstructModule("assembler-construct-module"){{
            size = 3;
            squareSprite = false;
            buildSpeed = 1.1f;
            consumeLiquid(Liquids.hydrogen, 3f / 60f);
            consumePower(20f/60f);
            requirements(Category.units, ItemStack.with(Items.tungsten, 200, Items.silicon, 300, Items.graphite, 500));
        }};

        assemblerExpandInterfaceModule = new UnitAssemblerInterfaceModule("assembler-expand-interface-module"){{
            requirements(Category.units, with(Items.oxide, 200, Items.tungsten, 400, Items.silicon,200, Items.graphite, 200));
            regionSuffix = "-dark";
            researchCostMultiplier = 0.75f;

            size = 3;
        }};

        unitFabricator = new UnitFactory("unit-fabricator"){{
            requirements(Category.units, with(DTItems.iron, 60, Items.lead, 70));
            plans = Seq.with(
                    new UnitPlan(DTUnitTypes.lancet, 60f * 15, with(Items.silicon, 15)),
                    new UnitPlan(DTUnitTypes.verity, 60f * 35, with(Items.silicon, 30, Items.lead, 15))
            );
            size = 3;
            consumePower(1.2f);
        }};

        additiveRefabricator = new Reconstructor("additive-refabricator"){{
            requirements(Category.units, with(DTItems.iron, 200, Items.lead, 120, Items.silicon, 90));

            size = 3;
            consumePower(3f);
            consumeItems(with(Items.silicon, 40, Items.graphite, 40));

            constructTime = 60f * 10f;

            upgrades.addAll(
                    new UnitType[]{DTUnitTypes.lancet, DTUnitTypes.talwar},
                    new UnitType[]{DTUnitTypes.verity, DTUnitTypes.truth}
            );
        }};

        multiplicativeRefabricator = new Reconstructor("multiplicative-refabricator"){{
            requirements(Category.units, with(Items.lead, 650, Items.silicon, 450, DTItems.silver, 350, DTItems.iridium, 650));

            size = 5;
            consumePower(6f);
            consumeItems(with(Items.silicon, 130, DTItems.silver, 80, Items.metaglass, 40));

            constructTime = 60f * 30f;

            upgrades.addAll(
                    new UnitType[]{DTUnitTypes.talwar, DTUnitTypes.estoc},
                    new UnitType[]{DTUnitTypes.truth, DTUnitTypes.solve}
            );
        }};

        exponentialRefabricatingPlatform = new ReconstructPlatform("exponential-refabricating-platform"){{
            requirements(Category.units, with(Items.lead, 2000, Items.silicon, 1000, DTItems.silver, 2000, DTItems.iridium, 750, DTItems.magnetismAlloy, 450, DTItems.conductionAlloy, 600));

            size = 7;
            consumePower(13f);
            consumeItems(with(Items.silicon, 850, DTItems.silver, 750, DTItems.conductionAlloy, 650));

            constructTime = 60f * 60f * 1.5f;

            dronePos = new float[]{
                    30, 30,
                    -30,30,
                    -30,-30,
                    30,-30
            };

            droneType = DTUnitTypes.refabricatingDrone;

            upgrades.addAll(
                    new UnitType[]{DTUnitTypes.estoc, DTUnitTypes.spear},
                    new UnitType[]{DTUnitTypes.solve, DTUnitTypes.essence}
            );
        }};

        tetrativeRefabricatingPlatform = new ReconstructPlatform("tetrative-refabricating-platform"){{
            requirements(Category.units, with(Items.lead, 4000, Items.silicon, 3000, DTItems.iridium, 1000, DTItems.conductionAlloy, 600, DTItems.magnetismAlloy, 600, Items.surgeAlloy, 800));

            size = 9;
            consumePower(25f);
            consumeItems(with(Items.silicon, 1000, DTItems.conductionAlloy, 600, Items.surgeAlloy, 500, DTItems.magnetismAlloy, 350));

            constructTime = 60f * 60f * 4;

            dronePos = new float[]{
                    38, 38,
                    -38,38,
                    -38,-38,
                    38,-38
            };

            droneType = DTUnitTypes.refabricatingDrone;

            upgrades.addAll(
                    new UnitType[]{DTUnitTypes.spear, DTUnitTypes.epee},
                    new UnitType[]{DTUnitTypes.essence, DTUnitTypes.celestial}
            );
        }};

        //endregion
        //region turret

        //TODO balancing

         overheat = new LaserTurret("overheat"){{
            requirements(Category.turret, with(Items.copper, 1200, Items.lead, 350, Items.graphite, 300, Items.surgeAlloy, 325, Items.silicon, 325));
            shootEffect = Fx.shootBigSmoke2;
            shootCone = 40f;
            recoil = 4f;
            size = 5;
            shake = 2f;
            range = 195f;
            reload = 90f;
            firingMoveFract = 0.5f;
            shootDuration = 230f;
            shootSound = Sounds.laserbig;
            loopSound = Sounds.beam;
            loopSoundVolume = 2f;
            envEnabled |= Env.space;

            shootType = new ContinuousLaserBulletType(78){{
                length = 200f;
                hitEffect = Fx.hitMeltdown;
                hitColor = Pal.meltdownHit;
                status = StatusEffects.melting;
                drawSize = 420f;

                incendChance = 0.4f;
                incendSpread = 5f;
                incendAmount = 1;
                ammoMultiplier = 1f;
            }
                @Override
                public void draw(Bullet b){
                    super.draw(b);
                    if(b.timer.get(2, 7)){
                        float fout = Mathf.clamp(b.time > b.lifetime - fadeTime ? 1f - (b.time - (lifetime - fadeTime)) / fadeTime : 1f);
                        float realLength = Damage.findLength(b, length * fout, laserAbsorb, pierceCap);
                        Vec2 target = new Vec2();
                        target.trns(b.rotation(), realLength);
                        target.add(b.x,b.y);
                        for (int i = 0; i < 2; i++) {
                            DTFx.chainFire.at(b.x,b.y,0,Color.valueOf("fedf30"), target);
                        }
                    }
                }
            };

            scaledHealth = 200;
            coolant = consumeCoolant(0.5f);
            consumePower(17f);
        }};

        impact = new ItemTurret("impact"){{
            requirements(Category.turret, with(Items.metaglass, 200, Items.lead, 400, Items.silicon, 400, Items.copper, 500));

            ammo(Items.graphite, new FlakBulletType(10f, 20){{
                        hitSize = 4.8f;
                        width = 10f;
                        height = 15f;
                        shootEffect = Fx.shootBig;
                        ammoMultiplier = 4;
                        knockback = 0.5f;
                        collidesGround = false;
                    }},
                    Items.blastCompound, new FlakBulletType(10f, 20){{
                        hitSize = 4.8f;
                        width = 10f;
                        height = 13f;
                        shootEffect = Fx.shootBig;
                        ammoMultiplier = 4;
                        knockback = 0.5f;
                        collidesGround = false;
                    }},
                    Items.metaglass, new FlakBulletType(10f, 20){{
                        ammoMultiplier = 4f;
                        shootEffect = Fx.shootSmall;
                        width = 10f;
                        height = 13f;
                        hitEffect = Fx.flakExplosion;
                        splashDamage = 45f;
                        splashDamageRadius = 25f;
                        fragBullet = new BasicBulletType(3f, 12, "bullet"){{
                            width = 5f;
                            height = 12f;
                            shrinkY = 1f;
                            lifetime = 20f;
                            backColor = Pal.gray;
                            frontColor = Color.white;
                            despawnEffect = Fx.none;
                        }};
                        fragBullets = 4;
                        explodeRange = 20f;
                        collidesGround = false;
                    }}
            );
            shoot = new ShootBarrel(){{
                barrels = new float[]{
                        2f, 2f, 0f,
                        8f, 0f, 0f,
                        -2f,2f, 0f,
                        -8f,0f, 0f
                };
                shots = 2;
                shotDelay = 2;
            }};
            shootY = 12f;

            reload = 5f;
            rotateSpeed = 3f;
            shootCone = 30f;
            consumeAmmoOnce = true;
            shootSound = Sounds.shootAltLong;

            drawer = new DrawTurret();

            targetGround = false;
            targetAir = true;
            inaccuracy = 8f;

            scaledHealth = 100;
            range = 360f;
            size = 4;

            coolant = consume(new ConsumeCoolant());
            coolantMultiplier = 2f;
            ammoPerShot = 1;
            maxAmmo = 60;

            limitRange(-5f);
        }};

        fracture = new ItemTurret("fracture"){{
            requirements(Category.turret, with(Items.surgeAlloy, 200, Items.oxide, 400, Items.silicon, 400, Items.beryllium, 500));

            ammo(Items.oxide ,new BasicBulletType(){{
                lightOpacity = 0.7f;
                damage = 60;
                hitColor = lightColor = Pal.berylShot;
                lightRadius = 70f;
                clipSize = 250f;
                shootEffect = DTFx.shootSparkBeryl;
                smokeEffect = Fx.shootBigSmoke;
                lifetime = 60f;
                sprite = "circle-bullet";
                backColor = Pal.berylShot;
                frontColor = Color.white;
                width = height = 12f;
                shrinkY = 0f;
                speed = 5f;
                trailLength = 20;
                trailWidth = 6f;
                trailColor = Pal.berylShot;
                trailInterval = 3f;
                splashDamage = 70f;
                splashDamageRadius = 20f;
                hitShake = 4f;
                trailRotation = true;
                status = StatusEffects.electrified;
                hitSound = Sounds.explosionbig;

                trailEffect = new Effect(16f, e -> {
                    color(Pal.berylShot);
                    for(int s : Mathf.signs){
                        Drawf.tri(e.x, e.y, 4f, 30f * e.fslope(), e.rotation + 90f*s);
                    }
                });
                hitEffect = despawnEffect = DTFx.hitFracture;
            }});

            reload = 17f;
            shootY = 10f;
            rotateSpeed = 2f;
            shootCone = 30f;
            consumeAmmoOnce = true;
            shootSound = Sounds.laser;

            Color haloColor = Pal.berylShot;
            float haloY = -10f, haloRotSpeed = 1.5f;
            var haloProgress = DrawPart.PartProgress.warmup;

            drawer = new DrawTurret("reinforced-"){{
                parts.addAll(new RegionPart("-mouth"){{
                                 moveY = -1.25f;
                                 under = true;
                                 progress = PartProgress.recoil;
                                 heatProgress = PartProgress.recoil.min(PartProgress.warmup);
                                 moves.add(new PartMove(PartProgress.recoil, 0f, -0.5f, 0f));
                             }},
                        new RegionPart("-mid"){{
                            under = true;
                            progress = PartProgress.recoil;
                            heatProgress = PartProgress.recoil.min(PartProgress.warmup);
                            heatColor = Color.green.cpy().a(0.5f);
                        }},
                        new RegionPart("-blade"){{
                            mirror = true;
                            under = true;
                            moveX = 1.75f;
                            moveY = -1f;
                            heatProgress = PartProgress.recoil.min(PartProgress.warmup);
                            heatColor = Color.green.cpy().a(0.5f);
                            moves.add(new PartMove(PartProgress.recoil, 0.4f, -3f, 0f));
                        }},
                        new ShapePart(){{
                            progress = haloProgress;
                            color = haloColor;
                            circle = true;
                            hollow = true;
                            stroke = 0f;
                            strokeTo = 2f;
                            radius = 10f;
                            layer = Layer.effect;
                            y = haloY;
                        }},
                        new ShapePart(){{
                            progress = haloProgress;
                            color = haloColor;
                            sides = 3;
                            rotation = 90f;
                            hollow = true;
                            stroke = 0f;
                            strokeTo = 2f;
                            radius = 3f;
                            layer = Layer.effect;
                            y = haloY;
                        }},
                        new HaloPart(){{
                            progress = haloProgress;
                            color = haloColor;
                            sides = 3;
                            shapes = 3;
                            hollow = true;
                            stroke = 0f;
                            strokeTo = 2f;
                            radius = 3f;
                            haloRadius = 10f + radius/2f;
                            haloRotateSpeed = haloRotSpeed;
                            layer = Layer.effect;
                            y = haloY;
                        }},

                        new HaloPart(){{
                            progress = haloProgress;
                            color = haloColor;
                            tri = true;
                            shapes = 4;
                            triLength = 0f;
                            triLengthTo = 10f;
                            radius = 6f;
                            haloRadius = 9f;
                            haloRotation = 180f;
                            layer = Layer.effect;
                            y = haloY;
                        }},
                        new HaloPart(){{
                            progress = haloProgress;
                            color = haloColor;
                            tri = true;
                            shapes = 4;
                            triLength = 0f;
                            triLengthTo = 3f;
                            radius = 6f;
                            haloRadius = 9f;
                            shapeRotation = 180f;
                            haloRotation = 180f;
                            layer = Layer.effect;
                            y = haloY;
                        }});
            }};

            targetGround = true;
            inaccuracy = 8f;

            shootWarmupSpeed = 0.07f;
            warmupMaintainTime = 15f;
            minWarmup = 0.96f;

            outlineColor = Pal.darkOutline;
            squareSprite = false;

            scaledHealth = 280;
            range = 310f;
            size = 5;

            coolant = consume(new ConsumeLiquid(Liquids.water, 20f / 60f));
            coolantMultiplier = 2.5f;
            consumePower(60);
            ammoPerShot = 1;

            limitRange(-5f);
        }};
        dissolve = new ContinuousLiquidTurret("dissolve"){{
            requirements(Category.turret, with(DTItems.nitride, 200, Items.graphite, 70, Items.silicon, 80, Items.beryllium, 90));

            reload = 50f;
            shake = 5f;
            range = 200f;
            recoil = 2f;
            squareSprite = false;
            rotateSpeed = 2;
            outlineColor = Pal.darkOutline;

            shootCone = 45;
            size = 5;
            shootY = 15;
            envEnabled |= Env.space;

            scaledHealth = 300;
            shootSound = Sounds.laser;
            float r = range;
            shootWarmupSpeed = 0.05f;
            minWarmup = 0.98f;
            liquidConsumed = 40f / 60f;
            ammo(Liquids.hydrogen, new ContinuousLaserBulletType(100){{
                length = r;
                width = 10;
                hitEffect = Fx.hitMeltdown;
                hitColor = Pal.meltdownHit;
                status = StatusEffects.melting;
                drawSize = 420f;

                incendChance = 0f;
                incendSpread = 0f;
                incendAmount = 0;
                ammoMultiplier = 1f;
            }});
            drawer = new DrawTurret("reinforced-"){{
                parts.addAll(
                        new RegionPart("-side"){{
                            heatProgress = PartProgress.warmup;
                            progress = PartProgress.warmup;
                            mirror = true;
                            moveX = 2f;
                            moveY = -2f;
                        }},
                        new RegionPart("-wing"){{
                            heatProgress = PartProgress.warmup;
                            progress = PartProgress.warmup;
                            mirror = true;
                            moveX = 1.5f;
                            moveY = -4f;
                            children.add(new RegionPart("-back"){{
                                heatProgress = PartProgress.warmup;
                                progress = PartProgress.warmup;
                                mirror = true;
                                moveX = 2f;
                                moveY = 2f;
                            }});
                        }},
                        new HaloPart(){{
                            mirror = true;
                            tri = true;
                            triLength = 0f;
                            triLengthTo = 17f;
                            radius = 3f;
                            radiusTo = 5f;
                            shapes = 1;
                            haloRotation = 9f;
                            progress = PartProgress.warmup;
                            x = 12;
                            y = -6;
                            moveX = 3f;
                            layer = Layer.effect;
                            color = Color.sky;
                        }},
                        new HaloPart(){{
                            mirror = true;
                            tri = true;
                            triLength = 0f;
                            triLengthTo = 3f;
                            radius = 3f;
                            radiusTo = 5f;
                            shapes = 1;
                            haloRotation = 9f;
                            shapeRotation = 180f;
                            progress = PartProgress.warmup;
                            x = 12;
                            y = -6;
                            moveX = 3f;
                            layer = Layer.effect;
                            color = Color.sky;
                        }},
                        new ShapePart(){{
                            progress = PartProgress.warmup;
                            circle = true;
                            hollow = true;
                            y = -9f;
                            radius = 5f;
                            radiusTo = 8f;
                            stroke = 0f;
                            strokeTo = 1.2f;
                            color = Color.sky;
                            layer = Layer.effect;
                        }},
                        new HaloPart(){{
                            progress = PartProgress.warmup;
                            color = Color.sky;
                            layer = Layer.effect;
                            y = -9;
                            haloRotateSpeed = -1;
                            shapes = 4;
                            triLength = 0f;
                            triLengthTo = 3f;
                            haloRotation = 45f;
                            haloRadius = 5f;
                            haloRadiusTo = 8f;
                            tri = true;
                            radius = 4f;
                        }},
                        new ShapePart(){{
                            progress = PartProgress.warmup.delay(0.7f);
                            circle = true;
                            hollow = true;
                            y = -9f;
                            radius = 2f;
                            radiusTo = 4f;
                            stroke = 0f;
                            strokeTo = 1f;
                            color = Color.sky;
                            layer = Layer.effect;
                        }},
                        new HaloPart(){{
                            mirror = true;
                            tri = true;
                            triLength = 0f;
                            triLengthTo = 13f;
                            radius = 4f;
                            shapes = 1;
                            shapeRotation = 90f;
                            progress = PartProgress.warmup.delay(0.7f);
                            x = -5f;
                            y = -19f;
                            moveX = -3f;
                            layer = Layer.effect;
                            color = Color.sky;
                        }},
                        new HaloPart(){{
                            mirror = true;
                            tri = true;
                            triLength = 0f;
                            triLengthTo = 5f;
                            radius = 4f;
                            shapes = 1;
                            shapeRotation = 270f;
                            progress = PartProgress.warmup.delay(0.7f);
                            x = -5f;
                            y = -19f;
                            moveX = -3f;
                            layer = Layer.effect;
                            color = Color.sky;
                        }});
                for(int i = 0; i < 3; i++){
                    int fi = i;
                    parts.add(new RegionPart("-spine"){{
                        progress = PartProgress.warmup;
                        heatProgress = PartProgress.warmup;
                        heatColor = Color.sky;
                        mirror = true;
                        under = true;
                        layerOffset = -0.3f;
                        turretHeatLayer = Layer.turret - 0.2f;
                        moveY = -22f / 4f - fi * 1.5f + 10f;
                        moveX = 52f / 4f - fi * 1f + 2f;
                        moveRot = -fi * 30f;
                        x = -5;
                        y = -7;

                        color = Color.sky.cpy();
                        moves.add(new PartMove(PartProgress.recoil, 0f, 0f, 15f * (1f + fi / 3f) ));
                    }});
                }
            }};
        }};

         blade = new ItemTurret("blade") {{
            requirements(Category.turret, with(Items.graphite, 70, Items.silicon, 80, Items.beryllium, 90));

            reload = 240f;
            shake = 4f;
            range = 360f;
            recoil = 2f;
            rotateSpeed = 3f;

            heatColor = Pal.berylShot.cpy().a(0.9f);

            shootCone = 15;
            size = 3;
            envEnabled |= Env.space;

            scaledHealth = 300;
            shootSound = Sounds.missileSmall;

            shoot = new ShootBarrel(){{
                shots = 4;
                shotDelay = 5;
                barrels = new float[]{
                        5.5f, -5f, 0f,
                        -5.5f, -5f, 0f
                };
                firstShotDelay = 100f;
            }};

            squareSprite = false;
            coolantMultiplier = 6f;
            coolant = consume(new ConsumeLiquid(Liquids.water, 15f / 60f));
            ammo(Items.silicon, new BulletType(){{
                shootEffect = new MultiEffect(Fx.shootBigColor, new WaveEffect(){{
                    colorFrom = colorTo = Pal.berylShot;
                    sizeTo = 15f;
                    lifetime = 12f;
                    strokeFrom = 3f;
                }});

                smokeEffect = new MultiEffect(Fx.shootBigSmoke2, DTFx.shootSmokeMissileBeryl);
                shake = 2f;
                speed = 0f;
                keepVelocity = false;
                inaccuracy = 2f;

                spawnUnit = new MissileUnitType("blade-missile"){{
                    trailColor = engineColor = Pal.berylShot;
                    engineSize = 1.75f;
                    engineOffset = 4f;
                    engineLayer = Layer.effect;
                    speed = 3.7f;
                    maxRange = 6f;
                    lifetime = 60f * 1.5f;
                    outlineColor = Pal.darkOutline;
                    health = 55;
                    lowAltitude = true;

                    parts.add(new FlarePart(){{
                        color1 = Pal.berylShot;
                        progress = PartProgress.life.slope().curve(Interp.pow2In);
                        radius = 0f;
                        radiusTo = 35f;
                        stroke = 3f;
                        rotation = 45f;
                        y = -5f;
                        followRotation = true;
                    }});

                    weapons.add(new Weapon(){{
                        shootCone = 360f;
                        mirror = false;
                        reload = 1f;
                        shootOnDeath = true;
                        bullet = new ExplosionBulletType(140f, 25f){{
                            shootEffect = new MultiEffect(Fx.massiveExplosion, new WrapEffect(Fx.dynamicSpikes, Pal.berylShot, 24f));
                        }};
                    }});
                }};
            }});
            drawer = new DrawTurret("reinforced-") {{
                parts.addAll(new RegionPart("-mid") {{
                    progress = PartProgress.recoil;
                    mirror = false;
                    under = true;
                    heatColor = Pal.berylShot.cpy().a(0.8f);
                }}, new RegionPart("-side") {{
                    progress = PartProgress.warmup;
                    mirror = true;
                    moveX = 2f;
                    under = true;
                    heatColor = Pal.berylShot.cpy().a(0.8f);
                }});
            }};
        }};

        encourage = new ItemTurret("encourage"){{
            requirements(Category.turret, with(Items.graphite, 70, Items.silicon, 80, Items.beryllium, 90));

            reload = 300f;
            shake = 5f;
            range = 300f;
            recoil = 3f;
            squareSprite = false;

            shootCone = 5;
            size = 3;
            envEnabled |= Env.space;

            scaledHealth = 300;
            shootSound = Sounds.lasershoot;

            coolantMultiplier = 6f;
            minWarmup = 0.9f;
            coolant = consume(new ConsumeLiquid(Liquids.water, 15f / 60f));
            ammo(Items.tungsten, new BasicBulletType(7, 150){{
                float rad = 20;
                scaleLife = true;
                splashDamage = 250;
                width = height = 15f;
                sprite = "circle-bullet";
                backColor = Pal.redLight;
                frontColor = Color.white;
                shrinkY = 0;
                keepVelocity = true;
                trailColor = Pal.redLight;
                trailLength = 15;
                trailWidth = 5;
                hitSound = Sounds.release;

                reflectable = false;
                splashDamageRadius = rad;
                ammoMultiplier = 1f;
                shootEffect = smokeEffect = new MultiEffect(Fx.shootBigSmoke, DTFx.shootEncourage);
                hitEffect = new Effect(50f, 100f, e -> {
                    e.scaled(7f, b -> {
                        color(Pal.redLight, b.fout());
                        Fill.circle(e.x, e.y, rad);
                    });

                    color(Pal.redLight);
                    stroke(e.fout() * 3f);
                    Lines.circle(e.x, e.y, rad);
                });
                fragBullet = new ShrapnelBulletType(){{
                    damage = 50;
                    length = 15;
                    width = 6;
                    lifetime = 100;
                    toColor = Pal.redLight;
                }};
                ammoPerShot = 6;

                fragBullets = 3;
                fragSpread = 120;
                fragRandomSpread = 0;

                limitRange();
            }});
            drawer = new DrawTurret("reinforced-"){{
                parts.addAll(new RegionPart("-blade"){{
                    heatProgress = PartProgress.recoil.add(-0.1f).clamp();
                    progress = PartProgress.warmup;
                    mirror = true;
                    moveX = 1f;
                    moves.add(new PartMove(PartProgress.recoil, 0f, 0f, -10f));
                    under = true;
                    children.addAll(
                            new RegionPart("-front"){{
                                progress = PartProgress.warmup;
                                mirror = true;
                                under = true;
                                moveX = 2;
                                moveY = -2;
                                moves.add(new PartMove(PartProgress.recoil, 0f, 0f, 0f));
                            }},
                            new RegionPart("-back"){{
                                progress = PartProgress.warmup;
                                mirror = true;
                                under = true;
                                moveX = moveY = -0.5f;
                                moves.add(new PartMove(PartProgress.recoil, 0f, 0f, 0f));
                            }}
                    );
                }});
            }};
        }};

        aegis = new PowerTurret("aegis"){{
            requirements(Category.turret, with(Items.graphite, 70, Items.silicon, 80, Items.beryllium, 90));

            reload = 60f;
            shake = 4f;
            range = 150f;
            recoil = 1f;
            squareSprite = false;

            shootCone = 3;
            size = 2;
            envEnabled |= Env.space;
            targetAir = targetGround = targetHealing = true;

            scaledHealth = 300;
            shootSound = Sounds.lasershoot;

            consumePower(1f);
            shootType = new LaserBoltBulletType(5f, 10f){{
                lifetime = 30f;
                healPercent = 5f;
                collidesTeam = true;
                backColor = Pal.heal;
                frontColor = Color.white;
                shootEffect = Fx.none;
            }};

            shoot = new ShootBarrel(){{
                shots = 4;
                barrels = new float[]{
                        3f, -2f, 0f,
                        -3f, -2f, 0f
                };
                shotDelay = 5f;
            }};
            minWarmup = 0.7f;
            drawer = new DrawTurret("reinforced-"){{
                parts.add(new RegionPart("-side"){{
                    progress = PartProgress.warmup;
                    mirror = true;
                    moveX = 0.8f;
                    moves.add(new PartMove(PartProgress.recoil, 0f, -0.5f, 0f));
                    under = true;
                }});
            }};
            limitRange(2);
        }};

        permeation = new ItemTurret("permeation"){{
            requirements(Category.turret, with(Items.graphite, 70, Items.silicon, 80, Items.beryllium, 90));

            reload = 50f;
            shake = 4f;
            range = 60f;
            recoil = 2f;
            squareSprite = false;

            heatColor = Color.sky.cpy().a(0.9f);

            shootCone = 10;
            size = 2;
            envEnabled |= Env.space;

            scaledHealth = 300;
            shootSound = Sounds.shotgun;

            coolantMultiplier = 6f;
            coolant = consume(new ConsumeLiquid(Liquids.water, 15f / 60f));
            ammo(Items.graphite, new ShrapnelBulletType(){{
                length = 50f;
                damage = 85f;
                ammoMultiplier = 5f;
                toColor = Color.sky;
                shootEffect = smokeEffect = Fx.shootBigSmoke;
            }});
            drawer = new DrawTurret("reinforced-"){{
                parts.addAll(new RegionPart("-mid"){{
                    progress = PartProgress.recoil;
                    heatProgress = PartProgress.recoil.add(-0.1f).clamp();
                    mirror = false;
                    under = true;
                    moveY = -0.7f;
                }}, new RegionPart("-side"){{
                    heatProgress = PartProgress.recoil.add(-0.1f).clamp();
                    progress = PartProgress.warmup;
                    mirror = true;
                    moveX = -0.7f;
                    moves.add(new PartMove(PartProgress.warmup, 2f, 0f, 0f));
                    under = true;
                }});
            }};
        }};
        holy = new PowerTurret("holy"){{
            scaledHealth = 260;
            size = 1;
            targetAir = true;
            targetGround = true;
            targetHealing = true;
            rotateSpeed = 5;
            coolant = consumeCoolant(0.2f);
            consumePower(2f);
            drawer = new DrawTurret("framed-");
            reload = 10;
            maxAmmo = 10;
            range = 180;
            shootSound = Sounds.lasershoot;
            inaccuracy = 3;
            shootEffect = Fx.shootHeal;
            shootType = new LaserBoltBulletType(5,10){{
                lifetime = 40;
                healPercent = 2;
                status = StatusEffects.corroded;
                frontColor = Color.white;
                backColor = Pal.heal;
                collidesTeam = true;
            }};
            requirements(Category.turret, with(DTItems.iron, 60, Items.graphite, 50, Items.silicon, 70));
        }};

        shard = new PowerTurret("shard"){{
            requirements(Category.turret, with(DTItems.iron, 60, Items.lead, 70, Items.silicon, 60));
            range = 145f;

            shoot.firstShotDelay = 40f;

            recoil = 2f;
            reload = 60f;
            shake = 2f;
            shootEffect = Fx.lancerLaserShoot;
            smokeEffect = Fx.none;
            heatColor = Color.red;
            size = 2;
            scaledHealth = 280;
            targetAir = false;
            moveWhileCharging = false;
            accurateDelay = true;
            shootSound = Sounds.laser;
            coolant = consumeCoolant(0.2f);

            consumePower(6f);

            shootType = new LaserBulletType(140){{
                colors = new Color[]{Pal.lancerLaser.cpy().a(0.4f), Pal.lancerLaser, Color.white};
                //TODO merge
                chargeEffect = new MultiEffect(Fx.lancerLaserCharge, Fx.lancerLaserChargeBegin);

                buildingDamageMultiplier = 0.25f;
                hitEffect = Fx.hitLancer;
                hitSize = 4;
                lifetime = 16f;
                drawSize = 400f;
                collidesAir = false;
                length = 173f;
                ammoMultiplier = 1f;
                pierceCap = 4;
            }};
        }};
        aerolite = new ItemTurret("aerolite"){{
            scaledHealth = 260;
            size = 2;
            targetAir = false;
            targetGround = true;
            rotateSpeed = 5;
            coolant = consumeCoolant(0.2f);
            drawer = new DrawTurret("framed-");
            reload = 40;
            maxAmmo = 10;
            range = 190;
            shootSound = Sounds.bang;
            inaccuracy = 3;
            shootEffect = Fx.shootSmokeSquareBig;
            ammo(
                    DTItems.iron, new ArtilleryBulletType(4f, 30){{
                        knockback = 0.8f;
                        lifetime = 80f;
                        width = height = 15f;
                        collidesTiles = false;
                        splashDamageRadius = 25f * 0.75f;
                        splashDamage = 20f;
                    }},
                    Items.graphite, new ArtilleryBulletType(4f, 20){{
                        knockback = 0.8f;
                        lifetime = 80f;
                        width = height = 13f;
                        collidesTiles = false;
                        splashDamageRadius = 25f * 0.75f;
                        splashDamage = 33f;
                    }}
            );
            requirements(Category.turret, with(DTItems.iron, 60, Items.graphite, 50, Items.silicon, 70));
        }};
        sparkover = new ElectricTowerTurret("sparkover"){{
            hasPower = true;
            size = 2;
            range = 200f;
            damage = 100f;
            reload = 150f;
            maxTargets = 15;

            targetAir = true;
            targetGround = true;

            consumePower(4f);

            requirements(Category.turret, with(DTItems.iron, 200, Items.silicon, 110, DTItems.iridium, 90));
        }};

        focus = new ContinuousTurret("focus"){{
            consumePower(3);
            loopSound = Sounds.tractorbeam;
            requirements(Category.turret, with(DTItems.iron,150, Items.silicon, 100, Items.lead, 160));
            shootType = new PointLaserBulletType(){{
                beamEffectInterval = 7;
                color = Color.valueOf("9fb6ff");
                sprite = "disintegration-focus-laser";
            }};
            drawer = new DrawTurret("framed-"){{
                parts.addAll(new RegionPart("-side"){{
                    moveX = 1;
                    moveY = -2;
                    mirror = true;
                }});
            }};
            range = 120f;
            rotateSpeed = 3f;
            size = 2;
            scaledHealth = 200;
        }};
        axe = new ItemTurret("axe") {{
            requirements(Category.turret, with(Items.graphite, 70, Items.silicon, 80, DTItems.iron, 90));

            reload = 300f;
            shake = 4f;
            range = 440f;
            recoil = 2f;
            rotateSpeed = 3f;

            shootCone = 10;
            size = 2;
            envEnabled |= Env.space;

            scaledHealth = 300;
            shootSound = Sounds.missileSmall;

            shoot = new ShootBarrel(){{
                shots = 4;
                shotDelay = 10;
                barrels = new float[]{
                        3f, -6f, 0f,
                        -3f, -6f, 0f
                };
            }};

            coolant = consumeCoolant(0.2f);
            ammo(Items.blastCompound, new BulletType(){{
                shootEffect = Fx.shootBig;

                smokeEffect = Fx.shootBigSmoke2;
                shake = 2f;
                speed = 0f;
                keepVelocity = false;
                inaccuracy = 2f;

                spawnUnit = new MissileUnitType("axe-missile"){{
                    trailColor = engineColor = Pal.missileYellow;
                    engineSize = 1.75f;
                    engineOffset = 3.5f;
                    engineLayer = Layer.effect;
                    speed = 4.8f;
                    maxRange = 6f;
                    lifetime = 115;
                    outlineColor = Pal.darkOutline;
                    health = 5;
                    lowAltitude = true;

                    weapons.add(new Weapon(){{
                        shootCone = 360f;
                        mirror = false;
                        reload = 0f;
                        shootOnDeath = true;
                        bullet = new ExplosionBulletType(70f, 25f){{
                            shootEffect = new MultiEffect(Fx.massiveExplosion, new WrapEffect(Fx.dynamicSpikes, Pal.missileYellow, 24f));
                        }};
                    }});
                }};
                ammoPerShot = 5;
            }});
            drawer = new DrawTurret("framed-") {{
                parts.addAll(new RegionPart("-bottom"){},
                        new RegionPart("-display"){{
                            progress = PartProgress.reload;
                            moveY = -4f;
                        }},
                        new RegionPart("-top"){}
                );
            }};
            limitRange();
        }};
        ambush = new ItemTurret("ambush"){{
            requirements(Category.turret, with(DTItems.iron, 90, Items.graphite, 40));
            ammo(
                    Items.silicon, new ArtilleryBulletType(3f, 1){{
                        knockback = 0.8f;
                        lifetime = 80f;
                        width = height = 15f;
                        collidesTiles = false;
                        fragBullet = new BlockBulletType(){{
                            shrinkY = shrinkX = 0;
                            speed = 3f;
                            damage = 1f;
                            rotateSprite = false;
                            hitEffect = Fx.coreBuildBlock;
                        }};
                        fragOnHit = true;
                        fragOnAbsorb = true;
                        fragBullets = 5;
                        fragLifeMax = 0.2f;
                        fragLifeMin = 0.5f;
                        fragVelocityMax = 0.5f;
                        fragVelocityMin = 0.1f;
                    }}
            );
            targetAir = false;
            reload = 80f;
            size = 2;
            recoil = 2f;
            range = 235f;
            inaccuracy = 1f;
            shootCone = 10f;
            health = 260;
            shootSound = Sounds.bang;
            coolant = consumeCoolant(0.3f);
            limitRange(0f);
        }};
        voltage = new PowerTurret("voltage"){{
            scaledHealth = 260;
            size = 3;
            targetAir = true;
            targetGround = true;
            rotateSpeed = 5;
            coolant = consumeCoolant(0.2f);
            consumePower(5f);
            drawer = new DrawTurret("framed-");
            reload = 420;
            maxAmmo = 10;
            range = 180;
            shootSound = Sounds.laser;
            inaccuracy = 3;
            shootType = new RandomSpreadBulletType(1,120){{
                shrinkY = 0;
                drag = 0.005f;
                lifetime = 360;
                width = height = 15f;
                status = StatusEffects.shocked;
                sprite = "circle-bullet";
                backColor = frontColor = Pal2.lightningWhite;
                intervalChance = 0.05f;
                scaleLife = false;

                hittable = reflectable = false;

                homingPower = 0.02f;
                shootEffect = despawnEffect = hitEffect = new ExplosionEffect(){{
                    waveColor = Pal.regen;
                    waveStroke = 4f;
                    waveRad = 40f;
                }};
                intervalBullet = new ConnectBulletType(){{
                    damage = 40;
                    lifetime = speed = 0;

                    hitColor = Pal2.lightningWhite;
                    chainHitEffect = Fx.lightning;
                    chainEffect = Fx.chainLightning;
                    hitEffect = Fx.none;
                    radius = 50f;
                    chainColor = Pal.lancerLaser;
                    lightning = 2;
                    lightningColor = Pal.lancerLaser;
                    lightningLength = 7;
                }};
                lightning = 5;
                lightningColor = Pal.lancerLaser;
                lightningLength = 8;
            }};
            requirements(Category.turret, with(DTItems.iridium, 1));
        }};
        sabre = new PowerTurret("sabre"){{
            requirements(Category.turret, with());
            Color color = Color.valueOf("d370d3");
            shootType = new WarpBulletType(8f, 20f){{
                sprite = "missile-large";

                lifetime = 45f;
                width = 12f;
                height = 22f;

                shoot = new ShootSpread(3, 20f);

                hitSize = 7f;
                shootEffect = Fx.shootSmokeSquareBig;
                smokeEffect = Fx.shootSmokeDisperse;
                hitColor = backColor = trailColor = color;
                frontColor = Color.white;
                trailWidth = 3f;
                trailLength = 12;
                despawnEffect = Fx.hitBulletColor;
                buildingDamageMultiplier = 0.3f;

                pierce = pierceBuilding = true;

                trailEffect = Fx.colorSpark;
                trailRotation = true;
                trailInterval = 3f;

                warpRotMin = -30f;
                warpRotMax = 30f;
                warpDistance = 0f;

                splashDamage = 10f;
                splashDamageRadius = 10f;

                homingPower = 0.01f;
                homingDelay = 19f;
                homingRange = 160f;

                despawnShake = 3f;
                hitEffect = Fx.hitSquaresColor;
                collidesGround = true;
            }};
            consumePower(10f);

            reload = 30f;
            range = 500;
            shootCone = 30f;
            scaledHealth = 370;
            rotateSpeed = 2f;
            recoil = 0.5f;
            recoilTime = 30f;
            shake = 3f;
            limitRange(9f);
        }};

        condense = new ContinuousTurret("condense"){{
            loopSound = Sounds.tractorbeam;
            requirements(Category.turret, with());
            size = 5;
            range = 250;
            aimChangeSpeed = 0.9f;
            rotateSpeed = 1f;
            consumePower(17f);
            drawer = new DrawTurret("framed-");
            shootType = new PointLaserBulletType(){{
                sprite = "disintegration-freeze-laser";
                damage = 200f;
                buildingDamageMultiplier = 0.3f;

                hitColor = Color.valueOf("77bcfe");
                status = DTStatusEffects.frozen;
            }


            @Override
            public void draw(Bullet b){
                Lines.stroke(3);
                Draw.color(Color.valueOf("77aaff"));
                Lines.ellipse((b.x+b.aimX)/2,(b.y+b.aimY)/2,10,Mathf.dst(b.x,b.y,b.aimX,b.aimY)/20,b.fslope()*(Mathf.absin(3,0.3f)+0.2f),Mathf.atan2(b.x-b.aimX,b.y-b.aimY)*Mathf.radiansToDegrees);
                super.draw(b);

                rand.setSeed(b.id);
                float base = (Time.time / 30);
                Draw.color(Color.valueOf("97dcfe"));
                Lines.stroke(b.fslope()*(Mathf.absin(1.7f,0.3f)+3f));
                Lines.circle(b.aimX,b.aimY,b.fslope()*Mathf.absin(3,2f)+6.5f);
                for(int i = 0; i < 20; i++){
                    float fin = (rand.random(1f) + base) % 1f, fout = 1f - fin;
                    float angle = rand.random(360f) + b.rotation();
                    float len = 36 * Interp.pow2Out.apply(fout);
                    Draw.color(Color.valueOf("97dcfe"));
                    Lines.stroke(Interp.pow3Out.apply(fin) * 2 * b.fslope());
                    Lines.lineAngle(b.aimX + Angles.trnsx(angle, len), b.aimY + Angles.trnsy(angle, len), angle, 12 * Interp.pow3Out.apply(fin) * b.fslope());
                }
            }
            };
        }};

        blaze = new LaserTurret("blaze"){{
            requirements(Category.turret, with(DTItems.iron, 1200, Items.lead, 350, Items.graphite, 300, Items.surgeAlloy, 325, Items.silicon, 325));
            shootEffect = Fx.shootBigSmoke2;
            shootCone = 40f;
            recoil = 4f;
            size = 5;
            shake = 2f;
            range = 195f;
            reload = 90f;
            firingMoveFract = 0.5f;
            rotateSpeed = 2;
            shootDuration = 230f;
            shootSound = Sounds.laserbig;
            loopSound = Sounds.beam;
            loopSoundVolume = 2f;
            envEnabled |= Env.space;

            drawer = new DrawTurret("framed-");

            shootType = new ContinuousLaserBulletType(80){{
                length = 250f;
                width = 12f;
                hitEffect = Fx.hitMeltdown;
                hitColor = Pal.meltdownHit;
                status = StatusEffects.melting;
                drawSize = 420f;

                incendChance = 0.4f;
                incendSpread = 5f;
                incendAmount = 1;
                ammoMultiplier = 1f;
                colors = new Color[]{Color.valueOf("ec945855"), Color.valueOf("fcb44aaa"), Color.valueOf("ffbc5a"), Color.white};
            }
                @Override
                public void draw(Bullet b){
                    super.draw(b);
                    if(b.timer.get(2, 7)){
                        float fout = Mathf.clamp(b.time > b.lifetime - fadeTime ? 1f - (b.time - (lifetime - fadeTime)) / fadeTime : 1f);
                        float realLength = Damage.findLength(b, length * fout, laserAbsorb, pierceCap);
                        Vec2 target = new Vec2();
                        target.trns(b.rotation(), realLength);
                        target.add(b.x,b.y);
                        for (int i = 0; i < 2; i++) {
                            DTFx.chainFire.at(b.x,b.y,0,Color.valueOf("fedf30"), target);
                        }
                    }
                }
            };

            scaledHealth = 200;
            coolant = consumeCoolant(0.5f);
            consumePower(17f);
        }};
        //endregion
        //region drill
        quarry = new Quarry("quarry"){{
           size = 3;
           regionRotated1 = 1;
           itemCapacity = 100;
           acceptsItems = true;

           areaSize = 11;
           liquidBoostIntensity = 1.5f;
           mineTime = 400f;

           deploySpeed = 0.015f;
           deployInterp = new Interp.PowOut(4);
           deployInterpInverse = new Interp.PowIn(4);
           drillMoveSpeed = 0.07f;
           consumePower(20);
           consumeLiquid(Liquids.hydrogen, 5f / 60f);
           consumeLiquid(Liquids.nitrogen, 6f / 60f).boost();
           requirements(Category.production, with(DTItems.iridium, 1));
        }};
        pressureDrill = new Drill("pressure-drill"){{
            requirements(Category.production, with(DTItems.iron, 12), true);
            tier = 2;
            drillTime = 500;
            size = 2;

            envEnabled ^= Env.space;

            consumeLiquid(Liquids.water, 0.05f).boost();
        }};

        stiffDrill = new Drill("stiff-drill"){{
            requirements(Category.production, with(DTItems.iron, 50, Items.graphite, 10), true);
            tier = 3;
            drillTime = 280;
            size = 3;

            envEnabled ^= Env.space;

            consumeLiquid(Liquids.water, 0.05f).boost();
        }};
        rockExtractor = new GenericCrafter("rock-extractor"){{
            requirements(Category.production, ItemStack.with(Items.graphite, 40, DTItems.iron, 50));
            consumePower(0.6f);
            size = 2;
            ambientSound = Sounds.drill;
            updateEffect = Fx.pulverizeSmall;
            craftEffect = Fx.mine;
            craftTime = 60f / 2f;
            drawer = new DrawMulti(new DrawDefault(), new DrawRegion("-rotator", 1.4f, true), new DrawRegion("-top"));
            outputItem = new ItemStack(Items.sand, 3);
        }};

        portableDrill = new PortableDrill("portable-drill"){{
            hasItems = true;
            hasLiquids = true;
            liquidBoostIntensity = 2.56f;
            consumeLiquid(Liquids.water, 0.02f).boost();
            removalEffect = Fx.mineHuge;
            portableUnitType = new UnitType("portable-drill-unit"){{
                speed = 0.5f;
                legStraightness = 0f;
                legLength = 8f;
                lockLegBase = true;
                legContinuousMove = true;
                legExtension = -4f;
                legBaseOffset = 2f;
                legMaxLength = 1.1f;
                legMinLength = 0.2f;
                legLengthScl = 0.96f;
                legForwardScl = 1.1f;
                legGroupSize = 2;
                rippleScale = 0f;
                rotateSpeed = 0;
                constructor = LegsUnit::create;
                legCount = 4;
                drawCell = true;
                hidden = true;
                abilities.add(new PortableBlockAbility(){{
                    placeEffect = Fx.mineImpactWave;
                    placeSound = Sounds.drillImpact;
                }});
            }};
            requirements(Category.production, with(Items.copper, 12));
        }};
        ((PortableBlockAbility)((PortableDrill)portableDrill).portableUnitType.abilities.get(0)).unitContent = portableDrill;

        new PortableDrillUnloadPoint("portable-drill-unloader"){{
            size = 2;
            range = 90f;
            update = true;
            solid = true;
            hasItems = true;
            itemCapacity = 50;
            requirements(Category.production, with(Items.copper, 12));
        }};
        //endregion
        //region effect
        repairDroneStation = new RepairDroneStation("repair-drone-station"){{
            size = 3;
            unitType = DTUnitTypes.repairDrone;
            consumeLiquid(Liquids.ozone, 3f / 60f);
            consumePower(1);
            hasLiquids = true;
            requirements(Category.effect, ItemStack.with(Items.beryllium, 100, Items.graphite, 70, Items.silicon, 60, Items.oxide, 30));
        }};
        blastMine = new ShockMine("blast-mine"){{
            requirements(Category.effect, with(DTItems.iron, 15, Items.silicon, 12));
            hasShadow = false;
            health = 50;
            bullet = new ExplosionBulletType(20, 10){{
                hitEffect = Fx.pointHit;
            }};
            tileDamage = 7f;
            tendrils = 0;
        }};

        for (BulletType b : ((ItemTurret)ambush).ammoTypes.values()) {
            ((BlockBulletType)b.fragBullet).bulletContent = blastMine;
        }

        for (int i = 1; i <= 9; i+=2) {
            int finalI = i;
            new FloorBuilder("space-station-builder-" + finalI){{
                requirements(Category.effect, with(DTItems.spaceStationPanel, 20 + 2 * finalI * finalI));
                size = 1;
                envEnabled = envRequired = Env.space;
                range = finalI;
                rotate = true;
                floorOffset = range + 1;
                rotateDraw = false;
                floor = spaceStationFloor.asFloor();
                buildCostMultiplier = 0.05f;
                whiteList = Seq.with(Blocks.empty.asFloor());
            }};
        }

        for (int i = 1; i <= 9; i+=2) {
            int finalI = i;
            new FloorBuilder("space-station-breaker-" + finalI){{
                requirements(Category.effect, with(DTItems.spaceStationPanel, 20));
                size = 1;
                envEnabled = envRequired = Env.space;
                range = finalI;
                floor = Blocks.empty.asFloor();
                buildCostMultiplier = 0.05f;
                returnItem = new ItemStack(DTItems.spaceStationPanel, DTVars.spaceStationBaseRequirement);
                whiteList = Seq.with(spaceStationFloor.asFloor());
            }};
        }
        //endregion
        //region campaign
        spaceStationLaunchPad = new SpaceStationLaunchPad("space-station-launch-pad"){{
            requirements(Category.effect, with(Items.copper, 10));
            size = 10;
            health = 9000;
        }};

        orbitalLaunchPad = new OrbitalLaunchPad("orbital-launch-pad"){{
            requirements(Category.effect, BuildVisibility.shown, with(Items.copper, 350, Items.silicon, 140, Items.lead, 200, Items.titanium, 150));
            size = 5;
            itemCapacity = 100;
            launchTime = 60f * 20;
            hasPower = true;
            consumePower(4f);
        }};
        //endregion
        //region debug
        sandboxBlock = new DebugBlock("sandbox-block"){{
            buildVisibility = DTVars.debugMode ? BuildVisibility.shown : BuildVisibility.hidden;
            envEnabled = Env.any;
            runs = b -> Vars.state.rules.infiniteResources = !Vars.state.rules.infiniteResources;
            buildCostMultiplier = 0.01f;
            requirements(Category.effect, with(), true);
        }};

        editorBlock = new DebugBlock("editor-block"){{
            buildVisibility = DTVars.debugMode ? BuildVisibility.shown : BuildVisibility.hidden;
            envEnabled = Env.any;
            runs = b -> Vars.state.rules.editor = !Vars.state.rules.editor;
            buildCostMultiplier = 0.01f;
            requirements(Category.effect, with(), true);
        }};

        cheatBlock = new DebugBlock("cheat-block"){{
            buildVisibility = DTVars.debugMode ? BuildVisibility.shown : BuildVisibility.hidden;
            envEnabled = Env.any;
            runs = b -> Vars.state.rules.teams.get(b.team).cheat = Vars.state.rules.teams.get(b.team).cheat;
            buildCostMultiplier = 0.01f;
            requirements(Category.effect, with(), true);
        }};

        shaderTestBlock = new ShaderTestBlock("shader-test-block"){{
            buildVisibility = DTVars.debugMode ? BuildVisibility.shown : BuildVisibility.hidden;
            envEnabled = Env.any;
            shader = DTShaders.blackHole;
            hasShadow = false;
            requirements(Category.effect, with(), true);
        }};
        dpsBlock = new DPSBlock("dps-block"){{
            size = 5;
            health = 999999;
            envEnabled = Env.any;
            buildVisibility = DTVars.debugMode ? BuildVisibility.shown : BuildVisibility.hidden;
            requirements(Category.effect, with(), true);
        }};

        blackHoleBlock = new BlackHoleBlock("black-hole-block"){{
            buildVisibility = DTVars.debugMode ? BuildVisibility.shown : BuildVisibility.hidden;
            envEnabled = Env.any;
            buildCostMultiplier = 0.01f;
            requirements(Category.effect, with(), true);
        }};

        blackHoleClearBlock = new DebugBlock("black-hole-clear-block"){{
            buildVisibility = DTVars.debugMode ? BuildVisibility.shown : BuildVisibility.hidden;
            envEnabled = Env.any;
            buildCostMultiplier = 0.01f;
            runs = b -> DTGroups.blackHole.each(Entityc::remove);
            requirements(Category.effect, with(), true);
        }};
        //endregion
    }
}
