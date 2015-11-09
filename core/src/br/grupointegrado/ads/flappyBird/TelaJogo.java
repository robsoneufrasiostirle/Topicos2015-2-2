package br.grupointegrado.ads.flappyBird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;


/**
 * Created by Robson on 28/09/2015.
 */

public class TelaJogo extends TelaBase {

    private OrthographicCamera camera; // camera do jogo
    private World mundo; // representa o mundo do Box2D

    private Box2DDebugRenderer debug; // desenha o mundo na tela para ajudar no desenvolvimento

    private Body chao; //corpo do chao
    private Passaro passaro;

    private Array<Obstaculo> obstaculos = new Array<Obstaculo>();

    private BitmapFont fontePontuacao;
    private  int pontuacao = 0;
    private Stage palcoInformacoes;
    private Label lbPontuacao;
    private ImageButton btnplay;
    private ImageButton btnGameOver;
    private OrthographicCamera cameraInfo;

    private boolean gameOver = false;


    public TelaJogo(MainGame game) {
        super(game);
    }

    @Override
    public void show() {

        camera = new OrthographicCamera(Gdx.graphics.getWidth() / Util.escala, Gdx.graphics.getHeight() / Util.escala);
        cameraInfo = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        debug = new Box2DDebugRenderer();
        mundo = new World(new Vector2(0, -9.8f), false);

        mundo.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                detectarColisao(contact.getFixtureA(), contact.getFixtureB());
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });

        initChao();
        initPassaro();
        initFontes();
        initInformacoes();

    }

    private void detectarColisao(Fixture fixtureA, Fixture fixtureB) {

        if ("Passaro".equals(fixtureA.getUserData()) || ("Passaro".equals(fixtureB.getUserData()))){
            // game over

            gameOver = true;

        }
    }

    private void initFontes() {
        FreeTypeFontGenerator.FreeTypeFontParameter fonteParam = new FreeTypeFontGenerator.FreeTypeFontParameter();

        fonteParam.size = 56;
        fonteParam.color = Color.WHITE;
        fonteParam.shadowColor = Color.BLACK;
        fonteParam.shadowOffsetY = 4;
        fonteParam.shadowOffsetX = 4;

        FreeTypeFontGenerator gerador = new FreeTypeFontGenerator(Gdx.files.internal("fonts/roboto.ttf"));

        fontePontuacao = gerador.generateFont(fonteParam);
        gerador.dispose();
    }

    private void initInformacoes() {

        palcoInformacoes = new Stage(new FillViewport(cameraInfo.viewportWidth, cameraInfo.viewportHeight, cameraInfo));
        Gdx.input.setInputProcessor(palcoInformacoes);

        Label.LabelStyle estilo = new Label.LabelStyle();
        estilo.font = fontePontuacao;
        lbPontuacao = new Label("0", estilo);
        palcoInformacoes.addActor(lbPontuacao);
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
        palcoInformacoes.draw();

    }

    private void atualizar(float delta) {

        palcoInformacoes.act(delta);
        passaro.atuaizar(delta);

        mundo.step(1f / 60f, 6, 2);

        atualizaInformacoes();

        atualizarObstaculos();
        atualizarCamera();
        atualizarChao();

        if(pulando){
            passaro.pular();
        }

    }

    private void atualizaInformacoes() {
        lbPontuacao.setText(pontuacao + "");

        lbPontuacao.setPosition(cameraInfo.viewportWidth / 2 - lbPontuacao.getPrefWidth() / 2,
                cameraInfo.viewportHeight - lbPontuacao.getPrefHeight());

    }

    private void atualizarObstaculos() {
        // enquanto a lista tiver menos de 4 , cria os obstaculos
        while (obstaculos.size < 4){
            Obstaculo ultimo = null;

            if (obstaculos.size > 0){
                 ultimo = obstaculos.peek(); // recupera o ultimo item da lista
            }

            Obstaculo o = new Obstaculo(mundo, camera, ultimo);
            obstaculos.add(o);
        }

        // verifica se os obstaculos sairam da tela para remove-los
        for (Obstaculo o : obstaculos){
            float inicioCamera = passaro.getCorpo().getPosition().x -
                    (camera.viewportWidth / 2 / Util.pixel_metro) - o.getLargura();
            // verifica se o obstaculo saiu da tela
            if (inicioCamera > o.getPosX()){
                o.remover();
                obstaculos.removeValue(o, true);
            }else if (!o.isPassou() && o.getPosX() < passaro.getCorpo().getPosition().x) {
                o.setPassou(true);
                // calcular a pontuação e reproduzir som
                pontuacao ++;

            }
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
        cameraInfo.setToOrtho(false, width, height);
        cameraInfo.update();
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
        palcoInformacoes.dispose();
        fontePontuacao.dispose();
    }
}
