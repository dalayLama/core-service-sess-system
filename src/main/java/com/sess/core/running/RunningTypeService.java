package com.sess.core.running;

import com.sess.core.exceptions.DeleteException;
import com.sess.core.exceptions.SaveException;

import java.util.List;

public interface RunningTypeService {

    RunningType create(RunningType runningType) throws SaveException;

    void update(RunningType runningType) throws SaveException;

    void remove(long id) throws DeleteException;

    List<RunningType> getAllByGroup(long groupId);

}
