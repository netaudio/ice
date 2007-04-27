// **********************************************************************
//
// Copyright (c) 2003-2007 ZeroC, Inc. All rights reserved.
//
// This copy of Ice-E is licensed to you under the terms described in the
// ICEE_LICENSE file included in this distribution.
//
// **********************************************************************

package IceInternal;

public class FixedReference extends Reference
{
    public
    FixedReference(Instance inst,
		   Ice.Communicator com,
    		   Ice.Identity ident,
		   String fs,
		   int md,
		   Ice.Connection[] fixedConns)
    {
    	super(inst, com, ident, fs, md, false);
        _fixedConnections = fixedConns;
    }

    public Endpoint[]
    getEndpoints()
    {
        return new Endpoint[0];
    }

    public Reference
    changeRouter(Ice.RouterPrx newRouter)
    {
        return this;
    }

    public Reference
    changeLocator(Ice.LocatorPrx newLocator)
    {
        return this;
    }

    public Reference
    changeTimeout(int newTimeout)
    {
	return this;
    }

    public void
    streamWrite(BasicStream s)
	throws Ice.MarshalException
    {
	 throw new Ice.MarshalException("Cannot marshal a fixed proxy");
    }

    public String
    toString()
	throws Ice.MarshalException
    {
	 throw new Ice.MarshalException("Cannot marshal a fixed proxy");
    }

    public Ice.Connection
    getConnection()
    {
	//
	// If a reference is secure or the mode is datagram or batch
	// datagram then we throw a NoEndpointException since IceE lacks
	// this support.
	//
	if(getSecure() || getMode() == ModeDatagram || getMode() == ModeBatchDatagram || _fixedConnections.length == 0)
	{
	    if(_fixedConnections.length == 0)
	    {
		Ice.NoEndpointException ex = new Ice.NoEndpointException();
		ex.proxy = ""; // No stringified representation for fixed proxies.
		throw ex;
	    }

	    Ice.FeatureNotSupportedException ex = new Ice.FeatureNotSupportedException();
	    if(getSecure())
	    {
		ex.unsupportedFeature = "ssl";
	    }
	    else if(getMode() == ModeDatagram)
	    {
		ex.unsupportedFeature = "datagram";
	    }
	    else if(getMode() == ModeBatchDatagram)
	    {
		ex.unsupportedFeature = "batch datagram";
	    }
	    throw ex;
	}

	//
	// Choose a random connection
	//
	Ice.Connection connection = _fixedConnections[Math.abs(_rand.nextInt() % _fixedConnections.length)];
	if(IceUtil.Debug.ASSERT)
	{
	    IceUtil.Debug.Assert(connection != null);
	}
	connection.throwException();

	return connection;
    }

    public boolean
    equals(java.lang.Object obj)
    {
        if(this == obj)
	{
	    return true;
	}
	if(!(obj instanceof FixedReference))
	{
	    return false;
	}
        FixedReference rhs = (FixedReference)obj;
        if(!super.equals(rhs))
        {
            return false;
        }
	
	return IceUtil.Arrays.equals(_fixedConnections, rhs._fixedConnections);
    }

    protected
    FixedReference()
    {
    }

    protected void
    shallowCopy(FixedReference ref)
    {
	super.shallowCopy(ref);
	ref._fixedConnections = _fixedConnections;
	ref._rand = _rand;
    }

    public java.lang.Object
    ice_clone()
    {
	FixedReference result = new FixedReference();
        shallowCopy(result);
	return result;
    }

    private Ice.Connection _fixedConnections[];
    private java.util.Random _rand = new java.util.Random();
}
