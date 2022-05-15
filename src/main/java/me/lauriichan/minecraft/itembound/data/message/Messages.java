package me.lauriichan.minecraft.itembound.data.message;

public final class Messages {

    private Messages() {
        throw new UnsupportedOperationException("Constant class");
    }

    public static final Message PREFIX;

    public static final Message COMMAND_BIND_SUCCESS;
    public static final Message COMMAND_BIND_ONLY_PLAYER;
    public static final Message COMMAND_BIND_NOT_PERMITTED;
    public static final Message COMMAND_BIND_NO_ACTION;
    public static final Message COMMAND_BIND_NO_ITEM;

    public static final Message DATA_LOAD_START;
    public static final Message DATA_LOAD_SUCCESS;
    public static final Message DATA_LOAD_FAILED;
    public static final Message DATA_SAVE_START;
    public static final Message DATA_SAVE_SUCCESS;
    public static final Message DATA_SAVE_FAILED;

    static {
        PREFIX = Message.register("prefix", "&dItem&5Bound &8\u00BB");

        COMMAND_BIND_SUCCESS = Message.register("command.bind.success",
            "$#prefix &aSuccessfully &7bound the specified action to the item in your hand!");
        COMMAND_BIND_ONLY_PLAYER = Message.register("command.bind.only.player",
            "$#prefix &7This command can only be executed by a player!");
        COMMAND_BIND_NOT_PERMITTED = Message.register("command.bind.not.permitted",
            "$#prefix &7You are lacking the permission &8'&citembound.use&8'&7 to use this command!");
        COMMAND_BIND_NO_ACTION = Message.register("command.bind.no.action", "$#prefix &7You have to specify an action!");
        COMMAND_BIND_NO_ITEM = Message.register("command.bind.no.item", "$#prefix &7You need to hold an item in your main hand!");

        DATA_LOAD_START = Message.register("data.load.start", "Trying to load data from '$name'...");
        DATA_LOAD_SUCCESS = Message.register("data.load.success", "Data was successfully loaded from $name!");
        DATA_LOAD_FAILED = Message.register("data.load.failed", "Something went wrong while loading data from $name!");
        DATA_SAVE_START = Message.register("data.save.start", "Trying to save data to '$name'...");
        DATA_SAVE_SUCCESS = Message.register("data.save.success", "Data was successfully saved to $name!");
        DATA_SAVE_FAILED = Message.register("data.save.failed", "Something went wrong while saving data to $name!");
    }

}
