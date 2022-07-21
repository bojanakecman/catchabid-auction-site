package at.ac.ase.util.exceptions;

import org.springframework.lang.NonNull;

public class ObjectNotFoundException extends RuntimeException {

    @NonNull
    private Class<?> expectedObjectClass;

    @NonNull
    private String expectedObjectId;

    public Class<?> getExpectedObjectClass() {
        return expectedObjectClass;
    }

    public String getExpectedObjectId() {
        return expectedObjectId;
    }
}
