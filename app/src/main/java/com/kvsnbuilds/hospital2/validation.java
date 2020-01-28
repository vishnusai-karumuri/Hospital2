package com.kvsnbuilds.hospital2;

public class validation
{
    int evalidation(String m)
    {
        int ret = 0;
        String emailpattern = "[a-zA-Z][a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (m.isEmpty())
        {
            ret = 0;
        }
        else if(!m.matches(emailpattern))
        {
            ret = 1;
        }
        else if(m.matches(emailpattern))
        {
            ret = 2;
        }
        return ret;
    }

    int pvalidation(String p)
    {
        int ret = 0;
        String ppattern = "((?=.*\\d)(?=.*[a-z])(?=.*[@#$%]).{6,20})";
        if(p.isEmpty())
        {
            ret = 0;
        }
        else if(!p.matches(ppattern))
        {
            ret = 1;
        }
        else if(p.matches(ppattern))
        {
            ret = 2;
        }
        return ret;
    }
}
