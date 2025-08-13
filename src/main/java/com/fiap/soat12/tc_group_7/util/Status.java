package com.fiap.soat12.tc_group_7.util;

import com.fiap.soat12.tc_group_7.entity.ServiceOrder;
import com.fiap.soat12.tc_group_7.exception.InvalidTransitionException;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public enum Status {

    OPENED {
        @Override
        public void diagnose(ServiceOrder order) {
            order.setStatus(IN_DIAGNOSIS);
            order.setUpdatedAt(new Date());
        }
    },
    IN_DIAGNOSIS {
        @Override
        public void waitForApproval(ServiceOrder order) {
            order.setStatus(WAITING_FOR_APPROVAL);
            order.setUpdatedAt(new Date());
        }
    },
    WAITING_FOR_APPROVAL {
        @Override
        public void approve(ServiceOrder order) {
            order.setStatus(APPROVED);
            order.setUpdatedAt(new Date());
        }

        @Override
        public void reject(ServiceOrder order) {
            order.setStatus(REJECTED);
            order.setUpdatedAt(new Date());
        }
    },
    APPROVED {
        @Override
        public void waitForStock(ServiceOrder order) {
            order.setStatus(WAITING_ON_STOCK);
            order.setUpdatedAt(new Date());
        }

        @Override
        public void execute(ServiceOrder order) {
            order.setStatus(IN_EXECUTION);
            order.setUpdatedAt(new Date());
        }
    },
    REJECTED {
        @Override
        public void finish(ServiceOrder order) {
            order.setStatus(FINISHED);
            order.setFinishedAt(new Date());
        }
    },
    WAITING_ON_STOCK {
        @Override
        public void execute(ServiceOrder order) {
            order.setStatus(IN_EXECUTION);
            order.setUpdatedAt(new Date());
        }
    },
    IN_EXECUTION {
        @Override
        public void finish(ServiceOrder order) {
            order.setStatus(FINISHED);
            order.setFinishedAt(new Date());
        }
    },
    FINISHED {
        @Override
        public void deliver(ServiceOrder order) {
            order.setStatus(DELIVERED);
        }
    },
    DELIVERED,
    CANCELED;

    public static final String MSG_ERROR = "Não é possível mover para o status %s, a partir do status %s.";

    public void diagnose(ServiceOrder order) throws InvalidTransitionException {
        throw new InvalidTransitionException(String.format(MSG_ERROR, IN_DIAGNOSIS.name(), this.name()));
    }

    public void waitForApproval(ServiceOrder order) throws InvalidTransitionException {
        throw new InvalidTransitionException(String.format(MSG_ERROR, WAITING_FOR_APPROVAL.name(), this.name()));
    }

    public void approve(ServiceOrder order) throws InvalidTransitionException {
        throw new InvalidTransitionException(String.format(MSG_ERROR, APPROVED.name(), this.name()));
    }

    public void reject(ServiceOrder order) throws InvalidTransitionException {
        throw new InvalidTransitionException(String.format(MSG_ERROR, REJECTED.name(), this.name()));
    }

    public void waitForStock(ServiceOrder order) throws InvalidTransitionException {
        throw new InvalidTransitionException(String.format(MSG_ERROR, WAITING_ON_STOCK.name(), this.name()));
    }

    public void execute(ServiceOrder order) throws InvalidTransitionException {
        throw new InvalidTransitionException(String.format(MSG_ERROR, IN_EXECUTION.name(), this.name()));
    }

    public void finish(ServiceOrder order) throws InvalidTransitionException {
        throw new InvalidTransitionException(String.format(MSG_ERROR, FINISHED.name(), this.name()));
    }

    public void deliver(ServiceOrder order) throws InvalidTransitionException {
        throw new InvalidTransitionException(String.format(MSG_ERROR, DELIVERED.name(), this.name()));
    }

    public static List<Status> getStatusesForPendingOrders() {
        return Arrays.asList(Status.OPENED, Status.IN_DIAGNOSIS, Status.APPROVED, Status.IN_EXECUTION);
    }
}
