package missingpets.enums;


public enum Role {
    USER("User"), ADMIN("Admin");
    
    private String displayName;
    
    private Role (String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}

