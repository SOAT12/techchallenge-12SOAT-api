package com.fiap.soat12.tc_group_7.cleanarch.util;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ServiceOrder;
import com.fiap.soat12.tc_group_7.cleanarch.exception.InvalidTransitionException;
import lombok.Getter;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Getter
public enum Status {

    OPENED("Aberta", 6) {
        @Override
        public void diagnose(ServiceOrder order) {
            order.setStatus(IN_DIAGNOSIS);
            order.setUpdatedAt(new Date());
        }
    },
    IN_DIAGNOSIS("Em diagnóstico", 4) {
        @Override
        public void waitForApproval(ServiceOrder order) {
            order.setStatus(WAITING_FOR_APPROVAL);
            order.setUpdatedAt(new Date());
        }
    },
    WAITING_FOR_APPROVAL("Aguardando Aprovação", 2) {
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
    APPROVED("Aprovada", 5) {
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
    REJECTED("Rejeitada", 8) {
        @Override
        public void finish(ServiceOrder order) {
            order.setStatus(FINISHED);
            order.setFinishedAt(new Date());
        }
    },
    WAITING_ON_STOCK("Aguardando Estoque", 3) {
        @Override
        public void execute(ServiceOrder order) {
            order.setStatus(IN_EXECUTION);
            order.setUpdatedAt(new Date());
        }
    },
    IN_EXECUTION("Em Execução", 1) {
        @Override
        public void finish(ServiceOrder order) {
            order.setStatus(FINISHED);
            order.setFinishedAt(new Date());
        }
    },
    FINISHED("Finalizada", 9) {
        @Override
        public void deliver(ServiceOrder order) {
            order.setStatus(DELIVERED);
        }
    },
    DELIVERED("Entregue", 10),
    CANCELED("Cancelada", 7);

    Status(String label, int sortOrder) {
        this.label = label;
        this.sortOrder = sortOrder;
    }

    private final String label;
    private final int sortOrder;

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
