package com.legent.pojos;

import com.google.common.base.Objects;

abstract public class AbsKeyPojo<ID> extends AbsPojo implements IKeyPojo<ID> {

    @Override
    public boolean equals(Object o) {
        if (o instanceof IKeyPojo) {
            IKeyPojo<?> pojo = (IKeyPojo<?>) o;
            return Objects.equal(getID(), pojo.getID());
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return getID().hashCode();
    }

    @Override
    public void setName(String name) {

    }
}
