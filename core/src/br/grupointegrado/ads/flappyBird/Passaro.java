package br.grupointegrado.ads.flappyBird;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.sun.media.sound.UlawCodec;

/**
 * Created by Robson on 06/10/2015.
 */
public class Passaro {

    private final World mundo;
    private final OrthographicCamera camera;
    private final Texture[] texturas;
    private Body corpo;

    public Passaro(World mundo, OrthographicCamera camera, Texture[] texturas) {

        this.mundo = mundo;
        this.camera = camera;
        this.texturas = texturas;

        initCorpo();
    }

    private void initCorpo() {

        float x = (camera.viewportWidth / 2) / Util.pixel_metro;
        float y = (camera.viewportHeight / 2) / Util.pixel_metro;

        corpo = Util.criarCorpo(mundo, BodyDef.BodyType.DynamicBody, x, y);

        CircleShape shape = new CircleShape();
        shape.setRadius(18 / Util.pixel_metro);
        Fixture forma = Util.criarForma(corpo, shape, "Passaro");
        shape.dispose();
    }
}