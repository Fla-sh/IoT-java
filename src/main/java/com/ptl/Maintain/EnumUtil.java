package com.ptl.Maintain;

/**
 * Class containing some utilities for operations on Enumerations
 */
public class EnumUtil {

    /**
     * Determine if enumeration has an element in in or not
     * @param enumerator the enumeration to check
     * @param value the element to check
     * @param <T> type of elements in enumeration
     * @return Boolean value true if enumeration has an element in it
     */
    public static <T extends Enum<T>> boolean enumContains(Class<T> enumerator, String value)
    {
        for (T c : enumerator.getEnumConstants()) {
            if (c.name().equals(value)) {
                return true;
            }
        }
        return false;
    }

}
