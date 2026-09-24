package com.example.common.api;

public interface DomainEventPublisher {
	 void publish(DomainEvent event);
}
