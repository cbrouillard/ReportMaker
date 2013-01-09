package com.headbangers.reportmaker.exception;

// Exception à balancer pour les rapports utilisateurs
public class TwrException extends RuntimeException {

	private static final long serialVersionUID = 123700061466762280L;

	public TwrException(Throwable ex) {
		this.initCause(ex);
	}

}
