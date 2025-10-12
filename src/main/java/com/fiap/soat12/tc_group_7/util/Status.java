package com.fiap.soat12.tc_group_7.util;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.ServiceOrderEntity;
import com.fiap.soat12.tc_group_7.exception.InvalidTransitionException;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public enum Status {

    OPENED {
        @Override
        public void diagnose(ServiceOrderEntity order) {
            order.setStatus(IN_DIAGNOSIS);
            order.setUpdatedAt(new Date());
        }
    },
    IN_DIAGNOSIS {
        @Override
        public void waitForApproval(ServiceOrderEntity order) {
            order.setStatus(WAITING_FOR_APPROVAL);
            order.setUpdatedAt(new Date());
        }
    },
    WAITING_FOR_APPROVAL {
        @Override
        public void approve(ServiceOrderEntity order) {
            order.setStatus(APPROVED);
            order.setUpdatedAt(new Date());
        }

        @Override
        public void reject(ServiceOrderEntity order) {
            order.setStatus(REJECTED);
            order.setUpdatedAt(new Date());
        }
    },
    APPROVED {
        @Override
        public void waitForStock(ServiceOrderEntity order) {
            order.setStatus(WAITING_ON_STOCK);
            order.setUpdatedAt(new Date());
        }

        @Override
        public void execute(ServiceOrderEntity order) {
            order.setStatus(IN_EXECUTION);
            order.setUpdatedAt(new Date());
        }
    },
    REJECTED {
        @Override
        public void finish(ServiceOrderEntity order) {
            order.setStatus(FINISHED);
            order.setFinishedAt(new Date());
        }
    },
    WAITING_ON_STOCK {
        @Override
        public void execute(ServiceOrderEntity order) {
            order.setStatus(IN_EXECUTION);
            order.setUpdatedAt(new Date());
        }
    },
    IN_EXECUTION {
        @Override
        public void finish(ServiceOrderEntity order) {
            order.setStatus(FINISHED);
            order.setFinishedAt(new Date());
        }
    },
    FINISHED {
        @Override
        public void deliver(ServiceOrderEntity order) {
            order.setStatus(DELIVERED);
        }
    },
    DELIVERED,
    CANCELED;

    public static final String MSG_ERROR = "Não é possível mover para o status %s, a partir do status %s.";

    public void diagnose(ServiceOrderEntity order) throws InvalidTransitionException {
        throw new InvalidTransitionException(String.format(MSG_ERROR, IN_DIAGNOSIS.name(), this.name()));
    }

    public void waitForApproval(ServiceOrderEntity order) throws InvalidTransitionException {
        throw new InvalidTransitionException(String.format(MSG_ERROR, WAITING_FOR_APPROVAL.name(), this.name()));
    }

    public void approve(ServiceOrderEntity order) throws InvalidTransitionException {
        throw new InvalidTransitionException(String.format(MSG_ERROR, APPROVED.name(), this.name()));
    }

    public void reject(ServiceOrderEntity order) throws InvalidTransitionException {
        throw new InvalidTransitionException(String.format(MSG_ERROR, REJECTED.name(), this.name()));
    }

    public void waitForStock(ServiceOrderEntity order) throws InvalidTransitionException {
        throw new InvalidTransitionException(String.format(MSG_ERROR, WAITING_ON_STOCK.name(), this.name()));
    }

    public void execute(ServiceOrderEntity order) throws InvalidTransitionException {
        throw new InvalidTransitionException(String.format(MSG_ERROR, IN_EXECUTION.name(), this.name()));
    }

    public void finish(ServiceOrderEntity order) throws InvalidTransitionException {
        throw new InvalidTransitionException(String.format(MSG_ERROR, FINISHED.name(), this.name()));
    }

    public void deliver(ServiceOrderEntity order) throws InvalidTransitionException {
        throw new InvalidTransitionException(String.format(MSG_ERROR, DELIVERED.name(), this.name()));
    }

    public static List<Status> getStatusesForPendingOrders() {
        return Arrays.asList(Status.OPENED, Status.IN_DIAGNOSIS, Status.APPROVED, Status.IN_EXECUTION);
    }
}
