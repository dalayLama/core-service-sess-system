package com.sess.core.running;

import com.sess.core.components.message.MessageId;
import com.sess.core.components.message.MessageService;
import com.sess.core.components.validator.Validator;
import com.sess.core.dao.repositories.RunningTypeJpaRepository;
import com.sess.core.exceptions.*;
import com.sess.core.running.exceptions.RunningTypeNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class JpaRunningTypeService implements RunningTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(JpaRunningTypeService.class);

    private final RunningTypeJpaRepository repository;

    private final Validator validator;

    private final MessageService messageService;

    public JpaRunningTypeService(
            RunningTypeJpaRepository repository,
            Validator validator,
            MessageService messageService) {
        this.repository = repository;
        this.validator = validator;
        this.messageService = messageService;
    }

    @Override
    @Transactional
    public RunningType create(RunningType runningType) throws SaveException {
        if (Objects.nonNull(runningType.getId())) {
            ErrorMessage erMsg = messageService.sayError(MessageId.NOT_NULLABLE_RUNNING_TYPE_ID);
            throw new NotNullableId(erMsg);
        }
        validate(runningType);
        return repository.save(runningType);
    }

    @Override
    public void update(RunningType runningType) throws SaveException {
        repository.findById(runningType.getId()).ifPresentOrElse(
                rt -> {
                    rt.setCaption(runningType.getCaption());
                    repository.saveAndFlush(rt);
                },
                () -> {
                    ErrorMessage errMsg = messageService.sayError(MessageId.RUNNING_TYPE_NOT_FOUND, runningType.getId());
                    throw new RunningTypeNotFoundException(errMsg);
                }
        );
    }

    @Override
    public void remove(long id) throws DeleteException {
        repository.findById(id).ifPresentOrElse(
                rt -> {
                    rt.setDeleted(true);
                    repository.saveAndFlush(rt);
                },
                () -> LOG.warn("running type with id {} wasn't found", id)
        );
    }

    @Override
    public List<RunningType> getAll(long groupId) {
        return repository.findAllByGroupIdAndDeletedFalse(groupId);
    }

    private void validate(RunningType runningType) throws ValidationException {
        Set<String> result = validator.validate(runningType);
        if (Objects.isNull(runningType.getId())) {
            if (repository.exists(runningType.getGroup().getId(), runningType.getCaption())) {
                ErrorMessage erMsg = messageService.sayError(
                        MessageId.RUNNING_TYPE_CAPTION_ALREADY_EXISTS_IN_GROUP, runningType.getCaption(),
                        runningType.getGroup().getId());
                result.add(erMsg.getMessage());
            } else if (repository.exists(runningType.getGroup().getId(), runningType.getCaption(), runningType.getId())) {
                ErrorMessage erMsg = messageService.sayError(
                        MessageId.RUNNING_TYPE_CAPTION_ALREADY_EXISTS_IN_GROUP, runningType.getCaption(),
                        runningType.getGroup().getId());
                result.add(erMsg.getMessage());
            }
        }
        if (!result.isEmpty()) {
            throw new ValidationException(result);
        }
    }

}
