// **********************************************************************
//
// Copyright (c) 2003-2018 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

namespace Ice
{
    namespace facets
    {
        public sealed class AI : Test.ADisp_
        {
            public AI()
            {
            }

            public override string callA(Ice.Current current)
            {
                return "A";
            }
        }
    }
}
