package com.sess.core.api.rest.handlers;

import com.sess.core.api.rest.handlers.exceptions.HttpStatusOperationException;
import com.sess.core.dto.DTOUser;

/**
 * Handler which adapt DTO to entity classes and delegate operations to concrete services
 */
public interface UserRestApiHandler {

    DTOUser register(DTOUser user) throws HttpStatusOperationException;

}
