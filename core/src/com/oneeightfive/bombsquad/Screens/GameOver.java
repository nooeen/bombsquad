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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.oneeightfive.bombsquad.Audio.BGM;
import com.oneeightfive.bombsquad.BombSquad;
import com.oneeightfive.bombsquad.Database.SQL;
import com.oneeightfive.bombsquad.Database.Score;

public class GameOver implements Screen {
    private final BombSquad game;
    private final SpriteBatch batch;

    public Stage stage;
    public OrthographicCamera camera;
    public Viewport viewport;

    private final TextureRegion bg;
    private final BitmapFont font;
    private final BitmapFont titleFont;

    private final TextField playerName;

    private final int WIDTH = 1280;
    private final int HEIGHT = 720;

    private float delayTimer = 0;

    public GameOver(BombSquad game) {
        this.game = game;
        batch = game.getBatch();

        camera = new OrthographicCamera();
        camera.position.set(BombSquad.V_WIDTH / 2f, BombSquad.V_HEIGHT / 2f, 0);
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


        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.fontColor = Color.WHITE;
        paramFont.size = 24;
        style.font = genFont.generateFont(paramFont);
        playerName = new TextField("...", style);
        playerName.setPosition(WIDTH / 2f - 100f, HEIGHT / 3f + 75f);
        playerName.setCursorPosition(3);
        stage.addActor(playerName);
        Gdx.input.setInputProcessor(stage);
        stage.setKeyboardFocus(playerName);

        genFont.dispose();

        BGM bgm = new BGM();
        bgm.playMenu();
    }

    public void handleInput(float delta) {
        delayTimer += delta;
        if (delayTimer >= 0.15) {
            if (Gdx.input.isKeyPressed(Input.Keys.ENTER)
                    || Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
                (new SQL()).addRecord(playerName.getText(), Score.current);
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
        batch.draw(bg, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        titleFont.draw(batch, "Game Over!!!", WIDTH / 2f - 130f, HEIGHT / 3f + 240f);
        font.draw(batch, "Press ESCAPE or ENTER to return", WIDTH / 2f - 190f, HEIGHT / 3f + 180f);
        font.draw(batch, "Score: " + Score.current, WIDTH / 2f - 190f, HEIGHT / 3f + 140f);
        font.draw(batch, "Name: ", WIDTH / 2f - 190f, HEIGHT / 3f + 100f);
        batch.end();

        stage.draw();
        stage.act();
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

    }
}
