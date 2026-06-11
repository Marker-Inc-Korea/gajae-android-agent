package com.gajae.androidagent.core;

public final class InstructionParseResult {
    public final boolean ok;
    public final AppCommand command;
    public final String error;

    public InstructionParseResult(boolean ok, AppCommand command, String error) {
        this.ok = ok;
        this.command = command;
        this.error = TextUtil.clean(error);
    }

    public static InstructionParseResult ok(AppCommand command) {
        return new InstructionParseResult(true, command, "");
    }

    public static InstructionParseResult fail(String error) {
        return new InstructionParseResult(false, new AppCommand("", ""), error);
    }
}
