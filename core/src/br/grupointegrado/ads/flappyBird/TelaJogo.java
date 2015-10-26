package br.grupointegrado.ads.flappyBird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


/**
 * Created by Robson on 28/09/2015.
 */

public class TelaJogo extends TelaBase {

    private OrthographicCamera camera; // camera do jogo
    private World mundo; // representa o mundo do Box2D

    private Box2DDebugRenderer debug; // desenha o mundo na tela para ajudar no desenvolvimento

    private Body chao; //corpo do chao
    private Passaro passaro;

    public TelaJogo(MainGame game) {
        super(game);
    }

    @Override
    public void show() {

        camera = new OrthographicCamera(Gdx.graphics.getWidth() / Util.escala, Gdx.graphics.getHeight() / Util.escala);
        debug = new Box2DDebugRenderer();
        mundo = new World(new Vector2(0, -9.8f), false);

        initChao();
        initPassaro();

        new Obstaculo(mundo, camera, null);
    }


    private void initChao() {
        chao = Util.criarCorpo(mundo, BodyDef.BodyType.StaticBody, 0, 0);


    }

    private void initPassaro() {
        passaro = new Passaro(mundo, camera, null);

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(.25f, .25f, .25f, 1);// limpa a tela e pinta a cor de fundo
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);// mantem o buffer de cores

        capturaTeclas();

        atualizar(delta);
        renderizar(delta);

        debug.render(mundo, camera.combined.cpy().scl(Util.pixel_metro));

    }


    private boolean pulando = false;

    private void capturaTeclas() {

        pulando = false;
        if(Gdx.input.justTouched()){
            pulando = true;
        }
    }

    private void renderizar(float delta) {

    }

    private void atualizar(float delta) {
        passaro.atuaizar(delta);

        mundo.step(1f / 60f, 6, 2);

        atualizarCamera();
        atualizarChao();

        if(pulando){
            passaro.pular();
        }

    }

    private void atualizarCamera() {

        camera.position.x = (passaro.getCorpo().getPosition().x + 34 * Util.pixel_metro) * Util.pixel_metro;
        camera.update();
    }


    // atualiza a posicao do chao para acompanhar o passaro
    private void atualizarChao() {

        Vector2 posicao = passaro.getCorpo().getPosition();

        chao.setTransform(posicao.x, 0, 0);

    }

    @Override
    public void resize(int width, int height) {

        camera.setToOrtho(false, width / Util.escala, height / Util.escala);
        camera.update();
        redimensionaChao();
    }

    private void redimensionaChao() {
        chao.getFixtureList().clear();
        float largura = camera.viewportWidth / Util.pixel_metro;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(largura / 2, Util.altura_chao / 2);
        Fixture forma = Util.criarForma(chao, shape, "chao");
        shape.dispose();

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        debug.dispose();
        mundo.dispose();
    }
}
