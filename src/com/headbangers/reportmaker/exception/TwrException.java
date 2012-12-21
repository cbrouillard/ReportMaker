package com.headbangers.reportmaker.exception;

// Exception à balancer pour les rapports utilisateurs
public class TwrException extends RuntimeException {

	public TwrException(Throwable ex) {
		this.initCause(ex);
	}

}
