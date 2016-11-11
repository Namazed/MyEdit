package com.namazed.myedit.main_screen;

enum IdMenu {
    CLEAR(0),
    OPEN(1),
    SAVE(2);

    private final int id;

    IdMenu(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
