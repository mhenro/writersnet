package com.writersnets.utils;

import com.writersnets.models.entities.security.CustomAuditEntity;
import com.writersnets.models.security.AuthUser;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class CustomRevisionListener implements RevisionListener {
    @Override
    public void newRevision(Object revisionEntity) {
        if (revisionEntity instanceof CustomAuditEntity) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                Object princinal = auth.getPrincipal();
                if (princinal instanceof AuthUser) {
                    ((CustomAuditEntity) revisionEntity).setUserId(((AuthUser) princinal).getUsername());
                }
            }
        }
    }
}
