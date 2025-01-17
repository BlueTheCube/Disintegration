package disintegration.graphics;

import arc.Core;
import arc.Events;
import arc.files.Fi;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.gl.FrameBuffer;
import arc.graphics.gl.Shader;
import arc.struct.FloatSeq;
import disintegration.DTVars;
import mindustry.game.EventType;

import static mindustry.Vars.state;

public class DTShaders {
    public static BlackHoleShader blackHole;

    public static void init(){
        blackHole = new BlackHoleShader();
    }
    public static class BlackHoleShader extends Shader {
        static final int max = 32;
        static final int size = 4;

        //x y radius force
        protected FloatSeq data = new FloatSeq();
        protected FloatSeq uniforms = new FloatSeq();
        protected boolean hadAny = false;
        protected FrameBuffer buffer = new FrameBuffer();
        public BlackHoleShader() {
            super(Core.files.internal("shaders/screenspace.vert"), getDTShaderFi("blackHole.frag"));
            Events.run(EventType.Trigger.update, () -> {
                if(state.isMenu()){
                    data.size = 0;
                }
            });

            Events.run(EventType.Trigger.preDraw, () -> {
                hadAny = data.size > 0;
                data.clear();

                if(hadAny){
                    buffer.resize(Core.graphics.getWidth(), Core.graphics.getHeight());
                    buffer.begin(Color.clear);
                }
            });

            Events.run(EventType.Trigger.postDraw, () -> {
                if(hadAny){
                    buffer.end();
                    Draw.blend(Blending.disabled);
                    buffer.blit(this);
                    Draw.blend();
                }
            });
        }
        @Override
        public void apply(){
            int count = data.size / size;

            setUniformi("u_blackhole_count", count);
            if(count > 0){
                setUniformf("u_resolution", Core.camera.width, Core.camera.height);
                setUniformf("u_campos", Core.camera.position.x - Core.camera.width/2f, Core.camera.position.y - Core.camera.height/2f);

                uniforms.clear();

                var items = data.items;
                for(int i = 0; i < count; i++){
                    int offset = i * size;

                    uniforms.add(
                            items[offset], items[offset + 1], //xy
                            items[offset + 2], //radius
                            items[offset + 3] //force
                    );
                }

                setUniform4fv("u_blackholes", uniforms.items, 0, uniforms.size);
            }
        }

        public void add(float x, float y, float radius){
            add(x, y, radius, radius);
        }

        public void add(float x, float y, float radius, float force){
            //replace first entry
            if(data.size / size >= max){
                var items = data.items;
                items[0] = x;
                items[1] = y;
                items[2] = radius;
                items[3] = force;
            }else{
                data.addAll(x, y, radius, force);
            }
        }
    }

    public static Fi getDTShaderFi(String file){
        return DTVars.modFile.child("shaders").child(file);
    }
}