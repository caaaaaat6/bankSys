package com.example.banksys.presentationlayer.utils.validator;

import jakarta.validation.GroupSequence;

/**
 * 组织验证顺序
 */
@GroupSequence({CheckItFirst.class, ThenCheckIt.class})
public interface OrderedChecks {
}
