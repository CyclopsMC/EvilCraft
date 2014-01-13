package evilcraft.api.config;

public enum ElementTypeCategory {
    
    ITEM("item"),
    BLOCK("block"),
    LIQUID("liquid"),
    ENTITY("entity"),
    GENERAL("general"),
    OREGENERATION("oregeneration"),
    ENCHANTMENT("enchantment"),
    MOB("mob"),
    
    CORE("core");
    
    private String name;
    
    private ElementTypeCategory(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
