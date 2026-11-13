package main;

import model.Model;
import presenter.Presenter;
import view.View;

public class Main {
    public static void main(String[] args) {
        // 1. Model: load the 4 sheets once into the pool
        Model model = new Model();
        model.loadSheet("gold", "/sprites/gold.png", 2, 2, 4);
        model.loadSheet("npc1", "/sprites/npc1.png", 2, 2, 4);
        model.loadSheet("npc5", "/sprites/npc5.png", 4, 1, 4);
        model.loadSheet("npc6", "/sprites/npc6.png", 3, 2, 4);   // last row incomplete

        // 2. Ten animations in a 5 x 2 grid, all sharing those 4 sheets
        String[] sheets = { "gold", "npc1", "npc5", "npc6" };
        for (int i = 0; i < 10; i++) {
            int x = 30 + (i % 5) * 160;
            int y = 40 + (i / 5) * 210;
            model.addAnimation(sheets[i % 4], x, y, 4 + i, 4);
        }

        // 3. View and presenter, then start
        View view = new View();
        Presenter presenter = new Presenter(model, view);
        presenter.start();
    }
}
