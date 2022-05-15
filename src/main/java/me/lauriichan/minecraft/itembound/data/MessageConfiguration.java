package me.lauriichan.minecraft.itembound.data;

import java.io.File;

import me.lauriichan.minecraft.itembound.data.config.yaml.YamlConfig;
import me.lauriichan.minecraft.itembound.data.io.ConfigReloadable;
import me.lauriichan.minecraft.itembound.data.message.Message;

public final class MessageConfiguration extends ConfigReloadable<YamlConfig> {

    public MessageConfiguration(final File file) {
        super(YamlConfig.class, file);
    }

    @Override
    protected void onConfigLoad() throws Throwable {
        Message[] messages = Message.values();
        for (Message message : messages) {
            String value = config.getValue(message.getId(), String.class);
            if (value == null) {
                config.setValue(message.getId(), message.getFallback());
                message.setTranslation(null);
                continue;
            }
            message.setTranslation(value);
        }
    }

}
