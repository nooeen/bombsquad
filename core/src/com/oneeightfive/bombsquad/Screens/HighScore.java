package com.oneeightfive.bombsquad.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.oneeightfive.bombsquad.Audio.BGM;
import com.oneeightfive.bombsquad.Audio.Sounds;
import com.oneeightfive.bombsquad.BombSquad;
import com.oneeightfive.bombsquad.Database.SQL;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class HighScore implements Screen {
    private final BombSquad game;
    private final SpriteBatch batch;

    public Stage stage;
    public OrthographicCamera camera;
    public Viewport viewport;

    private final TextureRegion bg;
    private final BitmapFont font;
    private final BitmapFont titleFont;

    private final BGM bgm;
    private final Sounds sounds;

    public static Map<String,Integer> list = new LinkedHashMap<>();

    private final int WIDTH = 1280;
    private final int HEIGHT = 720;

    private float delayTimer = 0;

    public HighScore(BombSquad game) {
        this.game = game;
        batch = game.getBatch();

        camera = new OrthographicCamera();
        camera.position.set(BombSquad.V_WIDTH/2f, BombSquad.V_HEIGHT/2f, 0);
        viewport = new StretchViewport(WIDTH, HEIGHT, camera);
        stage = new Stage(viewport, batch);

        bg = new TextureRegion(new Texture("textures/highscoresbg.jpg"), 0, 0, 1024, 578);

        FreeTypeFontGenerator genFont = new FreeTypeFontGenerator(Gdx.files.internal("fonts/minecraft.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter paramFont = new FreeTypeFontGenerator.FreeTypeFontParameter();
        paramFont.size = 24;
        paramFont.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.!'()>?:";
        paramFont.borderColor = new Color(Color.BLACK);
        paramFont.borderWidth = 1;
        font = genFont.generateFont(paramFont);
        paramFont.size = 48;
        titleFont = genFont.generateFont(paramFont);
        genFont.dispose();

        bgm = new BGM();
        bgm.playInGame();
        sounds = new Sounds();

        (new SQL()).listAll();
    }

    public void list() {
        float xTitle = WIDTH / 2f - 75f;
        float yTitle = HEIGHT / 2f + 120f;

        int c = 0;

        font.setColor(Color.WHITE);

        for (Map.Entry<String, Integer> set : list.entrySet()) {
            if (c<=9) {
                font.draw(batch, set.getKey() + " " + set.getValue(), xTitle, yTitle - c * 45f);
                c++;
            }
        }
    }

    public void handleInput(float delta) {
        delayTimer += delta;
        if(delayTimer >= 0.15) {
            if (Gdx.input.isKeyPressed(Input.Keys.ENTER)
                || Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
                game.setScreen(new MainMenu(game));
                dispose();
            }
            delayTimer = 0;
        }
    }

    public void update(float delta) {
        handleInput(delta);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        batch.begin();
        batch.draw(bg, 0, 0, viewport.getWorldWidth(),viewport.getWorldHeight());
        titleFont.draw(batch, "High Scores", WIDTH / 2f - 125f,HEIGHT / 2f + 240f);
        font.draw(batch, "Press ESCAPE or ENTER to return", WIDTH / 2f - 185f,HEIGHT / 2f + 180f);
        list();
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        bgm.dispose();
        sounds.dispose();
        font.dispose();
    }
}
