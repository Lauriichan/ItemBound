package me.lauriichan.minecraft.itembound.data.config;

import java.io.File;

public interface IConfiguration<C, T> extends ISection<C, T> {

    void load(File file) throws Throwable;

    void save(File file) throws Throwable;

}
