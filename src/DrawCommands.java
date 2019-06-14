public enum DrawCommands {
    PLOT("PLOT"),
    LINE("LINE"),
    RECTANGLE("RECTANGLE"),
    ELLIPSE("ELLIPSE"),
    POLYGON("POLYGON"),
    PEN("PEN"),
    FILL("FILL");

    private String drawCmd;

    DrawCommands(String drawCmd) {
        this.drawCmd = drawCmd;
    }
}
