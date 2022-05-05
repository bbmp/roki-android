package com.legent.io.protocols;

import com.legent.pojos.AbsObject;

abstract public class AbsProtocol extends AbsObject implements IProtocol {

	public AbsProtocol() {
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

}
