package com.example.common.core;

import com.example.common.api.DomainEvent;
import com.example.common.api.DomainEventHandler;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DomainEventDispatcher {

    private final List<DomainEventHandler<?>> handlers;

    public DomainEventDispatcher(List<DomainEventHandler<?>> handlers) {
        this.handlers = handlers;
    }

    @EventListener
    @SuppressWarnings("unchecked")
    public void onDomainEvent(DomainEvent event) {
        for (DomainEventHandler<?> handler : handlers) {
            if (handler.eventType().isInstance(event)) {
                ((DomainEventHandler<DomainEvent>) handler).handle(event);
            }
        }
    }
}