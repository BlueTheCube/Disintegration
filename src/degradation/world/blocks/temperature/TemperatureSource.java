package degradation.world.blocks.temperature;

import arc.graphics.g2d.TextureRegion;
import mindustry.gen.Building;
import mindustry.world.Block;

public class TemperatureSource extends Block{
    public TextureRegion bottomRegion;
    public TemperatureSource(String name) {
        super(name);

        update = true;
    }

    public class TemperatureSourceBuild extends Building implements TemperatureBlock{
        @Override
        public float temperature() {
            return 1000;
        }

        @Override
        public float conductionSpeed() {
            return 1;
        }
    }
}