package model;

import exceptions.NotOnMenuException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

public class Menu extends Observable {
    private Loader loader;
    private HashMap<String, Double> menu;

    public Menu() {
        loader = new Loader();
        menu = loader.loadMenu("menu");
    }

    public double getPrice(String menuItem) throws NotOnMenuException {
        if (!getMenuItems().contains(menuItem)) {
            throw new NotOnMenuException();
        } else {
            return menu.getOrDefault(menuItem, 0.0);
        }
    }

    public void addItem(String name, Double price) {
        menu.put(name, price);
        Item item = new Item(name, price);
        setChanged();
        notifyObservers(item);
    }

    public List<String> getMenuItems() {
        return new ArrayList<>(menu.keySet());
    }

    public HashMap<String, Double> getMenu() {
        return menu;
    }

    public void save() {
        loader.save(menu,"menu");
    }
}
