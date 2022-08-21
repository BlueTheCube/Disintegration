package degradation.content;

import arc.graphics.Color;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Mathf;
import degradation.graphics.Pal2;
import mindustry.ai.types.BuilderAI;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.bullet.EmpBulletType;
import mindustry.entities.bullet.LaserBoltBulletType;
import mindustry.gen.EntityMapping;
import mindustry.gen.Sounds;
import mindustry.gen.UnitEntity;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.type.ammo.PowerAmmoType;

import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Lines.stroke;

public class DTUnitTypes {
    public static UnitType
            //air-Hyper
            lancet, raven,
            //ground-Hyper
            essence, truth, solve,
            //air-offensive
            knife,
            //ground-offensive
            //air-subsidiary
            //ground-subsidiary
            //core-unit
            separate
            ;
    public static void load(){
        //air-Hyper
        //T1 lancet
        lancet = new UnitType("lancet"){{
            constructor = UnitEntity::create;
            speed = 2.7f;
            accel = 0.08f;
            drag = 0.04f;
            armor = 2;
            flying = true;
            health = 200;
            engineOffset = 10;
            engineSize = 3.7f;
            hitSize = 11;
            weapons.add(new Weapon(){{
                    y = 10f;
                    x = 0f;
                    reload = 140f;
                    ejectEffect = Fx.hitLancer;
                    bullet = new EmpBulletType(){{
                            float rad = 48f;
                            shootSound = Sounds.laser;
                            scaleLife = true;
                            lightOpacity = 0.7f;
                            timeIncrease = 1f;
                            timeDuration = 60f * 20f;
                            powerDamageScl = 1f;
                            damage = 9;
                            hitColor = lightColor = Pal2.hyperBlue;
                            lightRadius = 40f;
                            clipSize = 250f;
                            shootEffect = Fx.hitLancer;
                            smokeEffect = Fx.shootBigSmoke2;
                            lifetime = 60f;
                            sprite = "circle-bullet";
                            backColor = Pal2.hyperBlue;
                            frontColor = Color.white;
                            width = height = 9f;
                            shrinkY = 0f;
                            speed = 3f;
                            trailLength = 10;
                            trailWidth = 4.5f;
                            trailColor = Pal2.hyperBlue;
                            trailInterval = 5f;
                            splashDamage = 8f;
                            splashDamageRadius = rad;
                            radius = rad;
                            hitShake = 4f;
                            trailRotation = true;
                            status = DTStatusEffects.electricResonated;
                            hitSound = Sounds.plasmaboom;
                            hitPowerEffect = Fx.hitLancer;
                            chainEffect = Fx.chainLightning;

                            trailEffect = new Effect(16f, e -> {
                                color(Pal2.hyperBlue);
                                for(int s : Mathf.signs){
                                    Drawf.tri(e.x, e.y, 4f, 10f * e.fslope(), e.rotation + 90f*s);
                                }
                            });

                            hitEffect = new Effect(50f, 100f, e -> {
                                e.scaled(7f, b -> {
                                    color(Pal2.hyperBlue, b.fout());
                                    Fill.circle(e.x, e.y, rad);
                                });

                                color(Pal2.hyperBlue);
                                stroke(e.fout() * 3f);
                                Lines.circle(e.x, e.y, rad);

                                int points = 10;
                                float offset = Mathf.randomSeed(e.id, 360f);
                                for(int i = 0; i < points; i++){
                                    float angle = i* 360f / points + offset;
                                    //for(int s : Mathf.zeroOne){
                                    Drawf.tri(e.x + Angles.trnsx(angle, rad), e.y + Angles.trnsy(angle, rad), 6f, 15f * e.fout(), angle/* + s*180f*/);
                                    //}
                                }

                                Fill.circle(e.x, e.y, 12f * e.fout());
                                color();
                                Fill.circle(e.x, e.y, 6f * e.fout());
                                Drawf.light(e.x, e.y, rad * 1.6f, Pal2.hyperBlue, e.fout());
                            });
                    }};
                }});
        }};
        separate = new UnitType("separate"){{
            constructor = UnitEntity::create;
            ammoType = new PowerAmmoType(900);
            aiController = BuilderAI::new;
            isEnemy = false;

            lowAltitude = true;
            flying = true;
            mineSpeed = 6.5f;
            mineTier = 1;
            buildSpeed = 0.5f;
            drag = 0.05f;
            speed = 3f;
            rotateSpeed = 15f;
            accel = 0.1f;
            itemCapacity = 30;
            health = 150f;
            engineOffset = 6f;
            hitSize = 8f;
            alwaysUnlocked = true;
            weapons.add(new Weapon("degradation-laser-bolt-mount"){{
                reload = 17f;
                x = 2f;
                y = 0f;
                top = false;
                ejectEffect = Fx.casing1;
                shootSound = Sounds.lasershoot;
                bullet = new LaserBoltBulletType(2.5f, 10){{
                    collidesTeam = true;
                    healPercent = 2;
                    lifetime = 60f;
                    shootEffect = DTFx.hitLaserYellow;
                    smokeEffect = Fx.shootSmallSmoke;
                    despawnEffect = DTFx.hitLaserYellow;
                    hitEffect = DTFx.hitLaserYellow;
                    buildingDamageMultiplier = 0.01f;
                    healColor = Pal.bulletYellow;
                }};
            }});
        }};
    }
}