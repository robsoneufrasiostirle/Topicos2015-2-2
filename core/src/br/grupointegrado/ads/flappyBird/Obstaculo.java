package br.grupointegrado.ads.flappyBird;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Robson on 26/10/2015.
 */
public class Obstaculo {

    private World mundo;
    private OrthographicCamera camera;
    private Body corpoCima, corpoBaixo;
    private float posX;
    private float posYCima, posYBaixo;
    private boolean passou;

    private Obstaculo ultimmoObstaculo; // ultimo antes do atual

    private float largura, altura;

    public Obstaculo(World mundo, OrthographicCamera camera, Obstaculo ultimmoObstaculo){
        this.mundo = mundo;
        this.camera = camera;
        this.ultimmoObstaculo = ultimmoObstaculo;

        initPosicao();
        initCorpoCima();
        initCorpoBaixo();
    }

    private void initCorpoBaixo() {
        corpoBaixo = Util.criarCorpo(mundo, BodyDef.BodyType.StaticBody, posX, posYBaixo);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(largura / 2, altura/2);

        Util.criarForma(corpoBaixo, shape, "OBSTACULO_BAIXO");
        shape.dispose();
    }

    private void initCorpoCima() {
        corpoCima = Util.criarCorpo(mundo, BodyDef.BodyType.StaticBody, posX, posYCima);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(largura / 2, altura/2);

        Util.criarForma(corpoCima, shape, "OBSTACULO_CIMA");
        shape.dispose();
    }

    private void initPosicao() {
        largura = 40 / Util.pixel_metro;
        altura = camera.viewportHeight / Util.pixel_metro;

        float xInicial = largura + (camera.viewportWidth / 2 / Util.pixel_metro);
        if(ultimmoObstaculo != null){
            xInicial = ultimmoObstaculo.getPosX();
        }
        posX = xInicial + 4; // 4 � o espa�o entre os obstaculos

        // tamanho da tela dividido por 6, para encontrar a posicao Y do obstaculo
        float parcela = (altura - Util.altura_chao) / 6;

        int multiplicador = MathUtils.random(1,3); // numero aleatorio entre 1 e 3

        posYBaixo = Util.altura_chao + (parcela + multiplicador) - (altura / 2);

        posYCima = posYBaixo + altura + 2f; // 2f espa�o entre os canos
    }

    public float getPosX(){

        return this.posX;
    }

    public void remover(){
        mundo.destroyBody(corpoCima);
        mundo.destroyBody(corpoBaixo);
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getLargura() {
        return largura;
    }

    public void setLargura(float largura) {
        this.largura = largura;
    }

    public float getAltura() {
        return altura;
    }

    public void setAltura(float altura) {
        this.altura = altura;
    }

    public boolean isPassou() {
        return passou;
    }

    public void setPassou(boolean passou) {
        this.passou = passou;
    }
}
