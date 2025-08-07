package com.fiap.soat12.tc_group_7.util;

import com.fiap.soat12.tc_group_7.entity.ServiceOrder;
import com.fiap.soat12.tc_group_7.exception.InvalidTransitionException;

import java.util.Date;

public enum Status {

    OPENED {
        @Override
        public void diagnose(ServiceOrder order) {
            order.setStatus(IN_DIAGNOSIS);
        }
    },
    IN_DIAGNOSIS {
        @Override
        public void waitForApproval(ServiceOrder order) {
            order.setStatus(WAITING_FOR_APPROVAL);
        }
    },
    WAITING_FOR_APPROVAL {
        @Override
        public void approve(ServiceOrder order) {
            order.setStatus(APPROVED);
        }

        @Override
        public void reject(ServiceOrder order) {
            order.setStatus(REJECTED);
        }
    },
    APPROVED {
        @Override
        public void waitForStock(ServiceOrder order) {
            order.setStatus(WAITING_ON_STOCK);
        }

        @Override
        public void execute(ServiceOrder order) {
            order.setStatus(IN_EXECUTION);
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

    public void diagnose(ServiceOrder order) throws InvalidTransitionException {
        throw new InvalidTransitionException("Cannot start diagnosis from state " + this.name());
    }

    public void waitForApproval(ServiceOrder order) throws InvalidTransitionException {
        throw new InvalidTransitionException("Cannot wait for approval from state " + this.name());
    }

    public void approve(ServiceOrder order) throws InvalidTransitionException {
        throw new InvalidTransitionException("Cannot approve from state " + this.name());
    }

    public void reject(ServiceOrder order) throws InvalidTransitionException {
        throw new InvalidTransitionException("Cannot reject from state " + this.name());
    }

    public void waitForStock(ServiceOrder order) throws InvalidTransitionException {
        throw new InvalidTransitionException("Cannot wait for stock from state " + this.name());
    }

    public void execute(ServiceOrder order) throws InvalidTransitionException {
        throw new InvalidTransitionException("Cannot start execution from state " + this.name());
    }

    public void finish(ServiceOrder order) throws InvalidTransitionException {
        throw new InvalidTransitionException("Cannot finish from state " + this.name());
    }

    public void deliver(ServiceOrder order) throws InvalidTransitionException {
        throw new InvalidTransitionException("Cannot deliver from state " + this.name());
    }
}
