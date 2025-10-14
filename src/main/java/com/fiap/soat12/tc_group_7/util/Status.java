package com.fiap.soat12.tc_group_7.util;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ServiceOrder;
import com.fiap.soat12.tc_group_7.exception.InvalidTransitionException;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public enum Status {

    OPENED(6) {
        @Override
        public void diagnose(ServiceOrder order) {
            order.setStatus(IN_DIAGNOSIS);
            order.setUpdatedAt(new Date());
        }
    },
    IN_DIAGNOSIS(4) {
        @Override
        public void waitForApproval(ServiceOrder order) {
            order.setStatus(WAITING_FOR_APPROVAL);
            order.setUpdatedAt(new Date());
        }
    },
    WAITING_FOR_APPROVAL(3) {
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
    APPROVED(5) {
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
    REJECTED(8) {
        @Override
        public void finish(ServiceOrder order) {
            order.setStatus(FINISHED);
            order.setFinishedAt(new Date());
        }
    },
    WAITING_ON_STOCK(2) {
        @Override
        public void execute(ServiceOrder order) {
            order.setStatus(IN_EXECUTION);
            order.setUpdatedAt(new Date());
        }
    },
    IN_EXECUTION(1) {
        @Override
        public void finish(ServiceOrder order) {
            order.setStatus(FINISHED);
            order.setFinishedAt(new Date());
        }
    },
    FINISHED(9) {
        @Override
        public void deliver(ServiceOrder order) {
            order.setStatus(DELIVERED);
        }
    },
    DELIVERED(10),
    CANCELED(7);

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

    private final int sortOrder;

    Status(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    Status() {
        this(Integer.MAX_VALUE);
    }

    public int getSortOrder() {
        return sortOrder;
    }
}
