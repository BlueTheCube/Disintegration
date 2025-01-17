package disintegration;

import arc.Events;
import arc.struct.Seq;
import disintegration.content.*;
import disintegration.core.ExportHandler;
import disintegration.core.SpaceStationReader;
import disintegration.entities.DTGroups;
import disintegration.gen.entities.EntityRegistry;
import disintegration.graphics.DTShaders;
import disintegration.ui.DTUI;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.mod.Mod;
import rhino.ImporterTopLevel;
import rhino.NativeJavaPackage;

import java.io.IOException;

import static arc.Core.app;

public class DisintegrationJavaMod extends Mod{
    public DisintegrationJavaMod(){

        DTVars.init();
        DTGroups.init();
        app.addListener(DTVars.spaceStationReader = new SpaceStationReader());
        app.addListener(DTVars.exportHandler = new ExportHandler());
        DTVars.exportHandler.init();
        Events.run(EventType.Trigger.update, DTGroups::update);
        /*Events.run(EventType.Trigger.draw, DTVars.renderer3D.models::clear);
        Events.run(EventType.Trigger.postDraw, () -> {
            DTVars.renderer3D.cam.position.set(Core.camera.position, Core.camera.height);
            DTVars.renderer3D.render();
        });*/
    }

    @Override
    public void init(){
        DTPlanets.init();
        app.addListener(DTVars.DTUI = new DTUI());
        Vars.content.planets().each(p -> p.parent == p.solarSystem, p -> {
            p.orbitRadius *= 0.5;
            p.orbitRadius += p.solarSystem.radius * 2f;
        });
        Vars.content.planets().each(p -> p == p.solarSystem, p -> {
            p.radius *= 2f;
            p.reloadMesh();
            p.clipRadius *= 2f;
        });
        DTPlanets.luna.orbitRadius *= 0.5f;
        try {
            DTVars.spaceStationReader.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void loadContent() {
        EntityRegistry.register();
        DTShaders.init();
        DTItems.load();
        DTLiquids.load();
        DTStatusEffects.load();
        DTBullets.load();
        DTUnitTypes.load();
        DTBlocks.load();
        DTLoadouts.load();
        DTPlanets.load();
        DTSectorPresets.load();
    }
}
