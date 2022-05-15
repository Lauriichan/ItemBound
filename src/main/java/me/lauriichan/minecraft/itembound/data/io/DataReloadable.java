package me.lauriichan.minecraft.itembound.data.io;

import java.io.File;
import java.util.logging.Level;

import me.lauriichan.minecraft.itembound.data.message.Messages;
import me.lauriichan.minecraft.itembound.util.Tuple;

public abstract class DataReloadable extends Reloadable {

    private final Tuple<String, Object> name;

    public DataReloadable(File file) {
        super(file);
        this.name = Tuple.of("name", file.getName());
    }

    public DataReloadable(File file, boolean saveOnExit) {
        super(file, saveOnExit);
        this.name = Tuple.of("name", file.getName());
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void load() {
        Messages.DATA_LOAD_START.log(Level.INFO, name);
        if (!file.exists()) {
            Messages.DATA_LOAD_SUCCESS.log(Level.INFO, name);
            return;
        }
        try {
            onLoad();
            Messages.DATA_LOAD_SUCCESS.log(Level.INFO, name);
        } catch (Throwable exp) {
            Messages.DATA_LOAD_FAILED.log(Level.WARNING, exp, name);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void save() {
        Messages.DATA_SAVE_START.log(Level.INFO, name);
        try {
            if (!file.exists()) {
                if (file.getParentFile() != null) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
            onSave();
            Messages.DATA_SAVE_SUCCESS.log(Level.INFO, name);
        } catch (Throwable exp) {
            Messages.DATA_SAVE_FAILED.log(Level.WARNING, exp, name);
        }
    }

    protected void onLoad() throws Throwable {}

    protected void onSave() throws Throwable {}

}
