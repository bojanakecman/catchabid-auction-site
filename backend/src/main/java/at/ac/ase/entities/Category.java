package at.ac.ase.entities;


public enum Category {
    JEWELRY("JEWELRY"),
    ELECTRONICS("ELECTRONICS"),
    ART("ART"),
    CARS("CARS"),
    ANTIQUES("ANTIQUES"),
    TRAVEL("TRAVEL"),
    FURNITURE("FURNITURE"),
    MUSIC("MUSIC"),
    SPORT("SPORT"),
    OTHER("OTHER");

    private String name;

    Category(String name) {
        this.name = name;
    }

    public Category getCategoryByName(String name) {
        for (Category e : values()) {
            if (e.name.equals(name)) {
                return e;
            }
        }
        return null;
    }
}
