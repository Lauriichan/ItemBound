package me.lauriichan.minecraft.itembound.data;

import java.io.File;

import me.lauriichan.minecraft.itembound.data.config.yaml.YamlConfig;
import me.lauriichan.minecraft.itembound.data.io.ConfigReloadable;
import me.lauriichan.minecraft.itembound.data.io.Reloadable;

public final class MainConfiguration extends ConfigReloadable<YamlConfig> {

    public MainConfiguration(final File file) {
        super(YamlConfig.class, file);
    }

    @Override
    protected void onConfigLoad() throws Throwable {
        Reloadable.DEBUG = config.getValueOrDefault("debug", false);
    }

    @Override
    protected void onConfigSave() throws Throwable {
        config.setValue("debug", Reloadable.DEBUG);
    }

}
