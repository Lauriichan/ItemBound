package me.lauriichan.minecraft.itembound.data.io;

import java.io.File;

import me.lauriichan.minecraft.itembound.data.config.IConfiguration;
import me.lauriichan.minecraft.itembound.util.JavaInstance;

public abstract class ConfigReloadable<T extends IConfiguration<?, ?>> extends DataReloadable {

    protected final T config;

    public ConfigReloadable(T config, File file) {
        super(file);
        this.config = config;
    }

    public ConfigReloadable(T config, File file, boolean saveOnExit) {
        super(file, saveOnExit);
        this.config = config;
    }

    public ConfigReloadable(Class<T> clazz, File file) {
        super(file);
        this.config = JavaInstance.initialize(clazz);
    }

    public ConfigReloadable(Class<T> clazz, File file, boolean saveOnExit) {
        super(file, saveOnExit);
        this.config = JavaInstance.initialize(clazz);
    }

    @Override
    protected final void onLoad() throws Throwable {
        try {
            config.load(file);
        } catch (Throwable throwable) {
            onConfigLoad();
            throw throwable;
        }
        onConfigLoad();
    }

    protected void onConfigLoad() throws Throwable {}

    @Override
    protected final void onSave() throws Throwable {
        onConfigSave();
        config.save(file);
    }

    protected void onConfigSave() throws Throwable {}

}
