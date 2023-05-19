package com.example.banksys.presentationlayer.utils.validator;

import jakarta.validation.GroupSequence;

@GroupSequence({CheckItFirst.class, ThenCheckIt.class})
public interface OrderedChecks {
}
