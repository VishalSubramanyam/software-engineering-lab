package assignmentJava.entities;

import java.util.Objects;

/**
 * Abstract entity class from which all other entities are derived
 */
abstract class Entity {
    static private int count = 0;

    /**
     * The unique identification number of this entity that distinguishes it
     * from other entities.
     */
    private Integer uniqueId;

    /**
     * The name of this entity (may not be unique).
     */
    private String name;

    protected Entity(String name) {
        this.setUniqueId(count++);
        this.setName(name);
    }

    /**
     * @return the unique id of this entity
     * @see assignmentJava.entities.Entity#uniqueId
     */
    public Integer getUniqueId() {
        return uniqueId;
    }

    /**
     * @param uniqueId a unique identification number for this entity
     * @see assignmentJava.entities.Entity#uniqueId
     */
    protected void setUniqueId(Integer uniqueId) {
        this.uniqueId = uniqueId;
    }

    /**
     * @return the name of this entity
     * @see assignmentJava.entities.Entity#name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name for this entity
     * @see assignmentJava.entities.Entity#name
     */
    protected void setName(String name) {
        this.name = name;
    }

    /**
     * Two entities are equal, by definition, if and only if their unique IDs
     * are equal.
     * @param obj The entity to be tested for equality with.
     * @return True, if the two entities have the same unique ID, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Entity)) return false;
        else {
            Entity entity = (Entity) obj;
            return Objects.equals(this.getUniqueId(), entity.getUniqueId());
        }
    }
}
