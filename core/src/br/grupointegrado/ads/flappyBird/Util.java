package br.grupointegrado.ads.flappyBird;

/**
 * Created by Robson on 11/10/2015.
 */

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class Util {
    public static final float escala = 2;
    public static final float pixel_metro = 32;
    public static final float altura_chao = 2.5f; // altura do ch�o em metros

    public static Body criarCorpo(World mundo, BodyDef.BodyType tipo, float x, float y) {
        BodyDef definicao = new BodyDef();
        definicao.type = tipo;
        definicao.position.set(x, y);
        definicao.fixedRotation = true;
        Body corpo = mundo.createBody(definicao);
        return corpo;
    }

    /**
     * Cria uma forma para o corpo
     *
     * @param corpo
     * @param shape Forma geometrica do corpo
     * @param nome  nome utilizado para identificar na colis�o
     * @return
     */
    public static Fixture criarForma(Body corpo, Shape shape, String nome) {

        FixtureDef definicao = new FixtureDef();
        definicao.density = 1; // densidade do corpo
        definicao.friction = 0.03f; // fric��o/atrito entre um corpo e outro
        definicao.restitution = 0.3f; // elasticidadedo corpo
        definicao.shape = shape;
        Fixture forma = corpo.createFixture(definicao);
        forma.setUserData(nome);// identifica��o da forma
        return forma;

    }

}