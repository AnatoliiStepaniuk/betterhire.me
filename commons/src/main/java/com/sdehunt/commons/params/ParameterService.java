package com.sdehunt.commons.params;

/**
 * Manages stored parameters
 */
public interface ParameterService {

    /**
     * Returns parameter value by name
     */
    String get(String param);

}
