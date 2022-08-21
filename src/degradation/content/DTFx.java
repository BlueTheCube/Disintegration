package degradation.content;

import arc.graphics.Color;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import degradation.graphics.Pal2;
import mindustry.entities.Effect;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;

import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Lines.stroke;
import static arc.math.Angles.randLenVectors;

public class DTFx {
    public static final Effect
    electricResonated = new Effect(40f, e -> {
        color(Pal2.hyperBlue);
        randLenVectors(e.id, 2, 1f + e.fin() * 2f, (x, y) -> {
            Fill.square(e.x + x, e.y + y, e.fslope() * 1.1f, 45f);
        });
    }),
    hitLaserYellow = new Effect(8, e -> {
        color(Color.white, Pal.bulletYellow, e.fin());
        stroke(0.5f + e.fout());
        Lines.circle(e.x, e.y, e.fin() * 5f);

        Drawf.light(e.x, e.y, 23f, Pal.bulletYellow, e.fout() * 0.7f);
    })
            ;
}