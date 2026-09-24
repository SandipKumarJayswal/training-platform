package com.example.common.api;

public interface DomainEventHandler<T extends DomainEvent> {
	 Class<T> eventType();
	 void handle(T event);

}
